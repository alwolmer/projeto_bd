import { Employee } from "@/types/auth";
import { Category } from "@/types/storage";
import { queryOptions } from "@tanstack/react-query";
import { AxiosInstance } from "axios";

export function userFetch(api: AxiosInstance) {
  return queryOptions({
    queryKey: ["currentUser", api],
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
  return queryOptions({
    queryKey: ["categories", api],
    queryFn: () => {
      const response: Promise<Category[]> = api
        .get("/category")
        .then((response) => response.data)
        .catch((error) => {
          throw new Error(error.message);
        });

      return response;
    },
  });
}
