package com.zswb.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zswb.model.annotation.ExcelHeader;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("zczzb")
public class zczzb {

    /**
     * 设备编号（主关键字）,资产编号
     * 对应表中primary key `equipment_code`
     */
    @ExcelHeader("设备编号（资产编号）")
    @TableId("equipment_code")
    private String equipmentCode;

    /**
     * 设备名称，资产名称
     * 对应表中`equipment_name`字段
     */
    @ExcelHeader("设备名称（资产名称）")
    @TableField("equipment_name")
    private String equipmentName;

    /**
     * 分类号,详见2005高校实验室器材
     * 对应表中`category_code`字段
     */
    @ExcelHeader("分类号（2005高校实验室器材）")
    @TableField("category_code")
    private String categoryCode;

    /**
     * 规格，规格/型号
     * 对应表中`specification`字段
     */
    @ExcelHeader("规格/型号")
    @TableField("specification")
    private String specification;

    /**
     * 品牌型号，品牌
     * 对应表中`brand_model`字段
     */
    @ExcelHeader("品牌型号")
    @TableField("brand_model")
    private String brandModel;

    /**
     * 金额（单价）
     * 对应表中`JE`字段（与数据库字段直接映射）
     */
    @ExcelHeader("金额（单价）")
    @TableField("JE")
    private BigDecimal singleAmount;

    /**
     * 计量单位
     * 对应表中`jldw`字段（与数据库字段直接映射）
     */
    @ExcelHeader("计量单位")
    @TableField("jldw")
    private String measurementUnit;

    /**
     * 厂家
     * 对应表中`Cj`字段（与数据库字段直接映射）
     */
    @ExcelHeader("厂家")
    @TableField("Cj")
    private String manufacturer;

    /**
     * 购置日期
     * 对应表中`Ggrq`字段（与数据库字段直接映射）
     */
    @ExcelHeader("购置日期")
    @TableField("Ggrq")
    private Date purchaseDate;

    /**
     * 业务类型 现状（1:在用 2:闲置 3:待修 4:待报废 5:丢失 6:报废 7:出售 9:其它 A:调入 B:转入 C:转出 D:注销 E:盘亏 F:调剂 G:对外捐赠）
     * 对应表中`status`字段
     */
    @ExcelHeader("业务类型（现状）")
    @TableField("status")
    private String status;

    /**
     * 经费科目（1:教学 2:科研 3:基建 4:自筹经费 5:世界银行贷款 6:捐增 9:其它 A:研究生 B:贷款配套费 C:行政事业费 D:211经费 E:十五投资 F:985经费）
     * 对应表中`jfkm`字段（与数据库字段直接映射）
     */
    @ExcelHeader("经费科目")
    @TableField("jfkm")
    private String fundSubject;

    /**
     * 使用单位号，原使用单位号
     * 对应表中`using_unit_code`字段
     */
    @ExcelHeader("使用单位号")
    @TableField("using_unit_code")
    private String usingUnitCode;

    /**
     * 使用单位名，原使用单位名称
     * 对应表中`using_unit_name`字段
     */
    @ExcelHeader("使用单位名")
    @TableField("using_unit_name")
    private String usingUnitName;

    /**
     * 原存放地编号
     * 对应表中`storage_location_code`字段
     */
    @ExcelHeader("原存放地编号")
    @TableField("storage_location_code")
    private String storageLocationCode;

    /**
     * 原存放地名称
     * 对应表中`transfer_in_location_code`字段
     */
    @ExcelHeader("原存放地名称")
    @TableField("storage_location_name")
    private String storageLocationName;

    /**
     * 原数量
     * 对应表中`before_count`字段
     */
    @ExcelHeader("原数量")
    @TableField("before_count")
    private BigDecimal beforeCount;

    /**
     * 原金额
     * 对应表中`amount`字段
     */
    @ExcelHeader("原金额")
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 使用人编号
     * 对应表中`user_code`字段
     */
    @ExcelHeader("使用人编号")
    @TableField("user_code")
    private String userCode;

    /**
     * 使用人名称
     * 对应表中`user`字段
     */
    @ExcelHeader("使用人名称")
    @TableField("user")
    private String user;

    /**
     * 记帐人
     * 对应表中`accountant`字段
     */
    @ExcelHeader("记帐人")
    @TableField("accountant")
    private String accountant;

    /**
     * 入账时间
     * 对应表中`accounting_date`字段
     */
    @ExcelHeader("入账时间")
    @TableField("accounting_date")
    private Date accountingDate;

    /**
     * 备注
     * 对应表中`remarks`字段
     */
    @ExcelHeader("备注")
    @TableField("remarks")
    private String remarks;

    /**
     * 经手人
     * 对应表中`handler`字段
     */
    @ExcelHeader("经手人")
    @TableField("handler")
    private String handler;

    /**
     * 审核状态（0：未审 1：一审 2：二审 3：三审）
     * 对应表中`audit_status`字段
     */
    @ExcelHeader("审核状态")
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 业务单号（系统自动生成）
     * 对应表中`business_number`字段
     */
    @ExcelHeader("业务单号")
    @TableField("business_number")
    private String businessNumber;

    /**
     * 设备最近一次使用人
     * 对应表中input_person
     */
    @ExcelHeader("最近一次使用人")
    @TableField("input_person")
    private String inputPerson;

    /**
     * 设备最近一次输入时日期时间
     * 对应表中input_datetime
     */
    @ExcelHeader("最近一次输入时间")
    @TableField("input_datetime")
    private Timestamp inputDatetime;

    /**
     * 字符备用1
     * 对应表中`Zfby1`字段（与数据库字段直接映射）
     */
    @ExcelHeader("字符备用1")
    @TableField("Zfby1")
    private String reserveString1;

    /**
     * 字符备用2
     * 对应表中`Zfby2`字段（与数据库字段直接映射）
     */
    @ExcelHeader("字符备用2")
    @TableField("Zfby2")
    private String reserveString2;

    /**
     * 字符备用3
     * 对应表中`Zfby3`字段（与数据库字段直接映射）
     */
    @ExcelHeader("字符备用3")
    @TableField("Zfby3")
    private String reserveString3;

    /**
     * 字符备用4
     * 对应表中`Zfby4`字段（与数据库字段直接映射）
     */
    @ExcelHeader("字符备用4")
    @TableField("Zfby4")
    private String reserveString4;

    /**
     * 数字备用1
     * 对应表中`szby1`字段（与数据库字段直接映射）
     */
    @ExcelHeader("数字备用1")
    @TableField("szby1")
    private BigDecimal reserveNumber1;

    /**
     * 数字备用2
     * 对应表中`Szby2`字段（与数据库字段直接映射）
     */
    @ExcelHeader("数字备用2")
    @TableField("Szby2")
    private BigDecimal reserveNumber2;

    /**
     * 数字备用3
     * 对应表中`szby3`字段（与数据库字段直接映射）
     */
    @ExcelHeader("数字备用3")
    @TableField("szby3")
    private BigDecimal reserveNumber3;

    /**
     * 数字备用4
     * 对应表中`Szby4`字段（与数据库字段直接映射）
     */
    @ExcelHeader("数字备用4")
    @TableField("Szby4")
    private BigDecimal reserveNumber4;

}