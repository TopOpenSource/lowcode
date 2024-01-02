package com.ruoyi.form.service.impl;

import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutForm;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.dto.LayoutTableDto;
import com.ruoyi.form.dto.ObjectDTO;
import com.ruoyi.form.mapper.*;
import com.ruoyi.form.service.*;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author
 */
@Service("sysFormObjectService")
@Log4j2
public class ObjectServiceImpl implements ObjectService {
    private static Snowflake snowflake = new Snowflake();

    @Autowired
    private ObjectDao sysFormObjectDao;
    @Autowired
    private TableUtilService tableUtilService;
    @Autowired
    private LayoutFormDao sysFormLayoutFormDao;
    @Autowired
    private LayoutTableDao sysFormLayoutTableDao;
    @Autowired
    private LayoutViewDao sysFormLayoutViewDao;
    @Autowired
    private ObjectAttributeDao sysFormObjectAttributeDao;
    @Autowired
    private FlowObjectMapper flowObjectMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectAttributeDao objectAttributeDao;

    @Autowired
    private LayoutViewService layoutViewService;

    @Autowired
    private LayoutTableService layoutTableService;

    @Autowired
    private LayoutFormService layoutFormService;

    private final String dynamicComponent = "customer/CustomerList";

    @Override
    public void updateByPK(FormObject dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        Date now = new Date();
        dto.setGmtModified(now);
        dto.setModifiedAccount(loginUser.getUsername());

        sysFormObjectDao.updateByPK(dto);
    }

    @Transactional
    @Override
    public void deleteByPK(Long id) {

    }

    @Override
    public FormObject selectByPK(Long id) {
        FormObject sysFormObject = sysFormObjectDao.selectByPK(id);
        return sysFormObject;
    }

    @Override
    public List<FormObject> selectByDto(FormObject dto) {
        return sysFormObjectDao.selectByDto(dto);
    }

    @Transactional
    @Override
    public void insert(FormObject dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        long objectId = snowflake.nextId();

        // 创建默认视图
        buildDemoView(loginUser,objectId);

        // 新增业务对象
        Date now = new Date();
        FormObject sysFormObject = new FormObject();

        //生成随机表名
        dto.setTableName("lowcode_table_" + objectId);
        dto.setState("1");
        BeanUtils.copyProperties(dto, sysFormObject);

        sysFormObject.setId(objectId);
        sysFormObject.setGmtCreate(now);
        sysFormObject.setGmtModified(now);
        sysFormObject.setCreateAccount(loginUser.getUsername());

        //创建表
        tableUtilService.createTable(dto.getTableName());

        sysFormObjectDao.insert(sysFormObject);

        //添加业务对象默认属性
        List<FormObjectAttribute> defaultAttrs = this.buildDefaultAttrs(objectId);
        defaultAttrs.forEach(attr -> {
            objectAttributeDao.insert(attr);
        });

    }

    @Transactional
    public void buildDemoView(LoginUser loginUser, long objectId) {
        // 添加默认视图
        LayoutView layoutView = new LayoutView();
        layoutView.setObjectId(objectId);
        layoutView.setName("默认列表视图");
        layoutView.setRoles("-1");
        layoutView.setSort("AES");
        layoutView.setSortColumn("gmt_create");
        layoutView.setTableColumn("create_account,gmt_create");
        layoutView.setType("0");
        layoutView.setCreateAccount(loginUser.getUsername());
        layoutView.setModifiedAccount(loginUser.getUsername());
        long viewId = snowflake.nextId();
        layoutView.setId(viewId);
        layoutViewService.insert(layoutView);

        // 添加列表布局
        LayoutTableDto layoutTableDto = new LayoutTableDto();
        layoutTableDto.setName("默认列表布局");
        layoutTableDto.setObjectId(objectId);
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(viewId));
        layoutTableDto.setViewIds(list);
        layoutTableDto.setEnable("1");
        layoutTableService.insert(layoutTableDto);


