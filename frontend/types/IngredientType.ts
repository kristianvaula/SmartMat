import type { Unit } from "./UnitType";

export interface Ingredient {
    id: number,
    name: string,
    quantity: number,
    unit : Unit
}