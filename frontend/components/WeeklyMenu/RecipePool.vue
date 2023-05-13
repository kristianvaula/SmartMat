<template>
    <div data-test="buttons" class="top-buttons">
        <ButtonGreenButton class="button px-4 mt-4 mx-12 px-1 py-3 border-2 border-black rounded-lg text-black dark:bg-zinc-600" @click="randomRecipesEvent" :label="$t('generate_random_recipes')"></ButtonGreenButton>
        <ButtonGreenButton class="button px-4 mt-4 mx-12 px-1 border-2 border-black rounded-lg text-black dark:bg-zinc-600" @click="removeAllRecipes" :label="$t('remove_all_recipes')"></ButtonGreenButton>
    </div>
    <h1 v-if="weeklyMenuStore.$state.chosenWeek === 1" class="title dark:text-white">{{ $t("current_week") }}</h1>
    <h1 v-else class="title dark:text-white">{{ $t("next_week") }}</h1>
    <div class="recipe-pool" v-if="weeklyMenuStore.$state.chosenWeek === 1">
        <div v-for="weekday in activeWeekdays" :key="weekday" class="recipe-card">
            <div class="recipe-content">
                <div class="weekday">
                    <p class=" text-black dark:text-white">{{ weekday }}</p>
                </div>
                <div v-if="weeklyMenuStore.$state.currentWeek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recipe-event="findNewRecipe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecipe(getDayIndex(weekday))"
                    @see-recipe-event="seeRecipeCurrentWeek(getDayIndex(weekday))"
                    :recepe-info="weeklyMenuStore.$state.currentWeek[getDayIndex(weekday)]" 
                    :locked-boolean="weeklyMenuStore.$state.currentWeekLocks[getDayIndex(weekday)]" />
                </div>
            </div>
        </div>
    </div>
    <div class="recipe-pool" v-else>
        <div v-for="weekday in Weekdays" :key="weekday" class="recipe-card">
            <div class="recipe-content">
                <div class="weekday text-black dark:text-white">
                  <p class=" text-black dark:text-white">{{ weekday }}</p>
                </div>
                <div v-if="weeklyMenuStore.$state.nextWeek[getDayIndex(weekday)] === null">
                    <UnknownRecipe @add-recipe-event="findNewRecipe(getDayIndex(weekday))"/>
                </div>
                <div v-else>
                    <WeeklyMenuRecipeWeeklyCard @unlocked-event="unlockRecipe(getDayIndex(weekday))" 
                    @locked-event="lockRecipe(getDayIndex(weekday))" 
                    @remove-event="removeRecipe(getDayIndex(weekday))"
                    @see-recipe-event="seeRecipeNextWeek(getDayIndex(weekday))"
                    :recepe-info="weeklyMenuStore.$state.nextWeek[getDayIndex(weekday)]" 
                    :locked-boolean="weeklyMenuStore.$state.nextWeekLocks[getDayIndex(weekday)]"/>
                </div>
            </div>
        </div>
    </div>

    <div class="navigation-buttons">
        <ButtonGreenButton width="150px" height="50px" class="button m-2 px-1 border-2 border-black rounded-lg bg-white dark:bg-zinc-600" @click="goToPreviousWeek" :disabled="weeklyMenuStore.$state.chosenWeek === 1" :label="$t('current_week')"></ButtonGreenButton>
        <ButtonGreenButton width="150px" height="50px" class="button m-2 px-1 border-2 border-black rounded-lg bg-white dark:bg-zinc-600" @click="goToNextWeek" :disabled="weeklyMenuStore.$state.chosenWeek === 2" :label="$t('next_week')"></ButtonGreenButton>
    </div>
    
</template>

<script lang="ts">
import { useWeeklyMenuStore } from '~/store/WeeklyMenuStore';
import { Recipe } from '~/types/RecipeType';
import UnknownRecipe from './UnknownRecipe.vue';
import { fetchRecipes } from '~/service/httputils/RecipeService';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import {FetchRecipeDTO} from "~/types/FetchRecipeDTO";
import { Ingredient } from "~/types/IngredientType"
import type { Unit } from '~/types/UnitType';

