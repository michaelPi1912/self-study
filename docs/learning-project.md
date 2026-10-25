# Learning Project with AI — `commerce-pay-lab`

*Một project duy nhất để: (1) học kỹ thuật lên Middle, (2) luyện dùng AI **nhanh mà không mù**, (3) tạo nội dung thật để nói tiếng Anh và đưa lên CV.*

> **Không tạo project mới.** Bạn đã có lab Product/Order/Stock (LoggingAspect, EXPLAIN + index, Redis `@Cacheable`, Lua script trừ stock). File này **nâng lab đó** thành một modular monolith có thêm Payment/Wallet — đúng domain fintech của bạn và của MoMo. Đổi tên repo nếu muốn, giữ lịch sử commit.

---

## 0. Ý tưởng cốt lõi: tốc độ thật = sinh code + kiểm chứng

```
Thời gian 1 task  =  AI sinh code  +  BẠN kiểm chứng
                     (gần như 0)      (phụ thuộc hoàn toàn vào hiểu biết)

Không hiểu transaction → không biết chỗ nào phải nghi → merge mù
                       → bug lộ muộn → chậm gấp nhiều lần
```

Vì vậy project này luyện **hai việc song song**:

- **Hiểu sâu** những chỗ AI hay sai (concurrency, transaction, idempotency, N+1, security).
- **Quy trình** khiến AI làm phần tẻ nhạt thật nhanh, còn bạn kiểm chứng phần quan trọng thật rẻ.

---

## 1. Kiến trúc mục tiêu

```
                    ┌────────────── commerce-pay-lab (1 Spring Boot app) ──────────────┐
  HTTP  ──►  API    │  catalog      order         payment/wallet      notification     │
  (JWT)      layer  │  (stock)  ──► (state)  ──►  (idempotent pay) ──► (via outbox)    │
                    │     │            │              │                     ▲           │
                    └─────┼────────────┼──────────────┼─────────────────────┼───────────┘
                          ▼            ▼              ▼                     │
                     MySQL      ◄── Flyway      outbox table ──► Kafka ─────┘
                          ▲                    (M6)
                     Redis: cache, Lua stock, rate limit
               Observability: Actuator → Prometheus → Grafana (M8)
```

Luồng chính: **tạo order → reserve stock → thanh toán (idempotent) → đổi trạng thái → phát notification.**

Module là **package có ranh giới rõ**, chưa tách microservice. Lý do tách sau này (nếu có) phải ghi thành ADR.

---

## 2. Cấu trúc repo

```
commerce-pay-lab/
├── CLAUDE.md                 ← luật cho AI coding assistant (mục 4)
├── README.md                 ← tiếng Anh: chạy thế nào, kiến trúc, các con số
├── docker-compose.yml        ← mysql, redis, (kafka, prometheus, grafana sau)
├── .github/workflows/ci.yml
├── src/main/java/.../
│   ├── catalog/  order/  payment/  notification/  shared/
├── src/main/resources/db/migration/   ← Flyway V1__..., V2__...
├── src/test/java/...                  ← unit + Testcontainers + concurrency tests
├── k6/                                ← load test scripts
└── docs/
    ├── ai-log.md             ← nhật ký AI: đúng/sai/bạn bắt được gì (mục 5)
    ├── adr/                  ← quyết định kiến trúc có trade-off
    ├── labs/                 ← mỗi failure lab: dự đoán → thí nghiệm → kết quả
    ├── postmortems/
    └── numbers.md            ← mọi con số đo được (nguồn cho CV)
```

Tất cả viết bằng **tiếng Anh** — đây là phần Writing của English plan.

---

## 3. Mô hình làm việc với AI

### 3.1 Ba vùng giao việc

| Vùng | Ai làm | Ví dụ | Vì sao |
|---|---|---|---|
| 🟢 **Giao hẳn cho AI** | AI viết, bạn đọc lướt + chạy | Dockerfile, docker-compose, CI YAML, DTO/mapper, Flyway seed data, k6 script khung, OpenAPI annotation | Không có giá trị học; sai thì lộ ngay khi chạy |
| 🟡 **Cặp đôi** | Bạn viết test/spec trước → AI implement → bạn review từng dòng | Controller, validation, exception handler, repository query, Redis config | Test của bạn là hợp đồng; AI chỉ được "điền vào" |
| 🔴 **Tự làm trước** | Bạn tự viết bản đầu, AI chỉ được review/phản biện | Logic trừ stock, thanh toán idempotent, transaction boundary, locking, outbox, authorization check | Đây là chỗ AI sai nhiều nhất **và** là chỗ phỏng vấn đào sâu nhất |

