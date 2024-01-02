package com.ruoyi.dynamicform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.dynamicform.domain.InstFile;
import com.ruoyi.dynamicform.dto.InstFileDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * cheng
 */
public interface IInstFileService extends IService<InstFile> {
    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    InstFile upload(MultipartFile file) throws IOException;

    /**
     * 文件查询
     * @param dto
     * @return
     */
    List<InstFile> selectByDto(InstFileDto dto);

    void deleteByDto(InstFileDto dto);

    /**
     * 更新 实例和文件的关联
     */
    void updateByInst(List<String> idList,Long instId,Long attrId);

    void download(InstFile file, HttpServletResponse response);

    /**
     * 批量删除实例关联的文件
     * @param idList
     */
    void delBathByInstId(List<Long> idList);
}
