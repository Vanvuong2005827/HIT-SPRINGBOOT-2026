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

### Class Diagram:

```mermaid
classDiagram
    class PaymentMethod {
        <<interface>>
        +pay(amount: double) void
        +getMethodName() String
    }
    
    class MoMoPayment {
        +pay(amount: double) void
        +getMethodName() String
    }
    
    class BankTransferPayment {
        +pay(amount: double) void
        +getMethodName() String
    }
    
    class CashPayment {
        +pay(amount: double) void
        +getMethodName() String
    }
    
    PaymentMethod <|.. MoMoPayment
    PaymentMethod <|.. BankTransferPayment
    PaymentMethod <|.. CashPayment
    
    class NotificationService {
        <<interface>>
        +sendNotification(to: String, message: String) void
    }
    
    class EmailNotification {
        +sendNotification(to: String, message: String) void
    }
    
    class SmsNotification {
        +sendNotification(to: String, message: String) void
    }
    
    NotificationService <|.. EmailNotification
    NotificationService <|.. SmsNotification
    
    class OrderService {
        -paymentMethod: PaymentMethod
        -notificationService: NotificationService
        +OrderService(paymentMethod: PaymentMethod, notificationService: NotificationService)
        +processOrder(customer: String, product: String, amount: double) void
    }
    
    OrderService --> PaymentMethod : dependency injection
    OrderService --> NotificationService : dependency injection
```

### Nộp bài:
- Push lên GitHub, hạn nộp 23h00p 19/03/2026