Quy tắc chuyển vùng: một chủ đề 🔴 chỉ được hạ xuống 🟡 khi bạn đã đạt **Definition of Learned** (giải thích 5 phút không nhìn note + có failure lab + có test).

### 3.2 Năm vai của AI

| Vai | Khi nào | Câu lệnh mở đầu |
|---|---|---|
| **Socratic mentor** | Bị kẹt > 30' trên việc 🔴 | "Don't give me code. Ask me one question that exposes the flaw in my reasoning." |
| **Adversarial tester** | Sau khi bạn viết xong logic | "List 8 inputs or timings that could break this method. Rank by likelihood in production." |
| **Reviewer** | Trước mỗi commit/PR | Prompt review ở 3.4 |
| **Pair implementer** | Việc 🟡, đã có test đỏ | "Make these failing tests pass. Do not modify the tests. Explain any assumption." |
| **Boilerplate generator** | Việc 🟢 | Thoải mái, nhưng phải chạy được trước khi commit |

### 3.3 Spec-first: kỹ thuật tăng tốc thật sự

Đây là thói quen biến AI từ "đoán ý bạn" thành "làm đúng ý bạn":

```
1. Bạn viết test (đỏ)                ← bạn quyết định hành vi đúng
2. AI implement cho test xanh       ← AI không được sửa test
3. Bạn review diff + chạy lại        ← kiểm chứng rẻ vì test đã nói "đúng là gì"
4. Revert fix → test phải ĐỎ lại     ← chứng minh test thật sự bắt được bug
```

Bước 4 hay bị bỏ qua nhất. Một concurrency test vẫn xanh khi đã bỏ fix nghĩa là test **không chứng minh gì cả** (đúng câu Cổng kiểm tra tuần 6 của bạn).

### 3.4 Checklist kiểm chứng code AI (chạy mỗi PR)

- [ ] **Transaction boundary**: `@Transactional` đặt ở đâu? Có self-invocation không? Exception checked có làm commit sai không?
- [ ] **Concurrency**: có check-then-act không? Có read-modify-write trên hot row không?
- [ ] **Idempotency**: request/message lặp lại có tạo hiệu ứng trùng không?
- [ ] **Query**: có N+1 không? Bật SQL log và đếm số query.
- [ ] **Edge case**: null, rỗng, số âm, số tiền 0, ID không tồn tại, ID của người khác.
- [ ] **Security**: có kiểm tra ownership (user A thao tác resource của user B) không? Có log secret/token không?
- [ ] **Test**: test có fail khi bỏ fix không? Có mock repository trong test cần kiểm chứng transaction không?
- [ ] **Giải thích được**: mỗi dòng mình có nói lại được vì sao nó ở đó không? Không → chưa merge.

Prompt review:

```text
Review this diff as a strict senior Java engineer at a payments company.
Focus ONLY on: transaction boundaries, concurrency, idempotency,
N+1 queries, authorization, and whether the tests actually prove correctness.
For each issue: severity (blocker/major/minor), the exact line, and WHY it fails.
Do not rewrite the code. Do not comment on style.
```

### 3.5 Đo tốc độ (để biết AI có làm bạn nhanh thật không)

Mỗi task ghi vào `docs/ai-log.md`:

| Task | Vùng | Thời gian đến "verified done" | AI sai bao nhiêu chỗ | Bạn tự bắt bao nhiêu chỗ | Ghi chú |
|---|---|---|---|---|---|

"Verified done" = test xanh + revert check + checklist 3.4. Không tính thời gian sinh code.

Sau 4–6 tuần, bảng này trả lời được 2 câu phỏng vấn Axon/KMS chắc chắn hỏi:
*"How do you use AI daily?"* và *"Give an example where AI was wrong."* — bằng dữ liệu thật.

---

## 4. `CLAUDE.md` — luật cho AI assistant trong repo

Đặt ở gốc repo. Claude Code và nhiều công cụ tương tự đọc file này làm ngữ cảnh mặc định, nên luật học của bạn được áp dụng tự động thay vì phải nhắc mỗi lần.