export default {
    data() {
        return {
            Weekdays: [this.$t("monday"), this.$t("tuesday"), this.$t("wednesday"), this.$t("thursday"), this.$t("friday"), this.$t("saturday"), this.$t("sunday")],
        };
    },

    setup() {
    const weeklyMenuStore = useWeeklyMenuStore();
    const refrigeratorStore = useRefrigeratorStore();
    const {locale, locales, t} = useI18n()

    const fetchRecipeDTO: FetchRecipeDTO = reactive({
        refrigeratorId: -1,
        numRecipes: -1,
        recipesFetched: []
        });

    const addRecipeWeek = (index: number, recipe : Recipe) => {
      // Logic to add a recipe to the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeek(index, recipe);
      } else {
        weeklyMenuStore.setNextWeek(index, recipe);
      }
    };

    const lockRecipe = (index: number) => {
      // Logic to lock a recipe in the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeekLock(index, true);
      } else {
        weeklyMenuStore.setNextWeekLock(index, true);
      }
    };

    const unlockRecipe = (index: number) => {
      // Logic to unlock a recipe in the current or next week
      if (weeklyMenuStore.chosenWeek === 1) {
        weeklyMenuStore.setCurrentWeekLock(index, false);
      } else {
        weeklyMenuStore.setNextWeekLock(index, false);
      }
    };

    const removeRecipe = (index: number) => {
      // Logic to remove a recipe from the current or next week
      let recipeId = -1;
      if (weeklyMenuStore.chosenWeek === 1) {
        recipeId = weeklyMenuStore.$state.currentWeek[index].id;
        weeklyMenuStore.setCurrentWeek(index, null);
        weeklyMenuStore.setCurrentWeekLock(index, false);
      } else {
        recipeId = weeklyMenuStore.$state.nextWeek[index].id;
        weeklyMenuStore.setNextWeek(index, null);
        weeklyMenuStore.setNextWeekLock(index, false);
      }
      //remove id from array
      fetchRecipeDTO.recipesFetched = fetchRecipeDTO.recipesFetched.filter((element: number) => element !== recipeId);
    };


    const goToPreviousWeek = () => {
      // Logic to go to the previous week
      weeklyMenuStore.setChosenWeek(1);
    };

    const goToNextWeek = () => {
      // Logic to go to the next week
      weeklyMenuStore.setChosenWeek(2);
    };


    return {
      weeklyMenuStore,
      addRecipeWeek,
      lockRecipe,
      unlockRecipe,
      removeRecipe,
      goToPreviousWeek,
      goToNextWeek,
      refrigeratorStore,
      fetchRecipeDTO,
      locale, 
      locales,
      t
    };
  },

    computed: {
        //gets the remaining days of the current week
        activeWeekdays(): void {
            const currentDate = new Date();
            const currentDay = currentDate.getDay();
            const activeDays = this.Weekdays.slice(currentDay - 1);
            return activeDays;
        },
    },
    methods: {
        goToPreviousWeek() {
            this.goToPreviousWeek();
        },
        goToNextWeek() {
            this.goToNextWeek();
        },

        seeRecipeCurrentWeek(dayIndex: number) {
            this.$emit("seeRecipeEvent", this.weeklyMenuStore.$state.currentWeek[dayIndex]);
        },

        seeRecipeNextWeek(dayIndex: number) {
            this.$emit("seeRecipeEvent", this.weeklyMenuStore.$state.nextWeek[dayIndex]);
        },

        findNewRecipe(dayIndex: number) {
            this.weeklyMenuStore.$state.currentChosenIndex = dayIndex;
            this.$router.push("/recipe-list");
        },

        async addRecipe(dayIndex: number) {
            try {
              this.fetchRecipeDTO = {
                refrigeratorId: this.refrigeratorStore.getSelectedRefrigerator.id,
                numRecipes: 1,
                recipesFetched: this.fetchRecipeDTO.recipesFetched || [],
              };
                const response = await fetchRecipes(this.fetchRecipeDTO);
                console.log(response);
                if (response.status === 200) {
                    const recipe = response.data[0];
                    
                    const ingredients : Ingredient[] = []; 
                    for(let j = 0; j < response.data[0].ingredients.length; j++){
                        const ingredientDTO = response.data[0].ingredients[j]; 
                        const unit : Unit = {
                            id : ingredientDTO.unit.id,
                            name : ingredientDTO.unit.name,
                            weight : ingredientDTO.unit.weight 
                        }

                        const ingredient : Ingredient = {
                            id: ingredientDTO.simpleGrocery.id,
                            name: ingredientDTO.simpleGrocery.name,
                            quantity: ingredientDTO.quantity,
                            unit : unit
                        }

                        ingredients.push(ingredient); 
                    }

                    const newRecipe: Recipe = {
                        id: recipe.id,
                        name: recipe.name,
                        url: recipe.url,
                        ingredients: ingredients,
                    };

                    this.addRecipeWeek(dayIndex, newRecipe);
                    if (!this.fetchRecipeDTO.recipesFetched.includes(newRecipe.id)) {
                        this.fetchRecipeDTO.recipesFetched.push(newRecipe.id);
                    }
                }            
            } catch (error: any) {
                console.log(error);
            }
        },

      async randomRecipesEvent() {
        if(this.getAmountOfRecipesNeeded() > 0) {
            try {
                console.log(this.getAmountOfRecipesNeeded())
          this.fetchRecipeDTO.numRecipes = this.getAmountOfRecipesNeeded();
          this.fetchRecipeDTO.refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator.id;

          const response = await fetchRecipes(this.fetchRecipeDTO);
          console.log(response);
          const recipes = response.data.map((recipe : Recipe) => ({
              id: recipe.id,
              name: recipe.name,
              url: recipe.url,
              ingredients: []
            }));
                 
          if (response.status === 200) {
            for(let i = 0; i < recipes.length; i++) {
                const ingredients : Ingredient[] = []; 
                for(let j = 0; j < response.data[i].ingredients.length; j++){
                    const ingredientDTO = response.data[i].ingredients[j]; 
                    const unit : Unit = {
                        id : ingredientDTO.unit.id,
                        name : ingredientDTO.unit.name,
                        weight : ingredientDTO.unit.weight
                    }

                    const ingredient : Ingredient = {
                        id: ingredientDTO.simpleGrocery.id,
                        name: ingredientDTO.simpleGrocery.name,
                        quantity: ingredientDTO.quantity,
                        unit : unit
                    }

                    ingredients.push(ingredient); 
                }
                recipes[i].ingredients = ingredients;   
            }

            if (this.weeklyMenuStore.$state.chosenWeek === 1) {
              this.weeklyMenuStore.setCurrentWeekRandomly(recipes);
            } else {
              this.weeklyMenuStore.setNextWeekRandomly(recipes);
            }
          }
        } catch (error: any) {
          console.log(error);
        }
        }
      },

        lockRecipe(dayIndex : number) {
            this.lockRecipe(dayIndex);
        },

        unlockRecipe(dayIndex : number) {
            this.unlockRecipe(dayIndex);
        },

        getDayIndex(weekday : string){
            const index = this.Weekdays.indexOf(weekday)
            return index
        },

        getAmountOfRecipesNeeded() {
            let recipesNeeded = 0;
            if(this.weeklyMenuStore.$state.chosenWeek === 1) {
                for(let i = 0; i < this.activeWeekdays.length; i++) {
                    if(!this.weeklyMenuStore.$state.currentWeekLocks[i]) {
                        recipesNeeded++;
                    }
                }       
            } else {
                for(let i = 0; i < this.Weekdays.length; i++) {
                    if(!this.weeklyMenuStore.$state.nextWeekLocks[i]) {
                        recipesNeeded++;
                    }
                }  

            }
            return recipesNeeded;
        },

        removeAllRecipes() {
            if(this.weeklyMenuStore.$state.chosenWeek === 1 && !this.weeklyMenuStore.isCurrentWeekEmpty()) {
                if(confirm(this.t("remove_all_this_week"))) {
                    for(let i = 0; i < this.weeklyMenuStore.$state.currentWeek.length; i++) {
                    this.weeklyMenuStore.$state.currentWeek[i] = null;
                    this.weeklyMenuStore.$state.currentWeekLocks[i] = false;
                    }
                }
            } else if(this.weeklyMenuStore.$state.chosenWeek === 2 && !this.weeklyMenuStore.isNextWeekEmpty()) {
                if(confirm(this.t("remove_all_next_week"))) {
                    for(let i = 0; i < this.weeklyMenuStore.$state.nextWeek.length; i++) {
                    this.weeklyMenuStore.$state.nextWeek[i] = null;
                    this.weeklyMenuStore.$state.nextWeekLocks[i] = false;
                    } 
                } 
            } else {
                    alert(this.t("no_recipes_to_delete"))
                }
        }
    },
    components: { UnknownRecipe }
}

</script>

<style>

.recipe-pool {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: left;
    overflow: hidden;
    align-items: center;
    justify-content: center;
}

.top-buttons {
    display: flex;
    justify-content: space-between;
}

.recipe-card {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: left;
    overflow: hidden;
}

.recipe-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 5px;
    gap: 10px;
}

.weekday {
    margin-bottom: 5px;
    font-weight: bold;
}

.title {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
}

.navigation-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  margin: 20px;
  border: none;
  margin: 30px;
}

.button:hover{
  transform: scale(1.1);
}

.week-button {
    background-color: white;
    border-radius: 8%;
    border: solid black 5px;
}

.random-button {
    margin: 20px;
    background-color: white;
    border-radius: 8%;
    border: solid black 5px;
}

</style>