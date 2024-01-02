import request from '@/utils/request'

export function deployByXmlStr(query) {
  return request({
    url: '/lowcode/flow/addDeploymentByStr',
    method: 'post',
    data: query
  })
}

export function readXmlStr(query) {
  return request({
    url: '/lowcode/flow/readXmlStr/' + query,
    method: 'get'
  })
}


// 读取image文件
export function getFlowViewer(procInsId) {
  return request({
    url: '/lowcode/flow/flowViewer/' + procInsId,
    method: 'get'
  })
}
//获取业务对象流程配置信息
export function getFlowObject(query) {
  return request({
    url: '/lowcode/flow/flowObject/' + query,
    method: 'get'
  })
}
