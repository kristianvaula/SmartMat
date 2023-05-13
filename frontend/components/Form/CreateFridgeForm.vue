<template>
  <div class="wrapper">
    <form @submit.prevent="sendForm" class="form form-light-color dark:form-dark-color border-2 border-[#31C48D]/60">
      <BaseInput :cutWidth="'118px'" id="name" class="input-container" type="name" :label="$t('name')" v-model="form.name" />
      <BaseInput :cutWidth="'65px'" id="address" class="input-container" type="address" :label="$t('address')" v-model="form.address" />
      <div class="button-wrapper">
        <GreenButton :label="$t('create_refrigerator')" width="100%" height="50px" />
      </div>
      <ErrorAlert class="mt-4" v-if="catchError" :errorMessage="errorMessage" />
    </form>
  </div>
</template>

<script setup lang="ts">

import GreenButton from "~/components/Button/GreenButton.vue";
import GrayButton from "~/components/Button/GrayButton.vue";
import BaseInput from "~/components/Form/BaseInput.vue";
import { useUserStore } from "~/store/userStore";
import { postRegister } from "~/service/httputils/authentication/AuthenticationService";
import {RefrigeratorRegisterData} from "~/types/RefrigeratorRegisterData";
import {AxiosResponse} from "axios";
import axiosInstance from "~/service/AxiosInstance";
import { postRegisterFridge } from "~/service/httputils/RefrigeratorService";
import { useRefrigeratorStore} from "~/store/refrigeratorStore";
import ErrorAlert from "~/components/AlertBox/ErrorAlert.vue";

const refrigeratorStore = useRefrigeratorStore();

const catchError = ref(false);
const errorMessage = ref("");
const router = useRouter();

const form = reactive({
  name: '',
  address: '',
});

const sendForm = async () => {
  try {
    // Send the request to the server using the postRegisterFridge function
    const response = await postRegisterFridge(form);
    if (response.status === 200) {
      console.log("fridge created")
      //todo: do something
      router.push("/")
    }
  } catch (error: any) {
    errorMessage.value = error.response;
    console.error(error.response);
    catchError.value = true;
  }
};

</script>




<style scoped>



.form {
  width: 400px;
  height: fit-content;
  padding: 0 20px 20px;
  border-radius: 15px;
}

.input-container {
  margin-top: 40px;
  height: 50px;
  position: relative;
  width: 100%;
  margin-bottom: 20px;
}

.input{
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

.cut{
  background-color: rgba(92, 88, 88,0);
  border-radius: 10px;
  height: 20px;
  left: 20px;
  position: absolute;
  top: -20px;
  transform: translateY(0);
  transition: transform 200ms;
  width: 45px;
}

.input:focus ~ .cut,
.input:not(:placeholder-shown) ~ .cut {
  transform: translateY(8px);
  background-color: rgba(92, 88, 88,1);
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

.input:focus ~ .placeholder,
.input:not(:placeholder-shown) ~ .placeholder {
  transform: translateY(-30px) translateX(10px) scale(0.75);
}

.input:not(:placeholder-shown) ~ .placeholder {
  color: #808097;
}

.input:focus ~ .placeholder {
  color: white;
}


/* Add animation (fade in the popup) */
@-webkit-keyframes fadeIn {
  from {opacity: 0;}
  to {opacity: 1;}
}

@keyframes fadeIn {
  from {opacity: 0;}
  to {opacity:1 ;}
}


@media (max-width: 768px) {
  .form {
    width: 350px;
  }
}


</style>
