import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import UnknownRecipe from './UnknownRecipe.vue';
import {createRouter, createWebHistory} from "vue-router";
import register from "~/pages/register.vue";
import index from "~/pages/index.vue";

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
    $store: mockStore,
    $t: () => {}, // Add this line to mock the $t function
    localePath
}

describe('UnknownRecipe.vue', () => {
    const wrapper = mount(UnknownRecipe, {
        global: {
            mocks: globalMocks,
        },
    });

    test('renders UnknownRecipe component', () => {
        const input = wrapper.find('.unknown-recipe');
        expect(input.exists()).toBe(true);
    });

} );