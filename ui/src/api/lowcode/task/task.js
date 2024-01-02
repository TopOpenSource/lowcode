import request from '@/utils/request'

export const pageListMyTasks = query => {
  return request({
    url: '/lowcode/audit/myTasks',
    method: 'get',
    params: query
  })
};

export const selectMyTask = instId => {
  return request({
    url: '/lowcode/audit/selectMyTask/'+instId,
    method: 'get'
  })
};

export const myFinishedTask = query => {
  return request({
    url: '/lowcode/audit/myFinishedTask',
    method: 'get',
    params: query
  })
};

export const myUnSubmitTask = query => {
  return request({
    url: '/lowcode/audit/myUnSubmitTask',
    method: 'get',
    params: query
  })
};

export const myApplyTask = query => {
  return request({
    url: '/lowcode/audit/myApplyTask',
    method: 'get',
    params: query
  })
};

export const findReturnTaskList = taskId => {
  return request({
    url: '/lowcode/audit/findReturnTaskList/'+taskId,
    method: 'get'
  })
};


export const submit = (params) => {
  return request({
    url: '/lowcode/audit/submit',
    method: 'post',
    data: params
  })
};


export const complete = params => {
  return request({
    url: '/lowcode/audit/complete',
    method: 'post',
    data: params
  })
};


export const taskReturn = params => {
  return request({
    url: '/lowcode/audit/taskReturn',
    method: 'post',
    data: params
  })
};

export const taskReject = params => {
  return request({
    url: '/lowcode/audit/taskReject',
    method: 'post',
    data: params
  })
};

export const taskDelegate = params => {
  return request({
    url: '/lowcode/audit/taskDelegate',
    method: 'post',
    data: params
  })
};

export const taskAssign = params => {
  return request({
    url: '/lowcode/audit/taskAssign',
    method: 'post',
    data: params
  })
};

export function taskHistory(instId){
  return request({
    url: '/lowcode/audit/taskHistory/'+instId,
    method: 'get'
  })
};

export const selFlowInst = instId => {
  return request({
    url: '/lowcode/audit/selFlowInst/'+instId,
    method: 'get'
  })
};
