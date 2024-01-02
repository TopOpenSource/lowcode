package com.ruoyi.dynamicform.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@TableName("lowcode_inst_relation")
@Data
public class InstRelation {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "source_inst_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sInstId;

    @TableField(value = "source_obj_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sObjId;

    @TableField(value = "source_attr_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sAttrId;

    @TableField(value = "target_inst_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tInstId;

    @TableField(value = "target_obj_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tObjId;

    public InstRelation(){

    }

    public InstRelation(Long id,Long sObjId,Long sInstId,Long sAttrId,Long tInstId,Long tObjId){
        this.id=id;
        this.sObjId=sObjId;
        this.sInstId=sInstId;
        this.sAttrId=sAttrId;
        this.tInstId=tInstId;
        this.tObjId=tObjId;
    }
}
