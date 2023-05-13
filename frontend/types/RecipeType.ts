import { Ingredient } from "./IngredientType"
export interface Recipe {
    id : number,
    name : string,
    url : string,
    ingredients : Ingredient[]
}