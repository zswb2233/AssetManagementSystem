package com.zswb.assetinbook.service.serviceImpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zswb.assetinbook.dao.ZczzbDao;

import com.zswb.assetinbook.service.ZczzbService;
import com.zswb.model.entity.zczzb;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ZczzbServiceImpl extends ServiceImpl<ZczzbDao, zczzb> implements ZczzbService {

    @Autowired
    private ZczzbDao zczzbDao;


    public int importExcel(MultipartFile file) throws IOException {
        List<zczzb> list = new ArrayList<>();
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("请上传正确的Excel文件（.xls或.xlsx）");
        }

        try (InputStream is = file.getInputStream()) {
            // 根据文件后缀判断格式
            Workbook workbook;
            if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is); // 处理.xls格式
            } else {
                workbook = new XSSFWorkbook(is); // 处理.xlsx格式
            }

            // 后续解析逻辑不变...
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                zczzb entity = parseRowToEntity(row);
                if (entity != null) {
                    list.add(entity);
                }
            }
            return saveBatch(list);
        }
    }


    public int saveBatch(List<zczzb> list) {
        if (list.isEmpty()) return 0;

        // 使用MyBatis-Plus的批量插入方法
        int successCount = 0;
        for (zczzb entity : list) {
            // 检查设备编号是否已存在
            zczzb existEntity = zczzbDao.selectOne(
                    Wrappers.<zczzb>lambdaQuery()
                            .eq(zczzb::getEquipmentCode, entity.getEquipmentCode())
            );

            if (existEntity == null) {
                // 新增记录
                successCount += zczzbDao.insert(entity);
            } else {

                // 更新记录（根据业务需求选择是否更新）
                //暂时不设置是否差异化更新
                //successCount += zczzbDao.updateById(entity);
            }
        }

        return successCount;
    }

    /**
     * 将Excel行数据解析为实体对象
     */
    private zczzb parseRowToEntity(Row row) {
        try {
            zczzb entity = new zczzb();

            // 按实体类字段顺序映射Excel列
            entity.setEquipmentCode(getStringValue(row.getCell(0)));      // 设备编号
            entity.setEquipmentName(getStringValue(row.getCell(1)));     // 设备名称
            entity.setCategoryCode(getStringValue(row.getCell(2)));      // 分类号
            entity.setSpecification(getStringValue(row.getCell(3)));     // 规格
            entity.setBrandModel(getStringValue(row.getCell(4)));        // 品牌型号

            entity.setSingleAmount(getBigDecimalValue(row.getCell(5)));  // 金额（单价）

            entity.setMeasurementUnit(getStringValue(row.getCell(6)));   // 计量单位
            entity.setManufacturer(getStringValue(row.getCell(7)));      // 厂家

            entity.setPurchaseDate(getTimestampValue(row.getCell(8)));        // 购置日期

            entity.setStatus(getStringValue(row.getCell(9)));            // 现状
            entity.setFundSubject(getStringValue(row.getCell(10)));      // 经费科目

            entity.setUsingUnitCode(getStringValue(row.getCell(11)));    // 使用单位号
            entity.setUsingUnitName(getStringValue(row.getCell(12)));    // 使用单位名
            entity.setStorageLocationCode(getStringValue(row.getCell(13))); // 原存放地编号
            entity.setStorageLocationName(getStringValue(row.getCell(14))); // 原存放地名称

            entity.setBeforeCount(getBigDecimalValue(row.getCell(15)));     // 原数量
            entity.setAmount(getBigDecimalValue(row.getCell(16)));          // 原金额

            entity.setUserCode(getStringValue(row.getCell(17)));            // 使用人编号
            entity.setUser(getStringValue(row.getCell(18)));                // 使用人名称
            entity.setAccountant(getStringValue(row.getCell(19)));          // 记帐人

            entity.setAccountingDate(getTimestampValue(row.getCell(20)));        // 入账时间

            entity.setRemarks(getStringValue(row.getCell(21)));             // 备注
            entity.setHandler(getStringValue(row.getCell(22)));             // 经手人

            entity.setAuditStatus(getStringValue(row.getCell(23)));         // 审核状态

            entity.setBusinessNumber(getStringValue(row.getCell(24)));      // 业务单号
            entity.setInputPerson(getStringValue(row.getCell(25)));         // 设备最近一次使用人

            entity.setInputDatetime(getTimestampValue(row.getCell(26)));    // 设备最近一次输入时日期时间

//            entity.setReserveString1(getStringValue(row.getCell(27)));      // 字符备用1
//            entity.setReserveString2(getStringValue(row.getCell(28)));      // 字符备用2
//            entity.setReserveString3(getStringValue(row.getCell(29)));      // 字符备用3
//            entity.setReserveString4(getStringValue(row.getCell(30)));      // 字符备用4
//            entity.setReserveNumber1(getBigDecimalValue(row.getCell(31)));  // 数字备用1
//            entity.setReserveNumber2(getBigDecimalValue(row.getCell(32)));  // 数字备用2
//            entity.setReserveNumber3(getBigDecimalValue(row.getCell(33)));  // 数字备用3
//            entity.setReserveNumber4(getBigDecimalValue(row.getCell(34)));  // 数字备用4

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("解析第" + row.getRowNum() + "行失败: " + e.getMessage());
            return null;
        }
    }

    // 辅助方法：获取单元格的字符串值
    private String getStringValue(Cell cell) {
        /*
        STRING：字符串类型（如 "设备 A"）
        NUMERIC：数值类型（如 123.45）
        BOOLEAN：布尔类型（如 true/false）
        FORMULA：公式（如 "=SUM (A1:A10)"）
        BLANK：空单元格
        ERROR：错误值
         */
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    // 辅助方法：获取单元格的BigDecimal值
    private BigDecimal getBigDecimalValue(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue();
                    if (value != null && !value.trim().isEmpty()) {
                        return new BigDecimal(value.trim());
                    }
                    return null;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 辅助方法：获取单元格的Date值
    // 辅助方法：获取单元格的Date值
    private Date getDateValue(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }
                    return null;
                case STRING:
                    String dateStr = cell.getStringCellValue();
                    if (dateStr != null && !dateStr.trim().isEmpty()) {
                        // 支持多种日期时间格式
                        SimpleDateFormat[] formats = {
                                new SimpleDateFormat("yyyy-MM-dd"),
                                new SimpleDateFormat("yyyy/MM/dd"),
                                new SimpleDateFormat("yyyy年MM月dd日"),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss") // 新增支持Excel的日期时间格式
                        };

                        for (SimpleDateFormat format : formats) {
                            try {
                                return format.parse(dateStr);
                            } catch (ParseException ignored) {
                                // 尝试下一个格式
                            }
                        }
                        return null; // 所有格式都不匹配
                    }
                    return null;
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 辅助方法：获取单元格的Timestamp值
// 辅助方法：获取单元格的Timestamp值
    private Timestamp getTimestampValue(Cell cell) {
        Date date = getDateValue(cell);
        return date != null ? new Timestamp(date.getTime()) : null;
    }
}
