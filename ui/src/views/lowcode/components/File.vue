<template>
  <el-form-item :label="label" :rules="rules" :prop="prop">
      <el-upload
        size="small"
        v-if="edit"
        ref="upload"
        :limit="50"
        :accept="fileTypes==undefined?'*':fileTypes"
        :action="upload.url"
        :headers="upload.headers"
        :show-file-list="upload.showFileList"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="true">
        <el-button plain icon="el-icon-plus" size="mini"  type="primary">选取文件</el-button>
        <div slot="tip" class="el-upload__tip">{{ placeholder }}</div>
      </el-upload>

    <el-table ref="tables"  :data="fileList">
      <el-table-column label="文件名称"  prop="name"  />
      <el-table-column label="文件大小(bit)"  prop="size"  />
      <el-table-column label="上传时间"  prop="upTime" :formatter="formatDate">

      </el-table-column>

      <el-table-column label="操作" class-name="small-padding fixed-width" width="120px">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleDownload(scope.row)">下载</el-button>
          <el-button type="text" size="mini" @click="handleFileDel(scope.row)" v-if="edit">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

  </el-form-item>

</template>

<script>

import { getToken } from "@/utils/auth";
import {listFiles, downloadFile} from "@/api/lowcode/instance/instance";
import mixinProp from "@/views/lowcode/components/mixinProp";
import {parseTime} from "@/utils/ruoyi";

export default {
  data() {
    return {
      upload: {
        isUploading: false,// 是否禁用上传
        showFileList:false,//是否显示文件列表
        headers: { Authorization: "Bearer " + getToken() },// 设置上传的请求头部
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/lowcode/lowcode/file/upload",
      },
      fileList:[]
    }
  },
  mixins: [mixinProp],
  props: {
    fileTypes: null,
    attrId:null,
    instanceId:null,
  },
  watch: {
    instanceId:function(newVal,oldVal){
      console.info(newVal)
      this.loadData()
    }
  },
  methods: {
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false;
      if(response.data){
        this.fileList.push(response.data)
        this.$emit("input", this.fileList.map(row=> row.id).join(","));
      }
    },
    //删除文件
    handleFileDel(row){
      this.fileList.forEach((file,index)=>{
        if(file.id==row.id){
          this.fileList.splice(index,1)
          this.$emit("input", this.fileList.map(row=> row.id).join(","));
        }
      })
    },
    //下载文件
    handleDownload(row) {
      var name = row.name;
      var id = row.id;
      downloadFile(id,name);
    },
    loadData(){
       if(this.instanceId && this.attrId){
         //加载关联的文件数据
         listFiles({instId:this.instanceId,attrId:this.attrId}).then(res=>{
           this.fileList=res;

           this.$emit("input", this.fileList.map(row=> row.id).join(","));
         })
       }
    },
    formatDate(row, column) {
      return parseTime(row.upTime)
    }
  },
  mounted() {
    this.$nextTick(() => {
      //是否为空
      if(this.notnull=='1'){
        this.rules.push({required: true, message: "请选择", trigger: "blur"})
      }
    });
  }

}
</script>

<style scoped>

</style>
