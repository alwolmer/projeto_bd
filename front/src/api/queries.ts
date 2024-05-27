import { Employee } from "@/types/auth";
import { Category, Product, Supplier } from "@/types/storage";
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
