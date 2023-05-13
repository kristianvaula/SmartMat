<template>
    <div ref="el" class="grid grid-cols-12 w-full justify-center py-3">
      <div class ="col-span-8  text-sm md:text-base flex">
        <p data-test="description">{{ props.grocery.grocery.description }} |</p>

        <p data-test="quantity" class="mx-2"> {{ props.grocery.quantity }}{{ props.grocery.unit.name }}</p>
      </div>
      <div class="align-middle col-span-3 flex rounded-lg align-middle">
        <img v-if="needsConfirmation" src="../../assets\icons\done.png" alt="Menu" class="w-5 h-5 m-1 align-middle bg-green-500 hover:bg-green-600 rounded-xl" @click="setDate()">
        <input type="date" :id="'expiry-date-' + props.grocery.id" name="expiry-date" class="hover:bg-zinc-500 h-6 bg-inherit text-sm md:text-base rounded-lg" :class="{'text-red-500' : isNearExpiry() }">
      </div>
      <button class="justify-self-end align-middle hover:bg-zinc-500 rounded-3xl" @click="clicked()">
        <img src="../../assets\icons\menu.png" alt="Menu" class="h-5 w-5">
      </button>
    </div>
  </template>
  <script setup lang="ts">
  import type { GroceryEntity } from '~/types/GroceryEntityType';
  import { updateGrocery } from '~/service/httputils/GroceryService';
  import { getNotifications } from '~/service/httputils/NotificationService';
  import { useNotificationStore } from '~/store/notificationStore';
  import { useRefrigeratorStore } from '~/store/refrigeratorStore';
  import { defineComponent, defineProps, onMounted } from 'vue';
  const notificationStore = useNotificationStore();
  const refrigeratorStore = useRefrigeratorStore();

  async function loadNotifications(){
    try{
      const response = await getNotifications();
      if(response.status == 200){
        notificationStore.setNotification(response.data);
      }
    }catch(error : any){
      console.log(error);
    }
  }

  const props = defineProps({
    grocery: {
      type: Object as () => GroceryEntity,
      required: true,
    }
  });
  
  const el = ref<HTMLDivElement | null>(null);
  const emit = defineEmits(['element-height', 'emit-date']);
  
  function clicked() {
      emit('element-height', el.value?.getBoundingClientRect().y);
      refrigeratorStore.setSelectedGrocery(props.grocery);
  }
  
  function isNearExpiry(): boolean {
    const dateDifferenceInMilliseconds = Date.parse(props.grocery.physicalExpireDate.toString()) - Date.now();
    const daysUntilExpiry = Math.ceil(dateDifferenceInMilliseconds / (1000 * 60 * 60 * 24));
    return daysUntilExpiry <= 3;
  }

  async function setDate(){
    const expiryDateInput = document.getElementById(`expiry-date-${props.grocery.id}`) as HTMLInputElement | null;
    if(expiryDateInput){
      const newGrocery = props.grocery;
      if(expiryDateInput.valueAsDate !== null){
        console.log(expiryDateInput.value)
        newGrocery.physicalExpireDate = expiryDateInput.valueAsDate;
        try{
          const response = await updateGrocery(newGrocery);
          if(response.status == 200){
            emit('emit-date', newGrocery);
            needsConfirmation.value = false;
            loadNotifications();
          }
        }
        catch(error){
          console.log("Error occured while updating grocery" + error);
        }
      }
      else{
        console.log("invalid date")
      }
    }
    else{
      console.log("Could not find input element");
    }
  }
  

  let needsConfirmation = ref<boolean>(false);

  onMounted(() => {
    // get the expiry date input element
    const expiryDateInput = document.getElementById(`expiry-date-${props.grocery.id}`) as HTMLInputElement | null;
    if (expiryDateInput && props.grocery.physicalExpireDate instanceof Date) {
      // set the value of the input to the grocery's physicalExpireDate
      expiryDateInput.valueAsDate = props.grocery.physicalExpireDate;
      
      
      expiryDateInput.addEventListener('input', () => {
        if(expiryDateInput.valueAsDate?.getDate() !== props.grocery.physicalExpireDate.getDate()){
          needsConfirmation.value = true;
        }
        else{
          needsConfirmation.value = false;
        }
      } )
      // set the minimum value of the input to the current date
      const today = new Date().toISOString().substr(0, 10);
      expiryDateInput.min = today;
    } else {
      console.error('Could not find expiry date input element');
    }
  });
  </script>