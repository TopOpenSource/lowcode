<template>
  <el-form ref="attrForm" :model="attrForm" :rules="rules" label-width="100px" >
    <el-row :gutter="20">
      <el-col :sm="12" :md="12" :lg="12">
        <el-form-item label="名称" prop="name">
          <el-input v-model="attrForm.name" placeholder="请输入名称"></el-input>
        </el-form-item>
      </el-col>
      <el-col :sm="12" :md="12" :lg="12" class="top-radio-group">
        <el-form-item label="字段类型" prop="type">
          <el-select :disabled="option.isEdit" v-model="attrForm.type" @change="typeChange">
            <el-option
              v-for="attrType in attributeTypes"
              :key="attrType.type"
              :label="attrType.name"
              :value="attrType.type">
            </el-option>
          </el-select>
<!--            <el-radio-group :disabled="option.isEdit" v-model="attrForm.type" @change="typeChange">
            <template v-for="attrType in attributeTypes">
              <el-radio :label="attrType.type">{{attrType.name}}</el-radio>
            </template>
          </el-radio-group>-->
        </el-form-item>
      </el-col>
    </el-row>

    <div v-if="!!attrForm.type" class="top-content-title">
      <span>字段属性配置</span>
    </div>
    <!--phone-->
    <template v-if="attrForm.type == 'phone'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--email-->
    <template v-if="attrForm.type == 'email'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--website-->
    <template v-if="attrForm.type == 'website'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--int-->
    <template v-if="attrForm.type == 'int'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--float-->
    <template v-if="attrForm.type == 'float'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="整数位数" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入整数位数"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="小数位数" prop="decimals">
            <el-select :disabled="option.isDefault || option.isEdit" v-model="attrForm.decimals" placeholder="请选择小数位数">
              <el-option :label="0" :value="0"></el-option>
              <el-option :label="1" :value="1"></el-option>
              <el-option :label="2" :value="2"></el-option>
              <el-option :label="3" :value="3"></el-option>
              <el-option :label="4" :value="4"></el-option>
              <el-option :label="5" :value="5"></el-option>
              <el-option :label="6" :value="6"></el-option>
              <el-option :label="7" :value="7"></el-option>
              <el-option :label="8" :value="8"></el-option>
              <el-option :label="9" :value="9"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--text-->
    <template v-if="attrForm.type == 'text'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最小长度" prop="displayMinlength">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.displayMinlength" placeholder="请输入最小允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="有效性校验" prop="displayRegex" :rules="getItemRules('displayRegex', attrForm)">
            <el-input v-model="attrForm.displayRegex" placeholder="请输入正则表达式"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle" >
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--textarea-->
    <template v-if="attrForm.type == 'textarea'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最大长度" prop="dbLength" :rules="getItemRules('dbLength', attrForm, attrOldLength)">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.dbLength" placeholder="请输入最大允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="最小长度" prop="displayMinlength">
            <el-input :disabled="option.isDefault || option.isEdit" v-model="attrForm.displayMinlength" placeholder="请输入最小允许长度"></el-input>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--radio-->
    <template v-if="attrForm.type == 'radio'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="数据源" prop="dictTypeCode" >
            <el-select v-model="attrForm.dictTypeCode" filterable placeholder="搜索" :disabled="option.isDefault || option.isEdit">
              <el-option
                v-for="item in dictTypes"
                :key="item.dictType"
                :label="item.dictName"
                :value="item.dictType">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="展示方式" prop="displayFlat" style="height:33px">
            <el-radio-group v-model="attrForm.displayFlat">
              <el-radio label="0">下拉展示</el-radio>
              <el-radio label="1">平铺展示</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--checkbox-->
    <template v-if="attrForm.type == 'checkbox'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="数据源" prop="dictTypeCode" >
            <el-select v-model="attrForm.dictTypeCode" filterable placeholder="搜索" :disabled="option.isDefault || option.isEdit">
              <el-option
                v-for="item in dictTypes"
                :key="item.dictType"
                :label="item.dictName"
                :value="item.dictType">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="展示方式" prop="displayFlat">
            <el-radio-group v-model="attrForm.displayFlat">
              <el-radio label="0">下拉展示</el-radio>
              <el-radio label="1">平铺展示</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--date-->
    <template v-if="attrForm.type == 'date'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--datetime-->
    <template v-if="attrForm.type == 'datetime'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--addr-->
    <template v-if="attrForm.type == 'addr'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--file-->
    <template v-if="attrForm.type == 'file'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="附件格式" prop="displayRegex" :rules="getItemRules('displayRegex', attrForm)">
            <!--<el-input v-model="attrForm.displayRegex" placeholder="请输入附件格式, 多个用逗号隔开, 如.doc,.xls"></el-input>-->
            <el-select v-model="fileExt" multiple>
              <el-option
                v-for="dict in dict.type.lowcode_file_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--org-->
    <template v-if="attrForm.type == 'org'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="是否多选" prop="multiple">
            <el-radio-group v-model="attrForm.multiple">
              <el-radio :label="'0'">否</el-radio>
              <el-radio :label="'1'">是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
    <!--user-->
    <template v-if="attrForm.type == 'user'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>
