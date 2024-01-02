<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
    <!--平铺-->
    <el-radio-group v-model="inputValue" v-if="displayFlat=='1'" :placeholder="placeholder">
      <template v-for="option in options">
        <el-radio :label="option.code">{{ option.name }}</el-radio>
      </template>
    </el-radio-group>
    <!--下拉-->
    <el-select v-model="inputValue" placeholder="请选择" v-if="displayFlat=='0'" :placeholder="placeholder">
      <el-option v-for="item in options" :key="item.code" :label="item.name" :value="item.code">
      </el-option>
    </el-select>
    </template>
    <template v-else>
      <template v-for="item in options">
        <span class="tag-pad" v-if="item.code==inputValue"><el-tag>{{item.name}}</el-tag></span>
      </template>
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
  props: {
    options:null,
    displayFlat:{
      type:String,
      default: '0'
    },
  },
  watch:{
    inputValue: function (n, o) {
      this.$emit("input", n);
    },
    value:function(newValue,oldVal){
      if(oldVal==undefined){
        if(this.value==undefined){
          this.inputValue='';
        }else{
          this.inputValue = this.value;
        }
      }else{
        this.inputValue = newValue;
      }

    }
  },
  mounted(){
    if(this.value){
      this.inputValue=this.value
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
