package com.zswb.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    //初期数量
//    BigDecimal fromCount;
//    BigDecimal fromAmount;


//    //本期增加数
//    //购置，校内转入，其他，合计
//    BigDecimal[] nowAddCount;
//    BigDecimal[] nowAddAmount;
//    //本期减少数
//    //报废报损，校内转出，其他，合计
//    BigDecimal[] nowDeCount;
//    BigDecimal[] nowDeAmount;
//
//    //本期期末数
//    BigDecimal toCount;
//    BigDecimal toAmount;
//初期数量
//    //购置，校内转入，其他，合计
//    //报废报损，校内转出，其他，合计
//    //本期期末数
    //10项分别0-9对应
    /**
     * 0 初期
     * 1 购置
     * 2 校内转入
     * 3 其他
     * 4 合计
     * 5 报废报损
     * 6 校内转出
     * 7 其他
     * 8 合计
     * 9 期末
     */
    BigDecimal[] nowDeCount;
    BigDecimal[] nowDeAmount;

    //父节点，父节点只有一个
    private IndividualZcbdbTreeNode parent;
    //孩子节点
    private List<IndividualZcbdbTreeNode> children;


    // 构造函数

    // 构造函数
    public IndividualZcbdbTreeNode(String unitCode, String unitName) {
        this.unitCode = unitCode;
        this.unitName = unitName;

        // 初始化 nowDeCount 数组并设置默认值为 BigDecimal.ZERO
        this.nowDeCount = new BigDecimal[10];
        for (int i = 0; i < 10; i++) {
            this.nowDeCount[i] = BigDecimal.ZERO;
        }

        // 初始化 nowDeAmount 数组并设置默认值为 BigDecimal.ZERO
        this.nowDeAmount = new BigDecimal[10];
        for (int i = 0; i < 10; i++) {
            this.nowDeAmount[i] = BigDecimal.ZERO;
        }

        this.parent = null;
        this.children = new ArrayList<>(); // 初始化子节点列表（避免null）
    }
    public void setNowDeCountWithId( int index,BigDecimal Count){
        this.nowDeCount[index]=Count;
    }
    public void setNowDeAmountWithId( int index,BigDecimal Amount){
        this.nowDeAmount[index]=Amount;
    }

    public void addNowDeCountWithId( int index,BigDecimal Count){

// 检查索引是否越界
        if (index >= 0 && index < nowDeCount.length) {
            // 正确更新：将计算结果赋值回数组
            this.nowDeCount[index] = this.nowDeCount[index].add(Count);
        } else {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
    }
    public void addNowDeAmountWithId( int index,BigDecimal Amount){

// 检查索引是否越界
        if (index >= 0 && index < nowDeAmount.length) {
            // 正确更新：将计算结果赋值回数组
            this.nowDeAmount[index] = this.nowDeAmount[index].add(Amount);
        } else {
            throw new IllegalArgumentException("索引超出范围: " + index);
        }
    }
    /**
     * 递归汇总所有子节点数据到当前节点（父节点）
     */
    public void sumChildrenData() {
        // 1. 先递归汇总所有子节点的数据（确保子节点先完成自身汇总）
        if (this.children != null && !this.children.isEmpty()) {
            for (IndividualZcbdbTreeNode child : this.children) {
                // 递归调用：子节点先汇总自己的子节点数据
                child.sumChildrenData();
                // 将子节点的数据累加到当前父节点
                this.addChildDataToParent(child);
            }
        }
    }

    /**
     * 将子节点的数据累加到当前父节点对应索引位置
     * @param child 子节点
     */
    private void addChildDataToParent(IndividualZcbdbTreeNode child) {
        // 遍历数组的10个索引位置（0-9），逐个累加
        for (int i = 0; i < 10; i++) {
            // 处理数量（nowDeCount）
            if (child.nowDeCount[i] != null) {
                // 父节点当前索引值为null时，初始化为0
                BigDecimal parentCount = this.nowDeCount[i] == null ? BigDecimal.ZERO : this.nowDeCount[i];
                // 累加子节点数据并赋值回父节点
                this.nowDeCount[i] = parentCount.add(child.nowDeCount[i]);
            }

            // 处理金额（nowDeAmount）
            if (child.nowDeAmount[i] != null) {
                // 父节点当前索引值为null时，初始化为0
                BigDecimal parentAmount = this.nowDeAmount[i] == null ? BigDecimal.ZERO : this.nowDeAmount[i];
                // 累加子节点数据并赋值回父节点
                this.nowDeAmount[i] = parentAmount.add(child.nowDeAmount[i]);
            }
        }
    }
}
