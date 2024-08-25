# Medicine Consultations

This project hosts a server that provides the REST APIs for consultations, and investigations requests.
The server uses the Spring Boot framework and establishes a connection to a non-relational database (MongoDB).

## Running the application 

1. Reload the Maven Project.
2. Generate the gRPC code by running `mvn compile`. After the code generation, mark the following directories as **Sources Root**:
    - **com** - from target/generated-sources/protobuf/grpc-java/
    - **java** - from target/generated-sources/protobuf/
3. Make sure the database instance is started.
