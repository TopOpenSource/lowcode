<template>
  <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
    <!--提交审核-->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="驳回意见" prop="comment">
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
import {taskReject} from "@/api/lowcode/task/task";
import properties from "@/views/lowcode/task/options/properties";
export default {
  name: "TaskReturn",
  mixins: [properties],
  data() {
    return {
      title: '驳回',
    }
  },
  methods: {
    initData(objectId,objectInstanceId,taskId){
      this.initData_(objectId,objectInstanceId,taskId)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          taskReject(this.form).then(response => {
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
