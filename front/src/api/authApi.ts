import { LoginResponse } from "@/types/auth";
import axios, { AxiosError } from "axios";

export const signIn = async (cpf: string, password: string) => {
  const tokens: LoginResponse = await axios
    .post("http://localhost:8080/api/v1/auth/login", {
      cpf,
      password,
    })
    .then((response) => response.data)
    .catch((err: AxiosError) => {
      if (err.response?.status === 401 || err.response?.status === 400) {
        throw new Error("Invalid credentials");
      }
      throw new Error(err.message);
    });

  return tokens;
};
