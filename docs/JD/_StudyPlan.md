# Lộ trình vá lỗ hổng (từ _AssessmentResult.md)

Nguyên tắc: mỗi bước có một bài tập làm trong repo này và một "tiêu chí qua". Chưa qua tiêu chí thì chưa sang bước sau. Bài tập thuần Java viết dưới dạng test trong `src/test/java/com/ai/aiengineer/` (package con theo chủ đề) để chạy bằng `.\mvnw.cmd test -Dtest=...`.

Dự án hiện chỉ có `spring-boot-starter-webmvc` và Lombok. Các bước sau sẽ thêm dependency khi cần (JPA, H2/PostgreSQL, Security, Testcontainers).

## Giai đoạn 1: Nền tảng Java (không cần Spring)

### Bước 1. Concurrency
Bài tập:
1. Viết test chạy 2 thread, mỗi thread tăng `int counter` 100000 lần. Chạy nhiều lần, ghi lại kết quả sai.
2. Sửa 3 cách: `synchronized`, `AtomicInteger`, `ReentrantLock`. So sánh thời gian.
3. Thử `volatile int` và giải thích bằng văn bản vì sao vẫn sai.
4. Dùng `ExecutorService` chạy 10 task, gom kết quả bằng `Future` / `CompletableFuture`.
Tiêu chí qua: giải thích được lost update, visibility vs atomicity, và vì sao bean singleton phải stateless.

### Bước 2. Collection và Java 8+
Bài tập:
1. Tái hiện `ConcurrentModificationException` (câu 2), sửa bằng `removeIf` và `Iterator.remove()`.
2. Tự viết lớp `Point` có `equals`/`hashCode` sai (chỉ override `equals`), đưa vào `HashMap`/`HashSet`, chứng minh `get` trả về `null`. Sau đó sửa.
3. Viết 5 bài xử lý danh sách bằng vòng `for` rồi bằng Stream: filter, map, groupingBy, partitioningBy, joining. Ghi chú trường hợp Stream làm code khó đọc hơn.
4. Viết một hàm trả `Optional<User>` và dùng `map`/`orElseThrow`. Không dùng `.get()`.
Tiêu chí qua: nói được khi nào không dùng Stream, và 3 cách dùng Optional sai.

### Bước 3. OOP/SOLID
Bài tập: viết `OrderService` "xấu" làm cả validate, tính giá, lưu, gửi email trong một class. Refactor thành `OrderValidator`, `PriceCalculator`, `OrderRepository`, `NotificationService` (interface + implementation). Viết 1 đoạn giải thích SRP và DIP bằng chính ví dụ này.
Tiêu chí qua: chỉ ra được lý do thay đổi riêng của từng class.

## Giai đoạn 2: SQL và Git

### Bước 4. SQL
Bài tập (dùng PostgreSQL hoặc MySQL qua Docker, hoặc H2 cho nhanh):
1. Tạo bảng `customers`, `orders`, nạp 100k+ dòng (script sinh dữ liệu).
2. Viết lại truy vấn "top 3 khách chi nhiều nhất năm nay" (câu 5) không cần tra.
3. Chạy `EXPLAIN` trước và sau khi thêm index `(created_at, customer_id, total)`. Thử lọc bằng `YEAR(created_at)` rồi bằng khoảng ngày, so sánh plan.
4. Thử `LIKE '%abc'` và `LIKE 'abc%'` trên cột có index.
5. Tập 10 bài JOIN, GROUP BY, HAVING, subquery, window function cơ bản (`ROW_NUMBER`).
Tiêu chí qua: giải thích được plan, biết 4 trường hợp index không được dùng.

### Bước 5. Git
Bài tập: tạo repo thử, cố ý tạo conflict giữa 2 nhánh, xử lý bằng cả `merge` và `rebase`. Thử `git rebase -i` để gộp commit, `git revert`, `git reflog` để cứu commit.
Tiêu chí qua: nói được vì sao không rebase nhánh đã push chung.

## Giai đoạn 3: Spring chuyên sâu (trong project này)

