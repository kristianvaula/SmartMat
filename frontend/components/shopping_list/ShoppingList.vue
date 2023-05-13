<template>
    <div class="mt-5 font-mono text-sm flex justify-end">
    <div class="w-full font-mono text-sm flex justify-center">
        <div class="w-11/12  lg:w-full h-96 overflow-auto bg-white dark:bg-zinc-400 border-2 rounded-lg border-black relative">
            <div>
                <div class="m-2 pl-2 pr-2 flex justify-center text-lg font-sans font-medium">
                    <button @click.stop="selectListTab" :class="{'bg-sky-400 dark:bg-sky-900 dark:text-white': menuOptions.isShoppingListSelected, 'bg-white dark:bg-zinc-400 dark:text-white': !menuOptions.isShoppingListSelected}" class="pl-4 pr-4 border-2 rounded-l-lg border-black cursor-pointer">
                      {{ t('shopping_list') }} </button>
                    <button @click.stop="selectCartTab" :class="{'bg-sky-400 dark:bg-sky-900 dark:text-white': menuOptions.isShoppingCartSelected, 'bg-white dark:bg-zinc-400 dark:text-white': !menuOptions.isShoppingCartSelected}" class="pl-4 pr-4  border-2 rounded-r-lg border-black cursor-pointer">
                      {{ t('shopping_cart') }} </button>
                </div>
            </div>
            <div class="flex justify-center">
                <div v-if="menuOptions.isShoppingListSelected">
                    <div v-if="(categoryList === null || categoryList.length === 0) && (refrigeratorSuggestions === null || refrigeratorSuggestions.length === 0)">
                        <h3 class="mt-3"> {{ t('you_have_no_groceries_in_the_shopping_list') }} </h3>
                    </div>
                    <div class="grid grid-cols-1 gap-8">
                        <div v-if="categoryList !== null && categoryList.length !== 0" class="grid grid-cols-1 gap-8">
                            <ShoppingListCategory
                                v-for="category in categoryList"
                                :key="category.id"
                                :CategoryDetails="category"
                                :ShoppingListId="shoppingListId"
                                @updateList="loadCategories"
                                @prompt-refrigerator="promptRefrigerator()">
                            </ShoppingListCategory>
                        </div>
                        <div v-if="refrigeratorSuggestions !== null && refrigeratorSuggestions.length !== 0">
                            <RefrigeratorGroceries
                                :ShoppingListId="shoppingListId"
                                :CategoryListItems="refrigeratorSuggestions"
                                @updateList="loadLists()"
                                >
                            </RefrigeratorGroceries>
                        </div>
                    </div>
                  <div class="grid grid-cols-1 gap-8 mt-12">
                    <!-- ... -->
                    <div class="p-2 flex justify-end mb-4">
                      <button @click.stop="addNewElementSelected = true" class="pl-2 pr-2 text-lg rounded-lg font-sans border-2 border-black cursor-pointer hover:bg-sky-300 bg-sky-400">{{ t('add_a_new_grocery') }}</button>
                    </div>
                  </div>
                </div>

              <div v-if="menuOptions.isShoppingCartSelected">
                    <div v-if="shoppingCart === null || shoppingCart.length === 0">
                        <h3 class="mt-3"> {{ t('you_have_no_groceries_in_the_shopping_cart') }} </h3>
                    </div>
                    <div v-else class="grid grid-cols-1 gap-8">
                        <ShoppingListElement
                            v-for="element in shoppingCart"
                            :key="element.id"
                            :ElementDetails=element
                            @updateList="loadShoppingCart"
                            @prompt-refrigerator="promptRefrigerator()">
                        </ShoppingListElement>
                      <div class="grid grid-cols-1 gap-8 mt-12">
                        <!-- ... -->
                        <div class="p-2 flex justify-end mb-4">
                          <button @click.stop="addAllElementsToRefrigerator" class="pl-2 pr-2 text-lg font-sans border-2 rounded-full border-black cursor-pointer hover:bg-sky-300 bg-sky-400"> {{ t('put_everything_in_the_refrigerator') }} </button>
                        </div>
                      </div>
                    </div>
                </div>
            </div>
        </div>
      <div v-if="addNewElementSelected" class="w-4/5 md:w-2/5 h-96 p-1 bg-white dark:bg-zinc-400 border-2 rounded-lg border-black absolute">
        <div>
            <RefrigeratorDropdown @update-value="(payload) => {grocery = payload}" />
            <div class="flex justify-center h-full">
                <RefrigeratorSelectUnit  @unit-set="({unit, quantity}) => setUnit(unit, quantity)" />
                <button @click="addGroceryToShoppingList()" class="h-10 w-10 align-middle mx-3 p-1">
                        <img class="block" src="../../assets/icons/add.png" alt="add">
                </button>
            </div>
        </div>
        <div class="p-2 flex justify-end sticky -bottom-2 right-0">
          <button @click.stop="addNewElementSelected = false" class="hover:bg-sky-200 bg-sky-300 border-2 rounded-full border-black h-8 w-8">
            <img src="../../assets/icons/close.png" alt="Close">
          </button>
        </div>
      </div>
    </div>
