import { api } from "../../../api/client";
import type { AuthResponse, LoginRequest, SignupRequest } from "../types";

export const authApi = {
  login: async (body: LoginRequest) => (await api.post<AuthResponse>("/auth/login", body)).data,
  signup: async (body: SignupRequest) => (await api.post<AuthResponse>("/auth/signup", body)).data,
};
