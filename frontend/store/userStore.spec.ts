// stores/counter.spec.ts
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '~/store/userStore'
import MockAdapter from 'axios-mock-adapter';
import axiosInstance from "~/service/AxiosInstance";

// set up a mock axios instance to simulate network requests
const mockAxios = new MockAdapter(axiosInstance);
describe('user store', () => {
    beforeEach(() => {
        // creates a fresh pinia and make it active so it's automatically picked
        // up by any useStore() call without having to pass it to it:
        // `useStore(pinia)`
        setActivePinia(createPinia())
    })

    it('sets and gets the logged in user id', () => {
        const userStore = useUserStore()
        expect(userStore.getLoggedInUserId).toBe('')
        userStore.setLoggedInUserId('test-user')
        expect(userStore.getLoggedInUserId).toBe('test-user')
    })

    it('sets and gets the logged in user role', () => {
        const userStore = useUserStore()
        expect(userStore.getLoggedInUserRole).toBe('')
        userStore.setLoggedInUserRole('admin')
        expect(userStore.getLoggedInUserRole).toBe('admin')
    })

    it('sets and gets the authenticated status of the user', () => {
        const userStore = useUserStore()
        expect(userStore.isLoggedIn).toBe(false)
        userStore.setLoggedInUserStatus(true)
        expect(userStore.isLoggedIn).toBe(true)
    })

    it('sets and gets the favorite refrigerator id of the user', () => {
        const userStore = useUserStore()
        expect(userStore.getFavoriteRefrigeratorId).toBe(null)
        userStore.setFavoritedRefrigeratorId(123)
        expect(userStore.getFavoriteRefrigeratorId).toBe(123)
    })

    it('sets user status to authenticated when logIn is called', () => {
        const userStore = useUserStore()
        // @ts-ignore
        expect(userStore.isLoggedIn).toBe(false)
        userStore.logIn({ userId: 'test-user', userRole: 'admin', favoriteRefrigeratorId: 123 })
        // @ts-ignore
        expect(userStore.isLoggedIn).toBe(true)
    })

    it('sets user status to not authenticated when logOut is called', () => {
        const userStore = useUserStore()
        userStore.logIn({ userId: 'test-user', userRole: 'admin', favoriteRefrigeratorId: 123 })
        expect(userStore.isLoggedIn).toBe(true)
        userStore.logOut()
        expect(userStore.isLoggedIn).toBe(false)
    })


    test('fetches user status and sets authenticated status to false if status is not "AUTHENTICATED"', async () => {
        const userStore = useUserStore()

        // Mock the response of the /api/user-status endpoint
        mockAxios.onGet('/api/user-status').reply(200, {
            state: 'NOT_AUTHENTICATED',
        })

        await userStore.checkAuthStatus()

        expect(userStore.isLoggedIn).toBe(false)
    })

    test('fetches user status and sets authenticated status to false if response status is 401', async () => {
        const userStore = useUserStore()

        // Mock the response of the /api/user-status endpoint
        mockAxios.onGet('/api/user-status').reply(401)

        // Mock the window.alert() function
        window.alert = () => {}

        await userStore.checkAuthStatus()

        // Check that the user was logged out
        expect(userStore.isLoggedIn).toBe(false)
    })

    test('fetches user status and sets authenticated status to false if an error occurs', async () => {
        const userStore = useUserStore()

        // Mock the response of the /api/user-status endpoint to throw an error
        mockAxios.onGet('/api/user-status').networkError()

        // Mock the window.alert() function
        window.alert = () => {}

        await userStore.checkAuthStatus()

        // Check that the user was logged out
        expect(userStore.isLoggedIn).toBe(false)
    })
})