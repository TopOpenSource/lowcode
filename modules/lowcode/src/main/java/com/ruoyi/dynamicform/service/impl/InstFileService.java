package com.ruoyi.dynamicform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.domain.InstFile;
import com.ruoyi.dynamicform.dto.InstFileDto;
import com.ruoyi.dynamicform.mapper.InstFileMapper;
import com.ruoyi.dynamicform.service.IInstFileService;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service
public class InstFileService extends ServiceImpl<InstFileMapper, InstFile> implements IInstFileService {
    private static Snowflake snowflake = new Snowflake();

    @Autowired
    private RemoteFileService remoteFileService;

    @Override
    @Transactional
    public InstFile upload(MultipartFile file) throws IOException {
        // 上传并返回新文件名称  TODO
       /* String fileStorageName = FileUploadUtils.upload(filePath, file);
        InstFile sysFile = new InstFile(snowflake.nextId(), file.getOriginalFilename(), fileStorageName, BigDecimal.valueOf(file.getSize()), new Date());
        super.baseMapper.insert(sysFile);
        return sysFile;*/

        if (!file.isEmpty()) {
            R<SysFile> fileResult = remoteFileService.upload(file);
            String url = fileResult.getData().getUrl();
//            String str1 = url.substring(0,url.indexOf("statics/"))+"statics";
//            String str2 = url.substring(str1.length());
//            String path = localFilePath + str2;
            InstFile instFile = new InstFile(snowflake.nextId(), file.getOriginalFilename(), url, BigDecimal.valueOf(file.getSize()), DateUtils.getNowDate());
            super.baseMapper.insert(instFile);
            return instFile;
        }
        return null;
    }

    @Override
    public List<InstFile> selectByDto(InstFileDto dto) {

        QueryWrapper<InstFile> queryWrapper = new QueryWrapper();

        if (dto.getInstId() != null) {
            queryWrapper.eq("inst_id", dto.getInstId());
        }

        if (dto.getAttrId() != null) {
            queryWrapper.eq("attr_id", dto.getAttrId());
        }

        return super.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByDto(InstFileDto dto) {
        UpdateWrapper<InstFile> updateWrapper = new UpdateWrapper();

        updateWrapper.eq("inst_id", dto.getInstId());
        updateWrapper.eq("attr_id", dto.getAttrId());
        updateWrapper.set("inst_id",null).set("attr_id",null);

        super.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void updateByInst(List<String> idList,Long instId,Long attrId) {
        UpdateWrapper<InstFile> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idList).set("inst_id", instId).set("attr_id", attrId);
        super.baseMapper.update(null, updateWrapper);
    }

    @Override
    public void delBathByInstId(List<Long> idList) {
        QueryWrapper<InstFile> queryWrapper = new QueryWrapper();
        queryWrapper.in("inst_id",idList);
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public void download(InstFile file, HttpServletResponse response) {
        if(file!=null){
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            BufferedInputStream bufferedInputStream = null;

            try {
                URL url = getEncodeUrl(file.getStorageName());
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
//                conn.setRequestMethod("GET");
                //设置URL请求方式  GET方式
                conn.setRequestMethod("GET");
                //从HTTP响应消息获取状态码
                int code=conn.getResponseCode();
                //如果状态码为200表示请求成功
                if(code ==200){
                    bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                    ServletOutputStream outputStream = response.getOutputStream();
                    byte[] by=new byte[1024];
                    int len=0;
                    while((len=bufferedInputStream.read(by))!=-1) {
                        outputStream.write(by, 0, len);
                        //刷新
                        outputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedInputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public URL getEncodeUrl(String url) throws UnsupportedEncodingException, MalformedURLException {
        String[] array = url.split("/");
        String aim = array[array.length-1];
        String filePath = url.substring(0,url.indexOf(aim)) + URLEncoder.encode(aim, "UTF-8");
        URL url1 = new URL(filePath);
        return url1;
    }
}