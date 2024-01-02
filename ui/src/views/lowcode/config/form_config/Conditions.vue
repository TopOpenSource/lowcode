<template>
  <div class="conditions-task">
    <div slot="content">
      <el-form :model="form" ref="conditionsForm" >
        <div class="dep-box">
          <span class="dep-relation" @click="setGlobalRelation()" v-if="form.groupList.length>1">
            {{ form.relation === 'AND' ? '且' : '或' }}
          </span>
          <!--一级条件-->
          <div class="dep-list" v-for="(el,$index) in form.groupList" :key='$index' v-if="el.itemList.length">
          <span class="dep-line-pie" @click="setItemRelation($index)" >
              {{ el.relation === 'AND' ? '且' : '或' }}
          </span>

            <div class="dep-list-model">
              <!--二级条件-->
              <el-row v-for="(item,$itemIndex) in el.itemList" :key='$itemIndex' class="list" @click="itemIndex = $itemIndex" :gutter="10">
                <!--选择属性-->
                <el-col :sm="12" :md="5" :lg="5">
                  <el-form-item  :rules="form_rules.notnull_check"  :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.query'">
                    <el-select size="small" v-model="item.query" @change="handleChange(item)">
                      <el-option v-for="attrItem in attrList" :key="attrItem.alias" :label="attrItem.name" :value="attrItem.alias">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <!--选择条件-->
                <el-col :sm="12" :md="4" :lg="4">
                  <el-form-item  :rules="form_rules.notnull_check" :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.queryCriteria'" >
                    <el-select  size="small" v-model="item.queryCriteria">
                      <el-option v-for="criteriaItem in getAttrQueryCriteria(item.query)"
                                 :key="criteriaItem.value"
                                 :label="criteriaItem.label"
                                 :value="criteriaItem.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <!--比较值-->
                <template v-if="item.query!=null && item.query !=''" >
                  <el-col :sm="12" :md="12" :lg="12">
                    <template v-if="item.queryCriteria !='null' && item.queryCriteria !='notNull'">
                      <top-input
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_TEXT || attrMap.get(item.query).type==AttributeConstant.TYPE_PHONE || attrMap.get(item.query).type==AttributeConstant.TYPE_EMAIL || attrMap.get(item.query).type==AttributeConstant.TYPE_WEBSITE"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" notnull='1' :maxlength="attrMap.get(item.query).dbLength" ></top-input>

                      <top-number
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_INT"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" notnull='1' :maxlength="attrMap.get(item.query).dbLength" ></top-number>

                      <top-input
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_FLOAT"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" notnull='1' :maxlength="attrMap.get(item.query).dbLength" regex="(^-?[1-9]\d*$)|([0]{1,1}$)|(^([-]?)[0-9]+\.[0-9]{1,2}$)"></top-input>

                      <top-date
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_DATE"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value"></top-date>

                      <top-datetime
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_DATETIME"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value"></top-datetime>

                      <top-radio
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_RADIO || attrMap.get(item.query).type==AttributeConstant.TYPE_CHECKBOX"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" :options="dicts[attrMap.get(item.query).dictTypeCode]"></top-radio>

                      <top-user
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_USER"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" :userList="userList" :ext="'1'"></top-user>

                      <top-org
                        v-if="attrMap.get(item.query).type==AttributeConstant.TYPE_ORG"
                        :prop="'groupList.'+$index+'.itemList.'+$itemIndex+'.value'" v-model="item.value" :deptList="deptList" :ext="'1'"></top-org>

                    </template>
                  </el-col>
                </template>

                <!--操作-->
                <el-col :sm="3" :md="3" :lg="3">
                  <span class="operation">
                    <a href="javascript:" class="delete" @click="delItem($index,$itemIndex)" style="padding:0 3px ">
                      <em class="iconfont el-icon-delete" data-toggle="tooltip" data-container="body" title="删除"></em>
                    </a>
                    <a href="javascript:" class="add" @click="addItem($index)" v-if="el.itemList.length-$itemIndex==1" style="padding:0 3px; margin-left: 5px">
                       <em class="iconfont el-icon-circle-plus-outline" data-toggle="tooltip" data-container="body" title="新增" style="font-weight: bold"></em>
                    </a>

                  </span>
                </el-col>
              </el-row>
            </div>

            <el-button  class="delete-btn" type="text" icon="el-icon-close"  size="mini" data-toggle="tooltip" data-container="body" @click="delGroup($index)" title="删除"></el-button>
          </div>
        </div>
      </el-form>

      <div class="dep-opt">
        <a href="javascript:" @click="addGroup()" class="add-dep">
          <el-button type="primary" round plain data-toggle="tooltip" size="mini" title="新增条件" icon="el-icon-plus">新增条件</el-button>
        </a>
      </div>

    </div>

  </div>
</template>
<script>



