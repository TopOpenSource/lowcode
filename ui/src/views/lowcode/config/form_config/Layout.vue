<template>
  <section v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="24" class="customer-left">
        <section class="customer-bj-item">
          <div class="top-content-title fl">
            <span class="top-title-left">表单页面布局</span>
          </div>
          <div class="fr customer-left-btn">
            <el-button type="success"  size="mini"  icon="el-icon-plus" @click="addFormLayout('0')">添加表单页面布局</el-button>
          </div>
          <el-table :data="formLayoutTableData" style="width: 100%">
            <el-table-column prop="name" label="布局名称" width="180">

            </el-table-column>
            <el-table-column prop="modifiedName" label="调整人">
            </el-table-column>
            <el-table-column prop="gmtModified" label="调整时间" :formatter="dateFormatter">
            </el-table-column>
            <el-table-column label="是否有效">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.enable" :disabled="scope.row.enable" active-color="#13ce66"
                           inactive-color="#ff4949" @change="enableShowLayout(scope.row,'0')">
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column prop="address" label="" class-name="table-operate"  width="150">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="editFormLayout(scope.row.id,'0')">编辑</el-button>
                <el-button type="text" size="small" v-if="!scope.row.enable && scope.row.isDefault=='0'"
                           style="color: red"
                           @click="delShowLayout(scope.row.id)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </section>

        <section class="customer-bj-item">
          <div class="top-content-title fl">
            <span class="top-title-left">列表页面布局</span>
          </div>
          <div class="fr customer-left-btn">
            <el-button type="success"  size="mini"  icon="el-icon-plus" @click="addTableLayout()" v-if="activeName=='tableLayout'">
              添加列表页面布局
            </el-button>
            <el-button type="success"   size="mini"  icon="el-icon-plus" @click="addViewLayout()" v-if="activeName=='viewLayout'">
              添加列表视图
            </el-button>
            <el-button type="success"   size="mini"  icon="el-icon-plus" @click="addRefViewLayout()" v-if="activeName=='rViewLayout'">
              添加关联视图
            </el-button>
          </div>
          <el-tabs style="clear: both" type="card" v-model="activeName">

            <el-tab-pane label="列表布局" name="tableLayout">
              <el-table :data="tableLayoutTableData" style="width: 100%">
                <el-table-column prop="name" label="布局名称" width="180">
                </el-table-column>
                <el-table-column prop="modifiedName" label="调整人">
                </el-table-column>
                <el-table-column prop="gmtModified" label="调整时间" :formatter="dateFormatter">
                </el-table-column>
                <el-table-column label="是否有效">
                  <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" :disabled="scope.row.enable" active-color="#13ce66"
                               inactive-color="#ff4949" @change="enableTableLayout(scope.row,'1')">
                    </el-switch>
                  </template>
                </el-table-column>
                <el-table-column prop="address" label="" class-name="table-operate"  width="150">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editTableLayout(scope.row.id)">编辑</el-button>
                    <el-button type="text" size="small" v-if="!scope.row.enable && scope.row.isDefault=='0'"
                               style="color: red"
                               @click="delTableLayout(scope.row.id)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="列表视图" name="viewLayout">
              <el-table :data="viewLayoutTableData" style="width: 100%">
                <el-table-column prop="name" label="布局名称" width="180">
                </el-table-column>
                <el-table-column prop="modifiedName" label="调整人">
                </el-table-column>
                <el-table-column prop="gmtModified" label="调整时间" :formatter="dateFormatter">
                </el-table-column>
                <el-table-column prop="address" label="" class-name="table-operate"  width="150">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editViewLayout(scope.row.id)">编辑</el-button>
                    <el-button type="text" size="small" v-if="scope.row.enable && scope.row.isDefault=='0'"
                                 style="color: red" @click="delViewLayout(scope.row.id)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="关联视图" name="rViewLayout">
              <el-table :data="refViewLayoutTableData" style="width: 100%">
                <el-table-column prop="name" label="布局名称" width="180">
                </el-table-column>
                <el-table-column prop="modifiedName" label="调整人">
                </el-table-column>
                <el-table-column prop="gmtModified" label="调整时间" :formatter="dateFormatter">
                </el-table-column>
                <el-table-column label="是否有效">
                  <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" :disabled="scope.row.enable" active-color="#13ce66"
                               inactive-color="#ff4949" @change="enableRefViewLayout(scope.row,'1')">
                    </el-switch>
                  </template>
                </el-table-column>
                <el-table-column prop="address" label="" class-name="table-operate"  width="150">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editRefViewLayout(scope.row.id)">编辑</el-button>
                    <el-button type="text" size="small" v-if="scope.row.enable && scope.row.isDefault=='0'" style="color: red" @click="delRefViewLayout(scope.row.id)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

          </el-tabs>
        </section>

        <el-dialog  :close-on-click-modal="false"  :title="dialogFormLayoutEdit.title" :visible.sync="dialogFormLayoutEdit.visable" width="80%">
          <form-layout ref="formLayout" @afterSubmit="afterSubmitFormLayout" @close="closeFormLayout"></form-layout>
        </el-dialog>

        <el-dialog  :close-on-click-modal="false"  :title="dialogViewLayoutEdit.title" :visible.sync="dialogViewLayoutEdit.visable" width="800px">
          <view-layout ref="viewLayout" @afterSubmit="afterSubmitViewLayout" @close="closeViewLayout"></view-layout>
        </el-dialog>

        <el-dialog  :close-on-click-modal="false"  :title="dialogTableLayoutEdit.title" :visible.sync="dialogTableLayoutEdit.visable" width="800px">
          <table_layout ref="tableLayout" @afterSubmit="afterSubmitTableLayout" @close="closeTableLayout"></table_layout>
        </el-dialog>

      </el-col>
    </el-row>
  </section>
