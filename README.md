# Charge Detail Record (CDR) Service

## Overview

The Charge Detail Record (CDR) Service is designed to manage and retrieve charge detail records. It offers a RESTful API to create, search, and retrieve charge records with caching for improved performance.

## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Assumptions](#assumptions)
- [Decisions](#decisions)
- [Future Considerations](#future-considerations)
- [Building and Running the Project](#building-and-running-the-project)
- [Testing and Coverage](#testing-and-coverage)
- [Design Patterns Implemented](#design-patterns-implemented)
- [Known Issues](#known-issues)

## Technologies Used
- **Java**: The primary programming language used.
- **Spring Boot**: Framework used for building the application.
- **MongoDB**: Used as the primary database to store charge detail records.
- **Redis**: Used for caching to enhance performance.
- **Docker**: Used for containerization.
- **JUnit & Mockito**: Used for testing.
- **Maven**: Build automation tool.

## Assumptions
- `startTime` and `endTime` are in UTC.
- `startTime` is not equal to `endTime`.
- `SearchCdrRequest` can be extended to have more attributes.

## Decisions
- **MongoDB**: Chosen for its flexibility in handling unstructured data and ease of scalability.
- **Redis**: Selected for its in-memory caching capabilities which significantly enhance read performance and reduce load on the database.

## Future Considerations
- **Implement Authentication**: To secure the endpoints.
- **Non-blocking Realtime**: Improve the system to handle non-blocking real-time updates.

## Building and Running the Project

### Prerequisites
- Docker
- Java 22
- Maven

### Steps to Build and Run

1. **Build the project**:
    ```bash
    mvn clean install
    ```

2. **Run the application using Docker**:
    ```bash
    docker-compose up 
    ```
3. swagger is exposed at ``` http://localhost:8080/swagger-ui/index.html ```

The application will be accessible at `http://localhost:8080`.

## Testing and Coverage

The project includes unit and integration tests using JUnit and Mockito.

### Running Tests
Execute the following command to run all tests:
```bash
mvn test
```

## Design Patterns Implemented
- **Service Layer Pattern**: Used to define the application's business logic.
- **Repository Pattern**: Abstracts the data access layer, making it easier to swap out data sources.
- **Cache Aside Pattern**: Used with Redis to improve read performance.

## Known Issues
- **Concurrency Issues**: There might be race conditions when multiple requests try to update the same record.
- **Data Validation**: More rigorous validation on input data can be implemented.
- **Error Handling**: Improve error handling mechanisms to cover more edge cases.
- **No Coverage Report**: There is no coverage report generated after test case runs
- We can improve the management of embedded mongodb for testing

## Additional Guidelines

### Does your project have instructions on how to build and run it?
Yes, instructions for building and running the project are provided.

### Does your project consider any form of testing and coverage?
Yes, the project includes unit and integration tests, but there is no coverage reports .

### Does your project implement any design pattern(s) to help you solve known issues?
Yes, several design patterns like Service Layer, Repository, and Cache Aside are implemented.

### Try to make your project as production ready as possible
Efforts have been made to ensure the project is production-ready, including the use of Docker for containerization, caching for performance improvement, and detailed logging.

### Transparency is key, so if there are any known bugs/issues it could be a good idea to document them
Known issues are documented in the [Known Issues](#known-issues) section.
