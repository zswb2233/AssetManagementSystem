package com.zswb.assetinbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zswb.model.dto.AssetChangeDTO;
import com.zswb.model.entity.zcbdwsb;

public interface ZcbdwsbService extends IService<zcbdwsb> {
    void applicateAssetChange(AssetChangeDTO changeDTO);
}
