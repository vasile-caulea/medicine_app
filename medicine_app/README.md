# Medicine Application Service

This project hosts a server that contains the REST APIs for patients, physicians, and appointment requests. 
The server uses the Spring Boot framework and establishes a connection to a relational database (MariaDB).

## Running the application

1. Reload the Maven Project.
2. Generate the gRPC code by running `mvn compile`. After the code generation, mark the following directories as **Sources Root**:
    - **com** - from target/generated-sources/protobuf/grpc-java/
    - **java** - from target/generated-sources/protobuf/
3. Make sure the database instance is started.
