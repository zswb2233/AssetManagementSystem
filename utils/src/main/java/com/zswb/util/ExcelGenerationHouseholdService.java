package com.zswb.util;

import com.zswb.model.dto.IndividualZcbdbTreeNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelGenerationHouseholdService {

    /**
     * 生成与PDF格式一致的分户增减变动统计表Excel
     * @param unitLevel 打印到几级
     * @param root 树状数据
     * @param tableName 表头名
     * @param tableUnit 制表单位
     * @param tableDate 制表日期
     * @param formDateFrom 开始日期
     * @param formDateTo 结束日期
     * @param jldw 计量单位（元/万元）
     * @return Excel字节数组
     */
    public byte[] generateIndividualHouseholdExcel(Integer unitLevel, IndividualZcbdbTreeNode root,
                                                   String tableName, String tableUnit, Date tableDate,
                                                   Date formDateFrom, Date formDateTo, String jldw) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("分户增减变动统计表");

            // 创建表头
            createHeader(sheet, tableName, tableUnit, formDateFrom, formDateTo, jldw);

            // 填充数据
            int rowIndex = 5; // 表头占用5行
            rowIndex = fillData(sheet, root, rowIndex, 0, unitLevel, jldw);

            // 添加说明和制表信息
            addFooter(sheet, rowIndex, tableDate);

            // 调整列宽
            autoSizeColumns(sheet);

            // 输出Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成Excel失败", e);
        }
    }

    /**
     * 创建Excel表头
     */
    private void createHeader(Sheet sheet, String tableName, String tableUnit,
                              Date formDateFrom, Date formDateTo, String jldw) {
        // 标题行
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(tableName);
        titleCell.setCellStyle(createTitleStyle(sheet));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12)); // 合并标题行

        // 信息行
        Row infoRow = sheet.createRow(1);
        infoRow.createCell(0).setCellValue("制表单位：" + tableUnit);
        infoRow.createCell(5).setCellValue("资产入账时间：" +
                new SimpleDateFormat("yyyy-MM-dd").format(formDateFrom) + "至" +
                new SimpleDateFormat("yyyy-MM-dd").format(formDateTo));
        infoRow.createCell(10).setCellValue("(单位：台件、" + jldw + ")");

        // 表头行1（合并单元格）
        Row headerRow1 = sheet.createRow(2);
        headerRow1.createCell(0).setCellValue("单位名称");
        headerRow1.createCell(1).setCellValue("期初数");
        headerRow1.createCell(2).setCellValue("本期增加数");
        headerRow1.createCell(6).setCellValue("本期减少数");
        headerRow1.createCell(10).setCellValue("期末数");

        // 表头行2
        Row headerRow2 = sheet.createRow(3);
        headerRow2.createCell(1).setCellValue("数量");
        headerRow2.createCell(2).setCellValue("购置");
        headerRow2.createCell(3).setCellValue("校内转入");
        headerRow2.createCell(4).setCellValue("其它");
        headerRow2.createCell(5).setCellValue("合计");
        headerRow2.createCell(6).setCellValue("报废报损");
        headerRow2.createCell(7).setCellValue("校内转出");
        headerRow2.createCell(8).setCellValue("其它");
        headerRow2.createCell(9).setCellValue("合计");
        headerRow2.createCell(10).setCellValue("数量");

        // 表头行3（金额行）
        Row headerRow3 = sheet.createRow(4);
        headerRow3.createCell(1).setCellValue("金额");
        headerRow3.createCell(10).setCellValue("金额");

        // 合并单元格 - 修正后的合并区域
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0)); // 单位名称 - 合并第3行到第5行的第1列
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 1, 1)); // 期初数 - 合并第3行到第5行的第2列
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 5)); // 本期增加数 - 合并第3行的第3列到第6列
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 9)); // 本期减少数 - 合并第3行的第7列到第10列
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 10, 10)); // 期末数 - 合并第3行到第5行的第11列
    }

    /**
     * 递归填充数据
     */
    private int fillData(Sheet sheet, IndividualZcbdbTreeNode node, int rowIndex,
                         int level, int maxLevel, String jldw) {
        if (node == null || (maxLevel > 0 && level >= maxLevel)) {
            return rowIndex;
        }

        // 数量行
        Row quantityRow = sheet.createRow(rowIndex++);
        quantityRow.createCell(0).setCellValue(node.getUnitName() + "(" + node.getUnitCode() + ")");
        quantityRow.createCell(1).setCellValue(node.getNowDeCount()[0].toString()); // 期初数-数量
        quantityRow.createCell(2).setCellValue(node.getNowDeCount()[1].toString()); // 购置-数量
        quantityRow.createCell(3).setCellValue(node.getNowDeCount()[2].toString()); // 校内转入-数量
        quantityRow.createCell(4).setCellValue(node.getNowDeCount()[3].toString()); // 其它-数量
        quantityRow.createCell(5).setCellValue(node.getNowDeCount()[4].toString()); // 本期增加合计-数量
        quantityRow.createCell(6).setCellValue(node.getNowDeCount()[5].toString()); // 报废报损-数量
        quantityRow.createCell(7).setCellValue(node.getNowDeCount()[6].toString()); // 校内转出-数量
        quantityRow.createCell(8).setCellValue(node.getNowDeCount()[7].toString()); // 其它-数量
        quantityRow.createCell(9).setCellValue(node.getNowDeCount()[8].toString()); // 本期减少合计-数量
        quantityRow.createCell(10).setCellValue(node.getNowDeCount()[9].toString()); // 期末数-数量

        // 金额行
        Row amountRow = sheet.createRow(rowIndex++);
        amountRow.createCell(1).setCellValue(convertToWan(node.getNowDeAmount()[0], jldw).toString()); // 期初数-金额
        amountRow.createCell(2).setCellValue(convertToWan(node.getNowDeAmount()[1], jldw).toString()); // 购置-金额
        amountRow.createCell(3).setCellValue(convertToWan(node.getNowDeAmount()[2], jldw).toString()); // 校内转入-金额
        amountRow.createCell(4).setCellValue(convertToWan(node.getNowDeAmount()[3], jldw).toString()); // 其它-金额
        amountRow.createCell(5).setCellValue(convertToWan(node.getNowDeAmount()[4], jldw).toString()); // 本期增加合计-金额
        amountRow.createCell(6).setCellValue(convertToWan(node.getNowDeAmount()[5], jldw).toString()); // 报废报损-金额
        amountRow.createCell(7).setCellValue(convertToWan(node.getNowDeAmount()[6], jldw).toString()); // 校内转出-金额
        amountRow.createCell(8).setCellValue(convertToWan(node.getNowDeAmount()[7], jldw).toString()); // 其它-金额
        amountRow.createCell(9).setCellValue(convertToWan(node.getNowDeAmount()[8], jldw).toString()); // 本期减少合计-金额
        amountRow.createCell(10).setCellValue(convertToWan(node.getNowDeAmount()[9], jldw).toString()); // 期末数-金额

        // 递归处理子节点
        if (node.getChildren() != null) {
            for (IndividualZcbdbTreeNode child : node.getChildren()) {
                rowIndex = fillData(sheet, child, rowIndex, level + 1, maxLevel, jldw);
            }
        }

        return rowIndex;
    }

    /**
     * 添加页脚信息
     */
    private void addFooter(Sheet sheet, int rowIndex, Date tableDate) {
        Row noteRow = sheet.createRow(rowIndex++);
        noteRow.createCell(0).setCellValue("注：房屋、土地、构筑物分别按建筑面积、使用权面积、构筑物计量数进行统计。");
        sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 10));

        Row footerRow = sheet.createRow(rowIndex++);
        footerRow.createCell(0).setCellValue("制表日期：" + new SimpleDateFormat("yyyy/MM/dd").format(tableDate) +
                " 制表人：admin");
        sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 10));
    }

    /**
     * 自动调整列宽
     */
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i <= 10; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500); // 增加额外宽度
        }
    }

    /**
     * 金额单位转换（万元）
     */
    private BigDecimal convertToWan(BigDecimal amount, String jldw) {
        if ("万".equals(jldw)) {
            return amount.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    /**
     * 创建标题样式
     */
    private CellStyle createTitleStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}