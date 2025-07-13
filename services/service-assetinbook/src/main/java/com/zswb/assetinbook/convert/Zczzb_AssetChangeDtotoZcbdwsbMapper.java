package com.zswb.assetinbook.convert;

import com.zswb.assetinbook.utils.AddressService;
import com.zswb.model.dto.AssetChangeDTO;
import com.zswb.model.entity.zcbdwsb;
import com.zswb.model.entity.zczzb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

// 标记为MapStruct映射器，componentModel="spring"表示生成Spring可注入的Bean
@Mapper(componentModel = "spring")
public interface Zczzb_AssetChangeDtotoZcbdwsbMapper {
    Zczzb_AssetChangeDtotoZcbdwsbMapper INSTANCE = Mappers.getMapper(Zczzb_AssetChangeDtotoZcbdwsbMapper.class);


    @Autowired
    AddressService addressService = new AddressService();

    zcbdwsb zczzbtoZcwsb(zczzb source);

    // 将AssetChangeDTO合并到zcbdwsb中
    @Mapping(target = "equipmentCode", ignore = true) // 避免覆盖已有值
    void updateZcbdwsbFromDTO(AssetChangeDTO dto, @MappingTarget zcbdwsb target);
}
