<template>
  <div class="flex justify-center mt-10 text-black dark:text-white">
    <h1 class="text-6xl text-black dark:text-white">{{t('recipes')}}</h1>
  </div>
  <div>
    <RecipeDisplay :recipe="recipe" @close-display-event="closeDisplayEvent"/>
    <RecipeCatalog @see-recipe-event="handleRecipeEvent"/>
  </div>
</template>

<script lang="ts">
import { Recipe } from "~/types/RecipeType";
export default {
  setup() {
    const { t } = useI18n();
    return {t}
  },
  data() {
      return {
        overlay : true,
        recipe : null as Recipe | null
      };
  },
  methods: {
      handleRecipeEvent(recipe : Recipe) {
        console.log(recipe)
        this.recipe = recipe
      },
      closeDisplayEvent(){
        this.recipe = null;
      }
    }
}




definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style scoped>

</style>