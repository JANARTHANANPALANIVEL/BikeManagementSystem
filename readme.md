# Bike Management System

The Bike Management System is a Java-based GUI application for managing bike rentals. It allows administrators and users to add, update, rent, and return bikes, as well as manage bike inventory using a MySQL database.

## Features

- Admin Features:
  - Add, update, and delete bike records.
  - View the list of all bikes.
  - Manage bike rentals and returns.

- User Features:
  - Rent and return bikes.
  - View available bikes.

## Prerequisites

- Java Development Kit (JDK) - version 8 or higher.
- MySQL Database - version 5.7 or higher.
- MySQL Connector/J - JDBC driver for MySQL.

## Project Structure

- `BikeManagementSystem.java`: Main Java application file containing the GUI and business logic.
- `bike_management.sql`: SQL script to set up the required database and tables.

## Getting Started

### Step 1: Set Up the Database

1. Open MySQL Workbench or any SQL client connected to your MySQL server.
2. Execute the `bike_management.sql` script to create the `bike_management` database, tables, and an initial admin user.

### Step 2: Update Database Credentials

In `BikeManagementSystem.java`, update the following constants with your MySQL database credentials:

```java
private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bike_management";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "password";
```

### Step 3: Compile and Run the Application

1. Open a terminal or command prompt.
2. Navigate to the directory containing `BikeManagementSystem.java`.
3. Compile the Java file:
   ```bash
   javac BikeManagementSystem.java
   ```
4. Run the application:
   ```bash
   java BikeManagementSystem
   ```

### Step 4: Log In

- Admin Login:
  - Username: `admin`
  - Password: `admin123`

This login provides access to all features, including deleting bikes.

## Database Schema

The database includes three main tables:

1. `users`: Stores user credentials and roles (admin or user).
2. `bikes`: Stores bike inventory details such as model, brand, price, and status.
3. `rentals`: Stores rental records including the bike ID, customer name, and rental duration.

## Additional Information

### Dependencies

- MySQL Connector/J - Make sure to add this JDBC driver to your Java classpath to allow the application to connect to the MySQL database.

### SQL Script (`bike_management.sql`)

The SQL script includes:
- Database creation.
- Table creation for `users`, `bikes`, and `rentals`.
- Sample admin user for login.

### Features in Detail

- Add Bike: Allows admin to add a new bike to the inventory.
- Update Bike: Updates details of an existing bike.
- Delete Bike: Admin-only feature to delete a bike from the inventory.
- Rent Bike: Lets users rent a bike for a specified duration.
- Return Bike: Manages returning a rented bike.

## Future Improvements

This project can be further extended to include:
- Enhanced user management (adding more roles).
- Reporting features (e.g., daily or weekly rentals).
- Notifications for due rental returns.

---

