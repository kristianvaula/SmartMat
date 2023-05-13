import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import ShoppingListElement from './ShoppingListElement.vue';
import {Unit} from "~/types/UnitType";

describe('ShoppingListElement.vue', () => {
    const wrapper = mount(ShoppingListElement, {
        props: {
            ElementDetails: {
                id: 1,
                description: "Test Description",
                quantity: 1,
                unitDTO : { id: 1, name:'liter', weight: 1000 },
                subCategoryName: "Test Category",
                isAddedToCart: false,
                isSuggested: false,
                isFromRefrigerator: false,
            }
        },
        data() {
            return {
                isElementAddedToCart:false,
                isElementSuggested:false,
                editElement:false,
                newQuantity:1,

            }
        },
    });

    test('renders ElementDetailsDescription', () => {
        const elementDetailsDescription = wrapper.find('[data-test="ElementDetailsDescription"]');
        expect(elementDetailsDescription.exists()).toBe(true);
        expect(elementDetailsDescription.text()).toBe('Test Description');
    });

    test('renders ElementDetailsQuantity', () => {
        const elementDetailsQuantity = wrapper.find('[data-test="ElementDetailsQuantity"]');
        expect(elementDetailsQuantity.exists()).toBe(true);
        expect(elementDetailsQuantity.text()).toBe('(1 liter)');
    });

    test('does not render AcceptSuggestionButton, because element is not suggested', () => {
        const acceptSuggestionButton = wrapper.find('[data-test="AcceptSuggestionButton"]');
        expect(acceptSuggestionButton.exists()).toBe(false);
    });

    test('does not render RemoveElementFromCart, because element is not in cart', () => {
        const removeElementFromCart = wrapper.find('[data-test="RemoveElementFromCart"]');
        expect(removeElementFromCart.exists()).toBe(false);
    });

    test('renders RemoveElementFromList, because element is in shopping list', () => {
        const removeElementFromList = wrapper.find('[data-test="RemoveElementFromList"]');
        expect(removeElementFromList.exists()).toBe(true);
    });

    test('does not render AddElementToRefrigerator, because element is not in cart', () => {
        const addElementToRefrigerator = wrapper.find('[data-test="AddElementToRefrigerator"]');
        expect(addElementToRefrigerator.exists()).toBe(false);
    });

    test('renders AddElementToShoppingCart, because element is in shopping list', () => {
        const addElementToShoppingCart = wrapper.find('[data-test="AddElementToShoppingCart"]');
        expect(addElementToShoppingCart.exists()).toBe(true);
    });

    test('renders the EditElementButton, and clicking the button changes the editElement value', () => {
        const editElement = wrapper.find('[data-test="EditElement"]');
        expect(editElement.exists()).toBe(true);
        expect(wrapper.vm.editElement).toBe(false)
        editElement.trigger('click');
        expect(wrapper.vm.editElement).toBe(true)
    });

    test('Does not render NewQuantityDecrement even if editElement is true, because current newQuantity is 1', () => {
        const newQuantityDecrement = wrapper.find('[data-test="EditElement"]');
        expect(wrapper.vm.editElement).toBe(true);
        expect(newQuantityDecrement.exists()).toBe(false);
    });

    test('Renders NewQuantityIncrement, because editElement is now true', () => {
        const newQuantityIncrement = wrapper.find('[data-test="NewQuantityIncrement"]');
        expect(wrapper.vm.editElement).toBe(true);
        expect(newQuantityIncrement.exists()).toBe(true);
    });

    test('Renders NewQuantity, and displays 1', () => {
        const newQuantity = wrapper.find('[data-test="NewQuantity"]');
        expect(newQuantity.exists()).toBe(true);
        expect(newQuantity.text()).toBe('1');
    });

    test('NewQuantityIncrement increments newQuantity when clicked', () => {
        const newQuantityIncrement = wrapper.find('[data-test="NewQuantityIncrement"]');
        expect(wrapper.vm.editElement).toBe(true);
        expect(wrapper.vm.newQuantity).toBe(1);
        newQuantityIncrement.trigger('click');
        expect(wrapper.vm.newQuantity).toBe(2);
    });

    test('Renders UpdateGroceryNewQuantity, because edit element is true', () => {
        const updateGroceryNewQuantity = wrapper.find('[data-test="UpdateGroceryNewQuantity"]');
        expect(wrapper.vm.editElement).toBe(true);
        expect(updateGroceryNewQuantity.exists()).toBe(true);
    });

    test('Renders UpdateGroceryClose, because edit element is true', () => {
        const updateGroceryClose = wrapper.find('[data-test="UpdateGroceryClose"]');
        expect(wrapper.vm.editElement).toBe(true);
        expect(updateGroceryClose.exists()).toBe(true);
    });




});
