# Tự đánh giá năng lực Java ~2 năm

Cách dùng: với mỗi mục, chấm theo 3 mức.
- 0 = chưa biết / chỉ nghe tên
- 1 = đã dùng, làm theo mẫu hoặc tutorial được
- 2 = tự làm được trong dự án, giải thích được "tại sao", debug được khi lỗi
- 3 = có thể thiết kế, review code người khác, chọn giữa các phương án và nêu trade-off

Kỳ vọng ở mức 2 năm (theo JD): nhóm A và B đạt mức 2; nhóm C đạt mức 1–2; nhóm D đạt mức 0–1.

## A. Bắt buộc (JD nào cũng hỏi)

| Mục | Câu tự kiểm tra (trả lời được không cần tra) | Điểm |
|---|---|---|
| Java Core | HashMap hoạt động thế nào? equals/hashCode liên quan gì? | |
| Java 8+ | Stream vs for-loop khi nào dùng? Optional dùng sai ở đâu? | |
| OOP/SOLID | Cho ví dụ vi phạm SRP/DIP trong code bạn từng viết | |
| Exception | Checked vs unchecked, khi nào wrap exception? | |
| Concurrency cơ bản | synchronized, ExecutorService, race condition là gì? | |
| SQL | JOIN, GROUP BY, index, đọc EXPLAIN cơ bản | |
| Git | rebase vs merge, xử lý conflict, revert | |

## B. Spring (trọng tâm)

| Mục | Câu tự kiểm tra | Điểm |
|---|---|---|
| Spring IoC/DI | Bean lifecycle, scope, constructor vs field injection | |
| Spring MVC/REST | Validation, @ControllerAdvice, status code, DTO vs entity | |
| Spring Data JPA | N+1 là gì và cách sửa? LAZY vs EAGER? | |
| Transaction | @Transactional hoạt động qua proxy, self-invocation sai ở đâu? | |
| Spring Security + JWT | Luồng đăng nhập, filter chain, refresh token | |
| Config/Profile | application.yaml, profile, biến môi trường, không commit secret | |
| Spring AOP | Dùng cho logging/audit, hiểu proxy | |

## C. Chất lượng và hạ tầng

| Mục | Câu tự kiểm tra | Điểm |
|---|---|---|
| Unit test | JUnit 5 + Mockito, mock vs spy, test service tách khỏi DB | |
| Integration test | @SpringBootTest, Testcontainers | |
| Docker | Viết Dockerfile, docker-compose app + DB | |
| CI/CD | Pipeline build + test + deploy (GitHub Actions/Jenkins) | |
| Redis/caching | Cache-aside, TTL, cache invalidation | |
| Logging/debug | Đọc stack trace, log đúng mức, debug lỗi production | |
| Performance | Tìm query chậm, profile cơ bản | |

## D. Thường là điểm cộng

| Mục | Điểm |
|---|---|
| Kafka/RabbitMQ (producer, consumer, retry, idempotency) | |
| Microservices (gateway, service discovery, config, inter-service call) | |
| Cloud (AWS: EC2/S3/RDS/ECS hoặc tương đương) | |
| Kubernetes cơ bản | |
| Design patterns (Factory, Strategy, Builder, ...) | |
| System design cơ bản (scale, cache, queue, DB choice) | |
| Frontend cơ bản (React/Angular) | |
| AI coding tools có kiểm chứng đầu ra | |
| Gọi LLM API từ Spring Boot | |

## Đọc kết quả

- Nhóm A hoặc B có mục dưới 2: đó là lỗ hổng cần vá trước, vì mọi JD middle đều hỏi sâu phần này.
- A, B đều đạt 2 và C đạt 1–2: bạn đúng mức 2 năm, đủ ứng tuyển JD như KMS, Momo, Axon, Hitachi, OmiGroup, Amaris Jr/Mid.
- Thêm D đạt 1–2 (Kafka, AWS, K8s) và nhiều mục đạt 3: bạn đang ở ngưỡng middle 3–4 năm (HCLTech, NAB, Saigon Tech).

## Gợi ý ngăn chặn tự đánh giá lệch
- Mức 2 nghĩa là bạn giải thích được "tại sao", không chỉ "làm thế nào".
- Với mỗi mục chấm 2 trở lên, thử nhớ lại một lần cụ thể bạn đã xử lý nó ở dự án thật. Không nhớ được thì hạ xuống 1.
