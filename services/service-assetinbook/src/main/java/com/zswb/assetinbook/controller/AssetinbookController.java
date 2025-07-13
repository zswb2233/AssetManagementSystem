package com.zswb.assetinbook.controller;

import com.alibaba.nacos.api.model.v2.Result;
import com.zswb.assetinbook.service.ZcbdwsbService;
import com.zswb.assetinbook.service.ZczzbService;
import com.zswb.assetinbook.utils.ExcelUtils;
import com.zswb.model.dto.AssetChangeDTO;
import com.zswb.model.entity.zczzb;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/assetinbbok")
public class AssetinbookController {
    // 注入zczzb的Service（用于查询原在账数据）
    @Autowired
    private ZczzbService zczzbService;

//    // 注入zcbdwsb的Service（用于保存未审变动数据）
    @Autowired
    private ZcbdwsbService zcbdwsbService;

    /**
     * 处理资产变动，将zczzb转化为zcbdwsb（未审状态）
     */
    //TODO 后端传变动类型：
    //校内调拨，使用人调整，资产减值，闲置调剂

    @PostMapping("/upload/zcbdApplication")
    public Result<String> applicateAssetChange(@RequestBody List<AssetChangeDTO> changeDTOlist) {
        if(changeDTOlist.isEmpty()) return  Result.failure("请传入变动数据");
        //TODO 回退，当我某个没插入成功怎么办
        try {
            for(AssetChangeDTO changeDTO:changeDTOlist){
                zcbdwsbService
                        .applicateAssetChange(changeDTO);
            }
            return Result.success(); // 使用 Nacos 的 success() 方法
        } catch (Exception e) {
            return Result.failure("资产变更申请失败：" + e.getMessage()); // 使用 Nacos 的 failure() 方法
        }
    }


    @PostMapping("/upload/excel")
    public Result<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.failure("上传失败，请选择文件");
            }

            // 调用Service处理Excel文件
            int count = zczzbService.importExcel(file);
            String message = "上传成功，共导入" + count + "条数据";

            // 返回成功消息和导入数量
            return Result.success(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("上传失败：" + e.getMessage());
        }
    }
    //TODO 前端传回equipmentCode的列表，后端将对应数据写入excel传回前端提供下载。





    // 下载Excel模板
    @GetMapping("/download/excelmodel")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("资产信息模板.xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        ExcelUtils.generateTemplate(response.getOutputStream());
    }

    // 下载包含数据的Excel文件
    @GetMapping("/download/exceldata")
    public void downloadData(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("资产信息数据.xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 从数据库或其他数据源获取数据
        List<zczzb> dataList = zczzbService.getAllData();

        ExcelUtils.generateExcelWithData(dataList, response.getOutputStream());
    }
}
