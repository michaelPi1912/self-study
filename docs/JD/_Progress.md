# Theo dõi tiến độ

Thang 0–3 (xem _SelfAssessment.md): 0 = chưa biết; 1 = biết khái niệm, hay phải tra; 2 = làm được, giải thích được "tại sao"; 3 = thành thạo, thiết kế và nêu trade-off.
Mục tiêu middle: nhóm A, B, C ≥ 2; nhóm D ≥ 1–2; nhóm E ≥ 1–2. Chi tiết từng bước nằm trong _StudyPlan.md.

Ghi chú cột "Ban đầu": điểm lấy từ vòng phỏng vấn thử (_AssessmentResult.md, câu số trong ngoặc). Ô trống nghĩa là chưa đo, cần tự chấm hoặc kiểm tra ở vòng sau. Mục chưa đo không được tính vào trung bình.

## Điểm theo chủ đề

### A. Bắt buộc

| Mục | Ban đầu (2026-10-18) | Lần 2 | Lần 3 | Bước học |
|---|---|---|---|---|
| Java core: HashMap, equals/hashCode | 2 (câu 1) | | | 2 |
| Java 8+: Stream, Optional | 1 (câu 3) | | | 2 |
| Collection: fail-fast iterator | 1 (câu 2) | | | 2 |
| OOP/SOLID (SRP) | 1 (câu 7) | | | 3 |
| Exception: checked vs unchecked, wrap | | | | 13 |
| Concurrency: race condition, atomic | 1 (câu 4) | | | 1 |
| SQL: join, group by | 1 (câu 5) | | | 4 |
| SQL: index khi nào không hiệu quả | 2 (câu 6) | | | 4 |
| Git: conflict, merge vs rebase | 1 (câu 8) | | | 5 |

### B. Spring (trọng tâm)

| Mục | Ban đầu | Lần 2 | Lần 3 | Bước học |
|---|---|---|---|---|
| IoC/DI, thread-safety của singleton | 1 (câu 11) | | | 9 |
| Bean lifecycle, scope | | | | 14 |
| MVC/REST: validation, ControllerAdvice, DTO vs entity | | | | 7 |
| Spring Data JPA: N+1, LAZY vs EAGER | 2 (câu 10) | | | 6 |
| Transaction: proxy, rollback, self-invocation | 2 (câu 9) | | | 6 |
| Security + JWT | 2 (câu 12) | | | 8 |
| Config/Profile, secret | | | | 14 |
| AOP | | | | 14 |

### C. Chất lượng và hạ tầng

| Mục | Ban đầu | Lần 2 | Lần 3 | Bước học |
|---|---|---|---|---|
| Unit test: JUnit 5, Mockito | | | | 10 |
| Integration test: @SpringBootTest, Testcontainers | | | | 10 |
| Docker, docker-compose | | | | 11 |
| CI/CD | | | | 11 |
| Redis, caching | | | | 12 |
| Logging, debug lỗi production | | | | 13 |
| Performance: query chậm, profile | | | | 4, 15 |

### D. Điểm cộng

| Mục | Ban đầu | Lần 2 | Lần 3 | Bước học |
|---|---|---|---|---|
| Kafka/RabbitMQ (retry, idempotency) | | | | 12 |
| Microservices | | | | 19 |
| Cloud (AWS hoặc tương đương) | | | | 19 |
| Kubernetes cơ bản | | | | 19 |
| Design patterns | | | | 17 |
| System design cơ bản | | | | 17 |
| Frontend cơ bản | | | | ngoài kế hoạch |

### E. Mở rộng (bạn chấp thuận thêm ngày 2026-10-18)

