package com.zswb.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AssetChangeDTO {
    // 唯一标识：原在账资产的设备编号
    private String equipmentCode;

    // 以下为可能的变动字段（根据业务需求增减）
    private String transferInUnitCode; // 转入单位号
    private String transferInLocationCode; // 现存放地编号
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
    // 其他需要变动的字段...
}