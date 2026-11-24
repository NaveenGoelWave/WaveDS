import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { setUnauthorizedHandler } from "../../../api/client";
import { authApi } from "../api/authApi";
import { clearAuth, loadAuth, saveAuth } from "../storage";
import type { AuthResponse, AuthUser, LoginRequest, SignupRequest, StoredAuth } from "../types";

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (body: LoginRequest) => Promise<void>;
  signup: (body: SignupRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const queryClient = useQueryClient();
  const [auth, setAuth] = useState<StoredAuth | null>(() => loadAuth());

  const logout = useCallback(() => {
    clearAuth();
    setAuth(null);
    queryClient.clear(); // never show one user's cached data to the next person
  }, [queryClient]);

  useEffect(() => {
    setUnauthorizedHandler(logout);
    return () => setUnauthorizedHandler(null);
  }, [logout]);

  const accept = useCallback((res: AuthResponse) => {
    const stored: StoredAuth = { token: res.token, user: res.user };
    saveAuth(stored);
    setAuth(stored);
  }, []);

  const login = useCallback(async (body: LoginRequest) => accept(await authApi.login(body)), [accept]);
  const signup = useCallback(async (body: SignupRequest) => accept(await authApi.signup(body)), [accept]);

  const value = useMemo<AuthContextValue>(
    () => ({ user: auth?.user ?? null, isAuthenticated: auth !== null, login, signup, logout }),
    [auth, login, signup, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside <AuthProvider>");
  return ctx;
}
