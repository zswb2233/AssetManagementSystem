package com.zswb.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sbflb2")
public class sbflb2 {
    /**
     * 分类编号
     */
    @TableField("FLDM")
    private String fldm;
    /**
     * 分类名称
     */
    @TableField("FLMC")
    private String flmc;
}
