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
      <!-- <el-form-item label="申请人" prop="applyName">
        <el-select
          v-model="queryParams.applyName"
          placeholder="申请人"
          clearable
          style="width: 240px"
        >
        <el-option v-for="item in userList" :key="item.id" :label="item.nickName" :value="item.userName"/>
        </el-select>
      </el-form-item> -->
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

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center"/> -->
      <el-table-column label="申请标题"  prop="comment"/>
      <!-- <el-table-column label="申请人"  prop="applyName" :formatter="formatApply"/> -->
      <el-table-column label="时间"  prop="createTime" :formatter="formatDate">
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" width="80px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleProcess(scope.row)" >
             提交
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


  </div>
</template>

<script>
import {parseTime} from "@/utils/ruoyi";
import {myUnSubmitTask} from "@/api/lowcode/task/task";
import store from "@/store";
import {listAllUser} from "@/api/system/user";

export default {
  name: "unsubmit",
  data() {
    return {
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
      // 日期范围
      dateRange: [],
      // 用户
      userList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      users:null, //公共用户数据-用于
      orgs:null,  //公共组织数据-
      userMap:store.getters.userMap,
    };
  },
  created() {
    this.getList();
  },
  components: {
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      myUnSubmitTask(this.addDateRange(this.queryParams,this.dateRange)).then(response => {
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
    /** 提交 **/
    handleProcess(row) {
      this.$router.push({ path: '/lowcode/audit', query: { objectId: row.objectId,instanceId:row.objectInstanceId,from:{ url:'/lowcode/unsubmit_task',query:{}} }});
    },
    formatDate(row, column) {
      return parseTime(row.createTime);
    },
    formatApply(row) {
      if(row.applyName!=null){
        let valShow= row.applyName.split(",").map(res=>{
          return this.userMap.get(res);
        })
        return valShow.join();;
      }
    },
  }
};
</script>
