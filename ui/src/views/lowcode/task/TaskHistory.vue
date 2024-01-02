<template>
  <el-timeline>

    <el-timeline-item v-for="task in taskList">
        <el-row class="taskList-top">
          <el-col :md="20" class="taskList-top-left"> <span style="font-weight: 700">{{task.taskName}}</span></el-col>
  <!--      <span>{{checkState[task.checkState]}}</span>-->
          <el-col :md="4" class="taskList-top-right"> <el-tag type="success" effect="dark">{{checkState[task.checkState]}}</el-tag></el-col>
        </el-row>
<!--      <el-card :body-style="{ padding: '10px' }" >-->
         <el-row class="taskList-content">
           <el-col :span="10" class="taskList-content-left">
             <i class="el-icon-user" style="padding-right: 5px"></i>审批人
           </el-col>
           <el-col :span="14" class="taskList-content-right">
             {{task.taskAssignee}}
           </el-col>
           <el-col :span="10" class="taskList-content-left">
             <i class="el-icon-time" style="padding-right: 5px"></i>审批时间
           </el-col>
           <el-col :span="14" class="taskList-content-right">
             {{task.createTime}}
           </el-col>
           <el-col :span="10" class="taskList-content-left">
             <i class="el-icon-chat-line-square" style="padding-right: 5px"></i>备注/原因
           </el-col>
           <el-col :span="14" class="taskList-content-right">
             {{task.comment}}
           </el-col>
         </el-row>
<!--        <el-descriptions class="margin-top" :column="1" size="small" border >
          <el-descriptions-item label-class-name="my-label">
            <template slot="label"><i class="el-icon-user" style="padding-right: 5px"></i>审批人</template>
            {{task.taskAssignee}}
          </el-descriptions-item>

&lt;!&ndash;          <el-descriptions-item label-class-name="my-label">
            <template slot="label"><i class="el-icon-user"></i>审批状态</template>
            {{checkState[task.checkState]}}
          </el-descriptions-item>&ndash;&gt;

          <el-descriptions-item label-class-name="my-label">
            <template slot="label"><i class="el-icon-time" style="padding-right: 5px"></i>审批时间</template>
            {{task.createTime}}
          </el-descriptions-item>

          <el-descriptions-item label-class-name="my-label">
            <template slot="label"><i class="el-icon-chat-line-square" style="padding-right: 5px"></i>备注/原因</template>
            {{task.comment}}
          </el-descriptions-item>

        </el-descriptions>-->

<!--      </el-card>-->
    </el-timeline-item>

  </el-timeline>

</template>

<script>
import {taskHistory} from "@/api/lowcode/task/task";
export default {
  name: "TaskHistory",
  data(){
    return{
       taskList:[],
       checkState:{"1":"同意","2":"退回","3":"驳回","4":"委派","5":"转办"}
    }
  },
  props:{
    instId:null
  },
  methods:{
    loadData(){
      if(this.instId!=undefined && this.instId!=null && this.instId!=''){
        taskHistory(this.instId).then(res=>{
          this.taskList=res.data
        })
      }
    }
  },
  mounted() {
    this.$nextTick(()=>{
      this.loadData()
    })
  }
}
</script>

<style scoped>

</style>