```markdown
# CLAUDE.md

## Context
Learning project. The owner is a Java backend developer (~2 years) moving to
mid-level. Goal: learn deeply AND ship fast. Stack: Java 21, Spring Boot 3,
MySQL 8, Redis, Flyway, JUnit 5, AssertJ, Testcontainers, Maven.

## Zones (strict)
- RED (do NOT write implementation first): stock reservation, payment and
  idempotency, transaction boundaries, locking, outbox/consumer logic,
  authorization checks. In these areas: ask questions, review, or suggest
  edge cases only. Write code only if the owner says "I'm stuck, show me".
- YELLOW: implement only to make the owner's existing failing tests pass.
  Never modify or delete tests to make them pass.
- GREEN: boilerplate (Docker, CI, DTOs, mappers, seed data). Go ahead.

## Always
- Propose a short plan before editing more than one file.
- Keep diffs small. One concern per change.
- After any change touching data, explain the transaction boundary.
- Flag check-then-act, read-modify-write, N+1, and missing ownership checks.
- Write commit messages and docs in clear, simple English.

## Never
- Add a dependency without explaining why.
- Swallow exceptions or log secrets/tokens.
- Mock the repository in tests meant to verify transactions or constraints.
```

---

## 5. Vòng lặp mỗi task (~60–90 phút)

```
 1. HỎI       (5')   Viết câu hỏi + dự đoán vào docs/labs/xx.md TRƯỚC khi code
 2. TÁI HIỆN  (20')  Failing test cho bug/hành vi. Không AI.
 3. SỬA       (20')  Vùng 🔴: tự sửa. Vùng 🟡: AI implement theo test.
 4. KIỂM      (10')  Checklist 3.4 + revert check + AI review
 5. GHI       (5')   ai-log + numbers.md (nếu có số) + commit tiếng Anh
 6. NÓI       (10')  Giải thích task bằng tiếng Anh 90s, ghi âm
                     (= bài tập ngày của English plan)
```

Một task = một câu hỏi = một thứ kể được. Không gộp.

---

## 6. Milestones

Trạng thái cột đầu dựa trên `Ke_hoach_12_tuan.xlsx` — **tự kiểm tra lại với repo thật**.

| # | Milestone | Trạng thái (theo xlsx) |
|---|---|---|
| M0 | Nền móng + luật AI | Một phần |
| M1 | Stock atomic — bậc thang 3 tầng | Phần lớn xong (tầng 2 + Lua) |
| M2 | Idempotent payment | Chưa |
| M3 | Testing thật + CI xanh | Chưa |
| ★ | **Đưa repo lên CV** | |
| M4 | Query performance + con số | Một phần (EXPLAIN đã đo) |
| M5 | Cache đúng cách + Redis lỗi | Một phần (`@Cacheable` xong, stale data chưa) |
| M6 | Outbox + Kafka + idempotent consumer | Chưa |
| M7 | Security: JWT + ownership | Chưa |
| M8 | Observability + load test + incident | Chưa |

Thứ tự được xếp theo **giá trị phỏng vấn**: M0–M3 xong là repo đủ tốt để lên CV.

---

### M0 — Nền móng + luật AI (3–4 ngày)

- **Build:** Java 21, Spring Boot 3, MySQL 8 qua Docker Compose, Flyway V1, cấu trúc package theo module, `CLAUDE.md`, `docs/` skeleton, README tiếng Anh tối thiểu.
- **AI:** 🟢 toàn bộ phần hạ tầng — đây là bài tập đầu tiên về "giao hẳn cho AI rồi kiểm chứng bằng cách chạy".
- **Failure lab:** sửa một migration Flyway đã chạy → app không khởi động. Vì sao? (checksum). Cách đúng là gì?
- **English output:** README section *How to run*.
- **Exit gate:** `docker compose up` + `mvn verify` chạy trên máy sạch theo README.

### M1 — Stock atomic: bậc thang 3 tầng (gần xong — hoàn thiện)

- **Học:** race trong JVM vs giữa nhiều instance; atomic conditional UPDATE; Redis Lua.
- **Build:** endpoint reserve stock với 3 cách; concurrency test 100 thread cho cả 3.
- **AI:** 🔴. Sau khi xong, dùng vai *Adversarial tester*: "Redis says in stock but DB says sold out — list failure scenarios."
- **Failure lab:** chạy 2 instance app → tầng `synchronized` oversell. Chụp số liệu.
- **English output:** kể "3-tier oversell protection" 3 phút (Cổng kiểm tra tuần 3).
- **Exit gate:** test đỏ với tầng 1 khi chạy 2 instance, xanh với tầng 2 và 3. Revert check pass.
- **Câu PV nó trả lời:** *"Two users buy the last item at the same time…"*

