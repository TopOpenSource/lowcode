/**
 * 请求参数工具类
 */
import MapUtil from "./MapUtil";
import DateUtil from "./DateUtil.js"
import ObjectUtil from "./ObjectUtil.js"
class ParamsUtil {
	//将查询参数赋值路由参数
	static setParams(route, search) {
		if (route.query && search.query) {
			let query = route.query;
			let searchQuery = search.query;
			MapUtil.copyByRightKey(query, searchQuery);
		}
		if (route.params && search.params) {
			let params = route.params;
			let searchParams = search.params;
			MapUtil.copyByRightKey(params, searchParams);
		}
	}
	//将路由参数赋值到查询条件参数
	static setSeach(route, search) {
		if (route.query && search.query) {
			let query = route.query;
			let searchQuery = search.query;
			MapUtil.copyNotNullByLeftKey(searchQuery, query);
		}
		if (route.params && search.params) {
			let params = route.params;
			let searchParams = search.params;
			MapUtil.copyNotNullByLeftKey(searchParams, params);
		}
	}

	//将 search的 query 和 params 放到一维
	static getSearch(search) {
		let searchVal = new Object();
		MapUtil.copyNotNullByRightKey(searchVal, search.query);
		MapUtil.copyNotNullByRightKey(searchVal, search.params);
		return searchVal;
	}
	
	//获取路由中的菜单值
	static setRouteMenu(route){
		if(route.query){
			let query = route.query;
			
			let menuId=query.menuId;
			let moduleId=query.moduleId;
			//清空route
			ObjectUtil.clear(query);
			
			query.menuId=menuId;
			query.moduleId=moduleId;
		}
	}
}
export default ParamsUtil;
