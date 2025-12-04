import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";
import { VendorLayout } from "@/components/layout/VendorLayout";

export const VendorRouteProtector = () => {
    const { isAuthenticated, user } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (user?.role !== "VENDOR") {
        // Redirect based on actual role
        if (user?.role === "USER") {
            return <Navigate to="/user/home" replace />;
        }
        if (user?.role === "ADMIN") {
            return <Navigate to="/admin/dashboard" replace />;
        }
        return <Navigate to="/login" replace />;
    }

    // TODO: Add sidebar for vendor
    return (
        <VendorLayout>
            <Outlet />
        </VendorLayout>
    );
};
