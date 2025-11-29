---
trigger: always_on
---

# PROJECT CONTEXT & RULES: SPORTS BOOKING SYSTEM (MVP 1.0)
## 1. TỔNG QUAN DỰ ÁN (Project Overview)
### Tên dự án: Sports Booking & Virtual Wallet System.

Mục tiêu MVP: Hệ thống đặt sân thể thao trung gian. Flow: Nạp tiền -> Đặt sân -> Giữ tiền (Escrow) -> Quyết toán tự động.

Thiết bị phát triển: Macbook Air M2 (Apple Silicon) + OrbStack (thay cho Docker Desktop).

Môi trường: Local Development hướng tới Deployment-Ready.

## 2. TECH STACK (Backend)
Language: Java 21 (LTS) - Distribution: Eclipse Temurin (Tối ưu cho ARM64).

Framework: Spring Boot 3.4.x.

Database: PostgreSQL 16 (Image: postgres:16-alpine).

Build Tool: Maven.

Core Libraries:

Lombok: Giảm boilerplate code.

MapStruct: Mapping Entity <-> DTO.

JJWT (0.11.x): Xử lý JSON Web Token.

SpringDoc OpenAPI: Swagger UI documentation.

Spring Security: Authentication & Authorization.

Validation: Hibernate Validator.

## 3. KIẾN TRÚC & CẤU TRÚC (Architecture)
Mô hình: Modular Monolith (Package by Feature).

Cấu trúc thư mục:

```plaintext
src/main/java/com/minhdev/sportbooking/
├── common/             # Các class dùng chung (Exception, ApiResponse, Utils)
├── config/             # Cấu hình (Security, Swagger, Cors)
├── modules/            # CHIA THEO TÍNH NĂNG (Feature-based)
│   ├── auth/           # Login, Register, Security Logic
│   ├── wallet/         # Ví tiền, Giao dịch
│   ├── booking/        # (Future) Đặt sân
│   └── venue/          # (Future) Quản lý sân
└── SportBookingBackendApplication.java
Quy tắc Service: KHÔNG tạo Interface cho Service (VD: IAuthService) trừ khi cần tính đa hình (Polymorphism). Dùng trực tiếp Concrete Class (AuthService).
```

## 4. QUY TẮC CODING (CODING RULES) - Bắt buộc tuân thủ
### A. Entity (JPA)
NO @Data: Tuyệt đối không dùng @Data của Lombok cho Entity để tránh lỗi StackOverflowError và hiệu năng kém khi có quan hệ 2 chiều.

Lombok Safe Usage: Chỉ dùng @Getter, @Setter, @Builder, @NoArgsConstructor, @AllArgsConstructor.

toString(): Phải Override thủ công. Chỉ in các trường đơn giản (ID, Code, Name). CẤM in các trường quan hệ (List, @ManyToOne, @OneToOne).

Audit: Dùng @CreationTimestamp và @UpdateTimestamp với kiểu dữ liệu Instant (chuẩn UTC).

Data Types:

Tiền tệ: BigDecimal.

Khóa ngoại/ID: Long (Wrapper Class).

Trạng thái/Role: Enum (UserStatus, Role).

### B. DTO & Mapper
Input (Request): Ưu tiên dùng Java record + Validation Annotations (@NotBlank, @Email).

Output (Response): Dùng Class thường với @Builder. KHÔNG trả về Entity trực tiếp trong Controller.

Mapper: Bắt buộc dùng MapStruct. Không map thủ công bằng tay.

### C. Controller & Response
Unified Response: Mọi API phải trả về cấu trúc thống nhất ApiResponse<T>.

```json
{
  "code": 1000,
  "message": "Success",
  "data": { ... }
}
```
Helper Methods: Sử dụng ApiResponse.success(data), ApiResponse.created(data), ApiResponse.error(code, msg).

Versioning: Đường dẫn API phải có version: /api/v1/auth/....

### D. Security & Auth
Stateless: Không dùng HttpSession. Dùng JWT Filter.

Secrets:

DB Password & JWT Secret KHÔNG hardcode giá trị thật trong application.yml.

Dùng placeholder: ${JWT_SECRET_KEY:unsafe_default...}.

Inject giá trị thật qua Environment Variables của IDE hoặc Docker.

User Principle: Tách biệt User (Entity) và UserDetails (Spring Security). Dùng CustomUserDetailsService để chuyển đổi.

### E. Database Rules
Timezone: Bắt buộc UTC. Cấu hình Docker command -c timezone='UTC' và Spring Boot jpa.properties.hibernate.jdbc.time_zone=UTC.

Optimistic Locking: Bảng nhạy cảm (Wallet) phải có cột @Version private Long version;.

Constraints: Luôn dùng unique, nullable trong @Column để đồng bộ với DB.

## 5. DATABASE SCHEMA (CẬP NHẬT MỚI NHẤT)
users: id, username, password (hash), email, role (ENUM), status (ENUM: ACTIVE/BANNED), created_at (TIMESTAMPTZ).

wallets: id, user_id (OneToOne), balance (DECIMAL), version (Optimistic Lock).

venues: id, image_url (Thumbnail), open_time, close_time (Local Time).

venue_images: id, venue_id, image_url (Gallery).

bookings: id, code (Unique String), start_time (UTC), end_time (UTC).

## 6. TIẾN ĐỘ ĐÃ HOÀN THÀNH (COMPLETED)
### Infrastructure
[x] Setup docker-compose.yml chạy PostgreSQL 16 (Alpine).

[x] Cấu hình IntelliJ Environment Variables cho DB và JWT Secret.

[x] Cấu hình application.yml chuẩn "Deployment-Ready" (Placeholder supports).

---

### Backend Core
[x] Init Project Spring Boot 3.4.x + Java 21.

[x] Cấu hình Global Exception Handler (AppException, Validation Error).

[x] Cấu hình ApiResponse chuẩn (Builder Pattern).

---

### Module Auth
[x] Register API:

[x] Validate Input (RegisterRequest).

[x] Check trùng Username/Email.

[x] Hash Password (BCrypt).

[x] Auto-create Wallet (0đ) ngay khi đăng ký (Transactional).

[x] Login API:

[x] Custom UserDetailsService.

[x] Xác thực qua AuthenticationManager.

[x] Generate JWT Token (HMAC-SHA256) với Hex Key an toàn.

---

### Security
[x] JwtAuthenticationFilter: Chặn request, verify token, set SecurityContext.

[x] SecurityConfig: Cấu hình Whitelist (/api/v1/auth/**), tắt CSRF, Stateless Session.

## 7. NEXT STEPS (VIỆC CẦN LÀM TIẾP THEO)
Module Wallet: Viết API Nạp tiền (Deposit) giả lập.

Module Venue: API CRUD Sân bãi (Tạo sân, thêm ảnh).

Module Booking: Logic đặt sân, kiểm tra khung giờ trống (Overlap check).