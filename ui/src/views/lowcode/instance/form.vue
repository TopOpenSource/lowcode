<template>
  <el-form :model="form" :rules="form_rules" ref="form" class="formDetail" label-position="left">
    <el-row :gutter="20">
      <template v-for="(attr,index) in form_options.attributes">
        <el-col :sm="24" :md="(attr.displayFullline=='1')?24:12" :lg="(attr.displayFullline=='1')?24:12"
                :xl="(attr.displayFullline=='1')?24:8">
          <!--分隔栏-->
          <template v-if="attr.type=='splitline'">
            <div class="top-content-title">
              <span>{{ attr.name }}</span>
            </div>
          </template>
          <!--文本-->
          <template v-if="attr.type==AttributeConstant.TYPE_TEXT">
            <top-input v-model="form[attr.alias]"
                       :prop="attr.alias"
                       :placeholder="attr.displayTitle"
                       :notnull="attr.notnull"
                       :maxlength="attr.dbLength"
                       :minLength="attr.displayMinlength"
                       :regex="attr.displayRegex"
                       :label="attr.name"
                       v-if="form_options.attrListShow[attr.alias]"
                       :edit="form_options.attrListEdit[attr.alias]"

            ></top-input>

          </template>
          <!--文本域-->
          <template v-if="attr.type==AttributeConstant.TYPE_TEXTAREA">
            <top-textarea v-model="form[attr.alias]"
                          :prop="attr.alias"
                          :placeholder="attr.displayTitle"
                          :notnull="attr.notnull"
                          :maxlength="attr.dbLength"
                          :minLength="attr.displayMinlength"
                          :regex="attr.displayRegex"
                          :label="attr.name"
                          v-if="form_options.attrListShow[attr.alias]"
                          :edit="form_options.attrListEdit[attr.alias]"
            ></top-textarea>
          </template>
          <!--手机或座机-->
          <template v-if="attr.type==AttributeConstant.TYPE_PHONE">
            <top-phone v-model="form[attr.alias]"
                       :prop="attr.alias"
                       :placeholder="attr.displayTitle"
                       :notnull="attr.notnull"
                       :maxlength="attr.dbLength"
                       :minLength="attr.displayMinlength"
                       :label="attr.name"
                       v-if="form_options.attrListShow[attr.alias]"
                       :edit="form_options.attrListEdit[attr.alias]"
            ></top-phone>
          </template>
          <!--email-->
          <template v-if="attr.type==AttributeConstant.TYPE_EMAIL">
            <top-email v-model="form[attr.alias]"
                       :prop="attr.alias"
                       :placeholder="attr.displayTitle"
                       :notnull="attr.notnull"
                       :maxlength="attr.dbLength"
                       :minLength="attr.displayMinlength"
                       :label="attr.name"
                       v-if="form_options.attrListShow[attr.alias]"
                       :edit="form_options.attrListEdit[attr.alias]"
            ></top-email>
          </template>
          <!--website-->
          <template v-if="attr.type==AttributeConstant.TYPE_WEBSITE">
            <top-website v-model="form[attr.alias]"
                         :prop="attr.alias"
                         :placeholder="attr.displayTitle"
                         :notnull="attr.notnull"
                         :maxlength="attr.dbLength"
                         :minLength="attr.displayMinlength"
                         :label="attr.name"
                         v-if="form_options.attrListShow[attr.alias]"
                         :edit="form_options.attrListEdit[attr.alias]"
            ></top-website>
          </template>
          <!--int-->
          <template v-if="attr.type==AttributeConstant.TYPE_INT">
            <top-number v-model="form[attr.alias]"
                        :prop="attr.alias"
                        :placeholder="attr.displayTitle"
                        :notnull="attr.notnull"
                        :maxlength="attr.dbLength"
                        :minLength="attr.displayMinlength"
                        :label="attr.name"
                        v-if="form_options.attrListShow[attr.alias]"
                        :edit="form_options.attrListEdit[attr.alias]"
            ></top-number>
          </template>
          <!--float-->
          <template v-if="attr.type==AttributeConstant.TYPE_FLOAT">
            <top-number v-model="form[attr.alias]"
                        :prop="attr.alias"
                        :placeholder="attr.displayTitle"
                        :notnull="attr.notnull"
                        :maxlength="attr.dbLength"
                        :minLength="attr.displayMinlength"
                        :decimals="attr.decimals"
                        :label="attr.name"
                        v-if="form_options.attrListShow[attr.alias]"
                        :edit="form_options.attrListEdit[attr.alias]"
            ></top-number>
          </template>
          <!--date-->
          <template v-if="attr.type==AttributeConstant.TYPE_DATE">
            <top-date v-model="form[attr.alias]"
                      :prop="attr.alias"
                      :placeholder="attr.displayTitle"
                      :notnull="attr.notnull"
                      :label="attr.name"
                      v-if="form_options.attrListShow[attr.alias]"
                      :edit="form_options.attrListEdit[attr.alias]"
            ></top-date>
          </template>
          <!--datetime-->
          <template v-if="attr.type==AttributeConstant.TYPE_DATETIME">
            <top-datetime v-model="form[attr.alias]"
                          :prop="attr.alias"
                          :placeholder="attr.displayTitle"
                          :notnull="attr.notnull"
                          :label="attr.name"
                          v-if="form_options.attrListShow[attr.alias]"
                          :edit="form_options.attrListEdit[attr.alias]"
            ></top-datetime>
          </template>
          <!--radio-->
          <template v-if="attr.type==AttributeConstant.TYPE_RADIO">
            <top-radio v-model="form[attr.alias]"
                       :prop="attr.alias"
                       :placeholder="attr.displayTitle"
                       :notnull="attr.notnull"
                       :options="form_options.dicts[attr.dictTypeCode]"
                       :displayFlat="attr.displayFlat"
                       :label="attr.name"
                       v-if="form_options.attrListShow[attr.alias]"
                       :edit="form_options.attrListEdit[attr.alias]"
            ></top-radio>
          </template>
          <!--checkbox-->
          <template v-if="attr.type==AttributeConstant.TYPE_CHECKBOX">
            <top-checkbox v-model="form[attr.alias]"
                          :prop="attr.alias"
                          :placeholder="attr.displayTitle"
                          :notnull="attr.notnull"
                          :options="form_options.dicts[attr.dictTypeCode]"
                          :displayFlat="attr.displayFlat"
                          :label="attr.name"
                          v-if="form_options.attrListShow[attr.alias]"
                          :edit="form_options.attrListEdit[attr.alias]"
            ></top-checkbox>
          </template>

          <template v-if="attr.type==AttributeConstant.TYPE_USER">
            <top-user v-model="form[attr.alias]"
                      :prop="attr.alias"
                      :label="attr.name"
                      :notnull="attr.notnull"
                      :multiple="attr.multiple"
                      :userList="form_options.userList"
                      v-if="form_options.attrListShow[attr.alias]"
                      :edit="form_options.attrListEdit[attr.alias]"
                      clearable></top-user>
          </template>
          <template v-if="attr.type==AttributeConstant.TYPE_ORG">
            <top-org v-model="form[attr.alias]"
                     :prop="attr.alias"
                     :label="attr.name"
                     :notnull="attr.notnull"
                     :multiple="attr.multiple"
                     :deptList="form_options.deptList"
                     v-if="form_options.attrListShow[attr.alias]"
                     :edit="form_options.attrListEdit[attr.alias]"
                     clearable></top-org>
          </template>

          <template v-if="attr.type==AttributeConstant.TYPE_RELATION">
            <top-relation
              v-model="form[attr.alias]"
              :prop="attr.alias"
              :targetObjId="attr.dictTypeCode"
              :attrId="attr.id"
              :label="attr.name"
              :notnull="attr.notnull"
              :objectId="attr.dictTypeCode"
              :instanceId="form.id"
              v-if="form_options.attrListShow[attr.alias]"
              :edit="form_options.attrListEdit[attr.alias]"
            ></top-relation>
          </template>

          <template v-if="attr.type==AttributeConstant.TYPE_FILE">
            <top-file
              v-model="form[attr.alias]"
              :prop="attr.alias"
              :attrId="attr.id"
              :instanceId="form.id"
              :label="attr.name"
              :notnull="attr.notnull"
              v-if="form_options.attrListShow[attr.alias]"
              :edit="form_options.attrListEdit[attr.alias]"
            ></top-file>
          </template>

        </el-col>
      </template>
    </el-row>
  </el-form>
