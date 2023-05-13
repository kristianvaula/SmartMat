import { defineStore } from 'pinia'
import { Category } from '~/types/CategoryType';
import type { GroceryEntity } from '~/types/GroceryEntityType'
import { Refrigerator } from '~/types/RefrigeratorType';
import {useUserStore} from "~/store/userStore";

export interface RefrigeratorState{
    refrigerators : Refrigerator[],
    selectedRefrigerator : Refrigerator | null,
    selectedGrocery : GroceryEntity | null
}

const sessionStorageMock = {
    getItem: () => null,
    setItem: () => null,
    removeItem: () => null,
    clear: () => null,
};

const isTestEnvironment = process.env.NODE_ENV === 'test';


export const useRefrigeratorStore = defineStore('refrigerator', {
    state: () : RefrigeratorState => ({
        refrigerators : [] as Refrigerator[],   
        selectedRefrigerator : null as Refrigerator | null,
        selectedGrocery: null as GroceryEntity | null, // Update the initial state here
    }),
    persist: {
        storage: isTestEnvironment ? sessionStorageMock : sessionStorage,
    },

    getters: {
        getSelectedGrocery : (state : RefrigeratorState) : GroceryEntity | null => {
            return state.selectedGrocery;
        },
        getSelectedRefrigerator : (state : RefrigeratorState) : Refrigerator | null => {
            const userStore = useUserStore();
            const selectedFridge = state.selectedRefrigerator;
            if (!selectedFridge && userStore.isLoggedIn) {
                // If the user is logged in but has not selected a fridge, and there is only one fridge available,
                // automatically select that fridge
                if (state.refrigerators.length === 1) {
                    state.selectedRefrigerator = state.refrigerators[0];
                }
            }
            return state.selectedRefrigerator;
        },
        getRefrigerators: (state : RefrigeratorState) : Refrigerator[] => {
            return state.refrigerators;
        },
        getRefrigeratorById: (state: RefrigeratorState) => (id: number): Refrigerator | undefined => {
            return state.refrigerators.find(refrigerator => refrigerator.id === id);
          },
    },
    actions : {
        setSelectedGrocery(search : GroceryEntity) : void {
            this.selectedGrocery = search;
        },
        setSelectedRefrigerator(refrigeratorSearch : Refrigerator | null) : boolean {
            if(refrigeratorSearch === null) {
                this.selectedRefrigerator = null;
                return true;
            }
            const index = this.refrigerators.findIndex(refrigerator => refrigerator.id === refrigeratorSearch.id);
            if(index !== -1){
                this.selectedRefrigerator = refrigeratorSearch;
                return true;
            }
            return false;
        },
        setRefrigerators(newFridges : Refrigerator[]){
            this.refrigerators = newFridges;
        },
        resetState() {
            this.refrigerators = [];
            this.selectedRefrigerator = null;
            this.selectedGrocery = null;
        },
        deleteRefrigerator(refrigerator: Refrigerator) {
            // @ts-ignore
            const index = this.refrigerators.findIndex(r => r.id === refrigerator);
            if (index !== -1) {
                this.refrigerators.splice(index, 1);
                console.log("after delete")
                console.log(this.refrigerators)
            }
        },
    }

})