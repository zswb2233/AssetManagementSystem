package com.zswb.assetinbook.utils;

import com.zswb.model.annotation.ExcelHeader;
import com.zswb.model.entity.zczzb;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class ExcelUtils {

    // 生成Excel模板（中文表头）
    public static void generateTemplate(OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("资产信息");

        // 创建表头行
        Row headerRow = sheet.createRow(0);

        // 获取zczzb类的所有字段
        Field[] fields = zczzb.class.getDeclaredFields();
        for (int i = 0; i < fields.length-8; i++) {
            Field field = fields[i];
            // 获取字段上的ExcelHeader注解
            ExcelHeader header = field.getAnnotation(ExcelHeader.class);
            String headerName = header != null ? header.value() : field.getName(); // 有注解用中文，否则用字段名

            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerName); // 中文表头
        }

        workbook.write(outputStream);
        workbook.close();
    }

    // 生成带数据的Excel（中文表头）
    public static void generateExcelWithData(List<zczzb> dataList, OutputStream outputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("资产信息");

        // 创建表头行（中文）
        Row headerRow = sheet.createRow(0);
        Field[] fields = zczzb.class.getDeclaredFields();
        for (int i = 0; i < fields.length-8; i++) {
            Field field = fields[i];
            ExcelHeader header = field.getAnnotation(ExcelHeader.class);
            String headerName = header != null ? header.value() : field.getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerName);
        }

        // 填充数据（和之前一样，不变）
        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 1);
            zczzb item = dataList.get(rowIndex);

            for (int colIndex = 0; colIndex < fields.length; colIndex++) {
                Field field = fields[colIndex];
                field.setAccessible(true);
                Cell cell = dataRow.createCell(colIndex);

                Object value = field.get(item);
                if (value != null) {
                    // 处理不同类型的数据（保持不变）
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) value).doubleValue());
                    } else if (value instanceof Date) {
                        // 日期格式化（可选，更友好）
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cell.setCellValue(sdf.format((Date) value));
                    } else if (value instanceof Timestamp) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cell.setCellValue(sdf.format((Timestamp) value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }

        workbook.write(outputStream);
        workbook.close();
    }
}