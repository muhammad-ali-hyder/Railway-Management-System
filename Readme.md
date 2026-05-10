# Railway Management System

A Java-based Railway Management System with MySQL database integration.

---
### By: 
Aamir Hussain Hullio (CMS: 023-25-0195)
Muhammad Ali Hyder (CMS:023-25-0167)
Mujeeb narejo(CMS:023-25-0535)
### Instructor: Dr. Saif Hassan 

---
# Introduction 
The Railway Management System is a desktop-based application developed using Java Swing and 
MySQL. The system is designed to computerize railway operations that are traditionally handled 
through manual paper-based processes, which are slow, error-prone, and difficult to maintain at scale. 
The application manages trains, passengers, ticket bookings, seat allocation, and user accounts in an 
efficient and centralized manner. The graphical user interface allows administrators and passengers to 
perform operations easily without requiring any advanced technical knowledge. 
The database serves as the backbone of the entire system. All information regarding trains, 
passengers, bookings, and users is stored in well-structured relational tables. JDBC connectivity 
enables real-time and secure communication between the Java frontend and the MySQL database 
backend. 

# Motivation:
One of our friends sarmad bhai had a ticket to his city and he was not able to cancel it because they wanted him to visit the station personally that's why we decided to make a simpler system that is effective in booking and cancellation

# **Group Division Of Work** 
**Muhammad Ali Hyder**:

**Core Backend & Database Integration**

DatabaseConnection.java
JDBC connectivity with MySQL
Database setup and testing
SQL integration and query handling
Fixing database-related bugs

**Authentication System**

LoginFrame.java
Signup & Login functionality
Admin/User role handling
Session management

**Booking System Logic**

Booking.java
Train seat management
Ticket booking functionality
Booking cancellation logic
Seat restoration after cancellation

**Admin Functionalities**

AdminFrame.java
Add/Delete trains
System-wide booking management
Dashboard statistics

**Final Integration**

Connecting all modules together
Debugging whole project
Final testing and execution

**Mujeeb Narejo**

**User Features**

UserFrame.java
Search trains by source/destination
View personal bookings
Passenger data handling

**Models & Business Logic**

Train.java
Passenger.java
User.java

**Database Tables**

Creating and managing:
trains
passengers
bookings

**Testing**

Functional testing of user operations
Checking train search and booking flow

**Aamir Hussain Hullio**

**UI & Design**

UITheme.java
Improve Swing UI appearance
Colors, fonts, layouts

**Project Setup & Documentation**

README formatting
Setup instructions
Compile/run instructions
IntelliJ/Eclipse setup documentation

**Main Structure**

Main.java
Project folder organization
File management

**Presentation & Support**

Slides preparation
Screenshots
Demo preparation
Known limitations section
Helping in testing/debugging


---

# Requirements

Before running the project, make sure the following are installed:

- Java JDK 8 or later
- MySQL Server
- MySQL Connector/J JDBC Driver

---

## Tech Stack
- **Java (Swing)** — Desktop GUI
- **JDBC** — Database connectivity
- **MySQL** — Data storage
---

# Project Structure

```
RailwayManagementSystem/
│
├── src/
│   └── railway/
│       ├── Main.java
│       ├── DatabaseConnection.java
│       └── ...
│
├── database_setup.sql
├── mysql-connector-j-9.7.0.jar
└── Readme.md
```

---

# Database Setup

## Step 1: Start MySQL Server

Make sure your MySQL server is running.

---

## Step 2: Create Database

Open MySQL and run the SQL script:

```sql
SOURCE database_setup.sql;
```

Or import the file using MySQL Workbench.

---

## Step 3: Configure Database Credentials

Open:

```text
src/railway/DatabaseConnection.java
```

Update these values according to your MySQL setup:

```java
private static final String URL = "jdbc:mysql://localhost:3306/railwaydb";
private static final String USER = "root";
private static final String PASSWORD = "12345";
```

---

# Compile and Run

## Windows

Open Command Prompt inside the project folder and run:

```bash
mkdir out

javac -encoding UTF-8 -cp ".;mysql-connector-j-9.7.0.jar" -d out -sourcepath src src/railway/Main.java

java -cp ".;out;mysql-connector-j-9.7.0.jar" railway.Main
```

---

## Linux / macOS

Open Terminal inside the project folder and run:

```bash
mkdir out

javac -encoding UTF-8 -cp ".:mysql-connector-j-9.7.0.jar" -d out -sourcepath src src/railway/Main.java

java -cp ".:out:mysql-connector-j-9.7.0.jar" railway.Main
```

---

# Features

- Train Management
- Passenger Management
- Ticket Booking
- Reservation System
- MySQL Database Integration
- Console-Based User Interface

---

# Notes

- Ensure MySQL server is running before launching the application.
- Ensure the JDBC connector file is present in the project root directory.
- If compilation fails due to encoding issues, make sure the `-encoding UTF-8` flag is included.

---

# Troubleshooting

## MySQL Connection Error

Check:

- MySQL server is running
- Database name exists
- Username/password are correct
- JDBC connector JAR file is present

---

## Java Not Recognized

Make sure Java JDK is installed and added to system PATH.

Check installation:

```bash
java -version
javac -version
```

---
## Default Login Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |
| User  | (signup) | (you set) |

---
## Features

### Admin
- ✅ Dashboard with live statistics (trains, bookings, users)
- ✅ Add new trains with all details
- ✅ Remove trains (cascades bookings)
- ✅ View all bookings system-wide

### User
- ✅ Sign up / Login
- ✅ Search trains by source & destination
- ✅ Book tickets with passenger details
- ✅ View personal bookings
- ✅ Cancel bookings (restores seat availability)

---

## Database Tables

| Table      | Description                        |
|------------|------------------------------------|
| users      | Admin + user accounts              |
| trains     | Train schedules and seat info      |
| passengers | Passenger details per booking      |
| bookings   | Ticket bookings with status        |

---

## Known Limitations 
- Offline system only (no web/cloud)
- Basic UI (Java Swing)
- No online payment integration
- Passwords stored as plain text (add hashing for production)


# Author

Aamir hussain hulio
Suggestions for Readme Muhammad Ali hyder 

