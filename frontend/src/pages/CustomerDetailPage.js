import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getCustomerById } from '../api/customerApi';

function CustomerDetailPage() {
  const { id } = useParams();
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadCustomer();
  }, [id]);

  const loadCustomer = async () => {
    try {
      setLoading(true);
      const response = await getCustomerById(id);
      if (response.data.success) {
        setCustomer(response.data.data);
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError('Failed to load customer details');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="card">Loading...</div>;
  if (error) return <div className="alert-error">{error}</div>;
  if (!customer) return <div className="alert-error">Customer not found</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1 className="page-title">Customer Details</h1>
        <Link to={`/customers/${id}/edit`} className="btn btn-success">Edit Customer</Link>
      </div>

      <div className="card">
        <h3>Basic Information</h3>
        <p><strong>ID:</strong> {customer.id}</p>
        <p><strong>Name:</strong> {customer.name}</p>
        <p><strong>Date of Birth:</strong> {customer.dateOfBirth}</p>
        <p><strong>NIC Number:</strong> {customer.nicNumber}</p>
        <p><strong>Created At:</strong> {new Date(customer.createdAt).toLocaleString()}</p>
        <p><strong>Updated At:</strong> {new Date(customer.updatedAt).toLocaleString()}</p>
      </div>

      {/* Mobile Numbers */}
      {customer.mobileNumbers && customer.mobileNumbers.length > 0 && (
        <div className="card">
          <h3>Mobile Numbers</h3>
          <ul>
            {customer.mobileNumbers.map(mobile => (
              <li key={mobile.id}>
                {mobile.mobileNumber} {mobile.isPrimary && <strong>(Primary)</strong>}
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Addresses */}
      {customer.addresses && customer.addresses.length > 0 && (
        <div className="card">
          <h3>Addresses</h3>
          {customer.addresses.map(address => (
            <div key={address.id} style={{ marginBottom: '15px', padding: '10px', background: '#f9f9f9', borderRadius: '4px' }}>
              <p><strong>Address:</strong> {address.addressLine1}</p>
              {address.addressLine2 && <p><strong>Address Line 2:</strong> {address.addressLine2}</p>}
              <p><strong>City:</strong> {address.cityName}, {address.countryName}</p>
              <p><strong>Primary:</strong> {address.isPrimary ? 'Yes' : 'No'}</p>
            </div>
          ))}
        </div>
      )}

      {/* Family Members */}
      {customer.familyMembers && customer.familyMembers.length > 0 && (
        <div className="card">
          <h3>Family Members</h3>
          <ul>
            {customer.familyMembers.map(family => (
              <li key={family.familyMemberId}>
                {family.familyMemberName} ({family.familyMemberNic}) - {family.relationshipType}
              </li>
            ))}
          </ul>
        </div>
      )}

      <Link to="/" className="btn btn-secondary">Back to List</Link>
    </div>
  );
}

export default CustomerDetailPage;