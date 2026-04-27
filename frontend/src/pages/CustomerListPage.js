import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllCustomers } from '../api/customerApi';

function CustomerListPage() {
  const navigate = useNavigate();

  // State variables
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const size = 10;

  // Load customers when page changes
  useEffect(() => {
    loadCustomers();
  }, [page]);

  // Fetch customers from API
  const loadCustomers = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await getAllCustomers(page, size);
      setCustomers(response.data.data.content);
      setTotalPages(response.data.data.totalPages);
    } catch (err) {
      setError('Failed to load customers');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Page Header */}
      <div style={styles.header}>
  <h1 className="page-title">Customers</h1>
  <div style={styles.buttonGroup}>
    <button
      className="btn btn-primary"
      onClick={() => navigate('/customers/new')}>
      + Add Customer
    </button>
    <button
      className="btn btn-primary"
      onClick={() => navigate('/bulk-upload')}>
      📤 Bulk Upload
    </button>
  </div>
   </div>

      {/* Error Message */}
      {error && (
        <div className="alert-error">{error}</div>
      )}

      {/* Loading */}
      {loading ? (
        <div style={styles.loading}>Loading...</div>
      ) : (

        <>
          {/* Customer Table */}
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>NIC Number</th>
                <th>Date of Birth</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {customers.length === 0 ? (
                <tr>
                  <td colSpan="4" 
                      style={styles.noData}>
                    No customers found
                  </td>
                </tr>
              ) : (
                customers.map(customer => (
                  <tr key={customer.id}>
                    <td>{customer.name}</td>
                    <td>{customer.nicNumber}</td>
                    <td>{customer.dateOfBirth}</td>
                    <td>
                      {/* View Button */}
                      <button
                        className="btn btn-primary"
                        onClick={() => navigate(
                          `/customers/${customer.id}`)}>
                        View
                      </button>
                      {/* Edit Button */}
                      <button
                        className="btn btn-success"
                        onClick={() => navigate(
                          `/customers/${customer.id}/edit`)}>
                        Edit
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>

          {/* Pagination */}
          <div style={styles.pagination}>
            <button
              className="btn btn-secondary"
              onClick={() => setPage(page - 1)}
              disabled={page === 0}>
              Previous
            </button>

            <span style={styles.pageInfo}>
              Page {page + 1} of {totalPages}
            </span>

            <button
              className="btn btn-secondary"
              onClick={() => setPage(page + 1)}
              disabled={page >= totalPages - 1}>
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}

const styles = {
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '20px',
  },
  buttonGroup: {
    display: 'flex',
    gap: '10px',  
  },
  loading: {
    textAlign: 'center',
    padding: '50px',
    fontSize: '18px',
    color: '#666',
  },
  noData: {
    textAlign: 'center',
    padding: '30px',
    color: '#666',
  },
  pagination: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    gap: '15px',
    marginTop: '20px',
  },
  pageInfo: {
    fontSize: '14px',
    color: '#666',
  },
};

export default CustomerListPage;