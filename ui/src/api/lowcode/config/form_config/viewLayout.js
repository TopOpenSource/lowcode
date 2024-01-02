//import {jsonInstanceNotoken,jsonInstance} from '../apiv2.js'
import request from '@/utils/request'

export const selectViewLayoutById = params => {
  return request({
    url: '/lowcode/form/layoutView/selectByObjectId',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/layoutView/selectByObjectId`,params).then(res => res.data);
};

export const enableViewLayout = params => {
  return request({
    url: '/lowcode/form/layoutView/enable',
    method: 'post',
    data: params
  })
  //   return jsonInstance.post(`/api/form/layoutView/enable`,params).then(res => res.data);
};

export const deleteViewLayout = params => {
  return request({
    url: '/lowcode/form/layoutView/delete',
    method: 'post',
    data: params
  })
  //  return jsonInstance.post(`/api/form/layoutView/delete`,params).then(res => res.data);
};

export const saveOrUpdate = params => {
  return request({
    url: '/lowcode/form/layoutView/saveOrUpdate',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/layoutView/saveOrUpdate`,params).then(res => res.data);
};

export const selectByPK = params => {
  return request({
    url: '/lowcode/form/layoutView/selectByPK',
    method: 'post',
    data: params
  })
  //  return jsonInstance.post(`/api/form/layoutView/selectByPK`,params).then(res => res.data);
};

export const savePersonalLayoutView = params => {
  return request({
    url: '/lowcode/form/layoutView/savePersonalLayoutView',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/layoutView/savePersonalLayoutView`,params).then(res => res.data);
};

export const savePersonalTableColumn = params => {
  return request({
    url: '/lowcode/form/layoutView/savePersonalTableColumn',
    method: 'post',
    data: params
  })
  //  return jsonInstance.post(`/api/form/layoutView/savePersonalTableColumn`,params).then(res => res.data);
};

export const updataPersonalLayoutView = params => {
  return request({
    url: '/lowcode/form/layoutView/updataPersonalLayoutView',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/layoutView/updataPersonalLayoutView`,params).then(res => res.data);
};
