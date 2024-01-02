<template>
<section>
   <el-row :gutter="20" class="row-margin2">
        <el-col :span="24">
          <el-form label-width="90px" ref="form" :model="form" :rules="form_rules">
              <el-form-item label="布局名称" prop="name">
                <el-input placeholder="布局名称" v-model="form.name"></el-input>
              </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="6" class="sys-left">
          <el-scrollbar wrap-class="scrollbar-list-Layout">
            <div class="sys-menu">
              <section class="sysmenu-title">
                <i class="el-icon-menu sysmenu-title-icon"></i>
                <span class="sysmenu-title-text">表单元素</span>
              </section>
              <draggable :list="leftDrag.attrs" :group="{ name: 'attr', pull: 'clone', put: false }"
                         :clone="cloneLeftAttr" filter=".disable_attr">
                <!--如果右侧包含则显示禁用样式-->
                <div v-for="attr in leftDrag.attrs"
                     :key="attr.id"
                     :class="rightIncludes(attr.id)?'disable_attr':'sys-menuLay'">
                  {{ attr.name }}
                </div>
              </draggable>
            </div>
          </el-scrollbar>
        </el-col>
        <el-col :span="18" class="sys-right">
            <el-row>
              <el-col :sm="24" :md="24" :lg="24" class="sys-right-content">
                <section class="sysmenu-title">
                  详情页面布局
                  <span style=" color:#F56C6C; font-size:12px; ">(从左侧选择拖拽字段到指定位置)</span>
                </section>
                <el-form label-width="0">
                  <draggable :list="rightDrag.attrs" group="attr">
                    <section class="sysmenu-item" v-for="(attr,index) in rightDrag.attrs" :key="index">
                      <!--分割线-->
                      <el-row  v-if="attr.displayType=='splitline'">
                        <el-col :sm="20" :md="20" :lg="20">
                          <div class="top-content-title">
                            <span></span>
                            <el-input v-model="attr.displayTitle" placeholder="请输入标题"></el-input>
                          </div>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'" style="margin-top: 10px">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--输入框 数字-->
                      <el-row  v-if="attr.displayType=='textarea'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="16" :md="16" :lg="16" class="center-sysmc">
                          <el-input v-model="attr.value" type="textarea"></el-input>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--文本框-->
                      <el-row  v-if="attr.displayType=='input' || attr.displayType=='number'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <el-input v-model="attr.value"></el-input>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--日期-->
                      <el-row  v-if="attr.displayType=='date'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <el-date-picker type="date" placeholder="选择日期"></el-date-picker>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--日期+时间-->
                      <el-row  v-if="attr.displayType=='datetime'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <el-date-picker type="datetime" placeholder="选择日期"></el-date-picker>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--单选-->
                      <el-row  v-if="attr.displayType=='radio'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <!--平铺-->
                          <template v-if="attr.displayFlat=='1'">
                            <el-radio v-model="rightDrag.options.value" label="1">选项1</el-radio>
                            <el-radio v-model="rightDrag.options.value" label="2">选项2</el-radio>
                          </template>
                          <!--下拉-->
                          <template v-if="attr.displayFlat=='0'">
                            <el-select placeholder="请选择" v-model="rightDrag.options.value">
                              <el-option :label="'选项1'" :value="'0'"></el-option>
                              <el-option :label="'选项2'" :value="'0'"></el-option>
                            </el-select>
                          </template>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>
                      <!--多选-->
                      <el-row  v-if="attr.displayType=='checkbox'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <!--平铺-->
                          <template v-if="attr.displayFlat=='1'">
                            <el-checkbox v-model="rightDrag.options.value">选项1</el-checkbox>
                            <el-checkbox v-model="rightDrag.options.value">选项2</el-checkbox>
                          </template>
                          <!--下拉-->
                          <template v-if="attr.displayFlat=='0'">
                            <el-select placeholder="请选择" multiple v-model="rightDrag.options.value2">
                              <el-option :label="'选项1'" :value="'0'"></el-option>
                              <el-option :label="'选项2'" :value="'0'"></el-option>
                            </el-select>
                          </template>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>
                      <!--文件-->
                      <el-row  v-if="attr.displayType=='file'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <el-upload
                              disabled
                              class="upload-demo"
                              multiple
                              action=""
                              :limit="3"
                              :file-list="rightDrag.options.fileList">
                            <el-button size="small" type="primary" disabled>点击上传</el-button>
                          </el-upload>
                        </el-col>
                        <!--删除按钮-->
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>
                      <!--组织-->
                      <el-row  v-if="attr.displayType=='org'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <template>
                            <el-select placeholder="请选择" v-model="rightDrag.options.value">
                              <el-option :label="'选项1'" :value="'0'"></el-option>
                              <el-option :label="'选项2'" :value="'0'"></el-option>
                            </el-select>
                          </template>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>
                      <!--用户-->
                      <el-row v-if="attr.displayType=='user'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <template >
                            <el-select placeholder="请选择" v-model="rightDrag.options.value">
                              <el-option :label="'选项1'" :value="'0'"></el-option>
                              <el-option :label="'选项2'" :value="'0'"></el-option>
                            </el-select>
                          </template>
                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                      <!--关联关系-->
                      <el-row v-if="attr.displayType=='relation'" class="layout-item">
                        <el-col :sm="6" :md="6" :lg="6" class="left-sysmc">
                          {{ attr.name }}
                        </el-col>
                        <el-col :sm="8" :md="8" :lg="8" class="center-sysmc">
                          <el-button type="primary" plain icon="el-icon-plus" size="mini" >新增</el-button>

                          <el-table :data="rightDrag.relationList"  stripe  style="width: 100%">
                            <el-table-column  prop="name" label="姓名" width="80">
                            </el-table-column>
                            <el-table-column prop="age" label="年龄" width="80">
                            </el-table-column>
                            <el-table-column prop="job" label="岗位">
                            </el-table-column>
                            <el-table-column  label="操作">
                               <template>
                                 <el-button type="primary" plain icon="el-icon-delete" size="mini" >删除</el-button>
                               </template>
                            </el-table-column>
                          </el-table>

                        </el-col>
                        <el-col :sm="4" :md="4" :lg="4" class="btn-sys" v-if="attr.isDefault!='1'">
                          <el-button icon="el-icon-close" @click="deleteAttr(index)" type="text"></el-button>
                        </el-col>
                      </el-row>

                    </section>
                  </draggable>
                </el-form>
              </el-col>
            </el-row>
        </el-col>


      </el-row>
      <!--      底部按钮-->
      <div class="footer-icon">
      <el-button  @click="cancel" size="medium">取消</el-button>
      <el-button type="primary" @click="saveOrUpdate" size="medium">保存</el-button>
      </div>
