import { expect, test } from 'vitest';
import {mount, shallowMount} from '@vue/test-utils';
import { createPinia } from 'pinia';
import { createRouter, createWebHistory } from 'vue-router'
import register from '../../pages/register.vue'
import index from '../../pages/index.vue'
import ErrorAlert from '../AlertBox/ErrorAlert.vue';
import GreenButton from "../Button/GreenButton.vue";
import GrayButton from "../Button/GrayButton.vue";
import LoginForm from "./LogInForm.vue";



const pinia = createPinia()
const mockStore = {
    someGetter: 'mockValue'
}



const router = createRouter({
    history: createWebHistory(''),
    routes: [
        {
            path: '/register',
            component: register,
        },
        {
            path: '/',
            component: index,
        },
    ],
})

const localePath = (path: string) => router.resolve({ path }).href

const globalMocks = {
    $pinia: pinia,
    $store: mockStore,
    $t: () => {}, // Add this line to mock the $t function
    localePath
}


describe('LoginForm.vue', () => {
    const wrapper = mount(LoginForm, {
        global: {
            plugins: [pinia, router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            }
        },
    });

    test('renders LoginForm component', () => {
        const input = wrapper.find('.wrapper');
        expect(input.exists()).toBe(true);
    });

    test('renders email and password input fields', () => {
        const emailInput = wrapper.find('#inpEmail');
        const passwordInput = wrapper.find('#inpPassword');
        expect(emailInput.exists()).toBe(true);
        expect(passwordInput.exists()).toBe(true);
    });

    test('renders login and new user buttons', () => {
        const loginButton = wrapper.findComponent(GreenButton);
        const newUserButton = wrapper.findComponent(GrayButton);
        expect(loginButton.exists()).toBe(true);
        expect(newUserButton.exists()).toBe(true);
    });
});


