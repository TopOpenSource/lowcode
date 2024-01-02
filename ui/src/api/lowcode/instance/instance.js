import request from '@/utils/request'
import { download } from '../../../utils/request'


export function selRefView(objectId){
  return request({
    url: '/lowcode/lowcode/instance/selRefView/'+objectId,
    method: 'get'
  })
};


export function selTable(objectId){
  return request({
    url: '/lowcode/lowcode/instance/selTable/'+objectId,
    method: 'get'
  })
};


export function pageList(pageInfo,params){
  return request({
    url: '/lowcode/lowcode/instance/pageList'+pageInfo,
    method: 'post',
    data: params
  })
};

export function listRelInstance(params){
  return request({
    url: '/lowcode/lowcode/instance/listRelInstance',
    method: 'post',
    data:params
  })
};

export function listFiles(params){
  return request({
    url: '/lowcode/lowcode/file/listFiles',
    method: 'post',
    data:params
  })
};

export function downloadFile(id,name){
  return download('/lowcode/lowcode/file/download/'+id,{},name)
}

export function delInstBatch(params) {
  return request({
    url: '/lowcode/lowcode/instance/delBatch',
    method: 'post',
    data:params
  })
}

