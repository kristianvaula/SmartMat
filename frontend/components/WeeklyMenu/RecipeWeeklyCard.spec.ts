import { mount } from '@vue/test-utils';
import RecipeWeeklyCard from '~/components/WeeklyMenu/RecipeWeeklyCard.vue';
import { Recipe } from '~/types/RecipeType';
import LoginForm from "~/components/Form/LogInForm.vue";
import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";
import {expect} from "vitest";

const globalMocks = {
    $t: () => {}, // Add this line to mock the $t function
}
describe('YourComponentName.vue', () => {
    const mockRecipe: Recipe = {
        id: 1,
        name: 'Test Recipe',
        url: 'https://example.com/test-recipe.jpg',
        ingredients: [
            {name: 'Ingredient 1', quantity: '1 cup'},
            {name: 'Ingredient 2', quantity: '2 cups'},
        ],
    };

    const wrapper = mount(RecipeWeeklyCard, {
        global: {
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            }
        },
        props: {recepeInfo: mockRecipe, lockedBoolean: false},
    });

    it('renders recipe name', () => {
        expect(wrapper.find('.recepe-title').text()).toBe(mockRecipe.name);
    });

    it('renders locked icon when lockedBoolean is true', () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: true},
        });

        expect(wrapper.find('.lock-icon').attributes('src')).toContain('Locked.webp');
    });

    it('renders unlocked icon when lockedBoolean is false', () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: false},
        });

        expect(wrapper.find('.lock-icon').attributes('src')).toContain('unlock.png');
    });

    it('emits seeRecipeEvent when "See Recipe" button is clicked', async () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: false},
        });

        await wrapper.find('button:nth-child(1)').trigger('click');

        expect(wrapper.emitted().seeRecipeEvent).toBeTruthy();
    });

    it('emits removeEvent when "Remove" button is clicked', async () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: false},
        });

        await wrapper.find('button:nth-child(2)').trigger('click');

        expect(wrapper.emitted().removeEvent).toBeTruthy();
    });

    it('emits lockedEvent when "locked" icon is clicked', () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: false},
        });

        wrapper.find('.lock-icon').trigger('click');

        expect(wrapper.emitted().lockedEvent).toBeTruthy();
    });

    it('emits unlockedEvent when "unlocked" icon is clicked', () => {
        const wrapper = mount(RecipeWeeklyCard, {
            global: {
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                },
            },
            props: {recepeInfo: mockRecipe, lockedBoolean: true},
        });

        wrapper.find('.lock-icon').trigger('click');

        expect(wrapper.emitted().unlockedEvent).toBeTruthy();
    });


});
