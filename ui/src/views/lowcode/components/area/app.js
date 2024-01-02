import REGION_DATA from './data'
import cloneDeep from 'lodash/cloneDeep'

// code转汉字大对象
const codeToText = {}
const provinceObject = REGION_DATA['86'] // 省份对象
const regionData = []

codeToText[''] = '全部'

// 计算省
for (const prop in provinceObject) {
  regionData.push({
    value: prop, // 省份code值
    label: provinceObject[prop] // 省份汉字
  })
  codeToText[prop] = provinceObject[prop]
}
// 计算市
for (let i = 0, len = regionData.length; i < len; i++) {
  const provinceCode = regionData[i].value
  const provinceText = regionData[i].label
  const provinceChildren = []
  for (const prop in REGION_DATA[provinceCode]) {
    provinceChildren.push({
      value: prop,
      label: REGION_DATA[provinceCode][prop]
    })
    codeToText[prop] = REGION_DATA[provinceCode][prop]
  }
  if (provinceChildren.length) {
    regionData[i].children = provinceChildren
  }
}

// 计算区
for (let i = 0, len = regionData.length; i < len; i++) {
  const province = regionData[i].children
  const provinceText = regionData[i].label
  if (province) {
    for (let j = 0, len = province.length; j < len; j++) {
      const cityCode = province[j].value
      const cityText = province[j].label
      const cityChildren = []
      for (const prop in REGION_DATA[cityCode]) {
        cityChildren.push({
          value: prop,
          label: REGION_DATA[cityCode][prop]
        })
        codeToText[prop] = REGION_DATA[cityCode][prop]
      }
      if (cityChildren.length) {
        province[j].children = cityChildren
      }
    }
  }
}

export { regionData,codeToText }
