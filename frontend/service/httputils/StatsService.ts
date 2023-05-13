import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";

const getStatsData = (refrigeratorId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/history/grocery/lastYear/${refrigeratorId}`);
};

export default {
    getStatsData,
}