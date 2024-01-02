package com.ruoyi.dynamicform.dto;

import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutView;
import lombok.Data;

import java.util.List;

/**
 * 视图Dto
 */
@Data
public class ViewDto {
    private List<FormObjectAttribute> attrs;
    private LayoutView view;
}