### M2 — Idempotent payment (1 tuần) ★ câu chuyện fintech số 1

- **Học:** check-then-act; idempotency key; unique constraint + business effect cùng transaction; trả lại **cùng response** cho request lặp.
- **Build:** `POST /payments` với header `Idempotency-Key`; bảng `idempotency_record (key UNIQUE, request_hash, response, status)`; ví có `balance` cập nhật bằng atomic UPDATE.
- **Failure lab (3 cái):**
  1. Bản ngây thơ `SELECT` rồi `INSERT` → 2 thread cùng key → trừ tiền 2 lần. Tái hiện bằng test.
  2. Cùng key nhưng **body khác** (số tiền khác) → phải trả 422, không phải trả kết quả cũ. Vì sao cần `request_hash`?
  3. Request 1 **đang xử lý**, request 2 đến cùng key → trả gì? (409 / chờ / trả trạng thái PROCESSING). Ghi ADR.
- **AI:** 🔴 cho logic; 🟢 cho DTO/migration.
- **English output:** PR description theo mẫu trong English plan + câu trả lời 90s *"What is idempotency…"*.
- **Exit gate:** test 50 thread cùng key → đúng 1 lần trừ tiền; revert fix → đỏ.

### M3 — Testing thật + CI (1–1,5 tuần)

- **Học:** unit vs integration; khi nào không được mock; Testcontainers vs H2; test flaky vì đâu.
- **Build:** unit test service (Mockito) + `@DataJpaTest`/`@SpringBootTest` với Testcontainers MySQL; GitHub Actions chạy `mvn verify` mỗi push; badge CI trên README.
- **AI:** 🟡 — bạn viết tên test + assertion, AI điền phần setup. Dùng prompt: *"Generate tests that would FAIL if the implementation had a bug. Don't mirror the implementation."* Sau đó tự kiểm: test nào AI viết chỉ đang lặp lại implementation? Ghi vào ai-log.
- **Failure lab:** cố tình viết 1 concurrency test flaky (sleep-based) → làm nó ổn định bằng `CountDownLatch`/`ExecutorService`.
- **English output:** giải thích *"unit vs integration test, and when you shouldn't mock"* theo khung 5 câu.
- **Exit gate:** CI xanh; mọi test M1–M2 chạy trong CI với database thật.

### ★ Checkpoint CV

Sau M3, thêm mục **Projects** vào CV với link repo. Chỉ ghi những gì đã xanh trong CI. Số liệu lấy từ `docs/numbers.md`.

### M4 — Query performance (1 tuần)

- **Học:** N+1, `JOIN FETCH`/`@EntityGraph`, `MultipleBagFetchException`, keyset vs OFFSET pagination, composite index & thứ tự cột.
- **Build:** `GET /orders?cursor=` lịch sử giao dịch; seed 1 triệu dòng.
- **Failure lab:** tái hiện N+1 (đếm query bằng Hibernate statistics); OFFSET 900000 chậm thế nào vs keyset.
- **AI:** 🟡. Dùng AI đọc `EXPLAIN ANALYZE` **sau khi** bạn đã tự viết nhận định — so sánh hai bản.
- **English output:** *"When does an index NOT help?"*
- **Exit gate:** `numbers.md` có ít nhất 3 cặp số trước/sau.

### M5 — Cache đúng cách (1 tuần)

- **Học:** cache-aside, TTL vs evict, cache stampede, **cái gì không được cache** (số dư ví!), Redis down thì sao.
- **Build:** cache cho catalog/config; rate limit `POST /payments` theo user bằng Redis.
- **Failure lab:** stale data khi quên evict; tắt Redis giữa load test → app phải degrade, không sập (timeout + fallback về DB).
- **AI:** 🟡.
- **English output:** *"Why would you never cache a wallet balance with a long TTL?"*
- **Exit gate:** k6 có số p95 trước/sau cache; test "Redis down" xanh.

### M6 — Outbox + Kafka + idempotent consumer (1,5 tuần)

