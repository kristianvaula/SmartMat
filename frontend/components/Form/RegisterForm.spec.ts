import { expect, test } from 'vitest';
import {mount, shallowMount} from '@vue/test-utils';
import LoginForm from './LoginForm.vue';
import { createPinia } from 'pinia';
import { createRouter, createWebHistory } from 'vue-router'
import register from '../../pages/register.vue'
import index from '../../pages/index.vue'
import ErrorAlert from '../AlertBox/ErrorAlert.vue';
import GreenButton from "../Button/GreenButton.vue";
import GrayButton from "../Button/GrayButton.vue";
import NuxtLink from "#app/components/nuxt-link";
import RegisterForm from "./RegisterForm.vue";
import { createI18n } from 'vue-i18n'
import BaseInput from "~/components/Form/BaseInput.vue";



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


describe('RegisterForm.vue', () => {
    const wrapper = mount(RegisterForm, {
        global: {
            plugins: [pinia, router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            }
        },
    });

    test('renders RegisterForm component', () => {
        const input = wrapper.find('.wrapper');
        expect(input.exists()).toBe(true);
    });

    test('renders name, email and password input fields', () => {
        const nameInput = wrapper.find('#inpName');
        const emailInput = wrapper.find('#inpEmail');
        const passwordInput = wrapper.find('#inpPassword');
        expect(nameInput.exists()).toBe(true);
        expect(emailInput.exists()).toBe(true);
        expect(passwordInput.exists()).toBe(true);
    });

    test('renders login and new user buttons', () => {
        const loginButton = wrapper.findComponent(GreenButton);
        const newUserButton = wrapper.findComponent(GrayButton);
        expect(loginButton.exists()).toBe(true);
        expect(newUserButton.exists()).toBe(true);
    });

    test('renders baseinput', () => {
        const input = wrapper.findComponent(BaseInput);
        expect(input.exists()).toBe(true);
    });

    test('renders id', () => {
        const nameInput = wrapper.find('#inpName');
        const emailInput = wrapper.find('#inpEmail');
        const passwordInput = wrapper.find('#inpPassword');
        expect(nameInput.attributes('id')).toBe('inpName');
        expect(emailInput.attributes('id')).toBe('inpEmail');
        expect(passwordInput.attributes('id')).toBe('inpPassword');
    });

    test('renders type', () => {
        const nameInput = wrapper.find('#inpName');
        const emailInput = wrapper.find('#inpEmail');
        const passwordInput = wrapper.find('#inpPassword');
        expect(nameInput.attributes('type')).toBe('name');
        expect(emailInput.attributes('type')).toBe('email');
        expect(passwordInput.attributes('type')).toBe('password');
    });
});


