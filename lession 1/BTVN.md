# 📝 BTVN Buổi 1

## Đề bài: Hệ thống Thanh toán Đơn hàng

Xây dựng module xử lý đơn hàng cho ứng dụng e-commerce, áp dụng **Dependency Injection** và **IoC**.

### Yêu cầu:

- Tạo interface `PaymentMethod` với method `pay(double amount)` và `getMethodName()`
- Implement 3 phương thức thanh toán: `MoMoPayment`, `BankTransferPayment`, `CashPayment`
- Tạo interface `NotificationService` với method `sendNotification(String to, String message)`
- Implement 2 cách gửi thông báo: `EmailNotification`, `SmsNotification`
- Tạo class `OrderService` nhận `PaymentMethod` và `NotificationService` qua **Constructor Injection**, có method `processOrder(String customer, String product, double amount)` để thanh toán và gửi thông báo
- Trong `main()`: tạo nhiều `OrderService` với các tổ hợp payment + notification khác nhau, chứng minh có thể **đổi implementation mà không sửa `OrderService`**
- Chuyển sang **Spring IoC**: dùng `@Component`, `@Primary`/`@Qualifier`, `@Autowired` để Spring tự inject dependency

### Nộp bài:
- Push lên GitHub, hạn nộp buổi học tiếp theo
