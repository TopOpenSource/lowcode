<template>
  <section class="top-content-pad">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form ref="ruleForm" :rules="rules" label-width="110px" :model="ruleForm">
          <input type="text" style="display: none;">
          <el-row :gutter="24">
            <el-col :sm="21" :md="21" :lg="21">
              <el-form-item label="列表布局名称" prop="name" :rules="rules.notnull">
                <el-input placeholder="请输入列表布局名称" maxlength="50" v-model="ruleForm.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
      <el-col :span="24">
        <div class="top-content-title">
          <span>选择列表视图</span>
        </div>
        <transfer v-model="viewList.value" :data="viewList.data" :props="viewList.props" :titles="['未选择视图', '已选择视图']"></transfer>
      </el-col>
    </el-row>
    <div class="footer-icon">
      <el-button @click="close" size="medium">取消</el-button>
      <el-button type="primary" @click="submit" size="medium" class="el-button--orange">保存</el-button>
    </div>
  </section>

</template>

<script>

import {selectViewLayoutById} from "@/api/lowcode/config/form_config/viewLayout";
import {insertOrUpdateTableLayout, selectDtoById} from "@/api/lowcode/config/form_config/tableLayout";
import Transfer from '@/views/lowcode/components/Transfer';

export default {
  data() {
    return {
      loading: false,
      viewList: {
        data: [],
        props: {
          key: "id",
          label: "name", // 标签显示
        },
        value: []
      },
      data: [],
      value: [],
      ruleForm: {
        id: '',
        name: '',
        objectId: '',
        oldViewIds: []
      },
      rules: {
        notnull: {required: true, message: '请输入列表布局名称', trigger: 'blur'},
      }

    };
  },

  methods: {
    async loadData(id, objectId) {
      this.clearValidate()
      this.cleanData()
      this.ruleForm.objectId = objectId
      selectViewLayoutById({objectId: objectId, type: '0'}).then(res => {
        this.viewList.data = res
      })
      if (id) {
        selectDtoById({id: id}).then(res => {
          this.viewList.value = res.viewIds
          console.info(this.viewList.value)
          this.ruleForm.id = id
          this.ruleForm.name = res.name
          this.ruleForm.oldViewIds = res.viewIds
        })
      }
    },
    cleanData() {
      this.viewList.data = []
      this.viewList.value = []
      this.ruleForm.name = ''
      this.ruleForm.objectId = ''
      this.ruleForm.id = ''
      this.ruleForm.oldViewIds = []
    },
    close() {
      this.$emit("close");
    },
    submit() {
      this.loading = true;
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (this.viewList.value.length == 0) {
            this.$notify.error({
              message: '请选择列表视图'
            });
            this.loading = false;
          } else {
            let para = {
              id: this.ruleForm.id,
              name: this.ruleForm.name,
              objectId: this.ruleForm.objectId,
              viewIds: this.viewList.value,
              oldViewIds: this.ruleForm.oldViewIds
            }
            insertOrUpdateTableLayout(para).then(res => {
              if (res.result == 0) {
                this.$notify({
                  message: this.ruleForm.id == '' ? '添加成功' : '编辑成功',
                  type: 'success'
                });
                this.loading = false;
                this.$emit("afterSubmit");
                this.close()
              } else {
                this.$notify.error({
                  message: this.ruleForm.id == '' ? '添加失败' : '编辑失败',
                });
                this.loading = false;
              }
            }).catch(err => {
              this.loading = false;
            })
          }
        } else {
          this.loading = false;
        }
      });

    },
    clearValidate() {
      this.$nextTick(function () {
        this.$refs["ruleForm"].clearValidate();
      });
    },


  },
  components: {
    Transfer
  },
  mounted() {

  }
};
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
