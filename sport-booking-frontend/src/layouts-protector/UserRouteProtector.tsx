import { MainLayout } from "@/components/layout/MainLayout";
import { useAuth } from "@/context/AuthContext";
import { Navigate, Outlet } from "react-router-dom";

export const UserLayout = () => {
    const { isAuthenticated, user } = useAuth();

    // Guard 1: If user is not authenticated, redirect to login
    if (!isAuthenticated) {
        return <Navigate to={"/login"} replace />;
    }

    // Guard 2: If user is not a user, redirect to it's role page
    if (user?.role !== "USER") {
        if (user?.role === "VENDOR") {
            return <Navigate to={"/vendor/dashboard"} replace />;
        } else if (user?.role === "ADMIN") {
            return <Navigate to={"/admin/dashboard"} replace />;
        }
        return <Navigate to={"/login"} replace />;
    }

    return (
        <MainLayout>
            <Outlet />
        </MainLayout>
    )
};
