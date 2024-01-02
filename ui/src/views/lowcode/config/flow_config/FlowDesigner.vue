<script src="src/views/lowcode/components/pub/MapUtil.js"></script>
<template>
  <div class="app-container">
    <el-row class="mb8">
      <el-col :span="24" style="text-align:right">
        <el-button type="success" icon="el-icon-folder-checked" size="small" @click="handleSaveModel">
          保存
        </el-button>

        <el-button type="warning" icon="el-icon-refresh-right" size="small" @click="handleRest">
          重置
        </el-button>

      </el-col>
    </el-row>

    <!--流程设计区-->
    <div id="canvas" class="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>

    <!--任务节点配置-->
    <flow-task ref="flowTask" :modeler="bpmnModeler" :element="element" :objectId="flow.formObjectId" :config="config"></flow-task>
    <!--服务任务节点配置-->
    <flow-service-task ref="flowServiceTask" :modeler="bpmnModeler" :element="element" :objectId="flow.formObjectId" :config="config"></flow-service-task>
    <!--条件配置-->
    <flow-line ref="flowLine" :modeler="bpmnModeler" :element="element" :objectId="flow.formObjectId" :config="config"></flow-line>
  </div>
</template>

<script>
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
//左侧工具栏
import PaletteModule from '@/assets/js/bpmn/bpmn-js/lib/features/palette';
//自定义节点按钮
import ContextPadModule from '@/assets/js/bpmn/bpmn-js/lib/features/context-pad';
import getInitStr from './init'
import flowableModdle from './flowable.json'

import {deployByXmlStr, readXmlStr} from "@/api/lowcode/config/flow_config/processdefinition"
import task from './task/index'
import line from './line/index'
import ServiceTask from './service_task/index'
import store from "@/store";

