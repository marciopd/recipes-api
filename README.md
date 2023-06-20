# recipes-api

This is a sample application which exposes a RESTful API for managing and searching recipes.

The data is persisted in a H2 in-memory database.

## Requirements
- JDK 17 installation
- Gradle 7.6.* (build tool) installation

## Key frameworks and libraries used:
- Spring Boot 3
- JPA and Spring Data JPA
- Spring security
- Swagger
- Flyway
- JUnit 5
- Mockito
- JWT
- Bean Validation

## Building and testing
In a terminal (Windows powershell example):
1) `git clone https://github.com/marciopd/recipes-api.git`
2) `cd recipes-api`
3) `./gradlew clean test`

## Running the application
In a terminal (Windows powershell example):

`./gradlew clean bootRun`

Then you can access the API docs at http://localhost:8080/swagger-ui.html.

## Features
The API is not fully implemented according to my plans, but allows to:
1) Login as a user via the `tokens` endpoint.
    - Admin: username: admin, password: test123
    - Customer: username: customer, password: test123
2) Create/Update/Delete `recipes` as an authenticated user
    - Use the access token obtained in the previous step for that
    - It's possible to enter the access token in Swagger by clicking on the "Authorize" button. 
      - The token will be sent as a bearer token in the subsequent requests.
      - Users can't change recipes of other users, unless they are admins
3) Get or search for `recipes` (public endpoints)
4) Get all `tags`
    - vegetarian is an example of tag
5) Create new tags (only admins)

There are some request examples documented in the controller integration tests.

## Layering

Typical 3 principal layers application:
1) `controller`: exposes REST API services (HTTP requests/responses)
2) `business`: business logic implementation
3) `persistence`: data access implementation

There is a `domain` witch is shared by controller and business layer. 
Basically requests and response objects to save boilerplate code.

Persistence entities are not allowed to leave the business layer.
