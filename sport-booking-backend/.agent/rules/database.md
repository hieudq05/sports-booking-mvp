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
-- [SỬA] Dùng TIMESTAMPTZ để lưu mốc thời gian thực
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
-- 3. MODULE VENUE
-- =============================================

CREATE TABLE venues (
id BIGSERIAL PRIMARY KEY,
owner_id BIGINT NOT NULL REFERENCES users(id),
name VARCHAR(100) NOT NULL,
address TEXT NOT NULL,
district VARCHAR(50),
description TEXT,
thumbnail TEXT NOT NULL,

    -- [LƯU Ý] Giờ mở cửa giữ nguyên TIME (Không Timezone)
    -- Vì nó là giờ địa phương (Local Wall-clock time)
    open_time TIME NOT NULL,  
    close_time TIME NOT NULL, 
    
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE venue_images (
id BIGSERIAL PRIMARY KEY,
venue_id BIGINT NOT NULL REFERENCES venues(id) ON DELETE CASCADE,
image_url TEXT NOT NULL,
active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_venue_images_venue ON venue_images(venue_id);

CREATE TABLE courts (
id BIGSERIAL PRIMARY KEY,
venue_id BIGINT NOT NULL REFERENCES venues(id),
name VARCHAR(50) NOT NULL,
type VARCHAR(20) NOT NULL,
price_per_hour DECIMAL(10, 2) NOT NULL,
active BOOLEAN DEFAULT TRUE,
capacity INT NOT NULL,
thumbnail TEXT NOT NULL,
version BIGINT DEFAULT 0
);

-- =============================================
-- 4. MODULE BOOKING
-- =============================================

CREATE TABLE bookings (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL REFERENCES users(id),
court_id BIGINT NOT NULL REFERENCES courts(id),

    -- [QUAN TRỌNG] Slot đặt sân phải là tuyệt đối (UTC)
    start_time TIMESTAMPTZ NOT NULL, 
    end_time TIMESTAMPTZ NOT NULL,   
    
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED', 
    payment_status VARCHAR(20) DEFAULT 'PAID',
    code VARCHAR(50) UNIQUE
    
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
status VARCHAR(20) DEFAULT 'PROCESSING',
admin_decision VARCHAR(50),
created_at TIMESTAMPTZ DEFAULT NOW(),
updated_at TIMESTAMPTZ DEFAULT NOW()
);

---

## LƯU Ý KỸ THUẬT (Technical Note)

1. **Constraint `CHECK (balance >= 0)`:** Tôi thêm dòng này vào bảng `wallets`. Nếu Code Java của bạn bị lỗi logic lỡ tay trừ quá tiền, Database sẽ chặn lại và báo lỗi ngay lập tức. Đây là chốt chặn an toàn cuối cùng.
2. **Index `idx_booking_overlap`:** Khi user đặt giờ `17:00 - 18:00`, hệ thống sẽ tìm trong bảng `bookings` xem có dòng nào trùng giờ không. Index này giúp việc tìm kiếm nhanh gấp trăm lần.

## [Bắt buộc] CẤU HÌNH PHÍA BACKEND

Cấu hình để Java biết mà chuyển giờ về UTC trước khi lưu xuống DB.

Thêm dòng này vào file `application.properties`:

```bash
# 1. Ép Hibernate lưu xuống DB dưới dạng UTC
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# 2. Ép Jackson (Thư viện convert JSON) trả về format chuẩn ISO-8601 (có chữ Z ở cuối)
spring.jackson.time-zone=UTC
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
```

**→** **Kết quả sau khi sửa:**

- User đặt sân lúc `17:00` giờ Việt Nam (+7).
- Java nhận được, convert sang `10:00` UTC.
- Postgres lưu `10:00+00`.
- Khi hiển thị lại cho User ở Nhật (+9), Frontend nhận `10:00Z` và tự hiển thị thành `19:00`. -> **Chính xác tuyệt đối.**