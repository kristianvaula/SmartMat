<template>
  <div class="container md:h-fit">
    <div class="content-wrapper mt-24">
        <ProfileInfo  />
      <div class="grid grid-cols-1 xl:grid-cols-2 gap-2 mt-12 xl:mt-24 text-center items-center justify-center text-center pt-10 space-y-1 flex flex-wrap">
        <NuxtLink :to="localePath('/my-profile/edit')">
          <InfoBox id="myAccountBtn" :title="$t('edit_user_details')" content="View and edit my account details" class=""/>
        </NuxtLink>
        <NuxtLink :to="localePath('/system-settings')">
          <InfoBox :title="$t('system_settings')" content="Systeminnstillinger" class=""/>
        </NuxtLink>
<!--        <NuxtLink :to="localePath('')">
          <InfoBox title="Ekstra knapp" @click="" content="Ekstra knapp" class=""/>
        </NuxtLink>-->
      </div>
    </div>
    <!--    <div class="wave-container">
          <Wave></Wave>
        </div>-->
  </div>
</template>



<script setup lang="ts">
//import Wave from '@/components/Wave/Wave.vue'
import { ref, computed} from 'vue';
import { useUserStore } from "~/store/userStore";
import InfoBox from "~/components/Profile/InfoBox.vue";
import ProfileInfo from "~/components/Profile/MyProfile.vue";


const userStore = useUserStore();
const totalPages = ref(1);

const pages = computed(() => {
  const pageArray = [];
  for (let i = 1; i <= totalPages.value; i++) {
    pageArray.push(i);
  }
  return pageArray;
});

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})


const router = useRouter();
function route(route : string){
  router.push(route);
}
</script>

<style scoped>


/*
@media screen and (max-width: 1000px) {
  .container{
    min-height: 100vh !important;
  }
}

 */

/*
.grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  width: fit-content;
  gap: 50px;
}

 */

/*
.content-wrapper{
  justify-content: center;
  display: flex;
  flex-direction: column;
  margin-bottom: 120px;
}

 */


.wave-container{
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
}

/*
Wave {
  position: relative;
  width: 100%;
  height: 15vh;
  min-height: 100px;
  max-height: 150px;
}

 */
.container {
  animation: gradient 15s ease infinite;
  margin: 0 auto;
}

.item-list-wrapper::-webkit-scrollbar {
  width: 10px;
}

.item-list-wrapper::-webkit-scrollbar-track {
  background-color: #f1f1f1;
  border-radius: 10px;
}

.item-list-wrapper::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 10px;
}

.item-list-wrapper::-webkit-scrollbar-thumb:hover {
  background-color: #555;
}

@keyframes gradient {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* Animation */

.parallax > use {
  animation: move-forever 25s cubic-bezier(.55,.5,.45,.5)     infinite;
}
.parallax > use:nth-child(1) {
  animation-delay: -2s;
  animation-duration: 10s;
}
.parallax > use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 13s;
}
.parallax > use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 16s;
}
.parallax > use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 23s;
}
@keyframes move-forever {
  0% {
    transform: translate3d(-90px,0,0);
  }
  100% {
    transform: translate3d(85px,0,0);
  }
}

/*
@media (max-width: 768px) {
  item-list {
    width: 100%;
  }
  .item-list-wrapper {
    padding-top: 0;
    width: 95%;
    height: 95%;
    background-color: white;
    border-radius: 10px;
    position: absolute;
  }

  .close-btn {
    color: white;
    position:fixed;
    top: 5px;
    right: 5px;
  }
  .grid {
    padding-top: 2rem;
    flex-direction: column;
    align-items: center;
    gap: 30px;
    margin: 0 auto 0;
  }
  .overlay {
    width: 100%;
    height: 100%;
  }
  .content-wrapper{
    margin: 60px auto;
    width: 80%;
  }

  .wave-container {
  }

  .container {
    height: fit-content;
  }
}

 */
</style>
