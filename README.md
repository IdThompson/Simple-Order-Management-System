# Simple-Order-Management-System
Overview
The Simple Order Management System is a web-based application designed to manage product orders for users. It allows users to place orders for products, tracks order details, and manages the order's lifecycle. This system includes features such as:

User Management: Allows users to register, view orders, and place new orders.

Product Management: Allows admin users to add and manage products.

Order Management: Allows users to place orders for products and track order statuses.

Stock Management: Automatically reduces product stock based on the orders placed.

Features
Order Creation: Users can create new orders by selecting products.

Order Pagination: Supports paginated retrieval of orders.

Stock Check: Automatically checks product stock before order placement.

Error Handling: Proper validation and error handling for various cases like insufficient stock and invalid users.

Technologies Used
Spring Boot: Used for building the backend API.

JPA/Hibernate: Used for database interaction and persistence.

Mysql Database: used for local development and testing.

JUnit: For unit testing.

Mockito: For mocking dependencies in unit tests.

Lombok: Reduces boilerplate code by generating getters, setters, constructors, etc.

Spring Data JPA: For repository handling and pagination.

Maven: For project dependency management.

Prerequisites
Before running this project, ensure you have the following installed on your machine:

Java 17 or higher

Maven

Git

Spring Boot (optional: can use IDE's integrated Spring Boot support)
