import axiosInstance from "~/service/AxiosInstance";
import {AxiosResponse} from "axios";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import type {Refrigerator} from "~/types/RefrigeratorType"; 
import type { MemberRequest } from "~/types/MemberRequest";
import { RemoveMemberRequest } from "~/types/RemoveMemberRequest";

export const getRefrigerators = async () : Promise<AxiosResponse> => {
    return axiosInstance.get('/api/refrigerator/user');
}

export const getRefrigeratorById = async(refrigeratorId : Number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/${refrigeratorId}`); 
}

export const getMatchingIngredientsInRefrigerator = async(refrigeratorId : Number, recipeId:  Number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/matching-recipe/${refrigeratorId}/${recipeId}`); 
}

export const postRegisterFridge = async (refrigeratorRegisterData : RefrigeratorRegisterData): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/new', refrigeratorRegisterData);
}

export const postAddMember = async (memberRequest : MemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/new', memberRequest);
}

export const postEditFridge = async (refrigerator : Refrigerator): Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/edit', refrigerator);
}

export const postEditMember = async (memberRequest : MemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-role', memberRequest); 
}

export const postRemoveMember = async (removeMemberRequest : RemoveMemberRequest) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/remove', removeMemberRequest);
}

export const postEditMembers = async (memberRequests : MemberRequest[]) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-roles', memberRequests);
}

export const postEditFavorite = async (favoriteRefrigeratorId : Number) : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-favorite', favoriteRefrigeratorId);
}

export const postRemoveFavorite = async () : Promise<AxiosResponse> => {
    return axiosInstance.post('/api/refrigerator/members/edit-favorite', -1);
}