//import {jsonInstanceNotoken,jsonInstance} from '../apiv2.js'
import request from '@/utils/request'

export const selectFormLayoutByObjectId = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutForm/selectByObjectId',
    method: 'post',
    data: params
  })

   // return jsonInstance.post(`/api/form/sysFormLayoutForm/selectByObjectId`,params).then(res => res.data);
};

export const selectFormLayoutById = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutForm/selectById',
    method: 'post',
    data: params
  })
  //return jsonInstance.post(`/api/form/sysFormLayoutForm/selectById`,params).then(res => res.data);
};

export const saveOrUpdateFormLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutForm/saveOrUpdate',
    method: 'post',
    data: params
  })

  //return jsonInstance.post(`/api/form/sysFormLayoutForm/saveOrUpdate`,params).then(res => res.data);
};

export const enableFormLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutForm/enable',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/sysFormLayoutForm/enable`,params).then(res => res.data);
};

export const deleteFormLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutForm/delete',
    method: 'post',
    data: params
  })

  //return jsonInstance.post(`/api/form/sysFormLayoutForm/delete`,params).then(res => res.data);
};

/**
 * 获取启用的表单
 * @param params
 * @returns {PromiseLike<any> | Promise<any>}
 */
export const getEnableForm = params => {
   // return jsonInstance.post(`/api/form/sysFormLayoutForm/getEnableForm`,params).then(res => res.data);
  return request({
    url: '/lowcode/form/sysFormLayoutForm/getEnableForm',
    method: 'post',
    data: params
  })

};

export function hasEnableForm(params){
  return request({
    url: '/lowcode/form/sysFormLayoutForm/hasEnableForm',
    method: 'post',
    data: params
  })
}
