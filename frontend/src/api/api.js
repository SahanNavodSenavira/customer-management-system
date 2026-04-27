import axios from 'axios';

// Base URL pointing to your Spring Boot backend
const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default API;