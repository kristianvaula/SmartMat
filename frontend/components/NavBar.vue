<template>
<header class="navbar-light-color dark:navbar-dark-color dark:border-black" style="min-width:360px; max-height: 96px;">
  <nav class="mx-auto flex max-w-7xl items-center p-6 lg:px-8" aria-label="Global">
    <NuxtLink sm:hidden to="/" class="-m-1.5 p-1.5 mr-4 hidden pointer-events-none sm:block sm:pointer-events-auto">
      <span class="sr-only">SmartMat</span>
      <img class="hidden dark:block h-12 w-auto hover:transform hover:scale-125 pointer-events-none" src="../assets/icons/smartmat/leafTransparent.png" alt="">
      <img class="dark:hidden h-12 w-auto hover:transform hover:scale-125 pointer-events-none" src="../assets/icons/smartmat/leaf.png" alt="">
    </NuxtLink>
    <div class="hidden lg:flex flex-1 space-x-5 items-center lg:justify-start">
      <NuxtLink v-if="isSelected()" to="/home" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('refrigerator')}}</NuxtLink>
      <NuxtLink v-else to="/create-fridge" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('refrigerator')}}</NuxtLink>
      <NuxtLink v-if="isSelected()" to="/weekly-menu" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('weekly_menu')}}</NuxtLink>
      <NuxtLink v-else to="/create-fridge" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('weekly_menu')}}</NuxtLink>
      <NuxtLink v-if="isSelected()" to="/recipe-list" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('recipes')}}</NuxtLink>
      <NuxtLink v-else to="/create-fridge" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('recipes')}}</NuxtLink>
      <NuxtLink v-if="isSelected()" to="/stats" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('statistics')}}</NuxtLink>
      <NuxtLink v-else to="/create-fridge" class="text-md font-semibold leading-6 text-gray-900 dark:text-white hover:transform hover:-translate-y-0.5 hover:scale-125 hover:text-white">{{t('statistics')}}</NuxtLink>
    </div>
    <div class="flex flex-1 space-x-2 items-center justify-end">
      <FridgeSelector v-if="loggedIn"/>
      <NuxtLink v-if="loggedInStatus" href="/notifications" title="Varslinger" class="lg:hidden -ml-4 mr-8 p-1.5 text-sm font-semibold leading-6 text-gray-900">
        <span class="sr-only">Varslinger</span>
        <img v-if = "notificationStore.getNotifications.length > 0" class="h-8 w-auto" src="../assets/icons/bell-not.png" alt="">
        <img v-else class="h-8 w-auto hover:transform hover:scale-125" src="../assets/icons/bell.png" alt="">
      </NuxtLink>
      <div class=" w-8"></div>
      <div class="flex lg:hidden">
        <button type="button" class="-m-2.5 inline-flex items-center justify-center rounded-md min-w-fit p-2.5 text-gray-700" @click="mobileMenuOpen = true">
          <span class="sr-only">Open main menu</span>
          <img class="h-10 w-auto min-w-fit" aria-hidden="true" src="../assets/icons/menu.png" alt="" />
        </button>
      </div>
      <div class="hidden lg:inline-flex items-center">
        <NuxtLink v-if="loggedInStatus" href="/notifications" title="Varslinger" class="-ml-4 mr-8 p-1.5 text-sm font-semibold leading-6 text-gray-900">
          <span class="sr-only">Varslinger</span>
          <img v-if = "notificationStore.getNotifications.length > 0" class="h-8 w-auto" src="../assets/icons/bell-not.png" alt="">
          <img v-else class="h-8 w-auto hover:transform hover:scale-125" src="../assets/icons/bell.png" alt="">
        </NuxtLink>
        <HeadlessMenu v-if="loggedInStatus" as="div" class="relative inline-block text-left">
          <div>
            <HeadlessMenuButton title="Profil og innstillinger" class="inline-flex w-full justify-center gap-x-1.5 rounded-md bg-light-color ring-1 ring-gray-300 dark:ring-zinc-600 dark:bg-zinc-600 px-3 py-2 text-sm font-semibold text-gray-900" >
              <img class="h-8 w-auto hover:transform hover:scale-125" src="../assets/icons/profile.png" alt="">
            </HeadlessMenuButton>
          </div>
 
          <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
            <HeadlessMenuItems class="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white dark:button-dark-color shadow-md ring-1 ring-black ring-opacity-5 focus:outline-none">
              <div class="py-1">
                <HeadlessMenuItem v-if="loggedIn" v-slot="{ active }">
                  <NuxtLink to="/my-profile" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900 dark:text-white' : 'text-gray-700  dark:text-white', 'block px-4 py-2 text-sm']">{{t('edit_profile')}}</NuxtLink>
                </HeadlessMenuItem>
                <HeadlessMenuItem v-slot="{ active }">
                  <NuxtLink to="/system-settings" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900 dark:text-white' : 'text-gray-700  dark:text-white', 'block px-4 py-2 text-sm']">{{t('system_settings')}}</NuxtLink>
                </HeadlessMenuItem>
                <li class="border-t border-gray-200 max-h-10 list-none"></li>
                <HeadlessMenuItem v-if="loggedIn" v-slot="{ active }">
                  <button @click="handleLogOut()" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900' : 'text-gray-700  dark:text-white', 'block w-full px-4 py-2 text-left text-sm']">{{t('log_out')}}</button>
                </HeadlessMenuItem>
                <HeadlessMenuItem v-else v-slot="{ active }">
                  <NuxtLink to="/login" :class="[active ? 'bg-gray-100 dark:bg-zinc-700 text-gray-900 dark:text-white' : 'text-gray-700  dark:text-white', 'block px-4 py-2 text-sm']">{{t('log_in')}}</NuxtLink>
                </HeadlessMenuItem>
              </div>
            </HeadlessMenuItems>
          </transition>
        </HeadlessMenu>
      </div>
    </div>
  </nav>
  <HeadlessDialog as="div" class="lg:hidden" @close="mobileMenuOpen = false" :open="mobileMenuOpen">
      <div class="fixed inset-0 z-10" />
      <HeadlessDialogPanel class="fixed inset-y-0 right-0 z-10 w-full overflow-y-auto bg-white dark:bg-zinc-600 px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-gray-900/10">
        <div class="flex items-center justify-between">
          <a href="#" class="-m-1.5 p-1.5">
            <span class="sr-only">SmartMat</span>
            <img class=" h-12 w-auto" src="../assets/icons/smartmat/leafTransparent.png" alt="">
          </a>
          <button type="button" class="-m-2.5 rounded-md p-2.5 text-gray-700" @click="mobileMenuOpen = false">
            <span class="sr-only">Close menu</span>
            <img class="h-6 w-6" alt="close" src="../assets/icons/close.png" aria-hidden="true" />
          </button>
        </div>
        <div class="mt-6 flow-root">
          <div class="-my-6 divide-y divide-gray-700/10 dark:divide-black">
            <div class="space-y-2 py-6">
              <NuxtLink to="/" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('home')}}</NuxtLink>
              <NuxtLink v-if="isSelected()" to="/weekly-menu" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('weekly_menu')}}</NuxtLink>
              <NuxtLink v-else to="/create-fridge" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('weekly_menu')}}</NuxtLink>
              <NuxtLink v-if="isSelected()" to="/recipe-list" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('recipes')}}</NuxtLink>
              <NuxtLink v-else to="/create-fridge" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('recipes')}}</NuxtLink>
              <NuxtLink v-if="isSelected()" to="/stats" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50 dark:text-white dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('statistics')}}</NuxtLink>
              <NuxtLink v-else to="/create-fridge" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50 dark:text-white dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('statistics')}}</NuxtLink>
            </div>
            <div class="py-6">
              <NuxtLink v-if="loggedIn" to="/my-profile" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('edit_profile')}}</NuxtLink>
              <NuxtLink v-else to="/login" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('log_in')}}</NuxtLink>
              <NuxtLink to="/system-settings" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400" @click="closeMobileMenu">{{t('system_settings')}}</NuxtLink>
              <button v-if="loggedIn" @click="handleLogOut()" class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 dark:text-white hover:bg-gray-50 dark:hover:bg-zinc-400">{{t('log_out')}}</button>
            </div>
          </div>
        </div>
      </HeadlessDialogPanel>
    </HeadlessDialog>
  </header>
