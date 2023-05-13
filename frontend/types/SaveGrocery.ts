import { Unit } from "./UnitType";

export interface SaveGrocery {
    groceryId: Number;
    unitDTO : Unit,
    quantity : Number;
    foreignKey: Number; //can be used for both shoppingListId and shoppingCartId
}