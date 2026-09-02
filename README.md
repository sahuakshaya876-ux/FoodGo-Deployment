# FoodGo — Full-Stack Food Delivery Platform

FoodGo is a Swiggy/Zomato-style food delivery platform built as a DevOps portfolio
project. It demonstrates a clean, monolithic Spring Boot backend and a modern
React frontend, wired end-to-end into a real CI/CD pipeline: GitHub → Jenkins →
Maven → SonarQube → Trivy → Docker → Amazon ECR → Ansible → AWS EKS →
Kubernetes rolling deployment with health checks.

---

## 1. Project Overview

FoodGo supports three roles:

- **Customer** — browse restaurants and food, manage cart/wishlist/addresses,
  place orders, track status, cancel, and leave reviews.
- **Restaurant Owner** — register a restaurant, manage its menu, accept/reject
  and progress orders, and view sales analytics.
- **Admin** — approve/reject restaurants, manage users, view all orders, and
  monitor platform-wide statistics.

---

## 2. Features

### Customer
Register/login, profile management, address book, search & filter restaurants
and food, browse categories, view restaurant/menu/food details, cart
(add/update/remove/clear), wishlist, checkout with mock payments, place/track/
cancel orders, and rate/review restaurants.

### Restaurant Owner
Dashboard (orders, revenue, popular items), restaurant profile management,
menu CRUD with availability toggles, incoming order management with status
transitions, and sales analytics.

### Admin
Platform dashboard (users, restaurants, orders, revenue), user management
(enable/disable), restaurant approval workflow, category management, and
order oversight.

---

## 3. Architecture

```
                            ┌─────────────────────────┐
                            │        Browser           │
                            └────────────┬─────────────┘
                                         │ HTTPS
                            ┌────────────▼─────────────┐
                            │   React + Vite Frontend   │
                            │   (served by Nginx)       │
                            └────────────┬─────────────┘
                                         │ REST /api/**  (JWT bearer)
                            ┌────────────▼─────────────┐
                            │  Spring Boot Monolith     │
                            │  ─────────────────────    │
                            │  controller → service     │
                            │  → repository → entity     │
                            │  Spring Security + JWT     │
                            │  GlobalExceptionHandler    │
                            └────────────┬─────────────┘
                                         │ JDBC
                            ┌────────────▼─────────────┐
                            │        PostgreSQL          │
                            └────────────────────────────┘

CI/CD pipeline:
GitHub → Jenkins → Maven build/test → SonarQube analysis + Quality Gate
       → Trivy filesystem scan → Docker build → Trivy image scan
       → AWS ECR push → Ansible → AWS EKS → Kubernetes rolling deployment
       → readiness/liveness health checks
```

---

## 4. Technology Stack

**Backend:** Java 21, Spring Boot 3.3, Spring Web, Spring Data JPA, Spring
Security, JWT (jjwt), Bean Validation, PostgreSQL, Lombok, Spring Boot
Actuator, JUnit 5, Mockito, Maven.

**Frontend:** React 19, Vite, JavaScript, React Router, Axios, Tailwind CSS,
lucide-react icons.

**DevOps:** Docker, Jenkins, SonarQube + SonarScanner, Trivy, Amazon ECR,
Ansible, AWS EKS, Kubernetes.

---

## 5. Project Structure

```
foodgo/
├── backend/                  # Spring Boot monolith
│   ├── pom.xml
│   ├── Dockerfile
│   ├── sonar-project.properties
│   └── src/
│       ├── main/java/com/foodgo/
│       │   ├── auth/         # register/login, JWT issuance
│       │   ├── user/         # user entity, profile
│       │   ├── address/      # customer addresses
│       │   ├── restaurant/   # restaurant CRUD + approval status
│       │   ├── category/     # food categories
│       │   ├── food/         # food items, search/filter
│       │   ├── cart/         # cart & cart items
│       │   ├── order/        # order placement, pricing, status flow
│       │   ├── payment/      # mock payment system
│       │   ├── review/       # ratings & reviews
│       │   ├── wishlist/     # customer wishlist
│       │   ├── admin/        # admin dashboard & management
│       │   ├── security/     # JWT filter, Spring Security config
│       │   ├── exception/    # GlobalExceptionHandler
│       │   └── common/       # ApiResponse, BaseEntity
│       └── test/java/com/foodgo/   # unit tests
│
├── frontend/                 # React + Vite + Tailwind SPA
│   ├── package.json
│   ├── Dockerfile
│   ├── nginx.conf
│   └── src/
│       ├── api/               # axios client + endpoint modules
│       ├── context/           # Auth & Cart context providers
│       ├── components/        # shared UI (Navbar, cards, states)
│       └── pages/
│           ├── auth/          # login, register
│           ├── customer/      # home, restaurants, cart, checkout, orders...
│           ├── restaurant/    # owner dashboard, menu, orders, analytics
│           └── admin/         # admin dashboard, users, restaurants, orders
│
├── k8s/                       # Kubernetes manifests
├── ansible/                   # Ansible deployment automation
├── Jenkinsfile                # 13-stage CI/CD pipeline
├── docker-compose.yml         # Local development only
└── README.md
```