</template>
<script lang="ts">
import {computed, defineComponent} from 'vue'
import {useUserStore} from "~/store/userStore";
import { useNotificationStore } from "~/store/notificationStore";
import { useRefrigeratorStore } from "~/store/refrigeratorStore";
import {postLogOut} from "~/service/httputils/authentication/AuthenticationService";

export default defineComponent({

  setup() {
    const userStore = useUserStore();
    const notificationStore = useNotificationStore();
    const refrigeratorStore = useRefrigeratorStore();
    const {locale, locales, t} = useI18n()
    const loggedIn = computed(() => userStore.isLoggedIn);
    return {
      isLoggedIn: userStore.isLoggedIn,
      userStore,
      locale,
      locales,
      notificationStore,
      t,
      loggedIn,
      refrigeratorStore
    }
  },
  data() {
    return {
      selected: -1,
      loggedInStatus: true,
      mobileMenuOpen: false
    }
  },
  methods: {
    async handleLogOut() {
        try{
          const response = await postLogOut();
          const userStore = useUserStore();
          const router = useRouter();
          if (response.status === 200){
            userStore.logOut();
            router.push('/login');
          }
        } catch (error) {
          console.error(error);
        }
      },
      isSelected(){
    try{
      return this.refrigeratorStore.getRefrigerators.length > 0;
    }
    catch(error){
      return false;
    }
  },
    closeMobileMenu() {
      this.mobileMenuOpen = false;
    }
  },
  mounted() {
    if(!this.loggedIn){
      this.$router.push("/login");
    }
  }

})


</script>