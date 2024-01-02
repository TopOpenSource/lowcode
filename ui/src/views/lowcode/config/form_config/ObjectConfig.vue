<template>
 <div class="my-content" style="padding-top: 6px">
  <el-tabs v-model="activeName">
    <el-tab-pane label="字段" name="first">
      <attributeList ref="attributeList"></attributeList>
    </el-tab-pane>
    <el-tab-pane label="布局" name="second">
      <layout ref="layout"></layout>
    </el-tab-pane>
  </el-tabs>
  </div>
</template>

<script>
import AttributeList from "./AttributeList";
import Layout from "./Layout";

export default {
  data() {
    return {
      activeName: 'first',
    }
  },
  components: {
    'attributeList': AttributeList,
    'layout': Layout,
  },
  methods: {

  },
  mounted(){
    const  objectId = this.$route.query.objectId;
    this.$nextTick(() => {
      this.$refs.attributeList.loadData(objectId);
      this.$refs.layout.loadData(objectId);
    });
  }
}
</script>

<style>

</style>
