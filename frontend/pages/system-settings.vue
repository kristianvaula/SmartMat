<template>
  <div class="w-3/4 sm:w-2/3 min-h-fit h-4/5 md:h-3/5 bg-slate-100 border-4 border-[#31C48D]/60 dark:bg-zinc-500 mx-auto text-center rounded-xl py-6 mt-10">

    <p class="text-center dark:text-white mt-7 text-xl font-bold">{{t('system_settings')}}</p>

    <div class="w-3/4 sm:w-2/3 mx-auto pt-2">

      <div class="divider"></div>

      <div class="w-3/4 sm:w-2/3 mx-auto">
        <HeadlessListbox as="div" v-model="selected">
          <HeadlessListboxLabel class="block text-xl leading-6 text-gray-900 dark:text-gray-100">{{t('language')}}</HeadlessListboxLabel>
          <div class="mx-auto relative mt-2">
            <HeadlessListboxButton class="hover:cursor-pointer relative w-full cursor-default rounded-md bg-white dark:bg-zinc-400 py-1.5 pl-3 pr-10 text-left text-gray-900 dark:text-gray-100 shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-zinc-400 mt-3 focus:outline-none focus:ring-2 focus:ring-green-500 dark:ring-green-600 sm:text-sm sm:leading-6">
              <span class="flex items-center">
                <span class="ml-3 block truncate">{{ nameOfLocale }}</span>
              </span>
              <span class="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2"></span>
            </HeadlessListboxButton>

            <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
              <HeadlessListboxOptions class="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white dark:bg-zinc-400 py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 dark:ring-zinc-400 focus:outline-none sm:text-sm">
                <HeadlessListboxOption as="template" v-for="item in availableLocales"
                                       :key="typeof item === 'object' ? item.code : item"
                                       :value="typeof item === 'object' ? item.code : item"
                                       v-slot="{ active, nameOfLocale }"
                                       @click="selected = item.code">
                  <li :class="[active ? 'bg-emerald-400 dark:bg-green-500 text-white' : 'text-gray-900', 'relative cursor-default select-none py-1 pl-3 pr-9']">
                    <div class="w-full h-full">
                      <NuxtLink
                          :to="switchLocalePath(item.code)"
                          class="text-black dark:text-white-600 hover:underline block w-full h-full py-3"
                      >
                        {{ item.name }}
                      </NuxtLink>
                    </div>
                  </li>
                </HeadlessListboxOption>
              </HeadlessListboxOptions>
            </transition>
          </div>
        </HeadlessListbox>
      </div>

      <!-- Dark Mode -->
      <div class="flex items-center mt-12 justify-between">
        <p class="font-bold text-xl text-gray-900 dark:text-gray-100 mr-4">{{t('dark_mode')}}</p>
        <div class="w-16 flex flex-col items-center" @click="toggleDarkmode = !toggleDarkmode; setColorTheme(toggleDarkmode ? 'dark' : 'light')">
          <div class="hover:cursor-pointer w-16 h-10 flex items-center bg-gray-300 rounded-full p-1 duration-300 ease-in-out" :class="{ 'bg-green-400 dark:bg-green-600': toggleDarkmode }">
            <div class="bg-white dark:bg-gray-300 w-8 h-8 rounded-full shadow-md transform duration-300 ease-in-out" :class="{ 'translate-x-6': toggleDarkmode }"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script lang="ts">

type Theme = 'light' | 'dark';

export default defineComponent({
  data() {
    return {
      toggleDarkmode: false,
      toggleNotifications: false,
    };
  },
  methods: {
    setColorTheme(newTheme: Theme) {
      useColorMode().preference = newTheme
    }
  },
  mounted() {
    const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
    const colorMode = useColorMode().preference;
    if (colorMode === "system" && prefersDark) {
      this.setColorTheme('dark');
      this.toggleDarkmode = true;
    } else if (colorMode === 'dark') {
      this.toggleDarkmode = true;
    }
  },
  setup() {
    const {locales, locale, t, setLocale } = useI18n();
    const selected = ref(locale.value);

    const switchLocalePath = useSwitchLocalePath()
    const availableLocales = computed(() => {
      return (locales.value).filter(i => i.code !== locale.value)
    })

    const usedLocale = computed(() => {
      return (locales.value).filter(i => i.code === locale.value)
    })

    const firstMatchingLocale = usedLocale.value[0] // Get the first element of the filtered array
    const nameOfLocale = firstMatchingLocale?.name // Access the name property with optional chaining


    const language = computed({
      get: () => locale.value,
      set: (value) => {
        selected.value = value
        setLocale(value)
      },
    });

    watch(selected, (newVal) => {
      console.log(newVal)
      language.value = newVal;

    });

    return { locale, locales, selected, switchLocalePath, availableLocales, t, usedLocale, nameOfLocale };
  },
});
</script>


<style scoped>
.divider{
  width: 100%;
  height: 2px;
  background-color: gray;
  margin: 20px 0;
}
</style>
