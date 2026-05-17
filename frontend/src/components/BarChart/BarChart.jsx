import React from 'react';
import styles from './BarChart.module.css';

const BarChart = ({ data = [], title = "Statistics" }) => {
  const maxValue = Math.max(...data.map(d => d.value), 1);
  const height = 200;
  const labelSpace = 40; // Pixels to reserve for values on top

  return (
    <div className={styles.container}>
      <div className={styles.chartArea}>
        {data.map((item, index) => {
          const barHeight = (item.value / maxValue) * (height - labelSpace);
          return (
            <div key={index} className={styles.barWrapper}>
              <div className={styles.barValue}>{item.value}</div>
              <div 
                className={styles.bar} 
                style={{ height: `${barHeight}px` }}
              >
                <div className={styles.tooltip}>{item.label}: {item.value}</div>
              </div>
              <div className={styles.label}>{item.label}</div>
            </div>
          );
        })}
      </div>
      <h3 className={styles.title}>{title}</h3>
    </div>
  );
};

export default BarChart;
