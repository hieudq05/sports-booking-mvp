import { useAuth } from "@/context/AuthContext";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { authService } from "../services/auth.service";

export const useLogout = () => {
    const navigate = useNavigate();
    const { logout: removeAuth } = useAuth();

    return useMutation({
        mutationFn: () => authService.logout(),
        onSuccess: () => {
            removeAuth();

            navigate('/login');
        },
        onError: (error: Error) => {
            console.error('Logout failed:', error);
        }
    });
};