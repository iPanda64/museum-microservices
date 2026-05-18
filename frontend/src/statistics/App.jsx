import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../context/AuthContext';
import { hasRole } from '../utils/auth';
import BarChart from '../components/BarChart/BarChart';
import PieChart from '../components/PieChart/PieChart';
import GenericButton from '../components/GenericButton/GenericButton';
import { getStatisticsTypes, getStatisticsArtists, getStatisticsDensity } from '../services/statistics';
import styles from './Statistics.module.css';

function StatisticsContent() {
  const { loading: authLoading } = useAuth();
  const [loading, setLoading] = useState(true);
  const [typesData, setTypesData] = useState([]);
  const [artistsData, setArtistsData] = useState([]);
  const [densityData, setDensityData] = useState([]);
  const [summary, setSummary] = useState({ totalArt: 0, totalArtists: 0 });

  useEffect(() => {
    if (!hasRole('MANAGER')) return;

    const fetchAllStats = async () => {
      setLoading(true);
      const [types, artists, density] = await Promise.all([
        getStatisticsTypes(),
        getStatisticsArtists(),
        getStatisticsDensity()
      ]);

      // Map object { "Key": Value } to array [{ label: "Key", value: Value }]
      const mappedTypes = Object.entries(types).map(([label, value]) => ({ label, value }));
      const mappedArtists = Object.entries(artists).map(([label, value]) => ({ label, value }));
      const mappedDensity = Object.entries(density).map(([label, value]) => ({
        label: `${label} Photo${parseInt(label) !== 1 ? 's' : ''}`,
        value
      }));

      setTypesData(mappedTypes);
      setArtistsData(mappedArtists);
      setDensityData(mappedDensity);

      // Calculate totals
      const totalArt = Object.values(types).reduce((a, b) => a + b, 0);
      const totalArtists = Object.keys(artists).length;
      setSummary({ totalArt, totalArtists });

      setLoading(false);
    };

    fetchAllStats();
  }, []);

  if (authLoading || (hasRole('MANAGER') && loading)) {
    return <div className={styles.loader}>Analyzing museum data...</div>;
  }

  if (!hasRole('MANAGER')) {
    return (
      <div className={styles.container}>
        <Navbar />
        <div className={styles.accessDenied}>
          <h1>Access Denied</h1>
          <p>You do not have the necessary permissions to view statistics.</p>
          <GenericButton onClick={() => window.location.href = '/'}>Return Home</GenericButton>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <Navbar />
      <main className={styles.main}>
        <div className={styles.hero}>
          <h1>Museum Insights</h1>
          <p>Live analytics of our collections and master artists.</p>
        </div>

        <div className={styles.grid}>
          <BarChart
            title="Artworks by Category"
            data={typesData}
          />
          <PieChart
            title="Artist Contributions"
            data={artistsData}
          />
        </div>

        <div className={styles.grid}>
          <BarChart
            title="Photo Density (Photos per Artwork)"
            data={densityData}
          />
          <div className={styles.summaryGridSmall}>
            <div className={styles.statBox}>
              <span className={styles.statValue}>{summary.totalArt}</span>
              <span className={styles.statLabel}>Total Artworks</span>
            </div>
            <div className={styles.statBox}>
              <span className={styles.statValue}>{summary.totalArtists}</span>
              <span className={styles.statLabel}>Active Artists</span>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <StatisticsContent />
    </AuthProvider>
  );
}
