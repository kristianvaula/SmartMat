<template>
  <div class="profile-container form-light-color dark:form-dark-color border-4 border-[#31C48D]/60 p-2 h-40 w-3/4  lg:w-6/12 lg:h-52 justify-center items-center">
    <div class="flex flex-row justify-between align-middle h-full relative">
      <div class="user-details pt-5">
        <h1 class="user-name text-black dark:text-white ml-1 text-2xl lg:text-4xl font-light">{{ user.name }}</h1>
        <p class="user-email mt-2 lg:mt-5 ml-2 lg:ml-2 text-black dark:text-white lg:text-4xl text-lg font-light">{{ user.email }}</p>
      </div>
      <div class="flex items-center">
        <img class="h-24 lg:h-32 w-24 lg:w-32 mr-1 pointer-events-none" src="../../assets/profile.png" alt="Profile Picture" />
      </div>
    </div>
    <button class="box logout-btn text-black dark:text-white form-light-color dark:form-dark-color border-2 border-[#31C48D]/60" @click="logOut">
      {{ $t('log_out') }}</button>
  </div>
</template>



<script setup lang="ts">
import { getUserData } from "~/service/httputils/authentication/AuthenticationService";
import type { User } from "@/types/UserType";
import { onMounted, ref } from 'vue';
import { useUserStore } from "~/store/userStore";
import { postLogOut} from "~/service/httputils/authentication/AuthenticationService";

const userStore = useUserStore();

const user = ref({} as User);
const router = useRouter();

onMounted(() => {
  loadData();
});

async function logOut() {
  try{
    const response = await postLogOut();
    if (response.status === 200){
      userStore.logOut();
      router.push('/')
    }
  } catch (error) {
    console.error(error);
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

.logout-btn {
  position: absolute;
  bottom: -24px;
  left: 90px;
  width: fit-content;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 7px 15px;
  border-radius: 15px;
}

/*
.user-email{
  font-size: 1.4rem;
  color: white;
  font-weight: 300;
}

 */

/*
.profile-picture-container{
  flex: 0.5;
  display: flex;
  justify-content: right;
}

 */

.profile-container {
  flex-direction: row;
  justify-content: center;
  margin: 0 auto;
  border-radius: 10px;
  position: relative;
}

/*
.user-details {
  grid-row: 1;
  grid-column: 1;
  margin-right: 20px;
}

 */

/*
.user-name {
  font-size: 6rem;
  color: white;
  font-weight: 300;
  height: fit-content;
}

 */

/*
.profile-picture {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

 */


/*
@media (max-width: 768px) {
  .profile-picture{
    width: 100px;
    height: 100px;
  }

  .profile-container{
    height: 150px;
  }

  .profile-picture-container{
    justify-content: center !important;
  }


}

 */

.box:hover {
  cursor: pointer;
  transform: scale(1.15);
  transition: transform .5s ease-out;
}
</style>
