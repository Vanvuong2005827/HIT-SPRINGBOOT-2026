# Buổi 1: Nền tảng Java, OOP & Khởi động Spring Boot

---

## Mục tiêu buổi học

- Ôn tập kiến thức nền tảng OOP trong Java
- Nắm được các tính năng mới trong Java 17 – 21
- Cài đặt môi trường phát triển: JDK, JAVA_HOME, Maven, IDE
- Hiểu hệ sinh thái Spring và tư duy "Container quản lý Object"
- Hiểu IoC/DI là gì, vì sao giúp code dễ test – dễ mở rộng
- Setup project Spring Boot và chạy API đầu tiên

---

## I. Kiến thức nền tảng OOP

### 1. Bốn tính chất của OOP

#### a. Encapsulation (Đóng gói)

- Ẩn dữ liệu bên trong class, chỉ cho phép truy cập thông qua các phương thức public (getter/setter)
- Bảo vệ dữ liệu khỏi bị truy cập và sửa đổi trái phép

```java
public class Student {
    private String name;  // Ẩn dữ liệu
    private int age;

    public String getName() {      // Truy cập qua getter
        return name;
    }

    public void setAge(int age) {  // Kiểm soát qua setter
        if (age > 0 && age < 150) {
            this.age = age;
        }
    }
}
```

#### b. Inheritance (Kế thừa)

- Class con kế thừa thuộc tính và phương thức từ class cha
- Tái sử dụng code, giảm trùng lặp

```java
public class Animal {
    protected String name;

    public void eat() {
        System.out.println(name + " đang ăn");
    }
}

public class Dog extends Animal {
    public void bark() {
        System.out.println(name + " đang sủa");
    }
}
```

#### c. Polymorphism (Đa hình)

- Cùng một phương thức nhưng hành vi khác nhau tùy vào đối tượng cụ thể
- Gồm: **Compile-time** (overloading) và **Runtime** (overriding)

```java
public class Animal {
    public void sound() {
        System.out.println("Animal makes a sound");
    }
}

public class Cat extends Animal {
    @Override
    public void sound() {
        System.out.println("Mèo kêu meo meo");
    }
}

public class Dog extends Animal {
    @Override
    public void sound() {
        System.out.println("Chó sủa gâu gâu");
    }
}

// Runtime polymorphism
Animal animal = new Cat();
animal.sound();  // Output: Mèo kêu meo meo
```

#### d. Abstraction (Trừu tượng)

- Ẩn chi tiết triển khai, chỉ hiển thị những gì cần thiết
- Thực hiện qua **abstract class** hoặc **interface**

```java
public abstract class Shape {
    abstract double area();  // Chỉ khai báo, không triển khai

    public void display() {
        System.out.println("Diện tích: " + area());
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}
```

### 2. Interface vs Abstract Class

| Tiêu chí | Interface | Abstract Class |
|---|---|---|
| Từ khóa | `interface` | `abstract class` |
| Đa kế thừa | Có (implements nhiều interface) | Không (chỉ extends 1 class) |
| Constructor | Không có | Có |
| Biến | Chỉ `public static final` | Mọi loại biến |
| Phương thức | Mặc định `abstract`, có thể `default`/`static` (Java 8+) | Có cả abstract và non-abstract |
| Khi nào dùng | Định nghĩa **hành vi** (what) | Chia sẻ **code chung** giữa các class liên quan |

### 3. SOLID Principles (Tổng quan)

| Nguyên tắc | Ý nghĩa |
|---|---|
| **S** – Single Responsibility | Mỗi class chỉ nên có 1 lý do để thay đổi |
| **O** – Open/Closed | Mở cho mở rộng, đóng cho sửa đổi |
| **L** – Liskov Substitution | Class con có thể thay thế class cha mà không làm sai logic |
| **I** – Interface Segregation | Không ép class implement interface mà nó không dùng |
| **D** – Dependency Inversion | Phụ thuộc vào abstraction, không phụ thuộc vào implementation |

> **Lưu ý:** Nguyên tắc **D – Dependency Inversion** chính là nền tảng tư tưởng cho **Dependency Injection** trong Spring. Ta sẽ đi sâu ở phần sau.

---

## II. Java 17 – 21: Các tính năng mới cần biết

