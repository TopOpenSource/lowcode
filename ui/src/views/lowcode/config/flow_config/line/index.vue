<template>
  <!--任务节点配置-->
  <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
    <div class="app-container" style="padding: 0px 0px 0px 0px">
      <el-form ref="form" :model="form" :rules="rules" label-width="40px">

      <el-form-item label="名称">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <!--条件-->
      <m-conditions ref="conditions" :objectId="objectId" >
      </m-conditions>
        <div style="text-align: right;padding-top: 10px">
        <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-form>
    </div>
  </el-dialog>
</template>

<script>

import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";
import conditions from "@/views/lowcode/config/form_config/Conditions";
export default {
  name: "TaskDesigner",
  mixins: [mixinProperties],
  data() {
    return {
      title: '',
      open: false,
      form:{
        name:null,
        condition:null
      },
      rules: {}
    }
  },
  components: {
    'm-conditions':conditions
  },
  methods:{
    initData(){
      this.$nextTick(() => {
        if(this.config!=undefined){
          let condition=null
          let elementConfig=this.config[this.element.id]
          if(elementConfig){
            condition=elementConfig.condition
            this.$refs.conditions.initData(condition)
            this.form=elementConfig
          }else{
            this.$refs.conditions.initData()
          }
        }

      });
    },
    save(){
      this.updateProperties( {'name':this.form.name});

      let elmentId=this.element.id;
      const newCondition = this.modeler.get('moddle').create('bpmn:FormalExpression', { body: "${flowableToolService.flowValid(objectId,instanceId,'"+elmentId+"')}" })
      this.updateProperties({ conditionExpression: newCondition })
      // this.updateProperties({ 'flowable:skipExpression': val })

      //保存配置文件
      this.config[this.element.id]={
        condition:this.$refs.conditions.getValue(),
        name:this.form.name
      }

      this.$modal.msgSuccess("设置成功，保存后生效");
    }
  }
}
</script>

<style scoped>

</style>
