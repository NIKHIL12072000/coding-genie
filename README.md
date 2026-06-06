![Alt text](./GKE_Architecture.png "GKE Architecture")



# 1. High-Level GKE Architecture

```text
                           INTERNET
                               |
                               |
                    codinggenie.in DNS
                               |
                               v
                    NGINX Ingress Controller
                               |
      --------------------------------------------------
      |                     |                         |
      |                     |                         |
      v                     v                         v

 codinggenie.in       api.codinggenie.in    *.previews.codinggenie.in

      |                     |                         |
      v                     v                         v

Frontend Service      API Gateway           Coding Genie Proxy
      |                     |                         |
      |                     |                         |
      |              -----------------               |
      |              |       |       |               |
      |              v       v       v               |
      |         Account  Workspace  Intelligence     |
      |         Service   Service    Service         |
      |              |       |       |               |
      |              -----------------               |
      |                      |                       |
      |                      v                       |
      |                Config Service               |
      |                                              |
      ------------------------------------------------
                             |
      ------------------------------------------------
      |            |             |                   |
      v            v             v                   v

   Redis        Kafka       PGVector             MinIO
```

---

# 2. Namespace Layout

You have separated workloads into two namespaces:

```text
GKE Cluster
|
├── coding-genie
│    ├── Frontend
│    ├── API Gateway
│    ├── Account Service
│    ├── Workspace Service
│    ├── Intelligence Service
│    ├── Config Service
│    ├── Redis
│    ├── Kafka
│    ├── PGVector
│    ├── MinIO
│    └── Proxy
│
└── coding-genie-previews
     ├── Runner Pool
     ├── Dynamic Preview Pods
     └── Sandbox Policies
```

---

# 3. Request Flow (Main Application)

```text
User
 |
 v
https://codinggenie.in
 |
 v
Ingress
 |
 v
Frontend Service
 |
 v
API Gateway
 |
 +-----------------------------+
 |                             |
 v                             v

Account Service        Workspace Service
                              |
                              |
                              v

                       Intelligence Service

```

The API Gateway acts as the single entry point for backend APIs.

---

# 4. Preview Environment Flow 


## Step 1

User creates a project.

```text
User
 |
 v
Workspace Service
```

---

## Step 2

Workspace Service creates preview resources.

Because it has:

* ServiceAccount
* Role
* RoleBinding

it can manage resources inside:

```text
coding-genie-previews
```

namespace.

---

## Step 3

Runner Pool launches preview containers.

```text
Workspace Service
         |
         v
   Runner Pool
         |
         v
 Preview Pod
```

---

## Step 4

Preview gets a URL.

Example:

```text
preview-123.previews.codinggenie.in
```

---

## Step 5

Workspace Service stores mapping in Redis.

```text
preview-123.previews.codinggenie.in
                |
                v

route:preview-123.previews.codinggenie.in
                |
                v

preview-123-svc:5173
```

---

## Step 6

Wildcard Proxy routes traffic.

From your uploaded proxy source:

* Reads hostname
* Looks up Redis
* Finds target preview service
* Proxies HTTP/WebSocket traffic



```text
User
 |
 v
preview-123.previews.codinggenie.in
 |
 v
Ingress
 |
 v
Coding Genie Proxy
 |
 v
Redis Lookup
 |
 v
Preview Service
 |
 v
Preview Pod
```

---

# 5. Preview Routing Architecture

```text
                      *.previews.codinggenie.in
                                   |
                                   v
                              Ingress
                                   |
                                   v
                        Coding Genie Proxy
                                   |
                      Redis Route Lookup
                                   |
             ----------------------------------
             |                |               |
             v                v               v

     preview-1-svc    preview-2-svc   preview-3-svc
             |                |               |
             v                v               v

         Preview Pod      Preview Pod     Preview Pod
```

---

# 6. Data Layer

```text
                    Backend Services
                           |
      ------------------------------------------------
      |                 |                |           |
      v                 v                v           v

    Redis            Kafka          PGVector      MinIO
      |                 |                |           |
      |                 |                |           |
 Preview Routes     Events        AI Embeddings   Files
 Session Data      Messaging      Vector Search   Uploads
```

### Redis

Used for:

* Preview URL mapping
* Fast lookups

### Kafka

Used for:

* Async communication
* Event-driven workflows

### PGVector

Used for:

* Embeddings
* RAG
* Semantic search

### MinIO

Used for:

* Generated code
* Assets
* Project storage

---

# 7. Security Architecture

Implemented security in two layers.

## Network Policies

```text
Production Namespace
       |
       +---- allow internal traffic
       |
       +---- allow ingress traffic
       |
       +---- allow preview access to MinIO
```

---

## Sandbox Isolation

```text
coding-genie-previews
         |
         v
strict-preview-sandbox
```

Preview containers cannot freely communicate with the rest of the cluster.

This is exactly what should happen in an AI code-generation platform.

---

# 8. What Happens When User Clicks "Preview"

```text
User clicks Preview
         |
         v
Workspace Service
         |
         v
Creates Preview Pod
         |
         v
Creates Preview Service
         |
         v
Stores Route in Redis
         |
         v
Returns URL

preview-123.previews.codinggenie.in
         |
         v
User Opens URL
         |
         v
Ingress
         |
         v
Proxy
         |
         v
Redis Lookup
         |
         v
Preview Pod
```

---
