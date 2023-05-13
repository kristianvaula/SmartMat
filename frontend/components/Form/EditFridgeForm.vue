<template>
    <div class="wrapper">
      <form @submit.prevent="sendForm" class="form form-light-color dark:form-dark-color">
        <FormBaseInput :cut-width="'50px'" :disabled="!isSuperUser" id="name" class="input-container" type="name" :label="$t('name')" :initiated-value="refrigerator?.name" v-model="form.name" />
        <FormBaseInput :cut-width="'72px'" :disabled="!isSuperUser" id="address" class="input-container" type="address" :label="$t('address')" :initiated-value="refrigerator?.address" v-model="form.address" />
        <div class="button-wrapper">
          <ButtonGreenButton v-if="isSuperUser" :label="$t('update_information')" width="100%" height="50px" />
        </div>
      </form>
    </div>
  </template>
  
  <script lang="ts">
  import { useUserStore } from "~/store/userStore";
  import { postRegister } from "~/service/httputils/authentication/AuthenticationService";
  import { Refrigerator } from '~/types/RefrigeratorType'
  import { useRefrigeratorStore } from '~/store/refrigeratorStore';
  import { postEditFridge } from "~/service/httputils/RefrigeratorService";

  export default {
    props: {
      refrigerator : Object as () => Refrigerator | null,
      isSuperUser : {
        type : Boolean, 
        required: false,
        default: false 
      }
      
    },
    setup() {
        const {locale, locales, t} = useI18n()
        const errorMessage = ref("");
        const router = useRouter();
        const refrigeratorStore = useRefrigeratorStore();

        const form = reactive({
            address: '',
            name: '',
        });     

        return {
            errorMessage,
            router,
            refrigeratorStore,
            form,
            locale,
            locales,
            t
        }
    },
    methods : {
      sendForm (){
          try {
              if(this.refrigerator !== undefined && this.refrigerator !== null && (this.form.name !== this.refrigerator.name || this.form.address !== this.refrigerator.address)){
                console.log("1")
                if(this.form.name.length > 0 && this.form.name.length < 255){
                  console.log("2")
                  let refrigeratorDTO : Refrigerator = {id : this.refrigerator.id, name : this.form.name, address : this.form.address, members : null}
                  postEditFridge(refrigeratorDTO).then((response) => {
                    if(response){
                      alert(this.t("edit_refrigerator_success"))
                      location.reload();
                    }
                    else{
                      this.errorMessage = this.t("edit_refrigerator_failure"); 
                    }
                  })

                }
                else {
                  this.errorMessage = "Invalid entries, please fill in valid name"
                }
              }
          } catch (error: any) {
            this.errorMessage = error.response;
          }
      }
    }
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
      width: 290px;
    }
  }
  
  
  </style>