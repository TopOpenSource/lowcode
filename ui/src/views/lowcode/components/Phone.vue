<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
    <el-input v-model="inputValue" :placeholder="placeholder"></el-input>
    </template>
    <template v-else>
      <span>{{inputValue}}</span>
    </template>
  </el-form-item>

</template>
<script>
import {validator} from '@/views/lowcode/components/pub/validator'
import mixinProp from "@/views/lowcode/components/mixinProp";
export default {
  data(){
    return{
      inputValue:'',
    }
  },
  mixins: [mixinProp],
  watch:{
    inputValue: function (n, o) {
      this.$emit("input", n);
    },
    value:function(newValue,oldVal){
      if(this.value==undefined){
        this.inputValue='';
      }else{
        this.inputValue = this.value;
      }
    }
  },
  mounted(){
    if(this.value==undefined){
      this.inputValue='';
    }else{
      this.inputValue = this.value;
    }

    this.rules.push({validator: validator.phoneOrMobile, trigger: "blur"})
    //最大长度
    this.rules.push({max:this.maxlength, message: "最大长度为："+this.maxlength, trigger: "blur"});

    if(this.notnull=='1'){
      this.rules.push({required: true, message: "请输入数据", trigger: "blur"})
    }

    if (this.valids != undefined && this.valids.length > 0) {
      this.rules = this.rules.concat(this.valids);
    }
  }
}
</script>
