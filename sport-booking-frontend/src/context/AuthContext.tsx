import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import type { LoginResponse } from '@/features/auth/types';

interface AuthContextType {
  user: LoginResponse | null;
  isAuthenticated: boolean;
  login: (data: LoginResponse) => void;
  refreshToken: (data: string) => void;
  logout: () => void;
  getToken: () => string | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<LoginResponse | null>(null);

  // Khôi phục state từ localStorage khi app load
  useEffect(() => {
    const token = localStorage.getItem('auth_token');
    const userData = localStorage.getItem('auth_user');
    
    if (token && userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const login = (data: LoginResponse) => {
    localStorage.setItem('auth_token', data.token);
    localStorage.setItem('auth_user', JSON.stringify({
      username: data.username,
      role: data.role,
    }));
    setUser(data);
  };

  const refreshToken = (data: string) => {
    localStorage.setItem('auth_token', data);
  };

  const logout = () => {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
    setUser(null);
  };

  const getToken = () => localStorage.getItem('auth_token');

  return (
    <AuthContext.Provider value={{
      user,
      isAuthenticated: !!user,
      login,
      refreshToken,
      logout,
      getToken,
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};