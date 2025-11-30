---
trigger: always_on
---

# TECH STACK (HARD CONSTRAINTS)
- **Core:** React 18+ (Vite), TypeScript 5.x.
- **Styling:** TailwindCSS (Primary), Shadcn/ui (Library), Lucide-react (Icons).
- **State Management:**
  - *Server State:* TanStack Query v5 (BẮT BUỘC cho mọi API Call).
  - *Local State:* useState, useReducer.
  - *Forbidden:* TUYỆT ĐỐI KHÔNG dùng Redux, Toolkit, Zustand (trừ khi có chỉ định override).
- **Forms:** React Hook Form + Zod (Validation Schema tách biệt).
- **Date/Time:** date-fns, react-big-calendar.
- **Routing:** React Router DOM v6.

# CODING RULES & STRUCTURE
1. **Project Structure (Feature-based):**
   - `src/features/[feature_name]`: Chứa logic nghiệp vụ (auth, wallet, booking...).
   - `src/components/ui`: Chỉ chứa Shadcn components dùng chung.
   - `src/lib`: Chứa cấu hình (axios instance, queryClient).

2. **TypeScript & Type Safety:**
   - **NO `any`:** Cấm tuyệt đối. Dùng `unknown` hoặc Generics nếu cần.
   - **Interfaces:** Dùng cho Data Objects/DTOs. Phải khớp 1:1 với Backend JSON.
   - **Types:** Dùng cho Union, Props phức tạp.

3. **State & API:**
   - **No useEffect for Fetching:** Cấm dùng useEffect để gọi API. Bắt buộc dùng `useQuery`/`useMutation`.
   - **Axios Instance:** Không dùng axios trần. Phải import từ `src/lib/axios.ts` (để auto-inject Token).
   - **Query Keys:** Phải quản lý tập trung (Factory pattern), không hardcode string rải rác.

4. **Environment:**
   - Dùng `import.meta.env.VITE_...`. Không hardcode URL Backend.