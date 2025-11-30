import { z } from 'zod';

export const registerSchema = z.object({
  username: z.string()
    .min(3, 'Username phải có ít nhất 3 ký tự')
    .trim(),
  password: z.string()
    .min(6, 'Mật khẩu phải có ít nhất 6 ký tự'),
  confirmPassword: z.string(),
  email: z.string()
    .email('Email không hợp lệ'),
  fullName: z.string()
    .min(1, 'Họ tên không được để trống')
    .trim(),
  phoneNumber: z.string()
    .regex(/^\d{10,11}$/, 'Số điện thoại phải có 10-11 chữ số'),
}).refine((data) => data.password === data.confirmPassword, {
  message: 'Mật khẩu xác nhận không khớp',
  path: ['confirmPassword'],
});

export type RegisterFormData = z.infer<typeof registerSchema>;