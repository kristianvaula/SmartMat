<template>
  <div class="weekly-menu">
    <RecipeDisplay :recipe="recipe" @close-display-event="closeDisplayEvent"/>
    <WeeklyMenuRecipePool @see-recipe-event="handleRecipeEvent"/>
  </div>
</template>

<script lang="ts">
import type { Recipe } from '~/types/RecipeType';

export default {
    data() {
        return {
          overlay : true,
          recipe : null as Recipe | null
        };
    },
    methods: {
      handleRecipeEvent(recipe : Recipe) {
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