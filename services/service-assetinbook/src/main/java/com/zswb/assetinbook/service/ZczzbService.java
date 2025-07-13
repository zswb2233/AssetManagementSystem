package com.zswb.assetinbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zswb.model.entity.zczzb;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ZczzbService extends IService<zczzb> {
    /**
     * 导入Excel文件并保存数据
     * @param file Excel文件
     * @return 导入成功的记录数
     * @throws IOException 文件处理异常
     */
    int importExcel(MultipartFile file) throws IOException;
}
