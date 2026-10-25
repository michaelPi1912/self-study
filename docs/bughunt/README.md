# Săn bug: mini shop

Ứng dụng nhỏ trong `src/main/java/com/ai/aiengineer/shop/` (đặt hàng, liệt kê đơn, xem sản phẩm; dữ liệu H2 trong bộ nhớ, tự nạp khi khởi động). Code chạy được và nhìn bình thường, nhưng **có 5 bug được cài sẵn**. Việc của bạn là tìm ra từng bug, chứng minh bằng test, rồi sửa.

Không có comment nào chỉ chỗ có bug, và file đáp án không nằm trong repo. Bạn muốn gợi ý thì hỏi mình.

## Luật chơi
1. Mỗi bug thuộc một mảng khác nhau: **Java core**, **concurrency**, **JPA/Hibernate**, **transaction**, **thiết kế API**. Mỗi mảng có đúng một bug.
2. Với mỗi bug bạn cần làm đủ 4 việc:
   - **Tìm:** chỉ ra dòng code gây lỗi.
   - **Chứng minh:** viết một test **fail** khi bug còn đó (đặt trong `src/test/java/com/ai/aiengineer/bughunt/`).
   - **Sửa:** sửa code cho test xanh, không phá test khác.
   - **Giải thích:** 3 dòng vào `_Scoreboard.md`: nguyên nhân, vì sao hay gặp, cách phát hiện sớm.
3. Bạn được dùng AI hỗ trợ, nhưng phải tự giải thích được từng dòng bạn sửa. Đây cũng là bài tập cho Bước 20.
4. Nên làm tuần tự: đọc code trước, đoán bug, rồi mới chạy thử.

## Chạy thử
```powershell
.\mvnw.cmd spring-boot:run
```
SQL của Hibernate được in ra console (`spring.jpa.show-sql` đã bật).

| Việc | Lệnh |
|---|---|
| Liệt kê đơn | `curl http://localhost:8080/orders` |
| Xem sản phẩm | `curl http://localhost:8080/products/SKU-001` |
| Đặt hàng | `curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d "{\"customerEmail\":\"a@example.com\",\"lines\":[{\"sku\":\"SKU-001\",\"quantity\":2}]}"` |

Hành vi đúng mà ứng dụng cần có:
- Sản phẩm bị thu hồi không được bán.
- Mỗi đơn có mã số duy nhất.
- Thanh toán bị từ chối thì không được để lại đơn nào trong hệ thống.
- Liệt kê N đơn không được tốn N câu SQL.
- API không được lộ thông tin nội bộ của doanh nghiệp.

Dữ liệu seed: 5 sản phẩm (`SKU-001` đến `SKU-005`) và 30 đơn. Cổng thanh toán giả lập từ chối đơn trên 1000.

## Cách chấm
Mỗi bug 20 điểm: tìm được 5, test chứng minh 5, sửa đúng 5, giải thích đúng 5. Tổng 100.
Dùng gợi ý thì trừ 2 điểm mỗi lần.
