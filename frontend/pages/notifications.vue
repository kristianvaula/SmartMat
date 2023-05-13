<template>
    <div class="w-full h-full items-center">
        <div class="w-1/2 md:w-1/4 mx-auto">
          <h1 class="p-5 text-black text-center dark:text-white">
            {{t("Notification")}}
          <div class="divider"></div>
          </h1>
        </div>
        <NotificationsList @delete-notif="(payload) => deleteNotification(payload)" :notifications="notificationStore.getNotifications"/>
    </div>
</template>

<script setup lang="ts">
import { useNotificationStore } from '~/store/notificationStore';
import { getNotifications } from '~/service/httputils/NotificationService';
import { GroceryNotification } from '~/types/GroceryNotificationType';
import { deleteNotifications } from '~/service/httputils/NotificationService'
import { useUserStore } from "~/store/userStore";
import { onMounted } from 'vue';

const notificationStore = useNotificationStore();

const userStore = useUserStore();


const { t } = useI18n();

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


async function deleteNotification(notification : GroceryNotification){
  try{
      const response = await deleteNotifications(notification.id);
      if(response.status == 200){
          notificationStore.deleteNotification(notification);
      }
  }
  catch(error : any){
      console.log("Error occured while deleting notification" + error);
  }
}

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})

onMounted(() => {
  loadNotifications();
});
</script>


<style scoped>
.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}
</style>
