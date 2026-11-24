import type { StoredAuth } from "./types";

const KEY = "waveds.auth";

// localStorage can be unavailable or throw (private mode, blocked storage), so every access is guarded.

export function loadAuth(): StoredAuth | null {
  try {
    const raw = localStorage.getItem(KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw) as StoredAuth;
    return parsed.token && parsed.user ? parsed : null;
  } catch {
    return null;
  }
}

export function saveAuth(auth: StoredAuth): void {
  try {
    localStorage.setItem(KEY, JSON.stringify(auth));
  } catch {
    /* ignore: the user stays signed in for this page view only */
  }
}

export function clearAuth(): void {
  try {
    localStorage.removeItem(KEY);
  } catch {
    /* ignore */
  }
}

export function getToken(): string | null {
  return loadAuth()?.token ?? null;
}