Thêm dependency: `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, H2 (hoặc PostgreSQL). Lưu cấu hình trong `application.yaml`, không commit secret.

### Bước 6. JPA, N+1 và Transaction
Bài tập:
1. Tạo entity `Order` 1-N `OrderItem`, API `GET /orders`.
2. Bật SQL log (`spring.jpa.show-sql: true`), tái hiện N+1 với 20 order. Đếm số query.
3. Sửa bằng `JOIN FETCH`, `@EntityGraph`, rồi `@BatchSize`. So sánh số query. Ghi lại hạn chế của `JOIN FETCH` khi phân trang.
4. Tái hiện `LazyInitializationException`, sửa đúng cách (không dùng EAGER bừa).
5. Tái hiện self-invocation của `@Transactional` bằng test (gọi method trong cùng class, chứng minh không rollback). Sửa bằng tách bean.
6. Chứng minh checked exception không rollback, sửa bằng `rollbackFor`.
Tiêu chí qua: nhìn log SQL là nhận ra N+1; giải thích được proxy.

### Bước 7. REST sạch
Bài tập: DTO tách khỏi entity, `@Valid` với `@NotNull`/`@Size`, `@RestControllerAdvice` trả lỗi thống nhất, đúng status code (400/404/409/422). Viết test với `MockMvc`.
Tiêu chí qua: không có entity lộ ra response, mọi lỗi trả cùng một format.

### Bước 8. Spring Security + JWT
Bài tập:
1. Thêm `spring-boot-starter-security`. Viết `POST /auth/login` trả access token và refresh token.
2. Viết `JwtAuthFilter extends OncePerRequestFilter`, đặt trước `UsernamePasswordAuthenticationFilter`, set `SecurityContextHolder`.
3. Hash mật khẩu bằng BCrypt. Phân quyền: `@PreAuthorize("hasRole('ADMIN')")` cho một endpoint.
4. Viết endpoint refresh, lưu refresh token phía server để thu hồi được.
Tiêu chí qua: vẽ lại được luồng và chỉ ra đâu là authentication, đâu là authorization.

### Bước 9. DI và thread-safety
Bài tập: viết một bean singleton có field `Map` thay đổi được, gọi API đồng thời bằng nhiều thread (test), chứng minh lỗi. Sửa bằng cách làm bean stateless hoặc dùng `ConcurrentHashMap`. Chuyển toàn bộ bean sang constructor injection + `@RequiredArgsConstructor` của Lombok.
Tiêu chí qua: giải thích được vì sao constructor injection được khuyên dùng.

## Giai đoạn 4: Chất lượng và hạ tầng (bạn chưa có kinh nghiệm, nên làm sau cùng)

### Bước 10. Testing
Bài tập: viết unit test cho service bằng JUnit 5 + Mockito (mock repository), test repository với `@DataJpaTest`, thêm 1 test tích hợp bằng Testcontainers (PostgreSQL). Đo coverage cơ bản.
Tiêu chí qua: tách được unit test khỏi DB; phân biệt mock/spy/verify.

### Bước 11. Docker và CI
Bài tập: viết `Dockerfile` cho app, `docker-compose` chạy app + DB. Tạo workflow GitHub Actions: build + test mỗi lần push.
Tiêu chí qua: `docker compose up` chạy được toàn bộ; pipeline chạy xanh.

### Bước 12. Redis và messaging (bước nâng cao)
Bài tập: thêm Redis cache cho `GET /orders/{id}` (cache-aside, TTL, xóa cache khi cập nhật). Sau đó thử Kafka hoặc RabbitMQ: gửi sự kiện "order created", consumer xử lý idempotent.
Tiêu chí qua: giải thích được cache invalidation và vì sao consumer cần idempotent.

## Giai đoạn 5: Mở rộng (phần _AssessmentResult.md chưa đo)

Các bước này bổ sung những mục có trong _SelfAssessment.md và _MiddleJavaSummary.md nhưng chưa nằm trong 12 bước đầu. Làm sau giai đoạn 3, xen kẽ với giai đoạn 4.

### Bước 13. Exception và logging
Bài tập:
1. Viết hệ phân cấp `BusinessException` (unchecked) và một checked exception cho lỗi I/O. Wrap exception bằng cách giữ `cause`, không nuốt lỗi.
2. Tái hiện 3 lỗi hay gặp: `catch (Exception e) {}` rỗng, log rồi throw lại (log trùng), mất stack trace khi `throw new X(e.getMessage())`.
3. Dùng SLF4J với đúng mức (ERROR/WARN/INFO/DEBUG), log có tham số `{}`, thêm correlation id bằng MDC. Đọc một stack trace dài và chỉ ra "Caused by" gốc.
Tiêu chí qua: nói được khi nào dùng checked vs unchecked và khi nào nên wrap; biết 3 lỗi xử lý exception ở trên.

### Bước 14. Spring core: lifecycle, scope, config, AOP
Bài tập:
1. Viết bean in ra thứ tự `constructor` → `@PostConstruct` → `@PreDestroy`. Thử scope `prototype` được inject vào singleton và giải thích vì sao vẫn chỉ có một instance.
2. Dùng `application.yaml` với 2 profile (`dev`, `prod`), `@ConfigurationProperties` có validation, đọc secret từ biến môi trường.
3. Viết `@Aspect` log thời gian chạy các method của service, rồi viết annotation tự tạo `@Audited`. Tái hiện self-invocation không chạy aspect (cùng gốc với `@Transactional`).
Tiêu chí qua: giải thích được proxy JDK vs CGLIB và vì sao AOP, `@Transactional`, `@Cacheable` cùng một giới hạn.

### Bước 15. Java 17/21 và JVM
Bài tập:
1. Dùng `record`, `sealed interface`, `switch` pattern matching, text block, `var` ở chỗ phù hợp. Chuyển một DTO Lombok sang record và ghi lại khác biệt.
2. Chạy 10.000 task với virtual thread (`Executors.newVirtualThreadPerTaskExecutor()`) và so với platform thread pool.
3. Tạo memory leak bằng `static List` hoặc `ThreadLocal` không xóa, chạy với `-Xmx64m`, đọc `OutOfMemoryError`. Dùng `jcmd`/`jconsole` xem heap và GC.
4. Chứng minh `String` pool: `==` vs `equals`, `intern()`. Tái hiện object mutable bị đổi sau khi đã làm key của `HashMap`.
Tiêu chí qua: nói được heap vs stack, các vùng chính của heap, khi nào GC không thu hồi được object, và khi nào virtual thread không giúp ích.

### Bước 16. Dữ liệu nâng cao
Bài tập:
1. Dùng 2 phiên làm việc để tái hiện dirty read, non-repeatable read, phantom read, so với `READ COMMITTED` và `REPEATABLE READ`.
2. Tái hiện lost update với JPA, sửa bằng `@Version` (optimistic) rồi `@Lock(PESSIMISTIC_WRITE)`. So sánh.
3. Thiết kế schema cho đơn hàng (khóa ngoại, ràng buộc, kiểu dữ liệu tiền tệ dùng `NUMERIC`/`BigDecimal`). Quản lý thay đổi bằng Flyway.
4. Cấu hình HikariCP, tái hiện cạn connection pool (giữ transaction mở lâu), đọc log.
Tiêu chí qua: chọn được isolation level và loại lock cho một tình huống cụ thể, biết vì sao không dùng `double` cho tiền.

### Bước 17. Design pattern và system design cơ bản
Bài tập:
1. Cài Strategy (tính phí vận chuyển), Factory, Builder, Observer/event (`ApplicationEventPublisher`). Với mỗi pattern, ghi 2 dòng: giải quyết vấn đề gì, khi nào không nên dùng.
2. Vẽ sơ đồ (giấy hoặc Mermaid) cho hệ thống rút gọn URL và hệ thống đặt hàng: ước lượng QPS, chọn DB, chỗ đặt cache, chỗ đặt queue.
Tiêu chí qua: kể được tình huống dùng đúng và dùng thừa của mỗi pattern; giải thích được các lựa chọn trong sơ đồ của chính bạn.

### Bước 18. API nâng cao
Bài tập:
1. Pagination (offset vs cursor), sắp xếp, lọc. Sinh OpenAPI/Swagger bằng springdoc.
2. Idempotency: endpoint tạo đơn nhận header `Idempotency-Key`, gọi hai lần chỉ tạo một đơn.
3. Gọi dịch vụ ngoài bằng `RestClient` với timeout, retry có backoff, và circuit breaker (Resilience4j). Giả lập dịch vụ ngoài chậm và lỗi.
4. Versioning API (`/v1`, `/v2`) và thay đổi không phá tương thích.
Tiêu chí qua: giải thích được vì sao cần timeout và idempotency, và retry nguy hiểm khi nào.

### Bước 19. Microservices, observability, cloud
Bài tập:
1. Tách `order-service` và `notification-service` chạy bằng docker-compose, gọi nhau qua HTTP rồi qua message queue.
2. Thêm Actuator (health, metrics), Micrometer + Prometheus/Grafana (hoặc chỉ đọc `/actuator/prometheus`), tracing cơ bản.
3. Đọc tài liệu và triển khai một container lên một cloud (AWS ECS/Cloud Run hoặc tương đương) bằng free tier. Viết 1 trang giải thích gateway, service discovery, config tập trung.
4. Chạy app trên cụm Kubernetes cục bộ (kind/minikube): Deployment, Service, ConfigMap.
Tiêu chí qua: nói được khi nào KHÔNG nên tách microservice, và các điểm hỏng khi gọi giữa các service.

### Bước 20. AI trong công việc
Bài tập:
1. Gọi một LLM API từ Spring Boot (key lấy từ biến môi trường, không commit): endpoint nhận câu hỏi, trả câu trả lời; xử lý timeout, lỗi, giới hạn token.
2. Làm RAG tối thiểu: chia tài liệu thành đoạn, nhúng vector, tìm đoạn gần nhất, đưa vào prompt.
3. Quy trình dùng AI coding tool: viết CLAUDE.md (đã có), giao task nhỏ, đọc diff, chạy test trước khi merge. Với mỗi task ghi: thời gian có AI, thời gian ước tính nếu không có, số chỗ phải sửa lại.
4. Cố tình để AI sinh code cho các bài ở bước 1, 6, 8, rồi tự tìm lỗi (race condition, N+1, lỗi auth) trong code đó.
Tiêu chí qua: có số liệu về hiệu quả khi dùng AI; tìm ra ít nhất một lỗi thật trong code AI sinh; giải thích được toàn bộ code mình merge.

## Cách theo dõi
- Làm xong mỗi bước, trả lời lại các câu hỏi liên quan trong vòng phỏng vấn thử và tự chấm lại.
- Ưu tiên theo mức quan trọng với JD: bước 1, 2, 4, 6, 8 trước, bước 10–12 sau.
- Khi xong giai đoạn 1–3, làm lại vòng phỏng vấn, kèm vòng 4 (test, Docker, Redis, Kafka).
