# Customer Management System

A full-stack web application for managing customer data, built using Spring Boot and React. This system allows users to efficiently create, update, and manage customer records with a clean and responsive interface.

---

## 📌 Technologies Used

* **Backend:** Spring Boot, Java, JPA/Hibernate
* **Frontend:** React, Axios, React Router
* **Database:** MySQL / MariaDB
* **Build Tools:** Maven, npm

---

## ✨ Features

* Create, view, update, and delete customers
* Manage multiple mobile numbers per customer
* Add and manage multiple addresses
* Basic relationship handling between customers
* Bulk upload customers using Excel/CSV files
* Paginated customer listing
* Responsive user interface

---

## 🚀 Getting Started

### 🔧 Prerequisites

Make sure you have installed:

* Java (8 or higher)
* Node.js (14 or higher)
* MySQL or MariaDB
* Maven

---

## 🗄️ Database Setup

Create a database:

```sql
CREATE DATABASE customer_management;
```

---

## ⚙️ Backend Setup (Spring Boot)

1. Navigate to backend folder:

```bash
cd backend
```

2. Configure database in:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customer_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

3. Run the application:

```bash
mvn spring-boot:run
```

Backend will run on:
👉 http://localhost:8080

---

## 💻 Frontend Setup (React)

1. Navigate to frontend folder:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Start the app:

```bash
npm start
```

Frontend will run on:
👉 http://localhost:3000

---

## 📡 API Endpoints

| Method | Endpoint                   | Description           |
| ------ | -------------------------- | --------------------- |
| GET    | /api/customers             | Get all customers     |
| GET    | /api/customers/{id}        | Get customer by ID    |
| POST   | /api/customers             | Create new customer   |
| PUT    | /api/customers/{id}        | Update customer       |
| DELETE | /api/customers/{id}        | Delete customer       |
| POST   | /api/customers/bulk-upload | Upload Excel/CSV file |

---

## 📁 Project Structure

```
customer-management-system/
├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── resources/
├── frontend/
│   ├── components/
│   ├── pages/
│   └── api/
└── README.md
```





## 👨‍💻 Author

Sahan Navod Senavira

---


