# Buổi 7: Giới thiệu Spring Security cơ bản

Mục tiêu buổi học:
- Giới thiệu về JWT, các loại JWT.
- Cấu hình Spring Security cơ bản.
- Bảo vệ API theo role.

## JWT

### JWT là gì?
JWT (JSON Web Token) là một tiêu chuẩn mở (RFC 7519) định nghĩa một cách an toàn để truyền thông tin giữa các bên dưới dạng một đối tượng JSON. Thông tin này được ký bằng chữ ký số hoặc mã hóa để đảm bảo tính toàn vẹn và xác thực.

#### Khi nào nên sử dụng
- Authentication: Các request phía người dùng sẽ kèm theo JWT, cho phép người dùng truy cập vào các resource mà token đó cho phép.

#### Luồng xử lý
- User thực hiện thao tác request như login.
- Authentication Server tạo JWT và trả về cho User thông qua response.
- User nhận JWT và dùng nó làm chìa khóa cho các request tiếp theo.
- API xác thực JWT đó và cấp quyền truy cập cho User.

#### No Sessions
Với JWT, hệ thống không cần giữ session trong suốt quá trình xử lý.
- No session storage: không cần lưu session nên tối ưu bộ nhớ hơn.
- Truly RESTful Services: khi không có session thì service mới gần với mô hình RESTful hơn.

### Cấu trúc của JWT
JWT bao gồm 3 phần:
1. Header: Chứa thông tin về thuật toán ký và loại token.
2. Payload: Chứa thông tin về người dùng và các thông tin khác.
3. Signature: Chứa chữ ký để xác thực token.

## Spring Security
### Spring Security là gì?
Spring Security cung cấp các tính năng xác thực và phân quyền, cũng như hỗ trợ các tiêu chuẩn và giao thức bảo mật như HTTPS, OAuth2, JWT, ...

Cơ chế hoạt động dựa trên cơ chế lọc (filter) và sự kiện (event) để can thiệp vào quá trình xử lý request và response.

### Gồm 3 phần
- Authentication
- Authorization
- Authentication Provider

#### Authentication
Là quá trình xác thực xem người dùng có quyền truy cập vào ứng dụng hay không.

Thường dựa trên các thông tin nhận dạng (identifier) và thông tin bí mật (credential) của người dùng.

- Cơ chế:
  - Form-based authentication: xác thực thông qua một form đăng nhập.
  - HTTP Basic authentication: xác thực thông qua header authorization.
  - Authentication via a custom login page: xác thực thông qua một trang đăng nhập tùy chỉnh.
  - Pre-authenticated authentication: xác thực thông qua các giá trị được cung cấp từ phía Client.
- Stateful và Stateless:
  - Stateful: hệ thống lưu thông tin xác thực của người dùng hoặc ứng dụng trong một session trên máy chủ.
  - Stateless: hệ thống chỉ sử dụng các mã token đã được ký số để xác thực, thường là JWT token.

#### Authorization
- Là quá trình xác định quyền truy cập của người dùng đối với các tài nguyên trong ứng dụng.
- Thường dựa trên các thông tin như role, group, permission, policy, ...

#### Authentication Provider
- Là một thành phần quan trọng chịu trách nhiệm xác minh thông tin xác thực của người dùng.
- Được sử dụng bởi Authentication Manager để xử lý.
- Authentication Provider chỉ hỗ trợ một loại Authentication cụ thể như:
  - UsernamePasswordAuthenticationToken
  - JwtAuthenticationToken
  - PreAuthenticatedAuthenticationToken

### Ưu và nhược điểm của Spring Security
- Ưu điểm
  - Là một framework bảo mật mạnh mẽ và linh hoạt, hỗ trợ nhiều tiêu chuẩn và giao thức bảo mật.
  - Được tích hợp sẵn với Spring Framework, giúp phát triển ứng dụng web an toàn và hiệu quả.
- Nhược điểm
  - Cấu hình có thể khá phức tạp và khó hiểu, đặc biệt khi làm việc với các tính năng nâng cao.
  - Một số tính năng có thể không phù hợp với từng loại ứng dụng web.
  - Yêu cầu kiến thức chuyên môn về bảo mật để sử dụng hiệu quả.

## Bảo vệ API theo role

