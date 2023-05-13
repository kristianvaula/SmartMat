<template>
  <div class="wrapper header text-black dark:text-white mt-8 text-center text-4xl font-bold">{{$t('change_password')}}</div>
  <div class="edit-container border-2 border-[#31C48D]/60 w-5/6 h-11/12 md:w-3/5 lg:w-5/12 mt-5 mx-auto form-light-color dark:form-dark-color">
    <div class="user-details">
      <div class="form-group w-full">
        <p class="text-black dark:text-white text-xl font-semibold">{{$t('enter_your_old_password')}}</p>
        <BaseInput :cut-width="'120px'" id="oldPassword" type="password" class="" :label="$t('old_password')" v-model="oldPassword"/>
      </div>
      <div class="form-group">
        <p class="text-black dark:text-white text-xl font-semibold">{{$t('enter_your_new_password')}}</p>
        <BaseInput id="newPassword" type="password" class="" :label="$t('new_password')" v-model="newPassword"/>
      </div>
      <div class="form-group">
        <p class="text-black dark:text-white text-xl font-semibold">{{$t('confirm_your_new_password')}}</p>
        <BaseInput id="verifyNewPassword" type="password" class="" :label="$t('verify_new_password')" v-model="verifyNewPassword"/>
      </div>
      <div class="flex flex-row">
        <NuxtLink :to="localePath('/my-profile')">
          <button class="w-36 h-14 border-2 border-[#31C48D]/60 button-light-color dark:button-dark-color dark:text-white text-black hover:bg-slate-400 hover:text-white font-bold items-center px-4 rounded transform hover:scale-100 sm:flex sm:justify-center mr-4">
            {{$t('go_back')}}
          </button>
        </NuxtLink>
        <button id="submit" @click="updatePassword()" class="w-36 h-14 border-2 border-[#31C48D]/60 button-light-color dark:button-dark-color dark:text-white text-black hover:bg-slate-400 hover:text-white font-bold items-center rounded transform hover:scale-100 sm:flex sm:justify-center">{{$t('change_password')}}</button>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import {getUserData} from "~/service/httputils/authentication/AuthenticationService";
import type { User } from "~/types/UserType";
import { onMounted, ref, computed } from 'vue';
import { useUserStore } from "~/store/userStore";
import axiosInstance from "~/service/AxiosInstance";
import BaseInput from "~/components/Form/BaseInput.vue";
import { AxiosError } from "axios";

const userStore = useUserStore();
const router = useRouter();
const user = ref({} as User);


onMounted(() => {
  loadData();
});

function route(route : string){
  router.push(route);
}

const oldPassword = ref('');
const newPassword = ref('');
const oldPasswordValid = computed(() => oldPassword.value.trim().length > 0);
const newPasswordValid = computed(() => newPassword.value.trim().length > 0);

const verifyNewPassword = ref('');

const passwordsMatch = computed(() => newPassword.value === verifyNewPassword.value);

const formValid = computed(() => oldPasswordValid.value && newPasswordValid.value && passwordsMatch.value);

async function updatePassword() {
  if (!formValid.value) {
    alert("Password fields do not match!")
    return;
  }

  if (oldPassword.value === newPassword.value && newPassword.value === verifyNewPassword.value) {
    alert("New password cannot be the same as the old password!");
    return;
  }

  const passwordData = {
    oldPassword: oldPassword.value,
    newPassword: newPassword.value,
  };

  try {
    const response = await axiosInstance.post('/api/my-profile/change-password', passwordData);
    console.log(response.data);
    if (response.status === 200) {
      await router.push('/my-profile');
    }
  }
  catch (error) {
    console.error(error);
    const axiosError = error as AxiosError;
    if (axiosError.response && axiosError.response.status === 400 && axiosError.response.data === 'Old password does not match current password.') {
      alert('Old password is incorrect!')
    }
    else if (axiosError.response && axiosError.response.status === 401) {
      alert('You are not authorized to change password!')
    }
    else {
      alert('Something went wrong!')
    }
  }
}