<!--          <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="唯一属性" prop="unique">
            <el-checkbox :disabled="option.isDefault || option.isEdit" v-model="attrForm.unique" true-label="1" false-label="0">唯一校验</el-checkbox>
          </el-form-item>
        </el-col>-->
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="是否多选" prop="multiple">
            <el-radio-group v-model="attrForm.multiple">
              <el-radio :label="'0'">否</el-radio>
              <el-radio :label="'1'">是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>

    <!--user-->
    <template v-if="attrForm.type == 'relation'">
      <el-row :gutter="20" >
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="关联业务对象" prop="dictTypeCode" >
            <el-select v-model="attrForm.dictTypeCode" filterable placeholder="搜索" :disabled="option.isDefault || option.isEdit">
              <el-option
                v-for="item in objects"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="必填属性" prop="notnull">
            <el-checkbox v-model="attrForm.notnull" true-label="1" false-label="0">必填</el-checkbox>
          </el-form-item>
        </el-col>

        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="是否多选" prop="multiple">
            <el-radio-group v-model="attrForm.multiple">
              <el-radio :label="'0'">否</el-radio>
              <el-radio :label="'1'">是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :sm="24" :md="12" :lg="12">
          <el-form-item label="提示语" prop="displayTitle">
            <el-input v-model="attrForm.displayTitle" placeholder="请输入提示语"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </template>

    <div class="footer-icon">
      <el-button  @click="cancle" size="medium">取消</el-button>
      <el-button type="primary" @click="save" size="medium" class="el-button--orange">保存</el-button>
    </div>
  </el-form>



</template>

<script>
import { Loading } from "element-ui";
import { listAttributeTypes, getFormObjectAttribute, saveFormObjectAttribute, updateFormObjectAttribute } from "@/api/lowcode/config/form_config/attribute";
import {optionselect} from '@/api/system/dict/type'
import {mapGetters,mapActions} from 'vuex';
import {listAll} from "@/api/lowcode/config/formObject";