### Các bước để bảo vệ API theo role
1. Tạo UserDetails và UserDetailsService để load thông tin người dùng.
2. Tạo BCryptPasswordEncoder để mã hóa mật khẩu.
3. Tạo AuthenticationProvider để xác thực người dùng.
4. Tạo SecurityConfig để cấu hình Spring Security.

## Cấu trúc thư mục Project (Base)

```text
base/
├── src/
│   ├── main/
│   │   ├── java/com/example/base/
│   │   │   ├── base/               # Các lớp base cho Response (RestData, VsResponseUtil...)
│   │   │   ├── config/             # Cấu hình hệ thống (SecurityConfig, OpenApiConfig...)
│   │   │   ├── constant/           # Các hằng số (RoleConstant, UrlConstant, SuccessMessage...)
│   │   │   ├── controller/         # Lớp xử lý request API (AuthController, UserController)
│   │   │   ├── domain/             # Chứa các đối tượng dữ liệu
│   │   │   │   ├── dto/            # Data Transfer Object (Request/Response)
│   │   │   │   ├── entity/         # Các thực thể database (User, Role, InvalidatedToken)
│   │   │   │   └── mapper/         # Chuyển đổi giữa Entity và DTO
│   │   │   ├── exception/          # Xử lý lỗi và ngoại lệ tùy chỉnh
│   │   │   ├── repository/         # Tầng giao tiếp database (UserRepository, TokenRepository)
│   │   │   ├── security/           # Cấu hình chi tiết Security (JWT, UserDetails, Filter)
│   │   │   ├── service/            # Lớp xử lý logic nghiệp vụ (AuthService, UserService)
│   │   │   └── BaseApplication.java # Class chạy chính của ứng dụng
│   │   └── resources/
│   │       └── application.properties # Cấu hình môi trường, DB, Security
│   └── test/                       # Thư mục chứa các bản kiểm thử (Unit test)
└── pom.xml                         # Quản lý dependencies (Spring Security, JWT, Lombok...)
```

---

## Tổng kết buổi học

Trong buổi học này, chúng ta đã tìm hiểu các kiến thức nền tảng về bảo mật trong ứng dụng Spring Boot:
- **JWT (JSON Web Token):** Hiểu được cấu trúc 3 phần (Header, Payload, Signature) và tại sao JWT lại phù hợp cho kiến trúc Stateless.
- **Spring Security Workflow:** Nắm bắt cách Spring Security chặn các request thông qua Filter Chain, xác thực người dùng qua Authentication Manager và phân quyền qua Authorization.
- **Cấu hình Security:** Biết cách thiết lập `SecurityConfig`, mã hóa mật khẩu bằng `BCryptPasswordEncoder` và tùy biến `UserDetailsService` để lấy thông tin từ database.
- **Phân quyền (Role-based Access Control):** Sử dụng các cấu hình để chỉ định các API nào cần đăng nhập, API nào dành cho ADMIN hoặc USER.

## Câu hỏi ôn tập

1. **JWT là gì?** Tại sao nó lại được sử dụng phổ biến trong các ứng dụng Stateless thay vì dùng Session?
2. **Phân biệt Authentication và Authorization?** Trong Spring Security, thành phần nào chịu trách nhiệm cho mỗi quá trình này?
3. **Tại sao không bao giờ nên lưu mật khẩu dạng bản rõ (plain text) trong database?** `BCryptPasswordEncoder` hoạt động như thế nào?
4. **Vai trò của `JwtAuthenticationFilter` là gì?** Tại sao nó cần phải đứng trước các filter xác thực mặc định của Spring?
5. **Làm thế nào để bảo vệ một API chỉ cho phép người dùng có Role `ADMIN` truy cập?**

## Bài tập thực hành

- **Bài 1:**: Thêm chức năng đăng ký (Register) cho người dùng mới, bao gồm việc lưu thông tin người dùng vào database và mã hóa mật khẩu. Không cần gửi OTP cho bài này.
- **Bài 2:**: Tạo các API dùng để getALLUser cho admin và updateProfile cho User.Đảm bảo rằng các API chỉ có thể truy cập bởi người dùng có role phù hợp.
- **Bài 3:**: Tự tìm hiểu về refresh token và implement chức năng refresh token trong ứng dụng. Hãy lưu ý đến invalidated token cũng như các cơ chế bảo mật như rotation token,...
