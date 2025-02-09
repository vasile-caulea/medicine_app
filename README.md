# Medicine Application with REST

The functionalities of this application manage:

- a data collection containing information about patients, physicians, and appointments
- a data set containing the consultations and investigations
- a simple identity management module

## Components

### Description

1. [medicine_app](medicine_app) - Server logic for patients, physicians and appointment requests - **Spring Boot (Java) & MariaDB**
2. [medicine_consults](medicine_consults) - Server logic for consultations and investigations requests - **Spring Boot (Java) & MongoDB**
3. [medicine_react](medicine_react) - Web user interface - **React (JS)**
4. [idm](idm) - Identity Management Service - **Python & gRPC**
5. [gateway_service](gateway_service) - **Python & FastAPI & gRPC**

### Diagram
![Component diagram](medicine_app.png)
