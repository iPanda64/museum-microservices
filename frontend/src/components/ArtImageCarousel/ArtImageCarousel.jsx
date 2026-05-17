import React from 'react';
import styles from './ArtImageCarousel.module.css';
import { getImageUrl } from '../../services/art';

const ArtImageCarousel = ({ images, title, currentIndex, setCurrentIndex }) => {
  const hasMultipleImages = images.length > 1;

  const nextImage = (e) => {
    e.stopPropagation();
    setCurrentIndex((prev) => (prev + 1) % images.length);
  };

  const prevImage = (e) => {
    e.stopPropagation();
    setCurrentIndex((prev) => (prev - 1 + images.length) % images.length);
  };

  return (
    <div className={styles.imageSection}>
      {hasMultipleImages && (
        <button className={`${styles.navBtn} ${styles.prev}`} onClick={prevImage}>&#10094;</button>
      )}
      
      {images.length > 0 ? (
        <img 
          src={getImageUrl(images[currentIndex]?.imagePath)} 
          alt={title} 
          className={styles.mainImage}
        />
      ) : (
        <div className={styles.placeholder}>No Image Available</div>
      )}
      
      {hasMultipleImages && (
        <button className={`${styles.navBtn} ${styles.next}`} onClick={nextImage}>&#10095;</button>
      )}

      {hasMultipleImages && (
        <div className={styles.dots}>
          {images.map((_, index) => (
            <span 
              key={index} 
              className={`${styles.dot} ${index === currentIndex ? styles.active : ''}`}
              onClick={() => setCurrentIndex(index)}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default ArtImageCarousel;
