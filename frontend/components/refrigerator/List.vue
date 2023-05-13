<template>
  <div class="h-fit overflow-y-scroll pb-10">
    <ul v-if="groceries.length > 0" class="space-y-1 list-none list-inside px-3">
      <li v-for="(category, index) in categorizedGroups" :key="index" :class="index % 2 === 0 ? 'bg-white' : 'bg-gray-200'" class="rounded-lg">
        <div @click="toggleCategory(index)" class="relative pl-1 py-2 border-2 border-gray-300 rounded-md">
          <div class="absolute inset-y-0 right-5 pl-2 flex items-center">
            <img src="../../assets/icons/expandMore.png" alt="Expand" class="h-4 w-4" />
          </div>
          <div class="pr-6 md:pl-1">
            {{ category.name }} ({{ categorySum (category) }})
              <div v-if="getClosestExpiryDateForCategory(category) !== null && !isCategoryOpen(index)" class="px-5" :class="{'text-red-500' : isNearExpiry(getClosestExpiryDateForCategory(category)!) }">
            {{ getClosestExpiryDateForCategory(category)!.toLocaleDateString() }}
          </div>
          <i :class="['fa', isCategoryOpen(index) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
            <i :class="['fa', isCategoryOpen(index) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
          </div>
        </div>
        <ul v-if="isCategoryOpen(index)" class="py-2 px-3 w-full list-none list-inside rounded-xl">
          <li v-for="(group, index2) in Array.from(category.groups.values())" :key="index2">
            <div @click="toggleCategoryGroup(index, index2)" class="flex">
              - {{ group.name }} ({{ group.groceries.length }}) 
              <div v-if="getClosestExpiryDateForGroup(group) !== null" class="px-5" :class="{'text-red-500' : isNearExpiry(getClosestExpiryDateForGroup(group)!) }">
                {{ getClosestExpiryDateForGroup(group)!.toLocaleDateString() }}
              </div>
              <i :class="['fa', isCategoryGroupOpen(index,index2) ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
            </div>
            <ul v-if="isCategoryGroupOpen(index,index2)" class="space-y-1 list-none list-inside">
              <li>
                <RefrigeratorElement @emit-date="(payload : GroceryEntity) => emit('emit-date', payload)" @element-height="(payload) => emit('popup-height',payload)" @selected-grocery="(payload) => emit('selected-grocery',payload)" v-for="grocery in group.groceries" :grocery="grocery" :key=grocery.id />
              </li>
            </ul>
          </li>
        </ul>
      </li>
    </ul>
    <div v-else class="ml-5">
      {{ t("no_items_in_refrigerator") }}
    </div>
  </div>
</template>



<script setup lang="ts">
import type { GroceryEntity } from '~/types/GroceryEntityType';
const { t } = useI18n();

    const emit = defineEmits(['popup-height', 'group-closed', 'emit-date', 'selected-grocery']);


    const props = defineProps({
      groceries:{
        type: Array as () => GroceryEntity[],
        required:true
    },
    });

    interface Group {
      name: string;
      groceries: GroceryEntity[];
    }
    interface Category {
      name: string,
      groups: Map<string, Group>
    }

    function getClosestExpiryDate(groceries: GroceryEntity[]): Date | null {
      if (groceries.length === 0) {
        return null;
      }
      let closestDate: Date | null = null;
      for (const grocery of groceries) {
        const expiryDate = grocery.physicalExpireDate;
        if (!closestDate || expiryDate < closestDate) {
          closestDate = expiryDate;
        }
      }
      return closestDate;
    }

  function isNearExpiry(date : Date): boolean {
    const dateDifferenceInMilliseconds = Date.parse(date.toString()) - Date.now();
    const daysUntilExpiry = Math.ceil(dateDifferenceInMilliseconds / (1000 * 60 * 60 * 24));
    return daysUntilExpiry <= 3;
  }
function getClosestExpiryDateForGroup(group: Group): Date | null {
  const closestDate = getClosestExpiryDate(group.groceries);
  return closestDate;
}

function getClosestExpiryDateForCategory(category: Category): Date | null {
  let closestDate: Date | null = null;
  for (const group of category.groups.values()) {
    const groupClosestDate = getClosestExpiryDate(group.groceries);
    if (groupClosestDate && (!closestDate || groupClosestDate < closestDate)) {
      closestDate = groupClosestDate;
    }
  }
  return closestDate;
}


    function categorySum(category : Category){
      let sum = 0;
      category.groups.forEach((group : Group) => {
         sum += group.groceries.length
      })
      return sum;
    }

   const openCategories = ref(new Map<number, {groups : Map<number, boolean>}>());

   const isCategoryOpen = (index : number) => openCategories.value.has(index);

   const toggleCategory = (index : number) => {
      if(isCategoryOpen(index)){
         openCategories.value.delete(index);
      }
      else{
         openCategories.value.set(index, {groups : new Map<number, boolean>()});
      }
      emit('group-closed');
   };

   const isCategoryGroupOpen = (catIndex : number, groupIndex : number) => {
      if(!isCategoryOpen(catIndex)){
         return false;
      }
      return openCategories.value.get(catIndex)?.groups.get(groupIndex);
   }

   const toggleCategoryGroup = (catIndex : number, groupIndex : number) => {
      const groups = openCategories.value.get(catIndex)?.groups!;
      if (groups.get(groupIndex)) {
    groups.set(groupIndex, false);
  } else {
    groups.set(groupIndex, true);
  }
  emit('group-closed');
}

const categorizedGroups = computed<Category[]>(() => {
  const categoryMap = new Map<number, Category>();
  for (const grocery of props.groceries) {
    const category = grocery.grocery.subCategory.category;
    if (!categoryMap.has(category.id)) {
      categoryMap.set(category.id, { name: category.name, groups: new Map<string, Group>() });
    }
    const group = grocery.grocery;
    const groupMap = categoryMap.get(category.id)?.groups!;
    if (!groupMap.has(group.name)) {
      groupMap.set(group.name, { name: group.name, groceries: [] });
    }
    groupMap.get(group.name)?.groceries.push(grocery);
  }
  const sortedCategories = Array.from(categoryMap.values()).sort((a, b) => a.name.localeCompare(b.name));
  sortedCategories.forEach((category) => {
    category.groups = new Map([...category.groups.entries()].sort((a, b) => a[1].name.localeCompare(b[1].name)));
  });
  return sortedCategories;
});
</script>