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

export interface Carrier {
  cnpj?: string;
  name: string;
  phone: string;
  email: string;
}

export interface Client {
  id?: string;
  name: string;
  phone: string;
  email: string;
  cpf?: string;
  cnpj?: string;
}

export interface Address {
  id?: string;
  recipientName: string;
  state: string;
  city: string;
  zip: string;
  street: string;
  number: string;
  details: string;
  clientId: string;
}
