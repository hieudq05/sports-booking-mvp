import axiosInstance from "@/lib/axios";
import type {
    LoginRequest,
    LoginResponse,
    RegisterRequest,
    UserResponse,
} from "../types";
import type { ApiResponse } from "@/types/api";

class AuthService {
    async login(data: LoginRequest): Promise<LoginResponse> {
        const response = await axiosInstance.post<ApiResponse<LoginResponse>>(
            "/auth/login",
            data,
            {
                withCredentials: true,
            }
        );

        if (response.data.status !== "OK") {
            throw new Error(response.data.msg);
        }

        return response.data.data;
    }

    async register(data: RegisterRequest): Promise<UserResponse> {
        const response = await axiosInstance.post<ApiResponse<UserResponse>>(
            "/auth/register",
            data
        );

        if (response.data.status !== "OK") {
            throw new Error(response.data.msg);
        }

        return response.data.data;
    }

    async refreshToken(): Promise<LoginResponse> {
        const response = await axiosInstance.post<ApiResponse<LoginResponse>>(
            "/auth/refresh",
            {},
            {
                withCredentials: true,
            }
        );

        if (response.data.status !== "OK") {
            throw new Error(response.data.msg);
        }

        return response.data.data;
    }

    async logout(): Promise<void> {
        const response = await axiosInstance.post<ApiResponse<void>>(
            "/auth/logout",
            {},
            {
                withCredentials: true,
            }
        );

        if (response.data.status !== "OK") {
            throw new Error(response.data.msg);
        }

        return;
    }
}

export const authService = new AuthService();
