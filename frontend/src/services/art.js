export const getArtworks = async () => {
  try {
    const response = await fetch('/api/art');
    if (!response.ok) {
      console.error(`Fetch failed with status: ${response.status}`);
      throw new Error('Failed to fetch artworks');
    }
    return await response.json();
  } catch (error) {
    console.error('Error in getArtworks:', error);
    return [];
  }
};

export const getArtworkById = async (id) => {
  try {
    const response = await fetch(`/api/art/${id}`);
    if (!response.ok) throw new Error('Failed to fetch artwork');
    return await response.json();
  } catch (error) {
    console.error('Error in getArtworkById:', error);
    return null;
  }
};

export const uploadArtworkPhoto = async (artworkId, file) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/art/${artworkId}/photo`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': file.type,
      'X-File-Name': file.name
    },
    body: file
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to upload photo');
  }
  return await response.text();
};

export const deleteArtworkPhoto = async (artworkId, imageId) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/art/${artworkId}/photo/${imageId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to delete photo');
  }
  return true;
};

export const updateArtwork = async (artworkId, data) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/art/${artworkId}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to update artwork');
  }
  return await response.json();
};

export const createArtwork = async (data) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch('/api/art', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to create artwork');
  }
  return await response.json();
};

export const deleteArtwork = async (artworkId) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/art/${artworkId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to delete artwork');
  }
  return true;
};

export const exportArtworks = async (format) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch('/api/export', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ format })
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to export artworks');
  }
  return await response.blob();
};

export const getImageUrl = (path) => {
  if (!path) return 'https://via.placeholder.com/300x400?text=No+Image';
  if (path.startsWith('http') || path.startsWith('/media')) return path;

  const cleanPath = `/media/art/${path}`;
  return cleanPath;
};
