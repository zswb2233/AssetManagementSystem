package com.zswb.changedasset.convert;

import com.zswb.changedasset.utils.AddressService;
import com.zswb.model.entity.zcbdb2;
import com.zswb.model.output.zcbdbOutput;
import com.zswb.model.vo.zcbdbVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

// 标记为MapStruct映射器，componentModel="spring"表示生成Spring可注入的Bean
@Mapper(componentModel = "spring")
public interface Zcbdb2OutputMapper {

    @Autowired
    AddressService addressService = new AddressService();

    // 单例实例（如果不使用Spring则用这个）
    Zcbdb2VoMapper INSTANCE = Mappers.getMapper(Zcbdb2VoMapper.class);

    /**
     * 将zcbdb2转换为zcbdbVO
     * 字段名一致的会自动映射，不一致的需手动指定
     */

   @Mapping(target = "transferInUnitName", expression = "java(addressService.getUnitNameByCode(zcbdb2.getTransferInUnitCode()))")
    @Mapping(target = "status",expression = "java(addressService.getStatusNameByCode(zcbdb2.getStatus()))")

    zcbdbOutput toOutput(zcbdb2 zcbdb2);
}