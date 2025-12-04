export interface ApiResponse<T> {
    status: number | string;
    code: number;
    msg: string;
    data: T;
}

export interface PageData<T> {
    content: T[];
    pageNo: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
}

export interface PageResponse<T> {
    code: number | string;
    msg: string;
    data: PageData<T>;
}