package com.zswb.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
    定义一个树结构进行分户统计
 */
@Data
public class IndividualZcbdbTreeNode {
    //单位号
    private String unitCode;
    //单位名
    private String unitName;

    //数据
    //10项分别0-9对应
    /**
     * 0 初期
     * 1 购置
     * 2 校内转入
     * 3 其他
     * 4 合计(购置+校内转入+其他)
     * 5 报废报损
     * 6 校内转出
     * 7 其他
     * 8 合计(报废报损+校内转出+其他)
     * 9 期末(初期+增加合计-减少合计)
     */
    private BigDecimal[] nowDeCount;
    private BigDecimal[] nowDeAmount;

    //父节点，父节点只有一个
    private IndividualZcbdbTreeNode parent;
    //孩子节点
    private List<IndividualZcbdbTreeNode> children;

    // 构造函数
    public IndividualZcbdbTreeNode(String unitCode, String unitName) {
        this.unitCode = unitCode;
        this.unitName = unitName;

        // 初始化数组并设置默认值为 BigDecimal.ZERO
        this.nowDeCount = new BigDecimal[10];
        this.nowDeAmount = new BigDecimal[10];
        for (int i = 0; i < 10; i++) {
            this.nowDeCount[i] = BigDecimal.ZERO;
            this.nowDeAmount[i] = BigDecimal.ZERO;
        }

        this.parent = null;
        this.children = new ArrayList<>();
    }

    public void setNowDeCountWithId(int index, BigDecimal count) {
        if (index < 0 || index >= nowDeCount.length) {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
        this.nowDeCount[index] = Objects.requireNonNullElse(count, BigDecimal.ZERO);
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
     * 递归汇总所有子节点数据到当前节点（父节点）
     */
    public void sumChildrenData() {
        // 1. 先递归汇总所有子节点的数据
        if (this.children != null && !this.children.isEmpty()) {
            for (IndividualZcbdbTreeNode child : this.children) {
                child.sumChildrenData();
                this.addChildDataToParent(child);
            }
        }
        // 2. 计算当前节点的合计值
        this.calculateSummaryValues();
    }

    /**
     * 将子节点的数据累加到当前父节点对应索引位置
     */
    private void addChildDataToParent(IndividualZcbdbTreeNode child) {
        for (int i = 0; i < 10; i++) {
            // 处理数量
            BigDecimal childCount = Objects.requireNonNullElse(child.nowDeCount[i], BigDecimal.ZERO);
            this.nowDeCount[i] = this.nowDeCount[i].add(childCount);

            // 处理金额
            BigDecimal childAmount = Objects.requireNonNullElse(child.nowDeAmount[i], BigDecimal.ZERO);
            this.nowDeAmount[i] = this.nowDeAmount[i].add(childAmount);
        }
    }

    /**
     * 计算当前节点的合计值
     */
    public void calculateSummaryValues() {
        // 计算增加部分的合计(索引4)
        BigDecimal addCountSum = BigDecimal.ZERO;
        BigDecimal addAmountSum = BigDecimal.ZERO;
        for (int i = 1; i < 4; i++) {
            addCountSum = addCountSum.add(nowDeCount[i]);
            addAmountSum = addAmountSum.add(nowDeAmount[i]);
        }
        this.nowDeCount[4] = addCountSum;
        this.nowDeAmount[4] = addAmountSum;

        // 计算减少部分的合计(索引8)
        BigDecimal deCountSum = BigDecimal.ZERO;
        BigDecimal deAmountSum = BigDecimal.ZERO;
        for (int i = 5; i < 8; i++) {
            deCountSum = deCountSum.add(nowDeCount[i]);
            deAmountSum = deAmountSum.add(nowDeAmount[i]);
        }
        this.nowDeCount[8] = deCountSum;
        this.nowDeAmount[8] = deAmountSum;

        // 计算期末值(索引9) = 初期值 + 增加合计 - 减少合计
        this.nowDeCount[9] = nowDeCount[0].add(nowDeCount[4]).add(nowDeCount[8]);
        this.nowDeAmount[9] = nowDeAmount[0].add(nowDeAmount[4]).add(nowDeAmount[8]);
    }
}