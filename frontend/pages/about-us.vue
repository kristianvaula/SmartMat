<template>
  <div class="w-full px-4 pt-16">
    <div class="mx-auto w-full max-w-md rounded-2xl form-light-color dark:form-dark-color border-2 border-[#31C48D]/60 dark:text-white p-2">
      <HeadlessDisclosure v-slot="{ open }">
        <HeadlessDisclosureButton
            class="flex w-full justify-between rounded-lg button-light-color button-green-color dark:button-dark-color px-4 py-2 text-left font-medium text-white hover:bg-emerald-800 dark:hover:bg-gray-800 focus:outline-none focus-visible:ring focus-visible:ring-purple-500 focus-visible:ring-opacity-75"
        >
          <span>{{ t('who_is_smart_mat_for') }}</span>
          <ChevronUpIcon
              :class="open ? 'rotate-180 transform' : ''"
              class="h-5 w-5 text-purple-500"
          />
        </HeadlessDisclosureButton>
        <HeadlessDisclosurePanel class="px-4 pt-4 pb-2 text-sm text-gray-500 dark:text-zinc-300">
          {{ t('who_is_smart_mat_for_response') }}
        </HeadlessDisclosurePanel>
      </HeadlessDisclosure>
      <HeadlessDisclosure as="div" class="mt-2" v-slot="{ open }">
        <HeadlessDisclosureButton
            class="flex w-full justify-between rounded-lg button-light-color button-green-color dark:button-dark-color px-4 py-2 text-left font-medium text-white hover:bg-emerald-800 dark:hover:bg-gray-800 focus:outline-none focus-visible:ring focus-visible:ring-purple-500 focus-visible:ring-opacity-75"
        >
          <span>{{ t('who_is_behind_smart_mat') }}</span>
          <ChevronUpIcon
              :class="open ? 'rotate-180 transform' : ''"
              class="h-5 w-5 text-purple-500"
          />
        </HeadlessDisclosureButton>
        <HeadlessDisclosurePanel class="px-4 pt-4 pb-2 text-sm text-gray-500 dark:text-zinc-300">
          {{ t('who_is_behind_smart_mat_response') }}
        </HeadlessDisclosurePanel>
      </HeadlessDisclosure>
    </div>
  </div>
</template>

<script setup>
const { t } = useI18n();
</script>

<style scoped>

</style>