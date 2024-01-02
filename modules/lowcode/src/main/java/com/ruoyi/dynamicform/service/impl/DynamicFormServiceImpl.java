package com.ruoyi.dynamicform.service.impl;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.constant.SystemConstant;
import com.ruoyi.dynamicform.domain.InstRelation;
import com.ruoyi.dynamicform.dto.*;
import com.ruoyi.dynamicform.mapper.DynamicFormMapper;
import com.ruoyi.dynamicform.mapper.InstRelationMapper;
import com.ruoyi.dynamicform.service.DynamicFormService;
import com.ruoyi.dynamicform.service.IInstFileService;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutTable;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.service.LayoutTableService;
import com.ruoyi.form.service.LayoutViewService;
import com.ruoyi.form.service.ObjectAttributeService;
import com.ruoyi.form.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class DynamicFormServiceImpl implements DynamicFormService {

    @Autowired
    private ObjectAttributeService attrService;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private LayoutViewService viewService;

    @Autowired
    private LayoutTableService tableService;

    @Autowired
    private DynamicFormMapper dynamicFormMapper;

    @Autowired
    private InstRelationMapper instRelationMapper;
    @Autowired
    private IInstFileService instFileService;

    private static Snowflake snowflake = new Snowflake();

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(Long objectId, CommonDto dto, FormObject formObject, List<FormObjectAttribute> attributes) {
        Long id = dto.getAsLong("id");
        dto.put("isDelete","0");
        //保存主表信息
        dynamicFormMapper.save(dto, attributes, formObject);
        //保存关系
        this.saveORupdateRelation(objectId,id,dto,attributes);
        //保存文件
        this.saveORupdateFile(id,dto,attributes);

    }
    //保存关联信息
    private void saveORupdateRelation(Long objectId, Long instId,CommonDto dto,List<FormObjectAttribute> attributes){
        List<InstRelation> relations=new ArrayList<>();

        //遍历 属性获取关系类型的属性
        attributes.stream().filter(attr -> {
            return attr.getType().equals("relation");
        }).forEach(attr->{
            //删除原记录
            instRelationMapper.delByAttr(instId,attr.getId());

            //追加信记录
            String value = dto.getAsString(attr.getAlias());
            if(!StringUtils.isEmpty(value)){
                String[] values=value.split(",");
                for(int i=0;i<values.length;i++){
                    InstRelation instRelation=new InstRelation(snowflake.nextId(),objectId,instId,attr.getId(),Long.valueOf(values[i]),Long.valueOf(attr.getDictTypeCode()));
                    relations.add(instRelation);
                }
            }
        });
        //批量保存
        if(relations.size()>0){
            instRelationMapper.insertBatch(relations);
        }

    }

    private void saveORupdateFile( Long instId,CommonDto dto,List<FormObjectAttribute> attributes){
        //遍历 文件类型的属性
        attributes.stream().filter(attr -> {
            return attr.getType().equals("file");
        }).forEach(attr->{
            //删除原记录
            InstFileDto instFileDto=new InstFileDto();
            instFileDto.setInstId(instId);
            instFileDto.setAttrId(attr.getId());
            instFileService.deleteByDto(instFileDto);

            //更新新纪录
            String valueStr = dto.getAsString(attr.getAlias());
            if(!StringUtils.isEmpty(valueStr)){
                List<String> idList = Arrays.asList(valueStr.split(","));
                instFileService.updateByInst(idList,instId,attr.getId());
            }
        });

    }


    //TODO 后台验证
    @Override
    public boolean valid(Long objectId, CommonDto dto, FormObject formObject, List<FormObjectAttribute> attributes) {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(Long objectId, CommonDto dto, FormObject formObject, List<FormObjectAttribute> attributes) {
        Long id = dto.getAsLong("id");
        //更新主表信息
        dynamicFormMapper.update(dto, attributes, formObject);
        //更新关联
        this.saveORupdateRelation(objectId,id,dto,attributes);
        //保存文件
        this.saveORupdateFile(id,dto,attributes);
    }

    @Override
    public CommonDto selectByPk(Long objectId, Long id,List<FormObjectAttribute> attributes) {
        //获取业务对象
        FormObject formObject = this.selObjectById(objectId);
        return dynamicFormMapper.selectByPk(id,attributes, formObject);
    }


    /**
     * 获取启用的属性
     *
     * @return
     */
    @Override
    public List<FormObjectAttribute> selAttributesByObjectId(Long objectId) {
        FormObjectAttribute attribute = new FormObjectAttribute();
        attribute.setEnable(SystemConstant.DEFUALT_1);
        attribute.setObjectId(objectId);
        List<FormObjectAttribute> attributes = attrService.selectByDto(attribute);
        return attributes;
    }

    @Override
    public FormObjectAttribute selAttributesByAlias(Long objectId, String alias) {
        FormObjectAttribute attribute = new FormObjectAttribute();
        attribute.setEnable(SystemConstant.DEFUALT_1);
        attribute.setObjectId(objectId);
        attribute.setAlias(alias);
        List<FormObjectAttribute> attrs = attrService.selectByDto(attribute);
        if (attrs != null && attrs.size() == 1) {
            return attrs.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取业务对象
     *
     * @param objectId
     * @return
     */
    @Override
    public FormObject selObjectById(Long objectId) {
        FormObject formObject1 = objectService.selectByPK(objectId);
        return formObject1;
    }

    @Override
    public LayoutView selRefViewByObjectId(Long objectId) {
        return viewService.selectDefaultRefView(objectId);
    }

    @Override
    public Integer checkRepeat(Long id,Object value, FormObjectAttribute attribute, FormObject formObject) {
        return dynamicFormMapper.checkRepeat(id, value, attribute, formObject);
    }

    //文件处理
    private boolean upFileAttrs(Long id, Long objectId, CommonDto dto, List<FormObjectAttribute> attributes) {
       /* for (FormObjectAttribute attribute : attributes) {
            if (AttributeConstant.TYPE_FILE.equals(attribute.getType())) {
                String fileStr = dto.getAsString(attribute.getAlias());
                SysFile refFile = new SysFile(id, objectId, attribute.getAlias());
                //清理原值
                fileService.clearRef4DyForm(refFile);
                //新值赋值
                if (!StringUtils.isEmpty(fileStr)) {
                    refFile.setIds(Arrays.asList(fileStr.split(",")));
                    fileService.updateRef4DyForm(refFile);
                }
            }
        }*/
        return true;
    }
    @Override
    public DynamicQueryDto  genDynamicQuery(LayoutViewDto dto){
        FormObject formObject = selObjectById(dto.getObjectId());

        DynamicQueryDto queryDto=new DynamicQueryDto();

        queryDto.setTableName(formObject.getTableName());

        //生成查询字段
        queryDto.setQueryCols(dto.genQueryCols());
        //生成where
        queryDto.setQueryWhere(dto.genWhere());

        queryDto.setQueryWhereAppend(dto.genWhereAppend());
        //排序
        queryDto.setOrderBy(dto.getSortColumn());
        queryDto.setOrderType(dto.getSort());

        return  queryDto;
    }
    @Override
    public List<CommonDto> selectList(DynamicQueryDto queryDto) {
        List<CommonDto> results = dynamicFormMapper.selectList(queryDto);
        return results;
    }

    @Override
    public List<CommonDto> selectListWithTask(DynamicQueryDto dto, TaskQueryDto taskQueryDto) {
        List<CommonDto> results = dynamicFormMapper.selectListWithTask(dto,taskQueryDto);
        return results;
    }

    /**
     * 获取多个属性的值
     * @param objectId
     * @param instanceId
     * @param attrIds
     * @return
     */
    @Override
    public CommonDto getAttrVal(Long objectId, Long instanceId, List<Long> attrIds) {
        FormObject formObject = objectService.selectByPK(objectId);
        return dynamicFormMapper.getAttrVal(formObject.getTableName(),instanceId,attrIds);
    }

    @Override
    public TableDto selectTableDto(Long objectId, Set<String> roleIds) {
        LayoutTable table = tableService.getEnableLayout(objectId);
        if(table!=null){
            TableDto tableDto=new TableDto();

            tableDto.setAttrs(attrService.getObjectAttributes(objectId));
            tableDto.setViews(tableService.selectRefViews(table.getId(),roleIds));

            return tableDto;
        }else{
            return null;
        }
    }

    @Override
    public void delBatch(String tableName, List<Long> ids) {
        dynamicFormMapper.delBatch(tableName,ids);
    }
}
