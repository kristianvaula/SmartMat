<template>
  <form @submit.prevent="sendForm" class="form form-light-color dark:form-dark-color">
    <FormBaseInput id="email" class="input-container" type="email" :label="$t('email')" v-model="form.email" />
    <div class="button-wrapper">
      <ButtonGreenButton :label="$t('invite_user')" width="100%"  height="50px" />
    </div>
  </form>
  </template>
  
  <script lang="ts">
  import { useRefrigeratorStore } from '~/store/refrigeratorStore';
  import { Refrigerator } from '~/types/RefrigeratorType'
  import { postAddMember } from '~/service/httputils/RefrigeratorService';
  import type { MemberRequest } from "~/types/MemberRequest";
  
  export default {
    props: {
        refrigerator : Object as () => Refrigerator | null,

    },
    setup() {
        const {locale, locales, t} = useI18n()
        const refrigeratorStore = useRefrigeratorStore();
        const errorMessage = ref("");
        const form = reactive({
          email: '',
        });

        const sendForm = async () => {
        try {
            const newMember: MemberRequest = {
            refrigeratorId: refrigeratorStore.getSelectedRefrigerator.id, 
            userName: form.email,
            fridgeRole: "USER", 
            };
            const response = await postAddMember(newMember);
            if(response !== null) {
              alert(t("member_added_success"))
              location.reload(); 
            }
            else alert(t("member_added_failure"))
          } catch (error: any) {
              console.log(error);
              errorMessage.value = error.response;
          }
      };
      return {form, sendForm, errorMessage, locale, locales, t}
    },
  }

  

</script>
  <style scoped>
   .form {
    width: 400px;
    height: fit-content;
    padding: 0 20px 20px;
    border-radius: 15px;
  }
  
  .input-container {
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
      width: 290px;
    }
  }
  
  </style>