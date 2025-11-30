import axiosInstance from "@/lib/axios";
import type { LoginRequest, LoginResponse, RegisterRequest, UserResponse } from "../types";
import type { ApiResponse } from "@/types/api";

class AuthService {
    async login(data: LoginRequest): Promise<LoginResponse> {
        const response = await axiosInstance.post<ApiResponse<LoginResponse>>(
            "/auth/login", 
            data
        );

        if (response.data.code !== 1000) {
            throw new Error(response.data.msg);
        }

        return response.data.data;
    }

    async register(data: RegisterRequest): Promise<UserResponse> {
        const response = await axiosInstance.post<ApiResponse<UserResponse>>(
            "/auth/register", 
            data
        );

        if (response.data.code !== 1000) {
            throw new Error(response.data.msg);
        }

        return response.data.data;
    }
}

export const authService = new AuthService();