| Mục | Ban đầu | Lần 2 | Lần 3 | Bước học |
|---|---|---|---|---|
| Java 17/21: record, sealed, pattern matching, virtual thread | | | | 15 |
| JVM: heap, GC, memory leak | | | | 15 |
| Isolation level, locking (optimistic/pessimistic) | | | | 16 |
| Schema design, Flyway, connection pool | | | | 16 |
| Pagination, idempotency, versioning, OpenAPI | | | | 18 |
| Timeout, retry, circuit breaker | | | | 18 |
| Observability: metrics, tracing, health check | | | | 19 |
| AI coding tools có kiểm chứng đầu ra | | | | 20 |
| Gọi LLM API từ Spring Boot, RAG cơ bản | | | | 20 |
| Tiếng Anh kỹ thuật (đọc tài liệu, viết PR, giao tiếp) | | | | tự rèn hằng ngày |

Trung bình trên 12 mục đã đo: **1.42** (ban đầu). Trung bình lần 2: ____ . Số mục đã đo / tổng: 12/41.

## Checklist các bước

Ưu tiên: 1, 2, 4, 6, 8 trước. Giai đoạn 5 (bước 13–20) xen kẽ với giai đoạn 4.

| Bước | Nội dung | Ưu tiên | Bắt đầu | Xong | Đã qua tiêu chí | Điểm tự chấm sau |
|---|---|---|---|---|---|---|
| 1 | Concurrency | Cao | 2026-10-19 | | [ ] | |
| 2 | Collection và Java 8+ | Cao | | | [ ] | |
| 3 | OOP/SOLID | Thường | | | [ ] | |
| 4 | SQL | Cao | | | [ ] | |
| 5 | Git | Thường | | | [ ] | |
| 6 | JPA, N+1, Transaction | Cao | | | [ ] | |
| 7 | REST sạch | Thường | | | [ ] | |
| 8 | Spring Security + JWT | Cao | | | [ ] | |
| 9 | DI và thread-safety | Thường | | | [ ] | |
| 10 | Testing | Sau | | | [ ] | |
| 11 | Docker và CI | Sau | | | [ ] | |
| 12 | Redis và messaging | Sau | | | [ ] | |
| 13 | Exception và logging | Thường | | | [ ] | |
| 14 | Spring core: lifecycle, config, AOP | Thường | | | [ ] | |
| 15 | Java 17/21 và JVM | Thường | | | [ ] | |
| 16 | Dữ liệu nâng cao: isolation, lock, Flyway | Cao | | | [ ] | |
| 17 | Design pattern và system design | Thường | | | [ ] | |
| 18 | API nâng cao | Thường | | | [ ] | |
| 19 | Microservices, observability, cloud | Sau | | | [ ] | |
| 20 | AI trong công việc | Cao | | | [ ] | |

## Nhật ký tuần

Mỗi tuần 3 dòng: làm được gì, kẹt ở đâu, tuần sau làm gì.

### Tuần 1 (bắt đầu: 2026-10-19)
- Làm được:
- Kẹt:
- Tuần sau:

## Săn bug (bắt đầu 2026-10-20)
Bài tập thay thế cho các bài điền TODO. Hướng dẫn: docs/bughunt/README.md, bảng điểm: docs/bughunt/_Scoreboard.md.
Đi qua các bước 1, 2, 6, 7 và 20. Xong cả 5 bug thì chấm lại câu 2, 4, 9, 10 trong bảng điểm ở trên.

## Mốc kiểm tra
- [ ] Hết giai đoạn 1 (bước 1–3): làm lại câu 2, 3, 4, 7, 11 và chấm lại.
- [ ] Hết giai đoạn 2 (bước 4–5): làm lại câu 5, 6, 8.
- [ ] Hết giai đoạn 3 (bước 6–9): làm lại câu 9, 10, 12.
- [ ] Vòng phỏng vấn đo các mục chưa đo: nhóm C, D, E (test, Docker, CI/CD, Redis, Kafka, JVM, isolation level, AI).
- [ ] Bài kiểm tra thực tế: tự build một service (CRUD, auth, DB, test, Docker) trong 1–2 tuần mà không bí.
- [ ] Bài kiểm tra AI: làm một tính năng có và không có AI, so thời gian và chất lượng, ghi số liệu.
