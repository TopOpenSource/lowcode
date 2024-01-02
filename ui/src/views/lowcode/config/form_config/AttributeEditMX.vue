<template>
    <el-form ref="attrForm" :model="attrForm" label-width="0px" :rules="rules">
      <el-table :data="attrForm.tableData" style="width: 100%">
        <el-table-column label="名称" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.name'" :rules="rules.name">
              <el-input v-model="scope.row.name" placeholder="请输入名称"></el-input>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="字段类型" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.type'" :rules="rules.item">
              <el-select v-model="scope.row.type" @change="typeChange(scope.row)">
                <el-option
                  v-for="attrType in attributeTypes"
                  :key="attrType.type"
                  :label="attrType.name"
                  :value="attrType.type">
                </el-option>
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="数据源" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.dictTypeCode'" :rules="rules.dictTypeCode">
              <el-select v-model="scope.row.dictTypeCode"
                v-if="(scope.row.type == 'relation' || scope.row.type == 'radio' || scope.row.type == 'checkbox')"
                filterable
                placeholder="搜索"
              >
                <div v-if="scope.row.type != 'relation'">
                  <el-option
                    v-for="item in dictTypes"
                    :key="item.dictType"
                    :label="item.dictName"
                    :value="item.dictType">
                  </el-option>
                </div>
                <div v-if="scope.row.type == 'relation'">
                  <el-option
                    v-for="item in objects"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </div>
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="最大长度" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.dbLength'" :rules="rules.dbLength">
              <el-input 
                v-if="(scope.row.type == 'int' || scope.row.type == 'text' || scope.row.type == 'textarea' || scope.row.type == 'phone' || scope.row.type == 'email')" 
                v-model="scope.row.dbLength" 
                placeholder="请输入最大允许长度">
              </el-input>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="提示语" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.displayTitle'">
              <el-input v-model="scope.row.displayTitle" placeholder="请输入提示语"></el-input>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="必填属性" align="center">
          <template slot-scope="scope">
            <el-form-item :prop="'tableData.' + scope.$index + '.notnull'">
              <el-checkbox v-model="scope.row.notnull" true-label="1" false-label="0">必填</el-checkbox>
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>
      <div class="footer-icon">
        <el-button  @click="cancle" size="medium">取消</el-button>
        <el-button type="primary" @click="save" size="medium" class="el-button--orange">保存</el-button>
      </div>
    </el-form>
</template>

<script>
import { listAttributeTypes, getFormObjectAttribute, saveFormObjectAttributeMX } from "@/api/lowcode/config/form_config/attribute";
import { optionselect } from '@/api/system/dict/type';
import { listAll } from "@/api/lowcode/config/formObject";


