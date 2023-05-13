import axiosInstance from "../AxiosInstance";
import type { AxiosResponse } from "axios";
import axios from "axios";
import { EntityName } from "typescript";
import { GroceryEntity } from "~/types/GroceryEntityType";
import type { Grocery } from "~/types/GroceryType";
import { Unit } from "~/types/UnitType";

export interface CreateGroceryDTO {
            groceryDTO : Grocery,
            unitDTO : Unit,
            quantity : Number,
}

export const getGroceriesByFridge = async (refrigeratorId : number) : Promise<AxiosResponse> => {
    return axiosInstance.get(`/api/refrigerator/grocery/${refrigeratorId}`);
}

export const createGrocery = async (refrigeratorId : number, grocery : Grocery, unit : Unit, quantity : Number) : Promise<AxiosResponse> => {
    const dto : CreateGroceryDTO = {
        groceryDTO : grocery,
        unitDTO : unit,
        quantity : quantity,
    };
    return axiosInstance.post(`/api/refrigerator/grocery/new/${refrigeratorId}`, dto
    );
}

export const getGroceriesDTOs = async () : Promise<AxiosResponse> => {
    return axiosInstance.get("/api/refrigerator/grocery/allDTOs");
}

export const deleteGrocery = async (grocery : GroceryEntity) : Promise<AxiosResponse> => {
    const refrigeratorGroceryId = grocery.id;
    return axiosInstance.delete("/api/refrigerator/grocery/remove");
} 

export const eatGrocery = async (grocery : GroceryEntity, unit :  Unit, quantity : number) : Promise<AxiosResponse> =>{
    console.log(grocery);
    const dto = {
        refrigeratorGroceryDTO : grocery,
        unitDTO : unit,
        quantity : quantity,
    }
    return axiosInstance.post("/api/refrigerator/grocery/eat", dto);
}
export const trashGrocery = async (grocery : GroceryEntity, unit :  Unit, quantity : number) : Promise<AxiosResponse> =>{
    const dto = {
        refrigeratorGroceryDTO : grocery,
        unitDTO : unit,
        quantity : quantity,
    }
    return axiosInstance.post("/api/refrigerator/grocery/trash", dto);
}
export const removeGrocery = async (grocery : GroceryEntity, unit :  Unit, quantity : number) : Promise<AxiosResponse> =>{
    const dto = {
        refrigeratorGroceryDTO : grocery,
        unitDTO : unit,
        quantity : quantity,
    }
    return axiosInstance.post("/api/refrigerator/grocery/remove", dto);
}
export const updateGrocery = async (grocery : GroceryEntity) : Promise<AxiosResponse> => {
    return axiosInstance.post("api/refrigerator/grocery/updateGrocery", grocery);
}
