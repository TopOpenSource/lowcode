import {listAll} from "@/api/system/dict/data";
import {getAllUsersForSel} from "@/api/system/user";
import {listDept} from "@/api/system/dept";
import {selectAttrsByDto} from "@/api/lowcode/config/form_config/attribute";
import {selectRoleAll} from "@/api/system/role";

const lowcode = {
  state: {
    dictMap: null,
    dictTypeMap: null,
    attrMap: null,
    userList: [],
    userMap: null,
    roleList: [],
    deptList:[]
  },
  mutations: {
    SET_DICTTYPE_MAP: (state, data) => {
       state.dictTypeMap=data
    },
    SET_DICT_MAP: (state, data) => {
      state.dictMap=data
    },
    SET_USER: (state, data) => {
      state.userList=data
    },
    SET_USER_MAP:(state, data)=>{
      state.userMap=data
    },
    SET_ROLES:(state,data)=>{
      state.roleList=data
    },
    SET_DEPTS:(state,data)=>{
      state.deptList=data
    },
    SET_ATTR_MAP:(state,data)=>{
      state.attrMap=data
    }
  },
  actions: {
    //初始化字典信息
    initDict({ commit }) {
      listAll().then(res => {
          let dictMap=new Map()
          let dictTypeMap=new Map()

          res.data.forEach(dict=>{
            // {dictType:{'va1':'label1','va2':'label2'}}
            let dicts=dictMap.get(dict.dictType)
            if(!dicts){
              dicts=new Map()
              dictMap.set(dict.dictType,dicts)
            }
            dicts.set(dict.dictValue,dict.dictLabel)

            // {dictType:[{},{}],}
            if (!dictTypeMap[dict.dictType]) {
              dictTypeMap[dict.dictType] = new Array();
            }
            dictTypeMap[dict.dictType].push({code: dict.dictValue, name: dict.dictLabel});
        })
        commit('SET_DICT_MAP',dictMap)
        commit('SET_DICTTYPE_MAP',dictTypeMap)
      })
    },
    //初始化用户信息
    initUsers({ commit }){
      getAllUsersForSel().then(res=>{
        commit('SET_USER',res)

        let userMap=new Map()
        res.forEach(user=>{
          userMap.set(user.userName,user.nickName)
        })
        commit('SET_USER_MAP',userMap)
      });
    },
    //初始化组织信息
    initDepts({ commit }){
      listDept().then(res => {
        commit('SET_DEPTS',res.data)
      });
    },
    //初始化属性
    initAttrs({ commit }){
      selectAttrsByDto({enable: '1'}).then(res => {
        let attrMap=new Map()
        //{"objectId":[attr1,attr2]}
        res.forEach(attr=>{
          let attrs=attrMap.get(attr.objectId)
          if(!attrs){
            attrMap.set(attr.objectId,new Array())
          }
          attrMap.get(attr.objectId).push(attr);
        })

        commit('SET_ATTR_MAP',attrMap)
       })
    },
    initRole({ commit }){
      selectRoleAll().then(res => {
        commit('SET_ROLES',res.data)
      })
    }

  }
}

export default lowcode
