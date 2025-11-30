import { Navigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

interface Props {
  children: React.ReactNode;
}

export const AuthenticatedRoute = ({ children }: Props) => {
  const { isAuthenticated, user } = useAuth();

  if (isAuthenticated && user?.role === "ROLE_USER") {
    return <Navigate to="/" replace />;
  } else if (isAuthenticated && user?.role === "ROLE_VENDOR") {
    return <Navigate to="/dashboard" replace />;
  } else if (isAuthenticated && user?.role === "ROLE_ADMIN") {
    return <Navigate to="/admin" replace />;
  }

  return <>{children}</>;
};