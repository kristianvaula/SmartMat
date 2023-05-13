<template>
  <div class="flex items-center py-10">
    <div class="flex administrate-fridge md:w-8/12  text-black dark:text-white form-light-color dark:form-dark-color m-auto border-2 border-[#31C48D]/60">
      <ButtonFavoriteToggler
        class="relative -top-7 -left-12"
        @favorite-event="favoriteEventHandler"
        :text="false"
        :large="true"
        :isFavorite="isFavorite()" />
      <h1 class="text-center">{{$t("administrate_refrigerator")}}</h1>
      <div>
        <div class="wrapper">
          <FormEditFridgeForm :is-super-user="isSuperUser" :refrigerator="fridge"/>
        </div>
      </div>
      <div class="divider"></div>
      <h1 v-if="isSuperUser" class="title">{{ $t("add_member") }}</h1>
      <FormInviteUserForm v-if="isSuperUser" :refrigerator="fridge" />
      <div class="w-full">
          <div v-if="fridge !== null">
              <h1 v-if="isSuperUser" class="title">{{ $t("edit_members") }}</h1>
              <h1 v-else class="title">{{ $t("members") }}</h1>
              <div class="userlist-wrapper" v-for="member in fridge.members" :key="member.username">
                  <div class="userinfo-divider flex md:flex-row justify-center">
                      <div class="userinfo w-1/2 md:w-3/12">
                          <div class="username">
                              <img class="h-8 w-auto" src="@/assets/icons/profile.png" alt="">
                              <h3 class="text-xs md:text-lg">{{ member.name }}</h3>
                          </div>
                          <div class="email-wrapper">
                           <h4 class="text-xs md:text-lg">{{ member.username }}</h4>
                          </div>
                      </div>
                      <div class="member-role items-center w-1/2 md:w-3/12">
                          <select
                          :disabled="!isSuperUser"
                          :class="[isSuperUser ? 'custom-select hover:cursor-pointer' : 'disabled-select', 'h-12 px-2 rounded-md ring-1 ring-gray-300 dark:ring-zinc-600  text-black bg-white']"
                          v-model="member.fridgeRole" @change="handleOptionChange(member)">
                              <option class="hover:cursor-pointer" value="USER">User</option>
                              <option class="hover:cursor-pointer" value="SUPERUSER">Superuser</option>
                          </select>
                      </div>
                      <div class="choice-wrapper w-1/2 md:w-3/12" v-if="isUser(member.username)" @click="handleLeaveFridge(member)">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/openDoor.png" alt="Door">
                              <h4 class="email-wrapper text-xs md:text-base">{{ $t("leave_refrigerator")}}</h4>
                          </div>
                      </div>
                      <div class="choice-wrapper w-1/2 md:w-3/12" v-else-if="isSuperUser" @click="deleteMember(member)">
                          <div class="action-choice">
                              <img class="choice-image" src="@/assets/icons/trash.png" alt="Trash">
                              <h4 class="email-wrapper text-xs md:text-base">{{ $t("remove_member")}}</h4>
                          </div>
                      </div>
                  </div>
                  <div class="divider"></div>
              </div>
          </div>
      </div>
      <ButtonGreenButton v-if="isSuperUser" :label="$t('save_userroles')" width="67%" height="50px" @click="handleSaveUserRoles"/>
  </div>
  </div>
</template>

<script lang="ts">
import { useRefrigeratorStore } from '~/store/refrigeratorStore';
import { useUserStore } from '~/store/userStore';
import { getRefrigeratorById, postEditMembers, postRemoveMember, postEditFavorite, postRemoveFavorite } from '~/service/httputils/RefrigeratorService';
import { getUserData } from "~/service/httputils/authentication/AuthenticationService";
import { RemoveMemberRequest } from "~/types/RemoveMemberRequest";
import { MemberRequest } from "~/types/MemberRequest";
import type { Refrigerator } from '~/types/RefrigeratorType'
import type {Member} from "~/types/MemberType"
import fridgeSelector from "~/components/FridgeSelector.vue";

