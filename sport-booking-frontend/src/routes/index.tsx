import { LoginPage } from "@/features/auth/pages/LoginPage";
import { RegisterPage } from "@/features/auth/pages/RegisterPage";
import type { Role } from "@/features/auth/types";
import { Home } from "lucide-react";
import type { JSX } from "react";

interface RouteConfig {
    path: string;
    element?: JSX.Element;
    layout?: "public" | "user" | "vendor" | "admin";
    requiredRole?: Role;
    children?: RouteConfig[];
}

const routes: RouteConfig[] = [
    // Public routes
    {
        path: "/",
        layout: "public",
        children: [
            { path: "/login", element: <LoginPage /> },
            { path: "/register", element: <RegisterPage /> },
        ],
    },

    // User routes
    {
        path: "/user",
        layout: "user",
        requiredRole: "USER",
        children: [
            { path: "home", element: <Home /> },
            { path: "venues", element: <VenueListPage /> },
            { path: "bookings", element: <MyBookingsPage /> },
        ],
    },

    // Vendor routes
    {
        path: "/vendor",
        layout: "vendor",
        requiredRole: "VENDOR",
        children: [
            { path: "dashboard", element: <VenueDashboard /> },
            { path: "venues", element: <ManageVenues /> },
        ],
    },

    // Admin routes
    {
        path: "/admin",
        layout: "admin",
        requiredRole: "ADMIN",
        children: [{ path: "dashboard", element: <DashboardPage /> }],
    },
];
