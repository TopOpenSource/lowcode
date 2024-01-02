package com.ruoyi.form.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import com.ruoyi.dynamicform.dto.LayoutViewDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LayoutView extends BaseModel {
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
	 * 视图名称
	 */
    private String name;
    /**
	 * 类型 0 视图 1其他视图
	 */
    private String type;
    /**
	 * 查询条件
	 */
    private String queryCriteria;

    /**
	 * 表列展示
	 */
    private String tableColumn;

    /**
     * 是否启用
     */
    private String enable;

    /**************************************/
    /**
     * 修改人姓名
     */
    private String modifiedName;
    /**
     * 整理排序
     */
    private String sort;

    /**
     * 表列展示 移动端
     */
    private String tableColumnMobile;


    private String sortColumn;

    private Integer index;

    private String roles;


    /**
     * view -> dto
     * @param layoutView
     * @return
     */
    public static LayoutViewDto parseView(LayoutView layoutView){
        LayoutViewDto layoutViewDto=new LayoutViewDto();
        layoutViewDto.setObjectId(layoutView.getObjectId());
        layoutViewDto.setSort(layoutView.getSort());
        layoutViewDto.setSortColumn(layoutView.getSortColumn());
        layoutViewDto.setQueryCriteria(layoutView.getQueryCriteria());
        layoutViewDto.setTableColumn(layoutView.getTableColumn());
        return layoutViewDto;
    }
}
