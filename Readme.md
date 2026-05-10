# Railway Management System
### By: 
Aamir Hussain Hullio (CMS: 023-25-0195)
Muhammad Ali Hyder (CMS:023-25-0167)
Mujeeb narejo(CMS:023-25-0535)
### Instructor: Dr. Saif Hassan 

---
Introduction 
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

Motivation:
One of our friends sarmad bhai had a ticket to his city and he was not able to cancel it because they wanted him to visit the station personally that's why we decided to make a simpler system that is effective in booking and cancellation

Group division of Work 
Muhammad Ali Hyder:

Core Backend & Database Integration

DatabaseConnection.java
JDBC connectivity with MySQL
Database setup and testing
SQL integration and query handling
Fixing database-related bugs

Authentication System

LoginFrame.java
Signup & Login functionality
Admin/User role handling
Session management

Booking System Logic

Booking.java
Train seat management
Ticket booking functionality
Booking cancellation logic
Seat restoration after cancellation

Admin Functionalities

AdminFrame.java
Add/Delete trains
System-wide booking management
Dashboard statistics

Final Integration

Connecting all modules together
Debugging whole project
Final testing and execution

Mujeeb Narejo

User Features

UserFrame.java
Search trains by source/destination
View personal bookings
Passenger data handling

Models & Business Logic

Train.java
Passenger.java
User.java

Database Tables

Creating and managing:
trains
passengers
bookings

Testing

Functional testing of user operations
Checking train search and booking flow

Aamir Hussain Hullio

UI & Design

UITheme.java
Improve Swing UI appearance
Colors, fonts, layouts

Project Setup & Documentation

README formatting
Setup instructions
Compile/run instructions
IntelliJ/Eclipse setup documentation

Main Structure

Main.java
Project folder organization
File management

Presentation & Support

Slides preparation
Screenshots
Demo preparation
Known limitations section
Helping in testing/debugging



## Tech Stack
- **Java (Swing)** — Desktop GUI
- **JDBC** — Database connectivity
- **MySQL** — Data storage

---

## Project Structure

```
RailwayManagementSystem/
│
├── database_setup.sql              ← Run this in MySQL first!
│
├── src/railway/
│   ├── Main.java                   ← Entry point
│   │
│   ├── db/
│   │   └── DatabaseConnection.java ← MySQL connection
│   │
│   ├── model/
│   │   ├── User.java
│   │   ├── Train.java
│   │   ├── Passenger.java
│   │   └── Booking.java
│   │
│   ├── ui/
│   │   ├── LoginFrame.java         ← Login & Signup screen
│   │   ├── AdminFrame.java         ← Admin dashboard
│   │   └── UserFrame.java          ← User dashboard
│   │
│   └── util/
│       └── UITheme.java            ← Colors, fonts, UI helpers
│
└── README.md
```

---

## Setup Instructions

### Step 1 — Install Requirements
- Java JDK 11 or higher
- MySQL Server
- MySQL Connector/J JAR (mysql-connector-java-8.x.x.jar)

Download MySQL Connector: https://dev.mysql.com/downloads/connector/j/

### Step 2 — Setup Database
1. Open MySQL Workbench or MySQL command line
2. Run the `database_setup.sql` file:
   ```sql
   SOURCE /path/to/database_setup.sql;
   ```
   Or paste its contents directly into MySQL Workbench and execute.

### Step 3 — Configure Database Connection
Open `src/railway/db/DatabaseConnection.java` and update:
```java
private static final String USER = "root";        // your MySQL username
private static final String PASSWORD = "";         // your MySQL password
```

### Step 4 — Compile the Project

**Option A: Using Command Line**
```bash
# Create output directory
mkdir -p out

# Compile (replace path to mysql connector jar)
javac -cp ".;mysql-connector-java-8.x.x.jar" -d out -sourcepath src src/railway/Main.java

# Run
java -cp ".;out;mysql-connector-java-8.x.x.jar" railway.Main
```

**On Linux/Mac use `:` instead of `;`:**
```bash
javac -cp ".:mysql-connector-java-8.x.x.jar" -d out -sourcepath src src/railway/Main.java
java -cp ".:out:mysql-connector-java-8.x.x.jar" railway.Main
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

Github Repository Link:
https://github.com/muhammad-ali-hyder/Railway-Management-System


Youtube Video link:
https://youtu.be/d7C3u7Apy9U

## Known Limitations 
- Offline system only (no web/cloud)
- Basic UI (Java Swing)
- No online payment integration
- Passwords stored as plain text (add hashing for production)
