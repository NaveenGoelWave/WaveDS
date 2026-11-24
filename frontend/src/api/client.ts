import axios, { isAxiosError } from "axios";

export const api = axios.create({ baseURL: "/api/v1" });

/** Shape of the backend's error responses (see GlobalExceptionHandler). */
export interface ApiError {
  status: number;
  message: string;
  fieldErrors?: Record<string, string>;
}

export function toApiError(error: unknown): ApiError {
  if (isAxiosError(error)) {
    const data = error.response?.data as Partial<ApiError> | undefined;
    return {
      status: error.response?.status ?? 0,
      message: data?.message ?? (error.response ? "Request failed" : "Cannot reach the server"),
      fieldErrors: data?.fieldErrors,
    };
  }
  return { status: 0, message: "Something went wrong" };
}
