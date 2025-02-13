# Social Media API

A RESTful API built with Spring Boot for a social media application, featuring secure user authentication, post management, and image handling.

## Architecture

The application follows a layered architecture pattern:

- **Controllers**: Handle HTTP requests and response mapping
- **Services**: Contain business logic and transaction management
- **Repositories**: Manage data persistence
- **DTOs**: Handle data transfer between layers
- **Models**: Define entity relationships
- **Mappers**: Transform between entities and DTOs

## Security Features

- JWT-based authentication
- Role-based access control (ADMIN, CASUAL)
- Secure password hashing with BCrypt
- Protected endpoints with Spring Security
- CORS configuration for frontend integration

## Key Features

- User authentication (register/login)
- Post management with image support
- Comment system
- Admin dashboard
- File upload handling
- Swagger/OpenAPI documentation

## Technologies

- Spring Boot 3.x
- Spring Security
- MySQL Database
- JWT Authentication
- Swagger/OpenAPI
- Lombok

## API Documentation

API documentation is available at:
- Swagger UI: `/swagger-ui.html`
- OpenAPI docs: `/v3/api-docs`

## Getting Started

1. Configure database settings in `application.properties`
2. Run the application using Maven: `mvn spring-boot:run`
3. Access Swagger UI for API documentation and testing

## Security Configuration

The application implements comprehensive security measures:
- Token-based authentication
- Secure endpoints with @PreAuthorize annotations
- Custom security utils for ownership verification
- Global exception handling for security events