<template>
  <div v-if="overLay()" class="fixed inset-0 bg-gray-950/80 z-10 lg:overflow-hidden sm:overflow-auto md:overflow-auto">
    <div class="flex items-center">
      <div class="mt-8 m-auto medium-box w-full px-5 pb-5 flex flex-col items-center
            border-2 border-black rounded-lg bg-white dark:bg-zinc-400"
      >
      <div class="hidden sm:inline-flex items-center justify-left w-full">
        <div @click="closeHandler" class="hover:cursor-pointer relative -left-10 -top-5 z-20 border-2 border-black rounded-3xl bg-white dark:bg-zinc-400 rounded-2xl inline-flex items-center">
          <img class="w-12" src="@/assets/icons/close.png" alt="">
        </div>
      </div>
      <img class="mt-5 sm:mt-0 border-2 border-black rounded-lg bg-white dark:bg-zinc-400 w-full h-auto " v-if="recipe !== null" :src="recipe.url" alt="">
      <h1 class="mt-5 ">{{ recipe?.name }}</h1>
      <table class="w-full mt-5 border-2 border-black rounded-lg bg-white dark:bg-zinc-400">
        <thead class="">
          <tr class="border-b-2 border-black">
            <th scope="col" class="px-1 sm:px-4 py-3">{{ $t("ingredients") }}</th>
            <th scope="col" class="px-0 sm:px-6 py-3">{{ $t("amount") }}</th>
            <th scope="col" class="px-2 sm:px-6 py-3 whitespace-nowrap">{{ $t("in_refrigerator") }}</th>
            <th scope="col" class="px-2 sm:px-6 py-3">{{ $t("add") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr class="text-center items-center" v-for="ingredient in recipe?.ingredients" :key="ingredient.id">
            <td class="whitespace-nowrap" >{{ ingredient.name }}</td>
            <td class="items-center">
              <div class="inline-flex items-center">
                <h3 v-if="ingredient.unit === undefined">{{ ingredient.quantity}}</h3>
                <h3 v-else >{{ ingredient.quantity + '' + ingredient.unit.name }}</h3>
              </div>
            </td>
            <td>
              <div class="flex items-center justify-start">
                <img v-if="isInFridge(ingredient) === 2" class="mx-1 w-4" src="@/assets/icons/figures/greencircle.png" alt="">
                <img v-else-if=" isInFridge(ingredient) == 1" class=" mx-1 w-4" src="@/assets/icons/figures/yellowcircle.png" alt="">
                <img v-else-if=" isInFridge(ingredient) == 0" class=" mx-1 w-4" src="@/assets/icons/figures/redcircle.png" alt="">
                <p class="opacity-80 text-sm whitespace-nowrap">{{ getFridgeStatus(ingredient.id) }}</p>
              </div>
            </td>
            <td :class="[showInputPopup ? 'opacity-50 hover:cursor-default' : 'opacity-100' ,'inline-flex']">
              <button :disabled="showInputPopup" @click="showPopup(ingredient)" :class="[showInputPopup ? 'opacity-50' : 'opacity-100 hover:cursor-pointer' ,'inline-flex justify-end w-6 m-1 focus:outline-none']">
                <img class="w-full h-full" src="@/assets/icons/add.png" alt="Add to Shopping List">
                <span class="sr-only">{{$t("add_grocery")}}</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
        <div v-if="showInputPopup" class=" z-50 bg-white flex flex-col items-center p-4 rounded-lg bg-white dark:bg-zinc-400" >
          <RefrigeratorSelectUnit @unit-set="handleSelectedUnitEvent"/>
          <div class="inline-flex items-center w-full mt-5">
            <ButtonGreenButton @click="hidePopup" class="m-1" width="100%" height="50px" :label="$t('cancel')"></ButtonGreenButton>
            <ButtonGreenButton @click="addToShoppingList" class="m-1" width="100%" height="50px" :label="$t('add')"></ButtonGreenButton>
          </div>
        </div>
        <ButtonGreenButton @click="closeHandler" class="sm:hidden m-1 mt-3 text-lg border-2 border-black rounded-lg" width="67%" height="50px" :label="$t('close')"></ButtonGreenButton>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import type { Recipe } from '~/types/RecipeType';
import type { Ingredient } from '~/types/IngredientType';
import type { SaveGrocery } from '~/types/SaveGrocery';
import type { Unit } from '~/types/UnitType';
import ShoppingListService from '@/service/httputils/ShoppingListService';
import { getMatchingIngredientsInRefrigerator} from '@/service/httputils/RefrigeratorService'; 
import { useRefrigeratorStore } from '~/store/refrigeratorStore';

  export default {
    data () {
      return {
        shoppingListId : -1,
        matchingIngredient :null as Map<Number, Ingredient[]> | null,
        showInputPopup : false,
        selectedIngredient : null as Ingredient | null,
        quantity : null as Number | null, 
        unit : null as Unit | null 
      }
    },
    props : {
      recipe : {
        type : Object as () => Recipe | null,
        default: null
      }
    },
    watch: {
      recipe() { 
        if(this.recipe !== null) {
          this.fetchShoppingList(); 
          this.fetchMatchingIngredients();
        }
      }
    },
    methods : {
      handleSelectedUnitEvent(data : {quantity : number, unit : Unit}){
        this.quantity = data.quantity; 
        let newUnit : Unit = {
          id : data.unit.id,
          name : data.unit.name,
          weight : data.unit.weight
        }
        this.unit = newUnit; 
      },
      isInFridge(grocery : Ingredient) : number{
        if(this.matchingIngredient===null) return 0; 
        let refIng : Ingredient[] | undefined = this.matchingIngredient?.get(grocery.id)
        
        if(refIng !== undefined && this.recipe !== null){
          let refWeight : number = 0; 
          for(let ingredient in refIng){
            refWeight += refIng[ingredient].quantity * refIng[ingredient].unit.weight; 
          }
          if(grocery.quantity*grocery.unit.weight <= refWeight){
            return 2; 
          }
          return 1; 
        }
        else return 0; 
      },
      getFridgeStatus(groceryId : Number) : string{
        if(this.matchingIngredient !== null && this.matchingIngredient.has(groceryId)){
          let ingredients : Ingredient[] | undefined = this.matchingIngredient.get(groceryId); 
          if(ingredients === undefined) return this.t("not_in_refrigerator"); 
          let refWeight : number = 0; 
          for(let ingredient in ingredients){
            refWeight += ingredients[ingredient].quantity * ingredients[ingredient].unit.weight; 
          }
          let message : string; 
          if(ingredients.length > 1) message = refWeight + "g"; 
          else message = ingredients[0].quantity + ingredients[0].unit.name; 
          return message; 
        }
        else return this.t("not_in_refrigerator"); 
      },
      closeHandler(){
        this.showInputPopup = false; 
        this.selectedIngredient = null; 
        this.quantity = null; 
        this.unit = null; 
        this.$emit("closeDisplayEvent"); 
      },
      overLay() : boolean {
        return this.recipe !== null
      },
      hidePopup(){
        this.showInputPopup = false; 
        this.selectedIngredient = null; 
        this.quantity = null; 
        this.unit = null; 
      },
      showPopup(ingredient : Ingredient){
        this.showInputPopup = true
        this.selectedIngredient = ingredient; 
      },
      async addToShoppingList(){
        if(this.selectedIngredient !== null && this.shoppingListId !== -1 && this.quantity !== null && this.unit !== null) {
          let grocery : SaveGrocery = {groceryId : this.selectedIngredient.id, quantity : this.quantity, foreignKey : this.shoppingListId, unitDTO : this.unit}
          ShoppingListService.saveGroceryToShoppingList(grocery)
            .then(async (response) => {
              await this.fetchMatchingIngredients(); 
              this.hidePopup();
            })
            .catch((error) => console.log(error));
        }
      },
      async fetchShoppingList(){
        let refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator?.id
        if(refrigeratorId === undefined) return 
        let response = await ShoppingListService.createShoppingList(refrigeratorId); 
        if(response && response.data) {
          this.shoppingListId = response.data; 
        }
      }, 
      async fetchMatchingIngredients(){
        let refrigeratorId = this.refrigeratorStore.getSelectedRefrigerator?.id; 
        let recipeId = this.recipe?.id; 
        if(refrigeratorId === undefined || recipeId === undefined) return 
        try{
          let response = await getMatchingIngredientsInRefrigerator(refrigeratorId, recipeId);
          if(response && response.data) {
            let map : Map<Number, Ingredient[]> = new Map<Number,Ingredient[]>();
            for(let key in response.data){
              for(let object in response.data[key]){
                let data = response.data[key][object];
                let ingredient : Ingredient = {
                  id : data.grocery.id,
                  name : data.grocery.name,
                  quantity : data.quantity,
                  unit : data.unit
                }; 
                
                if(!map.has(ingredient.id)){
                  map.set(ingredient.id, []);
                }
                map.get(ingredient.id)?.push(ingredient)
              }
            }
            if(map.size> 0) this.matchingIngredient = map;
          }
        }
        catch(error){
          console.log(error)
        }
      }

    }, 
    setup() {
      const refrigeratorStore = useRefrigeratorStore(); 
      const {t, locale, locales} = useI18n(); 

      return {refrigeratorStore, t, locale, locales}; 
    }
  }
</script>
<style  scoped>
  @media screen and (min-width: 640px) {
    .medium-box {
      min-width: 500px; 
      max-width: 500px; 
    }
  }

</style>