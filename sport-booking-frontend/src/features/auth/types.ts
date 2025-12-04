export type Role = "USER" | "VENDOR" | "ADMIN";
export type UserStatus = "ACTIVE" | "BANNED" | "UNVERIFIED";

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    username: string;
    role: Role;
    userId: number;
}

export interface RegisterRequest {
    username: string;
    password: string;
    email: string;
    fullName: string;
    phoneNumber: string;
}

export interface UserResponse {
    id: number;
    username: string;
    email: string;
    fullName: string;
    phoneNumber: string;
    role: Role;
    status: UserStatus;
}
