package com.ruoyi.dynamicform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.dynamicform.domain.InstFile;
import com.ruoyi.dynamicform.dto.InstFileDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstFileMapper extends BaseMapper<InstFile> {

}
