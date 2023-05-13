import type { AxiosResponse } from 'axios';
import axiosInstance from "~/service/AxiosInstance";
import type {SaveGrocery} from "~/types/SaveGrocery";


const createShoppingList = (refrigeratorId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/create/${refrigeratorId}`);
};

const getGroceriesFromShoppingList = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/groceries/${shoppingListId}`);
};

const getGroceriesFromCategorizedShoppingList = (shoppingListId: Number, categoryId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/category/groceries/${shoppingListId}/${categoryId}`);
};

const getCategoriesFromShoppingList = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/categories/${shoppingListId}`);
};

const saveGroceryToShoppingList = (grocery: SaveGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/add-grocery`, grocery);
};

const editGroceryQuantity = (editGrocery: SaveGrocery): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-grocery`, editGrocery);
};

const removeGroceryFromShoppingList = (groceryListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/delete-grocery/${groceryListId}`);
};

const removeRefrigeratorGroceryFromShoppingList = (refrigeratorShoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.delete(`/api/shopping-list/delete-refrigerator-grocery/${refrigeratorShoppingListId}`);
};

const getRequestedGroceries = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/requested/groceries/${shoppingListId}`);
};

const getRequestedGroceriesInCategories = (shoppingListId: Number, categoryId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/requested/groceries/${shoppingListId}/${categoryId}`);
};

const getSuggestedGroceriesFromRefrigerator = (shoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/shopping-list/suggested-refrigerator/groceries/${shoppingListId}`);
};

const transferGroceryToShoppingCart = (groceryShoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/transfer-shopping-cart/${groceryShoppingListId}`);
};

const transferRefrigeratorGroceryToShoppingCart = (groceryShoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/refrigerator/transfer-shopping-cart/${groceryShoppingListId}`);
};

const transferGroceryToShoppingList = (refrigeratorShoppingListId: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/transfer-shopping-list/${refrigeratorShoppingListId}`);
};

const updateGrocery = (groceryId: Number, quantity: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-grocery/${groceryId}/${quantity}`);
};

const updateRefrigeratorGrocery = (groceryRefrigeratorShoppingListId: Number, quantity: Number): Promise<AxiosResponse> => {
    return axiosInstance.post(`/api/shopping-list/edit-refrigerator-grocery/${groceryRefrigeratorShoppingListId}/${quantity}`);
};

export default {
    createShoppingList,
    getGroceriesFromShoppingList,
    getGroceriesFromCategorizedShoppingList,
    getCategoriesFromShoppingList,
    saveGroceryToShoppingList,
    editGroceryQuantity,
    removeGroceryFromShoppingList,
    removeRefrigeratorGroceryFromShoppingList,
    getRequestedGroceries,
    getRequestedGroceriesInCategories,
    getSuggestedGroceriesFromRefrigerator,
    transferGroceryToShoppingCart,
    transferRefrigeratorGroceryToShoppingCart,
    transferGroceryToShoppingList,
    updateGrocery,
    updateRefrigeratorGrocery
}
