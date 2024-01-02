<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import store from "@/store";
export default  {
  name:  'App',
    metaInfo() {
        return {
            title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
    },
  created(){
    //TODO 放的位置不好
    if(store.getters.token){
      console.info("数据初始化")
      this.$store.dispatch('initDict')
      this.$store.dispatch('initUsers')
      this.$store.dispatch('initDepts')
      this.$store.dispatch('initAttrs')
      this.$store.dispatch('initRole')
    }
  }
}
</script>
