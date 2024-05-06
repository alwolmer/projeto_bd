import { User } from "@/types/auth";
import { queryOptions } from "@tanstack/react-query";
import { AxiosInstance } from "axios";

export function userFetch(api: AxiosInstance) {
  return queryOptions({
    queryKey: ["currentUser", api],
    queryFn: () => {
      const response: Promise<User> = api
        .get("/auth/me")
        .then((response) => response.data)
        .catch((error) => {
          throw new Error(error.message);
        });

      return response;
    },
  });
}
