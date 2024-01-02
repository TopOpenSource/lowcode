<template>
  <!--任务节点配置-->
  <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body type="card" class="dialog-task">
    <div class="app-container" style="padding: 0px 0px 0px 0px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="节点名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入"></el-input>
        </el-form-item>

        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="form.serviceType" placeholder="请选择">
            <el-option v-for="item in serviceTypeList" :key="item.key" :label="item.name" :value="item.key"></el-option>
          </el-select>
        </el-form-item>

        <template v-if="form.serviceType=='com.ruoyi.flow.listener.SendProcessExc'">
          <el-form-item label="业务对象" prop="refObjectId">
            <el-select v-model="form.refObjectId" placeholder="请选择" @change="handleRefAttrs">
              <el-option v-for="item in refObjList" :key="item.key" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </template>

        <div class="top-content-title">
          <span>字段匹配</span>
        </div>
        <template v-for="item in refAttrsList"  v-if="JSON.stringify(form.attrsMatch) != '{}'">
          <el-row :gutter="10">
            <el-col :span="10">
              <div v-if="form.attrsMatch[item.alias].model==0">
                <el-form-item :label=item.name :prop="'attrsMatch.'+item.alias+'.alias'" :rules="[{required:(item.notnull==1 && form.attrsMatch[item.alias].model==0),message:'请选择',trigger:'change'}]">
                  <el-select v-if="form.attrsMatch[item.alias].model==0" v-model="form.attrsMatch[item.alias].alias" placeholder="请选择" @visible-change="handleAttrsList(item)">
                    <el-option v-for="item in attrsList" :key="item.id" :label="item.name" :value="item.alias"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div v-if="form.attrsMatch[item.alias].model==1">

                <template v-if="item.type==AttributeConstant.TYPE_TEXT">
                  <top-input v-model="form.attrsMatch[item.alias].value"
                             :prop="'attrsMatch.'+item.alias+'.value'"
                             :placeholder="item.displayTitle"
                             :notnull="item.notnull"
                             :maxlength="item.dbLength"
                             :minLength="item.displayMinlength"
                             :regex="item.displayRegex"
                             :label="item.name"

                             :edit="true"

                  ></top-input>

                </template>
                <!--文本域-->
                <template v-if="item.type==AttributeConstant.TYPE_TEXTAREA">
                  <top-textarea v-model="form.attrsMatch[item.alias].value"
                                :prop="'attrsMatch.'+item.alias+'.value'"
                                :placeholder="item.displayTitle"
                                :notnull="item.notnull"
                                :maxlength="item.dbLength"
                                :minLength="item.displayMinlength"
                                :regex="item.displayRegex"
                                :label="item.name"

                                :edit="true"
                  ></top-textarea>
                </template>
                <!--手机或座机-->
                <template v-if="item.type==AttributeConstant.TYPE_PHONE">
                  <top-phone v-model="form.attrsMatch[item.alias].value"
                             :prop="'attrsMatch.'+item.alias+'.value'"
                             :placeholder="item.displayTitle"
                             :notnull="item.notnull"
                             :maxlength="item.dbLength"
                             :minLength="item.displayMinlength"
                             :label="item.name"

                             :edit="true"
                  ></top-phone>
                </template>
                <!--email-->
                <template v-if="item.type==AttributeConstant.TYPE_EMAIL">
                  <top-email v-model="form.attrsMatch[item.alias].value"
                             :prop="'attrsMatch.'+item.alias+'.value'"
                             :placeholder="item.displayTitle"
                             :notnull="item.notnull"
                             :maxlength="item.dbLength"
                             :minLength="item.displayMinlength"
                             :label="item.name"

                             :edit="true"
                  ></top-email>
                </template>
                <!--website-->
                <template v-if="item.type==AttributeConstant.TYPE_WEBSITE">
                  <top-website v-model="form.attrsMatch[item.alias].value"
                               :prop="'attrsMatch.'+item.alias+'.value'"
                               :placeholder="item.displayTitle"
                               :notnull="item.notnull"
                               :maxlength="item.dbLength"
                               :minLength="item.displayMinlength"
                               :label="item.name"

                               :edit="true"
                  ></top-website>
                </template>
                <!--int-->
                <template v-if="item.type==AttributeConstant.TYPE_INT">
                  <top-number v-model="form.attrsMatch[item.alias].value"
                              :prop="'attrsMatch.'+item.alias+'.value'"
                              :placeholder="item.displayTitle"
                              :notnull="item.notnull"
                              :maxlength="item.dbLength"
                              :minLength="item.displayMinlength"
                              :label="item.name"

                              :edit="true"
                  ></top-number>
                </template>
                <!--float-->
                <template v-if="item.type==AttributeConstant.TYPE_FLOAT">
                  <top-number v-model="form.attrsMatch[item.alias].value"
                              :prop="'attrsMatch.'+item.alias+'.value'"
                              :placeholder="item.displayTitle"
                              :notnull="item.notnull"
                              :maxlength="item.dbLength"
                              :minLength="item.displayMinlength"
                              :decimals="item.decimals"
                              :label="item.name"

                              :edit="true"
                  ></top-number>
                </template>
                <!--date-->
                <template v-if="item.type==AttributeConstant.TYPE_DATE">
                  <top-date v-model="form.attrsMatch[item.alias].value"
                            :prop="'attrsMatch.'+item.alias+'.value'"
                            :placeholder="item.displayTitle"
                            :notnull="item.notnull"
                            :label="item.name"

                            :edit="true"
                  ></top-date>
                </template>
                <!--datetime-->
                <template v-if="item.type==AttributeConstant.TYPE_DATETIME">
                  <top-datetime v-model="form.attrsMatch[item.alias].value"
                                :prop="'attrsMatch.'+item.alias+'.value'"
                                :placeholder="item.displayTitle"
                                :notnull="item.notnull"
                                :label="item.name"
                                :edit="true"
                  ></top-datetime>
                </template>
                <!--radio-->
                <template v-if="item.type==AttributeConstant.TYPE_RADIO">
                  <top-radio v-model="form.attrsMatch[item.alias].value"
                             :prop="'attrsMatch.'+item.alias+'.value'"
                             :placeholder="item.displayTitle"
                             :notnull="item.notnull"
                             :options="dicts[item.dictTypeCode]"
                             :displayFlat="item.displayFlat"
                             :label="item.name"

                             :edit="true"
                  ></top-radio>
                </template>
                <!--checkbox-->
                <template v-if="item.type==AttributeConstant.TYPE_CHECKBOX">
                  <top-checkbox v-model="form.attrsMatch[item.alias].value"
                                :prop="'attrsMatch.'+item.alias+'.value'"
                                :placeholder="item.displayTitle"
                                :notnull="item.notnull"
                                :options="dicts[item.dictTypeCode]"
                                :displayFlat="item.displayFlat"
                                :label="item.name"

                                :edit="true"
                  ></top-checkbox>
                </template>

                <template v-if="item.type==AttributeConstant.TYPE_USER">
                  <top-user v-model="form.attrsMatch[item.alias].value"
                            :prop="'attrsMatch.'+item.alias+'.value'"
                            :label="item.name"
                            :notnull="item.notnull"
                            :multiple="item.multiple"
                            :userList="userList"

                            :edit="true"
                            clearable></top-user>
                </template>
                <template v-if="item.type==AttributeConstant.TYPE_ORG">
                  <top-org v-model="form.attrsMatch[item.alias].value"
                           :prop="'attrsMatch.'+item.alias+'.value'"
                           :label="item.name"
                           :notnull="item.notnull"
                           :multiple="item.multiple"
                           :deptList="deptList"
                           :edit="true"
                           clearable></top-org>
                </template>

              </div>
            </el-col>
            <el-col :span="4" >
              <el-button v-if="form.attrsMatch[item.alias].model==1" @click="modelSwitch(item.alias, 0)" size="small" type="primary" plain>
                映射属性
              </el-button>
              <el-button v-if="form.attrsMatch[item.alias].model==0" @click="modelSwitch(item.alias, 1)" size="small" type="primary" plain>
                自定义
              </el-button>
            </el-col>
          </el-row>
        </template>

      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" @click="save">保存</el-button>
      <el-button size="small" @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";
