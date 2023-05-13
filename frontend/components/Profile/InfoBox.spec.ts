import { expect, test } from 'vitest';
import {mount, shallowMount} from '@vue/test-utils';
import {createPinia, setActivePinia} from 'pinia';
import { createRouter, createWebHistory } from 'vue-router'
import register from '../../pages/register.vue'
import index from '../../pages/index.vue'
import MyProfile from "~/components/Profile/MyProfile.vue";
import InfoBox from '~/components/Profile/InfoBox.vue'



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
    $t: () => {},
    localePath,
    $pinia: createPinia(), // create new Pinia instance
};



describe('InfoBox.vue', () => {

    beforeEach(() => {
        setActivePinia(createPinia())
    })

    const wrapper = mount(InfoBox, {
        global: {
            plugins: [router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            },
        },
    });

    test('renders my profile component', () => {
        const input = wrapper.find('[data-test="box"]');
        expect(input.exists()).toBe(true);
    });

    it('renders the header', () => {
        expect(wrapper.find('[data-test="header"]').exists()).toBe(true);
    });


});


