class ListUtil {


  /**
   *
   * @param {Object} leftObj
   * @param {String} rightObj
   */
  static List2Map(listData,key,value){
     let out=new Map();
     listData.forEach(data=>{
       out.set(data[key],data[value])
     });
     return out;
  }

  static List2Map(listData,key){
    let out=new Map();
    listData.forEach(data=>{
      out.set(data[key],data)
    });
    return out;
  }


}
export default ListUtil;
