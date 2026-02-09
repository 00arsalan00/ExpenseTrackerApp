# Expanse Tracker App – Backend

Project Description:
This project focuses on building the backend of an Expense Tracker application using Spring Boot.


## Day 1:
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

## Day 4
- Designed request and response DTOs for authentication flow
- DTOs define what data is allowed to cross the API boundary.
- Integrated DTOs into signup, login, and token refresh workflows

## Day 5
- Implemented JWT-based authentication filter
- Configured Spring Security with stateless session management
- Integrated custom JwtAuthFilter into the security filter chain
- Wired AuthenticationManager and AuthenticationProvider for login flow
- Defined public and protected API endpoints

## Day 6
- Implemented /signup and /login mapping
- Implemented how Access token will be generated using Refresh Token

## Day 7
- Starting of Implementation of Expense Tracker App Logic
- Implemented Expense entity and ExpenseRepository
- Enums are used to create fixed Expense type and category
- Do not use FindById alone because this returns expense regardless of owner which can lead to data leaks.

## Testing and Bug Fixing
- Tested all 3 apis.
- A critical issue was identified in the authentication flow.
- During **signup**, a refresh token was generated and stored in the database along with the user ID.
- On **login**, the system attempted to generate and persist another refresh token for the same user.
- Since the `tokens` table enforces a **one-to-one relationship** between user and refresh token (unique `user_id`), this resulted in a **duplicate key constraint violation**.
- The root cause was improper refresh token lifecycle management.
- Enforced a **single active refresh token per user**.
- On login (and signup), any existing refresh token for the user is deleted before creating a new one.
- This ensures token rotation instead of duplication and aligns with standard security practices.
- Signup and login flows now work correctly.
- No duplicate token collisions occur.
- Authentication is stable and production-ready.


### Classes Explained:

### Entity Classes

- **UserDetails**
    Represents a user record in the database, storing login credentials and profile identifiers.

- **UserRole**
    Defines authorization roles assigned to users for access control.

- **RefreshToken**
    Stores long-lived refresh tokens used to generate new access tokens without re-authentication.

- **Expense**
    Defing what is Expense and what type of Expense.


### Repository Classes

- **UserRepository**
    Provides database operations to fetch and persist user records using Spring Data.

- **RefreshTokenRepository**
    Manages CRUD operations for refresh tokens and token lookup.

- **ExpenseRepository**
    Tracking user expense using: UserId+Id, UserId, UserId+FromDate+ToDate, UserId+ExpenseCategory

### Service Classes

- **UserDetailsServiceImplementation**
    Loads user data from the database and adapts it to Spring Security’s authentication model.

- **CustomUserDetails**
    Converts the user entity into a security-safe representation required by Spring Security.

- **JwtService**
    Handles JWT creation, validation, and claim extraction for stateless authentication.

- **RefreshTokenService**
    Creates, validates, and expires refresh tokens for session continuity.


### DTO Classes

- **AuthRequestDto**  
    Carries username and password from the client during login requests.

- **UserDetailsDto**  
    Transfers user registration and profile data during signup without exposing entity internals.

- **JwtResponseDto**  
    Sends access token and refresh token to the client after successful authentication.

- **RefreshTokenRequestDto**  
    Carries refresh token from client to request a new access token.


### Auth Classes

- **JwtConfig**
    Validates token of each request

- **UserConfig**
    Declares PasswordEncoder and its Algo

- **SecurityConfig**
    Defines Security rulebook, checks which requests are public and which need authentication.
    How Authentication should happen and which filter should run in what order.


### Controller Classes
- **AuthController**
    This class implements /signup and /login request: creates user, authenticate user, generate access token(jwt), generate refresh token and return both.

- **TokenController**
    This class generates new access token from refresh token.

### Domain Enums
- An enum is a closed, finite set of valid values defined at compile time. These are the only values that can ever exist.