<template>
<div id="dropdown" class="z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 dark:bg-gray-700">
    <ul class="py-2 text text-gray-700 dark:text-gray-200">
      <li @click="emit('clicked', refrig)" v-for="refrig in props.refrigerators" class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
        {{ refrig.name }}
      </li>
    </ul>
</div>
</template>

<script setup lang="ts">
import { Refrigerator } from '~/types/RefrigeratorType';

const props = defineProps({
    refrigerators:{
        type : Array as () => Refrigerator[],
        required : true
    }
})

const emit = defineEmits(['clicked'])
</script>