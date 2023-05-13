import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";

export const getNotifications = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/notifications/all');
}

export const deleteNotifications = async (notificationId : number) : Promise<AxiosResponse> => {
    return axiosInstance.post('api/notifications/delete', notificationId);
} 