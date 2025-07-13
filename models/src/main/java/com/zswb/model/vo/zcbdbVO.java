package com.zswb.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;



import java.math.BigDecimal;
import java.util.Date;


/**
 * 设备变动表 VO 类，用于向前端传输数据
 */
@Data
public class zcbdbVO {
    /** 设备编号（主关键字）,资产编号 */
    private String equipmentCode;

    /** 设备名称，,资产名称 */
    private String equipmentName;

    /** 规格，规格/型号 */
    private String specification;

    /** 品牌型号，品牌 */
    private String brandModel;

    /**业务类型  */
    /** 现状（1:在用 2:闲置 3:待修 4:待报废 5:丢失 6:报废 7:出售 9:其它 A:调入 B:转入 C:转出 D:注销 E:盘亏 F:调剂 G:对外捐赠） */
    private String status;

    //下面几行是单位，存放地的改变，
    /** 使用单位号，原使用单位号 */
    private String usingUnitCode;

    /** 使用单位名，原使用单位名称 */
    private String usingUnitName;

    /** 转入单位号 */
    private String transferInUnitCode;

    /** 转入单位名称 */
    //由编号对应生成
    private String transferInUnitName;

    /** 原存放地编号 */
    private String storageLocationCode;
    //由编号对应生成
    /** 原存放地名称 */
    private String storageLocationName;

    /** 现存放地编号 */
    private String transferInLocationCode;
    //由编号对应生成
    /** 现存放地名称 */
    private String transferInLocationName;

    //原数量
    private BigDecimal  beforeCount ;
    //变动数量
    private BigDecimal  changedCount;

    //原金额
    /** 金额 */
    private BigDecimal amount;

    //变动金额(自己输入吧，反正有审核)
    /** 变动金额 */
    private BigDecimal changedAmount;

    //这个就不让他们修改了，当然也能拓展改变使用人
    /** 使用人编号 */
    private String userCode;

    /** 使用人名称 */
    private String user;

    //下三行是该资产入账时的情况。
    /** 记帐人 */
    private String accountant;

    /** 入账时间 */
    private Date accountingDate;

    /** 备注 */
    private String remarks;
    //下面三行表示一个资产变动表在申请，一审，二审，三审的状态
    //申请人->一审员->二审员->最终审核员
    /** 经手人 */
    private String handler;

    /** 审核状态（0：未审 1：一审 2：二审 3：三审） */
    private String auditStatus;

    /** 业务单号（系统自动生成） */
    private String businessNumber;


    //这些都是初审的时候生成的
    /** 变动申请人编号 */
    private String applicantCode;

    /** 变动申请人 */
    private String applicant;

    /** 变动日期 */
    private Date changeDate;

    /** 变动原因 */
    private String changeReason;




}