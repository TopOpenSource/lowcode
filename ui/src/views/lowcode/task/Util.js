import AttributeConstant from "@/views/lowcode/components/pub/AttributeConstant";
import DateUtil from "@/views/lowcode/components/pub/DateUtil";

class Util {
  constructor(attributes,content) {

  }
  //表单布局与属性整合
  parseForm2Attrs(attributes,content){
    //展示属性
    let showAttributes=[];
    //属性转为map
    let attributeMap=new Map();
    attributes.forEach(attribute=>{
      attributeMap.set(attribute.id,attribute);
    });
    //布局处理
    let contents=JSON.parse(content);
    contents.forEach(con=>{
      if(con.id=='-1'){
        showAttributes.push({name:con.displayTitle,type:'splitline',displayFullline:'1'});
      }else{
        if(attributeMap.get(con.id)!= undefined){
          showAttributes.push(attributeMap.get(con.id));
        }
      }
    });
    return showAttributes;
  }
  //显示列与attr整合
  parseCols2Attrs(attributes,colsStr){
    //属性转为map
    let attributeMap=new Map();
    attributes.forEach(attribute=>{
      attributeMap.set(attribute.alias,attribute);
    });

    //展示属性
    let showCols=[];
    colsStr.split(",").forEach(col=>{
      showCols.push(attributeMap.get(col))
    })

    return showCols;
  }
  // 列表字段显示内容
  formatterVal(attr,row,dictMap,userMap){
    let val = row[attr.alias];

    if (!val || val == "") {
      return val;
    }
    //单选  多选
    if (attr.type === AttributeConstant.TYPE_RADIO || attr.type === AttributeConstant.TYPE_CHECKBOX) {
      let valShow= val.split(",").map(res=>{
        return  dictMap.get(attr.dictTypeCode).get(res)
      })
      return valShow.join();
    }
    //日期
    if (attr.type === AttributeConstant.TYPE_DATE) {
      return DateUtil.formatDate(new Date(val), 'yyyy-MM-dd');
    }
    //日期
    if (attr.type === AttributeConstant.TYPE_DATETIME) {
      return DateUtil.formatDate(new Date(val), 'yyyy-MM-dd hh:mm:ss');
    }
    //user
    if(attr.type===AttributeConstant.TYPE_USER){
      let valShow= val.split(",").map(res=>{
        return  userMap.get(res)
      })
      return valShow.join();

    }
    //ORG
    if(attr.type===AttributeConstant.TYPE_ORG){

    }
    return val;
  }

}

export default Util;
