<template>
  <div class="app-container home">

  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
export default {
  name: "Index",
  data() {
    return {

    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  },
};
</script>

<style scoped lang="scss">

</style>

