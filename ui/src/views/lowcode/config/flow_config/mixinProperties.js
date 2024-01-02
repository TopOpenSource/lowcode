export default {
  props: {
    modeler: {
      type: Object,
    },
    element: {
      type: Object,
    },
    //业务对象ID
    objectId:{
      type: String
    },
    //配置信息
    config:{
      type:Object,
      default:{}
    }
  },
  methods: {
    updateProperties(properties) {
      const modeling = this.modeler.get('modeling')
      modeling.updateProperties(this.element, properties)
    }
  }
}
