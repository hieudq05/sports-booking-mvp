import { useAuth } from "@/context/AuthContext";
import { Navigate, Outlet } from "react-router-dom";

export const PublicLayout = () => {
    const { isAuthenticated, user } = useAuth();

    if (isAuthenticated && user) {
        switch (user.role) {
            case "USER":
                return <Navigate to="/user/home" replace />;
            case "VENDOR":
                return <Navigate to="/vendor/dashboard" replace />;
            case "ADMIN":
                return <Navigate to="/admin/dashboard" replace />;
        }
    }

    return <Outlet />;
};