        // 默认表单页面布局
        LayoutForm layoutForm = new LayoutForm();
        layoutForm.setId(snowflake.nextId());
        layoutForm.setCreateAccount(loginUser.getUsername());
        layoutForm.setObjectId(objectId);
        layoutForm.setName("默认表单页面布局");
        layoutForm.setType("0");
        layoutForm.setEnable("1");
        layoutForm.setContent("[{\"id\":\"-1\",\"displayTitle\":\"默认标题\"}]");
        layoutFormService.save(layoutForm);
    }


    @Transactional
    @Override
    public void deleteByObj(FormObject sysFormObject) {
        //1、校验业务对象中主表是否存在数据
        if (tableUtilService.checkExitDataMainTable(sysFormObject.getTableName())) {
            throw new RuntimeException("业务对象存在数据，不能进行删除!");
        }

        //2、删除业务对象表中信息
        //2.1、删除业务对象主表信息
        sysFormObjectDao.deleteByPK(sysFormObject.getId());

        //2.2、删除业务对象字段、表单信息
        sysFormObjectAttributeDao.deleteByObjectId(sysFormObject.getId());
        sysFormLayoutFormDao.deleteByObjectId(sysFormObject.getId());
        sysFormLayoutTableDao.deleteByObjectId(sysFormObject.getId());
        sysFormLayoutViewDao.deleteByObjectId(sysFormObject.getId());
        sysFormLayoutTableDao.deleteViewByObjectId(sysFormObject.getId());

        //2.3、删除业务对象对应的流程信息
        if (sysFormObject.getIsFlow().equals("1")) {
            //2.3.1删除流程定义信息
            FlowObject flowObject = flowObjectMapper.selectByFormObjectId(sysFormObject.getId());
            if (Objects.nonNull(flowObject)) {
                repositoryService.deleteDeployment(flowObject.getDeploymentId(), true);

                //2.3.2删除业务对象和流程关联信息
                flowObjectMapper.deleteByFormObjectId(sysFormObject.getId());
            }
        }

        //3、删除业务对象数据 TODO
        tableUtilService.deleteTableByName(sysFormObject.getTableName());
    }


    @Override
    public List<ObjectDTO> listObjectDTO(ObjectDTO dto) {
        return sysFormObjectDao.listObjectDTO(dto);
    }

    @Transactional
    @Override
    public void updateObjState(FormObject sysFormObject) {
        // 若为上线操作，若存在流程，则判断业务对象是否创建流程
        if (sysFormObject.getIsFlow().equals("1") && sysFormObject.getState().equals("0")) {
            FlowObject flowObject = flowObjectMapper.selectByFormObjectId(sysFormObject.getId());
            if (Objects.isNull(flowObject)) {
                throw new RuntimeException("请先为业务对象创建工作流！");
            }
        }
        sysFormObjectDao.updateObjState(sysFormObject);
    }

    @Transactional
    @Override
    public void updateSelectiveByPK(FormObject dto) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (dto.getIsFlow().equals("1") && dto.getState().equals("0")) {
            FlowObject flowObject = flowObjectMapper.selectByFormObjectId(dto.getId());
            if (Objects.isNull(flowObject)) {
                throw new RuntimeException("请先为业务对象创建工作流！");
            }
        }
        FormObject sysFormObject = new FormObject();
        BeanUtils.copyProperties(dto, sysFormObject);
        sysFormObjectDao.updateSelectiveByPK(sysFormObject);
    }


    /**
     * 构建默认字段属性，创建人，修改人，创建时间，修改时间
     *
     * @return
     */
    private List<FormObjectAttribute> buildDefaultAttrs(Long objectId) {
        List<FormObjectAttribute> defaultAttrs = new ArrayList<>();
        Snowflake snowflake = new Snowflake();
        String userId = SecurityUtils.getUserId().toString();
        Date now = new Date();

        //创建人属性构建
        FormObjectAttribute createAccount = new FormObjectAttribute();
        createAccount.setId(snowflake.nextId());
        createAccount.setObjectId(objectId);
        createAccount.setName("创建人");
        createAccount.setAlias("create_account");
        createAccount.setType("user");
        createAccount.setDbType("varchar");
        createAccount.setGmtCreate(now);
        createAccount.setGmtModified(now);
        createAccount.setCreateAccount(userId);
        createAccount.setModifiedAccount(userId);
        createAccount.setDisplayType("input");
        createAccount.setIsDefault("1");
        createAccount.setNotnull("0");
        createAccount.setUnique("0");
        createAccount.setEnable("1");
        createAccount.setDisplayFullline("0");
        createAccount.setMultiple("0");
        defaultAttrs.add(createAccount);

        //修改人属性构建
        FormObjectAttribute modifiedAccount = new FormObjectAttribute();
        BeanUtils.copyProperties(createAccount, modifiedAccount);
        modifiedAccount.setId(snowflake.nextId());
        modifiedAccount.setName("修改人");
        modifiedAccount.setAlias("modified_account");
        defaultAttrs.add(modifiedAccount);

        //创建时间属性构建
        FormObjectAttribute gmtCreate = new FormObjectAttribute();
        gmtCreate.setObjectId(objectId);
        gmtCreate.setId(snowflake.nextId());
        gmtCreate.setName("创建时间");
        gmtCreate.setAlias("gmt_create");
        gmtCreate.setType("date");
        gmtCreate.setDbType("date");
        gmtCreate.setGmtCreate(now);
        gmtCreate.setGmtModified(now);
        gmtCreate.setCreateAccount(userId);
        gmtCreate.setModifiedAccount(userId);
        gmtCreate.setDisplayType("date");
        gmtCreate.setIsDefault("1");
        gmtCreate.setNotnull("0");
        gmtCreate.setUnique("0");
        gmtCreate.setEnable("1");
        gmtCreate.setDisplayFullline("0");
        gmtCreate.setMultiple("0");
        defaultAttrs.add(gmtCreate);

        //修改时间属性构建
        FormObjectAttribute gmtModified = new FormObjectAttribute();
        BeanUtils.copyProperties(gmtCreate, gmtModified);
        gmtModified.setId(snowflake.nextId());
        gmtModified.setName("修改时间");
        gmtModified.setAlias("gmt_modified");
        defaultAttrs.add(gmtModified);

        return defaultAttrs;
    }
}
