<template>
  <div class="wrapper flex justify-center items-center">
    <div class="w-2/3 mx-auto bg-white mt-10 rounded-md py-5">
      <p class="text-center mt-10 text-xl font-bold"> {{$t('change_password')}}</p>
      <div class="w-2/3 mx-auto flex flex-col items-center mt-3">
        <div class="divider"></div>
        <div class="mb-4">
          <div class="input-container w-full">
            <BaseInput id="newPassword" type="password" class="w-full" :label="$t('new_password')" v-model="newPassword"/>
          </div>
          <div class="input-container w-full">
            <BaseInput id="verifyNewPassword" type="password" class="w-full" :label="$t('verify_new_password')" v-model="verifyNewPassword"/>
          </div>
          <div class="input-container w-full">
            <BaseInput id="oldPassword" type="password" class="w-full" :label="$t('old_password')" v-model="oldPassword"/>
          </div>
          <div class="flex flex-col" v-if="showPasswordMismatchMessage">
            <div class="mb-4">
              <div class="mx-auto w-full p-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400" role="alert">
                <span class="font-medium">{{ $t('warning') }}</span> {{ $t('password_mismatch') }}
              </div>
            </div>
          </div>
        </div>
        <div class="flex mb-4">
          <GrayButton id="submit" @click="submit" :label="$t('update_password')" width="100%" height="50px" class="mr-3" :disabled="!isFormValid"/>
          <NuxtLink to="/account-details" class="w-full rounded bg-gray-200 text-gray-800 text-center py-2 px-4">
            {{ $t('go_back') }}
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseInput from "~/components/Form/BaseInput.vue";
import GrayButton from '~/components/Button/GrayButton.vue'
import { ref, computed } from 'vue'

const oldPassword = ref('')
const verifyNewPassword = ref('')
const newPassword = ref('')
const showPasswordMismatchMessage = ref(false)

const submit = () => {
  const passwordsMatch = newPassword.value === verifyNewPassword.value
  showPasswordMismatchMessage.value = !passwordsMatch
  if (passwordsMatch) {
    oldPassword.value = ''
    verifyNewPassword.value = ''
    newPassword.value = ''
  }
}

const isFormValid = computed(() => {
  return oldPassword.value && verifyNewPassword.value && newPassword.value
})

</script>

<style scoped>
.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}
</style>