package com.ruoyi.dynamicform.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("lowcode_inst_file")
@Data
public class InstFile {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "file_name")
    private String name;

    @TableField(value = "storage_name")
    private String storageName;

    @TableField(value = "file_size")
    private BigDecimal size;

    @TableField(value = "up_time")
    private Date upTime;

    @TableField(value = "inst_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long instId;

    @TableField(value = "attr_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long attrId;

    public InstFile() {

    }

    public InstFile(Long id,String name, String storageName, BigDecimal size, Date upTime) {
        this.id=id;
        this.name = name;
        this.storageName = storageName;
        this.size = size;
        this.upTime = upTime;
    }
}
