import { SubCategory } from "./SubCategoryType";

export interface Grocery{
    id: number,
    name: string,
    description: string,
    groceryExpiryDate: number,
    subCategory: SubCategory,
}