---

## 6. Local Setup

### Prerequisites
- Java 21, Maven 3.9+
- Node.js 20+, npm
- PostgreSQL 16 (or Docker)
- Docker (optional, for containerized local run)

### Option A — Run with Docker Compose (fastest)

```bash
docker compose up --build
```

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api
- Postgres: localhost:5432

### Option B — Run natively

**1. Start PostgreSQL** (or use Docker: `docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=foodgo -p 5432:5432 postgres:16-alpine`)

**2. Backend**
```bash
cd backend
mvn clean test        # run unit tests
mvn clean package      # build the jar
java -jar target/foodgo-backend.jar
```
Backend runs on `http://localhost:8080`.

**3. Frontend**
```bash
cd frontend
cp .env.example .env   # adjust VITE_API_BASE_URL if needed
npm install
npm run dev
```
Frontend runs on `http://localhost:5173`.

---

## 7. Database Setup

The schema is created automatically via `spring.jpa.hibernate.ddl-auto=update`
(configurable). For a fresh local database:

```sql
CREATE DATABASE foodgo;
```

No manual migrations are required for local development; Hibernate creates/
updates tables for `User`, `Restaurant`, `FoodCategory`, `FoodItem`, `Cart`,
`CartItem`, `Address`, `Order`, `OrderItem`, `Payment`, `Review`, and
`Wishlist` on startup.

---

## 8. Environment Variables

No secrets are hard-coded anywhere in the codebase — everything is read from
environment variables with safe local defaults.

| Variable            | Description                              | Local default                              |
|---------------------|-------------------------------------------|---------------------------------------------|
| `DB_URL`            | JDBC connection string                    | `jdbc:postgresql://localhost:5432/foodgo`   |
| `DB_USERNAME`       | Database username                         | `postgres`                                   |
| `DB_PASSWORD`       | Database password                         | `postgres`                                   |
| `DDL_AUTO`          | Hibernate schema strategy                 | `update`                                     |
| `JWT_SECRET`        | HMAC signing secret for JWTs              | dev placeholder — **must** be overridden in prod |
| `JWT_EXPIRATION_MS` | JWT lifetime in milliseconds              | `86400000` (24h)                             |
| `SERVER_PORT`       | Backend HTTP port                         | `8080`                                       |
| `VITE_API_BASE_URL` | Frontend → backend base URL               | `http://localhost:8080/api`                  |

---

## 9. Maven Commands

```bash
mvn clean test                     # unit tests
mvn clean package                  # build the executable jar
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<token>             # SonarQube analysis
```

---

## 10. Docker Commands

```bash
# Backend
cd backend
docker build -t foodgo-backend:latest .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/foodgo \
  -e JWT_SECRET=change-me \
  foodgo-backend:latest

# Frontend
cd frontend
docker build -t foodgo-frontend:latest --build-arg VITE_API_BASE_URL=/api .
docker run -p 5173:80 foodgo-frontend:latest
```

---

## 11. Kubernetes Deployment

Manifests live in `k8s/`:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/postgres.yaml        # optional, skip if using RDS
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml

kubectl get pods -n foodgo
kubectl get svc -n foodgo
```

Both Deployments run **2 replicas** with a `RollingUpdate` strategy
(`maxUnavailable: 1`, `maxSurge: 1`), and `YOUR_ECR_REGISTRY/foodgo-*:IMAGE_TAG`
placeholders are substituted with the real ECR image URI at deploy time
(see Ansible, below) — never hard-coded.

---

## 12. Ansible Deployment

`ansible/deploy.yml` automates the EKS rollout:

```bash
cd ansible
ansible-playbook -i inventory deploy.yml \
  -e image_tag=42 \
  -e ecr_registry=123456789012.dkr.ecr.ap-south-1.amazonaws.com \
  -e aws_region=ap-south-1 \
  -e eks_cluster_name=foodgo-cluster
