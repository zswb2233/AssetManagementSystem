package com.zswb.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class sbflbDTO {
    /**
     * 分类编号
     */

    private String fldm;
    /**
     * 分类名称
     */

    private String flmc;

    private BigDecimal[] nowDeCount;
    private BigDecimal[] nowDeAmount;

    // 构造函数
    public sbflbDTO(String fldm, String flmc) {
        this.fldm = fldm;
        this.flmc = flmc;

        // 初始化数组并设置默认值为 BigDecimal.ZERO
        this.nowDeCount = new BigDecimal[8];
        this.nowDeAmount = new BigDecimal[8];
        for (int i = 0; i < 8; i++) {
            this.nowDeCount[i] = BigDecimal.ZERO;
            this.nowDeAmount[i] = BigDecimal.ZERO;
        }

    }
    public void setNowDeAmountWithId(int index, BigDecimal amount) {
        if (index < 0 || index >= nowDeAmount.length) {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
        this.nowDeAmount[index] = Objects.requireNonNullElse(amount, BigDecimal.ZERO);
    }

    public void addNowDeCountWithId(int index, BigDecimal count) {
        if (index < 0 || index >= nowDeCount.length) {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
        BigDecimal addend = Objects.requireNonNullElse(count, BigDecimal.ZERO);
        this.nowDeCount[index] = this.nowDeCount[index].add(addend);
    }

    public void addNowDeAmountWithId(int index, BigDecimal amount) {
        if (index < 0 || index >= nowDeAmount.length) {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
        BigDecimal addend = Objects.requireNonNullElse(amount, BigDecimal.ZERO);
        this.nowDeAmount[index] = this.nowDeAmount[index].add(addend);
    }


    /**
     * 计算合计值
     * 0 初期
     * 1 购置
     * 2 其他
     * 3 合计
     * 4 报废
     * 5 其他
     * 6 合计
     * 7 期末
     */
    public void calculateSummaryValues() {
        // 计算增加部分的合计(索引3)
        BigDecimal addCountSum = BigDecimal.ZERO;
        BigDecimal addAmountSum = BigDecimal.ZERO;
        for (int i = 1; i < 3; i++) {
            addCountSum = addCountSum.add(nowDeCount[i]);
            addAmountSum = addAmountSum.add(nowDeAmount[i]);
        }
        this.nowDeCount[3] = addCountSum;
        this.nowDeAmount[3] = addAmountSum;

        // 计算减少部分的合计(索引6)
        BigDecimal deCountSum = BigDecimal.ZERO;
        BigDecimal deAmountSum = BigDecimal.ZERO;
        for (int i = 4; i < 6; i++) {
            deCountSum = deCountSum.add(nowDeCount[i]);
            deAmountSum = deAmountSum.add(nowDeAmount[i]);
        }
        this.nowDeCount[6] = deCountSum;
        this.nowDeAmount[6] = deAmountSum;

        // 计算期末值(索引7) = 初期值 + 增加合计 - 减少合计
        this.nowDeCount[7] = nowDeCount[0].add(nowDeCount[3]).subtract(nowDeCount[6]);
        this.nowDeAmount[7] = nowDeAmount[0].add(nowDeAmount[3]).subtract(nowDeAmount[6]);
    }
}
