<template>
  <div class="app-container">
    <div class="my-card">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="名称"  prop="name" />
      <!-- <el-table-column label="表名"  prop="mainTableName" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" /> -->
      <el-table-column label="分类"  prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.lowcode_object_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="是否包含流程"  prop="isFlow">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no_01" :value="scope.row.isFlow"/>
        </template>
      </el-table-column>
      <el-table-column label="上线/下线"  prop="isFlow">
        <template slot-scope="scope">
          <el-switch active-value="0" inactive-value="1" v-model="scope.row.state" @change="handleUpdateState(scope.row)" active-color="#13ce66" inactive-color="#ff4949"></el-switch>

        </template>
        <!-- <template slot-scope="scope" v-else>
          --
        </template> -->
      </el-table-column>
      <el-table-column label="创建时间"  prop="gmtCreate"  width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtCreate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间"  prop="gmtModified" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtModified) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDesForm(scope.row)">设计表单</el-button>
          <el-divider direction="vertical"></el-divider>
          <template v-if="scope.row.isFlow==1">
            <el-button size="mini" type="text" @click="handleDesFlow(scope.row)">设计流程</el-button>
            <el-divider direction="vertical"></el-divider>
          </template>
          <el-button size="mini" type="text" @click="handleEnter(scope.row)">进入</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button size="mini" type="text" @click="handleEidt(scope.row)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button style="color: red" size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
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


    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col :span="18">
            <el-form-item label="表名" prop="mainTableName">
              <el-input v-model="form.mainTableName"  />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row>
          <el-col :span="18">
            <el-form-item label="类别" prop="type">
              <el-select v-model="form.type" placeholder="请选择类别">
                <el-option
                  v-for="dict in dict.type.lowcode_object_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-form-item label="包含流程" prop="isFlow">
              <el-radio-group v-model="form.isFlow">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no_01"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="菜单图标" prop="icon">
              <el-upload
                class="avatar-uploader"
                accept=".jpg,.jpeg,.png,.bmp,.gif"
                size="small"
                ref="upload"
                :show-file-list=false
                :action="upload.url"
                :headers="upload.headers"
                :on-success="handleIconSuccess"
                :auto-upload="true">
                <img v-if="this.form.icon" :src="this.form.icon" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <!-- <el-button slot="trigger" size="small" type="primary">选取文件</el-button> -->
                <!-- <div slot="tip" class="el-upload__tip">请上传图片</div> -->
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import {list, saveOrUpdate, deleteObj, updateObjState} from "@/api/lowcode/config/formObject";

export default {
  name: "formConfig",
  dicts: ['sys_yes_no_01','lowcode_object_type'],

  data() {
    return {
      upload: {
        isUploading: false,// 是否禁用上传
        showFileList:false,//是否显示文件列表
        headers: { Authorization: "Bearer " + getToken() },// 设置上传的请求头部
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/lowcode/form/sysFormObject/uploadIcon",
      },
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
      // 默认排序
      defaultSort: {prop: 'gmtModified', order: 'descending'},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        mainTableName: [
          { required: true, message: "表名不能为空", trigger: "blur" },
          { pattern: /^[a-z][a-z0-9_]*$/ , message: '以小写英文字母开头，且只能包含英文字母、数字、下划线', trigger: ['blur', 'change']}
        ],
        isFlow: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        icon: [{ required: true, message: "不能为空", trigger: "blur" }]
      }

    };
  },
  created() {
    this.getList();
  },
  methods: {
    //上传成功处理
    handleIconSuccess(res){
      // console.log(res);
      if(res.code == 500){
        this.$message.error(res.msg);
      }else{
        this.form.icon = res.url;
        this.$message.success("上传成功");
      }
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: undefined,
        // mainTableName: undefined,
        isFlow: undefined,
        type: undefined,
        icon: undefined
      };
      this.resetForm("form");
    },
    //新增
    handleAdd(){
      this.reset();
      this.open = true;
      this.title = "新增业务对象";
    },
    //编辑
    handleEidt(row){
      this.reset();
      this.form = {
        id: row.id,
        name: row.name,
        isFlow: row.isFlow,
        type: row.type,
        icon: row.icon,
        state: row.state
      };
      this.open = true;
      this.title = "编辑业务对象";
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // this.form.tableName=this.form.mainTableName+"_ext";
          saveOrUpdate(this.form).then(res=>{
            this.$modal.msgSuccess("保存成功");
            this.open = false;
            this.getList();
          })
        }
      });
    },
    //设计表单
    handleDesForm(row){
      this.$router.push({ path: '/lowcode/form_config', query: { objectId: row.id } })
    },
    //设计流程
    handleDesFlow(row){
      this.$router.push({ path: '/lowcode/flow_config', query: { formObjectId: row.id } })
    },
    //进入列表
    handleEnter(row){
      this.$router.push({ path: '/lowcode/front_list', query: { formObjectId: row.id } })
    },
    //删除业务对象
    handleDelete(row) {
      this.$confirm('此操作将永久删除该业务对象, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteObj(row).then(res => {
          this.$message.success("删除成功")
          this.getList();
        })
      }).catch(() => {
        this.$message.info("已取消删除")
      });
    },
    //启用停用
    handleUpdateState(row) {
      let strTmp= row.state=='0' ? '已上线' : '已下线'
      updateObjState(row).then(res => {
        this.$message.success(strTmp)
        this.getList();
      }).catch(err => {
        this.getList();
      })
    }

  }
};
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
}
.avatar {
  width: 50px;
  height: 50px;
  display: block;
}
</style>