</template>

<script>
import store from "@/store";
import request from "@/utils/request";
import {getEnableForm} from "@/api/lowcode/config/form_config/formLayout";
import Util from "@/views/lowcode/task/Util";
import AttributeConstant from "@/views/lowcode/components/pub/AttributeConstant";
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
import File from "@/views/lowcode/components/File";
import Addr from "@/views/lowcode/components/Addr";
import Org from "@/views/lowcode/components/Org";
import User from "@/views/lowcode/components/User";
import Relation from "@/views/lowcode/components/Relation";

let util = new Util();
export default {
  name: "InstanceForm",
  data(){
    return{
      form: {
        applyTitle:''
      },
      form_options: {
        //字典
        dicts: store.getters.dictTypeMap,
        //用户列表
        userList: store.getters.userList,
        //组织
        deptList: store.getters.deptList,
        //属性
        attributes: [],
        //属性编辑状态
        attrListEdit:{},
        //属性显示状态
        attrListShow:{}
      },
      form_rules: {

      },
      AttributeConstant: AttributeConstant,
    }
  },
  props: {
    //业务对象ID
    objectId: null,
    //实例ID
    instId:null
  },
  methods:{
    clear() {
      this.form = {};
      this.form_options.attrListShow={}
      this.form_options.attrListEdit={}
    },
    //加载表单配置信息
    loadFormInfo(attrListEdit,attrListShow){
      if(this.objectId){
        getEnableForm({objectId: this.objectId, type: '0'}).then(res => {
          this.form_options.attributes = util.parseForm2Attrs(res.attributes, res.content);
          //字段权限赋值
          this.form_options.attrListShow=attrListShow
          this.form_options.attrListEdit=attrListEdit
        }).catch(error => {

        })
      }
    },
    //加载实例信息
    loadInstInfo(){
      if(this.instId){
        request({
          url: "/lowcode/lowcode/instance/selectByPk",
          method: 'post',
          data: {id: this.instId, objectId: this.objectId}
        }).then(res => {
          this.form = res;
        })
      }
    },
    valid(){
      let $this = this;
      return new Promise(function (resolve, reject) {
        $this.$refs["form"].validate((valid) => {
          resolve(valid);
        })
      });
    },
    getData(){
      return this.form
    },
    init(attrListEdit,attrListShow){
      this.$nextTick(()=>{
        this.clear()
        this.loadFormInfo(attrListEdit,attrListShow)
        this.loadInstInfo()
      })

    }
  },
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
    'top-file': File,
    'top-addr': Addr,
    'top-org': Org,
    'top-user': User,
    'top-relation':Relation,
  },
  mounted(){

  }
}
</script>

<style scoped>

</style>
