<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
    <template v-if="edit">
      <el-select filterable v-model="inputValue" :placeholder="placeholder" :multiple="multiple == '0' ? false : true">
        <el-option
          v-for="item in userList"
          :key="item.userName"
          :label="item.nickName"
          :value="item.userName"
        ></el-option>
      </el-select>
    </template>

    <template v-else>
      <template v-for="item in userList">
        <span class="tag-pad" v-if="inputValue.includes(item.userName)"><el-tag>{{ item.nickName }}</el-tag></span>
      </template>
    </template>
  </el-form-item>
</template>

<script>

import mixinProp from "@/views/lowcode/components/mixinProp";

export default {
  name: 'User',
  data() {
    return {
      inputValue: [],
    }
  },
  mixins: [mixinProp],
  props: {
    multiple: {
      type: String,
      default: '0'
    },
    userList: {
      type: Array,
      default: []
    },
    //扩展  增加登陆人
    ext:{
      type: String,
      default: '0'
    }
  },
  methods: {},
  watch: {
    inputValue: function (newVal, oldVal) {
      //[1,2] ->  "1,2"
      if (this.multiple == 1) {
        //多选
        this.$emit("input", newVal.join(","));
      } else {
        this.$emit("input", newVal);
      }
    },
    value: function (newVal, oldVal) {
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
      //增加登陆人标志
      if(this.ext=='1'){
       let hasLogin= this.userList.filter(user=>{
          return user.userName=='{LOGIN_USER}'
        })

       if(hasLogin.length<1){
         this.userList.unshift({userName:'{LOGIN_USER}',nickName:"--登陆人--"})
       }
      }

      //是否为空
      if (this.notnull == '1') {
        this.rules.push({required: true, message: "请选择", trigger: "blur"})
      }

    })

  }
}
</script>

<style scoped>

</style>
