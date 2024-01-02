<template>
  <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
    <!--提交审核-->
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="申请标题" prop="applyTitle">
             <el-input v-model="form.applyTitle"  placeholder="请填写申请的关键信息"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="form.flow_inc_conf">
        <el-col>
          <div class="top-content-title">
            <span>设置环节待办人</span>
          </div>
        </el-col>
      </el-row>

      <el-row v-if="form.flow_inc_conf">
        <template v-for="(obj,key) in form.flow_inc_conf">
          <el-form-item :label="obj.name" :rules="rules.assignee"  :prop="'flow_inc_conf['+key+'].assignee'">
            <el-select filterable v-model="form.flow_inc_conf[key].assignee" placeholder="请选择" multiple >
              <el-option
                v-for="item in form_options.userList"
                :key="item.userName"
                :label="item.nickName"
                :value="item.userName"
              ></el-option>
            </el-select>
          </el-form-item>
        </template>
      </el-row>

    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      <el-button type="primary" size="small" @click="handleSave">保存为草稿</el-button>
      <el-button @click="cancel" size="small">取 消</el-button>
    </div>

  </el-dialog>
</template>

<script>
import store from '@/store'
import {selFlowInst, submit} from '@/api/lowcode/task/task'
import {getFlowObject} from "@/api/lowcode/config/flow_config/processdefinition"

export default {
  name: "TaskSubmit",
  data() {
    return {
      submitLoading: false,
      title: '提交申请',
      open: false,
      form:{
        applyTitle:'',
        flow_inc_conf:{ "Activity_0x3wwdt": { "assignee": "2", "name": "任务节点1" } }
      },
      formData:null,//表单信息
      rules:{
        applyTitle:[{ required: true, message: "不能为空", trigger: "blur" }],
        assignee:[
          { required: true, message: "不能为空", trigger: "blur" }
        ]
      },
      form_options:{
        userList:store.getters.userList,
      },
    }
  },
  methods:{
    reset(){
      this.form = {
        applyTitle:'',
        flow_inc_conf:null
      };
      this.resetForm("form");
    },
    //获取申请信息
    loadApplyInfo(instId){
      if(instId){
        selFlowInst(instId).then(res=>{
           this.form.applyTitle=res.data.comment
           this.form.flow_inc_conf=JSON.parse(res.data.flowIncConfig)
        })
      }
    },
    initData(objectId,instId,formData){
      this.reset();
       //获取流程配置信息
        getFlowObject(objectId).then(res=>{
          this.form.flow_inc_conf=this.filterApplyDiy(res.data.config)
          this.formData=formData
          //获取申请信息
          this.loadApplyInfo(instId)
        })
    },
    handleSave(){
      //设置提交状态为0 草稿
      this.submit("0")
    },
    handleSubmit(){
      this.submitLoading = true;
      //设置提交状态为1 提交
      this.submit("1");
    },
    submit(submitState){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.formData.submitState=submitState
          this.formData.flowIncConf=JSON.stringify(this.form.flow_inc_conf)
          this.formData.applyTitle=this.form.applyTitle
          submit(this.formData).then(res=>{
            this.$emit("refresh",res.data)

            this.submitLoading = false;
            this.$modal.msgSuccess("操作成功");
            this.open=false
          }).catch(error => {
            this.submitLoading = false;
          })
        }
      });
    },
    //过滤需要用户自定义人员的环节
    filterApplyDiy(config){
      let flow_inc_conf= {}
      let flowConfig=JSON.parse(config)
      for (let i in flowConfig){
        if(flowConfig[i] && flowConfig[i]['assignee']){
          let val=flowConfig[i]['assignee']
          if(val.auditUserType==4){
            flow_inc_conf[i]={"assignee":null,"name":val.name}
          }
        }
      }

      if(Object.keys(flow_inc_conf).length==0){
        return null
      }else{
        return flow_inc_conf
      }

    },
    cancel() {
      this.open=false
    }
  }
}
</script>

<style scoped>

</style>
