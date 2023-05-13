import axios from "axios";
import type { AxiosInstance } from "axios";
import { useUserStore } from "~/store/userStore";
import {Session} from "inspector";
import {useRouter} from "vue-router";

//check if we're in development mode or in production mode
//if we're in development mode, use localhost:8080 as baseURL
//if we're in production mode, use the domain name as baseURL
//const baseURL = "http://localhost:8080";

const baseURL = process.env.NODE_ENV === 'production' ? "https://api.smartmat.online" : "http://localhost:8080";

const axiosInstance: AxiosInstance = axios.create({
    baseURL,
    withCredentials:true,
    timeout: 5000,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    }
});
axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (!error.response) {
            console.error("An error occurred, but no response was provided:", error);
            return Promise.reject(error);
        }

        if (error.response.headers['error-message']) {
            alert("Old password is incorrect. Please try again.");
            return;
        }
        else if (error.response.status === 401 && useUserStore().isLoggedIn) {
            const router = useRouter();
            console.log("Session has expired. You've been logged out.");
            useUserStore().logOut();
            router.push({name: "login"});
        }
        else if(error.response.status !== 401) {
            console.log("An error has occurred. Errorcode: " + error.response.status);
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;