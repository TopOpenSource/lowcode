package com.ruoyi.instance.service.impl;

import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.dynamicform.dto.DynamicQueryDto;
import com.ruoyi.dynamicform.dto.LayoutViewDto;
import com.ruoyi.dynamicform.dto.ViewDto;
import com.ruoyi.dynamicform.service.DynamicFormService;
import com.ruoyi.dynamicform.service.IInstFileService;
import com.ruoyi.dynamicform.service.IInstRelationService;
import com.ruoyi.dynamicform.service.impl.InstRelationService;
import com.ruoyi.flow.service.WorkFlowService;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.service.LayoutViewService;
import com.ruoyi.instance.dto.InstanceDelDto;
import com.ruoyi.instance.dto.RelQueryDto;
import com.ruoyi.instance.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private DynamicFormService dynamicFormService;
    @Autowired
    private LayoutViewService viewService;
    @Autowired
    private IInstFileService instFileService;
    @Autowired
    private IInstRelationService instRelationService;
    @Autowired
    private WorkFlowService workFlowService;
    final Long objectId = 1L;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(CommonDto dto) {
        long objectId = Long.parseLong(dto.get("objectId").toString());
        List<FormObjectAttribute> attributes = dynamicFormService.selAttributesByObjectId(objectId);
        FormObject object = dynamicFormService.selObjectById(objectId);
        // dynamicFormService.valid(objectId,dto,object,attributes);
        dynamicFormService.save(objectId, dto, object, attributes);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(CommonDto dto) {
        long objectId = Long.parseLong(dto.get("objectId").toString());
        List<FormObjectAttribute> attributes = dynamicFormService.selAttributesByObjectId(objectId);
        FormObject object = dynamicFormService.selObjectById(objectId);
        // dynamicFormService.valid(objectId,dto,object,attributes);
        dynamicFormService.update(objectId, dto, object, attributes);
    }

    @Override
    public CommonDto selectByPk(CommonDto dto) {
        Long id = dto.getAsLong("id");
        Long objectId = dto.getAsLong("objectId");
        //获取配置的属性
        List<FormObjectAttribute> attributes = dynamicFormService.selAttributesByObjectId(objectId);
        //获取配置的属性
        CommonDto resultDto = dynamicFormService.selectByPk(objectId, id, attributes);
        return resultDto;
    }


    @Override
    public ViewDto listRefView(Long objectId) {
        //视图
        LayoutView layoutView = dynamicFormService.selRefViewByObjectId(objectId);
        //属性
        List<FormObjectAttribute> attributes = dynamicFormService.selAttributesByObjectId(objectId);

        ViewDto refViewDto=new ViewDto();
        refViewDto.setView(layoutView);
        refViewDto.setAttrs(attributes);
        return refViewDto;
    }

    @Override
    public Integer checkRepeat(Long id, Object value, String alias) {
        FormObjectAttribute attribute = dynamicFormService.selAttributesByAlias(objectId, alias);
        FormObject object = dynamicFormService.selObjectById(objectId);
        return dynamicFormService.checkRepeat(id, value, attribute, object);
    }

    @Override
    public CommonDto getAttrVal(Long objectId, Long instanceId, List<Long> attrIds) {
        return dynamicFormService.getAttrVal(objectId, instanceId, attrIds);
    }

    /**
     * 生成动态查询条件
     * @param dto
     * @return
     */
    @Override
    public DynamicQueryDto genDynamicQuery(LayoutViewDto dto) {
        LayoutView layoutView = viewService.selectByPK(dto.getViewId());
        LayoutViewDto view=LayoutViewDto.parseView(layoutView);
        view.setQueryCriteriaAppend(dto.getQueryCriteriaAppend());
        return  dynamicFormService.genDynamicQuery(view);
    }

    @Override
    public List<CommonDto> listRelInstance(RelQueryDto dto) {
        String tableColumn=dto.getTableColumn();
        if(tableColumn==null){
           //TODO 根据viewID
        }

        DynamicQueryDto queryDto=new DynamicQueryDto();

        FormObject formObject = dynamicFormService.selObjectById(dto.getTargetObjId());
        queryDto.setTableName(formObject.getTableName());

        //生成查询字段
        queryDto.setQueryCols(LayoutViewDto.genQueryCols(tableColumn));
        //生成where
        queryDto.setQueryWhere(" EXISTS (select * from lowcode_inst_relation tt where  tt.source_inst_id='"+dto.getSourceInstId()+"'  and tt.source_attr_id='"+dto.getSourceAttrId()+"'  and tt.target_inst_id= t1.id )");

        return dynamicFormService.selectList(queryDto);
    }

    @Override
    @Transactional
    public void delBatch(InstanceDelDto dto) {
        //表、lowcode_inst_file、lowcode_inst_relation、流程关联  流程
        FormObject formObject = dynamicFormService.selObjectById(dto.getObjectId());

        dynamicFormService.delBatch(formObject.getTableName(),dto.getIds());

        instFileService.delBathByInstId(dto.getIds());

        instRelationService.delBathByInstId(dto.getIds(), InstRelationService.INST_SOURCE);

        if(formObject.getIsFlow().equals("1")){
            workFlowService.delBatchByInstId(dto.getIds());
        }
    }

}
