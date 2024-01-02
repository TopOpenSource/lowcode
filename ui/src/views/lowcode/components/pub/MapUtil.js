class MapUtil {
	/**
	 * 将 rightobj的非空值且 leftobj 定义的 赋值到leftObj上
	 * @param {Object} leftObj
	 * @param {Object} rightObj
	 */
     static copyNotNullByLeftKey(leftObj,rightObj){
		 for (let key of Object.keys(leftObj)) {
		 	let rightVal = rightObj[key];
		 	if(rightVal!=undefined && rightVal !=null){
				//判断数据类型-根据原值判断
				let leftVal=leftObj[key];
				if(leftVal!=undefined&&leftVal!=null){
					let leftValType=typeof(leftVal);
					if(leftValType=="number"){
						leftObj[key]=Number(rightVal);
					}else if(leftValType=="string"){
						leftObj[key]=String(rightVal);
					}else{
						leftObj[key]=rightVal;
					}
				}else{
					leftObj[key]=rightVal;
				}
		 	}
		 }
	 }

	 /**
	  * 将 rightobj的非空赋值到leftObj上
	  * @param {Object} leftObj
	  * @param {Object} rightObj
	  */
	 static copyNotNullByRightKey(leftObj,rightObj){
	 		 for (let key of Object.keys(rightObj)) {
	 		 	let rightVal = rightObj[key];
	 		 	if(rightVal!=undefined && rightVal !=null){
	 		 		leftObj[key]=rightVal;
	 		 	}
	 		 }
	 }

	 /** 将 rightobj的值到leftObj上
	  * @param {Object} leftObj
	  * @param {Object} rightObj
	  */
	 static copyByRightKey(leftObj,rightObj){
	 		 for (let key of Object.keys(rightObj)) {
	 		 	let rightVal = rightObj[key];
	 		 	leftObj[key]=rightVal;
	 		 }
	 }

	 /**
	  * 清理对象数据
	  * @param {Object} obj
	  */
	 static clearObj(obj){
		 for (let key of Object.keys(obj)) {
		 	let valType=typeof(obj[key]);
			if(valType=="number"){
				obj[key]='';
			}else if(valType=="string"){
				obj[key]='';
			}else{
				obj[key]=null;
			}
		 }
	 }

  /**
   * map 转json
   * @param map
   */
  static map2json(map){
    return JSON.stringify([...map]);
   }

   static json2map(jsonStr){
     return new Map(JSON.parse(jsonStr));
   }
}
export default MapUtil;
