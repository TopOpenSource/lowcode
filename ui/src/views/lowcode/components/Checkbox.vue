<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
    <!--平铺-->
    <el-checkbox-group v-model="inputValue" v-if="displayFlat=='1'" :placeholder="placeholder">
      <template v-for="option in options">
      <el-checkbox :label="option.code">{{ option.name }}</el-checkbox>
      </template>
    </el-checkbox-group>

    <!--下拉-->
    <el-select v-model="inputValue" placeholder="请选择" multiple  v-if="displayFlat=='0'" :placeholder="placeholder">
      <el-option v-for="item in options" :key="item.code" :label="item.name" :value="item.code">
      </el-option>
    </el-select>
    </template>
    <template v-else>
      <template v-for="item in options">
        <span class="tag-pad" v-if="inputValue.includes(item.code)"><el-tag>{{item.name}}</el-tag></span>
      </template>
    </template>
  </el-form-item>
</template>
<script>
import mixinProp from "@/views/lowcode/components/mixinProp";

export default {
  data(){
    return{
      inputValue:[],
    }
  },
  mixins: [mixinProp],
  props: {
    options:null,
    displayFlat:{
      type:String,
      default: '0'
    }
  },
  watch:{
    inputValue: function (n, o) {
      //[1,2] ->  "1,2"
      if(this.inputValue.length>0){
        this.$emit("input",this.inputValue.join(","));
      }else{
        this.$emit("input","");
      }
    },
    value:function(newValue,oldVal){
      if(this.value==undefined || this.value ==""){
        this.inputValue=[];
      }else{
        this.inputValue = this.value.split(",");
      }
    }
  },
  mounted(){
    if(this.value==undefined || this.value ==""){

    }else{
      this.inputValue = this.value.split(",");
    }
    //是否为空
    if(this.notnull=='1'){
      this.rules.push({required: true, message: "请选择", trigger: "blur"})
    }

    if(this.valids!=undefined && this.valids.length>0){
      this.rules=this.rules.concat(this.valids);
    }
  }
}
</script>
