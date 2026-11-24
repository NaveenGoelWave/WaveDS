import axios, { isAxiosError } from "axios";
import { getToken } from "../features/auth/storage";

export const api = axios.create({ baseURL: "/api/v1" });

let onUnauthorized: (() => void) | null = null;

/** The auth provider registers a callback here so an expired token signs the user out everywhere. */
export function setUnauthorizedHandler(handler: (() => void) | null): void {
  onUnauthorized = handler;
}

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: unknown) => {
    if (isAxiosError(error) && error.response?.status === 401) {
      // A 401 from the login/signup calls just means bad credentials; only other 401s mean "session expired".
      const url = error.config?.url ?? "";
      if (!url.startsWith("/auth/login") && !url.startsWith("/auth/signup")) {
        onUnauthorized?.();
      }
    }
    return Promise.reject(error);
  },
);

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
