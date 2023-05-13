import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import ShoppingListCategory from './ShoppingListCategory.vue';

    const categoryListItems = [
        { id: 1, description: 'Apples', quantity: 3, unit: { id: 1, name:'liter', weight: 1000 }, subCategoryName: 'Fruits', isAddedToCart: false, isSuggested: false, isFromRefrigerator: false},
        { id: 2, description: 'Bananas', quantity: 2, unit: { id: 1, name:'liter', weight: 1000 }, subCategoryName: 'Fruits', isAddedToCart: false, isSuggested: false, isFromRefrigerator: false},
        { id: 3, description: 'Oranges', quantity: 1, unit: { id: 1, name:'liter', weight: 1000 }, subCategoryName: 'Fruits', isAddedToCart: false, isSuggested: false, isFromRefrigerator: false},
    ]

    describe('ShoppingListCategory.vue', () => {
        const wrapper = mount(ShoppingListCategory, {
            props: {
                CategoryDetails: {
                    id: 1,
                    name: "Milk"
                },
                ShoppingListId: 1
            },
            data() {
                return {
                    categoryListItems,
                    isCategoryExpanded: false,
                }
            }
        });

    test('renders category name', () => {
        const categoryName = wrapper.find('[data-test="categoryName"]');
        expect(categoryName.exists()).toBe(true);
        expect(categoryName.text()).toEqual('Milk');
    });

    test('renders numberOfCategoryItems', () => {
        const numberOfCategoryItems = wrapper.find('[data-test="numberOfCategoryItems"]');
        expect(numberOfCategoryItems.exists()).toBe(true);
        expect(numberOfCategoryItems.text()).toBe('[3]');
    });

    test('renders expand button and expand more img', () => {
        const expandBtn = wrapper.find('[data-test="expandBtn"]');
        const expandMoreImg = wrapper.find('[data-test="expandMore"]');
        
        expect(expandBtn.exists()).toBe(true);
        expect(expandMoreImg.exists()).toBe(true);
    });

    test('expands the category when clicked', async () => {
        const expandBtn = wrapper.find('[data-test="expandBtn"]');
        
        await expandBtn.trigger('click');

        expect(expandBtn.exists()).toBe(true);
        expect(wrapper.vm.isCategoryExpanded).toBe(true)
    });

    
    test('renders ShoppingListelement when isCategoryExpanded is true', () => {
        wrapper.setData({ isCategoryExpanded: true });
        const shoppingListElementWrapper = wrapper.find('[data-test="shoppingListElementWrapper"]');
        const shoppingListElement = shoppingListElementWrapper.find('[data-test="shoppingListElement"]');

        expect(shoppingListElement.exists()).toBe(true);
    });

    test('renders expand button and expand more img', () => {
        wrapper.setData({ isCategoryExpanded: false });
        const expandBtn = wrapper.find('[data-test="expandBtn"]');
        const expandMoreImg = wrapper.find('[data-test="expandLess"]');
        
        expect(expandBtn.exists()).toBe(true);
        expect(expandMoreImg.exists()).toBe(true);
    });
});
