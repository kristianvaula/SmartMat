import type {Member} from "~/types/MemberType"; 

export interface Refrigerator {
    id : number,
    name : string,
    address : string, 
    members : Member[] | null
}