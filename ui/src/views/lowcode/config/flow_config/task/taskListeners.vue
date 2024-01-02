<template>

</template>

<script>
import mixinProperties from "../mixinProperties";

export default {
  name: "taskListeners",
  mixins: [mixinProperties],
  methods: {
    initData(){
      console.log(11111111111111111111111122222222222222222);
      let defaultListener=this.modeler.get('moddle').create('flowable:TaskListener', {event:"create",class:"com.ruoyi.flow.listener.CreateAssignInfoListener"})
      let extensionList=[]
      extensionList.push(defaultListener)
      let extensions = this.modeler.get('moddle').create("bpmn:ExtensionElements", {
        values: extensionList
      });
      this.updateProperties( {extensionElements: extensions})
    }
  }
}
</script>

<style scoped>

</style>
