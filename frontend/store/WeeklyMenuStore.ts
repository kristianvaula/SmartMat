import { defineStore } from 'pinia';
import { Recipe } from '~/types/RecipeType';

export interface WeeklyMenuState {
  currentWeek: Recipe[] | null[];
  nextWeek: Recipe[] | null[];
  currentWeekLocks: boolean[];
  nextWeekLocks: boolean[];
  chosenWeek: number;
  currentChosenIndex: number | null
}

const sessionStorageMock = {
  getItem: () => null,
  setItem: () => null,
  removeItem: () => null,
  clear: () => null,
};

const isTestEnvironment = process.env.NODE_ENV === 'test';

export const useWeeklyMenuStore = defineStore({
  id: 'weekly-menu-store',
  state: (): WeeklyMenuState => ({
    currentWeek: (new Array(7)).fill(null) as Recipe[] | null[],
    nextWeek: (new Array(7)).fill(null) as Recipe[] | null[],
    currentWeekLocks: Array(7).fill(false) as boolean[],
    nextWeekLocks: Array(7).fill(false) as boolean[],
    chosenWeek: 1,
    currentChosenIndex: null
  }),
  persist: {
    storage: isTestEnvironment ? sessionStorageMock : sessionStorage,
  },
  actions: {
    resetState(){
      this.currentWeek = (new Array(7)).fill(null) as Recipe[] | null[],
      this.nextWeek = (new Array(7)).fill(null) as Recipe[] | null[],
      this.currentWeekLocks = Array(7).fill(false) as boolean[],
      this.nextWeekLocks = Array(7).fill(false) as boolean[],
      this.chosenWeek = 1,
      this.currentChosenIndex = null
    },
    setCurrentWeek(index: number, recipe: Recipe | null) {
      this.currentWeek[index] = recipe;
    },
    setNextWeek(index: number, recipe: Recipe | null) {
      this.nextWeek[index] = recipe;
    },
    setCurrentWeekLock(index: number, lock: boolean) {
      this.currentWeekLocks[index] = lock;
    },
    setNextWeekLock(index: number, lock: boolean) {
      this.nextWeekLocks[index] = lock;
    },
    setChosenWeek(week: number) {
      this.chosenWeek = week;
    },
    setCurrentChosenIndex(index: number | null) {
      this.currentChosenIndex = index;
    },
    setNextWeekRandomly(recipes: Recipe[]) {
        for (let i = 0; i < 7; i++) {
          const randomIndex = Math.floor(Math.random() * recipes.length);
          if (!this.nextWeekLocks[i]) {
            this.setNextWeek(i, recipes[randomIndex]);
          }
        }
      },
      
      setCurrentWeekRandomly(recipes: Recipe[]) {
        for (let i = 0; i < 7; i++) {
          if (!this.currentWeekLocks[i]) { 
            const randomIndex = Math.floor(Math.random() * recipes.length);
            this.setCurrentWeek(i, recipes[randomIndex]);
          }
        }
      },

      isCurrentWeekEmpty(): boolean {
        for(let i = 0; i < this.currentWeek.length; i++) {
            if(this.currentWeek[i] != null) {
                return false;
            }
        }
        return true;
      },

      isNextWeekEmpty(): boolean {
        for(let i = 0; i < this.nextWeek.length; i++) {
            if(this.nextWeek[i] != null) {
                return false;
            }
        }
        return true;
      },
      
  },
});

