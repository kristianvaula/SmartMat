import axiosInstance from '~/service/AxiosInstance';
import MockAdapter from 'axios-mock-adapter';
import { useUserStore } from '~/store/userStore';
import {createPinia} from "pinia";

const pinia = createPinia();

test('axiosInstance has correct baseURL', () => {
    // @ts-ignore
    expect(axiosInstance.defaults.baseURL).toBe(process.env.NODE_ENV === 'production' ? "https://api.smartmat.online" : "http://localhost:8080");
});

test('axiosInstance has withCredentials set to true', () => {
    // @ts-ignore
    expect(axiosInstance.defaults.withCredentials).toBe(true);
});

test('axiosInstance has a timeout of 5000', () => {
    // @ts-ignore
    expect(axiosInstance.defaults.timeout).toBe(5000);
});

test('axiosInstance has correct headers', () => {
    // @ts-ignore
    expect(axiosInstance.defaults.headers.Accept).toBe('application/json');
    // @ts-ignore
    expect(axiosInstance.defaults.headers['Content-Type']).toBe('application/json');
});


