<template>
    <div class="mt-7 max-w-sm mx-auto text-center card">
        <p class="mt-7 text-7xl font-bold">{{ error.statusCode }}</p>
        <p class="mt-7 text-6xl">Whops</p>
        <p class="mt-7"> {{ error.message }}</p>

        <GreenButton @click="handleClearError" :label="t('go_to_home')" width="100%" height="50px" class="px-3 mt-5 text-center"/>

    </div>
</template>

<script setup>
    import { useI18n } from 'vue-i18n';
    import GreenButton from "~/components/Button/GreenButton.vue";
    const {t} = useI18n();

    defineProps(['error'])

    const handleClearError = () => {
        clearError({redirect: '/'})
    }
</script>

<style scoped>

</style>