import { Unit } from "./UnitType";

export interface ResponseGrocery {
    id: number;
    description: string;
    quantity: number;
    unitDTO : Unit;
    subCategoryName: string;
  }
  