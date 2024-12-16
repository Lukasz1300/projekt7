# projekt7 - food ordering app

Applications can be run using Docker, use the following commands:

The image was built with project7.jar in the target file.

By command 
docker-compose up
applications can be launched

Once the application is launched, you can access it in your browser at: http://localhost:8085/ http://localhost:8085/security/login

User with administrator rights: Login: testuser Password: password

The application uses a production database: PostgreSQL, tests: H2.
Access settings and more information in the file:
src/main/resources/application.properties

The application includes an example integration test that uses the WireMock library. The test simulates the behavior of the external NBP API and verifies the functionality of the CurrencyService,
which is responsible for fetching exchange rates.
Key assumptions of the test:
WireMock acts as a local server simulating API responses.
The test checks the correctness of the returned USD exchange rate based on a predefined "stubbed" response.
