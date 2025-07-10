package com.zswb.model.output;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class zcbdbOutput {
    /** 使用单位名，原使用单位名称 */
    //由原单位编号生成
    //使用单位
    private String usingUnitName;

    /** 设备编号（主关键字）,资产编号 */
    //资产编号
    private String equipmentCode;

    /** 设备名称，,资产名称 */
    //资产名称
    private String equipmentName;

    /** 规格，规格/型号 */
    //规格/型号
    private String specification;


    /** 分类号 */
    //从资产分类号的前两位判断。
    //财政大类
    private String categoryCode;

    //业务类型
    private String status;

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

    //变动日期
    private Date changeDate;

    //转入单位
    private String transferInUnitName;
    //变动原因//
    private String changeReason;

}
