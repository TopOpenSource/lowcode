<template>
  <div>
    <el-row>
      <el-checkbox v-model="noticeEnable">启用</el-checkbox>
    </el-row>
    <el-row v-if="noticeEnable">
      <el-checkbox-group v-model="checkList">
        <el-checkbox label="dingding">钉钉</el-checkbox>
        <el-checkbox label="email">邮箱</el-checkbox>
      </el-checkbox-group>
    </el-row>
  </div>
</template>

<script>

import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";

export default {
  name: "notice",
  data() {
    return {
      noticeEnable: false,
      checkList: []
    }
  },
  mixins: [mixinProperties],
  methods: {
    clear() {
      this.noticeEnable = false;
      this.checkList = [];
    },
    save() {
      let result = true

      this.config[this.element.id]['notice'] = {};
      this.config[this.element.id]['notice']['noticeEnable'] = this.noticeEnable;
      if (this.noticeEnable) {
        this.config[this.element.id]['notice']['checkList'] = this.checkList;
      }

      return result
    },
    initData() {
      this.clear();
      if(this.config && this.config[this.element.id] && this.config[this.element.id]['notice']){
        this.noticeEnable = this.config[this.element.id]['notice']['noticeEnable'];
        if (this.noticeEnable) {
          this.checkList = this.config[this.element.id]['notice']['checkList']
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
