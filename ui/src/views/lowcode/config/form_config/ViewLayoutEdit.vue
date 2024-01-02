<template>
  <section class="top-content-pad">
    <el-form label-width="120px" :model="form" ref="form">

      <el-row>
          <el-form-item label="列表视图名称" prop="name" :rules="form_rules.notnull">
            <el-input placeholder="请输入列表视图名称" v-model="form.name" maxlength="50"></el-input>
          </el-form-item>
      </el-row>

      <el-row>
          <div class="top-content-title">
            <span>检索条件</span>
          </div>
          <condtions ref="conditions" :objectId="form.objectId"></condtions>
      </el-row>

      <el-row>
        <div class="top-content-title">
          <span>列表显示字段</span>
        </div>
        <top-transfer :data="form_options.columns" v-model="form_options.tableColumnList" :props="{key: 'alias',label: 'name'}" :titles="['未选择字段', '已选择字段']"></top-transfer>
      </el-row>

      <el-row>
        <div class="top-content-title" style="padding-top: 26px">
          <span>排序规则</span>
        </div>

        <el-col :sm="10" :md="10" :lg="10">
          <el-form-item prop="sortColumn" :rules="form_rules.notnull_check" label-width="0px">
            <el-select v-model="form.sortColumn" placeholder="请选择">
              <el-option v-for="item in form_options.columns" :key="item.alias" :label="item.name" :value="item.alias">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :sm="10" :md="10" :lg="10">
          <el-form-item prop="sort" :rules="form_rules.notnull_check" label-width="50px">
            <el-radio-group v-model="form.sort">
              <el-radio label="AES">升序</el-radio>
              <el-radio label="DESC">降序</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <div class="top-content-title" style="padding-top: 26px">
          <span>权限</span>
        </div>
        <el-col :sm="10" :md="10" :lg="10">
          <el-form-item prop="roles" label-width="0px" :rules="form_rules.notnull_check">
            <el-select v-model="form.roles" placeholder="请选择" multiple>
              <el-option key="-1" label="全部" value="-1" @click.native="chooseAll"></el-option>
              <el-option v-for="item in roleOptions" :key="item.roleKey" :label="item.roleName" :value="item.roleKey"  @click.native="chooseSel">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="footer-icon">
      <el-button @click="close" size="medium">取消</el-button>
      <el-button type="primary" @click="submit" size="medium" class="el-button--orange">保存</el-button>
    </div>
  </section>
</template>

<script>
import {saveOrUpdate, selectByPK} from "@/api/lowcode/config/form_config/viewLayout"
import Transfer from "@/views/lowcode/components/Transfer";
import Conditions from "./Conditions"
import store from "@/store";

export default {
  data() {
    return {
      loading: false,
      roleOptions: [],
      form: {
        id: '',
        objectId: '',
        name: '',
        queryCriteria: null,
        tableColumn: '',
        sortColumn: '',
        sort: 'AES',
        roles: null,
        type: '0',
      },
      form_rules: {
        notnull: [{required: true, message: '请输入数据', trigger: 'blur'}],
        notnull_check: [{required: true, message: '请选择', trigger: 'change'}]
      },
      form_options: {
        //属性
        columns: [],
        //逗号分隔开的选中列表属性
        tableColumnList: []
      },
    };
  },
  created() {
    this.roleOptions=store.getters.roles
  },
  methods: {
    close() {
      this.$emit("close");
    },
    async submit() {
      this.loading = true
      //验证search
      let searchPass;
       await this.$refs["conditions"].valid().then(valid=>{
         searchPass=valid;
       });
      //验证表单
      this.$refs["form"].validate((valid) => {
        if (valid && searchPass) {
          //查询框赋值
          this.form.queryCriteria=this.$refs["conditions"].getValue()
          //如果为关联属性默认不启用
          if (this.form.type == '-1') {
            this.form.enable = "0"
          }
          //数据提交
          this.form.tableColumn = this.form_options.tableColumnList.join(",")
          // this.form.roles.push('admin');
          let roles = this.form.roles.join(",");
          this.form.roles = roles;
          saveOrUpdate(this.form).then(res => {
            this.$notify({message: '保存成功', type: 'success'});
            this.$emit("afterSubmit");
            this.loading = false
          }).catch(error => {
            this.loading = false
          });

        } else {
          this.loading = false
        }
      });
    },

    clear(objectId) {
      this.form = {
        id: '',
        objectId: '',
        name: '',
        queryCriteria: null,
        tableColumn: '',
        tableColumnMobile: '',
        sortColumn: '',
        sort: 'AES',
        roles: null,
        type: '0',
      }

      this.form_options = {
        columns: [],
        tableColumnList: []
      }

      //初始化属性
      let attrs=store.getters.attrMap.get(objectId)
      this.form_options.columns = [];
      this.form_options.columns = attrs.filter(attr => {
        if (attr.type != 'file') {
          return true;
        } else {
          return false;
        }
      });

      this.form.sortColumn=this.form_options.columns[2].alias

      this.$nextTick(function () {
        this.$refs["form"].clearValidate();
        this.$refs["conditions"].clearVal();
      });

    },
    chooseAll(value){
      this.form.roles=['-1']
    },
    chooseSel(){
     // this.form.roles
      if(this.form.roles.findIndex(item => item === '-1')!=-1){
        this.form.roles.splice(this.form.roles.findIndex(item => item === '-1'), 1)
      }

    },
    async loadData(objectId, id, type) {
      this.loading = true
      //清理数据
      this.clear(objectId);

      //查询视图详情
      if (id != undefined && id != null) {
        await selectByPK({id: id}).then(res => {
          this.form = res;
          let roles = res.roles.split(",");
          this.form.roles = roles;
          this.form_options.tableColumnList = this.form.tableColumn.split(",")
        }).catch(error => {
          this.loading = false
        });
      }
      this.form.objectId = objectId;
      this.form.type = type;

      //初始化查询条件
      this.$refs.conditions.initData(this.form.queryCriteria)

      this.loading = false
    }
  },
  components: {
    "condtions":Conditions,
    "top-transfer":Transfer
  },
  watch: {},
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
