<template>
  <div class="wrapper">
    <Head>
      <Title>SmartMat</Title>
    </Head>
    <NavBar/>
    <div class="page-container bg-light-color dark:bg-dark-color">
      <slot />
    </div>

  </div>
</template>

<style scoped>
.page-container {
  height: calc(100vh - 96px);
  display: block;
  max-height: calc(100vh - 96px);
}

.wrapper{
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-height: 100vh;
}
</style>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { useUserStore } from "~/store/userStore";

const userStore = useUserStore();

onMounted(() => {
  if (sessionStorage.getItem("user") != null){
    userStore.checkAuthStatus();
  }
  document.body.classList.add('bg-light-color');
  document.body.classList.add('dark:bg-dark-color');
});

onUnmounted(() => {
  document.body.classList.remove('bg-light-color');
  document.body.classList.remove('dark:bg-dark-color');
});

</script>