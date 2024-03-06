<template>
  <div class="display">
    <font-awesome-icon icon="code" />
    <h2>Code to compile</h2>
    <textarea rows="10" cols="50" id="code-input" v-model="localCode" @input="emitUpdate" placeholder="Enter your python code here :)"></textarea>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

const props = defineProps({
  code: String,
});

const localCode = ref(props.code);

const emit = defineEmits(['update-code']);

function emitUpdate(){
  emit('update-code', localCode.value);
}
</script>

<style scoped>
.display {
  text-align: center;
  margin-top: 20px;
  font-size: 15px;
  color: #9f825a;
}
</style>