export default {
dicts: ['lowcode_file_type'],
data() {
  return {
    // 字段属性
    attrForm: {
      id: null,
      name: null,
      type: null,             // 字段类型
      dbLength: 0,              // 最大长度
      displayMinlength: 0,    // 最小长度
      notnull: '0',           // 必填
      unique: '0',            // 唯一性
      displayTitle: null,     // 提示语
      displayRegex: "",     // 校验方式, 正则、附件格式等
      decimals: 0,            // 小数点位数
      dictTypeCode: null,     // 数据源
      displayFlat: null,      // 展示方式, 平铺、下拉
      displayFullline: '0',   // 是否整行显示
      dbType: null,           // 数据库存储类型
      displayType: null,      // 界面模型格式
      objectId: null,
      multiple: '0'           // 是否多选 0否 1是
    },

    rules: {
      name: [
        {required: true, message: '请输入字段名称', trigger: 'blur'},
        {min: 0, max: 100, message: '长度不得超过 100 个字符', trigger: 'blur'}
      ],
      type: [
        {required: true, message: '请选择字段类型', trigger: 'blur'},
      ],
      displayTitle: [
        {min: 0, max: 50, message: '长度不得超过 50 个字符', trigger: 'blur'}
      ],
      displayMinlength: [
        { pattern: /^([1-9]\d*|[0]{1,1})$/, message: '最小长度是整数！' },
        { validator: (rule, value, callback)=>{
            if(value - this.attrForm.dbLength > 0){
              callback(new Error('最小长度小于等于最大长度'));
            }
            callback();
          },
          trigger: 'blur'
        },
      ],
      dictTypeCode: [
        {required: true, message: '请选择数据源', trigger: ['blur', 'change']},
      ],
      displayFlat: [
        {required: true, message: '请选择展示方式', trigger: 'blur'},
      ],
    },
    dictTypes:[],
    objects:[],
    attrOldLength: null, // 原长度， 编辑时用于获取dbLength -- rules
    attributeTypes: [], // 字段类型列表
  }
},
components: {
  selectTree: () => import("@/views/lowcode/components/SelectTree")
},
computed: {
...mapGetters(
    ["orgs"],
  ["users"],
),
  fileExt: {
    get() {
      if (!!this.attrForm.displayRegex) {
        return this.attrForm.displayRegex.split(",")
      }
      return []
    },
    set(val) {
      this.attrForm.displayRegex = val.join(",")
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

  // 获取编辑数据
  getEditAttrFormData(){
    let attrForm = {
      id: this.attrForm.id,
      objectId: this.attrForm.objectId,
      name: this.attrForm.name,
      type: this.attrForm.type,
      length: null,              // 最大长度
      displayMinlength: null,    // 最小长度
      notnull: this.attrForm.notnull,           // 必填
      unique: this.attrForm.unique,            // 唯一性
      displayTitle: this.attrForm.displayTitle,     // 提示语
      displayRegex: null,     // 校验方式, 正则、附件格式等
      decimals: null,            // 小数点位数
      dictTypeCode: null,     // 数据源
      displayFlat: null,      // 展示方式, 平铺、下拉
      displayFullline: this.attrForm.displayFullline,   // 是否整行显示
      dbType: null,           // 数据库存储类型
      displayType: null,      // 界面模型格式
    };
    if(['phone', 'email', 'website', 'int', 'text', 'textarea'].includes(attrForm.type)){
      attrForm.dbLength = this.attrForm.dbLength;
    }
    if(['float'].includes(attrForm.type)){
      attrForm.decimals = this.attrForm.decimals;
      attrForm.dbLength = this.attrForm.dbLength+this.attrForm.decimals;
    }
    if(['text', 'textarea'].includes(attrForm.type)){
      attrForm.displayMinlength = this.attrForm.displayMinlength;
    }
    if(['radio', 'checkbox'].includes(attrForm.type)){
      attrForm.dictTypeCode = this.attrForm.dictTypeCode;
      attrForm.displayFlat = this.attrForm.displayFlat;
      if('1' == this.attrForm.displayFlat){
        attrForm.displayFullline = '1';
      }
    }
    if(['text', 'file'].includes(attrForm.type)){
      attrForm.displayRegex = this.attrForm.displayRegex;
    }
    return attrForm;
  },

  insertAttr(){
    return saveFormObjectAttribute(this.attrForm).then(res=>{
      if(res.result == 0){
        this.$store.dispatch('initAttrs')
        this.$notify({type: 'success', message: '字段已添加成功，未在业务对象中生效，请前往“布局”对该字段进行设置！'});
        this.$emit('success');
      }else{
        this.$notify({type: 'error', message: '字段添加失败，请稍后再试！'});
        this.$emit('error');
      }
    })
  },

  updateAttr(){
    return updateFormObjectAttribute(this.getEditAttrFormData()).then(res=>{
      if(res.result == 0){
        this.$store.dispatch('initAttrs')
        this.$notify({type: 'success', message: '字段已编辑成功！'});
        this.$emit('success');
      }else{
        this.$notify({type: 'error', message: '字段编辑失败，请稍后再试！'});
        this.$emit('error');
      }
    })
  },

  async commitAttr(loading){
    if(!this.attrForm.id){
      await this.insertAttr().catch(_=>{
        loading.close();
      });
    }else{
      await this.updateAttr().catch(_=>{
        loading.close();
      });
    }
    loading.close();
  },

  // 保存
  save(){
    let loading = Loading.service({fullscreen: true});
    this.$refs["attrForm"].validate((valid) => {
      if (valid) {
        if(this.option.isEdit){
          this.commitAttr(loading);
          return;
        }

        this.$confirm('字段保存后，字段类型将不可更改，是否继续保存？', '提示', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          this.commitAttr(loading);
        }).catch(_=>{
          loading.close();
        });
      } else {
        loading.close();
      }
    });
  },

  // 切换字段类型
  typeChange(type){
    let attrType = this.attributeTypes.find(t=>t.type == type);
    this.attrForm.dbLength = attrType.dbLength;
    this.attrForm.displayMinlength = null;
    this.attrForm.notnull = '0';
    this.attrForm.unique = '0';
    this.attrForm.displayTitle = null;
    this.attrForm.displayRegex = attrType.displayRegex;
    this.attrForm.decimals = null;
    this.attrForm.dictTypeCode = null;
    this.attrForm.displayFlat = null;
    this.attrForm.displayFullline = attrType.displayFullline;
    this.attrForm.dbType = attrType.dbType;
    this.attrForm.displayType = attrType.displayType;
    if(type == 'float'){
      this.attrForm.dbLength = attrType.dbLength-attrType.decimals;
      this.attrForm.decimals = attrType.decimals;
    }
    this.$refs['attrForm'].clearValidate();
  },

  // 组装部分字段验证规则
  // float 整数位数最大为 attrType.dbLength - attrType.decimals, 数据库中长度包含小数位数
  getItemRules(itemProp, attr, attrOldLength){
    let attrType = this.attributeTypes.find(t=>t.type == attr.type);
    if(itemProp == 'dbLength'){
      let itemLable = '最大长度';
      let itemLength = attrType.dbLength;
      if(attr.type=='float'){
        itemLable = '整数位数';
        itemLength = attrType.dbLength - attrType.decimals;
      }
      // 编辑时
      if(this.option.isEdit){
        return [
          { required: true, message: '请输入'+itemLable, trigger: 'blur' },
          { pattern: /^\+?[1-9]\d*$/, message: itemLable+'是正整数！' },
          { validator: (rule, value, callback)=>{
              if(value - itemLength > 0){
                callback(new Error(itemLable+'小于等于'+itemLength));
              }
              if(value - attrOldLength < 0){
                callback(new Error(itemLable+'大于等于'+attrOldLength));
              }
              callback();
            },
            trigger: 'blur'
          },
        ];
      } else {
        return [
          { required: true, message: '请输入'+itemLable, trigger: 'blur' },
          { pattern: /^\+?[1-9]\d*$/, message: itemLable+'是正整数！' },
          { validator: (rule, value, callback)=>{
              if(value - itemLength > 0){
                callback(new Error(itemLable+'小于等于'+itemLength));
              }
              callback();
            },
            trigger: 'blur'
          },
        ];
      }
    }

    if(itemProp == 'displayRegex'){
      if(attr.type == 'file'){
        return [
          { required: true, message: '请输入附件格式', trigger: 'blur' },
          { min: 0, max: 200, message: '长度不得超过 200 个字符', trigger: 'blur' }
        ]
      }
      if(attr.type == 'text'){
        return [
          { min: 0, max: 200, message: '长度不得超过 200 个字符', trigger: 'blur' }
        ]
      }
    }
    return null;
  },

  // 加载attribute 信息
  loadAttributeInfo(id){
    if(!id){
      return ;
    }
    return getFormObjectAttribute({
      id: id,
    }).then(res=>{
      this.attrForm = res;
      this.attrOldLength = res.dbLength;
    });
  },

  // 加载字段类型列表
  loadAttributeTypes(){
    return listAttributeTypes().then(res=>{
      this.attributeTypes = res;
    });
  },

  // 还原数据
  clear(){

    this.initFormData();
  },
  // 初始化表单数据
  initFormData(){
    this.attrForm.id = null;
    this.attrForm.name = null;
    this.attrForm.type = null;
    this.attrForm.dbLength = null;
    this.attrForm.displayMinlength = null;
    this.attrForm.notnull = '0';
    this.attrForm.unique = '0';
    this.attrForm.displayTitle = null;
    this.attrForm.displayRegex = null;
    this.attrForm.decimals = null;
    this.attrForm.dictTypeCode = null;
    this.attrForm.displayFlat = null;
    this.attrForm.displayFullline = '0';
    this.attrForm.dbType = null;
    this.attrForm.displayType = null;
    this.attrForm.objectId = null;
    this.$nextTick(()=>{
      this.$refs.attrForm.clearValidate();
    })
  },
  //获取字典类型
  getAllDicts() {
    return optionselect().then(res => {
      this.dictTypes=res.data;
    })
  },
  //获取业务对象
  getObjects(){
    return listAll().then(res => {
      this.objects = res.rows;
    });
  },
  // 加载数据
  async loadData(){
   // let loading = Loading.service({target: '#attributeEdit'});
    await this.getAllDicts();
    await this.loadAttributeTypes();
    await this.getObjects();

    if(this.option.id){
      await this.loadAttributeInfo(this.option.id);
    }else {
      this.initFormData();
      this.attrForm.objectId = this.option.objectId;
    }
   // loading.close();
  }
},
mounted() {
  // this.loadData();
}
}
</script>

<style>

</style>
