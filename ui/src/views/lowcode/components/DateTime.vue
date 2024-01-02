<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
    <el-date-picker v-model="inputValue" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="placeholder"></el-date-picker>
    </template>
    <template v-else>
      <span>{{inputValue}}</span>
    </template>
  </el-form-item>

</template>
<script>
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
      if(n==null||n==""){
        this.$emit("input", null);
      }else{
        this.$emit("input", n);
      }
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

    //是否为空
    if(this.notnull=='1'){
      this.rules.push({required: true, message: "请输入数据", trigger: "blur"})
    }

    if(this.valids!=undefined && this.valids.length>0){
      this.rules=this.rules.concat(this.valids);
    }

  }
}
</script>
