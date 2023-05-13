<template>
  <div class="mt-14">
    <h1 class="w-3/4 text-center mx-auto flex justify-center text-black dark:text-white text-2xl sm:text-4xl ">{{t('create_new_account')}}</h1>
  </div>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-2 max-w-4xl mx-auto mt-8">
    <div class="hidden dark:hidden md:block md:pointer-events-none">
      <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="w-96 h-auto">
    </div>
    <div class="hidden dark:md:block md:pointer-events-none">
      <img src="../assets/icons/smartmat/smartMat_transparent.png" alt="logo" class="w-96 h-auto">
    </div>
    <div class="mx-auto">
      <RegisterForm id="log-in-form"></RegisterForm>
    </div>
  </div>
</template>

<script setup>
import RegisterForm from "~/components/Form/RegisterForm.vue";

const { t } = useI18n();

definePageMeta({
  layout: "custom",
});
</script>

<style scoped>
h1{
  margin-top: 30px;
}

template{
  max-height: calc(100vh - 96px);
}
</style>
