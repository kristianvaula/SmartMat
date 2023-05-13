import { postLogin, postLogOut, postRegister, getUserData } from "~/service/httputils/authentication/AuthenticationService";
import axiosInstance from "~/service/AxiosInstance";
import MockAdapter from 'axios-mock-adapter';
import { LoginData } from "~/types/LoginData";
import { RegisterData } from "~/types/RegisterData";
import { User } from "~/types/UserType";

const mockAxios = new MockAdapter(axiosInstance);

describe('UserService', () => {
    beforeEach(() => {
        mockAxios.reset();
    });

    describe('postLogin', () => {
        it('should call the /api/auth/login endpoint with the provided login data', async () => {
            const loginData: LoginData = {
                email: 'test@example.com',
                password: 'password'
            };
            mockAxios.onPost('/api/auth/login', loginData).reply(200, {});

            const response = await postLogin(loginData);

            expect(response.status).toBe(200);
            expect(mockAxios.history.post.length).toBe(1);
            expect(mockAxios.history.post[0].data).toEqual(JSON.stringify(loginData));
        });
    });

    describe('postLogOut', () => {
        it('should call the /api/auth/logout endpoint', async () => {
            mockAxios.onPost('/api/auth/logout').reply(200, {});

            const response = await postLogOut();

            expect(response.status).toBe(200);
            expect(mockAxios.history.post.length).toBe(1);
            expect(mockAxios.history.post[0].url).toBe('/api/auth/logout');
        });
    });

    describe('postRegister', () => {
        it('should call the /api/auth/register endpoint with the provided registration data', async () => {
            const registerData: RegisterData = {
                name: 'Test User',
                email: 'test@example.com',
                password: 'password'
            };
            mockAxios.onPost('/api/auth/register', registerData).reply(200, {});

            const response = await postRegister(registerData);

            expect(response.status).toBe(200);
            expect(mockAxios.history.post.length).toBe(1);
            expect(mockAxios.history.post[0].data).toEqual(JSON.stringify(registerData));
        });
    });

    describe('getUserData', () => {
        it('should call the /api/my-profile endpoint and return the user data', async () => {
            const userData: User = {
                id: 1,
                name: 'Test User',
                email: 'test@example.com',
                roles: ['USER'],
                favoriteRefrigeratorId: null
            };
            mockAxios.onGet('/api/my-profile').reply(200, userData);

            const response = await getUserData();

            expect(response).toEqual(userData);
            expect(mockAxios.history.get.length).toBe(1);
            expect(mockAxios.history.get[0].url).toBe('/api/my-profile');
        });

        it('should return null if an error occurs', async () => {
            mockAxios.onGet('/api/my-profile').reply(500);

            const response = await getUserData();

            expect(response).toBeNull();
            expect(mockAxios.history.get.length).toBe(1);
            expect(mockAxios.history.get[0].url).toBe('/api/my-profile');
        });
    });
});
