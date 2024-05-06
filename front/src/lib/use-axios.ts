import axios from "axios";
import { jwtDecode } from "jwt-decode";
import dayjs from "dayjs";

import { useAuthStore } from "@/store/auth-store";

const baseURL = "http://localhost:8080/api/v1";

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

    const response = await axios.post(`${baseURL}/auth/refresh`, {
      refresh: refreshToken,
    });

    setRefreshToken(response.data.refresh);
    setToken(response.data.access);

    req.headers.Authorization = `Bearer ${response.data.access}`;
    return req;
  });

  return axiosInstance;
};
