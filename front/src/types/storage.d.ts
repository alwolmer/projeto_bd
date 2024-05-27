export interface Category {
  id: string;
  name: string;
}

export interface Product {
  id?: string;
  name: string;
  weight: number;
  volume: number;
  categories: string[];
}

export interface Supplier {
  cnpj?: string;
  name: string;
  phone: string;
  email: string;
}
