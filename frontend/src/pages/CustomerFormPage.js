import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createCustomer, updateCustomer, getCustomerById } from '../api/customerApi';
import { getAllCities } from '../api/cityApi';

function CustomerFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditMode = !!id;

  // Form state
  const [formData, setFormData] = useState({
    name: '',
    dateOfBirth: '',
    nicNumber: '',
    mobileNumbers: [],
    addresses: [],
    familyMembers: []
  });

  // Temporary form state for nested objects
  const [newMobile, setNewMobile] = useState({ mobileNumber: '', isPrimary: false });
  const [newAddress, setNewAddress] = useState({
    addressLine1: '',
    addressLine2: '',
    cityId: '',
    isPrimary: false
  });
  const [newFamilyMember, setNewFamilyMember] = useState({
    familyMemberId: '',
    relationshipType: ''
  });

  const [cities, setCities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    loadCities();
    if (isEditMode) {
      loadCustomer();
    }
  }, [id]);

  const loadCities = async () => {
    try {
      const response = await getAllCities();
      if (response.data.success) {
        setCities(response.data.data);
      }
    } catch (err) {
      console.error('Failed to load cities', err);
    }
  };

  const loadCustomer = async () => {
    try {
      setLoading(true);
      const response = await getCustomerById(id);
      if (response.data.success) {
        setFormData(response.data.data);
      }
    } catch (err) {
      setError('Failed to load customer');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // Mobile number handlers
  const addMobile = () => {
    if (newMobile.mobileNumber) {
      setFormData({
        ...formData,
        mobileNumbers: [...formData.mobileNumbers, { ...newMobile, id: Date.now() }]
      });
      setNewMobile({ mobileNumber: '', isPrimary: false });
    }
  };

  const removeMobile = (index) => {
    const updated = [...formData.mobileNumbers];
    updated.splice(index, 1);
    setFormData({ ...formData, mobileNumbers: updated });
  };

  // Address handlers
  const addAddress = () => {
    if (newAddress.addressLine1 && newAddress.cityId) {
      setFormData({
        ...formData,
        addresses: [...formData.addresses, { ...newAddress, id: Date.now() }]
      });
      setNewAddress({
        addressLine1: '',
        addressLine2: '',
        cityId: '',
        isPrimary: false
      });
    }
  };

  const removeAddress = (index) => {
    const updated = [...formData.addresses];
    updated.splice(index, 1);
    setFormData({ ...formData, addresses: updated });
  };

  // Family member handlers
  const addFamilyMember = () => {
    if (newFamilyMember.familyMemberId && newFamilyMember.relationshipType) {
      setFormData({
        ...formData,
        familyMembers: [...formData.familyMembers, { ...newFamilyMember, id: Date.now() }]
      });
      setNewFamilyMember({ familyMemberId: '', relationshipType: '' });
    }
  };

  const removeFamilyMember = (index) => {
    const updated = [...formData.familyMembers];
    updated.splice(index, 1);
    setFormData({ ...formData, familyMembers: updated });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    // Validate required fields
    if (!formData.name || !formData.dateOfBirth || !formData.nicNumber) {
      setError('Please fill in all required fields');
      setLoading(false);
      return;
    }

    try {
      if (isEditMode) {
        await updateCustomer(id, formData);
        setSuccess('Customer updated successfully!');
        setTimeout(() => navigate('/'), 1500);
      } else {
        await createCustomer(formData);
        setSuccess('Customer created successfully!');
        setTimeout(() => navigate('/'), 1500);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Operation failed');
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEditMode) return <div className="card">Loading customer data...</div>;

  return (
    <div>
      <h1 className="page-title">{isEditMode ? 'Edit Customer' : 'Add New Customer'}</h1>
      
      {error && <div className="alert-error">{error}</div>}
      {success && <div className="alert-success">{success}</div>}

      <form onSubmit={handleSubmit}>
        {/* Basic Information */}
        <div className="card">
          <h3>Basic Information</h3>
          
          <div className="form-group">
            <label>Name *</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Date of Birth *</label>
            <input
              type="date"
              name="dateOfBirth"
              value={formData.dateOfBirth}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>NIC Number *</label>
            <input
              type="text"
              name="nicNumber"
              value={formData.nicNumber}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        {/* Mobile Numbers Section */}
        <div className="card">
          <h3>Mobile Numbers</h3>
          
          {formData.mobileNumbers.map((mobile, index) => (
            <div key={mobile.id} style={styles.listItem}>
              <span>{mobile.mobileNumber} {mobile.isPrimary && '(Primary)'}</span>
              <button type="button" onClick={() => removeMobile(index)} className="btn btn-danger">
                Remove
              </button>
            </div>
          ))}

          <div style={styles.addSection}>
            <input
              type="text"
              placeholder="Mobile Number"
              value={newMobile.mobileNumber}
              onChange={(e) => setNewMobile({ ...newMobile, mobileNumber: e.target.value })}
              style={styles.input}
            />
            <label style={styles.checkboxLabel}>
              <input
                type="checkbox"
                checked={newMobile.isPrimary}
                onChange={(e) => setNewMobile({ ...newMobile, isPrimary: e.target.checked })}
              />
              Primary
            </label>
            <button type="button" onClick={addMobile} className="btn btn-primary">
              Add Mobile
            </button>
          </div>
        </div>

        {/* Addresses Section */}
        <div className="card">
          <h3>Addresses</h3>
          
          {formData.addresses.map((address, index) => (
            <div key={address.id} style={styles.addressItem}>
              <div>
                <strong>{address.addressLine1}</strong>
                {address.addressLine2 && <div>{address.addressLine2}</div>}
                <div>City ID: {address.cityId}</div>
                {address.isPrimary && <div style={{ color: '#27ae60' }}>Primary Address</div>}
              </div>
              <button type="button" onClick={() => removeAddress(index)} className="btn btn-danger">
                Remove
              </button>
            </div>
          ))}

          <div style={styles.addSection}>
            <input
              type="text"
              placeholder="Address Line 1"
              value={newAddress.addressLine1}
              onChange={(e) => setNewAddress({ ...newAddress, addressLine1: e.target.value })}
              style={styles.input}
            />
            <input
              type="text"
              placeholder="Address Line 2 (Optional)"
              value={newAddress.addressLine2}
              onChange={(e) => setNewAddress({ ...newAddress, addressLine2: e.target.value })}
              style={styles.input}
            />
            <select
              value={newAddress.cityId}
              onChange={(e) => setNewAddress({ ...newAddress, cityId: e.target.value })}
              style={styles.input}
            >
              <option value="">Select City</option>
              {cities.map(city => (
                <option key={city.cityId} value={city.cityId}>
                  {city.cityName}, {city.countryName}
                </option>
              ))}
            </select>
            <label style={styles.checkboxLabel}>
              <input
                type="checkbox"
                checked={newAddress.isPrimary}
                onChange={(e) => setNewAddress({ ...newAddress, isPrimary: e.target.checked })}
              />
              Primary Address
            </label>
            <button type="button" onClick={addAddress} className="btn btn-primary">
              Add Address
            </button>
          </div>
        </div>

        {/* Family Members Section */}
        <div className="card">
          <h3>Family Members</h3>
          
          {formData.familyMembers.map((member, index) => (
            <div key={member.id} style={styles.listItem}>
              <span>Member ID: {member.familyMemberId} - {member.relationshipType}</span>
              <button type="button" onClick={() => removeFamilyMember(index)} className="btn btn-danger">
                Remove
              </button>
            </div>
          ))}

          <div style={styles.addSection}>
            <input
              type="number"
              placeholder="Family Member Customer ID"
              value={newFamilyMember.familyMemberId}
              onChange={(e) => setNewFamilyMember({ ...newFamilyMember, familyMemberId: e.target.value })}
              style={styles.input}
            />
            <input
              type="text"
              placeholder="Relationship Type (Spouse, Parent, Child, Sibling)"
              value={newFamilyMember.relationshipType}
              onChange={(e) => setNewFamilyMember({ ...newFamilyMember, relationshipType: e.target.value })}
              style={styles.input}
            />
            <button type="button" onClick={addFamilyMember} className="btn btn-primary">
              Add Family Member
            </button>
          </div>
        </div>

        {/* Form Actions */}
        <div style={styles.actions}>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Saving...' : (isEditMode ? 'Update Customer' : 'Create Customer')}
          </button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

const styles = {
  listItem: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '10px',
    marginBottom: '10px',
    backgroundColor: '#f9f9f9',
    borderRadius: '4px'
  },
  addressItem: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    padding: '10px',
    marginBottom: '10px',
    backgroundColor: '#f9f9f9',
    borderRadius: '4px'
  },
  addSection: {
    marginTop: '15px',
    padding: '15px',
    backgroundColor: '#f0f0f0',
    borderRadius: '4px'
  },
  input: {
    width: '100%',
    padding: '8px',
    marginBottom: '10px',
    border: '1px solid #ddd',
    borderRadius: '4px'
  },
  checkboxLabel: {
    display: 'flex',
    alignItems: 'center',
    gap: '5px',
    marginBottom: '10px'
  },
  actions: {
    display: 'flex',
    gap: '10px',
    marginTop: '20px'
  }
};

export default CustomerFormPage;