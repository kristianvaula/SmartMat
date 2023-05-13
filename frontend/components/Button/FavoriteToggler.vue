<template>
  <div 
    @click="favoriteHandler(false)" 
    v-if="isFavorite"
    class=" inline-flex items-center justify-left w-full"> 
    <div class="text-black dark:text-white form-light-color dark:form-dark-color border-2 border-[#31C48D]/60 rounded-2xl inline-flex items-center">
      <img 
        :class="[large ? 'h-16':'h-12']"
        class=" hover:transform hover:cursor-pointer " 
        src="../../assets/icons/heartfilled.png"
        alt="">
      <h2 class="px-2" v-if="text">{{ $t("remove_favorite") }}</h2>
    </div>
  </div>
  <div 
    v-else 
    @click="favoriteHandler(true)" 
    class=" inline-flex items-center justify-left w-full"> 
    <div class="text-black dark:text-white form-light-color dark:form-dark-color border-2 border-[#31C48D]/60 rounded-2xl inline-flex items-center">
      <img 
      :class="[large ? 'h-16':'h-12']"
      class=" hover:transform hover:cursor-pointer hover:scale-110" 
      src="../../assets/icons/heart.png"
      alt="">
      <h2 class="px-2" v-if="text">{{ $t("add_favorite") }}</h2>
    </div>
    
  </div>
</template>

<script lang="ts">
export default {
  props: {
    isFavorite : {
      type : Boolean, 
      required : true,
    },
    text : {
      type : Boolean, 
      required: false, 
      default: true
    },
    large: {
      type : Boolean, 
      required: false,
      default: false
    }
  },
  methods : {
    favoriteHandler(value : boolean) {
      this.$emit("favoriteEvent", value); 
    }
  }
}
</script>

<style lang="scss" scoped>

</style>