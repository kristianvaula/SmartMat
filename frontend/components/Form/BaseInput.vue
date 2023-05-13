<template>
  <div class="input-container">
    <input
        :id="id"
        placeholder=" "
        class="input"
        :type="type"
        :value="modelValue"
        required
        :disabled="disabled"
        :class="[disabled ? 'opacity-80' : 'opacity-100']"
        @input="$emit('update:modelValue', $event.target.value)"
    >
    <div class="cut" :style="{ width: cutWidth }"></div>
    <label :class="[disabled ? 'opacity-80' : 'opacity-100']" :for="id" class="placeholder">{{ label }}</label>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  props: {
    id: {
      type: String,
      required: true,
    },
    label: {
      type: String,
      default: '',
    },
    modelValue: {
      type: [String, Number],
      default: '',
    },
    type: {
      type: String,
      default: 'text',
    },
    cutWidth: {
      type: String,
      default: '70px',
    },
    initiatedValue: {
      type:[String, Number],
      required: false
    },
    disabled : {
      type: Boolean, 
      required: false 
    },
    form: {
      type: Object,
      required: false,
    },
  },
  methods : {
    updateInitiatedValue(){
      if(this.initiatedValue !== undefined && this.modelValue === '') {
        this.$emit('update:modelValue', this.initiatedValue);
      }
    }
  },
  watch : {
    initiatedValue(){this.updateInitiatedValue()}
  },
  mounted() {
    this.updateInitiatedValue();
  }
});
</script>

<style scoped>
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
}

.input:focus ~ .placeholder {
  color: white;
}
</style>