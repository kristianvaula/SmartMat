<template>
  <div class="mx-auto py-5 w-6/12 sm:w-4/12 md:w-3/12 flex">
    <label for="search" class="sr-only">{{t('search_recipes')}}</label>
    <div class="relative mx-auto">
      <input type="text" id="search" v-model="searchTerm" class="border border-gray-400 pl-6 p-2 rounded-md w-full" :placeholder="$t('search_recipes')">
      <div class="absolute inset-y-0 left-0 pl-2 flex items-center">
        <img src="../../assets/icons/search.png" alt="Search" class="h-4 w-4" />
      </div>
    </div>
  </div>
  <div class="flex flex-wrap justify-center w-3/4 mx-auto" v-if="recipesPage.length > 0">
    <RecipeCard
        v-for="recipe in displayedRecipes"
        :key="recipe.id"
        :recipe-info="recipe"
        @see-recipe-event="seeRecipeNextWeek"
        class="w-full sm:w-1/2 lg:w-1/3 xl:w-1/4 py-2 mr-2 sm:mr-0" />
  </div>
  <div class="flex justify-center my-4 pb-5" v-if="pageCount > 1">
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="previousPage" :disabled="currentPage === 1">{{ t('previous_page') }}</button>
    <button class="bg-gray-200 hover:bg-gray-300 mx-2 px-4 py-2 rounded" @click="nextPage" :disabled="currentPage === pageCount">{{ t('next_page') }}</button>
  </div>
</template>

<script lang="ts">
import {fetchRecipes } from "~/service/httputils/RecipeService";
import { useRefrigeratorStore } from "~/store/refrigeratorStore";
import {onMounted} from "vue";
import {integer} from "vscode-languageserver-types";
import { fetchAllRecipes} from "~/service/httputils/RecipeService";
import { useWeeklyMenuStore } from "~/store/WeeklyMenuStore";
import { Ingredient } from "~/types/IngredientType";
import { Unit } from "~/types/UnitType";
import { Recipe } from "~/types/RecipeType";

export default {
  data (){
    return {
      //recipesPage :[] as Recipe[] | null
    }
  },
  watch: {
    recipesPage(){
      //console.log(this.recipesPage); 
    }
  },
  setup() {
    const { t } = useI18n();
    const refrigeratorStore = useRefrigeratorStore();


    const recipesPage = ref<Recipe[]>([]);
    const searchTerm = ref("");
    const currentPage = ref(1);
    const pageSize = 12;
    let data = [] as Array<Recipe>;
    const weeklyMenuStore = useWeeklyMenuStore();


    const errorMessage = ref('');
    const catchError = ref(false);


    const filteredRecipes = computed(() =>
        recipesPage.value.filter((recipe) =>
            recipe.name.toLowerCase().includes(searchTerm.value.toLowerCase())
        )
    );

    const displayedRecipes = computed(() => {
      const start = (currentPage.value - 1) * pageSize;
      const end = start + pageSize;
      return filteredRecipes.value.slice(start, end);
    });


    const pageCount = computed(() => Math.ceil(filteredRecipes.value.length / pageSize));

    function previousPage(): void {
      currentPage.value--;

      console.log('previousPage called');
      window.scrollTo(0, 0);
    }

    function nextPage(): void {
      currentPage.value++;
      window.scrollTo(0, 0);
    }


    return {
      refrigeratorStore,
      t,
      searchTerm,
      currentPage,
      pageSize,
      data,
      weeklyMenuStore,
      recipesPage,
      errorMessage,
      catchError,
      displayedRecipes,
      pageCount,
      previousPage,
      nextPage,
    }
    },

    methods: {
      async loadRecipes() {
      try {
              const response = await fetchAllRecipes();
              
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
                this.recipesPage = recipes;  
              }
      } catch (error: any) {
        this.errorMessage = error.response.data || 'An error occurred.';
        this.catchError = true;
      }
    },

    seeRecipeNextWeek(recipe : Recipe) {
      this.$emit("seeRecipeEvent", recipe)
    }
    },
    mounted() {
      this.loadRecipes();
      console.log(this.recipesPage)
    }
}



</script>

<style scoped>

</style>