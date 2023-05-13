<template>
    <div class="unknown-recipe dark:bg-zinc-500" @click="addRecipe">
      <div class="add-recepe">
        <div class="centered-content">
          <img src="../../assets/icons/add.png" alt="icon" />
          <h3 class="text-black dark:text-white">{{$t("add_recipe_to_day")}}</h3>
        </div>
      </div>
    </div>
  </template>

<script lang="ts">
  export default {
    setup() {
    },
    methods: {

        addRecipe() {
            this.$emit("addRecipeEvent")
        }
    }

  }

    </script>
  
  <style scoped>
  .unknown-recipe {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 160px;
    height: 260px;
    border: 3px solid black;
    border-radius: 14px;
  }

  .unknown-recipe:hover {
    transform: scale(1.05) translate(0);
    transition:  0.3s ease;
    background-size: 1%;
    border-radius: 14px;
    box-shadow:  9px 9px 18px #e6e6e6;
    cursor: pointer;
  }
  .centered-content {
    display: flex;
    align-items: center;
    flex-direction: column;
  }
  </style>
  