- **Học:** dual-write problem; transactional outbox; at-least-once; idempotent consumer bằng `processed_message` UNIQUE (đúng câu chuyện file Idempotency của bạn); retry + DLQ.
- **Build:** payment thành công → ghi outbox cùng transaction → publisher đẩy Kafka → notification consumer.
- **Failure lab:** kill app giữa lúc publish → message vẫn đi sau restart; gửi cùng message 2 lần → 1 hiệu ứng.
- **AI:** 🔴 cho outbox + consumer; 🟢 cho Kafka trong docker-compose.
- **English output:** *"The charge succeeded but writing the ledger failed. What now?"* (Cổng kiểm tra tuần 8).
- **Exit gate:** test chứng minh không mất và không trùng message.

### M7 — Security (1 tuần)

- **Học:** authN vs authZ; filter chain; JWT; method-level authorization; IDOR.
- **Build:** login cấp JWT; user chỉ xem/thanh toán đơn của mình.
- **Failure lab:** user A gọi `GET /orders/{id của B}` → trước fix trả 200 (broken access control). Test cho 3 role.
- **AI:** 🔴 cho ownership check. Hỏi AI: *"Find every endpoint where a user could access another user's data."* — rồi tự xác nhận từng cái.
- **English output:** *"Authentication vs authorization, with an example from my project."*
- **Exit gate:** test authorization xanh; không token nào trong log.

### M8 — Observability + load test + incident (1,5 tuần)

- **Học:** logs/metrics/traces; correlation ID; p50/p95/p99; connection pool exhaustion.
- **Build:** Actuator + Micrometer → Prometheus → Grafana dashboard (latency, error rate, throughput); k6 kịch bản thanh toán.
- **Failure lab:** giảm Hikari pool xuống 2 + load test → quan sát triệu chứng → viết postmortem (timeline, impact, root cause, fix, prevention).
- **AI:** 🟢 cho dashboard/config; 🔴 cho việc chẩn đoán — tự đọc metric trước, hỏi AI sau.
- **English output:** đọc to postmortem như đang báo cáo trong incident review (Tuần 7 English plan).
- **Exit gate:** 1 postmortem hoàn chỉnh + ảnh dashboard trong README.

---

## 7. Mẫu file

### `docs/labs/xx-ten-lab.md`

```markdown
# Lab: <tên>
Question: <câu hỏi>
My prediction (before running): <dự đoán + lý do>
Experiment: <test/kịch bản>
Evidence: <output, số liệu, ảnh>
Prediction correct? <có/không — nếu sai, sai ở cơ chế nào>
Fix + why it works:
Trade-off / when this fix breaks:
New question:
```

### `docs/ai-log.md`

```markdown
## <ngày> — <task> (zone: 🔴/🟡/🟢)
- What I asked AI:
- What AI got right:
- What AI got wrong / missed:
- Did I catch it before AI admitted it? yes/no
- Time to verified done:
- Lesson (one line):
```

### `docs/adr/0001-xxx.md`

```markdown
# ADR-0001: <quyết định>
Status: accepted
Context: <vấn đề, constraint>
Options: <A, B, C — mỗi cái 1–2 dòng>
Decision: <chọn gì>
Consequences: <được gì, mất gì, khi nào phải xem lại>
```

---

## 8. Ranh giới bắt buộc

- **Không** đưa code, dữ liệu, tài liệu của FPT hoặc khách hàng vào project này hay vào bất kỳ AI tool nào chưa được công ty duyệt.
- **Không** ghi lên CV thứ chưa xanh trong CI.
- **Không** để AI viết bản đầu tiên cho vùng 🔴. Ba ngày liên tiếp vi phạm → 3 ngày AI-free cho business logic.

---

## 9. Bắt đầu trong 48 giờ

- [ ] Kiểm tra repo hiện tại: cái gì trong M0–M1 đã thật sự chạy được?
- [ ] Thêm `CLAUDE.md`, `docs/ai-log.md`, `docs/numbers.md`.
- [ ] Ghi các con số đã có (EXPLAIN, `@Cacheable` miss 34ms → hit 5ms) vào `numbers.md`.
- [ ] Viết `docs/labs/` cho lab M2 #1: **dự đoán trước** — 2 thread cùng `Idempotency-Key`, có bị trừ tiền 2 lần không, và vì sao?
- [ ] Ghi âm 90s giải thích dự đoán đó bằng tiếng Anh (= Ngày 0 của English plan, bài B).
