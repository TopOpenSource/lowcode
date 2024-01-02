package com.ruoyi.instance.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.dynamicform.domain.InstFile;
import com.ruoyi.dynamicform.dto.InstFileDto;
import com.ruoyi.dynamicform.service.IInstFileService;
import com.ruoyi.system.api.RemoteFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/lowcode/file")
public class FileController
{

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IInstFileService instFileService;

    /**
     * 通用下载请求
     * 
     */
    @RequestMapping("/download/{fileId}")
    public void fileDownload(@PathVariable("fileId") String fileId,HttpServletResponse response, HttpServletRequest request) throws IOException {

        InstFile file = instFileService.getById(fileId);
        instFileService.download(file,response);
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        InstFile upload = instFileService.upload(file);
        return AjaxResult.success(upload);
    }

    @PostMapping("/listFiles")
    public List<InstFile> listFiles(@RequestBody  InstFileDto dto){
       return instFileService.selectByDto(dto);
    }


}
