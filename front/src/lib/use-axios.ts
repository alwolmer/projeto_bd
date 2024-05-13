import axios from "axios";
import { jwtDecode } from "jwt-decode";
import dayjs from "dayjs";

import { useAuthStore } from "@/store/auth-store";
import { redirect } from "@tanstack/react-router";
import { toast } from "sonner";

const baseURL = "http://localhost:8080/";

export const useAxios = () => {
  const { token, refreshToken, setToken, setRefreshToken } = useAuthStore();

  const axiosInstance = axios.create({
    baseURL,
    headers: {
      "Content-Type": "application/json",
      Authorization: token !== null ? `Bearer ${token}` : "",
    },
  });

  axiosInstance.interceptors.request.use(async (req) => {
    const decodedToken = jwtDecode(token!);
    const isExpired = dayjs.unix(decodedToken.exp!).isBefore(dayjs());

    if (!isExpired) return req;

    const response = await axios
      .post(`${baseURL}/auth/refresh`, {
        refreshToken: refreshToken,
      })
      .then((res) => res.data)
      .catch(() => {
        toast.error("Error refreshing token");
        setRefreshToken(null);
        setToken(null);
        throw redirect({
          to: "/login",
        });
      });

    setRefreshToken(response.refresh);
    setToken(response.access);

    req.headers.Authorization = `Bearer ${response.access}`;
    return req;
  });

  return axiosInstance;
};
