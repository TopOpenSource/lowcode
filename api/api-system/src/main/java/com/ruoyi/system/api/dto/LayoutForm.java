package com.ruoyi.system.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LayoutForm extends BaseModel {
	/**
	 *
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 *
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long objectId;
	/**
	 * 表单布局名称
	 */
	private String name;
	/**
	 * 表单布局类型 0 新增编辑表单 1查看表单
	 */
	private String type;
	/**
	 * 表单内容
	 */
	private String content;
	/**
	 * 是否启用 0不启用 1启用
	 */
	private String enable;

	/**
	 * 是否默认
	 */
	private String isDefault;

	/**************************************/
	/**
	 * 修改人姓名
	 */
	private String modifiedName;


	@DateTimeFormat(pattern ="yyyy-MM-dd HH-mm-ss")
	private Date gmtCreate;
	@DateTimeFormat(pattern ="yyyy-MM-dd HH-mm-ss")
	private Date gmtModified;

	//属性列表
    private List<FormObjectAttribute> attributes;
 }