async function loadData() {
  try {
    const response = await getUserData();
    if (response) {
      user.value = response;
    } else {
      await userStore.logOut();
    }
  } catch (error) {
    console.error(error);
  }
}

</script>

<style scoped>


.button-wrapper {
  display: flex;
  justify-content: space-between;
}


.submit:hover{
  background-color: white !important;
  color: black !important;
  transform: scale(1.05);
}

/*
@media (min-width: 768px) {
  .new-btn {
    width: auto;
    margin-left: 0;
  }
}

 */


/*
.update-btn{
  font-size: 1.25rem;
  color: black;
  background-color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 5px;
  margin-top: 20px;
  width: 40%;
  z-index: 1000;
}

 */


.update-btn:hover{
  background-color: #cbcaca;
  color: black;
  transform: scale(1.05);
}
/*
@media (min-width: 768px) {
  button {
    width: auto;
  }
}

 */

/*
.header{
  font-size: 4rem;
  color: white;
  font-weight: 300;
  height: fit-content;
  margin-bottom: 20px;
}

 */

.user-email{
  font-size: 1.4rem;
  color: white;
  font-weight: 300;
  margin-top: -25px;
}

.edit-container{
  position: relative;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 20px;
  padding: 2rem;
}

.user-details {
  flex: 1;
}

.profile-picture-container{
  margin-left: 2rem;
}

.profile-container {
  display: grid;
  grid-template-columns: auto 1fr;
  align-items: center;
  column-gap: 1rem;
  justify-items: end;
  justify-content: end;
  height: 100%;
}

.user-name {
  font-size: 6rem;
  color: white;
  font-weight: 300;
  height: fit-content;
}

.profile-box {
  grid-row: 1;
  grid-column: 2;
  justify-self: end;
}

.profile-picture {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}

.log-out-btn {
  font-size: 1.25rem;
  color: black;
  background-color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 20px;
  align-self: flex-end;
  text-align: left;
  justify-self: start;
}

.log-out-btn:hover {
  background-color: lightgray;
}


.input {
  background-color: rgb(92, 88, 88);
  border-radius: 12px;
  border: 0;
  box-sizing: border-box;
  color: #eee;
  font-size: 18px;
  height: 100%;
  outline: 0;
  padding: 4px 20px 0;
  width: 100%;
}

.cut {
  background-color: rgba(92, 88, 88, 0);
  border-radius: 10px;
  height: 20px;
  left: 20px;
  position: absolute;
  top: -20px;
  transform: translateY(0);
  transition: transform 200ms;
  width: 45px;
}

.input:focus~.cut,
.input:not(:placeholder-shown)~.cut {
  transform: translateY(8px);
  background-color: rgba(92, 88, 88, 1);
}

.placeholder {
  color: white;
  font-family: sans-serif;
  left: 20px;
  line-height: 14px;
  pointer-events: none;
  position: absolute;
  transform-origin: 0 50%;
  transition: transform 200ms, color 200ms;
  top: 20px;
}

::placeholder {
  color: white;
}

.input:focus~.placeholder,
.input:not(:placeholder-shown)~.placeholder {
  transform: translateY(-30px) translateX(10px) scale(0.75);
}

.input:not(:placeholder-shown)~.placeholder {
  color: #808097;
}

.input:focus~.placeholder {
  color: white;
}

/*
@media (max-width: 768px) {
  .profile-picture {
    width: 200px;
    height: 200px;
  }

  .edit-container {
    flex-direction: column-reverse;
  }

  .profile-picture-container {
    margin: auto;
  }

  .update-btn{
    width: 100%;
    z-index: 999;
  }

  .header{
    font-size: 3rem;
    margin: auto;
  }

  .user-details{
    margin: auto;
    width: 85%;
  }

  div, button{
    margin-top: 50px;
  }
}

 */
</style>
