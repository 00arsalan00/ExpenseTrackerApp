# Expanse Tracker App – Backend

Project Description:
This project focuses on building the backend of an Expense Tracker application using Spring Boot.


#Day 1:
- Project initialization
- Designing the entity layer
- Defining relationships between entities

ER diagram And Relationship between each entity:
## Database Design

![ER Diagram](./doc/image/er-diagram.png)

## Day 2
- Restructured packages for better separation of concerns
- Implemented `JwtService` for JWT generation and validation
- Implemented `CustomUserDetails` as a bridge between database users and Spring Security
- CustomUserDetails converts database user into a format that Spring Security understands.
- Studied access token vs refresh token concepts
- Defined token-based authentication flow for client requests

## Day 3
- Implemented Spring Security `UserDetailsService` to authenticate users from the database
- Integrated `CustomUserDetails` as the security adapter for role-based authorization
- Designed refresh token mechanism for session continuity
- Implemented refresh token creation, validation, and expiry handling
- Used Spring Data `CrudRepository` for user and token persistence
- Strengthened understanding of authentication flow and token lifecycle

