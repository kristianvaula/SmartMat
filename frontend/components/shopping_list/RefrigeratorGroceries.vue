<template>
    <div class="grid grid-cols-1 gap-8">
        <div class="items stretch text-sm">
            <div class="ml-4 p-2 flex justify-end absolute left-0 font-extrabold">
                <h3 data-test="suggestedRefrigeratorCategory" class="mr-2"> Foreslått fra kjøleskap </h3>
                <h5 data-test="numberOfCategoryItems"> [{{ CategoryListItems?.length }}] </h5>
            </div>
            <div class="p-2 flex justify-end absolute right-0">
                <button data-test="expandBtn" @click.stop="isCategoryExpanded = !isCategoryExpanded" class="h-5 w-5 mr-4">
                    <img v-if="isCategoryExpanded" data-test="expandMore" src="../../assets/icons/expandLess.png" alt="Expand Less">
                    <img v-else data-test="expandLess" src="../../assets/icons/expandMore.png" alt="Expand More">
                </button>            
            </div>
        </div>
        <div v-if="isCategoryExpanded" data-test="shoppingListElementWrapper" class="grid grid-cols-1 gap-8">
            <ShoppingListElement
                v-for="element in CategoryListItems"
                :key="element.id"
                :ElementDetails="element"
                @updateList="updateList()"
                data-test="shoppingListElement"
                >
            </ShoppingListElement>
        </div>
    </div>
</template>

<script lang="ts">
import { ShoppingListElementType } from '~/types/ShoppingListElement';
import { defineComponent } from 'vue';
    export default defineComponent({
        props:{
            ShoppingListId: {
                type: Number,
                required: true
            },
            CategoryListItems: {
                type: Array as () => ShoppingListElementType[],
                require: true
            }
        },
        data() {
            return {
                isCategoryExpanded: false,
            }
        },
        methods: {
            updateList(){
                this.$emit('updateList')
            }
        }
    })
</script>