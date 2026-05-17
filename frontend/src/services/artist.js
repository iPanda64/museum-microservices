import { getArtworks, deleteArtwork } from './art';

export const getArtists = async () => {
  try {
    const response = await fetch('/api/artists');
    if (!response.ok) throw new Error('Failed to fetch artists');
    return await response.json();
  } catch (error) {
    console.error('Error in getArtists:', error);
    return [];
  }
};

export const getArtistById = async (id) => {
  try {
    const response = await fetch(`/api/artists/${id}`);
    if (!response.ok) throw new Error('Failed to fetch artist');
    return await response.json();
  } catch (error) {
    console.error('Error in getArtistById:', error);
    return null;
  }
};

export const createArtist = async (data) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch('/api/artists', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to create artist');
  }
  return await response.json();
};

export const updateArtist = async (artistId, data) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/artists/${artistId}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to update artist');
  }
  return await response.json();
};

export const deleteArtist = async (artistId) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/artists/${artistId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || 'Failed to delete artist');
  }
  return true;
};

export const deleteArtistWithArtworks = async (artistId) => {
  const artworks = await getArtworks();
  const artistArtworks = artworks.filter(art => art.artistId === artistId);
  
  for (const art of artistArtworks) {
    await deleteArtwork(art.artworkId);
  }
  
  await deleteArtist(artistId);
  return true;
};

export const uploadArtistPhoto = async (artistId, file) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/artists/${artistId}/photo`, {
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

export const deleteArtistPhoto = async (artistId) => {
  const token = sessionStorage.getItem('museum_token');
  const response = await fetch(`/api/artists/${artistId}/photo`, {
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

export const getArtistImageUrl = (path) => {
  if (!path) return 'https://via.placeholder.com/300x300?text=No+Photo';
  if (path.startsWith('http') || path.startsWith('/media')) return path;
  return `/media/artist/${path}`;
};
