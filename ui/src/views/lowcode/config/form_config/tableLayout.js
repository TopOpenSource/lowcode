//import {jsonInstanceNotoken,jsonInstance} from '../apiv2.js'
//import request from "@/utils/request";
import request from '@/utils/request'

export const selectTableLayoutById = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutTable/selectByObjectId',
    method: 'post',
    data: params
  })

   // return jsonInstance.post(`/api/form/sysFormLayoutTable/selectByObjectId`,params).then(res => res.data);
};

export const enableTableLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutTable/enable',
    method: 'post',
    data: params
  })
  // return jsonInstance.post(`/api/form/sysFormLayoutTable/enable`,params).then(res => res.data);
};

export const deleteTableLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutTable/delete',
    method: 'post',
    data: params
  })

  // return jsonInstance.post(`/api/form/sysFormLayoutTable/delete`,params).then(res => res.data);
};

export const insertOrUpdateTableLayout = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutTable/insertOrUpdate',
    method: 'post',
    data: params
  })

  //  return jsonInstance.post(`/api/form/sysFormLayoutTable/insertOrUpdate`,params).then(res => res.data);
};

export const selectDtoById = params => {
  return request({
    url: '/lowcode/form/sysFormLayoutTable/selectDtoById',
    method: 'post',
    data: params
  })

  // return jsonInstance.post(`/api/form/sysFormLayoutTable/selectDtoById`,params).then(res => res.data);
};
