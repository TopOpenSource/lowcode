<template>
  <div>
    <el-row :gutter="10">
      <el-col :sm="17" :md="17" :xl="17">
        <el-row>
        <el-card class="box-card box-card01" shadow="never">
          <div slot="header" class="clearfix">
            <span class="el-icon-document"><span style="padding-left: 10px">表单信息</span></span>
          </div>

          <instance-form ref="instForm" :objectId="objectId" :instId="instId"></instance-form>

          <el-row>
            <el-col :span="24" style="text-align: center">

              <el-button v-if="!instId" type="success"  @click="handleSubmit" icon="el-icon-folder-checked" size="mini" class="el-button--orange">保存/提交
              </el-button>

<!--              <el-button v-if="isMyTask && edit" type="primary" @click="handleSave" icon="el-icon-folder-checked" size="mini"  class="el-button&#45;&#45;orange" >-->
<!--                保存-->
<!--              </el-button>-->

              <el-button v-if="isMyTask && btnShow['1']" type="success" @click="handleComplate" icon="el-icon-circle-check" size="mini" class="el-button&#45;&#45;orange">
                同意
              </el-button>
              <el-button v-if="isMyTask && btnShow['2']" type="warning" @click="handleReturn" icon="el-icon-refresh-left" size="mini" class="el-button&#45;&#45;orange">
                退回
              </el-button>
              <el-button v-if="isMyTask && btnShow['3']" type="danger" @click="handleReject" icon="el-icon-circle-close" size="mini" class="el-button&#45;&#45;orange">
                驳回
              </el-button>
              <el-button v-if="isMyTask && btnShow['4']" type="primary" @click="handleDelegate" icon="el-icon-delete" size="mini" class="el-button&#45;&#45;orange">
                委派
              </el-button>
              <el-button v-if="isMyTask && btnShow['5']" type="warning" @click="handleAssign" icon="el-icon-delete" size="mini" class="el-button&#45;&#45;orange">
                转办
              </el-button>
            </el-col>
          </el-row>


          <el-dialog :title="flowDialog.title" :visible.sync="flowDialog.open" width="1000px" append-to-body>
            <flow-view ref="flowView"></flow-view>
          </el-dialog>
            </el-card>
        </el-row>

      </el-col>

      <el-col :sm="7" :md="7" :xl="7">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="el-icon-document"> <span style="padding-left: 10px">审批记录</span></span>
            <el-button type="text" style="padding-left: 10px;padding-top: 0px" icon="el-icon-s-flag" @click="showFlow">查看流程</el-button>
          </div>
         <task-history ref="taskHistory" :instId="instId"></task-history>
        </el-card>
      </el-col>

    </el-row>
    <!--提交-->
    <task-submit ref="taskSubmit" @refresh="saveOperation"></task-submit>
    <!--同意-->
    <task-complate ref="taskComplate" @refresh="saveOperation"></task-complate>
    <!--退回-->
    <task-return ref="taskReturn" @refresh="saveOperation"></task-return>
    <!--驳回-->
    <task-reject ref="taskReject" @refresh="saveOperation"></task-reject>
    <!--转办-->
    <task-assign ref="taskAssign" @refresh="saveOperation"></task-assign>
    <!--委派-->
    <task-delegate ref="taskDelegate" @refresh="saveOperation"></task-delegate>
  </div>
</template>

<script>


import {getFlowObject, readXmlStr} from "@/api/lowcode/config/flow_config/processdefinition";

import FlowView from './FlowView'

import TaskSubmit from "@/views/lowcode/task/options/TaskSubmit";
import TaskComplate from "@/views/lowcode/task/options/TaskComplate";
import TaskReturn from "@/views/lowcode/task/options/TaskReturn";
import TaskReject from "@/views/lowcode/task/options/TaskReject";
import TaskAssign from "@/views/lowcode/task/options/TaskAssign";
import TaskDelegate from "@/views/lowcode/task/options/TaskDelegate";
import TaskHistory from "@/views/lowcode/task/TaskHistory";

import InstanceForm from '../instance/form'
import {selectMyTask, submit} from "@/api/lowcode/task/task";
import store from "@/store";

