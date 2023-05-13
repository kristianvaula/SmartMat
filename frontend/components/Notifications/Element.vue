<template>
    <div class="relative w-full">
      <div class="flex-col border border-black text-lg  bg-green-color dark:form-dark-color dark:text-white rounded-xl my-3 mx-2 p-3">
        <button @click = "emit('delete-notif',notification)" class="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center">
        X
      </button>
        <div class="flex flex-cols-2 p-2 justify-center">
          <img src="../../assets\icons\restaurant.png" alt="Restaurant" class="h-5 w-5 ml-5 mr-2">
          <div>
            <div v-if="notification.daysLeft == 0" class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{ $t('expires_today') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
            <div v-else-if="notification.daysLeft == 1" class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{ $t('expires_tomorrow') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
            <div v-else class="mr-5 ml-2">
              {{notification.refrigeratorGrocery.grocery.name}} {{$t('expires_in')}} {{ notification.daysLeft }} {{ $t('days') }} : {{ notification.refrigeratorGrocery.physicalExpireDate.toLocaleDateString()}}
            </div>
          </div>
        </div>
        <div class="flex flex-cols-2 p-2 justify-center align-middle">
          <img src="../../assets\icons\refrigerator.png" alt="Refrigerator" class="h-5 w-5 ml-5 mr-2">
          <div class="text-center">{{notification.refrigeratorGrocery.refrigerator.name }}</div>
        </div>
        <div class="py-2 text-center " >
          <button @click="goToRecipes()" class="border bg-white dark:bg-neutral-50 text-black border-black w-36 mx-2 p-1 my-1 rounded hover:bg-slate-400 cursor-pointer">
            {{ $t('find_recipe') }}</button>
          <button @click="goToMenu()" class="border bg-white dark:bg-neutral-50 text-black border-black mx-2 w-36 p-1 my-1 rounded hover:bg-slate-400 cursor-pointer">
            {{ $t('go_to_weekly_menu') }}</button>
          <button @click="goToFridge()" class="border bg-white dark:bg-neutral-50 text-black border-black mx-2 w-36 p-1 my-1 rounded hover:bg-slate-400 cursor-pointer">
            {{ $t('to_refrigerator') }}</button>
        </div>
      </div>
    </div>
  </template>
  


<script setup lang="ts">
import { GroceryNotification } from '~/types/GroceryNotificationType';
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { useRouter } from "vue-router";
import { defineEmits, defineProps } from "vue";
const router = useRouter();
const refrigeratorStore = useRefrigeratorStore();
const props = defineProps({
    notification :{
        type: Object as () => GroceryNotification,
        required:true
    }
});

function goToFridge(){
  refrigeratorStore.setSelectedRefrigerator(props.notification.refrigeratorGrocery.refrigerator);
  router.push('/home');
}
function goToRecipes(){
  router.push('/recipe-list');
}
function goToMenu(){
  router.push('/weekly-menu');
}

const emit = defineEmits(['delete-notif'])

onMounted(() => {
    console.log(props.notification)
});
</script>