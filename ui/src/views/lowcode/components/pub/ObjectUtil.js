class ObjectUtil {
	static clear(obj){
		let keys=Object.keys(obj);
		keys.forEach(key=>{
			delete obj[key]
		})
	}
		
	static isString(obj){
		return Object.prototype.toString.call(obj) == '[object String]';
	}
	
	/*include NaN*/
	static isNumber(obj){
		return Object.prototype.toString.call(obj) == '[object Number]';
	}
	
	/*exclude NaN*/
	static isNum(obj){
		if(isNumber(obj)){
			return !isNaN(obj);
		}
		return false;
	}
	
	static isArray(obj){
		return Object.prototype.toString.call(obj) == '[object Array]';
	}
	
	static isObject(obj){
		return Object.prototype.toString.call(obj) == '[object Object]';
	}
	
	static isFunction(obj){
		return Object.prototype.toString.call(obj) == '[object Function]';
	}
	
	static isBoolean(obj){
		return Object.prototype.toString.call(obj) == '[object Boolean]';
	}
	
	static isUndefined(obj){
		return Object.prototype.toString.call(obj) == '[object Undefined]';
	}
	
	static isNull(obj){
		return Object.prototype.toString.call(obj) == '[object Null]';
	}
}

export default ObjectUtil;