export const validator = {
	account(rule,value,callback) {
		let regex = /^[a-zA-Z][a-zA-Z0-9_]{1,19}$/;
		if(!regex.test(value)) {
			callback(new Error('请输入2-20位的 以字母开头 包含 字母、数字、下划线！'))
		} else {
			callback();
		}
	},
	pwd(rule,value,callback) {
		let regex = /^(?![A-Z]+$)(?![a-z]+$)(?!\d+$)(?![\W_]+$)\S{6,16}$/;
		let regex2 = /[\u4E00-\u9FA5]/g;
		
		if(value!='' && value!=undefined){
			if(!regex.test(value) || regex2.test(value)){
				callback(new Error('请填写6-16位数字、字母、特殊字符两种以上组合!'))
			}else{
				callback();
			}
		}
		callback();
	},
	
	//限制只能输入0-100内的数字
	num100(rule,value,callback){
		let regex = /^100$|^(\d|[1-9]\d)$/;
		if(value!='' && value!=undefined){
			if(!regex.test(value)){
				callback(new Error('只能输入0-100内的数字'))
			}else{
				callback();
			}
		}
		callback();
	},
	//数字或小数
	num(rule,value,callback){
		let regex = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
	
		if(value!='' && value!=undefined){
			if(!regex.test(value)){
				callback(new Error('只能输入数字或小数'))
			}else{
				callback();
			}
		}
		callback();
	},
	num0(rule,value,callback){
		let regex = /^\+?[1-9]\d*$/;
		if(value!='' && value!=undefined){
			if(!regex.test(value)){
				callback(new Error('请输入大于零的整数!'))
			}else{
				callback();
			}
		}
		callback();
	},
	//手机号验证
	phone(rule,value,callback){
		if (value == undefined || value == '') {
			callback(new Error('请输入手机号'));
		} else if (!(/^1[3456789]\d{9}$/.test(value))) {
			callback(new Error('请输入正确的手机号'))
		} else {
			callback();
		}
	},
	//手机号或座机号
	phoneOrMobile(rule,value,callback){
		if (value == undefined || value == '') {
			callback();
		} else if (!(/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/.test(value))) {
			callback(new Error('请输入正确的手机号或座机号'))
		} else {
			callback();
		}
	},
	//邮箱验证
	email(rule,value,callback){
		// let regex = /^([A-z0-9]{6,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/;
		let regex = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
		if (value == undefined || value == '') {
			callback(new Error('请输入邮箱'));
		} else if (!(regex.test(value))) {
			callback(new Error('请输入正确的邮箱地址'))
		} else {
			callback();
		}
	},

	//网址验证
	website(rule,value,callback){
		// let regex = /^([A-z0-9]{6,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/;
		let regex = /^(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$/;

		if (value == undefined || value == '') {
			callback();
		} else if (!(regex.test(value))) {
			callback(new Error('请输入正确的网址'))
		} else {
			callback();
		}
	},
	//是否IP
	isIp(ip){
		let reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		if(!ip)
			return false;
		return reg.test(ip);
	},

   //自定义正则表达式
	regex(rule,value,callback){
		// let regex = /^([A-z0-9]{6,18})(\w|\-)+@[A-z0-9]+\.([A-z]{2,3})$/;
		let regex = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
		if (value == undefined || value == '') {
			callback();
		} else if (!(regex.test(value))) {
			callback(new Error('请输入正确的邮箱地址'))
		} else {
			callback();
		}
	},
}