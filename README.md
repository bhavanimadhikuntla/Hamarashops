# HamaraShops.ai — Enterprise Microservices Platform

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.2-blue.svg?style=flat-square&logo=spring)](https://spring.io/projects/spring-cloud)
[![React](https://img.shields.io/badge/React-19.0.0-61DAFB.svg?style=flat-square&logo=react)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-5.4.11-646CFF.svg?style=flat-square&logo=vite)](https://vitejs.dev/)
[![Google Cloud Run](https://img.shields.io/badge/GCP-Cloud%20Run-4285F4.svg?style=flat-square&logo=googlecloud)](https://cloud.google.com/run)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

# HamaraShops-AI

## AI-Powered Enterprise Platform with Multi-Agent Chatbot

HamaraShops-AI is an AI-powered enterprise web platform designed to provide intelligent business information, industry-specific AI solutions, and appointment assistance through a modern web application.

The platform combines a **React/Vite frontend**, **Spring Boot microservices**, an **API Gateway**, and a **Java-based multi-agent chatbot** powered by Groq.

---

## 🚀 Key Features

* Modern React/Vite web application
* Spring Boot backend services
* Spring Cloud API Gateway
* Java-based multi-agent chatbot
* Company information assistant
* Industry-specific AI solutions assistant
* Appointment assistance
* Groq LLM integration
* Centralized company and content data
* REST APIs
* Health and actuator endpoints
* Responsive chatbot interface
* Frontend-to-backend end-to-end integration

---

# 🏗️ System Architecture

```text
                         HAMARASHOPS-AI
                              │
                              ▼
                    ┌───────────────────┐
                    │   React Frontend  │
                    │     Vite + UI     │
                    └─────────┬─────────┘
                              │
                              │ REST API
                              ▼
                    ┌───────────────────┐
                    │    API Gateway    │
                    │      Port 8080    │
                    └─────────┬─────────┘
                              │
              ┌───────────────┼────────────────┐
              │               │                │
              ▼               ▼                ▼
       ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
       │   Business  │ │   Content   │ │   Contact   │
       │   Service   │ │   Service   │ │   Service   │
       │    :8082    │ │    :8081    │ │    :8083    │
       └──────┬──────┘ └─────────────┘ └─────────────┘
              │
              ▼
       ┌─────────────────┐
       │ ChatOrchestrator │
       └────────┬────────┘
                │
       ┌────────┼──────────┐
       │        │          │
       ▼        ▼          ▼
   Company   Industry   Appointment
    Agent      Agent       Agent
       │        │          │
       └────────┼──────────┘
                ▼
          ┌────────────┐
          │ Groq / LLM │
          └────────────┘
```

---

# 📁 Project Structure

```text
hamarashops.ai-main/
│
├── api-gateway/
│   ├── src/
│   └── pom.xml
│
├── business-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/hamarashops/
│   │   │   │       └── ai/
│   │   │   │           └── service/
│   │   │   │               ├── CompanyAgentImpl.java
│   │   │   │               ├── IndustryAgentImpl.java
│   │   │   │               ├── AppointmentAgentImpl.java
│   │   │   │               ├── ChatOrchestrator.java
│   │   │   │               └── GroqService.java
│   │   │   │
│   │   │   └── resources/
│   │   └── pom.xml
│   │
│   └── .env
│
├── content-service/
│   ├── src/
│   └── pom.xml
│
├── contact-service/
│   ├── src/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   └── common/
│   │   │       └── Chatbot.jsx
│   │   ├── services/
│   │   │   └── apiClient.js
│   │   └── ...
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

---

# 🤖 Multi-Agent Chatbot

The chatbot is implemented entirely in **Java using Spring Boot**.

There is **no separate Python AI service**.

The chatbot uses a `ChatOrchestrator` to determine which specialized agent should handle a user's request.

## Agents

### 1. Company Agent

Handles questions related to:

* Company information
* CEO
* Mission
* Vision
* Company journey
* Company locations
* General HamaraShops.ai information

Example:

```text
User:
Who is the CEO of HamaraShops.ai?

Company Agent:
CEO: Dheerendar Srivastav –
Founder & Chief Executive Officer.
```

---

### 2. Industry Agent

Handles industry-related questions including:

* Retail
* Healthcare
* Financial Services
* Manufacturing
* Industry-specific AI solutions
* AI use cases

Example:

```text
User:
What AI services do you provide for retail?

Industry Agent:
Provides retail-focused AI solutions and use cases.
```

---

### 3. Appointment Agent

Handles appointment-related requests including:

* Schedule a meeting
* Book a consultation
* Product discussion
* AI architecture discussion
* General inquiry

The agent does not falsely claim that an appointment has been booked or confirmed.

Example:

```text
User:
I want to schedule an appointment.

Appointment Agent:
Provides guidance for using the Schedule Appointment
option on the website.
```

---

# 🔀 ChatOrchestrator

The `ChatOrchestrator` is responsible for routing user requests to the appropriate agent.

```text
                    User Message
                         │
                         ▼
                  ChatOrchestrator
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
      Appointment      Industry       Company
         Agent          Agent          Agent
```

Examples of routing:

```text
"Schedule a meeting"
        ↓
Appointment Agent

"AI solutions for retail"
        ↓
Industry Agent

"Who is the CEO?"
        ↓
Company Agent
```

---

# 🌐 API Endpoints

## API Gateway

```text
http://localhost:8080
```

### Chat

```http
POST /api/v1/chat
```

Request:

```json
{
  "message": "Who is the CEO of HamaraShops.ai?"
}
```

Response:

```json
{
  "agent": "Company Agent",
  "message": "CEO: Dheerendar Srivastav..."
}
```

---

## Content Service

```text
http://localhost:8081
```

### Company Information

```http
GET /api/v1/company
```

The company information is loaded from:

```text
content-service/src/main/resources/data/company.json
```

The data includes:

* Company name
* CEO
* Designation
* Mission
* Vision
* Approach
* Journey
* Office locations

---

## Business Service

```text
http://localhost:8082
```

The Business Service contains the chatbot and business-related functionality.

---

## Contact Service

```text
http://localhost:8083
```

The Contact Service handles contact and appointment-related functionality.

---

# 💻 Frontend

The frontend is built using:

* React
* Vite
* Tailwind CSS
* Axios
* Lucide React

The chatbot component is located at:

```text
frontend/src/components/common/Chatbot.jsx
```

The API client is located at:

```text
frontend/src/services/apiClient.js
```

The frontend communicates with the API Gateway:

```text
http://localhost:8080/api/v1
```

---

# 🔄 End-to-End Chat Flow

```text
1. User opens the React website
              │
              ▼
2. User opens the AI chatbot
              │
              ▼
3. User enters a message
              │
              ▼
4. React sends POST request
              │
              ▼
   /api/v1/chat
              │
              ▼
5. API Gateway receives request
              │
              ▼
6. Request is routed to Business Service
              │
              ▼
7. ChatOrchestrator analyzes the message
              │
              ▼
8. Appropriate agent is selected
              │
              ▼
9. Agent processes the request
              │
              ▼
10. Groq LLM generates the response
              │
              ▼
11. Response returns to React
              │
              ▼
12. Chatbot displays the response
```

---

# 🛠️ Technologies Used

## Frontend

| Technology   | Purpose                |
| ------------ | ---------------------- |
| React        | User interface         |
| Vite         | Frontend build tool    |
| Tailwind CSS | Styling                |
| Axios        | REST API communication |
| Lucide React | Icons                  |

## Backend

| Technology           | Purpose                         |
| -------------------- | ------------------------------- |
| Java 17              | Programming language            |
| Spring Boot          | Backend framework               |
| Spring Cloud Gateway | API Gateway                     |
| Maven                | Build and dependency management |
| REST APIs            | Service communication           |
| Jackson              | JSON processing                 |
| Actuator             | Health monitoring               |

## AI

| Technology        | Purpose                  |
| ----------------- | ------------------------ |
| Groq              | LLM API                  |
| Java-based Agents | Multi-agent architecture |
| ChatOrchestrator  | Agent routing            |

---

# ⚙️ Prerequisites

Install the following:

* Java 17
* Maven 3.9+
* Node.js
* npm
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

Verify Node.js:

```bash
node -v
```

Verify npm:

```bash
npm -v
```

---

# 🔐 Environment Configuration

The Business Service requires a Groq API key.

Create/configure:

```text
business-service/.env
```

Example:

```env
GROQ_API_KEY=your_groq_api_key
```

Do not commit the real API key to GitHub.

Add `.env` to `.gitignore`:

```text
.env
```

---

# ▶️ Running the Project

Start the backend services first.

## 1. Content Service

```bash
cd content-service
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8081
```

---

## 2. Business Service

Open another terminal:

```bash
cd business-service
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8082
```

---

## 3. Contact Service

Open another terminal:

```bash
cd contact-service
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8083
```

---

## 4. API Gateway

Open another terminal:

```bash
cd api-gateway
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8080
```

---

## 5. Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Vite will display the local frontend URL, normally:

```text
http://localhost:5173
```

---

# 🧪 Testing

## Test Company Agent

```http
POST http://localhost:8080/api/v1/chat
```

Request:

```json
{
  "message": "Who is the CEO of HamaraShops.ai?"
}
```

Expected routing:

```text
Company Agent
```

---

## Test Industry Agent

Request:

```json
{
  "message": "What AI services do you provide for retail?"
}
```

Expected routing:

```text
Industry Agent
```

---

## Test Appointment Agent

Request:

```json
{
  "message": "I want to schedule an appointment"
}
```

Expected routing:

```text
Appointment Agent
```

---

## Test Company Locations

Request:

```json
{
  "message": "Where are the HamaraShops.ai offices located?"
}
```

Expected routing:

```text
Company Agent
```

---

## 📜 License & Acknowledgments

Distributed under the **MIT License**. See `LICENSE` for details.

Developed with ❤️ by **Bhavani (Full Stack Java Developer)**.
