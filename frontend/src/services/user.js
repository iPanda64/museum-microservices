import { getToken } from '../utils/auth';

/**
 * Fetches the Telegram linking URL for a specific user.
 * @param {string|number} userId - The ID of the user.
 * @returns {Promise<string|null>} - The Telegram linking URL.
 */
export const getTelegramLink = async (userId) => {
  const token = getToken();
  try {
    const response = await fetch(`/api/credentials/telegram/link/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      return data.url;
    } else {
      console.error('Failed to fetch Telegram link:', response.statusText);
      return null;
    }
  } catch (err) {
    console.error('Error fetching Telegram link:', err);
    return null;
  }
};
