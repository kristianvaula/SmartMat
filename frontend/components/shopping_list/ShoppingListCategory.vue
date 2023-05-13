<template>
    <div class="grid grid-cols-1 gap-8">
        <div class="items stretch text-sm">
            <div class="ml-4 p-2 flex justify-end absolute left-0 font-extrabold">
                <h3 data-test="categoryName" class="mr-2"> {{ CategoryDetails.name }} </h3>
                <h5 data-test="numberOfCategoryItems"> [{{ categoryListItems.length }}] </h5>
            </div>
            <div class="p-2 flex justify-end absolute right-0">
                <button data-test="expandBtn" @click.stop="isCategoryExpanded = !isCategoryExpanded" class="h-5 w-5 mr-4">
                    <img v-if="isCategoryExpanded" data-test='expandLess' src="../../assets/icons/expandLess.png" alt="Expand Less">
                    <img v-else data-test='expandMore' src="../../assets/icons/expandMore.png" alt="Expand More">
                </button>            
            </div>
        </div>
        <div v-if="isCategoryExpanded" data-test="shoppingListElementWrapper" class="grid grid-cols-1 gap-8">
            <ShoppingListElement
                v-for="element in categoryListItems"
                :key="element.id"
                :ElementDetails="element"
                @updateList="loadShoppingListCategories"
                @prompt-refrigerator="promptRefrigerator()"
                data-test="shoppingListElement">
            </ShoppingListElement>
        </div>
    </div>
</template>

<script lang="ts">
import ShoppingListService from "~/service/httputils/ShoppingListService";
import { ShoppingListElementType } from "~/types/ShoppingListElement";
import { ResponseGrocery } from "~/types/ResponseGrocery";
import { defineComponent } from 'vue';

    export default defineComponent({
        props:{
            CategoryDetails: {
                type: Object as () => ShoppingListCategory,
                required: true,
            },
            ShoppingListId: {
                type: Number,
                required: true
            }
        },
        data() {
            return {
                isCategoryExpanded: true,
                categoryListItems: [] as ShoppingListElementType[],
            }
        },
        async mounted() {   
            //loads shopping list    
            this.loadShoppingList()

            //loads suggestions
            this.loadSuggestions()
        },
        methods: {
            promptRefrigerator(){
                this.$emit('prompt-refrigerator');
            },
            async loadShoppingList() {
                try {
                    let response = await ShoppingListService.getGroceriesFromCategorizedShoppingList(this.ShoppingListId, this.CategoryDetails.id)
                    response.data.forEach((element: ResponseGrocery) => {
                        let object:ShoppingListElementType = { id: element.id, description: element.description, quantity: element.quantity,unitDTO : element.unitDTO , subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: false, isFromRefrigerator: false };
                        this.categoryListItems.push(object);
                    });
                } catch (error) {
                    console.error(error)
                }
            },
            async loadSuggestions() {
                try {
                    let responseSuggestions = await ShoppingListService.getRequestedGroceriesInCategories(this.ShoppingListId, this.CategoryDetails.id);
                    if (responseSuggestions.data.length > 0) {
                        responseSuggestions.data.forEach((element: ResponseGrocery) => {
                            let object: ShoppingListElementType = { id: element.id, description: element.description, quantity: element.quantity, unitDTO : element.unitDTO, subCategoryName: element.subCategoryName, isAddedToCart: false, isSuggested: true, isFromRefrigerator: false };
                            this.categoryListItems.push(object);
                        });
                    }
                } catch (error) {
                    console.error(error)
                }
            },
            async loadShoppingListCategories() {
                this.$emit('updateList')
            }
        }
    })
</script>