<template>
  <div class="" @click="seeRecipeNextWeek">
    <div class="bg-green-color dark:bg-zinc-500 hover:transform hover:scale-105 pb-3 w-11/12 h-56 justify-center relative rounded-xl">
      <div class="h-40 dark:hidden bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(49,196,141,0) 90%, rgba(49,196,141,1) 100%), url(${recipeInfo.url})` }"></div>
      <div class="h-40 hidden dark:block bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(39,39,42,0) 95%, rgba(39,39,42,1) 100%), url(${recipeInfo.url})` }"></div>
      <div class="">
        <div class="flex">
          <p class="my-auto ml-2 text-xl font-bold text-white">{{ recipeInfo.name }}</p>
        </div>
        <div v-if="weeklyMenuStore.$state.currentChosenIndex != null" class="w-48 mx-auto absolute bottom-0 left-6 mb-2">
          <button @click.stop="returnToWeeklyMenu(recipeInfo)" class="cursor-pointer w-full h-auto mx-auto my-1 text-l button-light-color text-black block hover:transform hover:scale-105 rounded-lg">{{ t('add_recipe_to_weekly_menu') }}</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script lang="ts">
import { useWeeklyMenuStore } from '~/store/WeeklyMenuStore';
import { Recipe } from '~/types/RecipeType';

export default {
  setup() {
    const { t } = useI18n();
    const weeklyMenuStore = useWeeklyMenuStore();
    return {
      t, 
      weeklyMenuStore
    }
  },
  props: {
    recipeInfo: {
        type: Object as () => Recipe,
        required: true,
      },
  },

  methods: {
    returnToWeeklyMenu(recipe : Recipe) {
      if(this.weeklyMenuStore.$state.chosenWeek === 1) {
        this.weeklyMenuStore.$state.currentWeek[this.weeklyMenuStore.$state.currentChosenIndex] = recipe;
      } else {
        this.weeklyMenuStore.$state.nextWeek[this.weeklyMenuStore.$state.currentChosenIndex] = recipe;
      }
      this.weeklyMenuStore.setCurrentChosenIndex(null);
      this.$router.push("/weekly-menu");
    },
    seeRecipeNextWeek(): void {
      this.$emit("seeRecipeEvent", this.recipeInfo);
    },
  },
}

</script>

<style scoped>

</style>