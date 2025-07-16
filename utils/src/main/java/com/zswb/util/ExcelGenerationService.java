package com.zswb.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.zswb.model.dto.IndividualZcbdbTreeNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.zswb.model.dto.sbflbDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ExcelGenerationService {

    // 生成分类统计Excel（对应PDF的 generateIndividualCategoryPdf 方法）
    public default byte[] generateIndividualCategoryExcel(
            List<sbflbDTO> sbflbDTOList,
            String tableName,
            String tableUnit,
            Date tableDate,
            Date formDateFrom,
            Date formDateTo,
            String jldw
    ) throws IOException {
        // 创建Excel工作簿（.xlsx格式）
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet(tableName);
        // 设置列宽（根据PDF列宽比例调整，单位为1/256个字符宽度）
        int[] columnWidths = {30*256, 15*256, 20*256, 18*256, 18*256, 20*256, 18*256, 18*256, 20*256, 20*256};
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i]);
        }

        // 1. 添加标题行
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(tableName);
        // 合并标题行（跨所有列）
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnWidths.length-1));
        // 设置标题样式（居中、加粗）
        CellStyle titleStyle = createStyle(workbook, true, HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        // 2. 添加信息行（制表单位、时间范围等，对应PDF的 infoTable）
        Row infoRow = sheet.createRow(rowIndex++);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        infoRow.createCell(0).setCellValue("制表单位：" + tableUnit);
        infoRow.createCell(3).setCellValue("资产入账时间：" + sdf.format(formDateFrom) + "至" + sdf.format(formDateTo));
        infoRow.createCell(6).setCellValue("(单位：台件、" + jldw + ")");
        // 设置信息行样式
        CellStyle infoStyle = createStyle(workbook, false, HorizontalAlignment.LEFT);
        for (int i = 0; i < 7; i++) {
            if (infoRow.getCell(i) != null) {
                infoRow.getCell(i).setCellStyle(infoStyle);
            }
        }

        // 3. 添加表头（对应PDF的 addHeaderCellsForCategoryPdf 方法）
        // 表头第一行（合并单元格）
        Row headerRow1 = sheet.createRow(rowIndex++);
        // 表头第二行（子标题）
        Row headerRow2 = sheet.createRow(rowIndex++);
        // 设置表头样式（灰色背景、加粗、居中）
        CellStyle headerStyle = createHeaderStyle(workbook);

        // 表头第一行：合并单元格设置
        // "分类名称" 跨2列2行
        setMergedHeader(headerRow1, 0, 0, 1, "分类名称", headerStyle, sheet);
        // "期初数" 跨1列2行
        setMergedHeader(headerRow1, 2, 2, 1, "期初数", headerStyle, sheet);
        // "本期增加数" 跨3列1行
        setMergedHeader(headerRow1, 3, 5, 0, "本期增加数", headerStyle, sheet);
        // "本期减少数" 跨3列1行
        setMergedHeader(headerRow1, 6, 8, 0, "本期减少数", headerStyle, sheet);
        // "期末数" 跨1列2行
        setMergedHeader(headerRow1, 9, 9, 1, "期末数", headerStyle, sheet);

        // 表头第二行：子标题（购置、其它、合计等）
        String[] subHeaders = {"", "", "", "购置", "其它", "合计", "报废报损", "其它", "合计"};
        for (int i = 0; i < subHeaders.length; i++) {
            Cell cell = headerRow2.createCell(i);
            cell.setCellValue(subHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // 4. 添加数据行（对应PDF的 addCategoryListToTable 方法）
        CellStyle dataStyle = createStyle(workbook, false, HorizontalAlignment.RIGHT); // 数据右对齐
        CellStyle dataLeftStyle = createStyle(workbook, false, HorizontalAlignment.LEFT); // 分类名称左对齐
        for (sbflbDTO dto : sbflbDTOList) {
            // 数据行1：数量行
            Row dataRow1 = sheet.createRow(rowIndex++);
            // 数据行2：金额行
            Row dataRow2 = sheet.createRow(rowIndex++);

            // 分类名称（跨两行）
            Cell nameCell1 = dataRow1.createCell(0);
            nameCell1.setCellValue(dto.getFldm() + "." + dto.getFlmc());
            nameCell1.setCellStyle(dataLeftStyle);
            sheet.addMergedRegion(new CellRangeAddress(dataRow1.getRowNum(), dataRow2.getRowNum(), 0, 0));

            // 数量/金额标识（跨两行）
            Cell flagCell1 = dataRow1.createCell(1);
            flagCell1.setCellValue("数量");
            flagCell1.setCellStyle(dataStyle);
            Cell flagCell2 = dataRow2.createCell(1);
            flagCell2.setCellValue("金额");
            flagCell2.setCellStyle(dataStyle);
            sheet.addMergedRegion(new CellRangeAddress(dataRow1.getRowNum(), dataRow2.getRowNum(), 1, 1));

            // 填充数量数据（第一行）和金额数据（第二行）
            fillDataToRow(dataRow1, dto.getNowDeCount(), jldw, false, dataStyle); // 数量
            fillDataToRow(dataRow2, dto.getNowDeAmount(), jldw, true, dataStyle);  // 金额
        }

        // 5. 添加备注和页脚（对应PDF的 note 和 footerPara）
        Row noteRow = sheet.createRow(rowIndex++);
        noteRow.createCell(0).setCellValue("注：房屋、土地、构筑物分别按建筑面积、使用权面积、构筑物计量数进行统计。");
        noteRow.getCell(0).setCellStyle(infoStyle);

        Row footerRow = sheet.createRow(rowIndex++);
        footerRow.createCell(0).setCellValue("制表日期：" + sdf.format(tableDate) + " 制表人：admin");
        footerRow.getCell(0).setCellStyle(infoStyle);

        // 输出Excel到字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }

    // 工具方法：创建单元格样式
    private CellStyle createStyle(Workbook workbook, boolean isBold, HorizontalAlignment alignment) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        style.setFont(font);
        style.setAlignment(alignment);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true); // 自动换行
        return style;
    }

    // 工具方法：创建表头样式（灰色背景）
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = createStyle(workbook, true, HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // 浅灰色背景
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    // 工具方法：设置合并表头
    private void setMergedHeader(Row row, int startCol, int endCol, int rowSpan, String value, CellStyle style, Sheet sheet) {
        Cell cell = row.createCell(startCol);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        int firstRow = row.getRowNum();
        sheet.addMergedRegion(new CellRangeAddress(firstRow, firstRow + rowSpan, startCol, endCol));
    }

    // 工具方法：填充数据到行
    private void fillDataToRow(Row row, BigDecimal[] data, String jldw, boolean isAmount, CellStyle style) {
        for (int i = 0; i < 8; i++) { // 对应8列数据（期初数+增加+减少+期末）
            BigDecimal value = data != null && i < data.length ? data[i] : BigDecimal.ZERO;
            // 金额且单位为"万"时，转换为万元（除以10000）
            if (isAmount && "万".equals(jldw)) {
                value = value.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP);
            }
            Cell cell = row.createCell(i + 2); // 从第2列开始（0:分类名称，1:数量/金额）
            cell.setCellValue(value.toString());
            cell.setCellStyle(style);
        }
    }


}