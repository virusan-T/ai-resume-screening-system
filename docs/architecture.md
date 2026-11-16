# System Architecture

## Architecture Type

Modular Monolith + Separate AI Microservice

## Technologies

- Frontend: React + TypeScript
- Backend: Spring Boot
- AI Service: Python + FastAPI
- Database: PostgreSQL
- Authentication: Spring Security + JWT
- Deployment: Docker

## Communication

React communicates with Spring Boot using REST APIs.

Spring Boot communicates with FastAPI using internal REST APIs.

Spring Boot manages PostgreSQL database operations.

## Architecture Diagram

React
  |
  v
Spring Boot
  |
  |----> PostgreSQL
  |
  v
FastAPI
  |
  v
Machine Learning Models

## Main Modules

1. Authentication
2. User Management
3. Job Management
4. Resume Management
5. Resume Parsing
6. AI Matching
7. Candidate Ranking
8. Recruitment Management
9. Dashboard