export default {
  data(){
    return{
      rules:[{}],
    }
  },
  props: {
    label: String,
    value: null,
    notnull: {
      type: String,
      default: '0'
    },
    prop: {
      type: String
    },
    placeholder:{
      type:String,
      default:''
    },
    maxlength: {
      type: Number,
    },
    minLength: {
      type: Number,
      default: 0
    },
    //例如： ([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4}) ,^-?[1-9]\d*$
    regex: {
      type: String
    },
    //额外的验证
    valids: {
      type: Array
    },
    edit:{
      type:Boolean,
      default: true
    }
  },
  methods: {

  }
}