```

It verifies `kubectl`/AWS CLI, updates the kubeconfig for the target EKS
cluster, applies all manifests with the correct image tags substituted in,
waits for both rollouts, and verifies pods/services — failing the play (and
therefore the Jenkins stage) if anything doesn't come up healthy.

---

## 13. Jenkins Pipeline

The `Jenkinsfile` defines a 13-stage pipeline:

1. **Checkout** — pull source from GitHub
2. **Maven Clean Package** — compile & package the backend
3. **Unit Tests** — `mvn test`, published via JUnit
4. **SonarQube Analysis** — static analysis via the Maven Sonar plugin
5. **SonarQube Quality Gate** — pipeline aborts if the gate fails
6. **Trivy Filesystem Scan** — fails the build on HIGH/CRITICAL findings
7. **Docker Build** — builds backend & frontend images
8. **Trivy Docker Image Scan** — scans both built images
9. **AWS ECR Login** — authenticates Docker to ECR
10. **Docker Push to ECR** — pushes both images, tagged with `BUILD_NUMBER`
11. **Ansible Deployment** — runs `ansible/deploy.yml` against EKS
12. **Kubernetes Rollout Status** — confirms rollout completion
13. **Health Check** — curls `/actuator/health` inside a live backend pod

No AWS credentials, tokens, or passwords are stored in the Jenkinsfile —
everything is pulled from Jenkins Credentials at runtime.

---

## 14. SonarQube

The backend's `pom.xml` bundles the JaCoCo and `sonar-maven-plugin` so
`mvn clean verify sonar:sonar` produces coverage + static analysis in one
step. `sonar-project.properties` is provided for SonarScanner-CLI users.
The Quality Gate is enforced in the pipeline and is never bypassed.

---

## 15. Trivy

Both `trivy fs .` (source + dependency scan) and
`trivy image foodgo-backend:latest` / `foodgo-frontend:latest` (container
image scans) are wired into the Jenkins pipeline. Runtime images use minimal
bases (`eclipse-temurin:21-jre-alpine`, `nginx:1.27-alpine`) to keep the
attack surface — and Trivy findings — low.

---

## 16. AWS ECR

Images are tagged `<ECR_REGISTRY>/foodgo-backend:<BUILD_NUMBER>` and
`<ECR_REGISTRY>/foodgo-frontend:<BUILD_NUMBER>` and pushed after passing both
Trivy scans. Create the repositories once:

```bash
aws ecr create-repository --repository-name foodgo-backend
aws ecr create-repository --repository-name foodgo-frontend
```

---

## 17. AWS EKS

Provision a cluster (outside the scope of this repo, e.g. via `eksctl` or
Terraform), then point Ansible/Jenkins at it via `EKS_CLUSTER_NAME` and
`AWS_REGION`. The pipeline calls `aws eks update-kubeconfig` before every
`kubectl`/Ansible interaction, so no static kubeconfig needs to be checked in.

---

## 18. Health Checks

- Spring Boot Actuator exposes `GET /actuator/health` → `{"status":"UP"}`,
  used by both the Docker `HEALTHCHECK` and the Kubernetes
  readiness/liveness probes.
- The Nginx-served frontend exposes `GET /healthz` → `200 ok`, used the same way.

---

## 19. Rolling Deployment

Both Kubernetes Deployments use `RollingUpdate` with `maxUnavailable: 1` and
`maxSurge: 1` across 2 replicas, so a new version is deployed pod-by-pod with
zero downtime and instant rollback (`kubectl rollout undo`) if a probe fails.

---

## 20. Screenshots

_Add UI screenshots here once deployed — e.g. Home page, Restaurant detail,
Checkout, Restaurant Owner dashboard, and Admin dashboard._

```
docs/screenshots/
├── home.png
├── restaurant-detail.png
├── checkout.png
├── restaurant-dashboard.png
└── admin-dashboard.png
```

---

## License

This project was built as a personal DevOps learning/portfolio project and is
provided as-is for educational purposes.
