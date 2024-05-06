export interface AuthStore {
  token: string | null;
  refreshToken: string | null;
  setToken: (token: string | null) => void;
  setRefreshToken: (refreshToken: string | null) => void;
}

export interface LoginResponse {
  token: string;
  refresh: string;
}

export interface User {
  cpf: string;
  name: string;
  state: string;
  city: string;
  zip: string;
  number: string;
  complement: string;
  phone: string;
  email: string;
  managerCpf: string | null;
}
