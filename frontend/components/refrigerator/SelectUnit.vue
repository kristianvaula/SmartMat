<template>
  <div class="flex flex-row justify-center">
    <div class="mx-5">
      <input class="p-2 w-16 rounded-xl text-center rounded-sm shadow font-thin focus:outline-none focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300"
             type="number"
             :placeholder="$t('amount')"
             v-model="selectedQuantity"
             :max="allowedMax ? allowedMax : undefined"
             :min="0"
             @input="enforceMax"
             pattern="^\d+$"
             onkeypress="return /\d/.test(String.fromCharCode(event.keyCode));" />
    </div>
    <div class="dark:bg-zinc-400">
      <div class="w-full justify-center flex">
        <template v-for="(unit, index) in units">
          <input type="radio"
                 :id="unit.id"
                 :value="unit.id"
                 :checked="unit.id === selectedUnit?.id"
                 @click="setSelected(unit)"
                 class="sr-only" />
          <label :for="unit.id"
                 :class="[
              'w-8 text-center p-2 rounded-sm shadow font-thin focus:outline-none focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300',
              index === 0 ? 'rounded-l-lg' : '',
              index === units.length - 1 ? 'rounded-r-lg' : '',
              unit.id === selectedUnit?.id ? 'bg-green-color text-white' : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]">
            {{ unit.name }}
          </label>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Unit } from '~/types/UnitType';
import axiosInstance from '~/service/AxiosInstance';
import { GroceryEntity } from '~/types/GroceryEntityType';
import { defineProps, defineEmits, ref, watch, onMounted } from 'vue';

let open = ref(false);

let units = ref<Unit[]>([]);
let selectedUnit = ref<Unit | null>(null);
let selectedUnitName = ref("");
let selectedQuantity = ref<number>(0);
let allowedMax = ref<number | null>(null);


const props = defineProps({
  grocery : {
    type: Object as () => GroceryEntity
  }
});

function unitConversion(unitFrom : Unit , unitTo : Unit, quantity : number){
  return (unitFrom.weight*quantity)/unitTo.weight;
}

function enforceMax() {
  if (allowedMax.value !== null && selectedQuantity.value > allowedMax.value!) {
    selectedQuantity.value = allowedMax.value!;
  }
}

function loadGrocery(){
  if(props.grocery){
    setSelected(props.grocery.unit);
    selectedQuantity.value = props.grocery.quantity;
    allowedMax.value = props.grocery.quantity;
  }
}

async function getUnits(){
    const response = await axiosInstance.get("/api/refrigerator/units");
    if(response.status == 200){
        units.value = response.data;
    }
}
const emit = defineEmits(['unit-set']);

watch([selectedUnit, selectedQuantity], ([unit, quantity], [oldUnit, oldQuantity]) => {
    if (unit && quantity) {
      if(unit && oldUnit && props.grocery){
        if(unit.id !== oldUnit.id){
          allowedMax.value = unitConversion(oldUnit, unit, quantity);
          selectedQuantity.value = unitConversion(oldUnit, unit, quantity);
          quantity = unitConversion(oldUnit, unit, quantity);
          setSelected(unit);
        }
      }
        emit('unit-set', { unit, quantity });
    }
});



function setSelected(unit : Unit){
    selectedUnit.value = unit;
    selectedUnitName.value = unit.name;
}
onMounted(() => {
    loadGrocery();
    getUnits();

})
</script>

<style scoped>
#listwrapper {
  overflow-y: auto;
  height: auto;
  scrollbar-width: none;
  overflow-x: hidden;
}

#listwrapper::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
#listwrapper {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
