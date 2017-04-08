export const API_URL = 'http://localhost:8080';

export const PATHS = {
  location: (lat, long) => API_URL + '/location?lat=' + lat + '&long=' + long,
  rankUsers: () => API_URL + '/users/rank',
};
