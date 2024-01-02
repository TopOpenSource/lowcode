package com.ruoyi.form.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.file.FileTypeUtils;
import com.ruoyi.common.core.utils.file.MimeTypeUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.dto.ObjectDTO;
import com.ruoyi.form.service.ObjectService;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysFile;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/form/sysFormObject")
@Log4j2
public class ObjectController extends BaseController {
    @Autowired
    private ObjectService sysFormObjectService;
    @Autowired
    private RemoteFileService remoteFileService;

    @RequestMapping("selectByPK")
    public FormObject selectByPK(@RequestBody FormObject dto) {
        //租户
        LoginUser loginUser= SecurityUtils.getLoginUser();
        return sysFormObjectService.selectByPK(dto.getId());
    }

    /**
     * 分页查询数据
     *
     * @param dto
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(FormObject dto) {
        startPage();
        List<FormObject> list = sysFormObjectService.selectByDto(dto);
        return getDataTable(list);
    }


    @GetMapping("/listAll")
    public TableDataInfo listAll(ObjectDTO dto) {
        List<ObjectDTO> list = sysFormObjectService.listObjectDTO(dto);
        return getDataTable(list);
    }

    /**
     * 保存业务对象
     *
     * @param dto
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public AjaxResult save(@RequestBody FormObject dto) {
        if (dto.getId() == null) {
            sysFormObjectService.insert(dto);
        } else {
            sysFormObjectService.updateSelectiveByPK(dto);
        }
        return AjaxResult.success();
    }

    @PostMapping("/uploadIcon")
    public AjaxResult uploadIcon(MultipartFile file) {
        if (!file.isEmpty()) {
            String extension = FileTypeUtils.getExtension(file);
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION))
            {
                return AjaxResult.error("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
            }
            R<SysFile> fileResult = remoteFileService.upload(file);
            String url = fileResult.getData().getUrl();
            return AjaxResult.success().put("url",url);
        }
        return AjaxResult.error("上传失败");
    }

    /**
     * 根据id查询业务对象数据
     *
     * @param id
     * @return
     */
    @GetMapping("/select/{id}")
    public AjaxResult selectById(@PathVariable("id") Long id) {
        FormObject sysFormObject = sysFormObjectService.selectByPK(id);
        return AjaxResult.success(sysFormObject);
    }

    /**
     * 删除数据
     *
     * @param sysFormObject
     * @return
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody FormObject sysFormObject) {
        sysFormObjectService.deleteByObj(sysFormObject);
        return AjaxResult.success();
    }


    /**
     * 启用停用
     * @param sysFormObject
     * @return
     */
    @PostMapping("/updateObjState")
    public AjaxResult updateObjState(@RequestBody FormObject sysFormObject) {
        sysFormObjectService.updateObjState(sysFormObject);
        return AjaxResult.success();
    }




}

