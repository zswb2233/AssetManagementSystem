package com.zswb.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 前端往后端传的数据类
 */
@Data
public class AssetChangeDTO {
    // 唯一标识：原在账资产的设备编号
    private String equipmentCode;

    // 以下为可能的变动字段（根据业务需求增减）
    private String transferInUnitCode; // 转入单位号
    private String transferInLocationCode; // 转入存放地编号
    private BigDecimal changedCount; // 变动数量
    private BigDecimal changedAmount; // 变动金额
    private String userCode; // 使用人编号（若变动）
    private String user; // 使用人名称（若变动）
    private String applicantCode; // 变动申请人编号
    private String applicant; // 变动申请人
    private Date changeDate; // 变动日期
    private String changeReason; // 变动原因
    private String invoiceNumber; // 发票号
    private String supplier; // 供货商
    private String changedDocumentNumber; // 变动单据号
    private String changedReview; // 变动审核状态（默认未审）
    // 其他需要变动的字段...\
    /**
     * 业务类型 现状（1:在用 2:闲置 3:待修 4:待报废 5:丢失 6:报废 7:出售 9:其它 A:调入 B:转入 C:转出 D:注销 E:盘亏 F:调剂 G:对外捐赠）
     * 对应表中`status`字段
     */
    private String status;//变动的状态//传入进来的是数字表示


    /*
    {
  "equipmentCode": "0401267S",  // 必传：原资产的设备编号（主键）//银幕，给他换个使用人测试一下。
  "transferInUnitCode": "",  // 转入单位号（如校内调拨时必填）
  "transferInLocationCode": "",  // 转入存放地编号（如位置变动时必填）
  "changedCount": "",  // 变动数量（如数量调整时填写）
  "changedAmount": "",  // 变动金额（如减值时填写）
  "userCode": "USER004",  // 新使用人编号（如使用人调整时必填）
  "user": "张三",  // 新使用人名称（如使用人调整时必填）
  "applicantCode": "APP005",  // 变动申请人编号（必传）
  "applicant": "李四",  // 变动申请人姓名（必传）
  "changeDate": "2025-07-13",  // 变动日期（格式：yyyy-MM-dd）
  "changeReason": "测试调动使用人",  // 变动原因（必传）
  "invoiceNumber": "FP20250713001",  // 发票号（如涉及采购时填写）
  "supplier": "XX科技有限公司",  // 供货商（如涉及采购时填写）
  "changedDocumentNumber": "BD20250713001",  // 变动单据号（系统或手动生成） .BD+日期+今天的第几份.
  "changedReview": "0",  // 变动审核状态（默认0-未审，可省略由后端默认设置）
  "status": "F"  // 业务类型（如F-调剂，对应DTO中的说明）
}
     */
}