<template>
  <div class="header text-black dark:text-white mt-8 text-center text-4xl font-bold">{{ t('edit_user_details') }}</div>
  <div class="edit-container border-2 bg-slate-100 border-[#31C48D]/60 dark:border-2 dark:border-white w-5/6 h-fit-content md:w-1/3 mt-2 mx-auto form-light-color dark:form-dark-color">
    <div class="user-details ">
      <div class="form-group w-full">
        <BaseInput :cut-width="'50px'" id="inpName" class="input-container" type="text" :label="$t('name')" v-model="user.name"/>
      </div>
      <div class="form-group w-full">
        <BaseInput :cut-width="'55px'" id="inpEmail" class="input-container" type="text" :label="$t('email')" v-model="user.email"/>
      </div>
      <div class="button-wrapper">
        <NuxtLink :to="localePath('/my-profile')">
          <button class="update-btn py-2 px-4 mr-4 lg:py-4 lg:px-5 form-light-button text-white">{{t('go_back')}}</button>
        </NuxtLink>
        <NuxtLink :to="localePath('/my-profile/change-password')">
          <button class="update-btn py-2 px-4 mt-2 lg:py-4 lg:px-5 form-light-button text-white">{{ t('change_password') }}</button>
        </NuxtLink>
      </div>
      <button @click="updateAccount" id="update" class="update-btn py-1 px-4 mt-5 lg:py-4 lg:px-5 form-light-button text-white">{{ t('update_user_details') }}</button>
    </div>
  </div>
</template>


<script setup lang="ts">
import {getUserData} from "~/service/httputils/authentication/AuthenticationService";
import type { User } from "@/types/UserType";
import { onMounted, ref } from 'vue';
import { useUserStore } from "~/store/userStore";
import axiosInstance from "@/service/AxiosInstance";
import BaseInput from "@/components/Form/BaseInput.vue";
import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";

const { t } = useI18n();

const userStore = useUserStore();

const user = ref({} as User);

const router = useRouter();

onMounted(() => {
  loadData();
});

function route(route : string){
  router.push(route);
}


async function updateAccount() {
  const updatedUser = {
    name: user.value.name,
    email: user.value.email
  };

  try {
    const response = await axiosInstance.post('/api/my-profile/edit', updatedUser);

    if (response.status === 200) {
      // Successful update
      await router.push('/my-profile');
    } else {
      // Handle non-200 status codes
      console.error(`Unexpected status code: ${response.status}`);
      // You can show a user-friendly error message using a notification library or custom UI component
    }
  } catch (error) {
    console.error(error);
    // You can show a user-friendly error message using a notification library or custom UI component
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

/*
.button-wrapper {
  display: flex;
  justify-content: space-between;
}

 */


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

.update-btn{
  font-size: 1.25rem;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 5px;
  z-index: 1000;
}

.update-btn:hover{
  background-color: #cbcaca;
  color: black;
  transform: scale(1.05);
}
@media (min-width: 768px) {
  button {
    width: auto;
  }
}

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

p {
  color: white;
}

.input-container {
  margin-top: 40px;
  height: 50px;
  position: relative;
  margin-bottom: 20px;
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
