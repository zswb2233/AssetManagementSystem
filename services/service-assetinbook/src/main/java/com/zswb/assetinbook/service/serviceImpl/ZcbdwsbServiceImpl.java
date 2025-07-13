package com.zswb.assetinbook.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zswb.assetinbook.convert.Zczzb_AssetChangeDtotoZcbdwsbMapper;
import com.zswb.assetinbook.dao.ZcbdwsbDao;
import com.zswb.assetinbook.dao.ZczzbDao;
import com.zswb.assetinbook.service.ZcbdwsbService;
import com.zswb.model.dto.AssetChangeDTO;
import com.zswb.model.entity.zcbdwsb;
import com.zswb.model.entity.zczzb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZcbdwsbServiceImpl extends ServiceImpl<ZcbdwsbDao, zcbdwsb> implements ZcbdwsbService {
    @Autowired
    private ZczzbDao zczzbDao;

    @Autowired
    private ZcbdwsbDao zcbdwsbDao;


    @Override
    public void applicateAssetChange(AssetChangeDTO changeDTO) {
        //传入AssetChangeDTO;
        //获得里面的equipmentCode
        //用equipmentCode获得数据库里该对象，差异化更改。
        zczzb sourceEntity=zczzbDao.selectById(changeDTO.getEquipmentCode());
        //将资产在账表与AssetChangeDTO里的数据写入资产变动未审表
        zcbdwsb zcbdwsb = getZcbdwsb(changeDTO, sourceEntity);

        //将未审表写入数据库
        zcbdwsbDao.insert(zcbdwsb);

    }
    private zcbdwsb getZcbdwsb(AssetChangeDTO changeDTO,zczzb sourceEntity){
        zcbdwsb twsb= Zczzb_AssetChangeDtotoZcbdwsbMapper.INSTANCE.zczzbtoZcwsb(sourceEntity);

        Zczzb_AssetChangeDtotoZcbdwsbMapper.INSTANCE.updateZcbdwsbFromDTO(changeDTO,twsb);
        twsb.setChangedReview("0");//默认设置成“未审”;
        return twsb;

    }
}
