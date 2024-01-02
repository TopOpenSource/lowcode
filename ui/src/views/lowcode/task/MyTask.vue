<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="申请标题" prop="comment">
        <el-input
          v-model="queryParams.comment"
          placeholder="请输入申请标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人" prop="applyName">
        <el-select
          v-model="queryParams.applyName"
          placeholder="申请人"
          clearable
          style="width: 240px"
        >
        <el-option v-for="item in userList" :key="item.id" :label="item.nickName" :value="item.userName"/>
        </el-select>
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-s-check" size="mini" @click="handleAdd" >申请</el-button>
      </el-col>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange"
              :default-sort="defaultSort" @sort-change="handleSortChange">
      <!-- <el-table-column type="selection" width="55" align="center"/> -->
      <el-table-column label="申请标题"  prop="comment"/>
      <el-table-column label="申请人"  prop="applyName" :formatter="formatApply"/>
      <el-table-column label="当前环节"  prop="taskName"/>
      <!-- <el-table-column label="待办人"  prop="taskAssignee" :formatter="formatAssignee"/> -->
      <el-table-column label="时间"  prop="applyTime" :formatter="formatDate">
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" width="80px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleProcess(scope.row)" >
            处理
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--业务对象列表-->
    <el-dialog :title="objectSel.title" :visible.sync="objectSel.show" width="50%" append-to-body>
      <el-table :data="objectSel.objectList" style="width: 100%">
        <el-table-column prop="name" label="名称" > </el-table-column>

        <el-table-column label="操作" class-name="small-padding fixed-width" width="80px">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleSubmit(scope.row)" >
              申请
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>
import {parseTime} from "@/utils/ruoyi";
import {pageListMyTasks} from "@/api/lowcode/task/task";
import {listAll} from "@/api/lowcode/config/formObject";
import store from "@/store";
import {listAllUser} from "@/api/system/user";

export default {
  name: "formConfig",
  dicts: ['sys_common_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 用户
      userList: [],
      // 默认排序
      defaultSort: {prop: 'loginTime', order: 'descending'},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      objectSel: {
        title: '选择业务对象',
        show: false,
        objectList: [],
      },
      users:null, //公共用户数据-用于
      orgs:null,  //公共组织数据-
      userMap:store.getters.userMap,
    };
  },
  created() {
    this.listAll();
    this.getList();
  },
  components: {
  },
  methods: {
    /**获取有流程的业务对象**/
    async listAll() {
      this.loading = true;
      await listAll({isFlow: 1}).then(res => {
        this.objectSel.objectList = res.rows;
      });
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      pageListMyTasks(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
      listAllUser().then(res=>{
        this.userList = res.data;
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.infoId)
      this.multiple = !selection.length
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /**新建申请**/
    async handleAdd(){
      // this.listAll();
      await listAll({isFlow: 1}).then(res => {
        this.objectSel.objectList = res.rows;
        this.objectSel.show=true;
      });
    },
    /**新建申请**/
    handleSubmit(row){
      this.objectSel.show=false;
      this.$router.push({ path: '/lowcode/audit', query: { objectId: row.id,deployId:row.deployId,from:{url:this.$route.path} } });
    },
    /** 审核 **/
    handleProcess(row) {
     this.$router.push({ path: '/lowcode/audit',query: { objectId: row.objectId,
                                                          instanceId:row.objectInstanceId,
                                                          from:{ url:'/lowcode/my_task',query:{}}
                                                         } });
    },
    formatDate(row, column) {
      return parseTime(row.applyTime)
    },
    formatApply(row) {
      if(row.applyName!=null){
        let valShow= row.applyName.split(",").map(res=>{
          return this.userMap.get(res);
        })
        return valShow.join();;
      }

    },
    formatAssignee(row) {
      if(row.taskAssignees!=null){
        let valShow= row.taskAssignees.split(",").map(res=>{
          // console.log(this.userMap.get(res));
          return this.userMap.get(res);
        })
        // console.log(valShow);
        return valShow.join();;
      }

    }
  }
};
</script>