</div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
import ShoppingCartService from "~/service/httputils/ShoppingCartService";
import ShoppingListElement from "./ShoppingListElement.vue";
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import RefrigeratorGroceries from "./RefrigeratorGroceries.vue";
import { Unit } from "~/types/UnitType";
import { SaveGrocery } from "~/types/SaveGrocery";
import { Grocery } from "~/types/GroceryType";
import { ShoppingListElementType } from "~/types/ShoppingListElement";
import { ResponseGrocery } from"~/types/ResponseGrocery"
    export default defineComponent({
    props: {
        refrigeratorId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            menuOptions: {
                isShoppingListSelected: true,
                isShoppingCartSelected: false,
            },
            addNewElementSelected: false,
            refrigeratorId: -1,
            shoppingListId: -1,
            shoppingCartId: -1,
            categoryList: [] as ShoppingListCategory[],
            shoppingCart: [] as ShoppingListElementType[],
            refrigeratorSuggestions: [] as ShoppingListElementType[],
            unit : {} as Unit,
            quantity : -1,
            grocery : {} as Grocery,
               };
    },
    mounted() {
        const refrigeratorStore = useRefrigeratorStore();
        if (refrigeratorStore.getSelectedRefrigerator !== null) {
            this.refrigeratorId = refrigeratorStore.getSelectedRefrigerator.id
        }
        this.loadLists();
        watch(() => refrigeratorStore.getSelectedRefrigerator, () => {
            if(refrigeratorStore.getSelectedRefrigerator !== null){
                this.refrigeratorId = refrigeratorStore.getSelectedRefrigerator?.id;
                this.loadLists();
            }
        });
    },
    setup() {
      const { t } = useI18n();

      return { t }
    },
    
    
    methods: {
        promptRefrigerator(){
                this.$emit('prompt-refrigerator');
            },
        async addGroceryToShoppingList() {
            if(this.grocery.id && this.unit.id && this.quantity > 0){
                const grocery: SaveGrocery = { groceryId: this.grocery.id, quantity: this.quantity, foreignKey: this.shoppingListId, unitDTO : this.unit};
                let responseStatus = await ShoppingListService.saveGroceryToShoppingList(grocery);

                if (responseStatus.status !== 200) {
                    alert("Det oppstod en feil ved overføring av varen til handlelisten")
                }
                else{
                    this.loadCategories();
                    this.loadShoppingCart();
                    this.loadSuggestionsFromRefrigerator();
                }
            }
        },
        setUnit(newUnit : Unit, newQuantity : number){
            if(newUnit && newQuantity){
                this.unit = newUnit;
                this.quantity = newQuantity;
            }
        },
        async loadLists() {
            //create/load shopping list
            let responseListId = await ShoppingListService.createShoppingList(this.refrigeratorId);
            this.shoppingListId = responseListId.data;
            //create/load shopping cart
            let responseCartId = await ShoppingCartService.createShoppingCart(this.shoppingListId);
            this.shoppingCartId = responseCartId.data;

            //loads categories
            this.loadCategories()
            //loads shopping cart
            this.loadShoppingCart()
            //loasds suggestions list from refrigerator
            this.loadSuggestionsFromRefrigerator();
        },
        async loadCategories() {
            try {
                this.categoryList = []
                let responseCategories = await ShoppingListService.getCategoriesFromShoppingList(this.shoppingListId);
                if (responseCategories.data.length > 0) {
                    responseCategories.data.forEach((element: ShoppingListCategory) => {
                        this.categoryList.push(element);
                    });
                }
            } catch (error) {
                console.error(error);
                this.categoryList = [];
            }
        },
        async loadShoppingCart() {
            try {
                this.shoppingCart = []
                let responseCart = await ShoppingCartService.getGroceriesFromShoppingCart(this.shoppingCartId);
                if (responseCart.data.length > 0) {
                     responseCart.data.forEach((element: ResponseGrocery) => {
                        let object: ShoppingListElementType = { id: element.id, description: element.description, quantity: element.quantity,unitDTO : element.unitDTO, subCategoryName: element.subCategoryName, isAddedToCart: true, isSuggested: false, isFromRefrigerator: false };
                        this.shoppingCart.push(object);
                    }); 
                }
            } catch (error) {
                console.error(error);
                this.shoppingCart = [];
            }
        },
        async loadSuggestionsFromRefrigerator() {
                try {
                    let responseSuggestions = await ShoppingListService.getSuggestedGroceriesFromRefrigerator(this.shoppingListId);
                    this.refrigeratorSuggestions = [];
                    if (responseSuggestions.data.length > 0) {
                        responseSuggestions.data.forEach((element : ResponseGrocery) => {
                            let object : ShoppingListElementType = { id: element.id, description: element.description, quantity: element.quantity, unitDTO : element.unitDTO, subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: true, isFromRefrigerator: true };
                            this.refrigeratorSuggestions.push(object);
                        });
                    }
                    else{
                        this.refrigeratorSuggestions = [];
                    }
                } catch (error) {
                    console.error(error)
                }
        },
        selectListTab() {
            this.menuOptions.isShoppingListSelected = true
            this.menuOptions.isShoppingCartSelected = false
            this.loadCategories()
        },
        selectCartTab() {
            this.menuOptions.isShoppingListSelected = false
            this.menuOptions.isShoppingCartSelected = true
            this.loadShoppingCart()
        },
        async addAllElementsToRefrigerator() {
            // Add an element from the shoppingCart to the Refrigerator
            let groceries : SaveGrocery[] = [];
            this.shoppingCart.forEach((element: ShoppingListElementType) => {
                groceries.push({
                    groceryId : element.id,
                    unitDTO : element.unitDTO,
                    quantity : element.quantity,
                    foreignKey : -1,
                })
            });            
            let transferStatus = await ShoppingCartService.transferAllToRefrigerator(groceries);
            this.loadShoppingCart()
            if (transferStatus.status == 200) {
                this.$emit('prompt-refrigerator');
            } else {
                alert("Det oppstod en feil ved overføring av varen")
            }
        },
        closeAddGrocery() {
            this.addNewElementSelected = false
            this.loadCategories()
        }
    },
    components: { RefrigeratorGroceries }
})
</script>


<style scoped>
.height {
  height: 550px;
}

@media screen and (max-width: 640px) {
  .height {
    height: 350px;
  }
}
</style>