export default {
  data() {
    return {
      bpmnModeler: null,
      element: null,
      config: {},
      flow: {
        name: '测试流程',
        formObjectId: null,
        deploymentId: null,
        processDefinitionId: null,
        xml: null
      },

    }
  },
  components: {
    'flow-task': task,
    'flow-line': line,
    'flow-service-task':ServiceTask
  },
  methods: {
    async init() {
      // 创建Bpmn对象
      this.bpmnModeler = new BpmnModeler({
        container: this.$refs.canvas,
        additionalModules: [PaletteModule, ContextPadModule],
        moddleExtensions: {
          flowable: flowableModdle
        }
      });

      // 创建空disagram
      try {
        if (this.flow.xml) {
          await this.bpmnModeler.importXML(this.flow.xml);
        } else {
          await this.createNewDiagram();
        }
        this.addListener();
      } catch (err) {
        console.log(err.message, err.warnings);
      }
    },
    //添加监听
    addListener() {
      let $this = this;
      let eventBus = $this.bpmnModeler.get('eventBus');
      eventBus.on('element.dblclick', function (e) {
        let shape = $this.getShape(e.element.id);
        $this.element = shape;

        //根据节点类型获取不同的形状
        switch (shape.type) {
          case 'bpmn:StartEvent': {

            break;
          }
          case 'bpmn:UserTask': {
            if(e.element.id=="taskApply"){
              $this.$modal.msgError("请勿修改!");
            }else{
              $this.$refs.flowTask.open = true
              $this.$refs.flowTask.title = '任务节点配置'
              $this.$refs.flowTask.initData()
            }
            break;
          }
          case 'bpmn:ServiceTask': {
            $this.$refs.flowServiceTask.open = true
            $this.$refs.flowServiceTask.title = '服务节点配置'
            $this.$refs.flowServiceTask.initData()
            break;
          }
          case 'bpmn:SequenceFlow': {
            $this.$refs.flowLine.open = true
            $this.$refs.flowLine.title = '条件配置'
            $this.$refs.flowLine.initData()
            break;
          }
          case 'bpmn:ExclusiveGateway': {
            break;
          }
          case 'bpmn:EndEvent': {
            break;
          }
          default: {
            //statements;
            break;
          }
        }


      })

      eventBus.on('shape.added', function (e) {
        let shape = $this.getShape(e.element.id);
        $this.element = shape;
        switch (shape.type) {
          case 'bpmn:EndEvent': {
            //监听新增，如果是结束节点则增加事件
            $this.$nextTick(() => {
              $this.addEndListener(shape)
            })
          }
        }
      })

      eventBus.on('shape.removed', function (e) {
        let shape = $this.getShape(e.element.id);
        $this.element = shape;
        switch (shape.type) {
          case 'bpmn:UserTask': {
            //监听 将删除的元素配置删除
            delete $this.config[e.element.id]
          }
        }
      })
    },
    handleSaveModel() {
      //验证taskApply节点是否存在
      if(this.config['taskApply']){
        //保存模型
        this.bpmnModeler.saveXML({format: true}).then(({xml}) => {
          //this.setTaskApplyConfig()
          deployByXmlStr({
            formObjectId: this.flow.formObjectId,
            strBPM: encodeURI(xml),
            config: JSON.stringify(this.config)
          }).then(res => {
            this.$modal.msgSuccess("保存流程成功");
          })
        });
      }else{
        this.$modal.msgError("请勿修改申请节点!");
      }

    },
    //设置申请节点config
    setTaskApplyConfig() {
      if (this.config && this.config['taskApply']) {

      } else {
        this.config['taskApply'] = {}

        this.config['taskApply']['assignee'] = {name: "申请节点[请勿删除]", auditUserType: 3, auditType: 0}
        this.config['taskApply']['btn'] = [{id: 1, name: '同意', state: '1'}, {id: 2, name: '退回', state: '0'}, {
          id: 3,
          name: '驳回',
          state: '0'
        }, {id: 4, name: '委派', state: '0'}, {id: 5, name: '转办', state: '0'}]
        this.config['taskApply']['formShow'] = {}

        let attrs = {}
        store.getters.attrMap.get(this.flow.formObjectId).forEach(attr => {
          attrs[attr.alias] = 2
        });
        this.config['taskApply']['formShow']['attrs'] = attrs
        this.config['taskApply']['formShow']['edit'] = 1
      }
    },
    handleRest() {
      this.createNewDiagram();
      this.$modal.msgSuccess("重置成功，保存后生效!");
    },
    createNewDiagram() {
      let data = getInitStr();
      // 将字符串转换成图显示出来
      data = data.replace(/<!\[CDATA\[(.+?)]]>/g, function (match, str) {
        return str.replace(/</g, '&lt;')
      })
      this.bpmnModeler.importXML(data);
      this.setTaskApplyConfig()
    },
    getShape(id) {
      return this.bpmnModeler.get('elementRegistry').get(id)
    },
    //结束event事件
    addEndListener(elment){
      let defaultListener=this.bpmnModeler.get('moddle').create('flowable:ExecutionListener', {event:"start",class:"com.ruoyi.flow.listener.FlowEndListener"})
      let extensionList=[]
      extensionList.push(defaultListener)
      let extensions = this.bpmnModeler.get('moddle').create("bpmn:ExtensionElements", {
        values: extensionList
      });

      const modeling = this.bpmnModeler.get('modeling')
      modeling.updateProperties(this.element, {extensionElements: extensions})
    }
  },
  mounted() {
    //读取流程
    const query = this.$route.query
    if (query) {
      this.flow.formObjectId = query.formObjectId;

      readXmlStr(this.flow.formObjectId).then(res => {
        if (res.data) {
          this.flow.xml = res.data.xml
          this.config = JSON.parse(res.data.config)
        }
        this.init();
      });
    }
  }
}
</script>

<style lang="scss" scoped>

@import '~bpmn-js/dist/assets/diagram-js.css';
@import '~bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
@import '~bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css';
@import '~bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';

.containers {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}

.canvas {
  width: 100%;
  height: 500px;
}

.panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
}

</style>
