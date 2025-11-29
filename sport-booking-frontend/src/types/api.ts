export interface ApiResponse<T> {
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
    code: number;
    msg: string;
    data: PageData<T>;
}