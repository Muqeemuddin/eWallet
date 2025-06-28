# 💳 e-Wallet Application

A microservices-based e-Wallet application built with Spring Boot. It supports user registration, wallet linking, money transfers, transaction history tracking, and email notifications.

---

## 🚀 Features

- User registration and profile management
- Wallet linking and balance tracking
- Money transfers between users
- Viewable transaction history
- Email alerts for transaction confirmations
- RESTful APIs for all core operations
- Microservices architecture with modular services

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot, Java
- **Database:** MySQL
- **Messaging:** Kafka 
- **Email Notifications:** SMTP (JavaMailSender)
- **Containerization:** Docker
- **Testing:** JUnit
- **Architecture:** Microservices

---

## 📁 Project Structure
/e-wallet  
│  
├── user-service # Handles user registration and profile  
├── wallet-service # Manages wallet balances and linking  
├── transaction-service # Handles transfers and transaction history  
├── notification-service # Sends email alerts  
├── common-utils # Shared models and config  
├── docker-compose.yml # Container orchestration  
└── README.md  


---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL
- Docker (optional, for containerized setup)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Muqeemuddin/eWallet.git
   cd eWallet

2. Set up the database(MySql):
   ```bash
   CREATE DATABASE eWallet;

3. Configure environment:
   - Database Credentials
   - SMTP credentials

4. Run the services:
   ```bash
   mvn clean install
   mvn spring-boot:run

5. (Optional) Use docker:
   ```bash
   docker-compose up --build

🔌 API Endpoints  
  to be continued...
