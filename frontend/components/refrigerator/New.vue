<template>
    <div class="w-full flex justify-center h-fit overflow-y-scroll">
      <div class ="w-11/12 border-2 mt-4 ml-2 border-black rounded-xl bg-white dark:bg-zinc-400 ">
        <form @submit.prevent class ="form bg-white mt-3 ml-2 dark:bg-zinc-400 w-11/12 flex flex-col">
          <div>
            <RefrigeratorDropdown @update-value="(payload) => {grocery = payload}"  />
          </div>
          <div>
            <RefrigeratorSelectUnit @unit-set="({unit, quantity}) => setUnit(unit, quantity)"/>
          </div>
          <div class="flex flex-row justify-center">
            <ButtonGrayButton @click="onSubmit" class ="self-center text-xs sm:text-base my-4 mx-2" :label="$t('create_grocery')" width="30%" height="50px"/>
            <ButtonGrayButton @click="emit('toggle', false)" class ="self-center text-xs sm:text-base my-4 mx-2" id="submit" :label="$t('go_back')" width="30%" height="50px"/>
          </div>
        </form>
      </div>
    </div>
</template>

<script setup lang="ts">
import { createGrocery } from '~/service/httputils/GroceryService';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { Grocery } from '~/types/GroceryType';
import { Unit } from '~/types/UnitType';

const router = useRouter();

    const { t } = useI18n();

    let grocery : Grocery | null = null;
    let unit : Unit | null = null;
    let quantity : number | null = null;
    const refrigeratorStore = useRefrigeratorStore();
const emit = defineEmits(['toggle'])

onBeforeUnmount(() => {
  emit('toggle');
})

function setUnit(newUnit : Unit, newQuantity : number){
  unit = newUnit;
  quantity = newQuantity;
}




async function onSubmit(){
    if(grocery !== null && unit !== null && quantity !== null && quantity !== 0){
            try{
        const response = await createGrocery(refrigeratorStore.getSelectedRefrigerator!.id, grocery!, unit, quantity);
        if(response.status == 200){
           emit('toggle', false); 
        }
    }catch(error){
        console.log(error);
    }
        }
}
</script>