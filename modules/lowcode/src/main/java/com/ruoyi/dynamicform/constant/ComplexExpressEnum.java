package com.ruoyi.dynamicform.constant;

/**
 * @author yangzz
 */

public enum ComplexExpressEnum {

    EXPRESS_GT("gt", "{col} > '{val}'"),
    EXPRESS_GTEQ("gtEq", "{col} >= '{val}'"),
    EXPRESS_LT("lt", "{col} < '{val}'"),
    EXPRESS_LTEQ("ltEq", "{col} <= '{val}'"),
    //等于
    EXPRESS_EQ("eq", "{col} = '{val}'"),
    EXPRESS_NOTEQ("notEq", "( {col} != '{val}' or {col} IS NULL )"),
    //空
    EXPRESS_NULL("null", "( {col} IS NULL or {col} = '' )"),
    EXPRESS_NOTNULL("notNull", "( {col} IS NOT NULL and {col} != '' )"),
    //包含 字符串
    EXPRESS_CONTENT("content", "{col} LIKE '%{val}%'"),
    EXPRESS_NO_CONTENT("noContent", "{col} NOT LIKE '%{val}%'"),
    //包含 字典、用户、组织
    EXPRESS_CONTENT_("content_", "FIND_IN_SET('{val}',{col})"),
    EXPRESS_NO_CONTENT_("noContent_", ""),
    //包含 关系
    EXPRESS_CONTENT_REF("content_ref", "EXISTS (select * from lowcode_inst_relation where source_inst_id={instanceId}  and source_attr_id={col}  and target_inst_id={val} )"),
    EXPRESS_NO_CONTENT_REF("noContent_ref", " not EXISTS (select * from lowcode_inst_relation where  id=xxx  source_attr_id={col}  and target_inst_id={val} )"),
    ;

    private String code;
    private String operate;

    ComplexExpressEnum(String code, String operate) {
        this.code = code;
        this.operate = operate;
    }

    public String getCode() {
        return code;
    }

    public String getOperate() {
        return operate;
    }

    public static ComplexExpressEnum getExpressEnum(String code){
        for (ComplexExpressEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ComplexExpressEnum.EXPRESS_EQ;
    }
}
