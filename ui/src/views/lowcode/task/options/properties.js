export default {
  data() {
    return {
      open: false,
      form: {
        comment: null,
        objectId:null,
        objectInstanceId:null,
        taskId:null,
        taskAssignee:null,
        targetKey:null
      },
      rules: {
        comment: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
      }
    }
  },
  methods: {
    clear() {
      this.form = {
        comment: null,
        objectId: null,
        objectInstanceId: null,
        taskId: null,
        taskAssignee:null,
        targetKey:null
      }
    },
    initData_(objectId, objectInstanceId, taskId) {
      this.clear()
      this.form.objectId = objectId;
      this.form.objectInstanceId = objectInstanceId;
      this.form.taskId = taskId
    },
  }
}