export default {
  data() {
    return {
      loading: false,
      objectId: null,
      instId:null,
      taskId: null,
      actId: null,//流程ID
      btnShow:{"1":0,"2":0,"3":0,"4":0,"5":0},//审核按钮显示状态
      edit:false,
      isMyTask:false,
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
    loadData(objectId, instId) {
      this.objectId = objectId;
      this.instId=instId;

      //获取流程配置信息
      getFlowObject(objectId).then(res=>{
        let flowConfig=res.data.config

        if(this.instId!=undefined){
          //判断是否我的任务
          selectMyTask(this.instId).then(res1=>{
            this.isMyTask=(res1.data!=undefined)
            if(this.isMyTask){
              this.actId=res1.data.taskDefKey
              this.taskId=res1.data.taskId
            }
            this.formShow(flowConfig)
            this.$refs.instForm.init(this.attrListEdit,this.attrListShow)
          });
        }else{
          //提交
          //设置表单字段
          this.attrListEdit={}
          this.attrListShow={}
          store.getters.attrMap.get(this.objectId).forEach(attr=>{
            this.attrListEdit[attr.alias]=true
            this.attrListShow[attr.alias]=true
          });
          //初始化表单信息
          this.$refs.instForm.init(this.attrListEdit,this.attrListShow)
        }

      })

    },
    //处理
    handleComplate() {
      this.$refs.taskComplate.open = true;
      this.$refs.taskComplate.initData(this.objectId, this.instId, this.taskId);
    },
    handleReturn() {
      this.$refs.taskReturn.open = true;
      this.$refs.taskReturn.initData(this.objectId, this.instId, this.taskId);
    },
    handleReject() {
      this.$refs.taskReject.open = true;
      this.$refs.taskReject.initData(this.objectId, this.instId, this.taskId);
    },
    handleAssign(){
      this.$refs.taskAssign.open = true;
      this.$refs.taskAssign.initData(this.objectId, this.instId, this.taskId);
    },
    handleDelegate(){
      this.$refs.taskDelegate.open = true;
      this.$refs.taskDelegate.initData(this.objectId, this.instId, this.taskId);
    },
    handleSave() {
      this.$refs.instForm.valid().then(valid=>{
        if(valid){
          let formData=this.$refs.instForm.getData();
          formData.objectId=this.objectId;
          formData.submitState=-1;
          submit(formData).then(res => {
            this.refresh();
          });
        }
      })
    },
    //保存操作
    saveOperation(){
      if (this.isMyTask && this.edit){
        this.handleSave();
      } else {
        this.refresh();
      }
    },
    handleSubmit(){
      this.$refs.instForm.valid().then(valid=>{
        if(valid){
          let formData=this.$refs.instForm.getData()
          formData.objectId=this.objectId

          this.$refs.taskSubmit.open = true;
          this.$refs.taskSubmit.initData(this.objectId,this.instId,formData)
        }
      })

    },
    //控制表单、按钮 显示
    formShow(configStr) {
      let config = JSON.parse(configStr)
      if (config && config[this.actId] && config[this.actId]['formShow']) {
        let formShow = config[this.actId]['formShow']

        this.edit = formShow.edit == 1 ? true : false

        for(let key in formShow.attrs){
          this.attrListEdit[key]=(formShow.attrs[key]==2) && this.isMyTask
          this.attrListShow[key]=(formShow.attrs[key]>0)
        }
      }

      if(Object.keys(this.attrListShow).length==0){
        //设置默认值
        store.getters.attrMap.get(this.objectId).forEach(attr=>{
          this.attrListEdit[attr.alias]=false
          this.attrListShow[attr.alias]=true
        });
      }

      if (config && config[this.actId] && config[this.actId]['btn']) {
        let btns = config[this.actId]['btn']
        btns.forEach(btn=>{
          this.btnShow[btn.id]=(btn.state==1 && this.isMyTask)
        })
      }
    },
    refresh(){
      //this.loadData(this.objectId,this.instId)
      // this.$modal.msgSuccess("操作成功");
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: this.from.url,query:this.from.query });
    },
    showFlow(){
      this.flowDialog.open=true
      //初始化流程图
      this.$nextTick(()=>{
        //初始化流程图
        this.$refs.flowView.init(this.objectId,this.instId);
      })
    }
  },
  components: {
    FlowView,
    TaskComplate,
    TaskReturn,
    TaskReject,
    TaskAssign,
    TaskDelegate,
    TaskHistory,
    TaskSubmit,
    InstanceForm
  },
  mounted() {
    const query = this.$route.query;
    this.from= query.from;
    this.loadData(query.objectId, query.instanceId);
  }
}
</script>
<style scoped>

</style>
