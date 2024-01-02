package com.ruoyi.dynamicform.dto;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.dynamicform.constant.ComplexExpressEnum;
import lombok.Data;

import java.util.List;

@Data
class Item{
    private String query;
    private String queryCriteria;
    private String value;
    private String isDefault;
}


@Data
class Group{
    private String relation;
    private List<Item> itemList;
}

@Data
public class Query {
    private String relation;
    private List<Group> groupList;

    /**
     * 构建where条件
     */
    public String parseWhere(){
        String loginUserName= SecurityUtils.getUsername();
        String loginDeptId=String.valueOf(SecurityUtils.getLoginUser().getSysUser().getDeptId());


        StringBuffer sb=new StringBuffer();
        sb.append("(");

        for(int i=0;i<this.groupList.size();i++){
            if(i>0){
                //  and or
                sb.append(this.relation);
            }

            sb.append("(");

            Group group = this.groupList.get(i);
            for(int j=0;j<group.getItemList().size();j++){
                if(j>0){
                    //  and or
                    sb.append(group.getRelation());
                }

                Item item = group.getItemList().get(j);
                //获取对应的条件表达式
                String oper=ComplexExpressEnum.getExpressEnum(item.getQueryCriteria()).getOperate();

                String value="";
                if(item.getValue()!=null){
                    value=item.getValue().replace("{LOGIN_USER}",loginUserName).replace("{LOGIN_ORG}",loginDeptId);
                }

                sb.append(" "+oper
                        .replace("{col}",item.getQuery())
                        .replace("{val}",value)
                        +" ");
            }

            sb.append(")");
        }

        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        String str="{\"relation\":\"AND\",\"groupList\":[{\"itemList\":[{\"query\":\"c984392931567775744\",\"queryCriteria\":\"eq\",\"value\":\"张三\"}],\"relation\":\"AND\"}]}";
        Query query= JSONObject.parseObject(str,Query.class);
        System.out.println(query.parseWhere());
    }
}
