<template>
    <div class = "flex w-full">
        <RefrigeratorNew v-if="toggleCreate" @toggle="(payload) => onToggleCreate(payload)" />
        <RefrigeratorDisplay v-else @emit-date="(payload) => updateGrocery(payload)" @toggle-create="(payload) => onToggleCreate(payload)" :refrigerator="refrigeratorStore.getSelectedRefrigerator" class="font-mono" @group-closed="togglePos(false)" :groceries="groceries.groceries" @popup-height="(payload) => setPos(payload)" />
            <div>
            <Transition>
                <RefrigeratorEditGrocery @delete-grocery="reload()" :pos="position" v-if="toggle" @toggle-options="togglePos(false)"/>
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { getGroceriesByFridge } from '~/service/httputils/GroceryService';
import { Refrigerator } from '~/types/RefrigeratorType';
import { GroceryEntity } from '~/types/GroceryEntityType';
import { deleteGrocery } from '~/service/httputils/GroceryService';
import { getNotifications } from '~/service/httputils/NotificationService';
import { useNotificationStore } from '~/store/notificationStore';

const refrigeratorStore = useRefrigeratorStore();
const notificationStore = useNotificationStore();


const position = ref(0);

const toggle = ref(false);

async function onToggleCreate(payload : boolean){
    toggleCreate.value = payload;
    loadGroceries();
}

const toggleCreate = ref(false);

function togglePos(inp : boolean){
    toggle.value = inp;
}

function setPos(payload: number) {
    if(position.value === payload){
        togglePos(!toggle.value);
    }
  position.value = payload;
}

const groceries = reactive({
  groceries: [] as GroceryEntity[]
});

watch(() => refrigeratorStore.getSelectedRefrigerator, () => {
  loadGroceries();
});

function updateGrocery(grocery : GroceryEntity){
    const index = groceries.groceries.findIndex(search => search.id === grocery.id);
    if(index !== -1){
        groceries.groceries[index] = grocery;
    }
}

async function loadNotifications(){
    try{
      const response = await getNotifications();
      if(response.status == 200){
        notificationStore.setNotification(response.data);
      }
    }catch(error : any){
      console.log(error);
    }
  }

function reload(){
    loadGroceries();
    loadNotifications();
  }

async function loadGroceries(){
    try {
        const fridge : Refrigerator | null = refrigeratorStore.getSelectedRefrigerator;
        if(fridge !== null){
            const response = await getGroceriesByFridge(fridge.id);

            groceries.groceries = response.data;

            groceries.groceries.forEach((grocery) => {
                if(!(grocery.physicalExpireDate instanceof Date)){
                    grocery.physicalExpireDate = new Date(grocery.physicalExpireDate);
                }
            })
        }
        else useRouter().push("/create-fridge")
    }
    catch(error){
        console.log(error);
    }
}
onMounted(() => {
    loadGroceries();
}) 

defineExpose({
  loadGroceries
})

</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.2s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>