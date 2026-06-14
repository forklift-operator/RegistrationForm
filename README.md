# Simple Register Form API

This project is a basic user registration and login system built with Java. It uses a simple Java HTTP server for the backend, MySQL for storing user data, and plain HTML, and JavaScript for the frontend.

## Technologies Used

- **Backend:** Java 25 with the built-in HTTP server
- **Database:** MySQL
- **JSON Handling:** Google Gson
- **Testing:** JUnit 5
- **Frontend:** HTML5 and Vanilla JavaScript

## Features

### CAPTCHA System

The server creates a simple random math question and gives it a unique token. The correct answer is stored temporarily in memory using a `ConcurrentHashMap`. After the user tries to answer the CAPTCHA, it is removed so the same token cannot be reused.

### User Registration

Users can register by sending their details as JSON. The backend checks the input fields, validates the CAPTCHA, and checks the database to make sure the email is not already registered.

### Login and Sessions

Users can log in with their email and password. If the login is successful, the server creates a random session ID using `UUID.randomUUID()` and sends it back to the browser as a cookie.

### Logout

Users can log out, which removes the session from the server and clears the cookie in the browser.

### Profile View and Update

Logged-in users can view their profile using `/me` and update their details using `/update`. These routes are protected by checking the session cookie.

## Some Java Features Used

- `HttpServer.create()` for starting the backend server
- `gson.fromJson()` and `gson.toJson()` for converting between JSON and Java objects
- `String.matches()` for checking fields like email and password format
- `UUID.randomUUID()` for creating session and CAPTCHA tokens
- `ConcurrentHashMap` for storing temporary CAPTCHA answers and user sessions safely

## File Structure

### handler Package

- **StaticFileHandler.java**  
  Handles requests for the frontend files, such as the main HTML page.

- **CaptchaHandler.java**  
  Handles GET `/captcha` requests and returns a math CAPTCHA question with a token.

- **RegisterHandler.java**  
  Handles POST `/register` requests. It validates the submitted data and creates a new user in the database.

- **LoginHandler.java**  
  Handles POST `/login` requests. It checks the user's email and password, then creates a session cookie if the login is successful.

- **LogoutHandler.java**  
  Handles POST `/logout` requests. It removes the user's session and clears the cookie.

- **MeHandler.java**  
  Handles GET `/me` requests. It checks the session cookie and returns the logged-in user's profile information.

- **UpdateHandler.java**  
  Handles PUT `/update` requests. It allows logged-in users to update their profile information.

### service Package

- **ValidateService.java**  
  Contains the main validation logic for registration, login, password rules, and CAPTCHA checking.

- **SessionService.java**  
  Manages user sessions using a thread-safe `ConcurrentHashMap`.

- **CaptchaService.java**  
  Creates random math CAPTCHA questions and stores the correct answers temporarily.

### repository and database Packages

- **DatabaseConfig.java**  
  Sets up the MySQL database connection and creates the required table if it does not already exist.

- **UserRepository.java**  
  Contains the SQL queries for finding, creating, and updating users.

### Root Directory

- **Main.java**  
  Starts the server, sets the port, and connects each endpoint to its handler.

## Unit Test Code Coverage (>80%)

<img width="1129" height="352" alt="Unit test coverage screenshot" src="https://github.com/user-attachments/assets/8eedcb3a-9f30-41ba-9c8d-7bd55c68cfdb" />