</section>
</template>

<script>
import draggable from "vuedraggable";
import {selectAttrsByDto} from "@/api/lowcode/config/form_config/attribute";
import {selectFormLayoutById, saveOrUpdateFormLayout} from "@/api/lowcode/config/form_config/formLayout"

export default {
  data() {
    return {
      loading:false,
      form: {
        id: '',
        objectId: '',
        name: '',
        content: '',
        type: ''
      },
      form_rules: {
        name: [
          {required: true, message: '请输入布局名称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },
      leftDrag: {
        attrs: [],
        attrsMap: new Map()
      },
      rightDrag: {
        attrs: [],
        options: {
          fileList: [{name: '文件1.docx', url: ''},
            {name: '文件2.docx', url: ''}],
          value: '',
          value2: [],
        },
        relationList:[{name:'张小华',age:18,job:'工程师'}]
      }
    };
  },

  methods: {
    deleteAttr(index) {
      this.rightDrag.attrs.splice(index, 1);
    },
    //拖动clone
    cloneLeftAttr(obj) {
      return Object.assign({}, obj);
    },
    rightIncludes(id) {
      if (id == '-1') {
        return false;
      } else {
        let attr = this.rightDrag.attrs.filter(attr => {
          return attr.id == id;
        });
        if (attr != null && attr.length > 0) {
          return true;
        } else {
          return false;
        }
      }

    },
    //取消
    cancel() {
      this.$emit('close')
    },
    //保存
    saveOrUpdate() {
      this.loading=true;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let attrs = this.rightDrag.attrs.map(rightAttr => {
            return {id: rightAttr.id, displayTitle: rightAttr.displayTitle};
          })
          this.form.content = JSON.stringify(attrs);
          saveOrUpdateFormLayout(this.form).then(res => {
            this.$notify({message: '保存成功', type: 'success'});
            this.$emit("afterSubmit", this.form.type);
            this.loading=false;
          }).catch(e => {
            this.loading=false;
          });
        } else {
          this.loading=false;
        }
      });
    },
    //将content转为attr
    parseContent2Attr(content) {
      //将 leftAttrs 转存 map
      this.leftDrag.attrs.forEach(leftAttr => {
        this.leftDrag.attrsMap.set(leftAttr["id"], leftAttr);
      });
      //遍历content attrid 转为 attr
      let rightAttrs = JSON.parse(content);

      rightAttrs.forEach(rightAttr => {
        //查询左侧对应数据并clone
        let rightRefLeftAttr = this.leftDrag.attrsMap.get(rightAttr.id + "");

        if (rightRefLeftAttr != undefined && rightRefLeftAttr != null) {
          rightRefLeftAttr = Object.assign({}, rightRefLeftAttr);
          rightRefLeftAttr.displayTitle = rightAttr.displayTitle;
          this.rightDrag.attrs.push(rightRefLeftAttr);
        }
      });
    },
    clear() {
      this.form = {
        id: '',
        objectId: '',
        name: '',
        content: '',
        type: ''
      };
      this.leftDrag.attrs = [];
      this.leftDrag.attrsMap = new Map();
      this.rightDrag.attrs = [];
    },
    //加载数据
    async loadData(id, type, objectId) {
      this.loading=true;
      this.clear();
      //赋值
      this.form.objectId = objectId;
      this.form.type = type;
      //查询业务对象的属性
      await selectAttrsByDto({objectId: this.form.objectId, enable: '1'}).then(res => {
        //左侧属性
        this.leftDrag.attrs = res.filter(attr=>{
          return attr.alias !='create_account'
            && attr.alias !='modified_account'
            && attr.alias !='gmt_create'
            && attr.alias !='gmt_modified'
        });
        //追加 标题字段
        this.leftDrag.attrs.unshift({
          id: '-1',
          name: "标题",
          displayTitle: "标题",
          displayType: "splitline",
          type: "splitline",
          value: ''
        });
      }).catch(e => {
        this.loading=false;
      });

      //查询业务对象的属性
      if (id != null) {
        this.form.id = id;
        await selectFormLayoutById({id: id}).then(res => {
          this.parseContent2Attr(res.content);
          this.form = res;
        }).catch(e => {
          this.loading=false;
        });
      }else{
        //右侧展示默认字段
        /*this.rightDrag.attrs =this.leftDrag.attrs.filter(attr=>{
          return attr.isDefault=='1';
        });*/
      }
      this.loading=false;
    }
  },
  components: {
    draggable
  },
  mounted() {

  }
};
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.disable_attr {
  line-height: 32px;
  border: solid 1px #e6e6e6;
  padding: 0 10px;
  background-color: #f3f3f3;
  margin: 6px;
  color: #c5c3c3;
}

.sysmenu-title{
  //background-color: #EFF4F8;
  line-height: 40px;
  font-weight: bold;
  color: #333333;
  /*padding: 0 10px;*/
  font-size: 15px;
}
.sys-menuLay{
  line-height: 32px;
  border: solid 1px #e6e6e6;
  padding: 0 10px;
  background-color: #ffffff;
  margin: 6px 0;
}
.sys-menuLay:hover{
  background-color: #EFF4F8;
}
.left-sysmc{
  line-height: 32px;
  font-size: 13px;
}

.right-checkbox{
  padding-top: 6px;
}

.sysmenu-item .el-form-item--mini.el-form-item, .sysmenu-item .el-form-item--small.el-form-item {
  margin-bottom: 0px;
}

</style>
