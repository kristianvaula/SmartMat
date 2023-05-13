<template>
  <div class="wrapper">
    <Head>
      <Title>SmartMat</Title>
    </Head>
    <div v-if="select" class="flex justify-center w-44 truncate mx-auto rounded-xl">
      <SelectPrompt @clicked="(payload) => goToFridge(payload)" :refrigerators="refrigeratorStore.getRefrigerators"/>
    </div>
    <div v-else class="flex justify-center pb-5 sm:pb-0">
      <!-- logo -->
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="dark:hidden block w-72 h-auto sm:w-1/2 sm:h-auto image rounded-3xl pointer-events-none">
      <img src="../assets/icons/smartmat/smartMat_transparent.png" alt="logo" class="dark:block hidden w-72 h-auto sm:w-1/2 sm:h-auto image rounded-3xl pointer-events-none">
    </div>
    <div class="flex flex-col sm:flex-row sm:justify-center text-center mt-14" >
      <NuxtLink v-if="loggedIn && isSelected()" :to="localePath('/home')" class="sm:mt-5 sm:pr-4">
        <button class="w-56 h-14 border-2 border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black hover:bg-green-color hover:text-white font-bold items-center rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{  $t('go_to_my_fridge') }}
        </button>
      </NuxtLink>

      <NuxtLink v-else-if="loggedIn" @click="toggleSelect()" class="sm:mt-5 sm:pr-4">
        <button class="w-56 h-14 border-2 border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black hover:bg-green-color hover:text-white font-bold items-center rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{  $t('go_to_my_fridge') }}
        </button>
      </NuxtLink>


      <NuxtLink v-else :to="localePath('/login')" class="sm:mt-5 sm:pr-4">
        <button class="w-56 h-14 sm:w-50 border-2 border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black hover:bg-green-color hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ $t('log_in_here') }}
        </button>
      </NuxtLink>
      <NuxtLink :to="localePath('/about-us')" class="sm:mt-5 sm:pl-4">
        <button class="w-56 h-14 border-2 border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black hover:bg-green-color hover:text-white font-bold items-center rounded transform hover:scale-100 my-2 sm:my-0 sm:h-14 sm:flex sm:justify-center">
          {{ $t('read_more_about_us')}}
        </button>
      </NuxtLink>
    </div>
  </div>
  <div class="wave-container absolute bottom-0 left-0 w-full">
    <Wave class="h-3/5"></Wave>
  </div>

</template>


  <script setup lang="ts">
  import { useUserStore } from "~/store/userStore";
  import { onMounted, computed, ref } from "vue";
  import { getNotifications } from "~/service/httputils/NotificationService";
  import { useNotificationStore } from '~/store/notificationStore';
  import { getRefrigerators } from "~/service/httputils/RefrigeratorService";
  import { useRefrigeratorStore } from "~/store/refrigeratorStore";
  import { Refrigerator } from '~/types/RefrigeratorType';
  import { useRouter } from "vue-router";

  const router = useRouter();


  const userStore = useUserStore();
  const loggedIn = computed(() => userStore.isLoggedIn);

  const notificationStore = useNotificationStore();
  const refrigeratorStore = useRefrigeratorStore();



  const select = ref(false);
  function toggleSelect(){

    console.log(refrigeratorStore.getRefrigerators);
    if (refrigeratorStore.getRefrigerators.length == 0) {
      router.push('/create-fridge');
    } else {
      select.value = !select.value
    }
  }

  function isSelected(){
    try{
      return refrigeratorStore.getSelectedRefrigerator!.id > 0;
    }
    catch(error){
      return false;
    }
  }


  function goToFridge(value : any){
    refrigeratorStore.setSelectedRefrigerator(value);
      router.push('/home');
  }


  async function loadNotifications(){
    if (!userStore.isLoggedIn) return;
    try{
      const response = await getNotifications();
      if(response.status == 200){
        notificationStore.setNotification(response.data);
      }
    }catch(error : any){
      console.log(error);
    }
  }

  async function loadRefrigerators(){
    if (!userStore.isLoggedIn) {
      return;
    }
      try{
        const response = await getRefrigerators();
        refrigeratorStore.setRefrigerators(response.data);
        if(refrigeratorStore.getSelectedRefrigerator){
          if(userStore.favoriteRefrigeratorId !== null){
          const favoriteRefrigerator = refrigeratorStore.getRefrigeratorById(userStore.favoriteRefrigeratorId);
          if(favoriteRefrigerator !== undefined){
            refrigeratorStore.setSelectedRefrigerator(favoriteRefrigerator);
          }
          else{
            console.log("Could not find favorite refrigerator in store");
          }
        }
        }
    }
    catch(error){
      console.log(error);
    }
  }


  //todo: diskutabel
  definePageMeta({
    "requiresAuth": true,
    middleware: [
      'auth',
    ],
  })

  async function ensureLoggedIn(){
    if (userStore.isLoggedIn) return;
    console.log("Not logged in");
    try{
      const usedLocale = computed(() => {
        return (locales.value).filter(i => i.code === locale.value)
      })
      router.push(`/${usedLocale.value[0].code}/login`);
    } catch (error : any){
      console.log(error);
    }
  }

  onMounted(() => {
    ensureLoggedIn();
    loadNotifications();
    loadRefrigerators();
  });
</script>



<style scoped>

.wrapper{
  width: 60%;
  margin: 0 auto;
  max-height: calc(100vh - 96px);
}

</style>