<template>
    <div class="w-4/5">
        <canvas ref="chart"></canvas>
        <div class="md:flex hidden flex flex-row justify-center p-5 font-bold">
            <div class="w-80 h-48 bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl pt-10 m-5">
              <h1 class="flex justify-center text-xl mb-3"> {{t('total_food_eaten')}} </h1>
              <h3 class="flex justify-center text-green-custom text-3xl"> {{ totalFoodEaten / 1000 }} KG </h3> 
              <h3 class="flex justify-center text-green-custom text-xl"> {{ getTotalFoodEatenPercentage() }} % </h3> 
            </div>
            <div class="w-80 h-48 bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl pt-10 m-5">
              <h1 class="flex justify-center text-xl mb-3"> {{t('total_food_wasted')}} </h1>
              <h3 class="flex justify-center text-red-400 text-3xl"> {{ totalFoodWaste / 1000 }} KG </h3> 
              <h3 class="flex justify-center text-red-400 text-xl"> {{ getTotalFoodWastePercentage() }} % </h3> 
            </div>
            <div class="w-80 h-48 bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl pt-10 m-5">
              <div v-if="!showInfo">
                <h1 class="flex justify-center text-xl mb-3"> {{t('norwegian_average')}} </h1>
                <h3 class="flex justify-center text-red-400 text-3xl"> 160 KG </h3> 
                <div class="flex justify-center">
                  <button @click="showInfo = true" class="w-12 border-2 border-black dark:border-white rounded-3xl text-xl font-serif m-2">i</button>
                </div>
              </div>
              <div v-else>
                <a href="https://www.matprat.no/artikler/matsvinn/hvorfor-kaster-vi-mat/" target="_blank" class="flex justify-center text-xl"> MatPrat (2022) </a> 
                <h3 class="text-base text-center text-slate-400"> Based on an average norwegian household (4 pers)</h3>
                <div class="flex justify-center">
                  <button @click="showInfo = false" class="w-12 border-2 border-red-400 rounded-3xl text-xl text-red-400 font-mono m-2">X</button>
                </div>
              </div>
            </div>
        </div>
        <div class="md:hidden flex flex-col justify-center p-5 font-bold">
            <div class="w-64 md:w-80 bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl p-10 m-5">
              <h1 class="flex justify-center text-xl mb-3"> {{t('total_food_eaten')}} </h1>
              <h3 class="flex justify-center text-green-custom text-4xl"> {{ totalFoodEaten / 1000 }} KG </h3> 
              <h3 class="flex justify-center text-green-custom text-xl"> {{ getTotalFoodEatenPercentage() }} % </h3> 
            </div>
            <div class="w-64 md:w-80  bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl p-10 m-5">
              <h1 class="flex justify-center text-xl mb-3"> {{t('total_food_wasted')}} </h1>
              <h3 class="flex justify-center text-red-400 text-4xl"> {{ totalFoodWaste / 1000 }} KG </h3> 
              <h3 class="flex justify-center text-red-400 text-xl"> {{ getTotalFoodWastePercentage() }} % </h3> 
            </div>
            <div class="w-64 md:w-80 h-48 max-h-56 bg-zinc-100 dark:bg-zinc-700 dark:text-white border-2 border-black dark:border-white rounded-3xl p-10 m-5">
              <div v-if="!showInfo">
                <h1 class="flex justify-center text-xl mb-3"> {{t('norwegian_average')}} </h1>
                <h3 class="flex justify-center text-red-400 text-4xl"> 160 KG </h3> 
                <div class="flex justify-center">
                  <button @click="showInfo = true" class="w-12 border-2 border-black dark:border-white rounded-3xl text-xl font-serif m-2">i</button>
                </div>
              </div>
              <div v-else>
                <a href="https://www.matprat.no/artikler/matsvinn/hvorfor-kaster-vi-mat/" target="_blank" class="flex justify-center text-xl mb-1"> MatPrat (2022) </a> 
                <h3 class="text-base text-center text-slate-400"> Based on an average norwegian household (4 pers)</h3>
                <div class="flex justify-center">
                  <button @click="showInfo = false" class="w-12 border-2 border-red-400 rounded-3xl text-xl text-red-400 font-mono m-2">X</button>
                </div>
              </div>
            </div>
        </div>
    </div>    
  </template>
  
  <script lang="ts">
  import Chart from 'chart.js/auto';
  import StatsService from '~/service/httputils/StatsService';
  import { useRefrigeratorStore } from '~/store/refrigeratorStore';
  import { Refrigerator } from '~/types/RefrigeratorType';
  
  export default {
    data() {
      return {
        statMonths: [] as StatMonth[],
        totalFoodEaten: 0,
        totalFoodWaste: 0,
        totalFood: 0,
        refrigeratorId: -1,
        showInfo: false,
        chart: null as any,
      };
    },
    methods: {
      createChart() {
        if (this.chart) {
          this.chart.destroy();
        }
        this.chart = new Chart(this.$refs.chart, {
          type: 'bar',
          data: {
            labels: this.statMonths.map((month) => String(month.monthName)),
            datasets: [
              {
                label: this.t('food_eaten'),
                backgroundColor: '#31C48D',
                data: this.statMonths.map((month) => Number(month.foodEaten/1000)),
                borderColor: 'black',
                borderWidth: 1,
                borderRadius: 15,
              },
              {
                label: this.t('food_wasted'),
                backgroundColor: '#cc1244',
                data: this.statMonths.map((month) => Number(month.foodWaste/1000)),
                borderColor: 'black',
                borderWidth: 1,
                borderRadius: 15,
              },
            ],
          },
          options: {
            scales: {
              yAxes: [
                {
                  ticks: {
                    beginAtZero: true,
                  },
                },
              ],
            },
          },
        });
      },
      async fetchData() {
        try {
          this.statMonths = []
          let responseStatMonths = await StatsService.getStatsData(this.refrigeratorId);
          if (responseStatMonths.data.length > 0) {
              responseStatMonths.data.forEach((statMonth: StatMonth) => {
                this.statMonths.push(statMonth);
              }); 
          }
          } catch (error) {
              console.error(error);
              this.statMonths = [];
          }
        this.createChart();
        this.calculateTotalFoodStats();
      },
      calculateTotalFoodStats() {
            this.statMonths.forEach((month) => {
                this.totalFoodEaten += month.foodEaten;
                this.totalFoodWaste += month.foodWaste;
            });
            this.totalFood = this.totalFoodEaten + this.totalFoodWaste
        },
      getTotalFoodEatenPercentage() {
        const percentage = (this.totalFoodEaten / this.totalFood * 100).toFixed(2);
        return isNaN(percentage) ? '0.00' : percentage;
      },
      getTotalFoodWastePercentage() {
        const percentage = (this.totalFoodWaste / this.totalFood * 100).toFixed(2);
        return isNaN(percentage) ? '0.00' : percentage;
      }

    },
    mounted() {
      const refrigeratorStore = useRefrigeratorStore();
      const fridge : Refrigerator | null = refrigeratorStore.getSelectedRefrigerator;
      if (fridge !== null) {
        this.refrigeratorId = fridge.id;
      }
      this.fetchData();
    },
    setup() {
      const { t } = useI18n();
      return {
        t
      };
    },
    destroyed() {
      if (this.chart) {
        this.chart.destroy();
      }
    },
  };
  </script>
  