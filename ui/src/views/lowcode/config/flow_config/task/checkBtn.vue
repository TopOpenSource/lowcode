<template>
  <div>
    <el-table :data="btnList"   style="width: 100%">
      <el-table-column prop="name" label="按钮"/>

      <el-table-column  label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.state" active-value="1" inactive-value="0" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";

export default {
  name: "checkBtn",
  data(){
    return {
      btnList:null
    }
  },
  mixins: [mixinProperties],
  methods:{
    clear(){
      this.btnList=[{id:1,name:'同意',state:'1'},{id:2,name:'退回',state:'1'},{id:3,name:'驳回',state:'1'},{id:4,name:'委派',state:'1'},{id:5,name:'转办',state:'1'}]
    },
    save(){
      let result=true
      if(!this.config[this.element.id]){
        this.config[this.element.id]={}
      }
      this.config[this.element.id]['btn']=this.btnList

      return result
    },
    initData(){

      this.clear()
      if(this.config && this.config[this.element.id] && this.config[this.element.id]['btn']){
        this.btnList=this.config[this.element.id]['btn']
      }else{
         this.save()
      }
    }

  }
}
</script>

<style scoped>

</style>
