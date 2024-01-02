package com.ruoyi.dynamicform.service;

import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.dynamicform.dto.DynamicQueryDto;
import com.ruoyi.dynamicform.dto.LayoutViewDto;
import com.ruoyi.dynamicform.dto.TableDto;
import com.ruoyi.dynamicform.dto.TaskQueryDto;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DynamicFormService {
    /**
     * 保存表单信息
     * @param objectId
     * @param dto
     * @param formObject
     * @param attributes
     */
    void save(Long objectId, CommonDto dto, FormObject formObject, List<FormObjectAttribute> attributes);

    /**
     * 验证数据是否正确
     * @param objectId
     * @param dto
     * @param formObject
     * @param attributes
     * @return
     */
    boolean valid(Long objectId, CommonDto dto, FormObject formObject, List<FormObjectAttribute> attributes);

    /**
     * 更新表单信息
     * @param objectId
     * @param dto
     */
    void update(Long objectId, CommonDto dto, FormObject formObject,List<FormObjectAttribute> attributes);

    /**
     * 根据ID查询
     * @param objectId
     * @param id
     */
    CommonDto selectByPk(Long objectId, Long id,List<FormObjectAttribute> attributes);

    /**
     * 查询属性
     * @param objectId
     * @return
     */
    List<FormObjectAttribute> selAttributesByObjectId(Long objectId);

    /**
     * 查询属性
     * @param objectId
     * @param alias
     * @return
     */
    FormObjectAttribute selAttributesByAlias(Long objectId,String alias);

    /**
     * 查询业务对象信息
     * @param objectId
     * @return
     */
    FormObject selObjectById(Long objectId);


    /**
     * 查询默认的关联视图
     * @param objectId
     * @return
     */
    LayoutView selRefViewByObjectId(Long objectId);

    /**
     * 查询是否重复
     * @param id
     * @param value
     * @param attribute
     * @param formObject
     * @return
     */
    Integer checkRepeat(Long id,Object value,FormObjectAttribute attribute,FormObject formObject);


    /**
     * 动态查询
     * @param dto
     * @return
     */
     List<CommonDto> selectList(DynamicQueryDto dto);


    /**
     * 动态查询
     * @param dto
     * @return
     */
    List<CommonDto> selectListWithTask(DynamicQueryDto dto, TaskQueryDto taskQueryDto);

    /**
     * 生成 DynamicQueryDto
     * @param dto
     * @return
     */
     DynamicQueryDto  genDynamicQuery(LayoutViewDto dto);

    /**
     *查询 属性值
     * @param objectId
     * @param instanceId
     * @param attrIds
     * @return
     */
    CommonDto getAttrVal(Long objectId,Long instanceId, List<Long> attrIds);

    /**
     * 获取table 配置信息
     * @param objectId
     * @return
     */
    TableDto selectTableDto(Long objectId, Set<String> roleIds);

    void delBatch(String tableName,List<Long> ids);
}
