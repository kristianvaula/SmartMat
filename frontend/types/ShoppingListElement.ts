import { Unit } from "./UnitType";

export interface ShoppingListElementType {
    id: number,
    description: string,
    quantity: number,
    unitDTO : Unit,
    subCategoryName: string,
    isAddedToCart: boolean,
    isSuggested: boolean,
    isFromRefrigerator: boolean,
  }
  