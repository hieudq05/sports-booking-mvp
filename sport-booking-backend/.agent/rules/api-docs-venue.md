# Tài liệu API - Module Quản lý Địa điểm (Venue)

Đây là tài liệu mô tả các API liên quan đến việc quản lý địa điểm, sân bãi.

**URL Cơ sở:** `http://localhost:8080`
**Tiền tố:** `/api/v1`

---

## 1. API Công khai (Public)

Các API này có thể được gọi mà không cần xác thực.

### 1.1. Lấy danh sách Địa điểm

Lấy danh sách các địa điểm đang hoạt động, hỗ trợ phân trang và sắp xếp.

- **Method:** `GET`
- **Endpoint:** `/api/v1/venues`
- **Permissions:** `PUBLIC`

#### Query Parameters

| Tên | Kiểu | Bắt buộc | Mặc định | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| `page` | `int` | Không | `0` | Số thứ tự trang (bắt đầu từ 0). |
| `size` | `int` | Không | `10` | Số lượng mục trên mỗi trang. |
| `sort` | `string` | Không | `createdAt,desc` | Sắp xếp. Định dạng: `propertyName,direction`. Ví dụ: `name,asc`. |

#### Success Response (200 OK)

```json
{
  "code": 200,
  "msg": "Fetched all active venues successfully.",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Sân Cầu Lông ABC",
        "thumbnail": "http://example.com/image.jpg",
        "address": "123 Đường ABC, Quận 1",
        "district": "Quận 1",
        "status": "ACTIVE"
      }
    ],
    "meta": {
      "pageNo": 0,
      "pageSize": 10,
      "totalElements": 1,
      "totalPages": 1
    }
  }
}
```

### 1.2. Lấy chi tiết một Địa điểm

- **Method:** `GET`
- **Endpoint:** `/api/v1/venues/{id}`
- **Permissions:** `PUBLIC`

#### Path Parameters

| Tên | Kiểu | Mô tả |
| :--- | :--- | :--- |
| `id` | `long` | ID của địa điểm cần xem. |

#### Success Response (200 OK)

```json
{
  "code": 200,
  "msg": "Fetched venue successfully.",
  "data": {
    "id": 1,
    "name": "Sân Cầu Lông ABC",
    "address": "123 Đường ABC, Quận 1",
    "district": "Quận 1",
    "description": "Mô tả chi tiết về sân.",
    "thumbnail": "http://example.com/image.jpg",
    "status": "ACTIVE",
    "openTime": "08:00:00",
    "closeTime": "22:00:00",
    "owner": {
      "id": 11,
      "username": "vendor_user",
      "fullName": "Nguyễn Văn B"
    },
    "courts": [
      {
        "id": 101,
        "name": "Sân 1",
        "type": "BADMINTON",
        "pricePerHour": 100000.00,
        "capacity": 4,
        "thumbnail": "http://example.com/court1.jpg",
        "status": "AVAILABLE"
      }
    ],
    "images": [
      {
        "id": 201,
        "imageUrl": "http://example.com/gallery1.jpg"
      }
    ]
  }
}
```

#### Error Response

- **404 Not Found:** Nếu `id` không tồn tại.

---

## 2. API cho Chủ sân (Vendor)

Các API này yêu cầu xác thực (gửi `Bearer Token`) và quyền hạn phù hợp.

### 2.1. Tạo Địa điểm mới

- **Method:** `POST`
- **Endpoint:** `/api/v1/venues`
- **Permissions:** `VENDOR`

#### Request Body

```json
{
  "name": "Sân Cầu Lông Mới",
  "address": "456 Đường XYZ, Quận 2",
  "district": "Quận 2",
  "description": "Mô tả sân mới.",
  "thumbnail": "http://example.com/new_thumb.jpg",
  "openTime": "07:00",
  "closeTime": "23:00",
  "images": [
    "http://example.com/new_gallery1.jpg",
    "http://example.com/new_gallery2.jpg"
  ]
}
```

#### Success Response (201 Created)

- Body tương tự mục **1.2**, trả về chi tiết của địa điểm vừa được tạo với `status: "PENDING"`.

#### Error Response

- **400 Bad Request:** Nếu dữ liệu gửi lên không hợp lệ.
- **403 Forbidden:** Nếu người dùng không có vai trò `VENDOR`.

### 2.2. Cập nhật Địa điểm

- **Method:** `PUT`
- **Endpoint:** `/api/v1/venues/{id}`
- **Permissions:** `VENDOR` (phải là chủ sở hữu) hoặc `ADMIN`.

#### Request Body

- Tương tự mục **2.1**.

#### Success Response (200 OK)

- Body tương tự mục **1.2**, trả về chi tiết của địa điểm đã được cập nhật.

#### Error Response

- **403 Forbidden:** Nếu không có quyền cập nhật.
- **404 Not Found:** Nếu `id` không tồn tại.

### 2.3. Thêm ảnh vào Địa điểm

- **Method:** `POST`
- **Endpoint:** `/api/v1/venues/{id}/images`
- **Permissions:** `VENDOR` (phải là chủ sở hữu) hoặc `ADMIN`.

#### Request Body

```json
{
  "imageUrls": [
    "http://example.com/another_image.jpg"
  ]
}
```

#### Success Response (200 OK)

- Body tương tự mục **1.2**, trả về chi tiết của địa điểm đã được cập nhật với danh sách ảnh mới.

### 2.4. Xóa ảnh của Địa điểm

- **Method:** `DELETE`
- **Endpoint:** `/api/v1/venue-images/{id}`
- **Permissions:** `VENDOR` (chủ sở hữu venue), `ADMIN`, hoặc người tạo ảnh.

#### Path Parameters

| Tên | Kiểu | Mô tả |
| :--- | :--- | :--- |
| `id` | `long` | ID của bản ghi `VenueImage` cần xóa. |

#### Success Response (200 OK)

```json
{
  "code": 200,
  "msg": "Venue image deleted successfully.",
  "data": null
}
```

---

## 3. API cho Quản trị viên (Admin)

Các API này yêu cầu quyền `ADMIN`.

### 3.1. Lấy danh sách Địa điểm theo Trạng thái

- **Method:** `GET`
- **Endpoint:** `/api/v1/admin/venues`
- **Permissions:** `ADMIN`

#### Query Parameters

| Tên | Kiểu | Bắt buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `status` | `string` | Có | Trạng thái cần lọc. Ví dụ: `PENDING`, `ACTIVE`, `REJECTED`. |
| `page`, `size`, `sort` | - | Không | Tương tự mục **1.1**. |

#### Success Response (200 OK)

- Body tương tự mục **1.1**.

### 3.2. Cập nhật Trạng thái Địa điểm

- **Method:** `PATCH`
- **Endpoint:** `/api/v1/admin/venues/{id}/status`
- **Permissions:** `ADMIN`

#### Request Body

```json
{
  "status": "ACTIVE"
}
```

#### Success Response (200 OK)

- Body tương tự mục **1.2**, trả về chi tiết của địa điểm đã được cập nhật trạng thái.
```