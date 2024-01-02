<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
      <el-cascader  v-model="inputValue" :options="deptOptions" :props="props"  clearable></el-cascader>
    </template>
    <template v-else>
      <template v-for="item in deptList">
        <span class="tag-pad" v-if="inputValue.includes(item.deptId)"><el-tag>{{ item.deptName }}</el-tag></span>
      </template>
    </template>
  </el-form-item>
</template>

<script>

import mixinProp from "@/views/lowcode/components/mixinProp";

export default {
  name: 'Org',
  data() {
    return{
      inputValue: [],
      deptOptions:null,
      props:{
        value:'deptId',
        label:'deptName',
        emitPath:false,
        multiple:false
      }
    }
  },
  mixins: [mixinProp],
  props: {
    multiple: {
      type: String,
      default: '0'
    },
    deptList: {
      type:Array,
    },
    //扩展  增加登陆人
    ext:{
      type: String,
      default: '0'
    }
  },
  methods: {

  },
  watch:{
    inputValue: function (newVal, oldVal) {
      if(this.multiple==1){
        //多选
        this.$emit("input",newVal.join(","));
      }else{
        this.$emit("input",newVal+"");
      }
    },
    value:function (newVal,oldVal){
      //首次赋值
      if (oldVal == undefined) {
        //多选赋值
        if (this.multiple == 1) {
          if (newVal == undefined || newVal == "") {
            this.inputValue = [];
          } else {
            this.inputValue = newVal.split(",");
          }
        } else {
          //单选赋值
          this.inputValue = newVal
        }
      }
    }

  },
  mounted() {
    this.$nextTick(() => {
      this.props.multiple=(this.multiple==1);

      //增加登陆部门
      if(this.ext=='1'){
        let hasLogin= this.deptList.filter(user=>{
          return user.deptList=='{LOGIN_ORG}'
        })

        if(hasLogin.length<1){
          this.deptList.unshift({deptList:'{LOGIN_ORG}',deptName:"--登陆部门--",parentId:null})
        }
      }
      this.deptOptions=this.handleTree(this.deptList, "deptId");
      //是否为空
      if (this.notnull == '1') {
        this.rules.push({required: true, message: "请选择", trigger: "blur"})
      }
    });

  }
}
</script>

<style scoped>

</style>
