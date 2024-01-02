<template>
  <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
    <!--提交审核-->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="退回环节" prop="targetKey">
          <el-select v-model="form.targetKey"  style="width: 240px">
            <el-option v-for="returnTask in returnTaskList" :key="returnTask.id" :label="returnTask.name" :value="returnTask.id" />
          </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="退回意见" prop="comment">
            <el-input v-model="form.comment"  maxlength="30"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" @click="submitForm">提交</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

  </el-dialog>
</template>

<script>
import {findReturnTaskList} from "@/api/lowcode/task/task";
import {taskReturn} from "@/api/lowcode/task/task";
import properties from "@/views/lowcode/task/options/properties";
export default {
  name: "TaskReturn",
  mixins: [properties],
  data() {
    return {
      title: '退回',
      open: false,
      returnTaskList:[]
    }
  },
  methods: {
    initData(objectId,objectInstanceId,taskId){
      this.initData_(objectId,objectInstanceId,taskId)

      findReturnTaskList(taskId).then(res=>{
           this.returnTaskList=res
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          taskReturn(this.form).then(response => {
            this.$modal.msgSuccess("提交成功！");
            this.$emit('refresh')
            this.open=false;
          });
        }
      });
    },
    cancel(){
      this.open=false
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>
