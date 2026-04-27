import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/api';

function BulkUpload() {
  const navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [progress, setProgress] = useState(0);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      const fileType = selectedFile.name.split('.').pop().toLowerCase();
      if (fileType === 'xlsx' || fileType === 'xls' || fileType === 'csv') {
        setFile(selectedFile);
        setError(null);
        setResult(null);
      } else {
        setError('Please select an Excel file (.xlsx, .xls) or CSV file');
        setFile(null);
      }
    }
  };

  const handleUpload = async () => {
    if (!file) {
      setError('Please select a file');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      setUploading(true);
      setProgress(0);
      
      const interval = setInterval(() => {
        setProgress(prev => {
          if (prev >= 90) return prev;
          return prev + 10;
        });
      }, 500);

      const response = await API.post('/customers/bulk-upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        timeout: 300000
      });
      
      clearInterval(interval);
      setProgress(100);
      setResult(response.data);
      setFile(null);
      document.getElementById('file-input').value = '';
      
    } catch (err) {
      setError(err.response?.data?.message || 'Upload failed. Please check your file format.');
    } finally {
      setUploading(false);
      setTimeout(() => setProgress(0), 2000);
    }
  };

  const downloadTemplate = () => {
    const headers = ['Name', 'Date of Birth', 'NIC Number'];
    const sampleRows = [
      ['John Doe', '1990-01-15', '123456789V'],
      ['Jane Smith', '1985-05-20', '987654321V'],
      ['Bob Johnson', '1995-12-10', '456789123V']
    ];
    
    const csvContent = [
      headers.join(','),
      ...sampleRows.map(row => row.join(','))
    ].join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'customer_template.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  // Parse and display error messages nicely
  const formatResultMessage = (message) => {
    if (message && message.includes('✅ Success:')) {
      // Split into lines and format
      const lines = message.split('\n');
      return (
        <div>
          <div style={{ fontWeight: 'bold', marginBottom: '10px', fontSize: '16px' }}>
            {lines[0]}
          </div>
          {lines.slice(2).map((line, idx) => (
            <div key={idx} style={{ 
              fontFamily: 'monospace', 
              fontSize: '12px', 
              marginTop: '5px',
              color: line.includes('❌') ? '#e74c3c' : '#555',
              whiteSpace: 'pre-wrap'
            }}>
              {line}
            </div>
          ))}
        </div>
      );
    }
    return message;
  };

  return (
    <div style={styles.container}>
      {/* Header with Back Button and Title on SAME LINE */}
      <div style={styles.header}>
        <button 
          className="btn btn-primary"
          onClick={() => navigate('/')}
        >
          ← Back to Customers
        </button>
        <div style={{ flex: 1, display: 'flex', justifyContent: 'center' }}>
          <h1 className="page-title" style={{ margin: 0 }}>
            Bulk Customer Upload
          </h1>
        </div>
        <div style={{ width: '120px' }}></div>
      </div>

      <div className="card">
        <div style={styles.infoSection}>
          <h3>📋 Instructions</h3>
          <ul style={styles.instructions}>
            <li>Upload Excel file (.xlsx, .xls) or CSV file with customer data</li>
            <li>File must have these columns in order: <strong>Name, Date of Birth, NIC Number</strong></li>
            <li>Date format: <strong>YYYY-MM-DD</strong> (e.g., 1990-01-15)</li>
            <li>NIC numbers must be unique across all customers</li>
            <li>Supports up to <strong>1,000,000 records</strong> (processed in batches)</li>
            <li>Large files may take a few minutes to process</li>
          </ul>
        </div>

        <div style={styles.templateSection}>
          <h3>📥 Download Template</h3>
          <p>Download our template file to see the correct format:</p>
          <button 
            onClick={downloadTemplate}
            className="btn btn-primary"
          >
            Download CSV Template
          </button>
        </div>

        <div style={styles.uploadSection}>
          <h3>📤 Upload File</h3>
          
          <div style={styles.fileInputArea}>
            <input
              id="file-input"
              type="file"
              accept=".xlsx, .xls, .csv"
              onChange={handleFileChange}
              disabled={uploading}
              style={styles.fileInput}
            />
            {file && (
              <div style={styles.fileInfo}>
                ✅ Selected: {file.name} ({(file.size / 1024).toFixed(2)} KB)
              </div>
            )}
          </div>

          <button
            onClick={handleUpload}
            disabled={uploading || !file}
            className="btn btn-success"
            style={styles.uploadButton}
          >
            {uploading ? '⏳ Uploading...' : '🚀 Upload File'}
          </button>

          {uploading && (
            <div style={styles.progressSection}>
              <div style={styles.progressBar}>
                <div style={{ ...styles.progressFill, width: `${progress}%` }} />
              </div>
              <p style={styles.progressText}>Processing... Please wait</p>
            </div>
          )}

          {error && (
            <div className="alert-error" style={styles.message}>
              <strong>❌ Error:</strong> {error}
            </div>
          )}

          {/* Result display with formatted error messages */}
          {result && result.success && (
            <div className="alert-success" style={styles.message}>
              {formatResultMessage(result.message)}
              <button 
                onClick={() => navigate('/')}
                className="btn btn-primary"
                style={{ marginTop: '15px' }}
              >
                View Customers
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: '20px'
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '20px',
    gap: '20px'
  },
  infoSection: {
    backgroundColor: '#f0f7ff',
    padding: '15px',
    borderRadius: '8px',
    marginBottom: '20px'
  },
  instructions: {
    marginLeft: '20px',
    lineHeight: '1.8',
    color: '#555'
  },
  templateSection: {
    backgroundColor: '#f9f9f9',
    padding: '15px',
    borderRadius: '8px',
    marginBottom: '20px',
    textAlign: 'center'
  },
  uploadSection: {
    textAlign: 'center',
    padding: '20px'
  },
  fileInputArea: {
    marginBottom: '20px'
  },
  fileInput: {
    padding: '10px',
    border: '1px solid #ddd',
    borderRadius: '4px',
    width: '100%',
    maxWidth: '400px',
    marginBottom: '10px'
  },
  fileInfo: {
    marginTop: '10px',
    color: '#27ae60',
    fontWeight: 'bold'
  },
  uploadButton: {
    marginTop: '10px',
    fontSize: '16px',
    padding: '10px 30px'
  },
  progressSection: {
    marginTop: '20px'
  },
  progressBar: {
    width: '100%',
    height: '20px',
    backgroundColor: '#f0f0f0',
    borderRadius: '10px',
    overflow: 'hidden'
  },
  progressFill: {
    height: '100%',
    backgroundColor: '#3498db',
    transition: 'width 0.3s ease',
    borderRadius: '10px'
  },
  progressText: {
    marginTop: '10px',
    color: '#666',
    fontSize: '14px'
  },
  message: {
    marginTop: '20px',
    padding: '15px',
    textAlign: 'left'
  }
};

export default BulkUpload;