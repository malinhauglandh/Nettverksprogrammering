<template>
  <div class="wrapper">
    <h1>Welcome to the code compiler</h1>
    <Display :code="code" @update-code="updateCode($event)" />
    <Button :code="code" @run-and-compile="compile"/>
    <CompiledCodeDisplay v-if="compiledCode ":output="compiledCode" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import Button from '@/components/Button.vue'
import Display from '@/components/Display.vue'
import CompiledCodeDisplay from '@/components/CompiledCodeDisplay.vue';

const code = ref('') // Shared state for code
const compiledCode = ref('') // Shared state for compiled code

const apiUrl = 'http://localhost:8080/compile-and-run'


function updateCode(newCode){
  code.value = newCode
}

async function compile(){
  console.log('Compiling code:', code.value);
  await axios.post(apiUrl, { code: code.value}).then(response => {
    compiledCode.value = response.data.output;
    console.log('Compilation successful:', response.data.output);
  })
  .catch(error => {
    console.error('Compilation failed:', error);
    compiledCode.value = { output: `Error: ${error.message}` };
  });
}



</script>

<style scoped>
h1 {
  font-size: 50px;
  text-align: center;
  color: #9f825a;
}

.wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
