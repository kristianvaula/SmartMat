<template>
  <div class="lg:flex hidden ">

    <div class="flex-1 order-1 mr-2 mx-1">
      <RefrigeratorWrapper ref="refrigeratorRef"  />
    </div>

    <div class="flex-1 order-2 ml-2 mx-1">
      <ShoppingList
          @prompt-refrigerator="promptRefrigerator()"
          :shopping-list="shoppingList"
          :category-list="categoryList"
          :suggestions-list="suggestionsList"
          :shopping-cart="shoppingCart"
          :refrigeratorId="refrigeratorId"
      />
    </div>

  </div>

  <div class="lg:hidden flex flex-col justify-center">
    <div class="flex justify-center mt-5">
      <button @click="showRefrigerator = !showRefrigerator" class="bg-gray-200 hover:bg-gray-300 rounded-md py-2 px-4">
        {{ showRefrigerator ? t('view_shopping_list') : t('view_refrigerator') }}
      </button>
    </div>
    <div>
      <div v-if="showRefrigerator" class="ml-5">
        <RefrigeratorWrapper ref="refrigeratorRef" />
      </div>
      <div v-else class="w-full">
        <ShoppingList
            @prompt-refrigerator="promptRefrigerator()"
            :shopping-list="shoppingList"
            :category-list="categoryList"
            :suggestions-list="suggestionsList"
            :shopping-cart="shoppingCart"
            :refrigeratorId="refrigeratorId"
        />
      </div>
    </div>
  </div>
</template>


<script>
export default defineComponent({
  props: {
    shoppingList: Array,
    categoryList: Array,
    suggestionsList: Array,
    shoppingCart: Array,
    refrigeratorId: String,
  },
  setup(props) {
    const showRefrigerator = ref(true);
    const { t } = useI18n();

    return {
      showRefrigerator,
      shoppingList: props.shoppingList,
      categoryList: props.categoryList,
      suggestionsList: props.suggestionsList,
      shoppingCart: props.shoppingCart,
      refrigeratorId: props.refrigeratorId,
      t
    };
  },
  methods: {
    promptRefrigerator(){
      this.$refs.refrigeratorRef.loadGroceries();
    }
  }
});
</script>


<style scoped>

</style>

