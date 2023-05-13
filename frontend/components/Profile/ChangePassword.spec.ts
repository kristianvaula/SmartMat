import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import ChangePassword from './ChangePassword.vue';
import {createPinia} from "pinia";
import {createRouter, createWebHistory} from "vue-router";
import register from "~/pages/register.vue";
import index from "~/pages/index.vue";
import BaseInput from "~/components/Form/BaseInput.vue";
import GrayButton from "~/components/Button/GrayButton.vue";


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
        }
    ],
})

const localePath = (path: string) => router.resolve({ path }).href

const globalMocks = {
    $pinia: pinia,
    $store: mockStore,
    $t: () => {}, // Add this line to mock the $t function
    localePath
}


describe('ChangePassword', () => {
    const wrapper = mount(ChangePassword, {
        global: {
            plugins: [pinia, router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            }
        },
    });

    test('renders ChangePassword component', () => {
        const input = wrapper.find('.wrapper');
        expect(input.exists()).toBe(true);
    });

    test('renders form elements', () => {
        const baseInput = wrapper.findComponent(BaseInput);
        expect(baseInput.exists()).toBe(true);
    });

    test('renders password input', () => {
        const newPasswordInput = wrapper.find('#newPassword');
        const verifyPasswordInput = wrapper.find('#verifyNewPassword');
        const oldPasswordInput = wrapper.find('#oldPassword');
        expect(newPasswordInput.exists()).toBe(true);
        expect(verifyPasswordInput.exists()).toBe(true);
        expect(oldPasswordInput.exists()).toBe(true);
    });

    test('renders button', () => {
        const button = wrapper.find('#submit');
        expect(button.exists()).toBe(true);
    });

    test('does not allow form submission when input fields are empty', async () => {
        const button = wrapper.find('#submit');
        expect(button.element.disabled).toBe(true);
        await wrapper.find('#oldPassword').setValue('test');
        expect(button.element.disabled).toBe(true);
        await wrapper.find('#newPassword').setValue('test');
        expect(button.element.disabled).toBe(true);
        await wrapper.find('#verifyNewPassword').setValue('test');
        expect(button.element.disabled).toBe(false);
    });

    test('shows warning message when passwords do not match', async () => {
        const button = wrapper.find('#submit');
        await wrapper.find('#oldPassword').setValue('test');
        await wrapper.find('#newPassword').setValue('newpass');
        await wrapper.find('#verifyNewPassword').setValue('newpass2');
        await button.trigger('click');
        const message = wrapper.find('.text-red-800');
        expect(message.exists()).toBe(true);
    });

    test('clears input fields and logs passwords when form is submitted', async () => {
        const button = wrapper.find('#submit');
        await wrapper.find('#oldPassword').setValue('oldpass');
        await wrapper.find('#newPassword').setValue('newpass');
        await wrapper.find('#verifyNewPassword').setValue('newpass');
        await button.trigger('click');
        expect(wrapper.vm.oldPassword).toEqual('');
        expect(wrapper.vm.newPassword).toEqual('');
        expect(wrapper.vm.verifyNewPassword).toEqual('');
    });

});