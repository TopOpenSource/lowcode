<template>
  <div>
    <el-row :gutter="10">

        <el-card class="box-card box-card01" shadow="never">
          <div slot="header" class="clearfix">
            <span class="el-icon-document"><span style="padding-left: 10px">表单信息</span></span>
          </div>
          <instance-form ref="instForm" :objectId="objectId" :instId="instId"></instance-form>
          <el-row>
            <el-col :span="24" style="text-align: center">
              <el-button type="primary" @click="handleSave" icon="el-icon-folder-checked" size="mini"  class="el-button&#45;&#45;orange" >
                保存
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-row>

  </div>
</template>

<script>

import InstanceForm from './form'
import store from "@/store";
import {submit} from "@/api/lowcode/task/task";
export default {
  data() {
    return {
      loading: false,
      objectId: null,
      instId:null,
      attrListShow:{},
      attrListEdit:{},
      flowDialog:{
        title:'流程信息',
        open:false
      }
    }
  },
  methods: {
    close() {
      this.$emit("close");
    },
    clear(){
      this.objectId=null
      this.instId=null
      this.attrListShow= {}
      this.attrListEdit={}
    },
    loadData(objectId, instId) {
      this.clear()
      this.objectId = objectId;
      this.instId=instId;
      this.formShow();
      this.$refs.instForm.init(this.attrListEdit,this.attrListShow)
    },
    handleSave() {
      this.$refs.instForm.valid().then(valid=>{
        if(valid){
          let formData=this.$refs.instForm.getData()
          formData.objectId=this.objectId
          formData.submitState=-1
          submit(formData).then(res => {
            this.$modal.msgSuccess("操作成功");
            this.$store.dispatch('tagsView/delView', this.$route)
            this.$router.push({ path: '/lowcode/front_list', query: { formObjectId: this.objectId} })
          })
        }
      })
    },
    //控制表单、按钮 显示
    formShow() {
      store.getters.attrMap.get(this.objectId).forEach(attr=>{
        this.attrListEdit[attr.alias]=true
        this.attrListShow[attr.alias]=true
      });
    }
  },
  components: {
    InstanceForm
  },
  mounted() {
    const query = this.$route.query;
    this.loadData(query.objectId, query.instanceId);
  }
}
</script>
<style scoped>

</style>
