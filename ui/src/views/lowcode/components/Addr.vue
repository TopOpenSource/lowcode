<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
      <el-cascader size="large" :options="options" v-model="inputValue" :props="{ checkStrictly: true }" :placeholder="placeholder"></el-cascader>
    </template>
    <template v-else>
      <span>{{ showValue }}</span>
    </template>
  </el-form-item>

</template>
<script>
import {regionData,codeToText} from "./area/app.js";
import mixinProp from "./mixinProp";
export default {
  data() {
    return {
      options:regionData,
      inputValue: '',
      showValue:'',
    }
  },
  mixins: [mixinProp],
  watch: {
    inputValue: function (n, o) {
      if(this.inputValue.length>0){
        this.$emit("input",this.inputValue.join("/"));
      }else{
        this.$emit("input","");
      }
    },
    value: function (newValue, oldVal) {
      if(this.value==undefined || this.value ==""){
        this.inputValue=[];
      }else{
        this.inputValue = this.value.split("/");
        this.showValue = '';
        this.value.split("/").forEach(val=>{
          this.showValue=this.showValue+codeToText[val]
        });
      }
    }
  },
  mounted() {
    //是否为空
    if (this.notnull == '1') {
      this.rules.push({required: true, message: "请输入数据", trigger: "blur"})
    }
    //自定义验证
    if (this.regex != undefined && this.regex != '') {
      this.rules.push({
        validator: (rule, value, callback) => {
          let regex = new RegExp(this.regex, 'i');
          if (value == undefined || value == '') {
            callback();
          } else if (!(regex.test(value))) {
            callback(new Error('格式不正确！'))
          } else {
            callback();
          }
        }, trigger: "blur"
      })
    }

    if (this.valids != undefined && this.valids.length > 0) {
      this.rules = this.rules.concat(this.valids);
    }
  }
}
</script>
