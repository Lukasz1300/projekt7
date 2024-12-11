# projekt7

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
