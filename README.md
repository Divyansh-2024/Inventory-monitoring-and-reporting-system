# Inventory-monitoring-and-reporting-system
Overview

The Inventory Monitoring and Reporting System is a Java-based desktop application designed to manage, monitor, and generate reports for product inventory efficiently.
It helps businesses track stock levels, add or remove products, and generate insightful inventory reports.

This project demonstrates fundamental concepts of Java development, JDBC (Database Connectivity), Object-Oriented Design, and Testing using JUnit and Mockito.

## ⚙️ Features
User Authentication: Secure login and access control for users.
Product Management: Add, update, and delete products from inventory.
Reporting Module: Generate detailed inventory and sales reports.
Low Stock Alerts: Automatically alerts when stock levels fall below threshold.
Database Integration: Connected with MySQL database using JDBC.
CSV Export: Save reports in CSV format.
Unit Testing: Tested using JUnit and Mockito frameworks.

## Technology Stack
Category	Tools/Technologies
Language	Java
Database	MySQL
Libraries	JDBC, JUnit, Mockito
IDE	IntelliJ IDEA / VS Code
Version Control	Git & GitHub
Build Tool	Maven
Output	JAR File


## System Architecture
User Interface → Authentication Service → Inventory Service → Database (MySQL)
                               ↓
                          Reporting Module

## Modules

Authentication Module – Handles user login and access control.
Inventory Module – Manages all CRUD operations for products.
Report Module – Generates CSV reports and summaries.
Database Module – Ensures connection and data consistency via JDBC.
Exception Handling Module – Custom exceptions for validation and errors.

## Testing

Used JUnit for unit testing key functionalities.
Used Mockito for mocking service dependencies.
Tests cover scenarios like:
Product addition/removal
Duplicate product handling
Database connection failure
