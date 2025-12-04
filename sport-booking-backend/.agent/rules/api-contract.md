1. GLOBAL STANDARD (Cấu trúc chung)
   File: src/types/api.ts

Mọi response từ Backend đều được bọc trong cấu trúc này. Frontend bắt buộc phải unpack (mở gói) nó ra.

TypeScript

// Wrapper chuẩn cho mọi API
export interface ApiResponse<T> {
code: number;      // 1000 = Success, Khác 1000 = Lỗi nghiệp vụ
message: string;   // "Success", "Username exists", v.v.
data: T;           // Dữ liệu thật nằm ở đây (có thể null nếu lỗi)
}

// Wrapper cho phân trang (Dùng cho List Transaction, List Venue)
export interface PageResponse<T> {
content: T[];      // Danh sách item
meta: {
pageNo: number;    // Trang hiện tại (0, 1, 2...)
pageSize: number;  // Số lượng item/trang
totalElements: number; // Tổng số item trong DB
totalPages: number;    // Tổng số trang
}
}

2. MODULE AUTH (Đã hoàn thành BE)
   File: src/features/auth/types.ts

Backend: AuthController

A. Đăng ký (Register)
Endpoint: POST /api/v1/auth/register

Request Body:

TypeScript

export interface RegisterRequest {
username: string;     // @NotBlank, min 3
password: string;     // @NotBlank, min 6
email: string;        // @Email
fullName: string;     // @NotBlank
phoneNumber: string;  // @Pattern digits only
}

Response Data (data của ApiResponse): UserResponse

B. Đăng nhập (Login)
Endpoint: POST /api/v1/auth/login

Request Body:

TypeScript

export interface LoginRequest {
username: string;
password: string;
}

Response Data:

TypeScript

export interface LoginResponse {
token: string;    // JWT String (Lưu vào LocalStorage)
username: string;
role: 'USER' | 'VENDOR' | 'ADMIN'; // Enum khớp với BE
userId: number;   // Dùng để query ví, booking
}

C. User Profile (DTO trả về chung)
TypeScript

export interface UserResponse {
id: number;
username: string;
email: string;
fullName: string;
phoneNumber: string;
role: 'USER' | 'VENDOR' | 'ADMIN';
status: 'ACTIVE' | 'BANNED' | 'UNVERIFIED';
}

3. MODULE WALLET (Sắp làm)
   File: src/features/wallet/types.ts

Backend: WalletController

A. Xem thông tin Ví (Balance)
Endpoint: GET /api/v1/wallet (Lấy user từ Token -> không cần truyền ID trên URL)

Response Data:

TypeScript

export interface WalletResponse {
id: number;
balance: number;   // Backend là BigDecimal -> Frontend là number
userId: number;
createdAt: string; // ISO Date: "2025-11-29T10:00:00Z"
}
B. Nạp tiền (Deposit)
Endpoint: POST /api/v1/wallet/deposit

Request Body:

TypeScript

export interface DepositRequest {
amount: number; // Min 10000
}
Response Data: WalletResponse (Trả về số dư mới nhất sau khi nạp).

C. Lịch sử giao dịch (Transaction History)
Endpoint: GET /api/v1/wallet/transactions?page=0&size=10

Response Data: PageResponse<TransactionResponse>

DTO Chi tiết:

TypeScript

export interface TransactionResponse {
id: number;
amount: number;
type: 'DEPOSIT' | 'PAYMENT' | 'REFUND' | 'RECEIVE'; // Enum
status: 'SUCCESS' | 'FAILED' | 'PENDING';
description: string;
bookingId?: number; // Có thể null nếu là nạp tiền
createdAt: string;  // Hiển thị lên UI nhớ format lại
}
4. MODULE VENUE (Sân bãi)
   File: src/features/venue/types.ts

Backend: VenueController

A. Tạo sân (Vendor)
Endpoint: POST /api/v1/venues

Request Body:

TypeScript

export interface VenueRequest {
name: string;
address: string;
district: string;       // "Ba Dinh", "Cau Giay"...
description: string;
openTime: string;       // Format "HH:mm" (VD: "07:00")
closeTime: string;      // Format "HH:mm" (VD: "22:00")
imageUrl: string;       // URL ảnh (Sau khi upload lên Cloud)
}
B. Danh sách sân (Public/Search)
Endpoint: GET /api/v1/venues/search?district=...&page=...

Response Data: PageResponse<VenueResponse>

DTO Chi tiết:

TypeScript

export interface VenueResponse {
id: number;
name: string;
address: string;
district: string;
imageUrl: string;
openTime: string;
closeTime: string;
minPrice: number; // Giá thấp nhất trong các sân con (để hiển thị "Từ 100k")
maxPrice: number;
}
C. Chi tiết sân & Sân con (Venue Detail)
Endpoint: GET /api/v1/venues/{id}

Response Data:

TypeScript

export interface VenueDetailResponse extends VenueResponse {
description: string;
courts: CourtResponse[]; // Danh sách sân con
images: VenueImageResponse[]; // Album ảnh
}

export interface CourtResponse {
id: number;
name: string;       // "Sân 1", "Sân 5 người A"
type: 'SIZE_5' | 'SIZE_7' | 'SIZE_11';
pricePerHour: number;
}

export interface VenueImageResponse {
id: number;
imageUrl: string;
}
5. MODULE BOOKING (Đặt sân)
   File: src/features/booking/types.ts

Backend: BookingController

A. Kiểm tra khung giờ trống (Availability)
Endpoint: GET /api/v1/bookings/availability

Query Params: ?venueId=1&date=2025-11-30

Response Data: SlotResponse[]

DTO Chi tiết:

TypeScript

export interface SlotResponse {
startTime: string; // "17:00" (Local Time hiển thị cho user)
endTime: string;   // "18:00"
isBooked: boolean; // true = đỏ (kín), false = xanh (trống)
price: number;     // Giá tiền slot đó
courtId: number;   // Slot này thuộc sân con nào
}
B. Đặt sân (Create Booking)
Endpoint: POST /api/v1/bookings

Request Body:

TypeScript

export interface BookingRequest {
courtId: number;
date: string;       // "2025-11-30"
startTime: string;  // "17:00"
endTime: string;    // "18:00"
paymentMethod: 'WALLET'; // MVP chỉ hỗ trợ ví
}
Response Data:

TypeScript

export interface BookingResponse {
id: number;
code: string;       // Mã vé "BK-XYZ"
status: 'CONFIRMED' | 'PENDING' | 'CANCELLED';
totalPrice: number;
bookingDate: string;
}


💡 LƯU Ý QUAN TRỌNG CHO FRONTEND DEV
Date Time:

Venue (Giờ mở cửa): Backend trả về string "07:00". Frontend hiển thị nguyên xi.

Booking (Ngày đặt): Backend cần UTC Instant hoặc LocalDate. Để đơn giản cho MVP, Frontend gửi String "YYYY-MM-DD" và "HH:mm", Backend sẽ tự ghép lại và convert sang UTC để lưu.

Money:

Backend dùng BigDecimal. JSON trả về là số (100000.00). Frontend dùng number của JS là đủ (JS an toàn đến 9 triệu tỷ).

Khi hiển thị nhớ dùng: amount.toLocaleString('vi-VN') + ' đ'.

Enum:

Hãy copy y nguyên các giá trị Enum (như USER, VENDOR, DEPOSIT) vào code Frontend. Nếu sai 1 chữ cái là lỗi ngay.