> Tham khảo chi tiết: [Java version history – Wikipedia](https://en.wikipedia.org/wiki/Java_version_history#Java_SE_17_(LTS))

Java 17 và Java 21 là hai phiên bản **LTS (Long-Term Support)** — được khuyến nghị sử dụng trong production.

### 1. Text Blocks (Java 13+, chính thức Java 15)

```java
// Trước đây
String json = "{\n" +
              "  \"name\": \"HIT\",\n" +
              "  \"club\": \"Spring Boot\"\n" +
              "}";

// Text Block
String json = """
        {
          "name": "HIT",
          "club": "Spring Boot"
        }
        """;
```

### 2. `var` – Local Variable Type Inference (Java 10+)

```java
// Trước
List<String> names = new ArrayList<>();

// Dùng var — compiler tự suy luận kiểu
var names = new ArrayList<String>();
var count = 10;         // int
var message = "Hello";  // String
```

> **Lưu ý:** `var` chỉ dùng cho biến cục bộ, không dùng cho field, parameter, hay return type.

### 3. Records (Java 16+)

- Class bất biến (immutable) chỉ chứa dữ liệu, tự sinh `constructor`, `getter`, `equals()`, `hashCode()`, `toString()`

```java
// Trước đây: phải viết cả đống boilerplate
public class Student {
    private final String name;
    private final int age;
    // constructor, getter, equals, hashCode, toString...
}

// Dùng Record — chỉ 1 dòng
public record Student(String name, int age) {}

// Sử dụng
var student = new Student("Nguyen Van A", 20);
System.out.println(student.name());  // Nguyen Van A
System.out.println(student);         // Student[name=Nguyen Van A, age=20]
```

### 4. Sealed Classes (Java 17)

- Giới hạn class nào được phép kế thừa

```java
public sealed class Shape permits Circle, Rectangle, Triangle {}

public final class Circle extends Shape { }
public final class Rectangle extends Shape { }
public non-sealed class Triangle extends Shape { }  // Cho phép kế thừa tiếp
```

### 5. Pattern Matching for `switch` (Java 17 preview → Java 21 chính thức)

```java
// Trước (instanceof + cast thủ công)
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Pattern matching switch (Java 21)
switch (obj) {
    case Integer i -> System.out.println("Số nguyên: " + i);
    case String s  -> System.out.println("Chuỗi dài: " + s.length());
    case null      -> System.out.println("Null!");
    default        -> System.out.println("Kiểu khác");
}
```

### 6. Virtual Threads (Java 21)

- Thread siêu nhẹ, cho phép tạo hàng triệu thread mà không tốn tài nguyên như platform thread truyền thống
- Rất hữu ích cho các ứng dụng I/O-bound (web server, database calls...)

```java
// Tạo virtual thread
Thread.startVirtualThread(() -> {
    System.out.println("Hello from virtual thread!");
});

// Hoặc dùng Executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println("Task 1"));
    executor.submit(() -> System.out.println("Task 2"));
}
```

> **Trong Spring Boot 3.2+**, có thể bật Virtual Threads bằng:
> ```properties
> spring.threads.virtual.enabled=true
> ```

---

## III. Cài đặt môi trường phát triển

### 1. Cài đặt JDK 17 hoặc 21

- Tải JDK tại:
  - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
  - OpenJDK (Adoptium): https://adoptium.net/

- Chọn phiên bản **JDK 17** hoặc **JDK 21** (LTS) phù hợp với hệ điều hành

### 2. Cấu hình JAVA_HOME trên Windows

**Bước 1:** Mở System Environment Variables

- Nhấn `Win + R` → gõ `sysdm.cpl` → Enter
- Chọn tab **Advanced** → **Environment Variables**

**Bước 2:** Thêm biến `JAVA_HOME`

- Ở phần **System variables** → **New**
  - Variable name: `JAVA_HOME`
  - Variable value: `C:\Program Files\Java\jdk-21` (đường dẫn tới thư mục JDK)

**Bước 3:** Thêm vào `Path`

- Chọn biến `Path` → **Edit** → **New**
- Thêm: `%JAVA_HOME%\bin`

**Bước 4:** Kiểm tra

Mở **Command Prompt** hoặc **PowerShell** và chạy:

```bash
java -version
# Kết quả mong đợi: java version "21.x.x" ...

javac -version
# Kết quả mong đợi: javac 21.x.x

echo %JAVA_HOME%
# Kết quả: C:\Program Files\Java\jdk-21
```

### 3. Cài đặt Maven

- Maven là build tool quản lý dependency và build/run/deploy project Java
- Maven sử dụng file `pom.xml` (Project Object Model) để cấu hình project
- Tải Maven: https://maven.apache.org/download.cgi
- Hướng dẫn cài đặt chi tiết: https://stackjava.com/install/maven-phan-1-maven-la-gi-cai-dat-maven.html

Sau khi cài, kiểm tra:

```bash
mvn -version
# Kết quả: Apache Maven 3.x.x ...
```

> **Lưu ý:** Spring Boot sử dụng Maven Wrapper (`mvnw`) nên không bắt buộc cài Maven toàn cục, nhưng nên cài để tiện dùng.

### 4. Cài đặt IDE

**Khuyến nghị:** IntelliJ IDEA Community Edition (miễn phí)  
- Tải tại: https://www.jetbrains.com/idea/download/

**Hoặc:** Visual Studio Code + Extension Pack for Java  
- Tải VS Code: https://code.visualstudio.com/
- Cài extension: **Extension Pack for Java** và **Spring Boot Extension Pack**

---

## IV. Spring Framework & Spring Boot

### 1. Spring Framework là gì?

- Spring là một **Java framework** siêu to và khổng lồ, được chia thành nhiều module, mỗi module làm một chức năng riêng
- Spring được xây dựng dựa trên 2 khái niệm nền tảng: **Dependency Injection** và **[AOP](https://docs.spring.io/spring-framework/reference/core/aop.html)** (Aspect Oriented Programming)

### 2. Spring Boot là gì?

- Tạo một ứng dụng Spring thuần khá vất vả: cấu hình XML, setup server, quản lý thư viện thủ công...
- **Spring Boot ra đời** để khắc phục điều này — giúp tạo ứng dụng Spring nhanh chóng với cấu hình tối thiểu

| Spring Framework | Spring Boot |
|---|---|
| Cấu hình thủ công (XML, Java Config) | Auto-configuration |
| Phải setup server riêng (Tomcat) | Embedded server (Tomcat tích hợp sẵn) |
| Quản lý dependency phức tạp | Spring Starter dependencies |
| Khó khởi tạo project | Spring Initializr (start.spring.io) |

### 3. Hệ sinh thái Spring

- **Spring MVC:** Phát triển ứng dụng web theo mô hình MVC
- **Spring Security:** Xác thực (authentication) và phân quyền (authorization)
- **Spring Data:** Truy cập dữ liệu (JPA, MongoDB, Redis...)
- **Spring Boot:** Khởi tạo và chạy ứng dụng nhanh chóng, độc lập
- **Spring Cloud:** Xây dựng hệ thống microservices

### 4. Tư duy "Container quản lý Object"

> Trong Spring, **bạn không tự tạo object** — **Spring Container tạo và quản lý object cho bạn.**

- Spring Boot tạo ra một container gọi là **ApplicationContext**
- Container này quản lý một không gian lưu trữ gọi là **Context**
- Khi ứng dụng khởi động, container **tìm kiếm các Bean** trong project và đưa vào Context
- **Bean** = một object được quản lý bởi Spring Container (thay vì dev tự `new`)

```
┌───────────────────────────────────────────┐
│           ApplicationContext              │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐    │
│  │ Bean A  │ │ Bean B  │ │ Bean C  │    │
│  └─────────┘ └─────────┘ └─────────┘    │
│                                           │
│  Khi class X cần Bean A → Container tự   │
│  inject Bean A vào class X               │
└───────────────────────────────────────────┘
```

### 5. Web Server & Apache Tomcat

- **Web Server** là máy chủ tiếp nhận request từ client và trả về response qua HTTP
- Cách hoạt động: khởi động máy chủ → nhận request → xử lý → trả về response → kết thúc phiên

![Web Server](https://topdev.vn/blog/wp-content/uploads/2019/05/webserver-la-gi.jpg)

- **Apache Tomcat** là web server phổ biến nhất cho Java
- Spring Boot **tích hợp sẵn Tomcat** — không cần cài đặt riêng

Luồng hoạt động:
```
Client (Browser/Postman) → HTTP Request → Tomcat (Embedded) → Spring xử lý → HTTP Response → Client
```

---

## V. IoC & Dependency Injection

![IoC và DI](https://toidicodedao.files.wordpress.com/2015/09/ioc-and-mapper-in-c-1-638.jpg)

### 1. Dependency Injection (DI) là gì?

- DI là một **design pattern** giúp giảm sự phụ thuộc chặt chẽ (tight coupling) giữa các class
- Thay vì class tự tạo dependency bên trong, dependency được **truyền từ bên ngoài vào** (inject)
- DI là cách triển khai nguyên tắc **Dependency Inversion** trong SOLID

### 2. Inversion of Control (IoC) là gì?

- IoC là nguyên tắc **đảo ngược quyền kiểm soát**: thay vì dev tự quản lý vòng đời object, ta giao cho **framework** (Spring Container) quản lý
- **IoC Container** = ApplicationContext — kho chứa tất cả các Bean
- Khi class cần dependency → Container tự tìm Bean phù hợp và inject vào

### 3. Vì sao DI giúp code dễ test – dễ mở rộng?

#### Dễ test (Testability)

```java
// KHÔNG dùng DI — khó test
public class OrderService {
    private EmailService emailService = new EmailService(); // hard-coded

    public void placeOrder() {
        // ... logic
        emailService.sendEmail("Đặt hàng thành công");
    }
}
// → Khi test, KHÔNG THỂ thay EmailService bằng mock/fake

// CÓ DI — dễ test
public class OrderService {
    private final EmailService emailService;

    public OrderService(EmailService emailService) {
        this.emailService = emailService;  // inject từ bên ngoài
    }

    public void placeOrder() {
        // ... logic
        emailService.sendEmail("Đặt hàng thành công");
    }
}
// → Khi test, truyền MockEmailService vào → test nhanh, không gửi email thật
```

#### Dễ mở rộng (Extensibility)

```java
// Interface
public interface NotificationService {
    void send(String message);
}

// Implementation 1
@Component
public class EmailNotification implements NotificationService {
    public void send(String message) { /* gửi email */ }
}

// Implementation 2 — thêm sau, KHÔNG sửa code cũ
@Component
public class SmsNotification implements NotificationService {
    public void send(String message) { /* gửi SMS */ }
}

// OrderService chỉ phụ thuộc vào interface → đổi implementation không sửa code
```

### 4. Ba cách thực hiện DI trong Spring

#### a. Constructor Injection ✅ (Khuyến nghị)

```java
@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired  // Có thể bỏ @Autowired nếu class chỉ có 1 constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

#### b. Setter Injection

```java
@Component
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

#### c. Field Injection ⚠️ (Không khuyến nghị)

```java
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    // Khó test vì không thể truyền mock từ bên ngoài
}
```

> **Best Practice:** Ưu tiên **Constructor Injection** vì:
> - Dependency rõ ràng, bắt buộc
> - Hỗ trợ `final` field → immutable, an toàn
> - Dễ viết unit test (truyền mock qua constructor)

### 5. Ví dụ minh họa: Từ tight coupling → DI → IoC

#### Bước 1: Trước khi áp dụng DI — tạo object thủ công

```java
public class User {
    private IPhone iphone;

    public User() {
        iphone = new IPhone();  // Phụ thuộc trực tiếp vào IPhone
    }
}
```

**Vấn đề:**
- User bị ràng buộc chặt vào IPhone
- Muốn đổi sang Samsung → phải sửa code trong User
- Vi phạm nguyên tắc **Dependency Inversion**

#### Bước 2: Áp dụng DI — giảm sự phụ thuộc

```java
public interface Phone {
    void call();
}

public class IPhone implements Phone {
    @Override
    public void call() {
        System.out.println("Gọi điện bằng iPhone!");
    }
}

public class Samsung implements Phone {
    @Override
    public void call() {
        System.out.println("Gọi điện bằng Samsung!");
    }
}

// User phụ thuộc vào interface Phone, không phụ thuộc vào class cụ thể
public class User {
    private Phone phone;

    public User(Phone phone) {  // Inject qua constructor
        this.phone = phone;
    }

    public void makeCall() {
        phone.call();
    }

    public static void main(String[] args) {
        Phone phone = new Samsung();  // Dễ dàng thay đổi implementation
        User user = new User(phone);
        user.makeCall();  // Output: Gọi điện bằng Samsung!
    }
}
```

**Ưu điểm:** Giảm coupling, dễ mở rộng  
**Nhược điểm:** Vẫn phải tự tay tạo object và inject — cồng kềnh khi có nhiều dependency

#### Bước 3: IoC Container — Spring tự động quản lý

```java
@Component
interface Phone {
    void call();
}

@Component
@Primary
class IPhone implements Phone {
    @Override
    public void call() {
        System.out.println("Gọi điện bằng iPhone!");
    }
}

@Component
class Samsung implements Phone {
    @Override
    public void call() {
        System.out.println("Gọi điện bằng Samsung!");
    }
}

@Component
class User {
    private Phone phone;

    @Autowired
    public User(@Qualifier("samsung") Phone phone) {
        this.phone = phone;
    }

    public void makeCall() {
        phone.call();
    }
}

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.example");
        User user = context.getBean(User.class);  // Spring tự inject Samsung vào User
        user.makeCall();  // Output: Gọi điện bằng Samsung!
    }
}
```

**Lợi ích IoC Container:**
- Tự động quản lý dependency — không cần tự tay `new`
- Thay đổi implementation chỉ qua annotation/config, không sửa business code

### 6. Các Annotation cơ bản

| Annotation | Công dụng |
|---|---|
| `@Component` | Đánh dấu class là một Bean, Spring sẽ tự tạo instance và đưa vào Container |
| `@Autowired` | Yêu cầu Spring tự inject dependency phù hợp |
| `@Qualifier("name")` | Chỉ định rõ Bean nào khi có nhiều Bean cùng loại |
| `@Primary` | Đánh dấu Bean ưu tiên khi có nhiều Bean cùng loại |

---

## Tổng kết Buổi 1

| Chủ đề | Nội dung chính |
|---|---|
| OOP | 4 tính chất: Encapsulation, Inheritance, Polymorphism, Abstraction |
| Java 17–21 | Records, Sealed Classes, Pattern Matching Switch, Virtual Threads |
| Môi trường | JDK 17/21, JAVA_HOME, Maven, IDE (IntelliJ / VS Code) |
| Spring | Framework lớn, chia nhiều module, nền tảng là DI + AOP |
| Spring Boot | Giúp tạo ứng dụng Spring nhanh, auto-config, embedded Tomcat |
| IoC / DI | Container quản lý object, inject dependency tự động → dễ test, dễ mở rộng |

---

## Câu hỏi

1. Tại sao không dùng `static` thay cho Bean / DI?
2. `@Autowired` trên field hoạt động được, sao lại không khuyến nghị?
3. Nếu có 2 Bean cùng implement 1 interface mà không dùng `@Primary` hay `@Qualifier` thì sao?
4. Bean mặc định là Singleton — vậy nếu 2 thread cùng truy cập 1 Bean thì có lỗi không?
5. `new` một object có `@Component` thì Spring có quản lý nó không?
6. Abstract class có thể là Bean không?
7. Interface có được đánh dấu `@Component` không?
8. Khi nào dùng `@Bean` thay vì `@Component`?

---

## Bài tập thực hành

1. **Cài đặt môi trường:** Cài JDK 21, cấu hình JAVA_HOME, cài Maven, kiểm tra bằng terminal
2. **Ôn tập OOP:** Tạo một chương trình Java minh họa 4 tính chất OOP (tạo abstract class `Shape`, các class con `Circle`, `Rectangle` kế thừa, override method `area()`)
3. **Thực hành DI thủ công:** Tạo interface `Phone` với method `call()`, implement 2 class `IPhone` và `Samsung`, tạo class `User` nhận `Phone` qua constructor → chạy trong `main()` và thử đổi implementation
4. **Tìm hiểu thêm:**
   - Bean là gì? Bean Scope (singleton, prototype, request, session)
   - Bean Lifecycle — vòng đời của Bean (`@PostConstruct`, `@PreDestroy`)
   - Sự khác nhau giữa `@Component`, `@Service`, `@Repository`, `@Controller`
   - `@Bean` trong `@Configuration` class — tạo Bean thủ công cho thư viện bên thứ 3
