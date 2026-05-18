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

/**
 * Checks if the user has a specific role.
 * For Museum Staff (MANAGER, EMPLOYEE), a hierarchy applies.
 * For ADMIN, it is an isolated role with no access to staff features.
 */
export const hasRole = (requiredRole) => {
  const user = getUser();
  if (!user) return false;

  const userRole = (user.role || '').replace('ROLE_', '').toUpperCase();
  const targetRole = requiredRole.replace('ROLE_', '').toUpperCase();

  // Admin is isolated: it only matches itself.
  if (targetRole === 'ADMIN') {
    return userRole === 'ADMIN';
  }

  // Manager/Employee hierarchy (Admin is NOT part of this hierarchy)
  const STAFF_LEVELS = {
    'MANAGER': 2,
    'EMPLOYEE': 1,
    'USER': 0
  };

  const userLevel = STAFF_LEVELS[userRole] || 0;
  const targetLevel = STAFF_LEVELS[targetRole] || 0;

  return userLevel >= targetLevel;
};

/**
 * Staff includes MANAGER and EMPLOYEE. 
 * ADMIN is NOT staff (they are user management only).
 */
export const isStaff = () => {
  const user = getUser();
  if (!user) return false;
  const role = user.role.toUpperCase();
  return role === 'MANAGER' || role === 'EMPLOYEE';
};

export const isAuthenticated = () => {
  const token = getToken();
  return !!token;
};
