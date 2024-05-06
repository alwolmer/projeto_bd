import { create } from "zustand";
import { persist } from "zustand/middleware";

import { AuthStore } from "@/types/auth";

export const useAuthStore = create<AuthStore>()(
  persist(
    (set) => ({
      token: null,
      refreshToken: null,
      setToken: (token: string | null) => set({ token }),
      setRefreshToken: (refreshToken: string | null) => set({ refreshToken }),
    }),
    {
      name: "auth-storage",
    }
  )
);
