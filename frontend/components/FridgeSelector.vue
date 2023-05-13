<template>
  <div class="p-0.5 inline-flex items-center">
    <div style="min-width:150px;">
      <HeadlessListbox as="div" v-model="selected">
        <HeadlessListboxLabel></HeadlessListboxLabel>
        <div class="relative justify-center">
          <HeadlessListboxButton class="bg-light-color dark:bg-dark-color relative w-full h-full cursor-default rounded-md dark:bg-zinc-600 py-1.5 pr-10 text-left text-gray-900 dark:text-white shadow-sm sm:leading-6 hover:cursor-pointer">
            <span class="flex item-center w-32">
              <span v-if="selected === null" class="ml-3 block truncate opacity-70">{{ $t('create_refrigerator') }}</span>
              <span v-else class="ml-3 block truncate">{{ selected.name }}</span>
            </span>
            <span class="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">
              <svg 
                xmlns="http://www.w3.org/2000/svg" 
                fill="none" 
                viewBox="0 0 24 24" 
                stroke-width="1.5" 
                stroke="currentColor" 
                class="h-5 w-5 text-gray-400"
                aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 15L12 18.75 15.75 15m-7.5-6L12 5.25 15.75 9" />
              </svg>
            </span>
          </HeadlessListboxButton>

          <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div class="max-h-64 overflow-y-scroll">
              <HeadlessListboxOptions class=" absolute z-10 mt-1 max-h-64 w-full overflow-auto rounded-b-md bg-light-color dark:bg-zinc-600 py-0 text-base  shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                <HeadlessListboxOption as="template" v-for="fridge in refrigerators" :key="fridge.id" :value="fridge" v-slot="{ active, selected }" @click ="setSelected(fridge)">
                  <li :class="[active ? 'bg-emerald-400 dark:bg-green-500 text-white' : 'text-gray-900  dark:text-white', 'relative cursor-default select-none py-2 pl-1 pr-4','hover:cursor-pointer']">
                    <span @click="goToAdministrateFridgePage(fridge)" :class="[active ? 'text-white' : 'text-indigo-600', 'absolute inset-y-0 right-0 flex items-center pr-1']">
                      <!-- provide this to accessability: {{t('manage')}} -->
                      <img class=" h-5 w-auto" src="../assets/icons/settings.png" alt="">
                    </span>
                    <div class="flex items-center">
                      <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate']">{{ fridge.name }}</span>
                    </div>
                  </li>
                </HeadlessListboxOption>
                <li class="border-t border-gray-300 dark:ring-zinc-600 my-2"></li>
                <HeadlessListboxOption v-slot="{ active, selected }" as="template" :value="{ id: 'link', name: 'Link to page' }" >
                  <NuxtLink to="/create-fridge" :class="[active ? 'bg-emerald-400 dark:bg-green-500 text-white' : 'text-black  dark:text-white', 'block cursor-default select-none py-2 pl-3 pr-2','hover:cursor-pointer']" :aria-selected="selected">
                    <div class="flex items-center ">
                      <img class="hidden sm:block h-5 w-auto" src="../assets/icons/add.png" alt="">
                      <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-1 sm:ml-3 block truncate']">{{ $t('new_fridge') }}</span>
                    </div>
                  </NuxtLink>
                </HeadlessListboxOption>
              </HeadlessListboxOptions>
            </div>
          </transition>
        </div>
      </HeadlessListbox>
    </div>
  </div>
</template>

<script lang="ts">
import { Refrigerator } from "~/types/RefrigeratorType";
import { useRefrigeratorStore } from "~/store/refrigeratorStore";
import { useUserStore } from "~/store/userStore";
import { computed, defineComponent } from 'vue'

export default defineComponent ({
  data() {
    return {
      selected : null as Refrigerator | null
    }
  },
  setup() {
    const {locale, locales, t} = useI18n()
    const userStore = useUserStore();
    const refrigeratorStore = useRefrigeratorStore();
    const refrigerators = computed(() => refrigeratorStore.getRefrigerators);
    
    return { t,locale,locales, userStore, refrigeratorStore, refrigerators}
  },
  watch : {
    refrigerators() {
      this.fetchSelected(); 
    }
  },
  methods: {
    goToCreateFridgePage() {
      this.$router.push('/create-fridge');
    },
    goToAdministrateFridgePage(refrigerator : Refrigerator) {
      this.refrigeratorStore.setSelectedRefrigerator(refrigerator);
      this.$router.push('/administrate-fridge');
    },
    setSelected(fridge : Refrigerator){
      this.selected = fridge; 
      this.refrigeratorStore.setSelectedRefrigerator(fridge);
      const route = this.$route.path
      if(route === '/administrate-fridge'){
        location.reload();
      }

    },
    fetchSelected() {
      const currentSelected = this.refrigeratorStore.getSelectedRefrigerator; 
      if(currentSelected !== null) this.selected = currentSelected; 
      else {
        const favoriteId = this.userStore.getFavoriteRefrigeratorId;
        let favoriteRefrigerator : Refrigerator | undefined;
        if(favoriteId !== null){
          favoriteRefrigerator = this.refrigerators.find(refrigerator => refrigerator.id === favoriteId);
          if(favoriteRefrigerator !== undefined) {
            this.selected = favoriteRefrigerator; 
          }
        }
        if(favoriteRefrigerator === undefined && this.refrigerators !== null && this.refrigerators.length > 0){
          this.selected = this.refrigerators[0]; 
        }
        else if (favoriteRefrigerator === undefined && this.refrigeratorStore.getRefrigerators.length === 0) {
          this.selected = null;
        }
        else if(favoriteRefrigerator === undefined) {
          this.selected = null; 
        }
      }
      if (this.selected != null) {
        this.refrigeratorStore.setSelectedRefrigerator(this.selected);
      }
    }
  },
  mounted() {
    this.fetchSelected();
  }
});
</script>