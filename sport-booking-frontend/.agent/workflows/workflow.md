---
description:
---

# WORKFLOW (QUY TRÌNH LÀM VIỆC)

Trước khi viết bất kỳ dòng code nào, bạn phải tuân thủ quy trình suy nghĩ sau:

## BƯỚC 1: ĐỌC & HIỂU (Context Awareness)

-   Đọc kỹ nội dung file hiện tại.
-   Xác định Feature Module đang làm việc.
-   Nếu refactor: Đảm bảo hiểu logic cũ, không xóa code bừa bãi.

## BƯỚC 2: KẾ HOẠCH (Architecture Planning)

-   Xác định Interface/Type trước (Contract First).
-   Xác định State cần thiết (Server state hay Local state?).
-   Chọn Component UI phù hợp từ Shadcn (tránh viết mới nếu không cần thiết).

## BƯỚC 3: THỰC THI (Implementation)

-   QUAN TRỌNG: Nếu được yêu cầu code (VD: hãy code, hãy xây dựng,...) hãy sinh ra code. Nếu không, không được đụng vào các file và thay đổi, xoá hoặc thêm bất kỳ dữ liệu nào.
-   Viết Schema Validation (Zod) -> Viết Hook Logic -> Viết UI.
-   Luôn bọc API call trong try-catch hoặc error callback của React Query để hiện Toast thông báo lỗi.
-   Sử dụng hàm `cn()` để merge class Tailwind.

## BƯỚC 4: RÀ SOÁT (Review)

-   Kiểm tra lại các quy tắc cấm (No any, No useEffect fetch).
-   Đảm bảo code clean, tách biệt file nếu file quá 200 dòng.
