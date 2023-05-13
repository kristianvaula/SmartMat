import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import {FetchRecipeDTO} from "~/types/FetchRecipeDTO";
import qs from 'qs';

export const fetchRecipes = async (fetchRecipeDTO: FetchRecipeDTO): Promise<AxiosResponse> => {
    return axiosInstance.get('/api/recipe/fetch', {
        params: {
            refrigeratorId: fetchRecipeDTO.refrigeratorId,
            numRecipes: fetchRecipeDTO.numRecipes,
            recipesFetched: fetchRecipeDTO.recipesFetched,
        },
        paramsSerializer: (params) => {
            return qs.stringify(params, { arrayFormat: 'repeat' });
        },
    });
}

export const fetchAllRecipes = async (): Promise<AxiosResponse> => {
    return axiosInstance.get('/api/recipe/all');
}