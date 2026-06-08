# 👤 Account Service

> Core identity, authentication, authorization, subscription, and user management service for the Coding Genie platform.

![Account Service Architecture](./account_service_hld.png)

---

# 📖 Overview

The Account Service is responsible for managing users, authentication, authorization, subscriptions, billing integration, and account-related operations across the Coding Genie ecosystem.

It serves as the central identity provider for the platform and acts as the source of truth for user accounts, authentication tokens, subscription plans, and billing information.

All requests entering the platform are ultimately validated against authentication and authorization logic managed by this service.

---

# 🎯 Responsibilities

### User Management
- User Registration
- User Profile Management
- Account Updates
- Account Deletion

### Authentication
- Login
- Logout
- JWT Token Generation
- JWT Validation

### Authorization
- Role Management
- Access Control
- Permission Validation

### Subscription Management
- Free Tier
- Premium Plans
- Workspace Limits
- Usage Tracking

### Payment Integration
- Stripe Checkout
- Webhook Processing
- Subscription Activation
- Subscription Cancellation

---

# 🏗️ High-Level Architecture

```text
Browser
   |
   v
API Gateway
   |
   v
Account Service
   |
   +--------------------+
   |                    |
   v                    v

PostgreSQL         JWT Generation
   |
   v

User Accounts
Subscriptions
Roles
Billing Data
```

---

# 🌐 Request Flow

## User Registration

```text
Frontend
   |
   v
API Gateway
   |
   v
Account Service
   |
   v
PostgreSQL
```

### Process

1. User submits registration form.
2. Account Service validates request.
3. Password is encrypted.
4. User record is created.
5. Response returned to client.

---

## User Login

```text
Frontend
   |
   v
API Gateway
   |
   v
Account Service
   |
   v
Database Validation
   |
   v
JWT Generation
   |
   v
Response
```

### Login Workflow

```text
User
 |
Email + Password
 |
Account Service
 |
Validate Credentials
 |
Generate JWT
 |
Return Token
```

---

## JWT Authentication Flow

```text
Frontend
    |
    | Bearer Token
    v
API Gateway
    |
    v
Account Service
    |
    v
Validate JWT
    |
    v
Authorized Request
```

---

# 🗄️ Database Architecture

The Account Service owns its dedicated database.

```text
Account DB
│
├── Users
├── Roles
├── Permissions
├── Subscriptions
├── Payments
├── Plans
└── Audit Logs
```

This follows the microservices principle:

> Each service owns its own database.

---

# 💳 Stripe Integration Flow

## Subscription Purchase

```text
User
 |
Purchase Plan
 |
Frontend
 |
Account Service
 |
Stripe Checkout
 |
Payment Success
 |
Stripe Webhook
 |
Account Service
 |
Activate Subscription
 |
Database Update
```

---

## Webhook Processing

Stripe sends events:

```text
checkout.session.completed

invoice.payment_succeeded

invoice.payment_failed

customer.subscription.deleted
```

The Account Service consumes these events and updates user subscription status accordingly.

---

# 🔐 Security Architecture

## JWT Authentication

The service generates and validates:

```text
Access Tokens
Refresh Tokens
```

Used across the platform.

---

## Secret Management

Secrets are mounted into the container through Kubernetes Secrets.

### Stored Secrets

```text
DB_PASSWORD

JWT_SECRET

STRIPE_API_KEY

STRIPE_WEBHOOK_SECRET
```

---

## Password Security

Passwords should be:

```text
BCrypt Hashed
```

before persistence.

```text
Plain Password
      |
      v
 BCrypt Hash
      |
      v
 Database
```

---

# ⚙️ Configuration Management

The service retrieves centralized configuration from:

```text
Config Server
```

using:

```text
CONFIG_SERVER_URL
```

This enables:

- Environment-specific properties
- Centralized configuration
- Dynamic updates

---

# ☁️ Kubernetes Deployment Architecture

```text
Ingress
   |
API Gateway
   |
Service
   |
Deployment
   |
Pods
```

The Account Service is exposed internally through a Kubernetes Service and receives requests from the API Gateway.

---

## Pod Lifecycle

Each pod contains:

```text
Account Service
│
├── Startup Probe
├── Readiness Probe
└── Application Runtime
```

### Startup Probe

Ensures:

```text
Application initialized
Dependencies available
Database reachable
```

---

### Readiness Probe

Ensures:

```text
Ready to accept traffic
Healthy state
```

Only healthy pods receive requests.

---

# 🏛️ Kubernetes Resources

## Deployment

Responsible for:

```text
Pod Creation
Replica Management
Rolling Updates
Self Healing
```

---

## Service

Provides:

```text
Stable DNS
Load Balancing
Service Discovery
```

---

## Service Account

```text
account-service-account
```

Used for:

```text
Pod Identity
Kubernetes Authentication
Future RBAC Extensions
```

---

## ConfigMap

Provides:

```text
FRONTEND_URL

CONFIG_SERVER_URL

Environment Settings
```

---

## Secrets

Provides:

```text
JWT_SECRET

DB_PASSWORD

STRIPE_API_KEY

STRIPE_WEBHOOK_SECRET
```

---

# 📈 End-to-End Authentication Sequence

```text
User
 |
Frontend
 |
Login Request
 |
API Gateway
 |
Account Service
 |
Validate User
 |
Generate JWT
 |
Database Lookup
 |
Return Token
 |
Frontend
 |
Store Token
 |
Subsequent Requests
 |
API Gateway
 |
Token Validation
 |
Authorized Access
```

---

# 🔄 Interaction with Other Services

The Account Service acts as a foundational service for all other platform components.

```text
Frontend
   |
API Gateway
   |
   +--------------------+
   |                    |
   v                    v

Account Service     Workspace Service
                           |
                           v

                    Intelligence Service
```

Other services depend on:

- User identity
- Subscription status
- Authorization context

provided by Account Service.

---

# 📊 Core Entities

### User

```text
id
name
email
passwordHash
role
status
```

---

### Subscription

```text
id
userId
plan
status
startDate
expiryDate
```

---

### Payment

```text
id
userId
stripePaymentId
amount
status
createdAt
```

---

# 🚀 Scalability Design

The service supports horizontal scaling.

```text
1 Pod
   ↓
5 Pods
   ↓
20 Pods
```

Stateless architecture is maintained through:

- JWT Authentication
- External PostgreSQL Storage

allowing pods to scale independently.

---

# 🎯 Design Principles

- Stateless Services
- JWT-Based Authentication
- Database per Service
- Centralized Configuration
- Cloud Native Deployment
- Horizontal Scalability
- Kubernetes First
- Secure Secret Management

---

# 👨‍💻 Part of Coding Genie Platform

The Account Service serves as the identity and subscription backbone of Coding Genie, ensuring secure user authentication, authorization, billing integration, and account lifecycle management across the platform.
