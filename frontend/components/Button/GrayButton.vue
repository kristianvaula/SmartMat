<template>
  <button class="text-white rounded" :style="{ width: width, height: height }">
    {{ label }}
  </button>
</template>

<script setup>
const { label, width = '', height = '' } = defineProps({
  label: String,
  width: String,
  height: String
});
</script>

<style scoped>
button {
  border: none;
  outline: none;
  cursor: pointer;
  background-color: #5c5850;
}

button:hover{
  background-color: #7e796f;
}
</style>
