import { expect, test } from 'vitest';
import {mount, shallowMount} from '@vue/test-utils';
import MockAdapter from 'axios-mock-adapter';
import { createPinia } from 'pinia';
import { createRouter, createWebHistory } from 'vue-router'
import register from '../../pages/register.vue'
import index from '../../pages/index.vue'
import Dropdown from "./Dropdown.vue";
import axiosInstance from "~/service/AxiosInstance";


const mockAxios = new MockAdapter(axiosInstance);

const groceries = [
    {
        id: 1,
        name: "test",
        description: "string",
        groceryExpiryDate: 1,
        subCategory: {
            id : 1,
            name :"test",
            category : {
                id : 1,
                name : "test",
            }
        },
    },
]

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
    localePath,
    mockAxios
}

describe("Dropdown.vue", () => {
    const wrapper = mount(Dropdown, {
        global: {
            plugins: [router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            },
        },
    });
    beforeEach(() => {
        mockAxios.reset();
    });
    it("Test that dropdown has no list elements if get request returns empty", () => {
        const items = wrapper.findAll('li');
        expect(items.length).toBe(0);
    });
    it("displays the search input", () => {
        const searchInput = wrapper.find('input[type="search"]');
        expect(searchInput.exists()).toBe(true);
    })  
});