export default {
  data() {
    return {
      changes: [] as string[],
      fridge: null as Refrigerator | null,
      currentUser : null as String | null,
      favoriteRefrigeratorId : -1 as number,
      editedMembers : new Map<String, Member>(),
      isSuperUser : false 
    };
  },
  computed: {
    
  },
  setup() {
    const errorMessage = ref("");
    const refrigeratorStore = useRefrigeratorStore();
    const userStore = useUserStore();
    const {locale, locales, t} = useI18n()

    const sendForm = async () => {
      try {
      } catch (error: any) {
      errorMessage.value = error.response;
      }
    };

    const form = reactive({
      adress: '',
      name: '',
    });

    return {
      refrigeratorStore,
      sendForm,
      errorMessage,
      form,
      locale,
      locales,
      userStore,
      t
    }
  },
  watch : {
    fridge(){
      this.setRefrigeratorRole(); 
      this.isFavorite(); 
    }
  },
  methods: {
    isFavorite() : boolean {
      if(this.fridge === null) return false;
      else if(this.favoriteRefrigeratorId < 0) return false;
      else {
        return this.fridge.id === this.favoriteRefrigeratorId;
      }
    },
    handleOptionChange(member : Member) {
      this.editedMembers.set(member.username, member)
      console.log(this.editedMembers)
    },
    async handleSaveUserRoles(){
      let memberRequests : MemberRequest[] = [];
      this.editedMembers.forEach((member : Member) =>{
          const memberRequest : MemberRequest = {
          refrigeratorId : member.refrigeratorId,
          userName : member.username,
          fridgeRole : member.fridgeRole 
        }
        memberRequests.push(memberRequest); 
      })

      try{
          let response = await postEditMembers(memberRequests); 
          if(response !== null && response.status == 200){
            if(response.data == ""){
              alert(this.t("last_superuser_alert"))
            }
            else {
              alert(this.t("role_change_success"))
              this.getRefrigerator();
            }
          }
          else {
            alert(this.t("role_change_failure"))
            this.getRefrigerator();
          }
      }
      catch(error){
        alert(this.t("role_change_failure"))
        console.log(error)
        this.getRefrigerator();
      }
    },
    async leaveFridge(removeMemberRequest : RemoveMemberRequest) {
      try {
        let response = await postRemoveMember(removeMemberRequest);
        if(response !== null && response.status == 200){
          if(response.data == "" && removeMemberRequest.forceDelete == false){
            if(window.confirm(this.t("force_delete_alert"))) {
              removeMemberRequest.forceDelete = true;
              this.refrigeratorStore.deleteRefrigerator(removeMemberRequest.refrigeratorId);
              if(this.refrigeratorStore.getSelectedRefrigerator.id === removeMemberRequest.refrigeratorId){
                this.refrigeratorStore.setSelectedRefrigerator(null);
              }
              await this.leaveFridge(removeMemberRequest);
            }
          }
          else {
            alert(this.t("user_removed_success"))
            this.$router.push("/")
          }
        }
        else {
          alert(this.t("user_removed_failure"))
          this.getRefrigerator(); 
        }
      }
      catch(error) {
        alert(this.t("user_removed_failure"))
        console.log(error)
        this.getRefrigerator();
      }
    },
    async handleLeaveFridge(member : Member) {
      const removeMemberRequest : RemoveMemberRequest = {
        refrigeratorId : member.refrigeratorId,
        userName : member.username,
        forceDelete : false
      }
      await this.leaveFridge(removeMemberRequest); 
    },
    async deleteMember(member : Member) {
      const removeMemberRequest: RemoveMemberRequest = {
      refrigeratorId: member.refrigeratorId,
      userName: member.username,
      forceDelete: false,
      };
      try {
        const response = await postRemoveMember(removeMemberRequest);
        if(response !== null && response.status == 200) {
          alert(this.t("remove_member_succsess"))
          this.getRefrigerator();
        }
      } catch (error) {
        alert(this.t("remove_member_failed"))
        console.error(error);
      }
    },
    async getRefrigerator() {
      let refrigerator = null as Refrigerator | null;
      if(this.refrigeratorStore.getSelectedRefrigerator !== null){
        let response = await getRefrigeratorById(this.refrigeratorStore.getSelectedRefrigerator.id);
        if(response !== null){
            let element : Refrigerator = response.data; 
            let membersResponse = [] as Member[]; 
            element.members?.forEach((element : Member) => {
                const object : Member = {refrigeratorId : element.refrigeratorId, name : element.name, username : element.username, fridgeRole : element.fridgeRole}
                membersResponse.push(object); 
            })
            refrigerator = {id: element.id, name: element.name, address: element.address, members: membersResponse}
            this.fridge = refrigerator

        }
      }
    },
    isUser(email : String):  boolean {
      return email == this.currentUser; 
    },
    async getUserData() {
      const response = await getUserData();
      if (response) {
          this.currentUser = response.email;
          this.favoriteRefrigeratorId = response.favoriteRefrigeratorId;
      }
    },
    setRefrigeratorRole() {
      if(this.fridge !== null && this.currentUser !== null){
        const member = this.fridge.members?.find((member : Member) => member.username === this.currentUser);
        if(member !== undefined && member?.fridgeRole === 'SUPERUSER'){
          this.isSuperUser = true; 
        }
        else this.isSuperUser = false; 
      }
    },
    favoriteEventHandler(value : boolean) {
      if(this.fridge !== null) {
        if(value === true) this.favorite();
        else this.unfavorite();
      }
    },
    favorite(){
      const refId = this.fridge?.id
      if(refId !== undefined) {
        postEditFavorite(refId)
          .then((response) => {
            if(response.status === 200) {
              this.userStore.setFavoritedRefrigeratorId(refId);
              let numb = this.userStore.getFavoriteRefrigeratorId;
              if(numb !== null) this.favoriteRefrigeratorId = numb;
              alert(this.t("favorited_success"))
            }
            else {
              alert(this.t("favorited_failure"))
            }
          })
          .catch((error) => {
            console.log(error)
            alert(this.t("favorited_failure"))
          })
      }

    },
    unfavorite(){
      postRemoveFavorite()
          .then((response) => {
            if(response.status === 200) {
              this.userStore.setFavoritedRefrigeratorId(-1);
              this.favoriteRefrigeratorId = -1;
              alert(this.t("unfavorited_success"))
            }
            else {
              alert(this.t("unfavorited_failure"))
            }
          })
          .catch((error) => {
            console.log(error)
            alert(this.t("unfavorited_failure"))
          })
    }
  },
  created(){
    this.getUserData();
    this.getRefrigerator();
    this.setRefrigeratorRole();

  }
}

definePageMeta({
  "requiresAuth": true,
  middleware: [
    'auth',
  ],
})

</script>


<style>

.custom-select {
  -webkit-appearance: default;
  -moz-appearance: default;
  appearance: default;
}

.disabled-select {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.administrate-fridge {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: fit-content;
  padding: 0 20px 20px;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.email-wrapper {
  word-wrap: break-word;
  white-space: pre-wrap;
  word-break: break-word;
}
/*
.userinfo-divider {
  display: flex;
  flex-direction: row;
}

 */

.title {
  text-align:center;
  margin-bottom: 20px;
  margin-top:10px;
}

.divider{
width: 100%;
height: 2px;
background-color: gray;
margin: 20px 0;
}

.userinfo {
  display: flex;
  flex-direction: column;
  
}

.username {
  display: flex;
  flex-direction: row;
  margin-bottom: 5px;
}

.member-role {
  display: flex;
  justify-content: center;
  margin-right: 15px;
  margin-left: 5px;
}

.action-choice {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align:center;
}

.flex-row{
  display: flex;
  flex-direction: row;
}

.choice-wrapper {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
  cursor: pointer;
}

.choice-image {
  max-height: 30px;
}

</style>