import {list} from "@/api/lowcode/config/formObject";
import {getEnableForm} from '@/api/lowcode/config/form_config/formLayout'
import Util from '@/views/lowcode/task/Util'

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
import store from '@/store'


let AttributeConstant = {
  TYPE_PHONE: "phone",
  TYPE_EMAIL: "email",
  TYPE_WEBSITE: "website",
  TYPE_TEXT: "text",
  TYPE_INT: "int",
  TYPE_FLOAT: "float",
  TYPE_DATE: "date",
  TYPE_DATETIME: "datetime",
  TYPE_RADIO: "radio",
  TYPE_CHECKBOX: "checkbox",
  TYPE_TEXTAREA: "textarea",
  TYPE_ADDR: "addr",
  TYPE_FILE: "file",
  TYPE_ORG: "org",
  TYPE_USER: "user"
}
let util = new Util();
export default {
  name: "ServiceTaskDesigner",
  mixins: [mixinProperties],
  prop: ['objectId'],

  data() {
    return {
      AttributeConstant: AttributeConstant,
      dicts: store.getters.dictTypeMap,
      userList: store.getters.userList,
      deptList: store.getters.deptList,

      title: '',
      open: false,
      refObjList: [],
      // 该流程属性
      attrsList: [],
      // 发起流程属性
      refAttrsList: [],
      serviceTypeList: [
        {"key": "com.ruoyi.flow.listener.SendProcessExc", "name": "发起流程"}
      ],
      form: {
        name: '',
        serviceType: '',
        refObjectId: '',
        attrsMatch: {}
      },
      rules: {
        name: [
          {required: true, message: "不能为空", trigger: "blur"},
          {min: 2, max: 20, message: '长度必须介于 2 和 10 之间', trigger: 'blur'}
        ],
        serviceType: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        refObjectId: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
      }
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
    'top-org': Org,
    'top-user': User
  },
  created() {
    this.getRefObjectList();
  },
  methods: {
    modelSwitch(alias, type) {
      this.form.attrsMatch[alias]={"alias": null, "model": type, "value": null};
    },
    handleAttrsList(item) {
      this.handleAttrs(this.objectId, item.type);
    },
    handleRefAttrs() {
      getEnableForm({objectId: this.form.refObjectId, type: '0'}).then(res => {
        let list = util.parseForm2Attrs(res.attributes, res.content);
        let refAttrsList = [];
        list.forEach(item => {
          if (item.type != "splitline") {
            refAttrsList.push(item);
          }
        })
        this.refAttrsList = refAttrsList;
        let obj = {};
        if (JSON.stringify(this.form.attrsMatch) == '{}') {
          refAttrsList.forEach(item => {
            this.$set(obj, item.alias, {"alias": null, "model": 0, "value": null});
            this.form.attrsMatch = obj;
          })
        }
      }).catch(error => {
      })
    },
    handleAttrs(objId, type) {
      this.attrsList = [];
      getEnableForm({objectId: objId, type: '0'}).then(res => {
        let list = util.parseForm2Attrs(res.attributes, res.content);
        let attrsList = [];
        list.forEach(item => {
          if (item.type == type || type == null) {
            attrsList.push(item);
          }
        })
        this.attrsList = attrsList;
      }).catch(error => {
      })
    },
    getRefObjectList() {
      list().then(response => {
        this.refObjList = response.rows;
      });
    },
    reset(){
      this.form = {
        name: '',
        serviceType: '',
        refObjectId: '',
        attrsMatch: {}
      };
      this.resetForm("form");
    },
    initData() {
      this.reset();
      this.$nextTick(() => {
        //数据回填
        if (this.config && this.config[this.element.id]) {
          this.form = this.config[this.element.id]
          this.handleRefAttrs();
          this.handleAttrs(this.objectId);
        }
      });
    },
    save() {
      //任务节点保存
      this.$refs["form"].validate(valid => {
        if (valid) {
          //保存 class serviceTask流程使用
          this.updateProperties({'class': this.form.serviceType});
          this.updateProperties({'name': this.form.name});

          //保存配置信息
          this.config[this.element.id] = this.form;
          this.open = false;
        } else {
          this.$modal.msgError("信息验证失败，请检查！");
        }
      })
    },
    cancel() {
      this.open = false;
    }
  }
}
</script>

<style scoped>

</style>
