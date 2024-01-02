package com.ruoyi.common;

import com.ruoyi.common.constant.ResultCode;
import com.ruoyi.common.utils.JSONUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 结果dto
 * @author cheng
 */
@Data
public class ResultDto implements Serializable {
	private Integer result;
	private String message;
	private Object obj;


	public ResultDto() {

	}
	
	public ResultDto(Integer result) {
		this.result = result;
	}
	
	public ResultDto(Integer result, String message) {
		this.result = result;
		this.message = message;
	}

	public ResultDto(ResultCode resultCode) {
		this.result = resultCode.getCode();
		this.message = resultCode.getMsg();
	}

	public String toJsonString(){
		return JSONUtil.parseObject(this);
	}

	public void addMessage(Object obj){
		this.obj=obj;
	}

}
