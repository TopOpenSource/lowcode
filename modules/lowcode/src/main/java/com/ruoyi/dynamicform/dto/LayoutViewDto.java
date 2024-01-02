package com.ruoyi.dynamicform.dto;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.form.domain.LayoutView;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cheng
 */
@Data
public class LayoutViewDto {
    /**
	 * 
	 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long viewId;

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
	 * 查询条件-视图条件
	 */
    private String queryCriteria;

    /**
     * 查询条件-用户自定义条件
     */
    private String queryCriteriaAppend;


    /**
	 * 表列展示
	 */
    private String tableColumn;

    /**
     * 整理排序
     */
    private String sort;

    private String sortColumn;

    /**
     * 查询条件- 待办/已办
     */
    private String queryFlow;

    /**
     * 生成查询条件
     * @return
     */
    public String genWhere(){
        Query query= JSON.parseObject(this.queryCriteria,Query.class);
        if(query==null){
            return null;
        }
        return query.parseWhere();
    }

    public String genWhereAppend(){
        Query query= JSON.parseObject(this.queryCriteriaAppend,Query.class);
        if(query==null){
            return null;
        }
        return query.parseWhere();
    }

    /**
     * 生成查询字段   增加t2
     * TODO 需要支持default
     */
    public String genQueryCols(){
        return genQueryCols(this.tableColumn);
    }

    public static String genQueryCols(String tableColumn){
        List<String> cols = Arrays.stream(tableColumn.split(",")).map(str->{
            return  str;
        }).collect(Collectors.toList());
        return  String.join(", ", cols);
    }

    /**
     * view -> dto
     * @param layoutView
     * @return
     */
    public static LayoutViewDto  parseView(LayoutView layoutView){
        LayoutViewDto layoutViewDto=new LayoutViewDto();
        layoutViewDto.setObjectId(layoutView.getObjectId());
        layoutViewDto.setSort(layoutView.getSort());
        layoutViewDto.setSortColumn(layoutView.getSortColumn());
        layoutViewDto.setQueryCriteria(layoutView.getQueryCriteria());
        layoutViewDto.setTableColumn(layoutView.getTableColumn());

        return layoutViewDto;
    }
}
