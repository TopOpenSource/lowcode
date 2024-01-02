<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px">
    <el-col :span="24">
      <el-form-item label="节点名称" prop="name">
         <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="审批人设置" prop="auditUserType">
        <el-radio-group v-model="form.auditUserType">
          <el-radio :label="0">指定人</el-radio>
          <el-radio :label="1">指定角色</el-radio>
          <el-radio :label="2">部门主管</el-radio>
          <el-radio :label="3">发起人本人</el-radio>
          <el-radio :label="4">发起人自选</el-radio>
          <el-radio :label="5">表单成员</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-col>

    <el-col :span="24" v-if="form.auditUserType==0">
      <el-form-item label="选择审批人" prop="users">
        <el-select filterable multiple v-model="form.users">
          <el-option
            v-for="item in userList"
            :key="item.userName"
            :label="item.nickName"
            :value="item.userName"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item label="选择角色" v-if="form.auditUserType==1" prop="roles">
        <el-select v-model="form.roles" multiple filterable placeholder="请选择">
          <el-option
            v-for="item in roleList"
            :key="item.roleId"
            :label="item.roleName"
            :value="item.roleId">
          </el-option>
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item label="表单成员字段" v-if="form.auditUserType==5" prop="attrs">
        <el-select v-model="form.attrs" multiple filterable placeholder="请选择">
          <el-option
            v-for="item in attrList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="24">
      <el-form-item label="多人审批方式" v-if="form.auditUserType!=3" prop="auditType">
        <el-select v-model="form.auditType" filterable placeholder="请选择">
          <el-option
            v-for="item in auditTypeList"
            :key="item.id"
            :label="item.label"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </el-col>
  </el-form>
</template>

<script>

import mixinProperties from "../mixinProperties";
import store from "@/store";

export default {
  name: "assignee",
  mixins: [mixinProperties],
  data() {
    return {
      userList: store.getters.userList,
      roleList: store.getters.roleList,
      attrList: [],
      auditTypeList: [
        {id: 0, label: '会签（所有审批人同意）'},
        {id: 1, label: '或签（一名审批人同意即可）'},
        {id: 2, label: '依次审批（按顺序依次审批）'}
      ],
      form: {
        name:null,
        auditUserType: 0,//审批人类型
        users: [],
        roles: [],
        attrs: [],
        auditType: 0
      },
      rules: {
        name: [
          { required: true, message: "不能为空", trigger: "blur" },
          { min: 2, max: 20, message: '长度必须介于 2 和 10 之间', trigger: 'blur' }
        ],
        auditUserType:[
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        users:[
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        roles:[
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        attrs:[
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        auditType:[
          { required: true, message: "不能为空", trigger: "blur" },
        ]
      }
    }
  },
  methods: {
    save() {
      let result=false
      this.$refs["form"].validate(valid => {
        if (valid) {
            let elmentId=this.element.id;

            this.updateProperties( {'name':this.form.name});

            //设置待办人
            if (this.form.auditUserType == 0) {
              this.setLoopCharacteristics("${flowableToolService.getByUsers('"+this.form.users.join(",")+"')}",this.form.auditType);
            }

            if (this.form.auditUserType == 1) {
              this.setLoopCharacteristics("${flowableToolService.getByRoles('"+this.form.roles.join(",")+"')}",this.form.auditType);
            }

            if (this.form.auditUserType == 2) {
              this.setLoopCharacteristics("${flowableToolService.getByLeader(applyUserName)}",this.form.auditType);
            }

            if (this.form.auditUserType == 3) {
              delete this.element.businessObject.loopCharacteristics
              this.updateProperties( {'flowable:assignee': '${applyUserName}'})
            }

            if (this.form.auditUserType == 4) {
              this.setLoopCharacteristics("${flowableToolService.getByDiy(instanceId,'"+elmentId+"')}",this.form.auditType);
            }

            if (this.form.auditUserType == 5) {
              this.setLoopCharacteristics("${flowableToolService.getByAttrs(objectId,instanceId,'"+this.form.attrs.join(",")+"')}",this.form.auditType);
            }

            //保存配置文件
            if(!this.config[this.element.id]){
              this.config[this.element.id]={}
            }

            this.config[this.element.id]['assignee']={
              name:this.form.name,
              auditUserType: this.form.auditUserType,//审批人类型
              users: this.form.users,
              roles: this.form.roles,
              attrs: this.form.attrs,
              auditType: this.form.auditType
            }

          result=true
        }else{
          this.$modal.msgError("任务信息验证失败，请检查！");
        }

      })
      return result
    },

    setLoopCharacteristics(collection, auditType) {
      let loopCharacteristics = this.modeler.get('moddle').create('bpmn:MultiInstanceLoopCharacteristics')
      loopCharacteristics['isSequential'] = (auditType==2)
      loopCharacteristics['collection'] = collection
      loopCharacteristics['elementVariable'] = 'username'
      let completionCondition = (auditType == 1 ? "${nrOfCompletedInstances > 0 }" : "${nrOfCompletedInstances==nrOfInstances}")
      loopCharacteristics['completionCondition'] = this.modeler.get('moddle').create('bpmn:Expression', {body: completionCondition})
      this.updateProperties( {'flowable:assignee': '${username}',loopCharacteristics: loopCharacteristics})
    },


    clear(){
      this.form={
          name:null,
          auditUserType: 0,
          users: [],
          roles: [],
          attrs: [],
          auditType: 0
      };
      this.resetForm("form");
    },
    initData(){
      this.clear()

      if(store.getters.attrMap.get(this.objectId)){
        //加载用户类型的字段
        this.attrList =store.getters.attrMap.get(this.objectId).filter(res=>{
          return res.type==='user'
        })
      }else{
        this.$modal.msgError("找不到字段，请先配置表单！");
      }

      //数据回填
      if(this.config && this.config[this.element.id] && this.config[this.element.id]['assignee']){
        this.form=this.config[this.element.id]['assignee']
      }
    }
  },
  watch:{

  },
  mounted() {
    this.$nextTick(() => {


    });
  }
}
</script>

<style scoped>

</style>
