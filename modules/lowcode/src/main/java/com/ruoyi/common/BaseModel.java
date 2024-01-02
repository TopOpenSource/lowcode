package com.ruoyi.common;

import lombok.Data;

import java.util.Date;

/**
 * 
 * @author cheng
 *
 */
@Data
public class BaseModel {
    private Date gmtCreate;
    private Date gmtModified;
    private String createAccount;
    private String modifiedAccount;
    private String isDelete="0";

    public void setInsertOptInfo(Date gmtCreate,String createAccount){
        this.gmtCreate=gmtCreate;
        this.createAccount=createAccount;
    }

    public void setUpdataOptInfo(Date gmtModified,String modifiedAccount){
       this.gmtModified=gmtModified;
       this.modifiedAccount=modifiedAccount;
    }
}