</template>

<script>
import {selectFormLayoutByObjectId, enableFormLayout, deleteFormLayout} from "@/api/lowcode/config/form_config/formLayout";
import {selectTableLayoutById, enableTableLayout, deleteTableLayout} from '@/api/lowcode/config/form_config/tableLayout'
import {selectViewLayoutById, enableViewLayout, deleteViewLayout} from '@/api/lowcode/config/form_config/viewLayout'
import DateUtil from "@/views/lowcode/components/pub/DateUtil";
export default {
  data() {
    return {
      loading:false,
      objectId: undefined,
      //表单布局列表
      formLayoutTableData: [],
      //表格布局列表
      tableLayoutTableData: [],
      //视图布局列表
      viewLayoutTableData: [],
      //关联视图
      refViewLayoutTableData: [],
      dialogFormLayoutEdit: {
        title: '',
        visable: false
      },
      dialogViewLayoutEdit: {
        title: '',
        visable: false
      },
      dialogTableLayoutEdit: {
        title: '',
        visable: false
      },
      activeName: 'tableLayout'
    }
  },
  components: {
    'form-layout': () => import("./FormLayoutEdit"),
    'view-layout': () => import("./ViewLayoutEdit"),
    'table_layout': () => import("./TableLayoutEdit")
  },
  methods: {
    dateFormatter(row, column){
      return DateUtil.formatDate(new Date(row[column.property]),"yyyy-MM-dd hh:mm");
    },
    //新增展示布局
    addFormLayout(type) {
      this.dialogFormLayoutEdit.title = type == "1" ? "新建详情页面布局" : "新建表单页面布局";
      this.dialogFormLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.formLayout.loadData(null, type, this.objectId);
      });
    },
    //修改展示布局
    editFormLayout(id, type) {
      this.dialogFormLayoutEdit.title = type == "1" ? "修改详情页面布局" : "修改表单页面布局";
      this.dialogFormLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.formLayout.loadData(id, type, this.objectId);
      });
    },
    //删除展示布局
    delShowLayout(id) {
      this.$confirm("此操作将删除所选数据, 是否继续?", "提示", {
        distinguishCancelAndClose: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        this.loading=true;
        //删除
        deleteFormLayout({id: id}).then(res => {
          this.$notify({message: '删除成功', type: 'success'});
          this.loadFormLayout().then(res => {
            this.loading=false;
          }).catch(error => {
            this.loading=false;
          });
        });
      }).catch(action => {
      });
    },
    //启用展示布局
    enableShowLayout(obj, type) {
      this.loading=true;
      //启用
      enableFormLayout({id: obj.id, objectId: obj.objectId, type: type}).then(res => {
        if (type == "0") {
          //加载表单数据
          this.loadFormLayout().then(res => {
            this.loading=false;
          }).catch(error => {
            this.loading=false;
          });
        }

      }).catch(error => {
        this.loading=false;
      });
    },
    //新增或修改完成后的操作
    afterSubmitFormLayout(type) {
      this.dialogFormLayoutEdit.visable = false;
      if(type=='0'){
        this.loadFormLayout();
      }
    },
    //删除展示布局
    delTableLayout(id) {
      this.$confirm("此操作将删除所选数据, 是否继续?", "提示", {
        distinguishCancelAndClose: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        this.loading=true;
        //删除
        deleteTableLayout({id: id}).then(res => {
          this.$notify({message: '删除成功', type: 'success'});
          //加载数据
          this.loadTabeLayout().then(res => {
            this.loading=false;
          }).catch(error => {
            this.loading=false;
          });
        });
      }).catch(action => {

      });
    },
    //启用展示布局
    enableTableLayout(obj) {
      this.loading=true;
      //启用
      enableTableLayout({id: obj.id, objectId: obj.objectId}).then(res => {
        //加载数据
        this.loadTabeLayout().then(res => {
          this.loading=false;
        }).catch(error => {
          this.loading=false;
        });
      }).catch(error => {
        this.loading=false;
      });
    },
    addViewLayout() {
      this.dialogViewLayoutEdit.title = "新建列表视图";
      this.dialogViewLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.viewLayout.loadData(this.objectId, null,"0");
      });
    },
    editViewLayout(id) {
      this.dialogViewLayoutEdit.title = "修改列表视图";
      this.dialogViewLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.viewLayout.loadData(this.objectId, id,"0");
      });
    },
    afterSubmitViewLayout() {
      this.dialogViewLayoutEdit.visable = false;
      this.loadViewLayout()
      this.loadRefViewLayout()
    },
    closeViewLayout() {
      this.dialogViewLayoutEdit.visable = false;
    },
    closeTableLayout() {
      this.dialogTableLayoutEdit.visable = false;
    },
    closeFormLayout(){
      this.dialogFormLayoutEdit.visable = false;
    },
    addTableLayout() {
      this.dialogTableLayoutEdit.title = "新建列表布局";
      this.dialogTableLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.tableLayout.loadData(null, this.objectId);
      });
    },
	  editTableLayout(id){
      this.dialogTableLayoutEdit.title = "编辑列表布局";
      this.dialogTableLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.tableLayout.loadData(id, this.objectId);
      });
    },
    afterSubmitTableLayout() {
      this.dialogTableLayoutEdit.visable = false;
      this.loadTabeLayout();
    },
    //启用视图布局
    enableViewLayout(obj, type) {
      this.loading=true;
	    let enable=obj.enable?"1":"0";
      enableViewLayout({id: obj.id, objectId: obj.objectId, enable:enable}).then(res => {
        this.loadViewLayout().then(res => {
          this.loading=false;
        }).catch(error => {
          this.loading=false;
        });
      }).catch(error => {
        this.loading=false;
      });
    },
    //删除视图布局
    delViewLayout(id) {
      this.$confirm("此操作将删除所选数据, 是否继续?", "提示", {
        distinguishCancelAndClose: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        this.loading=true;
        deleteViewLayout({id: id}).then(res => {
          this.$notify({message: '删除成功', type: 'success'});
          this.loadViewLayout().then(res => {
            this.loading=false;
          }).catch(error => {
            this.loading=false;
          });
        });
      }).catch(action => {

      });
    },
    //加载表单布局
    loadFormLayout() {
      return selectFormLayoutByObjectId({objectId: this.objectId, type: "0"}).then(res => {
        res.forEach(dto => {
          dto.enable = dto.enable == '1' ? true : false;
        });
        this.formLayoutTableData = res;
      });
    },
    //加载列表布局
    loadTabeLayout() {
      return selectTableLayoutById({objectId: this.objectId}).then(res => {
        res.forEach(dto => {
          dto.enable = dto.enable == '1' ? true : false;
        });
        this.tableLayoutTableData = res;
      });
    },
    //加载视图布局
    loadViewLayout() {
      return selectViewLayoutById({objectId: this.objectId, type: "0"}).then(res => {
        res.forEach(dto => {
          dto.enable = dto.enable == '1' ? true : false;
        });
        this.viewLayoutTableData = res;
      });
    },
    //加载关联视图布局
    loadRefViewLayout() {
      return selectViewLayoutById({objectId: this.objectId, type: "-1"}).then(res => {
        res.forEach(dto => {
          dto.enable = dto.enable == '1' ? true : false;
        });
        this.refViewLayoutTableData = res;
      });
    },
    addRefViewLayout(){
      this.dialogViewLayoutEdit.title = "新建关联视图";
      this.dialogViewLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.viewLayout.loadData(this.objectId, null,"-1");
      });
    },
    editRefViewLayout(id){
      this.dialogViewLayoutEdit.title = "修改关联视图";
      this.dialogViewLayoutEdit.visable = true;
      this.$nextTick(function () {
        this.$refs.viewLayout.loadData(this.objectId, id,"-1");
      });
    },
    delRefViewLayout(id) {
      this.$confirm("此操作将删除所选数据, 是否继续?", "提示", {
        distinguishCancelAndClose: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      }).then(() => {
        this.loading=true;
        deleteViewLayout({id: id}).then(res => {
          this.$notify({message: '删除成功', type: 'success'});
          this.loadRefViewLayout().then(res => {
            this.loading=false;
          }).catch(error => {
            this.loading=false;
          });
        });
      }).catch(action => {

      });
    },
    //启用视图布局
    enableRefViewLayout(obj, type) {
      this.loading=true;
      let enable=obj.enable?"1":"0";
      enableViewLayout({id: obj.id, objectId: obj.objectId, enable:enable,type:"-1"}).then(res => {
        this.loadRefViewLayout().then(res => {
          this.loading=false;
        }).catch(error => {
          this.loading=false;
        });
      }).catch(error => {
        this.loading=false;
      });
    },
    //加载数据
    async loadData(objectId) {
      this.loading=true;
      this.objectId=objectId;
      //加载表单布局
      await this.loadFormLayout();
      //加载表格布局
      await this.loadTabeLayout();
      //加载视图
      await this.loadViewLayout();
      //加载关联视图
      await this.loadRefViewLayout();
      this.loading=false;
    }
  },
  mounted() {
  }
}
</script>

<style scoped>


</style>
