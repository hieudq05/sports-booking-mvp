import { Navigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

interface Props {
  children: React.ReactNode;
}

export const AuthenticatedRoute = ({ children }: Props) => {
  const { isAuthenticated, user } = useAuth();

  if (isAuthenticated && user?.role === "USER") {
    return <Navigate to="/" replace />;
  } else if (isAuthenticated && user?.role === "VENDOR") {
    return <Navigate to="/dashboard" replace />;
  } else if (isAuthenticated && user?.role === "ADMIN") {
    return <Navigate to="/admin" replace />;
  }

  return <>{children}</>;
};