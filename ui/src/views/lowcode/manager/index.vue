<template>
  <div class="index-content">
    <el-collapse v-model="activeNames">
      <el-collapse-item :title="item.dictLabel" :name="index" v-for="(item,index) in bussiness" :key="index">
         <el-row :gutter="30">
           <el-col :sm="12" :md="6" :lg="4" :xl="3"  v-for="(b,index) in item.childs" :key="index">
               <div class="index-list" @click="handleEnter(b.id)">
                  <div class="list-icon" style="background-color: #409eff">
                    <!-- <svg-icon v-if="b.icon" :icon-class="b.icon" style="font-size: 20px; color: #FFFFFF"/> -->
                    <img :src="b.icon" class="icon">
                  </div>
                  <div class="list-title">
                     {{b.name}}
                  </div>
               </div>
           </el-col>
         </el-row>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import { getDicts } from "@/api/system/dict/data";
import {listAll} from "@/api/lowcode/config/formObject";


export default {
  data() {
    return {
      activeNames: [],
      dicts: [],
      list:[],
      bussiness:[]
    };
  },
  created(){
    getDicts("lowcode_object_type").then(res=>{
      const dicts = res.data;
      dicts.forEach((item) => {
        this.dicts.push({dictLabel:item.dictLabel,dictValue:item.dictValue});
      })
    });
    this.getList();
  },

  methods: {
    handleEnter(id) {
      this.$router.push({ path: '/lowcode/front_list', query: { formObjectId: id } })
    },
    getList() {
      this.loading = true;
      listAll().then(response => {
          this.list = response.rows;
          this.getBussiness();
          this.loading = false;
        }
      );
    },
    getBussiness() {
      const a = this.dicts;
      this.dicts.forEach((item,key)=>{
        this.$set(a[key],'childs',[])
        const dictValue = item.dictValue;
        this.list.forEach(item=>{
          if(dictValue == item.type){
            a[key].childs.push({name:item.name,id:item.id,icon:item.icon})
          }
        })
      });
      this.bussiness = a;
      this.bussiness.forEach((item,key)=>{
        this.activeNames.push(key)
      })
    },
  }
};
</script>

<style lang="scss" scoped>
.index-content{
  padding: 20px;
  background-color: #FFFFFF;
  .index-list{
    display: flex;
    /*justify-content: center;*/
    align-items: center;
    padding: 12px 20px;
    border: 2px solid #e8e8e8;
    border-radius: 6px;
    margin-bottom: 10px;
    .list-icon{
      width: 40px;
      height: 40px;
      background-color: #409eff;
      border-radius: 10px;
      text-align: center;
      line-height: 40px;
      i{
        color: #FFFFFF;
        display: inline-block;
        font-size: 20px;
        padding-top: 10px;
      }
    }
    .list-title{
      padding-left: 20px;
      font-size: 14px;
    }
     .el-collapse-item__header{
      font-size: 16px !important;
    }
    .el-collapse-item__content{
      padding: 10px 20px 20px 20px;
    }
    .icon {
      cursor: pointer;
      width: 40px;
      height: 40px;
      border-radius: 10px;
    }
  }
}
</style>
