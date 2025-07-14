package com.zswb.changedasset.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddressService {

    // 实际项目中应从数据库或缓存加载数据
    private static final Map<String, String> UNIT_CODE_NAME_MAP = new HashMap<>();
    private static final Map<String, String> LOCATION_CODE_NAME_MAP = new HashMap<>();
    private static final Map<String,String>  STATUS_STATUS_NAME_MAP = new HashMap<>();
    private static final Map<String,String>  JFKM_JFKM_NAME_MAP = new HashMap<>();
    private static final Map<String,String>  CATEGORY_CODE = new HashMap<>();

    static {
        // 示例数据（实际应从数据库加载）
        // 也可以直接在这里定义，数据库也存一份吧。
        //需要按照实际情况对应。
        //单位号
        UNIT_CODE_NAME_MAP.put("001", "计算机学院");
        UNIT_CODE_NAME_MAP.put("002", "信息工程系");
        UNIT_CODE_NAME_MAP.put("003", "机械工程学院");
        //存放地点
        LOCATION_CODE_NAME_MAP.put("A101", "主教学楼101室");
        LOCATION_CODE_NAME_MAP.put("B202", "实验楼202室");
        LOCATION_CODE_NAME_MAP.put("C303", "图书馆303室");
        //现状
        STATUS_STATUS_NAME_MAP.put("1", "在用");
        STATUS_STATUS_NAME_MAP.put("2", "闲置");
        STATUS_STATUS_NAME_MAP.put("3", "待修");
        STATUS_STATUS_NAME_MAP.put("4", "待报废");
        STATUS_STATUS_NAME_MAP.put("5", "丢失");
        STATUS_STATUS_NAME_MAP.put("6", "报废");
        STATUS_STATUS_NAME_MAP.put("7", "出售");
        STATUS_STATUS_NAME_MAP.put("9", "其它");
        STATUS_STATUS_NAME_MAP.put("A", "调入");
        STATUS_STATUS_NAME_MAP.put("B", "转入");
        STATUS_STATUS_NAME_MAP.put("C", "转出");
        STATUS_STATUS_NAME_MAP.put("D", "注销");
        STATUS_STATUS_NAME_MAP.put("E", "盘亏");
        STATUS_STATUS_NAME_MAP.put("F", "调剂");
        STATUS_STATUS_NAME_MAP.put("G", "对外捐赠");
        //经费科目
        JFKM_JFKM_NAME_MAP.put("1", "教学");
        JFKM_JFKM_NAME_MAP.put("2", "科研");
        JFKM_JFKM_NAME_MAP.put("3", "基建");
        JFKM_JFKM_NAME_MAP.put("4", "自筹经费");
        JFKM_JFKM_NAME_MAP.put("5", "世界银行贷款");
        JFKM_JFKM_NAME_MAP.put("6", "捐赠");
        JFKM_JFKM_NAME_MAP.put("9", "其它");
        JFKM_JFKM_NAME_MAP.put("A", "研究生");
        JFKM_JFKM_NAME_MAP.put("B", "贷款配套费");
        JFKM_JFKM_NAME_MAP.put("C", "行政事业费");
        JFKM_JFKM_NAME_MAP.put("D", "211经费");
        JFKM_JFKM_NAME_MAP.put("E", "十五投资");
        JFKM_JFKM_NAME_MAP.put("F", "985经费");
        //设备大类分类
        CATEGORY_CODE.put("11","计算机");

    }

    // 根据单位编号查询单位名称
    public String getUnitNameByCode(String unitCode) {
        return UNIT_CODE_NAME_MAP.getOrDefault(unitCode, unitCode);
    }

    // 根据存放地编号查询存放地名称
    public String getLocationNameByCode(String locationCode) {
        return LOCATION_CODE_NAME_MAP.getOrDefault(locationCode, locationCode);
    }
    //根据现状code -> 现状名称
    public String getStatusNameByCode(String statusCode){
        return STATUS_STATUS_NAME_MAP.getOrDefault(statusCode,statusCode);
    }
    //根据经费科目code -> 现状名称
    public String getJfkmNameNameByCode(String jfkmCode){
        return JFKM_JFKM_NAME_MAP.getOrDefault(jfkmCode,jfkmCode);
    }

}