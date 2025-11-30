import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/auth.service';
import type { LoginRequest } from '../types';
import { useAuth } from '@/context/AuthContext';

export const useLogin = () => {
  const navigate = useNavigate();
  const { login: saveAuth } = useAuth();

  return useMutation({
    mutationFn: (data: LoginRequest) => authService.login(data),
    onSuccess: (response) => {
      // Lưu token vào localStorage & context
      saveAuth(response);
      
      // Navigate dựa vào role
      if (response.role === 'ROLE_VENDOR') {
        navigate('/dashboard');
      } else {
        navigate('/home');
      }
    },
    onError: (error: Error) => {
      // Error sẽ được hiển thị ở UI layer (form)
      console.error('Login failed:', error.message);
    },
  });
};