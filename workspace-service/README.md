# 🏗️ Workspace Service

> Core workspace orchestration service of Coding Genie responsible for project lifecycle management, Kubernetes preview environment provisioning, workspace persistence, and distributed workflow coordination.

![Workspace Service Architecture](./workspace_service_hld)

---

# 📖 Overview

The Workspace Service is the heart of the Coding Genie platform.

It acts as the orchestration layer between:

- Frontend
- Intelligence Service
- Kubernetes
- Preview Environments
- Redis
- Kafka
- Object Storage

The service is responsible for creating, managing, and deleting user workspaces while coordinating AI code generation and dynamic preview deployments.

In many ways, Workspace Service serves a role similar to the control plane in platforms such as:

- Replit
- Lovable
- Bolt
- CodeSandbox

---

# 🎯 Responsibilities

## Workspace Management

- Create Workspaces
- Delete Workspaces
- Update Workspaces
- List User Workspaces
- Workspace Metadata Management

---

## Preview Environment Management

- Create Preview Pods
- Create Preview Services
- Destroy Preview Environments
- Monitor Preview Status
- Register Preview Routes

---

## AI Workflow Coordination

- Publish generation requests
- Receive AI results
- Manage workspace generation lifecycle
- Handle generation failures

---

## Kubernetes Orchestration

- Create Deployments
- Create Services
- Create Namespaces
- Scale Preview Environments
- Cleanup Resources

---

## Distributed Workflow Management

- Kafka Event Publishing
- Kafka Event Consumption
- Saga Coordination
- Workspace State Management

---

# 🏗️ High-Level Architecture

```text
User
 |
Frontend
 |
API Gateway
 |
Workspace Service
 |
 +----------------------------+
 |                            |
 |                            |
 v                            v

Kafka                  Kubernetes API
 |                            |
 v                            v

Intelligence Service     Preview Pods

 |                            |
 v                            v

MinIO                   Preview Services

```

---

# 🌐 Workspace Creation Flow

## Step 1 – Create Workspace

User creates a project.

```text
Frontend
 |
API Gateway
 |
Workspace Service
```

Example:

```json
{
  "name": "Todo App",
  "prompt": "Build a React Todo Application"
}
```

---

## Step 2 – Persist Workspace

Workspace Service stores:

```text
Workspace Metadata
User Ownership
Project Configuration
Status
```

into PostgreSQL.

---

## Step 3 – Publish Generation Request

Workspace Service publishes:

```text
workspace-events
```

Kafka topic.

Example:

```json
{
  "workspaceId": "123",
  "prompt": "Build a Todo App"
}
```

---

## Step 4 – AI Generation Begins

```text
Workspace Service
      |
      v
Kafka
      |
      v
Intelligence Service
```

The Intelligence Service consumes the request and begins code generation.

---

# 🤖 AI Generation Lifecycle

```text
Workspace Service
        |
        v
workspace-events
        |
        v
Intelligence Service
        |
        v
AI Provider
        |
        v
intelligence-results
        |
        v
Workspace Service
```

---

## Status Progression

```text
CREATED
   ↓

GENERATING
   ↓

READY
```

or

```text
CREATED
   ↓

GENERATING
   ↓

FAILED
```

---

# 🚀 Preview Environment Flow

One of the most important responsibilities of Workspace Service is dynamic preview creation.

---

## Step 1 – Request Preview

```text
User
 |
Preview
 |
Workspace Service
```

---

## Step 2 – Create Kubernetes Resources

Workspace Service uses Kubernetes APIs to create:

```text
Deployment

Service

ConfigMap

Secrets
```

inside:

```text
coding-genie-previews
```

namespace.

---

## Step 3 – Preview Pod Starts

```text
Workspace Service
       |
       v
Kubernetes
       |
       v
Preview Pod
```

Example:

```text
preview-123
```

---

## Step 4 – Preview Service Created

```text
preview-123-svc
```

becomes the stable network endpoint.

---

## Step 5 – Route Registration

Workspace Service registers:

```text
route:preview-123.previews.codinggenie.in
```

inside Redis.

```text
preview-123.previews.codinggenie.in
             |
             v

preview-123-svc:5173
```

---

## Step 6 – User Access

```text
User
 |
preview-123.previews.codinggenie.in
 |
Ingress
 |
Proxy
 |
Redis Lookup
 |
Preview Service
 |
Preview Pod
```

