package com.zswb.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备变动表实体类
 */
@Data
@TableName("zcbdb")
public class zcbdb implements Serializable {

    /** 使用单位号 */
    @TableField("lydwh")
    private String usingUnitCode;

    /** 使用单位名 */
    @TableField("lydwm")
    private String usingUnitName;

    /** 设备编号（主关键字） */
    @TableId("zcbh")
    private String equipmentCode;

    /** 分类号 */
    @TableField("zcflh")
    private String categoryCode;

    /** 设备名称 */
    @TableField("zcmc")
    private String equipmentName;

    /** 品牌型号 */
    @TableField("ppxh")
    private String brandModel;

    /** 规格 */
    @TableField("gg")
    private String specification;

    //下面两字段是单价。
    /** 金额 */
    @TableField("JE")
    private BigDecimal amount;

    /** 计量单位 */
    @TableField("jldw")
    private String measurementUnit;

    /** 厂家 */
    @TableField("Cj")
    private String manufacturer;

    /** 购置日期 */
    @TableField("Ggrq")
    private Date purchaseDate;

    /** 现状（1:在用 2:闲置 3:待修 4:待报废 5:丢失 6:报废 7:出售 9:其它 A:调入 B:转入 C:转出 D:注销 E:盘亏 F:调剂 G:对外捐赠） */
    @TableField("Xz")
    private String status;

    /** 经费科目（1:教学 2:科研 3:基建 4:自筹经费 5:世界银行贷款 6:捐增 9:其它 A:研究生 B:贷款配套费 C:行政事业费 D:211经费 E:十五投资 F:985经费） */
    @TableField("jfkm")
    private String fundSubject;

    /** 存放地编号 */
    @TableField("cfdbh")
    private String storageLocationCode;

    /** 存放地名称 */
    @TableField("cfdmc")
    private String storageLocationName;

    /** 使用人编号 */
    @TableField("SYRBH")
    private String userCode;

    /** 使用人 */
    @TableField("SYR")
    private String user;

    /** 经手人 */
    @TableField("JSR")
    private String handler;

    /** 审核状态（0：未审 1：一审 2：二审 3：三审） */
    @TableField("SHZT")
    private String auditStatus;

    /** 业务单号（系统自动生成） */
    @TableField("ywdh")
    private String businessNumber;

    /** 记帐人 */
    @TableField("jzr")
    private String accountant;

    /** 入账时间 */
    @TableField("Rzrq")
    private Date accountingDate;

    /** 备注 */
    @TableField("Bz")
    private String remarks;

    /** 字符备用1 */
    @TableField("Zfby1")
    private String reserveString1;

    /** 字符备用2 */
    @TableField("Zfby2")
    private String reserveString2;

    /** 数字备用1 */
    @TableField("szby1")
    private BigDecimal reserveNumber1;

    /** 数字备用2 */
    @TableField("Szby2")
    private BigDecimal reserveNumber2;

    /** 输入人 */
    @TableField("srr")
    private String inputter;

    /** 输入日期 */
    @TableField("srrq")
    private Date inputDate;

    /** 变动日期 */
    @TableField("bdrq")
    private Date changeDate;

    /** 变动原因 */
    @TableField("bdyy")
    private String changeReason;

    /** 转入单位号 */
    @TableField("zrdwh")
    private String transferInUnitCode;

    /** 申请人编号 */
    @TableField("sqrbh")
    private String applicantCode;

    /** 申请人 */
    @TableField("sqr")
    private String applicant;
}