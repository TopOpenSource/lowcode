<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
      <el-input v-model="inputValue" :placeholder="placeholder"></el-input>
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
  props: {
    //小数位数
    decimals: {
      type: Number,
      default: 0
    }
  },
  watch: {
    inputValue: function (n, o) {
      this.$emit("input", n);
    },
    value: function (newValue, oldVal) {
      if (this.value == undefined) {
        this.inputValue = '';
      } else {
        this.inputValue = this.value.toString();
      }
    }
  },
  mounted() {
    if (this.value == undefined) {
      this.inputValue = '';
    } else {
      this.inputValue = this.value;
    }

    //是否为空
    if (this.notnull == '1') {
      this.rules.push({required: true, message: "请输入数据", trigger: "blur"})
    }
    //整数
    if (this.decimals == undefined || this.decimals == 0) {
      this.rules.push({
        validator: (rule, value, callback) => {
          let regex = new RegExp("^-?[1-9]\\d*|[0]{1,1}$", 'i');
          if (value == undefined || value == '') {
            callback();
          } else if (!(regex.test(value))) {
            callback(new Error('格式不正确！'))
          } else {
            callback();
          }
        }, trigger: "blur"
      })

    } else {
      //小数
      this.rules.push({
        validator: (rule, value, callback) => {
          let regex = new RegExp("(^([-]?)[0-9]+\\.[0-9]{" + this.decimals + "}$)", 'i');
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

    //长度
    this.rules.push({
      validator: (rule, value, callback) => {
        if (value != undefined && value != '') {
          let vals = value.replace("-", "").split(".");
          //整数位
          let intLength=vals[0].length;
          let decimalLength=0;
          if (vals.length > 1) {
            //小数位
            let decimalLength=vals[1].length;
          }
          if(intLength>(this.maxlength-this.decimals)||decimalLength>this.decimals){
            if(this.decimals>0){
              callback(new Error("数字长度不正确：整数长度为："+(this.maxlength-this.decimals)+",小数位数为："+this.decimals));
            }else{
              callback(new Error("数字长度不正确：长度为："+(this.maxlength-this.decimals)));
            }
          }else{
            callback();
          }
        } else {
          callback();
        }

      }, trigger: "blur"
    });

    //其他验证
    if (this.valids != undefined && this.valids.length > 0) {
      this.rules = this.rules.concat(this.valids);
    }
  }
}
</script>
