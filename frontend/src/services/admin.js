import { getToken } from '../utils/auth';

/**
 * Helper to handle API responses and extract error messages.
 */
const handleResponse = async (response) => {
  if (response.ok) {
    if (response.status === 204) return true;
    return await response.json();
  }
  
  const errorText = await response.text();
  throw new Error(errorText || `Request failed with status ${response.status}`);
};

export const getAllCredentials = async () => {
  const token = getToken();
  try {
    const response = await fetch('/api/credentials', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return response.ok ? await response.json() : [];
  } catch (err) {
    console.error('Error fetching credentials:', err);
    return [];
  }
};

export const getAllProfiles = async () => {
  const token = getToken();
  try {
    const response = await fetch('/api/users', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return response.ok ? await response.json() : [];
  } catch (err) {
    console.error('Error fetching profiles:', err);
    return [];
  }
};

export const getTelegramStatus = async (userId) => {
  const token = getToken();
  try {
    const response = await fetch(`/api/credentials/telegram/${userId}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
      const data = await response.json();
      return !!data.linked;
    }
    return false;
  } catch (err) {
    return false;
  }
};

export const unlinkTelegram = async (userId) => {
  const token = getToken();
  const response = await fetch(`/api/credentials/telegram/${userId}`, {
    method: 'DELETE',
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return await handleResponse(response);
};

export const createCredentials = async (credentials) => {
  const token = getToken();
  const response = await fetch('/api/credentials', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(credentials)
  });
  return await handleResponse(response);
};

export const createProfile = async (userId, profile) => {
  const token = getToken();
  const response = await fetch(`/api/users/${userId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(profile)
  });
  return await handleResponse(response);
};

/**
 * Updates user credentials.
 * @param {string|number} userId 
 * @param {Object} credentials 
 * @returns {Promise<Object>}
 */
export const updateCredentials = async (userId, credentials) => {
  const token = getToken();
  const response = await fetch(`/api/credentials/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(credentials)
  });
  return await handleResponse(response);
};

/**
 * Updates user profile.
 * @param {string|number} userId 
 * @param {Object} profile 
 * @returns {Promise<Object>}
 */
export const updateProfile = async (userId, profile) => {
  const token = getToken();
  const response = await fetch(`/api/users/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(profile)
  });
  return await handleResponse(response);
};

/**
 * Deletes user credentials and profile.
 * @param {string|number} userId 
 * @returns {Promise<boolean>}
 */
export const deleteUserCompletely = async (userId) => {
  const token = getToken();
  
  // We'll attempt to delete both. In some cases, one might not exist.
  const [credsRes, profileRes] = await Promise.allSettled([
    fetch(`/api/credentials/${userId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    }),
    fetch(`/api/users/${userId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    })
  ]);

  const success = (credsRes.status === 'fulfilled' && credsRes.value.ok) || 
                  (profileRes.status === 'fulfilled' && profileRes.value.ok);
  
  if (!success) {
    throw new Error('Failed to delete user.');
  }
  
  return true;
};
