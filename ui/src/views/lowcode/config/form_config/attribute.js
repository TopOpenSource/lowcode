import request from '@/utils/request'

export const selectAttrsByDto = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/selectByDto',
    method: 'post',
    data: params
  })
    //return jsonInstance.post(`/api/form/sysFormObjectAttribute/selectByDto`,params).then(res => res.data);
};

export const listFormObjectAttribute = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/listFormObjectAttribute',
    method: 'post',
    data: params
  })

    //return jsonInstance.post(`/api/form/sysFormObjectAttribute/listFormObjectAttribute`,params).then(res => res.data);
};

export const toggleAttributeEnable = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/toggleAttributeEnable',
    method: 'post',
    data: params
  })

   // return jsonInstance.post(`/api/form/sysFormObjectAttribute/toggleAttributeEnable`,params).then(res => res.data);
};

export const listAttributeTypes = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/listAttributeTypes',
    method: 'post',
    data: params
  })

  // return jsonInstance.post(`/api/form/sysFormObjectAttribute/listAttributeTypes`,params).then(res => res.data);
};

export const getFormObjectAttribute = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/getFormObjectAttribute',
    method: 'post',
    data: params
  })
  //return jsonInstance.post(`/api/form/sysFormObjectAttribute/getFormObjectAttribute`,params).then(res => res.data);
};

export const saveFormObjectAttribute = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/saveFormObjectAttribute',
    method: 'post',
    data: params
  })

  // return jsonInstance.post(`/api/form/sysFormObjectAttribute/saveFormObjectAttribute`,params).then(res => res.data);
};

export const updateFormObjectAttribute = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/updateFormObjectAttribute',
    method: 'post',
    data: params
  })
  //  return jsonInstance.post(`/api/form/sysFormObjectAttribute/updateFormObjectAttribute`,params).then(res => res.data);
};

export const selectAttrsByObjectId = params => {
  return request({
    url: '/lowcode/form/sysFormObjectAttribute/selectByObjectId',
    method: 'post',
    data: params
  })
   // return jsonInstance.post(`/api/form/sysFormObjectAttribute/selectByObjectId`,params).then(res => res.data);
};

