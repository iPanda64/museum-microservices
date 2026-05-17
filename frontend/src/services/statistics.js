export const getStatisticsTypes = async () => {
  const token = sessionStorage.getItem('museum_token');
  try {
    const response = await fetch('/api/statistics/types', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch type statistics');
    return await response.json();
  } catch (error) {
    console.error('Error fetching stats/types:', error);
    return {};
  }
};

export const getStatisticsArtists = async () => {
  const token = sessionStorage.getItem('museum_token');
  try {
    const response = await fetch('/api/statistics/artists', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch artist statistics');
    return await response.json();
  } catch (error) {
    console.error('Error fetching stats/artists:', error);
    return {};
  }
};

export const getStatisticsDensity = async () => {
  const token = sessionStorage.getItem('museum_token');
  try {
    const response = await fetch('/api/statistics/density', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Failed to fetch density statistics');
    return await response.json();
  } catch (error) {
    console.error('Error fetching stats/density:', error);
    return {};
  }
};
