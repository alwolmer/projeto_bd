export interface AuthStore {
  token: string | null;
  refreshToken: string | null;
  setToken: (token: string | null) => void;
  setRefreshToken: (refreshToken: string | null) => void;
}

export interface LoginResponse {
  access: string;
  refresh: string;
}

export interface Employee {
  cpf?: string;
  name: string;
  email: string;
  phone: string;
  isManager: boolean;
  managerCpf?: string | null;
  passwordHash?: string;
}