---

# ☸️ Kubernetes Integration

Workspace Service acts as a Kubernetes client.

---

## Managed Resources

### Deployment

```text
Preview Containers
```

---

### Service

```text
Stable Networking
```

---

### ConfigMap

```text
Runtime Configuration
```

---

### Secret

```text
Environment Secrets
```

---

### Namespace

```text
Preview Isolation
```

---

# 📨 Kafka Integration

Workspace Service both produces and consumes events.

---

## Produced Topics

### workspace-events

Purpose:

```text
AI Generation Requests
Workspace Creation Events
Workspace Update Events
```

---

## Consumed Topics

### intelligence-results

Purpose:

```text
Generation Completed
Generation Failed
Artifacts Ready
```

---

### saga-response

Purpose:

```text
Distributed Transactions
Compensation Handling
Workflow Coordination
```

---

# 🗄️ Database Architecture

Workspace Service owns its database.

---

## Workspace Table

```text
id
userId
name
status
createdAt
updatedAt
```

---

## Preview Table

```text
id
workspaceId
url
status
createdAt
```

---

## Generation Table

```text
id
workspaceId
generationStatus
artifactLocation
```

---

# ⚡ Redis Usage

Redis provides fast lookups.

---

## Preview Routing

```text
route:preview-123.previews.codinggenie.in

→ preview-123-svc:5173
```

---

## Workspace State

```text
workspace:123

→ READY
```

---

## Session Data

```text
Temporary Workspace Metadata
```

---

# 🪣 MinIO Integration

Generated projects are stored in MinIO.

Structure:

```text
workspace-123/

├── frontend
├── backend
├── assets
└── docs
```

Workspace Service retrieves these artifacts when preparing preview environments.

---

# 🔐 Security Architecture

## Service Account

```text
workspace-service-account
```

Provides:

```text
Kubernetes Authentication
Pod Identity
RBAC Access
```

---

## Role-Based Access Control

Workspace Service is allowed to:

```text
Create Deployments

Create Services

Create Pods

Delete Resources

Manage Preview Namespace
```

---

## Secrets

```text
DB_PASSWORD

JWT_SECRET

MINIO_ACCESS_KEY

MINIO_SECRET_KEY

REDIS_PASSWORD
```

---

# ⚙️ Configuration Management

Configuration is retrieved from:

```text
Config Server
```

using:

```text
CONFIG_SERVER_URL
```

---

## Shared Configuration

Provided through ConfigMaps:

```text
REDIS_URL

KAFKA_URL

MINIO_URL

FRONTEND_URL
```

---

# 📈 End-to-End Sequence

```text
User
 |
Frontend
 |
API Gateway
 |
Workspace Service
 |
Create Workspace
 |
Kafka
 |
Intelligence Service
 |
AI Provider
 |
MinIO
 |
Kafka
 |
Workspace Service
 |
Create Preview Environment
 |
Kubernetes
 |
Preview Pod
 |
Redis Route Registration
 |
Proxy
 |
User Accesses Preview
```

---

# 🌍 Interaction with Other Services

```text
Frontend
    |
API Gateway
    |
    +-------------------------------+
    |                               |
    v                               v

Account Service           Workspace Service
                                   |
                                   |
                                   v

                          Intelligence Service
                                   |
                                   v

                                MinIO

```

Workspace Service acts as the central orchestrator connecting all major platform components.

---

# 🚀 Scalability Design

The service is horizontally scalable.

```text
1 Pod
  ↓

5 Pods
  ↓

20 Pods
```

Stateless architecture is achieved through:

- PostgreSQL
- Redis
- Kafka
- Kubernetes

allowing any pod to process incoming requests.

---

# 🎯 Design Principles

- Event-Driven Architecture
- Kubernetes-Native Design
- Workspace Isolation
- Distributed Coordination
- Stateless Services
- Horizontal Scalability
- Fault Tolerance
- Preview Environment Automation

---

# 👨‍💻 Part of Coding Genie Platform

The Workspace Service serves as the orchestration engine of Coding Genie, coordinating AI generation, workspace lifecycle management, Kubernetes preview provisioning, and distributed workflows across the platform.

Without Workspace Service, the platform would lose its ability to create, manage, and expose AI-generated applications in isolated preview environments.
