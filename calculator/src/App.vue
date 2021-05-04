<template>
  <div id="calculator-component">
    <calculator/>
  </div>
</template>

<script>
import calculator from "./components/Calculator.vue";

export default {
  name: "app",
  components: {
    calculator,
  },
};
</script>

<style>
  body {
    background-color: #191919;
  }
</style>
