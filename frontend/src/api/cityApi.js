import API from './api';

// Get all cities for dropdown
export const getAllCities = () => {
  return API.get('/cities');
};