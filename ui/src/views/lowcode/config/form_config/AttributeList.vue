<template>
  <section>
    <el-row :gutter="20">
      <el-col :span="24" class="customer-left">
        <section class="customer-bj-item">
          <div class="top-content-title fl">
            <span class="top-title-left">自定义字段</span>
          </div>
          <div class="fr customer-left-btn">
            <el-button type="success" size="mini" icon="el-icon-plus" @click="addAttributeMX">批量添加字段</el-button>
            <el-button type="success" size="mini" icon="el-icon-plus" @click="addAttribute">添加字段</el-button>
          </div>
          <el-table :data="customizedAttrs" style="width: 100%">
            <el-table-column prop="name" label="字段名称">
              <template slot-scope="{row}">
                <span>{{row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="typeName" label="字段类型" />
            <el-table-column prop="enable" label="状态">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.enable" active-value="1" inactive-value="0" active-color="#13ce66" inactive-color="#ff4949" @change="toggleAttributeEnable(scope.row, scope.$index)"></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="" class-name="table-operate" width="150">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="editAttribute(scope.row, true)">编辑</el-button>
                <el-button type="text" size="small" @click="deleteAttribute(scope.row)" style="color: red">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </section>

        <section class="customer-bj-item">
          <div class="top-content-title">
            <span>系统标准字段</span>
          </div>
          <el-table :data="defaultAttrs" style="width: 100%">
            <el-table-column prop="name" label="字段名称">
              <template slot-scope="{row}">
                <span>{{row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="typeName" label="字段类型"></el-table-column>
            <!-- <el-table-column label="" class-name="table-operate"  width="150">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="editAttribute(scope.row, false)">编辑</el-button>
              </template>
            </el-table-column> -->
          </el-table>
        </section>
      </el-col>
    </el-row>

    <!--新增/编辑字段-->
    <el-dialog  :close-on-click-modal="false"  :title="attrEdit.title" :visible.sync="attrEdit.visable" @open="openAttrEditDialog" @close="closeAttrEditDialog" :close-on-press-escape="false" width="70%">
       <attribute-edit ref="attributeEdit" :option="attrEdit" @close="closeAttrEditDialog" @error="closeAttrEditDialog" @success="actionSuccess"></attribute-edit>
    </el-dialog>

    <el-dialog  :close-on-click-modal="false"  :title="attrEditMX.title" :visible.sync="attrEditMX.visable" @open="openAttrEditMXDialog" @close="closeAttrEditMXDialog" :close-on-press-escape="false" width="90%">
       <attribute-edit-mx ref="attributeEditMX" :option="attrEditMX" @close="closeAttrEditMXDialog" @error="closeAttrEditMXDialog" @success="actionSuccessMX"></attribute-edit-mx>
    </el-dialog>

  </section>
</template>

<script>
import { Loading } from 'element-ui';
import { listFormObjectAttribute, toggleAttributeEnable, deleteAttr } from "@/api/lowcode/config/form_config/attribute";

const default_0 = '0';  // 自定义字段
const default_1 = '1';  // 系统默认字段
export default {
  data() {
    return {
      // 业务对象id
      objectId: undefined,
      // 自定义字段列表
      customizedAttrs: [],
      // 系统字段列表
      defaultAttrs: [],
      // 新增或编辑字段属性
      attrEdit: {
        title: '添加自定义字段',
        visable: false,
        isEdit: false,
        isDefault: false,
        id: null,
        objectId: null,
      },
      attrEditMX: {
        title: '添加自定义字段',
        visable: false,
        isEdit: false,
        isDefault: false,
        id: null,
        objectId: null,
      }
    }
  },
  components: {
    'attribute-edit': () => import("./AttributeEdit"),
    'attribute-edit-mx': () => import("./AttributeEditMX")
  },
  methods: {
    // 添加自定义字段
    addAttribute(){
      this.attrEdit.title = '新建自定义字段';
      this.attrEdit.visable = true;
      this.attrEdit.isEdit = false;
      this.attrEdit.isDefault = false;
      this.attrEdit.id = null;
      this.attrEdit.objectId = this.objectId;
    },
    addAttributeMX(){
      this.attrEditMX.title = '新建自定义字段';
      this.attrEditMX.visable = true;
      this.attrEditMX.isEdit = false;
      this.attrEditMX.isDefault = false;
      this.attrEditMX.id = null;
      this.attrEditMX.objectId = this.objectId;
    },

    // 编辑自定义字段属性
    editAttribute(attr, isSelf){
      this.attrEdit.title = isSelf ? '编辑自定义字段':'编辑系统标准字段';
      this.attrEdit.visable = true;
      this.attrEdit.isEdit = true;
      this.attrEdit.isDefault = attr.isDefault == default_1;
      this.attrEdit.id = attr.id;
      this.attrEdit.objectId = this.objectId;
    },

    deleteAttribute(row){
      deleteAttr(row).then((res)=>{
        this.loadAttrs('0');
        this.$message.success("删除成功")
      })
    },

    // 打开窗口时动作
    openAttrEditDialog(){
      this.$nextTick(()=>{
        this.$refs['attributeEdit'].loadData();
      })
    },
    openAttrEditMXDialog(){
      this.$nextTick(()=>{
        this.$refs['attributeEditMX'].loadData();
      })
    },

    // 关闭窗口的方法
    closeAttrEditDialog(){
      this.attrEdit.visable = false;
      this.attrEdit.isEdit = false;
      this.attrEdit.isDefault = false;
      this.attrEdit.id = null;
      this.attrEdit.objectId = this.objectId;

      this.$nextTick(()=>{
        this.$refs.attributeEdit.clear();
      })
    },
    closeAttrEditMXDialog(){
      this.attrEditMX.visable = false;
      this.attrEditMX.isEdit = false;
      this.attrEditMX.isDefault = false;
      this.attrEditMX.id = null;
      this.attrEditMX.objectId = this.objectId;

      this.$nextTick(()=>{
        this.$refs.attributeEditMX.clear();
      })
    },

    // 操作成功方法
    actionSuccess(){
      let isDefault = this.attrEdit.isDefault ? default_1 : default_0;
      this.closeAttrEditDialog();
      this.loadAttrs(isDefault);
    },

    actionSuccessMX(){
      let isDefault = this.attrEditMX.isDefault ? default_1 : default_0;
      this.closeAttrEditMXDialog();
      this.loadAttrs(isDefault);

    },

    // 复制字段属性
    copyAttribute(attr){
      alert("功能开发中。。。")
    },

    // 移除/恢复 字段
    toggleAttributeEnable(attr, index) {
      let message = attr.enable == '0' ? '移除该字段后，其所在的所有“布局”中该字段也将被移除，是否继续？' : '恢复该字段后，该字段对应的业务数据也将被恢复，是否继续？';
      this.$confirm(message, '提示', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        this.toggleAttr(attr);
      }).catch(_=>{
        attr.enable = attr.enable == '0'?'1':'0';
      });
    },

    async toggleAttr(attr){
      if (!!attr.id) {
        await this.toggleAttribute(attr);
        await this.reloadAttrs(attr.isDefault);
      }
    },

    // 后台 移除/恢复字段
    toggleAttribute(attr){
      let succMessage = attr.enable == '0' ? '字段已移除成功！' : '字段已恢复成功，未在业务对象中生效，请前往“布局”对该字段进行设置！';
      let faileMessage = attr.enable == '0' ? '字段移除失败，请稍后再试！' : '字段恢复失败，请稍后再试！';
      return toggleAttributeEnable({
        id: attr.id,
        objectId: attr.objectId,
        enable: attr.enable,
      }).then(res=>{
        if(res.result == 0){
          this.$notify({type: "success",message: succMessage,});
        }else{
          this.$notify({type: "error",message: faileMessage,});
        }
      }).catch(_=>{
        console.error(_);
        this.$notify({type: "error",message: faileMessage,});
      });
    },

    // 字段状态
    enableFormatter(row, column, cellValue, index){
      return row.enable == '1' ? '正常' : '已移除';
    },

    // 重新加载字段列表
    async reloadAttrs(isDefault){
      let loading = Loading.service({fullscreen: true});
      await this.loadAttrs(isDefault).catch(_ => {
        loading.close();
      });
      loading.close();
    },

    // 获取对应字段列表数据
    loadAttrs(isDefault){
      return listFormObjectAttribute({
        isDefault: isDefault,
        objectId: this.objectId,
      }).then((res)=>{
        if(isDefault == default_1){
          this.defaultAttrs = res;
        }else if(isDefault == default_0){
          this.customizedAttrs = res;
        }
      });
    },

    // 加载字段列表
    async loadData(objectId) {
      this.objectId=objectId;
      let loading = Loading.service({fullscreen: true});
      await this.loadAttrs(default_0).catch(_ => {
        loading.close();
      });
      await this.loadAttrs(default_1).catch(_ => {
        loading.close();
      });
      loading.close();
    },
  },
  mounted() {
    //this.loadData();
  }
}
</script>

<style scoped>
.top-content-title{
/*  padding-top: 5px;*/
  padding-bottom: 10px;
  font-size: 15px;
  font-weight: bold;
  color: #3D5266;

}

.top-content-title span{
  border-left: 5px solid #438AFE;
  padding-left: 15px;
  border-radius: 2px;
}

</style>
