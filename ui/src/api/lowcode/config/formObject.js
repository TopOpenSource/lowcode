import request from '@/utils/request'

export function saveOrUpdate(data) {
  return request({
    url: '/lowcode/form/sysFormObject/saveOrUpdate',
    method: 'post',
    data: data
  })
}


export function list(query) {
  return request({
    url: '/lowcode/form/sysFormObject/list',
    method: 'get',
    params: query
  })
}



export function listAll(query) {
  return request({
    url: '/lowcode/form/sysFormObject/listAll',
    method: 'get',
    params: query
  })
}

export function deleteObj(data) {
  return request({
    url: '/lowcode/form/sysFormObject/delete',
    method: 'post',
    data: data
  })
}

export function updateObjState(data) {
  return request({
    url: '/lowcode/form/sysFormObject/updateObjState',
    method: 'post',
    data: data
  })
}
