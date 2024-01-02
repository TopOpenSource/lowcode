<template>
  <div class="app-container">
  <el-form :model="queryParams" ref="queryForm" size="small"  label-width="50px">
    <el-row>
      <el-col :span="22">
        <condtions ref="conditions" :objectId="objectId" style="padding-left: 12px"></condtions>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="6">
          <el-form-item label="视图" prop="status">
            <el-select v-model="queryParams.viewId"  style="" @change="handleChangeView">
              <el-option v-for="view in views" :key="view.id" :label="view.name" :value="view.id" />
            </el-select>
          </el-form-item>
      </el-col>
      <el-col :span="6" v-if="flow.processDefinitionId!=null">
          <el-form-item label="任务" prop="status">
            <el-select v-model="queryParams.queryFlow">
              <el-option  label="全部" value="0" />
              <el-option  label="待办" value="1" />
              <el-option  label="已办" value="2" />
            </el-select>
          </el-form-item>
      </el-col>
      <el-col :span="2">
        <el-form-item label-width="20px">
            <el-button  type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-col>
    </el-row>

  </el-form>


    <!--操作栏   -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success"  plain icon="el-icon-plus" size="mini"  @click="handleAdd">新增</el-button>

        <el-button type="danger"  plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" >删除</el-button>
      </el-col>

      <div class="top-right-btn">
        <el-row>
          <el-tooltip class="item" effect="dark" content="刷新" placement="top">
            <el-button size="mini" circle icon="el-icon-refresh" @click="refresh" />
          </el-tooltip>
        </el-row>
      </div>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" v-if="!noTable">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="操作" align="center"  width="100">
        <template slot-scope="scope">

          <el-button size="mini" type="text"  @click="handleProcess(scope.row)" v-if="flow.processDefinitionId!=null">
            处理
          </el-button>

          <el-button size="mini" type="text"  @click="handleEdit(scope.row)" v-else>
            编辑
          </el-button>

        </template>
      </el-table-column>



      <template v-if="flow.processDefinitionId!=null">
        <el-table-column label="当前环节" width="100">
          <template slot-scope="{row, column}">
            {{row.taskName}}
          </template>
        </el-table-column>
        <el-table-column label="待办人" width="200">
          <template slot-scope="{row, column}">
            {{formatterUsers(row.taskAssignees)}}
          </template>
        </el-table-column>
      </template>

      <template v-for="col in columns">
        <el-table-column :label="col.name">
          <template slot-scope="{row, column}">
            {{ formatterVal(col, row, column) }}
          </template>
        </el-table-column>
      </template>
    </el-table>

    <el-empty description="请配置列表页面布局" v-if="noTable"></el-empty>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>

</template>

<script>

import Util from "@/views/lowcode/task/Util";

let util = new Util();
import store from "@/store";

import {selTable,pageList,delInstBatch} from "@/api/lowcode/instance/instance"
import AttributeConstant from "@/views/lowcode/components/pub/AttributeConstant";
import condtions from "@/views/lowcode/config/form_config/Conditions"
import {getFlowObject} from "@/api/lowcode/config/flow_config/processdefinition";
import {hasEnableForm} from "../config/form_config/formLayout";

export default {
  name: "formConfig",
  dicts: ['sys_common_status'],
  data() {
    return {
      noTable: true,
      objectId:null,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        viewId:null,
        //高级查询条件
        queryCriteria:null,
        queryFlow:null,
      },
      //显示列
      columns:[],
      //属性 所有的
      attrs:[],
      //视图
      views:[],
      //字典
      dictMap:store.getters.dictMap,
      //用户
      userMap:store.getters.userMap,
      //属性类型常量
      AttributeConstant:AttributeConstant,
      //流程信息
      flow: {
        processDefinitionId:null,
        deploymentId:null,
      }
    };
  },
  created() {
    //this.getList();
  },
  components:{
    condtions
  },
  mounted() {
    this.refresh();
  },
  methods: {
    refresh(){
      const query = this.$route.query;
      if (query) {
        this.objectId = query.formObjectId;
        //加载流程信息
        this.loadWorkFlow();
        //加载表格
        this.loadTable();
      }
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      pageList("?pageNum="+this.queryParams.pageNum+"&pageSize="+this.queryParams.pageSize,this.queryParams).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      //验证高级查询条件
      this.$refs.conditions.valid().then(valid=>{
        if(valid){
          //验证高级查询追加
          this.queryParams.queryCriteriaAppend=this.$refs.conditions.getValue()
          this.getList();
        }
      });

    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let $this=this
      this.$modal.confirm('是否确认删除？').then(function() {
       return  delInstBatch({objectId:$this.objectId,ids:$this.ids})
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});

    },
    //切换视图
    handleChangeView(){
      let view=this.views.filter(view=>(view.id===this.queryParams.viewId))[0]
      this.loadView(view)
    },
    //新增-无流程
    handleAdd(){
      hasEnableForm({objectId: this.objectId, type: '0'}).then(res => {
        if (res) {
          if(this.flow.processDefinitionId!=null){
            this.$router.push({ path: '/lowcode/audit', query: { objectId: this.objectId,deployId:this.flow.deploymentId,from: {url:'/lowcode/front_list',query:{formObjectId: this.objectId} }} });
          }else{
            this.$router.push({ path: '/lowcode/inst_edit', query: { objectId: this.objectId } });
          }
        } else {
          this.$message.error('请配置表单页面布局')
        }
      }).catch(err=>{

      });
    },
    //编辑-无流程
    handleEdit(row){
      this.$router.push({ path: '/lowcode/inst_edit', query: { objectId: this.objectId,instanceId:row.id } });
    },
    //查看和审核
    handleProcess(row){
      this.$router.push({ path: '/lowcode/audit', query: { objectId: this.objectId,instanceId:row.id,from: {url:'/lowcode/front_list',query:{formObjectId: this.objectId}}  } });
    },
    //加载表格
    loadTable(){
      selTable(this.objectId).then(res=>{
          if(res){
            this.noTable = false;
            if(res.views[0]){
              this.views=res.views
              //获取默认视图
              this.queryParams.viewId=this.views[0].id
              this.attrs=res.attrs
              //初始化查询条件
              this.$refs.conditions.initData()
              this.loadView(this.views[0])
            }else{
              this.$modal.msgError("未配置或没有启用的视图布局！");
              this.loading=false
            }
          }else{
            this.noTable = true;
            this.$modal.msgError("未配置或没有启用的列表布局！");
            this.loading=false
          }
      });
    },
    loadView(view){
      this.columns=util.parseCols2Attrs(this.attrs,view.tableColumn)
      //加载数据
      this.getList()
    },
    //加载workFlow
    loadWorkFlow(){
      getFlowObject(this.objectId).then(res=>{
        if(res.data){
          this.flow.processDefinitionId=res.data.processDefinitionId
          this.flow.deploymentId=res.data.deploymentId

          this.queryParams.queryFlow="0"
        }

      })
    },
    //table值显示
    formatterVal(attr, row, column){
      return util.formatterVal(attr,row,this.dictMap,this.userMap)
    },
    //table-user展示
    formatterUsers(usersStr){
      if(usersStr!=undefined){
        let valShow= usersStr.split(",").map(res=>{
          return  this.userMap.get(res)
        })
        return valShow.join()
      }else{
        return ""
      }
    }
  }
};
</script>
