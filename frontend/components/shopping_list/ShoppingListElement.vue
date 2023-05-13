<template>
    <div class="items stretch text-sm">
        <div :class="{'text-blue-800 font-bold': isElementSuggested}" class="ml-4 p-2 w-3/5 flex absolute left-0">
            <h3 class="mr-2 truncate break-words" data-test="ElementDetailsDescription"> {{ ElementDetails.description }} </h3>
            <h5 class="mr-2" data-test="ElementDetailsQuantity"> ({{ ElementDetails.quantity }} {{ ElementDetails.unitDTO.name }})</h5>
        </div>
        <div class="p-2 flex justify-end absolute right-0">
            <div v-if="isElementSuggested">
                <button @click.stop="updateGrocery(ElementDetails.quantity)" class="h-5 w-5 mr-1" data-test="AcceptSuggestionButton">
                    <img src="../../assets/icons/done.png" alt="Accept">
                </button>
            </div>
            <div v-if="!editElement">
               <div v-if="isElementAddedToCart">
                    <button @click.stop="removeElementFromCart" class="h-5 w-5 mr-4" data-test="RemoveElementFromCart">
                        <img src="../../assets/icons/undo.png" alt="Undo">
                    </button>
                </div>
                <div v-else>
                    <button @click.stop="removeElementFromList" class="h-5 w-5 mr-4" data-test="RemoveElementFromList">
                        <img src="../../assets/icons/remove.png" alt="Remove">
                    </button>
                </div> 
            </div>
            <div v-if="!editElement">
                <div v-if="isElementAddedToCart">
                    <button @click.stop="addElementToRefrigerator" class="h-5 w-5 mr-4" data-test="AddElementToRefrigerator">
                        <img src="../../assets/icons/refrigerator.png" alt="Add to refrigerator">
                    </button>
                </div>
                <div v-else>
                    <button @click.stop="addElementToShoppingCart" class="h-5 w-5 mr-4" data-test="AddElementToShoppingCart">
                        <img src="../../assets/icons/addToCart.png" alt="Add to cart">
                    </button>
                </div>
            </div>
            <div v-if="!editElement">
                <button @click.stop="editElement = true" class="h-5 w-5 mr-4" data-test="EditElement">
                    <img src="../../assets/icons/edit.png" alt="Edit"> 
                </button>
            </div>
            <div v-if="editElement" data-test="editWrapper" class="bg-white absolute flex justify-end right-0 mr-6">
                <div class="flex flex-row">
                    <button v-if="newQuantity > 1" @click.stop="newQuantity--" class="h-5 w-5 mr-2" data-test="NewQuantityDecrement">
                        <img src="../../assets/icons/expandMore.png" alt="decrement">
                    </button>
                    <h3 class="h-5 w-5 font-extrabold mr-2" data-test="NewQuantity"> {{ newQuantity }} </h3>
                    <button @click.stop="newQuantity++" class="h-5 w-5 mr-8" data-test="NewQuantityIncrement">
                        <img src="../../assets/icons/expandLess.png" alt="increment">
                    </button> 
                    <button @click.stop="updateGrocery(newQuantity)" class="h-5 w-5 mr-3" data-test="UpdateGroceryNewQuantity">
                        <img src="../../assets/icons/done.png" alt="Done">
                    </button>
                    <button @click.stop="editElement = false" class="h-5 w-5" data-test="UpdateGroceryClose">
                        <img src="../../assets/icons/close.png" alt="Close">
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ShoppingCartService from "~/service/httputils/ShoppingCartService";
import ShoppingListService from "~/service/httputils/ShoppingListService";
import { ShoppingListElementType } from "~/types/ShoppingListElement";
    export default defineComponent({
        props:{
            ElementDetails: {
                type: Object as () => ShoppingListElementType,
                required: true,
            }
        },
        data() {
            return {
                isElementAddedToCart:false,
                isElementSuggested:false,
                editElement:false,
                newQuantity:1,

            }
        },
        mounted() {        
            this.isElementAddedToCart = this.ElementDetails.isAddedToCart
            this.isElementSuggested = this.ElementDetails.isSuggested
            this.newQuantity = this.ElementDetails.quantity
            console.log(this.ElementDetails);
        },
        methods: {
            async removeElementFromCart() {
                // Remove the element from the cart and add it back to the list
                let transferStatus = await ShoppingCartService.transferGroceryToShoppingList(this.ElementDetails.id);
                console.log(transferStatus);
                if (transferStatus.data) {
                    this.$emit('prompt-refrigerator');
                    //alert("Varen ble vellykket lagt tilbake i handlelisten")
                } else {
                    alert("Det oppstod en feil ved overføring av varen")
                }
            },
            async removeElementFromList() {
                // Remove the element from the list
                let deleteResponse: any;            
    
                if (!this.ElementDetails.isFromRefrigerator) {
                    deleteResponse = await ShoppingListService.removeGroceryFromShoppingList(this.ElementDetails.id);
                } else {
                    deleteResponse = await ShoppingListService.removeRefrigeratorGroceryFromShoppingList(this.ElementDetails.id);
                }
                this.$emit('updateList')
                if (deleteResponse.data) {
                    //alert("Varen ble vellykket slettet")
                } else {
                    alert("Det oppstod en feil ved sletting av varen")
                }
            },
            async addElementToRefrigerator() {
                let transferStatus: any;
                if (!this.ElementDetails.isFromRefrigerator) {
                    transferStatus = await ShoppingCartService.transferToRefrigerator(this.ElementDetails.id, this.ElementDetails.unitDTO, this.ElementDetails.quantity);    
                }
                this.$emit('updateList')            
                if (transferStatus.status == 200) {
                    this.$emit('prompt-refrigerator');
                    //alert("Varen ble vellykket overført")
                } else {
                    alert("Det oppstod en feil ved overføring av varen")
                }
            },
            async addElementToShoppingCart() {
                // Add the element to the shoppingCart
                let transferStatus: any;
                if (!this.ElementDetails.isFromRefrigerator) {
                    transferStatus = await ShoppingListService.transferGroceryToShoppingCart(this.ElementDetails.id);
                } else {
                    transferStatus = await ShoppingListService.transferRefrigeratorGroceryToShoppingCart(this.ElementDetails.id);
                }
                this.$emit('updateList')
                if (transferStatus.data) {
                    //alert("Varen ble vellykket overført")
                } else {
                    alert("Det oppstod en feil ved overføring av varen")
                }
            },
            async updateGrocery(newQuantity: number) {
                this.editElement = false
                this.ElementDetails.quantity = newQuantity
                let quantity = newQuantity
                if (!this.ElementDetails.isFromRefrigerator) {
                    await ShoppingListService.updateGrocery(this.ElementDetails.id, quantity)
                } else {
                    await ShoppingListService.updateRefrigeratorGrocery(this.ElementDetails.id, quantity);
                }
                this.$emit('updateList')
            }
        }
    })
</script>
