import React from 'react';
import styles from './PieChart.module.css';

const PieChart = ({ data = [], title = "Distribution" }) => {
  const total = data.reduce((sum, item) => sum + item.value, 0);
  let cumulativePercent = 0;

  // Colors for slices - cycling through theme-friendly shades
  const colors = [
    '#3b82f6', // Action Primary
    '#10b981', // Green-ish
    '#f59e0b', // Yellow-ish
    '#8b5cf6', // Purple-ish
    '#ef4444', // Danger Red
  ];

  const getCoordinatesForPercent = (percent) => {
    const x = Math.cos(2 * Math.PI * percent);
    const y = Math.sin(2 * Math.PI * percent);
    return [x, y];
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <svg viewBox="-1.2 -1.2 2.4 2.4" className={styles.svg}>
          {data.map((item, index) => {
            const percent = item.value / total;
            const [startX, startY] = getCoordinatesForPercent(cumulativePercent);
            cumulativePercent += percent;
            const [endX, endY] = getCoordinatesForPercent(cumulativePercent);
            const largeArcFlag = percent > 0.5 ? 1 : 0;
            const pathData = [
              `M ${startX} ${startY}`,
              `A 1 1 0 ${largeArcFlag} 1 ${endX} ${endY}`,
              `L 0 0`,
            ].join(' ');

            return (
              <path 
                key={index} 
                d={pathData} 
                fill={colors[index % colors.length]} 
                className={styles.slice}
              >
                <title>{item.label}: {item.value}</title>
              </path>
            );
          })}
        </svg>
        
        <div className={styles.legend}>
          {data.map((item, index) => (
            <div key={index} className={styles.legendItem}>
              <span 
                className={styles.colorDot} 
                style={{ background: colors[index % colors.length] }}
              />
              <span className={styles.label}>{item.label}</span>
              <span className={styles.value}>{item.value}</span>
            </div>
          ))}
        </div>
      </div>
      <h3 className={styles.title}>{title}</h3>
    </div>
  );
};

export default PieChart;
