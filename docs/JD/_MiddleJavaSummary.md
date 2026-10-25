# Tổng hợp JD Java Middle (2–4 năm)

Nguồn (12 JD, mỗi JD có file riêng trong thư mục này, trừ ghi chú):
KMS, Momo, Axon, OmiGroup, Amaris (Junior/Middle + Middle), Hitachi, GoTymeX, HCLTech, và MetLife, NAB, Saigon Technology, VPBank (đọc trên LinkedIn VN, chưa lưu file; VPBank 5 năm chỉ để tham khảo).

| Công ty | Kinh nghiệm | Điểm nổi bật |
|---|---|---|
| Momo | 1.5+ năm | Spring AOP/Data/MVC, Redis, DSA, system design, fintech |
| KMS | 2+ năm | Spring Security, JPA/Hibernate, microservices, Azure/AWS |
| Axon (Junior) | 2+ năm hoặc bằng CS | AI-assisted dev, Java/Scala/Go/C# |
| Hitachi (Full-stack) | 2+ năm | Spring Boot + Angular, Elasticsearch, AWS, tích hợp LLM/RAG |
| Amaris (Jr/Mid) | 2.5+ năm | Microservices, Spring Security/JWT, HTTPS, banking |
| OmiGroup | 2.5+ năm | OAuth2/OIDC/JWT/Keycloak, Docker, CI/CD, AI coding tools |
| GoTymeX | "a few years" (mid) | Microservices, Kafka, Redis, AWS, DB schema design, banking |
| Amaris (Middle) | 3+ năm | Như trên, thêm Oracle PL/SQL |
| HCLTech (ANZ) | 4+ năm | Spring ecosystem, GCP/K8s, Kafka, Testcontainers, AI SDLC |
| MetLife | 4+ năm | JUnit/Mockito, CI/CD, DevSecOps, production support |
| NAB | 4+ năm | AWS (Lambda, ECS), Docker/K8s, Jenkins, tiếng Anh tốt |
| Saigon Tech | 4+ năm | AWS microservices, Kafka/RabbitMQ, SOLID, design patterns |
| VPBank (tham khảo) | 5+ năm | DDD, CQRS, event sourcing, Kafka/Redis, ELK/Prometheus/Grafana |

## Kỹ năng theo mức độ xuất hiện (ước lượng tay trên 12 JD)

### Gần như bắt buộc (10–12/12)
- Java Core: OOP, Collections, Exception, Java 8+
- Spring Boot + Spring MVC, RESTful API
- SQL / RDBMS (MySQL, PostgreSQL, Oracle, SQL Server)
- Git

### Rất phổ biến (6–9/12)
- Spring Data + JPA/Hibernate
- Spring Security: authN/authZ, JWT (OAuth2/OIDC/Keycloak ở vài JD)
- Microservices
- Docker, CI/CD (Jenkins, GitLab CI, GitHub Actions, Maven/Gradle)
- Cloud: AWS (nhiều nhất), GCP, Azure
- Unit test: JUnit, Mockito
- Tiếng Anh giao tiếp
- Debug/production support, tối ưu hiệu năng

### Thường là điểm cộng, ở level cao hơn thì thành bắt buộc (3–5/12)
- Kafka / RabbitMQ / Pub/Sub (messaging, event-driven)
- Redis (cache)
- Kubernetes
- Testcontainers, contract testing
- Design patterns, SOLID, system design, DSA
- Domain banking/fintech (rất nhiều JD)
- AI coding tools (Copilot, Claude, Cursor): xuất hiện ở 5/12 JD, kể cả JD middle bắt buộc có ví dụ dùng thực tế

### Hiếm hơn (1–2/12)
- Elasticsearch, GraphQL, tích hợp LLM/RAG (Hitachi)
- Terraform/Helm/IaC
- Saga, CQRS, event sourcing, outbox pattern
- Spring AOP, WebFlux, JVM tuning
- ELK/Prometheus/Grafana
- Frontend: Angular/React/Vue, HTML/CSS/JS
- NoSQL: MongoDB, Cassandra

## Nhận xét
- Mức "2 năm" thực tế là Spring Boot + JPA + REST + SQL vững, biết Docker và một chút Spring Security/JWT.
- Từ 3–4 năm, JD đòi thêm messaging (Kafka), cloud, K8s, testing nâng cao (Testcontainers) và kinh nghiệm production.
- Tiếng Anh và AI coding tools là yêu cầu mới, tăng nhanh ở JD 2026.
- Banking/fintech là domain xuất hiện nhiều nhất.

## Gợi ý lộ trình ưu tiên
1. Nền: Java 8–21 (stream, lambda, concurrency), OOP/SOLID, design patterns, SQL.
2. Spring Boot: REST, validation, exception handling, Spring Data JPA (N+1, transaction), Spring Security + JWT.
3. Chất lượng: JUnit 5, Mockito, Testcontainers, Git flow.
4. Hạ tầng: Docker, CI/CD, Redis cache, Kafka/RabbitMQ cơ bản.
5. Microservices + 1 cloud (AWS): API gateway, service-to-service, config, observability.
6. Dùng AI coding tool có kiểm chứng đầu ra; thử tích hợp LLM API trong một service Spring Boot.
