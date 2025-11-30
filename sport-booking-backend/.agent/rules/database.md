---
trigger: always_on
---

# Database

## DATABASE SCHEMA (PostgreSQL)

```sql
-- =============================================
-- 1. MODULE AUTH & USER
-- =============================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- USER, ADMIN, VENDOR
    status VARCHAR(20), -- BANNED, ACTIVE, UNVERIFIED
    full_name VARCHAR(100),
    phone_number VARCHAR(15),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- =============================================
-- 2. MODULE WALLET
-- =============================================

CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    balance DECIMAL(15, 2) DEFAULT 0 CHECK (balance >= 0),
    version BIGINT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    wallet_id BIGINT NOT NULL REFERENCES wallets(id),
    amount DECIMAL(15, 2) NOT NULL,
    type VARCHAR(30) NOT NULL,
    booking_id BIGINT,
    description TEXT,
    status VARCHAR(20) DEFAULT 'SUCCESS',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_transactions_wallet ON transactions(wallet_id);

-- =============================================
-- 3. MODULE VENUE (UPDATED)
-- =============================================

-- Bảng venues được cập nhật để phản ánh entity Venue.java
CREATE TABLE venues (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    district VARCHAR(50),
    description TEXT,
    thumbnail TEXT NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL, -- PENDING, ACTIVE, REJECTED, CLOSED
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Bảng courts được cập nhật để phản ánh entity Court.java
CREATE TABLE courts (
    id BIGSERIAL PRIMARY KEY,
    venue_id BIGINT NOT NULL REFERENCES venues(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL, -- BADMINTON, TENNIS, FOOTBALL
    price_per_hour DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    thumbnail TEXT NOT NULL,
    status VARCHAR(20) NOT NULL, -- AVAILABLE, UNDER_MAINTENANCE
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT DEFAULT 0, -- For optimistic locking
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Bảng venue_images được cập nhật để phản ánh entity VenueImage.java
CREATE TABLE venue_images (
    id BIGSERIAL PRIMARY KEY,
    venue_id BIGINT NOT NULL REFERENCES venues(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Bảng court_images được thêm mới để phản ánh entity CourtImage.java
CREATE TABLE court_images (
    id BIGSERIAL PRIMARY KEY,
    court_id BIGINT NOT NULL REFERENCES courts(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_venue_images_venue ON venue_images(venue_id);
CREATE INDEX idx_court_images_court ON court_images(court_id);


-- =============================================
-- 4. MODULE BOOKING
-- =============================================

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    court_id BIGINT NOT NULL REFERENCES courts(id),
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED, CANCELLED, COMPLETED
    payment_status VARCHAR(20) DEFAULT 'PAID', -- PAID, UNPAID, REFUNDED
    code VARCHAR(50) UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_booking_settlement ON bookings(status, end_time);
CREATE INDEX idx_booking_overlap ON bookings(court_id, start_time, end_time);

-- =============================================
-- 5. MODULE DISPUTE
-- =============================================

CREATE TABLE disputes (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    reporter_id BIGINT NOT NULL REFERENCES users(id),
    reason TEXT NOT NULL,
    evidence_image_url TEXT,
    vendor_response TEXT,
    status VARCHAR(20) DEFAULT 'PROCESSING', -- PROCESSING, RESOLVED, REJECTED
    admin_decision VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

## LƯU Ý KỸ THUẬT (Technical Note)

1.  **Constraint `CHECK (balance >= 0)`:** Tôi thêm dòng này vào bảng `wallets`. Nếu Code Java của bạn bị lỗi logic lỡ tay trừ quá tiền, Database sẽ chặn lại và báo lỗi ngay lập tức. Đây là chốt chặn an toàn cuối cùng.
2.  **Index `idx_booking_overlap`:** Khi user đặt giờ `17:00 - 18:00`, hệ thống sẽ tìm trong bảng `bookings` xem có dòng nào trùng giờ không. Index này giúp việc tìm kiếm nhanh gấp trăm lần.
3.  **`ON DELETE CASCADE`:** Tôi đã thêm vào các bảng `courts`, `venue_images`, `court_images`. Điều này có nghĩa là khi một `Venue` bị xóa, tất cả các `Court` và `VenueImage` liên quan sẽ tự động bị xóa theo. Tương tự khi xóa `Court`.

## [Bắt buộc] CẤU HÌNH PHÍA BACKEND

Cấu hình để Java biết mà chuyển giờ về UTC trước khi lưu xuống DB.

Thêm dòng này vào file `application.properties` hoặc `application.yml`:

**application.yml:**
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          time_zone: UTC
  jackson:
    time-zone: UTC
    date-format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
```

**→** **Kết quả sau khi sửa:**

- User đặt sân lúc `17:00` giờ Việt Nam (+7).
- Java nhận được, convert sang `10:00` UTC (`Instant` luôn là UTC).
- Postgres lưu `10:00+00`.
- Khi hiển thị lại cho User ở Nhật (+9), Frontend nhận `10:00Z` và tự hiển thị thành `19:00`. -> **Chính xác tuyệt đối.**
```