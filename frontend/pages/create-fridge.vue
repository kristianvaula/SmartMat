<template>
  <div class="create-fridge-wrapper">
    <div class=" mt-14">
      <h1 class="flex justify-center text-black dark:text-white text-4xl ">{{ t('new_fridge') }}</h1>
    </div>
    <div class="form-wrapper mx-auto">
      <CreateFridgeForm id="fridge"></CreateFridgeForm>
    </div>
  </div>
</template>

<script>
import CreateFridgeForm from "~/components/Form/CreateFridgeForm.vue";


export default {
  name: "create-fridge.vue",
  components: {CreateFridgeForm},
  setup() {
    const { t } = useI18n();

    return { t };
  },
}

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})
</script>

<style scoped>
.wrapper {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  align-items: center;
  justify-content: center;
  max-width: 800px;
  margin: 40px auto 0 auto;
  gap: 2rem;
}

.logo {
  display: flex;
  justify-content: center;
  width: 100%;
}
.image {
  width: 100%;
}
.form-wrapper {
  width: 100%;
  max-width: 400px;
}

#log-in-form {
  display: flex;
  justify-content: center;
  height: 100%;
}

h1{
  margin-top: 30px;
}

template{
  max-height: calc(100vh - 96px);
}
</style>
