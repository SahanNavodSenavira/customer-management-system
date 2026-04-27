import API from './api';

// Get all customers (paginated)
export const getAllCustomers = (page = 0, size = 20) => {
  return API.get(`/customers?page=${page}&size=${size}`);
};

// Get single customer by ID
export const getCustomerById = (id) => {
  return API.get(`/customers/${id}`);
};

// Create new customer
export const createCustomer = (customerData) => {
  return API.post('/customers', customerData);
};

// Update existing customer
export const updateCustomer = (id, customerData) => {
  return API.put(`/customers/${id}`, customerData);
};