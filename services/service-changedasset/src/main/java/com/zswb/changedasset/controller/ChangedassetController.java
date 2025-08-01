package com.zswb.changedasset.controller;

import com.alibaba.nacos.api.model.v2.ErrorCode;
import com.alibaba.nacos.api.model.v2.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage; // 新增：IPage 接口导入
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.text.DocumentException;
import com.zswb.model.dto.IndividualZcbdbTreeNode;
import com.zswb.model.dto.sbflbDTO;
import com.zswb.model.entity.zcbdb;
import com.zswb.changedasset.service.ChangedassetService;
// 新增：导入 models 模块的实体类
import com.zswb.model.entity.zcbdb2;
import com.zswb.model.output.zcbdbOutput;
import com.zswb.model.vo.zcbdbVO;
import com.zswb.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.zswb.util.PdfGenerator2.generateAssetTablePdf;

@Slf4j
@RestController
@RequestMapping("/api/changedasset")
public class ChangedassetController {

    @Autowired
    ChangedassetService changedassetService;

    //排序查询以及默认查询
    @GetMapping("/page")
    public Map<String, Object> getEquipmentPage(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) List<String> sortFields, // 排序字段列表
            @RequestParam(required = false) List<String> sortOrders,  // 排序方向列表
            @RequestParam(required = false) List<String> require  //查询条件
    ) {
        // 创建分页对象
        Page<zcbdb2> page = new Page<>(pageNum, pageSize);
        QueryWrapper<zcbdb2> queryWrapper = new QueryWrapper<>();

        return changedassetService.getPageWithQuery(page, queryWrapper, sortFields, sortOrders, require);
    }

    // 安全校验：排序字段白名单（防止SQL注入）
    //TODO 前端展示的数据可以双击进入详细页面

    //ToDo 明细表:报废明细表 校内调拨明细表 分户增减表 分类增减表 需要使用itext,已下载依赖
