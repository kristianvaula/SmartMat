<template>
    <div class="hidden md:flex flex-wrap w-full justify-center align-middle  rounded-xl p-5">
        <div v-if="notifications.length > 0 && notifications.length < 5" class="grid grid-cols-1">
            <NotificationsElement v-for="notification in props.notifications" :key="notification.id" @delete-notif = "(payload) => emit('delete-notif', payload)" :notification="notification"/>
        </div>
        <div v-else-if="notifications.length > 3 && notifications.length < 9" class="grid grid-cols-2">
            <NotificationsElement v-for="notification in props.notifications" :key="notification.id" @delete-notif = "(payload) => emit('delete-notif', payload)" :notification="notification"/>
        </div>
        <div v-else-if="notifications.length > 8" class="grid grid-cols-3">
            <NotificationsElement v-for="notification in props.notifications" :key="notification.id" @delete-notif = "(payload) => emit('delete-notif', payload)" :notification="notification"/>
        </div>
        <div v-else class="text-center dark:text-white">{{ t('no_notifications') }}!</div>
    </div>
    <div class="block md:hidden flex flex-wrap w-full justify-center align-middle  rounded-xl p-5">
      <div v-if="notifications.length > 0" class="grid grid-cols-1">
        <NotificationsElement v-for="notification in props.notifications" :key="notification.id" @delete-notif = "(payload) => emit('delete-notif', payload)" :notification="notification"/>
      </div>
      <div v-else class="text-center dark:text-white">{{ t('no_notifications') }}!</div>
    </div>
</template>

<script setup lang="ts">
import type { GroceryNotification } from '~/types/GroceryNotificationType';
const { t } = useI18n();
const props = defineProps({
    notifications:{
        type : Array as () => GroceryNotification[],
        required:true,
    },
});

const emit = defineEmits(['delete-notif'])

</script>