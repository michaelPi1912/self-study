# Kết quả phỏng vấn thử (vòng 1–3)

Thang 0–3 (xem _SelfAssessment.md). Chưa kiểm tra nhóm C (test, Docker, CI/CD, Redis) và nhóm D (Kafka, cloud, microservices).

| # | Chủ đề | Điểm |
|---|---|---|
| 1 | HashMap, equals/hashCode | 2 |
| 2 | Fail-fast iterator (ConcurrentModificationException) | 1 |
| 3 | Stream, Optional | 1 |
| 4 | Race condition, atomic | 1 |
| 5 | SQL join + group by + index | 1 |
| 6 | Khi nào index không hiệu quả | 2 |
| 7 | SOLID (SRP) | 1 |
| 8 | Git conflict, merge vs rebase | 1 |
| 9 | @Transactional, proxy, rollback | 2 |
| 10 | N+1, LAZY vs EAGER | 2 |
| 11 | DI, thread-safety của singleton | 1 |
| 12 | JWT + Spring Security | 2 |

Trung bình: 1.42/3. Spring: 1.75. Java core, SQL, Git, SOLID: 1.25.

## Lỗ hổng ưu tiên
1. Concurrency: synchronized, AtomicInteger, volatile, thread-safety của singleton bean.
2. Java 8+: Stream (khi nào không dùng), Optional, removeIf, fail-fast iterator.
3. SQL: viết query chạy được không cần tra, composite/covering index, không bọc hàm quanh cột trong WHERE.
4. Hibernate: JOIN FETCH, @EntityGraph, @BatchSize, LazyInitializationException, cách phát hiện N+1 (bật SQL log).
5. Spring: rollbackFor cho checked exception, cách sửa self-invocation, authentication vs authorization, OncePerRequestFilter/SecurityContext.
6. Git: rebase viết lại lịch sử, không rebase nhánh đã chia sẻ.
7. SOLID: SRP ở mức class, dùng ví dụ OrderService.
