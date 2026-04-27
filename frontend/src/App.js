import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import CustomerListPage from './pages/CustomerListPage';
import CustomerFormPage from './pages/CustomerFormPage';
import CustomerDetailPage from './pages/CustomerDetailPage';
import BulkUpload from './components/BulkUpload'; 
import './App.css';

function App() {
  return (
    <BrowserRouter>
      {/* Navigation Bar */}
      <nav style={styles.navbar}>
        <div style={styles.navBrand}>
          Customer Management System
        </div>
        <div style={styles.navLinks}>
          <Link to="/" style={styles.navLink}>
            Customers
          </Link>
          <Link to="/customers/new" style={styles.navLink}>
            Add Customer
          </Link>
          <Link to="/bulk-upload" style={styles.navLink}>
            Bulk Upload
          </Link>
        </div>
      </nav>

      {/* Page Content */}
      <div style={styles.container}>
        <Routes>
          <Route path="/" element={<CustomerListPage />} />
          <Route path="/customers/new" 
                 element={<CustomerFormPage />} />
          <Route path="/customers/:id/edit" 
                 element={<CustomerFormPage />} />
          <Route path="/customers/:id" 
                 element={<CustomerDetailPage />} />
          <Route path="/bulk-upload" element={<BulkUpload />} />
        </Routes>

      </div>
    </BrowserRouter>
  );
}

// Simple styles
const styles = {
  navbar: {
    backgroundColor: '#2c3e50',
    padding: '15px 30px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  navBrand: {
    color: 'white',
    fontSize: '20px',
    fontWeight: 'bold',
  },
  navLinks: {
    display: 'flex',
    gap: '20px',
  },
  navLink: {
    color: 'white',
    textDecoration: 'none',
    fontSize: '16px',
  },
  container: {
    padding: '30px',
    maxWidth: '1200px',
    margin: '0 auto',
  },
};

export default App;