let queryCriteria = {
  number: [
    {label: '大于', value: 'gt'},
    {label: '大于等于', value: 'gtEq'},
    {label: '小于', value: 'lt'},
    {label: '小于等于', value: 'ltEq'},
    {label: '等于', value: 'eq'},
    {label: '不等于', value: 'notEq'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  text: [
    {label: '包含', value: 'content'},
    {label: '等于', value: 'eq'},
    {label: '不等于', value: 'notEq'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  radio: [
    {label: '等于', value: 'eq'},
    {label: '不等于', value: 'notEq'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  checkbox: [
    {label: '包含', value: 'content_'},
    {label: '不包含', value: 'noContent_'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  date: [
    {label: '早于', value: 'lt'},
    {label: '早于等于', value: 'ltEq'},
    {label: '晚于', value: 'gt'},
    {label: '晚于等于', value: 'gtEq'},
    {label: '等于', value: 'eq'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  org: [
    {label: '包含', value: 'content_'},
    {label: '不包含', value: 'noContent_'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ],
  relation: [
    {label: '包含', value: 'content_ref'},
    {label: '不包含', value: 'noContent_ref'},
    {label: '为空', value: 'null'},
    {label: '不为空', value: 'notNull'},
  ]
};
//属性类型与查询条件关联
let typeRefQueryCriteria = {
  text: "text",
  textarea: "text",
  phone: "text",
  email: "text",
  website: "text",
  int: "number",
  float: "number",
  date: "date",
  datetime: "date",
  radio: "radio",
  checkbox: "checkbox",
  org: 'org',
  user: 'org'
}

let AttributeConstant ={
  TYPE_PHONE:"phone",
  TYPE_EMAIL:"email",
  TYPE_WEBSITE:"website",
  TYPE_TEXT:"text",
  TYPE_INT:"int",
  TYPE_FLOAT:"float",
  TYPE_DATE:"date",
  TYPE_DATETIME:"datetime",
  TYPE_RADIO:"radio",
  TYPE_CHECKBOX:"checkbox",
  TYPE_TEXTAREA:"textarea",
  TYPE_ADDR:"addr",
  TYPE_FILE:"file",
  TYPE_ORG:"org",
  TYPE_USER:"user"
}

import ListUtil from "@/views/lowcode/components/pub/ListUtil";
import Checkbox from "@/views/lowcode/components/Checkbox";
import Date from "@/views/lowcode/components/Date";
import DateTime from "@/views/lowcode/components/DateTime";
import Email from "@/views/lowcode/components/Email";
import Input from "@/views/lowcode/components/Input";
import Number from "@/views/lowcode/components/Number";
import Phone from "@/views/lowcode/components/Phone";
import Radio from "@/views/lowcode/components/Radio";
import TextArea from "@/views/lowcode/components/TextArea";
import WebSite from "@/views/lowcode/components/WebSite";
import Org from '@/views/lowcode/components/Org'
import User from '@/views/lowcode/components/User'
import store from "@/store";


export default {
  name: 'dependence',
  data() {
    return {
      form: {
        relation: 'AND',
        groupList: []
      },
      //属性List
      attrList: [],
      //属性Map
      attrMap: [],
      //字典
      dicts: store.getters.dictTypeMap,
      //用户
      userList:store.getters.userList,
      //组织
      deptList:store.getters.deptList,
      //静态变量
      AttributeConstant:AttributeConstant,
      form_rules: {
        notnull: {required: true, message: '请输入数据', trigger: 'blur'},
        notnull_check: {required: true, message: '请选择', trigger: 'change'},
      },
    }
  },
  props: {
    objectId:null,
  },
  methods: {
    addGroup() {
      this.form.groupList.push({
        itemList: [{query: '', queryCriteria: '', value: ''}],
        relation: 'AND'
      })
    },
    delGroup(i) {
      this.form.groupList.splice(i, 1)
    },
    setGlobalRelation() {
      this.form.relation = this.form.relation === 'AND' ? 'OR' : 'AND'
    },
    setItemRelation(i) {
      this.form.groupList[i].relation = (this.form.groupList[i].relation === 'AND' ? 'OR' : 'AND')
    },
    addItem(i) {
      this.form.groupList[i].itemList.push({query: '', queryCriteria: '', value: ''});
    },
    delItem(index, itemIndex) {
      this.form.groupList[index].itemList.splice(itemIndex, 1)
      //若itemList为空则删除一级
      if(this.form.groupList[index].itemList.length==0){
        this.form.groupList.splice(index,1)
      }

    },
    handleChange(item){
      item.isDefault=this.attrMap.get(item.query).isDefault
    },
    //获取比较条件
    getAttrQueryCriteria(alias) {
      if(alias){
        let attr=this.attrMap.get(alias);
        return queryCriteria[typeRefQueryCriteria[attr['type']]]
      }
    },
    initData(groupList) {
      this.$nextTick(function () {
        this.attrList=store.getters.attrMap.get(this.objectId)
        this.attrMap=ListUtil.List2Map(this.attrList,"alias")

        this.clearForm()

        if(groupList && groupList !='[]'){
          this.form= JSON.parse(groupList)
        }
      })
    },
    //清理form
    clearForm(){
      this.form.groupList=[]
      this.form.relation='AND'
    },
    //验证
    valid() {
      let $this = this;
      return new Promise(function (resolve, reject) {
        $this.$refs["conditionsForm"].validate((valid) => {
          resolve(valid);
        })
      });
    },
    //取消验证
    clearVal(){
      this.$refs["conditionsForm"].clearValidate();
    },
    //判断是否为空  逐层判断条件是否为空
    validNull(){
      let isNull=true
      if(this.form && this.form.groupList){
        this.form.groupList.forEach((group,index)=>{
          if(group.itemList && group.itemList.length>0){
            isNull=false
          }else{
            this.form.groupList.splice(index,1)
          }
        })
      }
      return isNull
    },
    //获取值
    getValue() {
      if(!this.validNull()){
        return JSON.stringify(this.form)
      }else{
        return null
      }
    },
  },
  watch: {

  },
  created() {
  },
  mounted() {
  },
  destroyed() {
  },
  computed: {},
  components: {
    'top-checkbox': Checkbox,
    'top-date': Date,
    'top-datetime': DateTime,
    'top-email': Email,
    'top-input': Input,
    'top-number': Number,
    'top-phone': Phone,
    'top-radio': Radio,
    'top-textarea': TextArea,
    'top-website': WebSite,
    'top-org': Org,
    'top-user': User
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.conditions-task {
  margin-top: -10px;

  .dep-opt {
    margin-bottom: 10px;
    padding-top: 3px;
    line-height: 24px;

    .add-dep {
      color: #0097e0;
      margin-right: 10px;

      i {
        font-size: 18px;
        vertical-align: middle;
      }
    }
  }

  .dep-list {
    margin-bottom: 10px;
    position: relative;
    border-left: 1px solid #eee;
    padding-left: 16px;
    margin-left: -16px;
    transition: all 0.2s ease-out;
    padding-top: 5px;
    padding-bottom: 4px;
    /*padding-bottom: 20px;*/
    .el-form-item--small.el-form-item{
      margin-bottom: 0;
    }


    .el-button--success.is-round{
      padding: 5px 10px;
    }
    .delete-btn{
      display: none;
      position: absolute;
      top: 3px;
      right: 8px;
      color: #666666;
      font-size: 16px;
      font-weight: bold;
    }
    &:hover {
      border-left: 1px solid #0097e0;
      transition: all 0.2s ease-out;
      background-color: #F5F5F9;
      .delete-btn{
        display: inline-block;
      }
      .dep-line-pie {
        transition: all 0.2s ease-out;
        border: 1px solid #0097e0;
        background: #0097e0;
        color: #fff;
      }
    }

    .dep-line-pie {
      transition: all 0.2s ease-out;
      position: absolute;
      width: 20px;
      height: 20px;
      border: 1px solid #e2e2e2;
      text-align: center;
      top: calc(50% - 10px);
      /* margin-top: -20px;*/
      z-index: 1;
      left: -10px;
      border-radius: 10px;
      background: #fff;
      font-size: 12px;
      cursor: pointer;

      &::selection {
        background: transparent;
      }

      &::-moz-selection {
        background: transparent;
      }

      &::-webkit-selection {
        background: transparent;
      }
    }

    .dep-delete {
      position: absolute;
      bottom: -6px;
      left: 14px;
      font-size: 18px;
      color: #ff0000;
      cursor: pointer;
    }
  }

  .dep-box {
    border-left: 4px solid #eee;
    /*margin-left: -46px;*/
    padding-left: 42px;
    position: relative;

    .dep-relation {
      position: absolute;
      width: 20px;
      height: 20px;
      border: 1px solid #e2e2e2;
      text-align: center;
      top: 50%;
      margin-top: -10px;
      z-index: 1;
      left: -12px;
      border-radius: 10px;
      background: #fff;
      font-size: 12px;
      cursor: pointer;

      &::selection {
        background: transparent;
      }

      &::-moz-selection {
        background: transparent;
      }

      &::-webkit-selection {
        background: transparent;
      }
    }
  }
}


.dep-list-model {
  position: relative;
  min-height: 32px;

  .list {
    /*margin-bottom: 6px;*/

    .operation {
      padding-left: 4px;
      display: inline-block;
      line-height: 36px;

      a {
        i {
          font-size: 18px;
          vertical-align: middle;
        }
      }

      .delete {
        color: #ff0000;
      }

      .add {
        color: #0097e0;
      }
    }
  }

  .instance-state {
    display: inline-block;
    width: 24px;

    .iconfont {
      font-size: 20px;
      vertical-align: middle;
      cursor: pointer;
      margin-left: 6px;

      &.icon-SUCCESS {
        color: #33cc00;
      }

      &.icon-WAITING {
        color: #888888;
      }

      &.icon-FAILED {
        color: #F31322;
      }
    }
  }
}
</style>
