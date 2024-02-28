<script setup>
import Button from '@/components/Button.vue'
import Display from '@/components/Display.vue'
</script>

<template>
  <div class="wrapper">
    <h1>Welcome to the code compiler</h1>
    <Display />
    <Button />
  </div>
</template>

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