# Mortgage Assignment

## Assignment

The assignment implement a Mortgage application using Spring boot that contains the operations for Mortgage - GET/POST.

## Tools and Technology

- Java 21
- Maven 3.5.1
- Git
- Docker
- Spring Framework
- Mockito
- Spring Security
- Lombok (Reduces boilerplate code)
- SLF4J (For logging)
- OpenApi Documentation
- Postman

## Steps to run the Mortgage Application

There are several ways to run a Spring Boot application on your local machine.

### Method 1: Using Docker

- Open cmd and run
  docker pull sakshi899/backend-mortgage-system:v1.0.0
- After image pull run the application by using below commad
  docker run -p 8080:8080 sakshi899/backend-mortgage-system:v1.0.0

### Method 2: Using Git and Docker

- Clone the repository:
    - git clone https://github.com/SakshiMahajan899/backend-mortgage-system.git
    - cd backend-mortgage-system
- Docker compose file that can be run with **docker-compose up --build** which should start up a functional application
  at port 8080 (including dependencies like a database)

### Access the application:

- Mortgage Application is up and running on localhost at port 8080 you can execute below metioned endpoints using
  POSTMAN.

## Spring Security

- This application uses the Spring Security for authentication
- Use **Basic Auth** inside Authorization and pass **Username** - user and **Password** - password

## Endpoints

<table>
<tr>
   <td>Endpoint</td><td>Description</td><td>Request body example</td><td>Response body example</td>
</tr>
<!-- GET /api/v1/interest-rates -->
<tr>
   <td> GET /api/v1/interest-rates </td>
   <td>

      get a list of current interest rates

   </td>
   <td>

   ```json
   
   ```

   </td>
   <td>

   ```json
         [
  {
    "maturityPeriod": 10,
    "interestRate": 5.0,
    "lastUpdate": "2025-03-03T12:00:00.000+00:00"
  },
  {
    "maturityPeriod": 20,
    "interestRate": 6.0,
    "lastUpdate": "2025-03-03T12:00:00.000+00:00"
  }
]
   ```

   </td>
</tr>

<!-- POST /api/v1/mortgage-check -->
<tr>
   <td>POST /api/v1/mortgage-check </td>
   <td>

        post the parameters to calculate for a mortgage check

   </td>
   <td>

   ```json
        {
  "income": 75000,
  "maturityPeriod": 20,
  "loanValue": 250000,
  "homeValue": 300000
}
   ```

   </td>
   <td>

   ```json
       {
  "monthlyCost": 1791.077646195432,
  "feasible": true
}
   ```

   </td>
</tr>


</table>

## Testing and Validation

### Unit Tests (UT) & Integration Tests (IT)

    - As part of testing strategy, i have ensured that all primary use cases are covered by unit test and/or integration test. 
    - This approach guarantees comprehensive validation and reliability of the system.
    - Test Coverage with UT and IT is 85% for Mortgage Application.

## Logging and Monitoring

- Proper logging is done to log requests, responses, and errors.
- Used Spring Boot Actuator for monitoring the health of application in production , exposed **/actuator/health** and *
  */actuator/metrics** endpoints for real-time monitoring

## Continuous Integration and Deployment

- Within the backend-mortgage-system/.github/workflows/ directory, you'll find the ci-cd.yml file, which is used to
  build the code & deploy the application image to dockerHub.
- All the Unit and Integration Test Cases are automatically triggered once pipeline execute.
- Pipeline Link for reference https://github.com/SakshiMahajan899/backend-mortgage-system/actions/runs/13706849864

## Code Design & Best Practices

### Separation of Concerns
  
  - MortgageService handles mortgage calculations.
  - InterestRateService handles interest rate retrieval.
  - MortgageCalculatorStrategy is used for flexible interest rate calculations.
    
### Caching (Performance Optimization)
  
  - @Cacheable("interestRates") is used to reduce database queries for interest rates.
    
### Custom Exceptions for Better Error Handling

 - Instead of generic exceptions, we have specific exceptions for better debugging.






