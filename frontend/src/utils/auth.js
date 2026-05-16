export const TOKEN_KEY = 'museum_token';

export const setToken = (token) => {
  sessionStorage.setItem(TOKEN_KEY, token);
  // Necessary for Nginx auth_request to see the token
  document.cookie = `jwt=${token}; path=/; SameSite=Strict`;
};

export const getToken = () => {
  return sessionStorage.getItem(TOKEN_KEY);
};

export const removeToken = () => {
  sessionStorage.removeItem(TOKEN_KEY);
  // Clean up Nginx auth cookie
  document.cookie = "jwt=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC; SameSite=Strict";
};

export const decodeToken = (token) => {
  if (!token) return null;
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error('Failed to decode token', e);
    return null;
  }
};

export const getUser = () => {
  const token = getToken();
  const decoded = decodeToken(token);
  if (!decoded) return null;

  // Backend uses 'sub' for userId and 'role' with 'ROLE_' prefix
  const rawRole = decoded.role || '';
  const normalizedRole = rawRole.replace('ROLE_', '');

  return {
    ...decoded,
    userId: decoded.sub,
    role: normalizedRole
  };
};

export const hasRole = (role) => {
  const user = getUser();
  if (!user) return false;

  const userRole = (user.role || '').replace('ROLE_', '').toUpperCase();
  const searchRole = role.replace('ROLE_', '').toUpperCase();

  return userRole === searchRole;
};

export const isAuthenticated = () => {
  const token = getToken();
  return !!token;
};
