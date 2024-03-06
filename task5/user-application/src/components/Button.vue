<template>
    <button class="button" @click="compileClick" :disabled="!validInput" id="compile-button">Run and compile code
        <font-awesome-icon v-if="compiling" icon="spinner" class="icon spinner" />
        <font-awesome-icon v-else icon="box" id="box" class="icon box" />
    </button>
</template>


<script setup>
import { computed, ref, defineEmits } from 'vue'

const props = defineProps({
  code: String
});

const emit = defineEmits(['run-and-compile']);

const compiling = ref(false)

const validInput = computed(() => props.code.trim().length > 0)

function compileClick() {  
  const boxbutton = document.getElementById('box')
  boxbutton.style.transform = 'translateX(40px)'
  boxbutton.style.opacity = '0'
  boxbutton.style.transition = 'all 1s'
  if(!validInput.value) return;
  compile();
}

async function compile() {
    console.log('Compiling code:', props.code);
    setTimeout(()=> {
      compiling.value = true
      setTimeout(() => {
        emit('run-and-compile')
        compiling.value = false
      }, 2000)
    }, 1000)

}

</script>

<style scoped>
.button {
  background-color: #9f825a;
  color: white;
  padding: 15px 32px;
  text-align: center;
  font-size: 14px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 12px;
  border: solid white;
}

.button:hover {
  background-color: #7a5d3f;
}

.button:disabled {
  background-color: transparent;
  border: 2px solid darkgray;
  color: black;
  cursor: not-allowed;
}

.icon {
   margin-left: 10px;
   width: 12px;
   aspect-ratio: 1;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }

}

@keyframes come {
  from {
    opacity: 0;
    transform: translateX(40px);
  }
  to {
    opacity: 100%;
    transform: translateX(0);
  }
}

.spinner {
    animation: rotate 2s linear;
}

#compile-button:disabled .box {
  animation: none;
}


.box:not(:disabled) {
  animation: come 1s linear
}


</style>
