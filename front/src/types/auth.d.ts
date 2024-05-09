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
  cpf: string;
  name: string;
  email: string;
  phone: string;
  state: string;
  city: string;
  zip: string;
  street: string;
  number: string;
  complement: string;
  managerCpf: string | null;
}