export default {
  dicts: ['lowcode_file_type'],
  data() {
    let validateName = (rule, value, callback) => {
      let index=rule.field.split(".")[1]
      let type = this.attrForm.tableData[index].type;
      let dictTypeCode = this.attrForm.tableData[index].dictTypeCode;
      let dbLength = this.attrForm.tableData[index].dbLength;

      if(type||dictTypeCode||dbLength){
        if (value === null || value === '') {
          callback(new Error('此处不可为空'));
        }
        callback();
      }
      callback();
    };
    let validateItems = (rule, value, callback) => {
      let index=rule.field.split(".")[1]
      let name = this.attrForm.tableData[index].name;
      if(name){
        if (value === null) {
          callback(new Error('此处不可为空'));
        }
        callback();
      }
      callback();
    };
    let validateDictTypeCode = (rule, value, callback) => {
      let index=rule.field.split(".")[1]
      let type = this.attrForm.tableData[index].type;
      if(type == 'relation' || type == 'radio' || type == 'checkbox'){
        if (value === null) {
          callback(new Error('此处不可为空'));
        }
        callback();
      }
      callback();
    };
    let validateDbLength = (rule, value, callback) => {
      let index=rule.field.split(".")[1]
      let type = this.attrForm.tableData[index].type;
      if(type == 'int' || type == 'text' || type == 'textarea' || type == 'phone' || type == 'email' ){
        if (value === null || value === '') {
          callback(new Error('此处不可为空'));
        }
        callback();
      }
      callback();
    };
    return {
      attrForm: {
        tableData:[]
      },
      dictTypes:[],
      objects:[],
      attrOldLength: null, // 原长度， 编辑时用于获取dbLength -- rules
      attributeTypes: [], // 字段类型列表

      rules: {
        name: [{validator: validateName, trigger: 'blur'}],
        item: [{validator: validateItems, trigger: 'blur'}],
        dictTypeCode: [{validator: validateDictTypeCode, trigger: 'blur'}],
        dbLength: [{validator: validateDbLength, trigger: 'blur'}]
      }
    }
  },

  props: {
    // 传递属性
    option: {
      type: Object,
      default() {
        return {};
      }
    },
  },
  methods: {
    // 取消
    cancle(){
      this.$refs['attrForm'].resetFields();
      this.$emit('close');
    },

    // 保存
    save(){
      this.$refs["attrForm"].validate((valid) => {
        if (valid) {
          let a = [];
          this.attrForm.tableData.forEach((item)=> {
            item.objectId = this.option.objectId;
            if(item.name){
              a.push(item)
            }
          })
          return saveFormObjectAttributeMX(a).then(
            this.$store.dispatch('initAttrs'),
            this.$emit('success')
          )
        }
      })
    },

    // 切换字段类型
  typeChange(row){
    // console.log(type);
    let attrType = this.attributeTypes.find(t=>t.type == row.type);
        row.dbLength = attrType.dbLength;
        row.notnull = '0';
        row.displayTitle = null;
        row.decimals = null;
        row.dictTypeCode = null;
        row.displayFullline = attrType.displayFullline;
        row.dbType = attrType.dbType;
        row.displayType = attrType.displayType;
    if(row.type == 'float'){
      row.dbLength = attrType.dbLength-attrType.decimals;
      row.decimals = attrType.decimals;
    }
    this.$refs['attrForm'].clearValidate();
  },

    // 加载attribute 信息
    loadAttributeInfo(id){
      console.log(id);
      if(!id){
        return ;
      }
      return getFormObjectAttribute({
        id: id,
      }).then(res=>{
        // this.attrForm = res;
        this.attrOldLength = res.dbLength;
      });
    },

    // 加载字段类型列表
    loadAttributeTypes(){
      return listAttributeTypes().then(res=>{
        this.attributeTypes = res;
      });
    },

    //获取业务对象
    getObjects(){
      return listAll().then(res => {
        this.objects = res.rows;
        // console.log(123456);
      });
    },

    // 还原数据
    clear(){
      this.initFormData();
    },

    // 初始化表单数据
    initFormData(){
      this.$nextTick(()=>{
        this.$refs.attrForm.clearValidate();
      })

      this.attrForm.tableData=[]
      for(let i=0;i<8;i++){
        this.attrForm.tableData.push({ id: null,
          name: null,
          type: null,             // 字段类型
          dbLength: null,              // 最大长度
          notnull: '0',           // 必填
          displayTitle: null,     // 提示语
          dictTypeCode: null,     // 数据源
          displayFlat: '0',      // 展示方式下拉
          displayFullline: '0',   // 是否整行显示
          objectId: null,
          dbType: null,           // 数据库存储类型
          displayType: null,      // 界面模型格式
          multiple: '0'           // 是否多选 0否
        })
      }
    },

    //获取字典类型
    getAllDicts() {
      return optionselect().then(res => {
        this.dictTypes=res.data;
      })
    },

    // 加载数据
    async loadData(){
      await this.getAllDicts();
      await this.loadAttributeTypes();
      await this.getObjects();
      this.initFormData();
    }
  },
  mounted() {
    // this.loadData();
  }
}
</script>

<style>

</style>
