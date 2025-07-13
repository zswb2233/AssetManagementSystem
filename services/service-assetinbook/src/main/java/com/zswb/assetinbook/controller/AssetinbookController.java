package com.zswb.assetinbook.controller;

import com.zswb.assetinbook.service.ZcbdwsbService;
import com.zswb.assetinbook.service.ZczzbService;
import com.zswb.model.dto.AssetChangeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/assetinbbok")
public class AssetinbookController {
    // 注入zczzb的Service（用于查询原在账数据）
    @Autowired
    private ZczzbService zczzbService;

//    // 注入zcbdwsb的Service（用于保存未审变动数据）
//    @Autowired
//    private ZcbdwsbService zcbdwsbService;

    /**
     * 处理资产变动，将zczzb转化为zcbdwsb（未审状态）
     */
    //TODO 后端传变动类型：
    //校内调拨，使用人调整，资产减值，闲置调剂
//    @PostMapping("/changeApplication")
//    public Map<String, Object> handleAssetChange(@RequestBody AssetChangeDTO changeDTO) {
//    }

    //todo 前端传excel文件到后端并让后端处理后将数据插入数据库。
    @PostMapping("/upload/excel")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "上传失败，请选择文件";
            }

            // 调用Service处理Excel文件
            int count = zczzbService.importExcel(file);
            return "上传成功，共导入" + count + "条数据";
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }
    }
}
