<template>
  <div>
    <div class="mt-14">
      <h1 class="w-3/4 text-center mx-auto flex justify-center text-black dark:text-white text-2xl md:text-4xl ">{{t('log_in_or_create_account')}}</h1>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-2 max-w-4xl mx-auto mt-8">
      <div class="hidden dark:hidden md:block md:pointer-events-none">
        <img src="../assets/icons/smartmat/smartMat.png" alt="logo" class="w-96 h-auto rounded-2xl">
      </div>
      <div class="hidden dark:md:block md:pointer-events-none">
        <img src="../assets/icons/smartmat/smartMat_transparent.png" alt="logo" class="w-96 h-auto">
      </div>
      <div class="mx-auto">
        <LogInForm id="log-in-form"></LogInForm>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import LogInForm from "~/components/Form/LogInForm.vue";
  const { t } = useI18n();
  definePageMeta({
    layout: "custom",
  }); 

</script>

<style scoped>
h1{
  margin-top: 30px;
}
</style>
