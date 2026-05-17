import React from 'react';
import styles from './SearchBar.module.css';

const SearchBar = ({ 
  searchValue, 
  onSearchChange, 
  placeholder = "Search...",
  sortValue,
  onSortChange,
  sortLabel = "Sort",
  showSort = false,
  actionElement,
  artistOptions = [], // New
  selectedArtist = "", // New
  onArtistChange, // New
  typeOptions = [], // New
  selectedType = "",
  onTypeChange,
  secondaryActionElement // New prop
  }) => {
  return (
    <div className={styles.controls}>
      <div className={styles.topRow}>
        <div className={styles.searchBox}>
          <input 
            type="text" 
            placeholder={placeholder}
            value={searchValue}
            onChange={(e) => onSearchChange(e.target.value)}
            className={styles.searchInput}
          />
        </div>
        {actionElement && <div className={styles.action}>{actionElement}</div>}
      </div>

      <div className={styles.filterRow}>
        <div className={styles.filtersLeft}>
          {artistOptions.length > 0 && (
            <select 
              className={styles.select} 
              value={selectedArtist} 
              onChange={(e) => onArtistChange(e.target.value)}
            >
              <option value="">All Artists</option>
              {artistOptions.map(opt => (
                <option key={opt.id} value={opt.id}>{opt.name}</option>
              ))}
            </select>
          )}

          {typeOptions.length > 0 && (
            <select 
              className={styles.select} 
              value={selectedType} 
              onChange={(e) => onTypeChange(e.target.value)}
            >
              <option value="">All Types</option>
              {typeOptions.map(opt => (
                <option key={opt} value={opt}>{opt}</option>
              ))}
            </select>
          )}

          {showSort && (
            <div className={styles.sortBox}>
              <label className={styles.checkboxLabel}>
                <input 
                  type="checkbox" 
                  checked={sortValue}
                  onChange={(e) => onSortChange(e.target.checked)}
                />
                {sortLabel}
              </label>
            </div>
          )}
        </div>

        {secondaryActionElement && (
          <div className={styles.secondaryAction}>
            {secondaryActionElement}
          </div>
        )}
      </div>
    </div>
  );
  };


export default SearchBar;
