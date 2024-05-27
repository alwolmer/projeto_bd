export interface Category {
  id: string;
  name: string;
}

export interface Product {
  id?: string;
  name: string;
  categories: string[];
}

export interface Supplier {
  cnpj?: string;
  name: string;
  phone: string;
  email: string;
}
export interface Item {
  id?: string;
  productId: string;
  supplierCnpj: string;
  employeeCpf?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface Discard {
  itemId: string;
  employeeCpf?: string;
  discardReason: string;
  createdAt?: Date;
  updatedAt?: Date;
}
