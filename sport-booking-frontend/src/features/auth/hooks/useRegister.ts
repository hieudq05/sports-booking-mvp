import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/auth.service';
import type { RegisterRequest } from '../types';

export const useRegister = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: RegisterRequest) => authService.register(data),
    onSuccess: () => {
      // Chuyển về trang login sau khi đăng ký thành công
      navigate('/login', {
        state: { message: 'Đăng ký thành công! Vui lòng đăng nhập.' }
      });
    },
  });
};