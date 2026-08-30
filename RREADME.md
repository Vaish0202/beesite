# Unified Banking Platform (Consent-Based)

A portfolio/research project simulating a consent-based, open-banking-style
platform using Spring Boot microservices, React, PostgreSQL, and ML-based
fraud detection. This project does NOT connect to real banks — all bank
servers are mock/simulated.

## Tech Stack
- Backend: Spring Boot (Maven Wrapper), Java 21 (running on JDK 23)
- Frontend: React + JavaScript (Vite)
- Database: PostgreSQL
- Fraud Detection: Rule-based (Spring Boot) → ML (Python FastAPI) later
- Deployment target (later): AWS

## Services & Ports
| Service | Port |
|---|---|
| api-gateway | 8080 |
| auth-service | 8081 |
| user-service | 8082 |
| banking-core-service | 8083 |
| consent-service | 8084 |
| fraud-service | 8085 |
| document-service | 8086 |
| audit-service | 8087 |
| mock-bank-sbi-service | 8091 |
| mock-bank-hdfc-service | 8092 |
| mock-bank-icici-service | 8093 |
| frontend | 5173 |

## Status
🚧 In active development — see /docs for architecture notes as they're added.