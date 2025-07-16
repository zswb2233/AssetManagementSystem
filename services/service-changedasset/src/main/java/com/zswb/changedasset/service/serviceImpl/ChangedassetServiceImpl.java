package com.zswb.changedasset.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zswb.changedasset.convert.Zcbdb2OutputMapper;
import com.zswb.changedasset.dao.ChangedassetDao;
import com.zswb.changedasset.convert.Zcbdb2VoMapper;
import com.zswb.changedasset.dao.DwbDao;
import com.zswb.changedasset.dao.SbflbDao;
import com.zswb.changedasset.service.ChangedassetService;
import com.zswb.model.dto.IndividualZcbdbTreeNode;
import com.zswb.model.dto.sbflbDTO;
import com.zswb.model.entity.dwb;
import com.zswb.model.entity.sbflb2;
import com.zswb.model.entity.zcbdb2;
import com.zswb.model.output.zcbdbOutput;
import com.zswb.model.vo.zcbdbVO;
import com.zswb.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class ChangedassetServiceImpl extends ServiceImpl<ChangedassetDao, zcbdb2> implements ChangedassetService {
    @Autowired
    private ChangedassetDao changedassetDao;

    @Autowired
    private Zcbdb2VoMapper zcbdb2VoMapper;

    @Autowired
    private Zcbdb2OutputMapper zcbdb2OutputMapper;

    @Autowired
    private DwbDao dwbDao;

    @Autowired
    private SbflbDao sbflbDao;

    @Override
    public Map<String, Object> getPageWithQuery(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper , List<String> sortFields, List<String> sortOrders,List<String> require ) {
        // 动态添加排序条件（安全过滤）
        if (sortFields != null && !sortFields.isEmpty()) {
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String order = i < sortOrders.size() ? sortOrders.get(i) : "asc"; // 默认升序

                // 安全校验：仅允许白名单中的字段排序（防止SQL注入）
                if (isValidSortField(field)) {
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), field);
                } else {
                    log.warn("非法排序字段: {}", field);
                }
            }
        }
        Map<String, Object> queryParams  = new HashMap<>();
        queryParams = QueryUtils.buildQueryParams(require);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 根据键的后缀确定操作符
            if (key.endsWith("_gt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .gt(field, value);
            } else if (key.endsWith("_gte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .ge(field, value);
            } else if (key.endsWith("_lt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .lt(field, value);
            } else if (key.endsWith("_lte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .le(field, value);
            } else {
                // 默认使用等于操作符

                queryWrapper
                        .eq(key, value);
            }
        }


        // 1. 执行分页查询，获取zcbdb2类型的分页数据
        IPage<zcbdb2> zcbdb2Page = this.page(page, queryWrapper);

        // 2. 将IPage<zcbdb2>转换为IPage<zcbdbVO>
        IPage<zcbdbVO> voPage = zcbdb2Page.convert(zcbdb2VoMapper::toVo);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", voPage.getTotal());
        result.put("pages", voPage.getPages());
        result.put("current", voPage.getCurrent());
        result.put("size", voPage.getSize());
        result.put("records", voPage.getRecords());

        return result;
    }

    @Override
    public Map<String, Object> getPageWithQuerytoOut(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper, List<String> sortFields, List<String> sortOrders, List<String> require) {
        // 动态添加排序条件（安全过滤）
        if (sortFields != null && !sortFields.isEmpty()) {
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String order = i < sortOrders.size() ? sortOrders.get(i) : "asc"; // 默认升序

                // 安全校验：仅允许白名单中的字段排序（防止SQL注入）
                if (isValidSortField(field)) {
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), field);
                } else {
                    log.warn("非法排序字段: {}", field);
                }
            }
        }
        Map<String, Object> queryParams  = new HashMap<>();
        queryParams = QueryUtils.buildQueryParams(require);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 根据键的后缀确定操作符
            if (key.endsWith("_gt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .gt(field, value);
            } else if (key.endsWith("_gte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .ge(field, value);
            } else if (key.endsWith("_lt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .lt(field, value);
            } else if (key.endsWith("_lte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .le(field, value);
            } else {
                // 默认使用等于操作符
                queryWrapper
                        .eq(key, value);
            }
        }


        // 1. 执行分页查询，获取zcbdb2类型的分页数据
        IPage<zcbdb2> zcbdb2Page = this.page(page, queryWrapper);

        // 2. 将IPage<zcbdb2>转换为IPage<zcbdbOutput>
        IPage<zcbdbOutput> voPage = zcbdb2Page.convert(zcbdb2OutputMapper::toOutput);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", voPage.getTotal());
        result.put("pages", voPage.getPages());
        result.put("current", voPage.getCurrent());
        result.put("size", voPage.getSize());
        result.put("records", voPage.getRecords());

        return result;
    }

    @Override
    public IndividualZcbdbTreeNode showIndividualHouseholdinAndDecrease(String tableType, Integer unitLevel, Integer status, Date formDateFrom, Date formDateTo, String accountSet) {
        //分户增减变动统计表数据返回



        //2.找到在formDateFrom与formDateTo之间的数据。需要在在变动账表里找.
        //期初数=期末数−本期增加数+本期减少数
        QueryWrapper<zcbdb2> queryWrapper=new QueryWrapper<>();
        // 按时间范围查询：变动日期在 formDateFrom 和 formDateTo 之间
        // 按时间范围查询：变动日期 >= formDateFrom 且 <= formDateTo
        queryWrapper.ge("change_date", formDateFrom);  // 大于等于起始日期
        queryWrapper.le("change_date", formDateTo);    // 小于等于结束日期

        // 可选：根据状态选择不同业务，但是现在仅仅一个
//        if (status != null && status != -1) {
//
//        }
//
//        // 可选：根据账套选择不同业务，但是现在仅仅一个
//        if (accountSet != null && !accountSet.isEmpty()) {
//
//        }

        // 3. 执行查询
        List<zcbdb2> assetChanges = changedassetDao.selectList(queryWrapper);

        // 4. 根据数据从最小往大求，*开始。
        //
        return buildTreeStructure(assetChanges);

    }

    @Override
    public List<sbflbDTO> showIndividualClassificationinAndDecrease(String tableType, Integer status, Date formDateFrom, Date formDateTo, String accountSet) {
        List<sbflb2> templist=sbflbDao.selectList(null);
        List<sbflbDTO> sbflbDTOList=new ArrayList<>();
        for(int i=0;i<templist.size();i++){
            sbflbDTOList.add(new sbflbDTO(templist.get(i).getFldm(),templist.get(i).getFlmc()));
        }
        //2.找到在formDateFrom与formDateTo之间的数据。需要在在变动账表里找.
        //期初数=期末数−本期增加数+本期减少数
        QueryWrapper<zcbdb2> queryWrapper=new QueryWrapper<>();
        // 按时间范围查询：变动日期在 formDateFrom 和 formDateTo 之间
        // 按时间范围查询：变动日期 >= formDateFrom 且 <= formDateTo
        queryWrapper.ge("change_date", formDateFrom);  // 大于等于起始日期
        queryWrapper.le("change_date", formDateTo);    // 小于等于结束日期

        // 可选：根据状态选择不同业务，但是现在仅仅一个
//        if (status != null && status != -1) {
//
//        }
//
//        // 可选：根据账套选择不同业务，但是现在仅仅一个
//        if (accountSet != null && !accountSet.isEmpty()) {
//
//        }

        // 3. 执行查询
        List<zcbdb2> assetChanges = changedassetDao.selectList(queryWrapper);
        buildSbflbDTOList(sbflbDTOList,assetChanges);
        return sbflbDTOList;
    }

    private void buildSbflbDTOList(List<sbflbDTO> sbflbDTOList,List<zcbdb2> assetChanges) {
        //算所有子节点的值
        for(zcbdb2 temp:assetChanges){
            //先创建完子类，然后往外递归。
            //获得状态，不同状态做不同处理
            String status=temp.getStatus();
            softAllClafic(status,temp,sbflbDTOList);
        }
        for (sbflbDTO t:sbflbDTOList){
            t.calculateSummaryValues();
        }
    }

    private void softAllClafic(String status, zcbdb2 temp, List<sbflbDTO> sbflbDTOList) {
        //先获得是哪个分类的。
        String categoryCode = temp.getCategoryCode();
        //前两位就是分类:
        int numCategoryCodeIndex = Integer.parseInt(categoryCode.substring(0,2))-1;
        //此时对sbflbDTOList中的numCategoryCodeIndex操作即是正确的操作位置。
        //初期给加上
        sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(0,temp.getAmount());
        sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(0,temp.getBeforeCount());
        // 根据不同状态处理资产变动
        switch (status) {
            case "1": // 在用
                // 通常在用状态不涉及数量/金额变动，可能仅更新资产状态
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(1,temp.getAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(1,temp.getBeforeCount());
                break;

            case "2": // 闲置
                // 闲置状态不涉及数量/金额变动，可能仅更新资产状态

                break;

            case "3": // 待修
                // 待修状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "4": // 待报废
                // 待报废状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "5": // 丢失
                // 丢失通常作为资产减少处理
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "6": // 报废
                // 报废处理，更新减少数据
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(4,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(4,temp.getChangedCount());
                break;

            case "7": // 出售
                // 出售处理，更新减少数据,不懂这个业务
//                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
//                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "9": // 其它
                // 其它变动，根据实际情况处理
                //这里只写了减少的
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "A": // 调入
                // 调入处理，更新增加数据

                break;

            case "B": // 转入
                // 转入处理，更新增加数据

                break;

            case "C": // 转出
                // 转出处理，更新减少数据
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "D": // 注销
                // 注销处理，更新减少数据
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "E": // 盘亏
                // 盘亏处理，更新减少数据
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            case "F": // 调剂
                // 调剂处理，可能需要特殊处理，这里简化为增加
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(2,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(2,temp.getChangedCount());
                break;

            case "G": // 对外捐赠
                // 对外捐赠处理，更新减少数据
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeAmountWithId(5,temp.getChangedAmount());
                sbflbDTOList.get(numCategoryCodeIndex).addNowDeCountWithId(5,temp.getChangedCount());
                break;

            default:
                // 未知状态，记录日志或其他处理
                break;

        }

    }

    private IndividualZcbdbTreeNode buildTreeStructure(List<zcbdb2> assetChanges ){
        //建立单位表树状关系
        IndividualZcbdbTreeNode root=buildInitTreeStructure();

        //算所有子节点的值
        for(zcbdb2 temp:assetChanges){
            //先创建完子类，然后往外递归。
            //获得状态，不同状态做不同处理
            String status=temp.getStatus();
            softDifStatus(status,temp,root);
        }
        //倒数第二层处理：现在List<zcbdb2> assetChanges内的所有数据都储存在root里面了
        //递归处理。
        // 3. 从根节点开始递归汇总所有子节点数据到父节点
        root.sumChildrenData();
        //现在，将父节点置空，避免反复嵌套
        removeParent(root);
        return root;

    }

    private void removeParent(IndividualZcbdbTreeNode root) {
        for(IndividualZcbdbTreeNode temp: root.getChildren()){
            temp.setParent(null);
            removeParent(temp);
        }
    }

    private void softDifStatus(String status, zcbdb2 temp, IndividualZcbdbTreeNode root) {
        // 获取单位编码，用于查找树节点
        String unitCode = temp.getUsingUnitCode();
        if (unitCode == null || unitCode.isEmpty()) {
            return; // 单位编码为空，无法处理
        }

        // 查找对应的树节点
        IndividualZcbdbTreeNode node = findNodeByUnitCode(root, unitCode);
        if (node == null) {
            return; // 未找到对应节点
        }



        // 根据不同状态处理资产变动
        switch (status) {
            case "1": // 在用
                // 通常在用状态不涉及数量/金额变动，可能仅更新资产状态

                break;

            case "2": // 闲置
                // 闲置状态不涉及数量/金额变动，可能仅更新资产状态

                break;

            case "3": // 待修
                // 待修状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "4": // 待报废
                // 待报废状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "5": // 丢失
                // 丢失通常作为资产减少处理
                updateIncreaseData(node,temp,7);
                break;

            case "6": // 报废
                // 报废处理，更新减少数据
                updateIncreaseData(node,temp,5);
                break;

            case "7": // 出售
                // 出售处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "9": // 其它
                // 其它变动，根据实际情况处理
                //这里只写了减少的
                updateIncreaseData(node,temp,7);
                break;

            case "A": // 调入
                // 调入处理，更新增加数据
                updateIncreaseData(node,temp,3);
                break;

            case "B": // 转入
                // 转入处理，更新增加数据
                updateIncreaseData(node,temp,2);
                break;

            case "C": // 转出
                // 转出处理，更新减少数据
                updateIncreaseData(node,temp,6);
                break;

            case "D": // 注销
                // 注销处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "E": // 盘亏
                // 盘亏处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "F": // 调剂
                // 调剂处理，可能需要特殊处理，这里简化为增加
                updateIncreaseData(node,temp,3);
                break;

            case "G": // 对外捐赠
                // 对外捐赠处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            default:
                // 未知状态，记录日志或其他处理
                break;
        }
    }
    // 在树中查找节点的方法
    private IndividualZcbdbTreeNode findNodeByUnitCode(IndividualZcbdbTreeNode root, String unitCode) {
        if (root == null || unitCode == null || unitCode.isEmpty()) {
            return null;
        }

        // 如果当前节点匹配，直接返回
        if (root.getUnitCode().equals(unitCode)) {
            return root;
        }

        // 递归查找子节点
        if (root.getChildren() != null && !root.getChildren().isEmpty()) {
            for (IndividualZcbdbTreeNode child : root.getChildren()) {
                IndividualZcbdbTreeNode found = findNodeByUnitCode(child, unitCode);
                if (found != null) {
                    return found;
                }
            }
        }

        return null; // 未找到
    }
    // 更新增加数据的辅助方法
    public void updateIncreaseData(IndividualZcbdbTreeNode node, zcbdb2 temp, int index) {
        // 检查参数有效性
        if (node == null || temp == null) {
            throw new IllegalArgumentException("无效参数");
        }

        BigDecimal beforeCount = temp.getBeforeCount();
        BigDecimal changedCount = temp.getChangedCount();
        BigDecimal beforeAmount = temp.getAmount();
        BigDecimal changedAmount = temp.getChangedAmount();

        // 0. 设置初期值
        node.setNowDeCountWithId(0, beforeCount);
        node.setNowDeAmountWithId(0, beforeAmount);

        // index. 设置变动值(购置、校内转入、其他)
        node.setNowDeCountWithId(index, changedCount);
        node.setNowDeAmountWithId(index, changedAmount);

        // 计算并设置合计值
        node.calculateSummaryValues();
    }

    /**
     * 计算并设置合计值和期末值
     */
    private void calculateAndSetSummaryValues(IndividualZcbdbTreeNode node) {
        // 4. 计算增加部分的合计(购置+校内转入+其他)
        BigDecimal addCountSum = BigDecimal.ZERO;
        BigDecimal addAmountSum = BigDecimal.ZERO;
        for (int i = 1; i < 4; i++) {
            addCountSum = addCountSum.add(node.getNowDeCount()[i]);
            addAmountSum = addAmountSum.add(node.getNowDeAmount()[i]);
        }
        node.setNowDeCountWithId(4, addCountSum);
        node.setNowDeAmountWithId(4, addAmountSum);

        // 8. 计算减少部分的合计(报废报损+校内转出+其他)
        // 注：此处假设减少部分的值已通过其他方式设置
        BigDecimal deCountSum = BigDecimal.ZERO;
        BigDecimal deAmountSum = BigDecimal.ZERO;
        for (int i = 5; i < 8; i++) {
            deCountSum = deCountSum.add(node.getNowDeCount()[i]);
            deAmountSum = deAmountSum.add(node.getNowDeAmount()[i]);
        }
        node.setNowDeCountWithId(8, deCountSum);
        node.setNowDeAmountWithId(8, deAmountSum);

        // 9. 计算期末值(初期值 + 增加合计 - 减少合计)
        BigDecimal endCount = node.getNowDeCount()[0].add(node.getNowDeCount()[4]).subtract(node.getNowDeCount()[8]);
        BigDecimal endAmount = node.getNowDeAmount()[0].add(node.getNowDeAmount()[4]).subtract(node.getNowDeAmount()[8]);
        node.setNowDeCountWithId(9, endCount);
        node.setNowDeAmountWithId(9, endAmount);

//        // 若需要单位转换，在这里处理
//        if ("万".equals(jldw)) {
//            convertToTenThousand(node);
//        }
    }

    /**
     * 将所有数值转换为"万"为单位
     */
    private void convertToTenThousand(IndividualZcbdbTreeNode node) {
        final BigDecimal DIVISOR = BigDecimal.valueOf(10000);

        for (int i = 0; i < 10; i++) {
            if (node.getNowDeCount()[i] != null) {
                node.setNowDeCountWithId(i, node.getNowDeCount()[i].divide(DIVISOR, 4, BigDecimal.ROUND_HALF_UP));
            }
            if (node.getNowDeAmount()[i] != null) {
                node.setNowDeAmountWithId(i, node.getNowDeAmount()[i].divide(DIVISOR, 4, BigDecimal.ROUND_HALF_UP));
            }
        }
    }

    /**
     * 构建单位树形结构
     * @return 根节点
     */
    private IndividualZcbdbTreeNode buildInitTreeStructure() {
        // 获取所有单位数据
        List<dwb> dwbList = dwbDao.selectList(null);

        // 用于快速查找节点的映射表
        Map<String, IndividualZcbdbTreeNode> nodeMap = new HashMap<>();

        // 创建虚拟根节点
        IndividualZcbdbTreeNode virtualRoot = null;


        // 第一遍遍历：创建所有节点并放入映射表
        for (dwb unit : dwbList) {
            IndividualZcbdbTreeNode node = new IndividualZcbdbTreeNode(unit.getUnitCode(), unit.getUnitName());
            if(unit.getParentUnitCode()==null){ virtualRoot=node;}
            nodeMap.put(unit.getUnitCode(), node);
        }

        // 第二遍遍历：构建父子关系
        for (dwb unit : dwbList) {
            IndividualZcbdbTreeNode currentNode = nodeMap.get(unit.getUnitCode());

            // 如果当前节点不存在（理论上不会发生）
            if (currentNode == null) {
                continue;
            }

            // 处理父节点
            String parentCode = unit.getParentUnitCode(); // 上级单位代码
            if(parentCode==null) continue;



            // 获取父节点
            IndividualZcbdbTreeNode parentNode = nodeMap.get(parentCode);


            // 设置子节点的父引用
            currentNode.setParent(parentNode);

            // 初始化父节点的子列表
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }

            // 将当前节点添加为子节点
            parentNode.getChildren().add(currentNode);
        }


        return virtualRoot;
    }

    //可以实现自定义业务逻辑
    private boolean isValidSortField(String field) {
        // 允许参与排序的字段（对应数据库表中的下划线字段）
        return Set.of(
                "using_unit_code", "using_unit_name", "equipment_code", "category_code",
                "equipment_name", "brand_model", "specification", "amount",
                "jldw", "Cj", "Ggrq", "status",
                "jfkm", "storage_location_code", "storage_location_name",  // 注：storage_location_name在实体类中未定义，需确认是否存在
                "user_code", "user", "handler", "audit_status", "business_number",
                "accountant", "accounting_date", "remarks", "inputter", "input_date",  // 注：inputter、input_date在实体类中未定义，需确认是否存在
                "change_date", "change_reason", "transfer_in_unit_code", "applicant_code", "applicant"
        ).contains(field);
    }

}
