[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/IE0ITl4j)
# Final Project for CS 5004 - (Influencer Data Management System)

(remove this and add your sections/elements)
This readme should contain the following information: 

* The group member's names and link to their personal githubs
* The application name and a brief description of the application
* Links to design documents and manuals
* Instructions on how to run the application

Ask yourself, if you started here in the readme, would you have what you need to work on this project and/or use the application?


## Introduction
Our Influencer Data Management System is a console-based application to help marketing professionals and businesses effectively manage social media influencer data. 
It follows the MVC (Model-View-Controller) architecture and implements various design patterns. Key features include data import/export 
in multiple formats (CSV, JSON), different searching, filtering and sorting options, user authentication with subscription-based access 
control, and a personalized favorites system allowing user to maintain a favorite list of influencers.

---

## Group Members and Github Links
Junyu Li [Brent-LI-Junyu](https://github.com/Brent-LI-Junyu)

Xingchen Liu [ivanlxc](https://github.com/ivanlxc)

Qianfu Peng [Cora1231](https://github.com/Cora1231)

---

## Design Documents and Manuals

- Design Document: [Final UML Design](DesignDocuments/FinalUMLDesign.md)
- User Manual:

---

## Instructions on Running the Application

### Prerequisites:

- Java Development Kit (JDK)
```bash
    Version: 11 or higher
```
- Gradle
```bash
    Version: 7.4.2 or higher
```

### Compilation and Running

#### On Unix/Linux/macOS:

- Build the project using Gradle:
```bash
./gradlew build
```

- Run the application:
```bash
./gradlew run
```

#### On Windows:

- Build the project using Gradle:
```bash
gradlew.bat build
```

- Run the application:
```bash
gradlew.bat run
```

### Running Tests

- To run all tests:
```bash
./gradlew test
```

### Features

- User registration and login
- Browse and search influencers
- Filter influencers by platform, category, follower count and country
- Sort influencers by various criteria
- Add influencers to favorites (requires login)
- Premium subscription features (ad rates visibility)
- Import data from CSV and JSON files
- Export data to CSV and JSON formats

### Brief Usage Guide

1. Register a new account or login with existing credentials
2. Navigate through the menu options to explore influencers
3. Subscribe to premium for additional features
4. Use the export/import options to save or load influencer data