//    @RequestMapping("/printPdfdemo")
//    public Result printChangedassetdemo(
//            @RequestParam(name = "headerName", defaultValue = "资产变动表") String headerName,
//            @RequestParam(name = "formUnit", defaultValue = "中国大学") String formUnit,
//            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd")Date formDateFrom,
//            @RequestParam(name = "formDateTo", required = true)@DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
//            @RequestParam(required = false) List<String> sortFields,
//            @RequestParam(required = false) List<String> sortOrders,
//            @RequestParam(required = false) List<String> require,
//
//            // 新增参数：是否打印全部数据
//            @RequestParam(name = "printAll", defaultValue = "true") boolean printAll,
//            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
//            @RequestParam(name = "pageSize", defaultValue = "1000") Integer pageSize
//    ) {
//
//        // 1. 处理日期参数（默认当前日期）
//        log.info("进入了打印");
//        Date formDate = new Date();
//
//
//        // 2. 查询需要打印的数据
//        Page<zcbdb2> page = new Page<>(printAll ? 1 : pageNum, printAll ? Integer.MAX_VALUE : pageSize);
//        QueryWrapper<zcbdb2> queryWrapper = new QueryWrapper<>();
//
//        // 调用已有的条件查询方法获取数据
//        Map<String, Object> resultOutput = changedassetService.getPageWithQuerytoOut(page, queryWrapper, sortFields, sortOrders, require);
//        //TODO 将查询到的数据输入及其他打印内容到打印模组的函数中，
//        String path = "C:\\CodeProgram\\AssetManagementSystemCloud\\utils\\src\\main\\resources\\pdfDocument\\asset_report.pdf";//可以写在yml里作为配置以及后续配置中心。
//        try {
//            generateAssetTablePdf(path, headerName, formUnit, formDate,formDateFrom,formDateTo, resultOutput);
//            //生成PDF后，用字节流返还给前端。
//            return  new Result<>(200);
//        } catch (DocumentException e) {
//
//            throw new RuntimeException(e);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/printPdf")
    public ResponseEntity<byte[]> printChangedasset(
            @RequestParam(name = "headerName", defaultValue = "资产变动表") String headerName,
            @RequestParam(name = "formUnit", defaultValue = "中国大学") String formUnit,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(required = false) List<String> sortFields,
            @RequestParam(required = false) List<String> sortOrders,
            @RequestParam(required = false) List<String> require,
            @RequestParam(name = "printAll", defaultValue = "true") boolean printAll,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "1000") Integer pageSize
    ) throws IOException, DocumentException {

        // 处理日期参数
        Date formDate = new Date();

        // 查询需要打印的数据
        Page<zcbdb2> page = new Page<>(printAll ? 1 : pageNum, printAll ? Integer.MAX_VALUE : pageSize);
        QueryWrapper<zcbdb2> queryWrapper = new QueryWrapper<>();

        // 调用已有的条件查询方法获取数据
        Map<String, Object> resultOutput = changedassetService.getPageWithQuerytoOut(page, queryWrapper, sortFields, sortOrders, require);

        try {
            // 生成 PDF 字节流
            byte[] pdfBytes = PdfGenerator2.generateAssetTablePdf(headerName, formUnit, formDate, formDateFrom, formDateTo, resultOutput);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // 文件名使用当前日期+标题，避免中文乱码
            String fileName = new String((headerName + "_" +
                    new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf").getBytes("UTF-8"),
                    "ISO-8859-1");

            // 设置为 inline 支持预览，如需强制下载可改为 attachment
            headers.setContentDispositionFormData("inline", fileName);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("PDF生成或返回失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


        /**
     * 转换为输出模型（zcbdbOutput）
     */
    private zcbdbOutput convertToOutput(zcbdb2 entity) {
        zcbdbOutput output = new zcbdbOutput();
        output.setUsingUnitName(entity.getUsingUnitName());
        output.setEquipmentCode(entity.getEquipmentCode());
        output.setEquipmentName(entity.getEquipmentName());
        output.setSpecification(entity.getSpecification());
        output.setCategoryCode(entity.getCategoryCode());
        output.setStatus(entity.getStatus());
        output.setBeforeCount(entity.getBeforeCount());
        output.setChangedCount(entity.getChangedCount());
        output.setAmount(entity.getAmount());
        output.setChangedAmount(entity.getChangedAmount());
        output.setChangeDate(entity.getChangeDate());
        output.setChangeReason(entity.getChangeReason());
        // 可补充转入单位名称（需要从transferInUnitCode转换）
        return output;
    }

    // 分户增减变动统计表（返回Nacos自带的Result格式）
    @GetMapping("/statisticalTable/individualHousehold/inAndDecrease/inquire")
    public Result<IndividualZcbdbTreeNode> getIndividualHouseholdinAndDecrease(
            @RequestParam(name = "tableType",required = false) String tableType,
            @RequestParam(name = "unitLevel",required = false) Integer unitLevel,
            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet
    ) {
        try {
            // 1. 参数默认值处理
            tableType = tableType == null ? "分户增减变动统计表" : tableType;
            unitLevel = unitLevel == null ? 1 : unitLevel;
            status = status == null ? -1 : status;

            // 2. 调用服务层获取树形结构数据（已完成子节点汇总）
            IndividualZcbdbTreeNode root = changedassetService.showIndividualHouseholdinAndDecrease(
                    tableType, unitLevel, status, formDateFrom, formDateTo, accountSet
            );

            // 3. 清除父节点引用（避免JSON序列化循环引用）
            if (root != null) {
                root.setParent(null);
            }

            // 4. 使用Nacos的Result.success()封装数据（code=0表示成功）
            return Result.success(root);

        } catch (Exception e) {
            // 5. 异常时使用Nacos的Result.failure()返回错误信息
            // 可根据实际场景选择ErrorCode（如SERVER_ERROR、PARAM_ERROR等）
            return Result.failure(ErrorCode.SERVER_ERROR, new IndividualZcbdbTreeNode(null, e.getMessage()));
        }
    }

    // 分户增减变动统计表打印PDF（返回Nacos自带的Result格式）
    @GetMapping("/statisticalTable/individualHousehold/inAndDecrease/inquirePdf")
    public ResponseEntity<byte[]> getIndividualHouseholdinAndDecreasePDF(
            @RequestParam(name = "tableType",required = false) String tableType,
            @RequestParam(name = "unitLevel",required = false) Integer unitLevel,
            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet,
            //下面是打印的项
            @RequestParam(name="tableName" ,defaultValue="中国大学分户增减变动统计表") String tableName,//表头名
            @RequestParam(name="tableUnit",defaultValue = "中国大学") String tableUnit,//制表单位
            @RequestParam(name = "tableDate", required = true)@DateTimeFormat(pattern = "yyyy-MM-dd") Date tableDate,//制表时间
            @RequestParam(name="jldw",defaultValue = "元") String jldw //计量单位，元与万元
    ) {
        try {
            // 1. 调用服务层获取树形结构数据（已完成子节点汇总）
            IndividualZcbdbTreeNode root = changedassetService.showIndividualHouseholdinAndDecrease(
                    tableType, unitLevel, status, formDateFrom, formDateTo, accountSet
            );
            // 2. 生成PDF
            PdfGenerationService pdfService = new PdfGenerationService();
            byte[] pdfBytes = pdfService.generateIndividualHouseholdPdf(unitLevel,root, tableName,tableUnit,tableDate,formDateFrom,formDateTo,jldw);

            // 3. 构建响应
            HttpHeaders headers = new HttpHeaders();
            headers
                    .setContentType(MediaType.APPLICATION_PDF);
            String fileName = URLEncoder.encode("分户增减变动统计表.pdf", StandardCharsets.UTF_8.name());
            headers
                    .setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(("生成PDF失败：" + e.getMessage()).getBytes());
        }

    }
    @GetMapping("/statisticalTable/individualHousehold/inAndDecrease/inquireExcel")
    public ResponseEntity<byte[]> getIndividualHouseholdinAndDecreaseExcel(
            // 参数与PDF接口相同
            @RequestParam(name = "tableType",required = false) String tableType,
            @RequestParam(name = "unitLevel",required = true) Integer unitLevel,
            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet,
            //下面是打印的项
            @RequestParam(name="tableName" ,defaultValue="中国大学分户增减变动统计表") String tableName,//表头名
            @RequestParam(name="tableUnit",defaultValue = "中国大学") String tableUnit,//制表单位
            @RequestParam(name = "tableDate", required = true)@DateTimeFormat(pattern = "yyyy-MM-dd") Date tableDate,//制表时间
            @RequestParam(name="jldw",defaultValue = "元") String jldw //计量单位，元与万元
    ) {
        try {
            // 获取树形结构数据
            IndividualZcbdbTreeNode root = changedassetService.showIndividualHouseholdinAndDecrease(
                    tableType, unitLevel, status, formDateFrom, formDateTo, accountSet
            );

            // 验证数据
            if (root == null || root.getChildren() == null || root.getChildren().isEmpty()) {
                throw new IllegalArgumentException("数据为空，无法生成Excel");
            }

            // 生成Excel
            ExcelGenerationHouseholdService excelService = new ExcelGenerationHouseholdService();
            byte[] excelBytes = excelService.generateIndividualHouseholdExcel(
                    unitLevel, root, tableName, tableUnit, tableDate, formDateFrom, formDateTo, jldw);

            // 构建响应
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 修正内容类型
            String fileName = URLEncoder.encode("分户增减变动统计表.xlsx", StandardCharsets.UTF_8.name()); // 修正扩展名
            headers.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            log.error("生成Excel失败", e); // 记录详细日志
            return ResponseEntity.status(500)
                    .body(("生成Excel失败：" + e.getMessage()).getBytes());
        }
    }

    // 分类增减变动统计表（返回Nacos自带的Result格式）Classification
    @GetMapping("/statisticalTable/individualClassification/inAndDecrease/inquire")
    public Result<List<sbflbDTO>> getIndividualClassificationinAndDecrease(
            @RequestParam(name = "tableType",required = false) String tableType,

            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet
    ) {
        try {
            // 1. 参数默认值处理
            tableType = tableType == null ? "分类增减变动统计表" : tableType;

            status = status == null ? -1 : status;

            // 2. 调用服务层获取List<sbflbDTO>
            List<sbflbDTO> sbflbDTOList= changedassetService.showIndividualClassificationinAndDecrease(
                    tableType,  status, formDateFrom, formDateTo, accountSet
            );



            // 4. 使用Nacos的Result.success()封装数据（code=0表示成功）
            return Result.success(sbflbDTOList);

        } catch (Exception e) {
            // 5. 异常时使用Nacos的Result.failure()返回错误信息
            // 可根据实际场景选择ErrorCode（如SERVER_ERROR、PARAM_ERROR等）
            return Result.failure(ErrorCode.SERVER_ERROR, new ArrayList<>());
        }
    }
    // 分类增减变动统计表pdf
    @GetMapping("/statisticalTable/individualClassification/inAndDecrease/inquirePdf")
    public ResponseEntity<byte[]> getIndividualClassificationinAndDecreasePDF(
            @RequestParam(name = "tableType",required = false) String tableType,

            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet,
            //下面是打印的项
            @RequestParam(name="tableName" ,defaultValue="中国大学分类增减变动统计表") String tableName,//表头名
            @RequestParam(name="tableUnit",defaultValue = "中国大学") String tableUnit,//制表单位
            @RequestParam(name = "tableDate", required = true)@DateTimeFormat(pattern = "yyyy-MM-dd") Date tableDate,//制表时间
            @RequestParam(name="jldw",defaultValue = "元") String jldw //计量单位，元与万元
    ) {
        try {
            // 1. 调用服务层获取树形结构数据（已完成子节点汇总）
            List<sbflbDTO> sbflbDTOList= changedassetService.showIndividualClassificationinAndDecrease(
                    tableType,  status, formDateFrom, formDateTo, accountSet
            );

            // 2. 生成PDF
            PdfGenerationService pdfService = new PdfGenerationService();
            byte[] pdfBytes = pdfService.generateIndividualCategoryPdf(sbflbDTOList,tableName,tableUnit,tableDate,formDateFrom,formDateTo,jldw);

            // 3. 构建响应
            HttpHeaders headers = new HttpHeaders();
            headers
                    .setContentType(MediaType.APPLICATION_PDF);
            String fileName = URLEncoder.encode("分类增减变动统计表.pdf", StandardCharsets.UTF_8.name());
            headers
                    .setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(("生成PDF失败：" + e.getMessage()).getBytes());
        }
    }

    //=================分类增减变动excel===================
    // 分类增减变动统计表pdf
    @GetMapping("/statisticalTable/individualClassification/inAndDecrease/inquireExcel")
    public ResponseEntity<byte[]> getIndividualClassificationinAndDecreaseExcel(
            @RequestParam(name = "tableType",required = false) String tableType,

            @RequestParam(name = "Financial_accounting_status", defaultValue = "-1") Integer status,
            @RequestParam(name = "formDateFrom", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateFrom,
            @RequestParam(name = "formDateTo", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDateTo,
            @RequestParam(name = "accountSet", defaultValue = "inCampus") String accountSet,
            //下面是打印的项
            @RequestParam(name="tableName" ,defaultValue="中国大学分类增减变动统计表") String tableName,//表头名
            @RequestParam(name="tableUnit",defaultValue = "中国大学") String tableUnit,//制表单位
            @RequestParam(name = "tableDate", required = true)@DateTimeFormat(pattern = "yyyy-MM-dd") Date tableDate,//制表时间
            @RequestParam(name="jldw",defaultValue = "元") String jldw //计量单位，元与万元
    ) {
        try {
            // 1. 调用服务层获取树形结构数据（已完成子节点汇总）
            List<sbflbDTO> sbflbDTOList= changedassetService.showIndividualClassificationinAndDecrease(
                    tableType,  status, formDateFrom, formDateTo, accountSet
            );

            // 2. 生成Excel
            ExcelGenerationService excelService = new ExcelGenerationService() {
                @Override
                public byte[] generateIndividualCategoryExcel(List<sbflbDTO> sbflbDTOList, String tableName, String tableUnit, Date tableDate, Date formDateFrom, Date formDateTo, String jldw) throws IOException {
                    return ExcelGenerationService.super.generateIndividualCategoryExcel(sbflbDTOList, tableName, tableUnit, tableDate, formDateFrom, formDateTo, jldw);
                }
            };
            byte[] excelBytes = excelService.generateIndividualCategoryExcel(sbflbDTOList,tableName,tableUnit,tableDate,formDateFrom,formDateTo,jldw);

            // 3. 构建响应
            HttpHeaders headers = new HttpHeaders();
            headers
                    .setContentType(MediaType.APPLICATION_PDF);
            String fileName = URLEncoder.encode("分类增减变动统计表.xls", StandardCharsets.UTF_8.name());
            headers
                    .setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(("生成Excel失败：" + e.getMessage()).getBytes());
        }
    }
}
