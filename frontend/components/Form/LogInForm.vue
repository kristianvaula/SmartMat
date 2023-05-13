<template>
  <div class="wrapper">
    <form @submit.prevent="sendForm" class="form border-2 border-[#31C48D]/60 form-light-color dark:form-dark-color">
      <BaseInput :cutWidth="'58px'" id="inpEmail" class="input-container" type="email" :label="$t('email')" v-model="form.email" :form="form" />
      <BaseInput :cutWidth="'68px'" id="inpPassword" class="input-container" type="password" :label="$t('password')" v-model="form.password" :form="form" />
      <div class="button-wrapper">
        <GreenButton id="login" :label="$t('log_in')" width="100%" height="50px" />
        <div class="divider"></div>
<!--        <nuxt-link :to="localePath('/register')">
          <GrayButton id="new-user" :label="$t('new_user')" width="100%" height="50px" />
        </nuxt-link>-->
        <GrayButton type="button" @click="$router.push(localePath('/register'))" id="new-user" :label="$t('new_user')" width="100%" height="50px" />
      </div>
      <ErrorAlert class="mt-4" v-if="catchError" :errorMessage="errorMessage" />
    </form>
  </div>
</template>

<script setup lang="ts">
import GreenButton from "../Button/GreenButton.vue";
import GrayButton from "../Button/GrayButton.vue";
import BaseInput from "./BaseInput.vue";
import { useUserStore } from "../../store/userStore";
import { postLogin } from "../../service/httputils/authentication/AuthenticationService";
import ErrorAlert from "../AlertBox/ErrorAlert.vue";
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const catchError = ref(false);
const errorMessage = ref('');
const router = useRouter();

const form = reactive({
  email: '',
  password: '',
});

const sendForm = async () => {
  try {
    const response = await postLogin(form);
    if (response.status === 200) {
      userStore.logIn(response.data);
      await userStore.checkAuthStatus();
      form.email = '';
      form.password = '';
      await router.push('/');
    }
  } catch (error: any) {
    errorMessage.value = error.response.data || 'An error occurred.';
    catchError.value = true;
  }
};

</script>




<style scoped>


.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}

h1{
  color: white;
  text-align: center;
  font-size: 40px;
  font-weight: 300;
}

.form {
  width: 400px;
  height: fit-content;
  padding: 0 40px 40px 40px;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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
    width: 370px;
  }
}


</style>