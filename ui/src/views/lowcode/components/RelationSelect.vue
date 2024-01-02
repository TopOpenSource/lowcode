<template>
  <el-dialog :title="title" :visible.sync="show" width="60%" append-to-body>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true"  label-width="68px">
        <condtions ref="conditions" :objectId="objectId" ></condtions>
          <el-button icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
      </el-form>

      <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <template v-for="col in columns">
          <el-table-column :label="col.name" align="center">
            <template slot-scope="{row, column}">
              {{ formatterVal(col, row, column) }}
            </template>
          </el-table-column>
        </template>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>

import Util from "@/views/lowcode/task/Util";

let util = new Util();
import store from "@/store";
import {pageList} from "@/api/lowcode/instance/instance"
import AttributeConstant from "@/views/lowcode/components/pub/AttributeConstant";
import condtions from "@/views/lowcode/config/form_config/Conditions"
export default {
  name: "formConfig",
  dicts: ['sys_common_status'],
  data() {
    return {
      title:'业务对象选择',
      show: false,
      objectId:null,
      // 遮罩层
      loading: true,
      // 选中数组
      rows: [],
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        queryCriteria:null,
      },
      //显示列
      columns:[],
      //属性 所有的
      attrs:[],
      //字典
      dictMap:store.getters.dictMap,
      //用户
      userMap:store.getters.userMap,
      AttributeConstant:AttributeConstant
    };
  },
  created() {

  },
  components:{
    condtions
  },
  methods: {
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
      this.rows = selection
    },

    loadView(objectId,view,attrs){
      this.$nextTick(() => {
        this.objectId=objectId

        this.queryParams.viewId=view.id
        this.attrs= attrs
        //初始化查询条件
        this.$refs.conditions.initData()
        this.columns=util.parseCols2Attrs(this.attrs,view.tableColumn)
        //加载数据
        this.getList()
      })
    },
    //table值显示
    formatterVal(attr, row){
       return util.formatterVal(attr,row,this.dictMap,this.userMap)
    },
    handleSubmit(){
      this.$emit("select",this.rows)
      this.show=false
    }
  }
};
</script>
