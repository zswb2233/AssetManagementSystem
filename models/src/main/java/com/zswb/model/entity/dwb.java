package com.zswb.model.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单位表实体类
 */
@Data
@TableName("dwb") // 指定对应的数据库表名
public class dwb  {


    /**
     * 单位编号，主键
     */
    @TableId("DWBH")
    private String unitCode;

    /**
     * 单位名称
     */
    @TableField("DWMC")
    private String unitName;

    /**
     * 上级单位编号
     */
    @TableField("DJDBH")
    private String parentUnitCode;

    /**
     * 单位性质,1.教学 2.科研 3.教辅4.行政 5.后勤 6.其它
     */
    @TableField("DWXZ")
    private Integer unitStatus;

    /**
     * 指单位的标志位，‘*’表示属于最低一级单位。
     */
    @TableField("DWBZ")
    private String isLast;

    /**
     * 字符备用1
     * 对应表中`Zfby1`字段（与数据库字段直接映射）
     */
    @TableField("Zfby1")
    private String reserveString1;

    /**
     * 字符备用2
     * 对应表中`Zfby2`字段（与数据库字段直接映射）
     */
    @TableField("Zfby2")
    private String reserveString2;



    /**
     * 数字备用1
     * 对应表中`szby1`字段（与数据库字段直接映射）
     */
    @TableField("szby1")
    private BigDecimal reserveNumber1;

    /**
     * 数字备用2
     * 对应表中`Szby2`字段（与数据库字段直接映射）
     */
    @TableField("Szby2")
    private BigDecimal reserveNumber2;
}