<template>
  <!--任务节点配置-->
  <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body type="card" class="dialog-task">
    <div class="app-container" style="padding: 0px 0px 0px 0px">
        <el-tabs>
          <el-tab-pane label="任务信息" name="0">
              <assignee ref="assigneePanel" :modeler="modeler" :element="element" :objectId="objectId" :config="config" ></assignee>
          </el-tab-pane>
          <el-tab-pane label="审核按钮" name="1">
             <check-btn ref="checkBtn" :modeler="modeler" :element="element" :objectId="objectId" :config="config" ></check-btn>
          </el-tab-pane>
          <el-tab-pane label="表单显示" name="2">
              <form-show ref="formShowPannel" :modeler="modeler" :element="element" :objectId="objectId" :config="config"></form-show>
          </el-tab-pane>

          <el-tab-pane label="通知配置" name="3">
              <notice ref="notice" :modeler="modeler" :element="element" :objectId="objectId" :config="config"></notice>
          </el-tab-pane>
        </el-tabs>
    </div>
    <taskListeners ref="listeners" :modeler="modeler" :element="element" :objectId="objectId" :config="config" ></taskListeners>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" @click="save">保存</el-button>
      <el-button size="small" @click="cancel">取 消</el-button>
    </div>

  </el-dialog>
</template>

<script>
import assignee from './assignee'
import checkBtn from "./checkBtn"
import formShow from "./formShow";
import notice from "./notice";
import taskListeners from "./taskListeners";
import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";

export default {
  name: "TaskDesigner",
  mixins: [mixinProperties],
  data() {
    return {
      title: '',
      open: false,
    }
  },
  components: {
    'assignee':assignee,
    'checkBtn':checkBtn,
    'formShow':formShow,
    'notice':notice,
    'taskListeners':taskListeners
  },
  methods:{
    initData(){
      this.$nextTick(() => {
        this.$refs.assigneePanel.initData();
        this.$refs.formShowPannel.initData();
        this.$refs.checkBtn.initData();
        this.$refs.notice.initData();
        this.$refs.listeners.initData();

      });
    },
    save(){
      //任务节点保存
      let result1=this.$refs.assigneePanel.save()
      let result2=this.$refs.checkBtn.save()
      let result3=this.$refs.formShowPannel.save()
      let result4=this.$refs.notice.save()

      if(result1 && result2 && result3 && result4){
        this.$modal.msgSuccess("任务节点配置成功！");
        this.open = false;
      }
    },
    cancel(){
      this.open = false;
    }
  }
}
</script>

<style scoped>

</style>
