export const API_URL = 'http://localhost:8080';

export const PATHS = {
  location: (name, lat, long) => API_URL + '/location/name?name=' + name + '&lat=' + lat + '&long=' + long,
  amenities: (lat, long) => API_URL + '/amenity/near?lat=' + lat + '&long=' + long,
  rankUsers: () => API_URL + '/users/rank',
};
