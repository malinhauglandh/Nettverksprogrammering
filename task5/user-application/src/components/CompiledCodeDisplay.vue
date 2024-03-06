<template>
  <div class="wrapper">
      <div class="output">
        <pre v-if="props.output.length !== 0">
            <h3>Output:</h3>
            <p>{{ output }}</p>
        </pre>
        <pre v-else>No output to display.</pre>
    </div>
  </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  
  // Receive 'output' as an object prop
  const props = defineProps({
    output: String
  });
  </script>
  
  <style scoped>
  .wrapper {
    margin-top: 20px;
    font-size: 15px;
    color: #9f825a;
  }
  
  pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    font-size: 20px;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    transition: 0.3s;
    min-width: 390px;
    box-sizing: border-box;
    display: grid;
    font-family: 'Times New Roman', Times, serif;
  }

  pre h3 {
    justify-self: center;
  }




  </style>
  