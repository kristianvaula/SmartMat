<template>
  <div class="Recipe-card-wrapper bg-green-color dark:bg-zinc-500">
    <div class="lock-container absolute top-2 right-2 bg-green-color rounded-3xl p-1 z-20 dark:button-dark-color ">
        <img
          v-if="lockedBoolean"
          class="lock-icon"
          src="../../assets/icons/Locked.webp"
          @click="unlockRecipe"
        />
        <img
          v-else-if="!lockedBoolean"
          class="lock-icon"
          src="../../assets/icons/unlock.png"
          @click="lockRecipe"
        />
      </div>
    <div class="Recipe-card">
      <div class="">
      <div class="h-40 dark:hidden bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(49,196,141,0) 90%, rgba(49,196,141,1) 100%), url(${recepeInfo.url})` }"></div>
      <div class="h-40 hidden dark:block bg-cover bg-center rounded-t-lg" :style="{backgroundImage: `linear-gradient(to bottom, rgba(39,39,42,0) 95%, rgba(39,39,42,1) 100%), url(${recepeInfo.url})` }"></div>
      </div>
      <div style="height:50px;" class="flex-1 items-center">
        <h3 class="recepe-title m-2">{{ recepeInfo.name }}</h3>
      </div>
      <div class="recipe-info mt-1">
        <div class="recipe-choices flex flex-col space-y-2">
          <button @click="handleOptionChange('option2')" class="border-2 bg-light-color border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black items-center px-4 rounded transform hover:scale-100 py-1 px-2 rounded text-sm w-full">
            {{ $t("see_recipe") }}
          </button>
          <button @click="handleOptionChange('option3')" class="border-2 bg-light-color border-[#31C48D]/60 dark:button-dark-color dark:text-white text-black items-center px-4 rounded transform hover:scale-100 py-1 px-2 rounded text-sm w-full">
            {{ $t("remove") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { Recipe } from "@/types/RecipeType";

  export default {
    data() {
      return {
        selectedOption: "",
      };
    },

    setup() {
    },

    props: {
      recepeInfo: {
        type: Object as () => Recipe,
        required: true,
      },

      lockedBoolean: {
        type: Boolean,
        required: true,
      },
    },
    methods: {
      lockRecipe() {
        this.$emit("lockedEvent");
      },
      unlockRecipe() {
        this.$emit("unlockedEvent");
      },
      handleOptionChange(option : string) {
        switch (option) {
          case "option2":
            this.$emit("seeRecipeEvent")
            break;
          case "option3":
            this.$emit("removeEvent");
            break;
        }
      },
      returnEvent() {
        this.selectedOption = "";
      },
    }
  };
</script>

<style>
 .Recipe-card-wrapper {
  position: relative;
  width: 200px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 1;
  border-radius: 14px;
}

.Recipe-card-wrapper:hover {
    transform: scale(1.05) translate(0);
    transition: 0.3s ease;
    background-size: 1%;
    border-radius: 14px;
    cursor: pointer;
  }

.lock-icon {
  width: 25px;
  cursor: pointer;
  margin: 2px;
}

.image-wrapper {
  overflow: hidden;
  border-radius: 8px;
  min-width: 170px;
  max-width: 170px;
  min-height: 130px;
  max-height: 130px;
  background-image: linear-gradient(to bottom, rgba(49, 196, 141, 0) 90%, rgba(49, 196, 141, 1) 100%);
}

.image-wrapper img {
  min-height: inherit;
  object-fit: cover;

}

.recipe-info {
  padding: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}


.recepe-title {
  font-size: 1.25em;
  margin-bottom: 15px;
  display: flex;
  justify-content: left;
  color: white;
  overflow:hidden; 
  font-size:large;  
}

.ingredients {
  padding: 10px;
  height: 150px;
}

.ingredients ul {
  margin: 0;
  padding-left: 20px;
}

.ingredients li {
  list-style-type: disc;
  margin-bottom: 5px;
}

.select {
  width: 20px;
}

.back-button {
  display: flex;
  justify-content: end;
}

</style>
