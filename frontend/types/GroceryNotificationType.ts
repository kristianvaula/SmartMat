import { GroceryEntity } from "./GroceryEntityType";

export interface GroceryNotification {

    id:number,
    refrigeratorGrocery:GroceryEntity,
    daysLeft : number,
    
}