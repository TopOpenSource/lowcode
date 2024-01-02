<script src="../../../api/lowcode/instance/instance.js"></script>
<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">

    <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-if="edit">新增</el-button>

    <el-table ref="tables"  :data="tableData">
      <template v-for="col in columns">
        <el-table-column :label="col.name" align="center">
          <template slot-scope="{row, column}">
            {{ formatterVal(col, row, column) }}
          </template>
        </el-table-column>
      </template>

      <el-table-column label="操作" v-if="edit">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleDel(scope.row)" >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
     <relation-select ref="relationSel" @select="handleSelect"></relation-select>
  </el-form-item>
</template>

<script>
import Util from "@/views/lowcode/task/Util";
import store from "@/store";
let util = new Util();
import mixinProp from "./mixinProp";
import RelationSelect from "@/views/lowcode/components/RelationSelect";
import {selRefView,listRelInstance} from "@/api/lowcode/instance/instance";
export default {
  name: "Relation",
  data(){
    return{
      tableData:[],
      view:null,
      //显示列
      columns:[],
      //属性 所有的
      attrs:[],
      //字典
      dictMap:store.getters.dictMap,
      //用户
      userMap:store.getters.userMap,
    }
  },
  components:{
    "relation-select":RelationSelect
  },
  watch:{
    inputValue: function (n, o) {

    },
    value:function(newValue,oldVal){

    }
  },
  mixins: [mixinProp],
  props: {
    objectId:null,
    attrId:null,
    instanceId:null,
  },
  methods:{
    formatterVal(col, row, column){
      return util.formatterVal(col,row,this.dictMap,this.userMap)
    },
    handleAdd(){
      this.$refs.relationSel.show=true
      this.$refs.relationSel.loadView(this.objectId,this.view,this.attrs)
    },
    loadRelation(){
      listRelInstance(
        {
          targetObjId:this.objectId,
          sourceAttrId:this.attrId,
          sourceInstId:this.instanceId,
          tableColumn:this.view.tableColumn})
        .then(res=>{
           this.tableData=res
         })
    },
    loadView(){
      selRefView(this.objectId).then(res=>{
        this.attrs=res.attrs
        this.view=res.view

        this.columns=util.parseCols2Attrs(this.attrs,this.view.tableColumn)

        if(!this.edit){
          this.loadRelation()
        }
      })
    },
    handleSelect(rows){
      this.tableData=rows
      this.$emit("input", this.tableData.map(row=> row.id).join(","));
    },
    handleDel(row){
      this.tableData.forEach((col,index)=>{
        if(col.id==row.id){
          this.tableData.splice(index,1)
          this.$emit("input", this.tableData.map(row=> row.id).join(","));
        }
      })

    }
  },
  mounted(){
    this.$nextTick(() => {
      //是否为空
      if(this.notnull=='1'){
        this.rules.push({required: true, message: "请选择", trigger: "blur"})
      }

      this.loadView()
    });
  }
}
</script>

<style scoped>

</style>
