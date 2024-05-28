import { Employee } from "@/types/auth";
import {
  Address,
  Carrier,
  Category,
  Client,
  ClientStats,
  Discard,
  Item,
  Order,
  Product,
  StockStats,
  Supplier,
} from "@/types/storage";
import { queryOptions } from "@tanstack/react-query";
import { AxiosInstance } from "axios";

export function userFetch(api: AxiosInstance) {
  return queryOptions({
    queryKey: ["currentUser"],
    queryFn: () => {
      const response: Promise<Employee> = api
        .get("/auth/me")
        .then((response) => response.data)
        .catch((error) => {
          throw new Error(error.message);
        });

      return response;
    },
  });
}

export function categoriesFetch(api: AxiosInstance) {
  const response: Promise<Category[]> = api
    .get("/category")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function productsFetch(api: AxiosInstance) {
  const response: Promise<Product[]> = api
    .get("/product")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });
  return response;
}

export function suppliersFetch(api: AxiosInstance) {
  const response: Promise<Supplier[]> = api
    .get("/supplier")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });
  return response;
}

export function employeesFetch(api: AxiosInstance) {
  const response: Promise<Employee[]> = api
    .get("/employee")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function itemsFetch(api: AxiosInstance) {
  const response: Promise<Item[]> = api
    .get("/item")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function discardsFetch(api: AxiosInstance) {
  const response: Promise<Discard[]> = api
    .get("/discard")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function carriersFetch(api: AxiosInstance) {
  const response: Promise<Carrier[]> = api
    .get("/carrier")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function clientsFetch(api: AxiosInstance) {
  const response: Promise<Client[]> = api
    .get("/client")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function addressFetch(api: AxiosInstance) {
  const response: Promise<Address[]> = api
    .get("/delivery-address")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function ordersFetch(api: AxiosInstance) {
  const response: Promise<Order[]> = api
    .get("/order")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function stockFetch(api: AxiosInstance) {
  const response: Promise<Item[]> = api
    .get("/item/stock")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function clientStatsFetch(api: AxiosInstance) {
  const response: Promise<ClientStats[]> = api
    .get("/client/stats")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}

export function stockStatsFetch(api: AxiosInstance) {
  const response: Promise<StockStats[]> = api
    .get("/item/stock/stats")
    .then((response) => response.data)
    .catch((error) => {
      throw new Error(error.message);
    });

  return response;
}
