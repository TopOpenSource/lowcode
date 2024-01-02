<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
      <el-input type="textarea" v-model="inputValue" :maxlength="maxlength"  :placeholder="placeholder"></el-input>
    </template>
    <template v-else>
      <span>{{ inputValue }}</span>
    </template>
  </el-form-item>

</template>
<script>
import mixinProp from "@/views/lowcode/components/mixinProp";

export default {
  data() {
    return {
      inputValue: '',
    }
  },
  mixins: [mixinProp],
  watch: {
    inputValue: function (n, o) {
      this.$emit("input", n);
    },
    value: function (newValue, oldVal) {
      if (this.value == undefined) {
        this.inputValue = '';
      } else {
        this.inputValue = this.value;
      }
    }
  },
  mounted() {
    if (this.value == undefined) {
      this.inputValue = '';
    } else {
      this.inputValue = this.value;
    }

    //最大长度 最大长度
    let min=0;
    if(this.minLength!=null){
      min=this.minLength;
    }
    this.rules.push({ min:min, max:this.maxlength, message: "最小长度为："+min+",最大长度为："+this.maxlength, trigger: "blur"});

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
