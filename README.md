# 🚀 Coding Genie    

> An AI-powered cloud development platform that enables users to generate, edit, preview, and manage full-stack applications in isolated Kubernetes-powered workspaces.

![Coding Genie Architecture](./coding_genie_full_hld)

[Coding Genie](http://codinggenie.in/)

---

## 📖 Overview

Coding Genie is a distributed, cloud-native AI coding platform inspired by tools such as Lovable, Replit, and Bolt. It combines AI-assisted code generation, microservices architecture, Kubernetes-based sandbox environments, and real-time preview deployments into a unified developer experience.

Users can create projects using natural language, collaborate through isolated workspaces, generate and modify code using AI, and instantly preview applications running in dedicated Kubernetes environments.

---

## ✨ Key Features

### 🤖 AI-Powered Development
- Generate applications using natural language prompts.
- AI-assisted code modifications and enhancements.
- Intelligent project scaffolding and code generation.
- Vector-search powered context retrieval using PGVector.

### 🏗️ Distributed Microservices Architecture
- API Gateway for centralized routing and security.
- Workspace Service for project and environment lifecycle management.
- Account Service for user management and authentication.
- Intelligence Service for AI orchestration and retrieval workflows.
- Config Server for centralized configuration management.

### ☁️ Kubernetes-Native Infrastructure
- Runs on Google Kubernetes Engine (GKE).
- Namespace-based workload isolation.
- Dynamic preview environment provisioning.
- Network policies for sandbox security.
- Scalable and production-ready deployment model.

### ⚡ Real-Time Preview Environments
- Dedicated preview pods created per workspace.
- Wildcard subdomain routing for previews.
- Redis-backed dynamic route discovery.
- Automatic traffic routing through a custom preview proxy.

### 📦 Modern Cloud Stack
- Kafka for event-driven communication.
- Redis for caching and preview routing.
- MinIO for object storage.
- PGVector for AI embeddings and semantic search.
- NGINX Ingress for external traffic management.

---

## 🏛️ High-Level Architecture

### Request Flow

```text
User
  │
  ▼
Frontend (React)
  │
  ▼
API Gateway
  │
  ├────────► Account Service
  │
  ├────────► Workspace Service
  │                │
  │                ├── Redis
  │                ├── Kafka
  │                └── Kubernetes API
  │
  └────────► Intelligence Service
                   │
                   ├── PGVector
                   ├── MinIO
                   └── AI Models
```

---

## 🔥 Preview Environment Architecture

```text
User
 │
 ▼
preview-123.previews.codinggenie.in
 │
 ▼
NGINX Ingress
 │
 ▼
Wildcard Preview Proxy
 │
 ▼
Redis Route Lookup
 │
 ▼
Preview Service
 │
 ▼
Preview Pod
```

Each workspace receives an isolated preview environment running inside a dedicated Kubernetes namespace with strict network policies.

---

## 🧩 System Components

| Component | Responsibility |
|------------|---------------|
| Frontend | User Interface and Workspace Experience |
| API Gateway | Routing, Authentication, Request Management |
| Account Service | User Accounts, Authentication, Authorization |
| Workspace Service | Workspace and Preview Lifecycle |
| Intelligence Service | AI Workflows, Code Generation, Retrieval |
| Config Server | Centralized Configuration |
| Redis | Routing Cache, Fast Lookups |
| Kafka | Event Streaming |
| PGVector | Vector Embeddings & Semantic Search |
| MinIO | Object Storage |
| GKE | Container Orchestration |
| NGINX Ingress | Traffic Routing |

---

## 🔐 Security

- Namespace-level isolation
- Kubernetes Network Policies
- Service Account based access control
- JWT Authentication
- Preview sandbox restrictions
- Internal service-to-service communication controls

---

## 🚀 Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Cloud Config
- Eureka Service Discovery
- Kafka
- Redis

### Frontend
- React
- TypeScript
- Vite

### Infrastructure
- Google Kubernetes Engine (GKE)
- NGINX Ingress
- Docker
- Kubernetes
- GitHub Actions

### Storage & AI
- PGVector
- MinIO
- Redis
- Kafka

---

## 🎯 Vision

Coding Genie aims to provide a complete AI-native development experience where developers can:

- Describe applications using natural language.
- Generate production-ready code.
- Instantly preview applications.
- Iterate with AI assistance.
- Deploy cloud-native applications seamlessly.

---

## 👨‍💻 Author

**Nikhil Dachepally**

Building next-generation AI-powered developer tooling with cloud-native architecture and intelligent automation.
