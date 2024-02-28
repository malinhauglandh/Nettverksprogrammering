<script setup>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

</script>

<template>
  <div class="display">
    <font-awesome-icon icon="code" />
    <h2>Code to compile</h2>
    <textarea rows="10" cols="50"></textarea>
  </div>
</template>

<style scoped>
.display {
  text-align: center;
  margin-top: 20px;
  font-size: 15px;
  color: #9f825a;
}
</style>