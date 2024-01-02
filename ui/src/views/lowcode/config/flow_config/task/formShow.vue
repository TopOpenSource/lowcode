<template>
  <div>
    <el-table :data="attrList" style="width: 100%">
      <el-table-column prop="name" label="属性名称"/>

      <el-table-column label="展示">
        <template slot-scope="scope">
          <el-radio-group v-model="scope.row.opt">
            <el-radio :label="0">不可见</el-radio>
            <el-radio :label="1">可见</el-radio>
            <el-radio :label="2">可编辑</el-radio>
          </el-radio-group>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
import mixinProperties from "@/views/lowcode/config/flow_config/mixinProperties";
import store from "@/store";

export default {
  name: "formShow",
  mixins: [mixinProperties],
  data() {
    return {
      attrList: [],
      attrObj: {}
    }
  },
  methods: {
    initData() {
      this.clear()

      this.attrList = store.getters.attrMap.get(this.objectId).filter(attr=>{return attr.isDefault!=1}).map(attr => {
        let attrOpt = this.attrObj[attr.alias]
        return {name: attr.name, alias: attr.alias, opt: attrOpt ? attrOpt : 1}
      });

      if (this.config && this.config[this.element.id] && this.config[this.element.id]['formShow']) {
        this.attrObj = this.config[this.element.id]['formShow']['attrs']
        this.attrList.forEach(attr => {
          attr.opt = this.attrObj[attr.alias]
        })
      } else {
        //设置默认值
        this.save()
      }
    },
    clear() {
      this.attrList = []
      this.attrObj = {}
    },
    save() {
      let result=true

      this.attrObj = {}
      let edit = 0
      this.attrList.forEach(attr => {
        this.attrObj[attr.alias] = attr.opt
        if (attr.opt == 2) {
          edit = 1
        }
      })

      if (!this.config[this.element.id]) {
        this.config[this.element.id] = {}
      }

      this.config[this.element.id]['formShow'] = {}
      this.config[this.element.id]['formShow']['attrs'] = this.attrObj
      this.config[this.element.id]['formShow']['edit'] = edit

      return result
    }
  },
  mounted() {

  },
}
</script>

<style scoped>

</style>
