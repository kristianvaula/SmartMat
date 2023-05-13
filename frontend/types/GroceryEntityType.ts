
import type {Grocery} from "./GroceryType";
import { Refrigerator } from "./RefrigeratorType";
import { Unit } from "./UnitType";
export interface GroceryEntity {
    id: number,
    physicalExpireDate: Date,
    refrigerator : Refrigerator,
    grocery: Grocery,
    unit : Unit,
    quantity : number
}