<template>
  <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
    <!--提交审核-->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="委派人" prop="comment">
            <el-select filterable v-model="form.taskAssignee" placeholder="请选择转办人">
              <el-option
                v-for="item in userList"
                :key="item.userName"
                :label="item.nickName"
                :value="item.userName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="委派意见" prop="comment">
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
import {taskDelegate} from "@/api/lowcode/task/task";
import properties from "@/views/lowcode/task/options/properties";
import store from "@/store";

export default {
  name: "TaskDelegate",
  mixins: [properties],
  data() {
    return {
      title: '委派',
      userList:store.getters.userList
    }
  },
  methods: {
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          taskDelegate(this.form).then(response => {
            this.$modal.msgSuccess("提交成功！");
            this.$emit('refresh')
            this.open = false;
          });
        }
      });
    },
    initData(objectId,objectInstanceId,taskId){
      this.initData_(objectId,objectInstanceId,taskId)
    },
    cancel() {
      this.open=false
    }
  }
}
</script>

<style scoped>

</style>
