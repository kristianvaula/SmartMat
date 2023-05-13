import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import { Unit } from '~/types/UnitType';
import type { SaveGrocery } from '~/types/SaveGrocery';


const createShoppingCart = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-cart/create/${shoppingListId}`);
};

const getGroceriesFromShoppingCart = (shoppingCartId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-cart/groceries/${shoppingCartId}`);
};

const transferToRefrigerator = (groceryShoppingCartId: Number, unit : Unit, quantity : Number): Promise<AxiosResponse> => {
    const dto = {
        groceryDTO : {},
        unitDTO : unit,
        quantity : quantity,
    };
    return axiosInstance.post(`api/shopping-cart/transfer-refrigerator/${groceryShoppingCartId}`, dto)
};

const transferAllToRefrigerator = (groceries : SaveGrocery[]): Promise<AxiosResponse> => {
    return axiosInstance.post('api/shopping-cart/all/transfer-refrigerator', groceries);
};

const transferGroceryToShoppingList = (groceryIds: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`api/shopping-cart/transfer-shoppingList/${groceryIds}`)
};

export default {
    createShoppingCart,
    getGroceriesFromShoppingCart,
    transferToRefrigerator,
    transferAllToRefrigerator,
    transferGroceryToShoppingList
}