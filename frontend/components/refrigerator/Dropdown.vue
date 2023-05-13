<template>
  <div class="h-80 w-full py-6 flex flex-col items-center dark:bg-zinc-400 sm:py-6 overflow-hidden">
    <div class="w-full flex justify-center">
      <input
          data-test="input"
          autocomplete="off"
          @click="open = !open"
          type="search"
          id="input"
          v-model="selectedGroceryName"
          :placeholder="$t('search_here')"
          class="py-3 w-11/12 sm:w-2/3 rounded-xl shadow font-thin focus:outline-none focus:shadow-lg focus:shadow-slate-200 duration-100 shadow-gray-300"
      />
    </div>
    <div id="listwrapper" class="relative mt-2 overflow-hidden border border-black rounded-lg bg-gray-200 w-11/12 sm:w-2/3">
      <ul data-test="list" v-if="open" @click="open = !open" class="h-fit">
        <div v-for="grocery in filteredGroceries" :key="grocery.id">
          <li
            @click="setGrocery(grocery)"
            :class="{ 'bg-gray-400': grocery.id === selectedGrocery?.id && grocery.name === selectedGroceryName }"
            class="w-full text-gray-700 bg-gray-200 p-2 mt-2 rounded-sm hover:bg-gray-300"
          >
            {{ grocery.description }}
          </li>
        </div>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { getGroceriesDTOs } from "~/service/httputils/GroceryService";
import { Grocery } from "~/types/GroceryType";
import { ref, computed, onMounted } from "vue";


let open = ref(false);

let selectedGroceryName = ref("");
let selectedGrocery = ref<Grocery | null>(null);

const emit = defineEmits(["update-value"]);

let groceries: Grocery[] = [];

async function loadGroceries() {
  try {
    const response = await getGroceriesDTOs();
    groceries = response.data;
  } catch (error) {
    console.log("Could not load groceries", error);
  }
}

function setGrocery(grocery: Grocery) {
  if (selectedGrocery.value === grocery) {
    selectedGrocery.value = null;
    selectedGroceryName.value = "";
    emit("update-value", null);
  } else {
    emit("update-value", grocery);
    selectedGroceryName = ref(grocery.name);
    selectedGrocery.value = grocery;
  }
}

const filteredGroceries = computed(() => {
  if (selectedGrocery.value !== null) {
    return [
      selectedGrocery.value,
      ...groceries.filter((grocery) => grocery.id !== selectedGrocery.value!.id && grocery.name.toLowerCase().includes(selectedGroceryName.value.toLowerCase())),
    ];
  } else {
    return groceries.filter((grocery) => grocery.name.toLowerCase().includes(selectedGroceryName.value.toLowerCase()));
  }
});

onMounted(() => {
  loadGroceries();
});
</script>

<style scoped>
#listwrapper {
  overflow-y: auto;
  height: auto;
  scrollbar-width: none;
  overflow-x: hidden;
}

#listwrapper::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
#listwrapper {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
</style>
