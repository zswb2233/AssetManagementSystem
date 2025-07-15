//
//package com.zswb.util;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//import com.zswb.model.dto.IndividualZcbdbTreeNode;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class PdfGenerationService {
//
//    // 定义中文字体（解决中文显示问题）
//    private static Font titleFont;
//    private static Font headerFont;
//    private static Font contentFont;
//    private static Font smallFont;
//    private static Font infoFont;
//
//    static {
//        try {
//            // 初始化字体（使用支持中文的字体）
//            // 设置中文字体
//            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H,
//                    BaseFont.EMBEDDED);
//            titleFont = new Font(baseFont, 16, Font.BOLD); // 标题字体
//            headerFont = new Font(baseFont, 10, Font.BOLD); // 表头字体
//            contentFont = new Font(baseFont, 10, Font.NORMAL); // 内容字体
//            smallFont = new Font(baseFont, 8, Font.NORMAL); // 小字体
//            infoFont = new Font(baseFont, 8, Font.NORMAL); // 信息字体
//        } catch (DocumentException | IOException e) {
//            throw new RuntimeException("初始化PDF字体失败", e);
//        }
//    }
//
//    /**
//     * 生成分户增减变动统计表PDF
//     * @param unitLevel 打印到几级（也就是往下找多少层数据进行打印)
//     * @param root 树状类型的需要打印的数据
//     * @param tableName 表头名
//     * @param tableUnit 制表单位
//     * @param tableDate 制表日期
//     * @param jldw 计量单位（元/万元，需要进行数量替换)
//     * @return
//     * @throws DocumentException
//     * @throws IOException
//     */
//    public byte[] generateIndividualHouseholdPdf(Integer unitLevel, IndividualZcbdbTreeNode root, String tableName,
//                                                 String tableUnit,
//                                                 Date tableDate,
//
//                                                 java.util.Date formDateFrom,
//                                                 java.util.Date formDateTo ,
//                                                 String jldw) throws DocumentException, IOException {
//        // 创建内存输出流
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        // 初始化文档（A4纸，纵向）
//        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); // 横向布局
//        PdfWriter writer = PdfWriter.getInstance(document, baos);
//
//        // 设置页脚区域
//        writer.setBoxSize("footer", new Rectangle(36, 36, 559, 50));
//
//        // 添加页脚
//        writer.setPageEvent(new HeaderFooter());
//
//        // 打开文档
//        document.open();
//
//        // 1. 添加标题
//        Paragraph title = new Paragraph(tableName, titleFont);
//        title.setAlignment(Element.ALIGN_CENTER); // 居中对齐
//        document.add(title);
//
//        // 添加空行分隔
//        document.add(new Paragraph("\n"));
//
//
////        ///////////
//        //创建一个三列的表格用于放置在左右内容
//        PdfPTable infoTable = new PdfPTable(3);
//        infoTable.setWidthPercentage(80);//宽度占满页面
//        infoTable.setSpacingAfter(2);//与下方表格的间隔
//
//        //设置列宽比例
//        float[] infoTableWidths={3f,3f,2f};
//        infoTable.setWidths(infoTableWidths);
//
//        //左侧内容：制表单位
//        Paragraph leftInfo = new Paragraph("制表单位："+tableUnit,infoFont);
//        PdfPCell leftCell = new PdfPCell(leftInfo);
//        leftCell.setBorder(Rectangle.NO_BORDER); // 去除边框
//        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 左对齐
//        infoTable.addCell(leftCell);
//        //中侧内容:资产入账时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String rightContent =
//                " 资产入账时间：" + sdf.format(formDateFrom) + "至" + sdf.format(formDateTo);
//        Paragraph midInfo = new Paragraph(rightContent, infoFont);
//        PdfPCell midCell = new PdfPCell(midInfo);
//        midCell.setBorder(Rectangle.NO_BORDER); // 去除边框
//        midCell.setHorizontalAlignment(Element.ALIGN_CENTER); // 右对齐
//        infoTable.addCell(midCell);
//        //右侧内容：(单位：台件、元/万元）
//        Paragraph rightInfo = new Paragraph("(单位：台件、"+jldw+"）",infoFont);
//        PdfPCell rightCell = new PdfPCell(rightInfo);
//        rightCell.setBorder(Rectangle.NO_BORDER); // 去除边框
//        rightCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 左对齐
//        infoTable.addCell(rightCell);
//        //将信息表格添加到文档
//        document.add(infoTable);
////        //////
//
//
//
//
//        // 2. 创建表格（11列：单位名称、期初数(数量/金额)、本期增加数(购置/校内转入/其它/合计)、本期减少数(报废报损/校内转出/其它/合计)、期末数）
//        PdfPTable table = new PdfPTable(12);
//        table.setWidthPercentage(95); // 表格宽度占页面百分比
//
//        // 定义列宽
//        float[] columnWidths = {15f, 8f, 5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f,5f, 8f};
//        table.setWidths(columnWidths);
//        table.setHorizontalAlignment(Element.ALIGN_CENTER); // 表格居中
//
//        // 3. 添加表头
//        addHeaderCells(table);
//
//        // 4. 递归添加树形结构数据到表格
//        addNodeToTable(table, root, 0, unitLevel);
//
//        // 将表格添加到文档
//        document.add(table);
//
//        // 添加说明文字
//        Paragraph note = new Paragraph("注：房屋、土地、构筑物分别按建筑面积、使用权面积、构筑物计量数进行统计。", smallFont);
//        note.setAlignment(Element.ALIGN_LEFT);
//        document.add(note);
//
//        // 添加制表日期和制表人
//        Paragraph footerPara = new Paragraph("制表日期：" + new SimpleDateFormat("yyyy/MM/dd").format(tableDate) +
//                " 制表人：admin", smallFont);
//        footerPara.setAlignment(Element.ALIGN_RIGHT);
//        document.add(footerPara);
//
//        // 关闭文档
//        document.close();
//
//        return baos.toByteArray();
//    }
//
//    /**
//     * 添加表头单元格
//     */
//    private void addHeaderCells(PdfPTable table) {
//        // 合并单元格
//        //单位名称下面有单位与数量金额
//        PdfPCell unitCell = new PdfPCell(new Phrase("单位名称", headerFont));
//        unitCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        unitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        unitCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        unitCell.setColspan(2);
//        unitCell.setRowspan(2);
//        table.addCell(unitCell);
//
//        // 期初数
//        PdfPCell beginCell = new PdfPCell(new Phrase("期初数", headerFont));
//        beginCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        beginCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        beginCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        beginCell.setColspan(1);
//        beginCell.setRowspan(2);
//        table.addCell(beginCell);
//
//        // 本期增加数
//        PdfPCell increaseCell = new PdfPCell(new Phrase("本期增加数", headerFont));
//        increaseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        increaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        increaseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        increaseCell.setColspan(4);
//        table.addCell(increaseCell);
//
//        // 本期减少数
//        PdfPCell decreaseCell = new PdfPCell(new Phrase("本期减少数", headerFont));
//        decreaseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        decreaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        decreaseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        decreaseCell.setColspan(4);
//        table.addCell(decreaseCell);
//
//        // 期末数
//        PdfPCell endCell = new PdfPCell(new Phrase("期末数", headerFont));
//        endCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        endCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        endCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        endCell.setColspan(1);
//        endCell.setRowspan(2);
//        table.addCell(endCell);
//
//        // 二级表头
//
//        table.addCell(new PdfPCell(new Phrase("购置", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("校内转入", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("其它", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("合计", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("报废报损", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("校内转出", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("其它", headerFont)));
//        table.addCell(new PdfPCell(new Phrase("合计", headerFont)));
//    }
//
//    /**
//     * 递归将树形节点添加到表格（带层级缩进）
//     * @param table PDF表格
//     * @param node 当前节点
//     * @param level 节点层级（用于缩进）
//     * @param maxLevel 最大层级
//     */
//    private void addNodeToTable(PdfPTable table, IndividualZcbdbTreeNode node, int level, int maxLevel) {
//        if (node == null || (maxLevel > 0 && level >= maxLevel)) {
//            return;
//        }
//
//        // 1. 单位名称（带层级缩进，每级缩进5个单位）
//        Paragraph unitNamePara = new Paragraph(node.getUnitName()+"("+node.getUnitCode()+")", contentFont);
//        unitNamePara.setFirstLineIndent(10 * level); // 每级缩进10个单位
//        PdfPCell nameCell = new PdfPCell(unitNamePara);
//        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        nameCell.setPadding(5);
//        nameCell.setRowspan(2); // 合并行
//
//        table.addCell(nameCell);
//
//        // 2. 期初数（数量）
//        PdfPCell beginCountCell = new PdfPCell(new Phrase(node.getNowDeCount()[0].toString(), contentFont));
//        beginCountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        beginCountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        beginCountCell.setPadding(5);
//        table.addCell(beginCountCell);
//
//        // 3. 期初数（金额）
//        PdfPCell beginAmountCell = new PdfPCell(new Phrase(node.getNowDeAmount()[0].toString(), contentFont));
//        beginAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        beginAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        beginAmountCell.setPadding(5);
//        table.addCell(beginAmountCell);
//
//        // 4. 本期增加数（购置）
//        PdfPCell purchaseCountCell = new PdfPCell(new Phrase(node.getNowDeCount()[1].toString(), contentFont));
//        purchaseCountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        purchaseCountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        purchaseCountCell.setPadding(5);
//        table.addCell(purchaseCountCell);
//
//        // 5. 本期增加数（校内转入）
//        PdfPCell transferInCell = new PdfPCell(new Phrase(node.getNowDeCount()[2].toString(), contentFont));
//        transferInCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        transferInCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        transferInCell.setPadding(5);
//        table.addCell(transferInCell);
//
//        // 6. 本期增加数（其它）
//        PdfPCell otherInCell = new PdfPCell(new Phrase(node.getNowDeCount()[3].toString(), contentFont));
//        otherInCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        otherInCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        otherInCell.setPadding(5);
//        table.addCell(otherInCell);
//
//        // 7. 本期增加数（合计）
//        PdfPCell increaseTotalCell = new PdfPCell(new Phrase(node.getNowDeCount()[4].toString(), contentFont));
//        increaseTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        increaseTotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        increaseTotalCell.setPadding(5);
//        table.addCell(increaseTotalCell);
//
//        // 8. 本期减少数（报废报损）
//        PdfPCell discardCell = new PdfPCell(new Phrase(node.getNowDeCount()[5].toString(), contentFont));
//        discardCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        discardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        discardCell.setPadding(5);
//        table.addCell(discardCell);
//
//        // 9. 本期减少数（校内转出）
//        PdfPCell transferOutCell = new PdfPCell(new Phrase(node.getNowDeCount()[6].toString(), contentFont));
//        transferOutCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        transferOutCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        transferOutCell.setPadding(5);
//        table.addCell(transferOutCell);
//
//        // 10. 本期减少数（其它）
//        PdfPCell otherOutCell = new PdfPCell(new Phrase(node.getNowDeCount()[7].toString(), contentFont));
//        otherOutCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        otherOutCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        otherOutCell.setPadding(5);
//        table.addCell(otherOutCell);
//
//        // 11. 本期减少数（合计）
//        PdfPCell decreaseTotalCell = new PdfPCell(new Phrase(node.getNowDeCount()[8].toString(), contentFont));
//        decreaseTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        decreaseTotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        decreaseTotalCell.setPadding(5);
//        table.addCell(decreaseTotalCell);
//
//        // 12. 期末数
//        PdfPCell endCell = new PdfPCell(new Phrase(node.getNowDeCount()[9].toString(), contentFont));
//        endCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        endCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        endCell.setPadding(5);
//        table.addCell(endCell);
//
//        // 递归添加子节点（层级+1）
//        List<IndividualZcbdbTreeNode> children = node.getChildren();
//        if (children != null && !children.isEmpty()) {
//            for (IndividualZcbdbTreeNode child : children) {
//                addNodeToTable(table, child, level + 1, maxLevel);
//            }
//        }
//    }
//
//    // 页眉页脚类
//    static class HeaderFooter extends PdfPageEventHelper {
//        public void onEndPage(PdfWriter writer, Document document) {
//            Rectangle rect = writer.getBoxSize("footer");
//
//            // 添加页码
//            ColumnText.showTextAligned(writer.getDirectContent(),
//                    Element.ALIGN_RIGHT, new Phrase(String.format("第 %d 页", writer.getPageNumber()), smallFont),
//                    rect.getRight() - 18, rect.getBottom() - 18, 0);
//        }
//    }
//}
package com.zswb.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.zswb.model.dto.IndividualZcbdbTreeNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfGenerationService {

    // 定义中文字体（解决中文显示问题）
    private static Font titleFont;
    private static Font headerFont;
    private static Font contentFont;
    private static Font smallFont;
    private static Font infoFont;

    static {
        try {
            // 初始化字体（使用支持中文的字体）
            // 设置中文字体
            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            titleFont = new Font(baseFont, 16, Font.BOLD); // 标题字体
            headerFont = new Font(baseFont, 10, Font.BOLD); // 表头字体
            contentFont = new Font(baseFont, 8, Font.NORMAL); // 内容字体
            smallFont = new Font(baseFont, 8, Font.NORMAL); // 小字体
            infoFont = new Font(baseFont, 8, Font.NORMAL); // 信息字体
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("初始化PDF字体失败", e);
        }
    }

    /**
     * 生成分户增减变动统计表PDF
     * @param unitLevel 打印到几级（也就是往下找多少层数据进行打印)
     * @param root 树状类型的需要打印的数据
     * @param tableName 表头名
     * @param tableUnit 制表单位
     * @param tableDate 制表日期
     * @param jldw 计量单位（元/万元，需要进行数量替换)
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public byte[] generateIndividualHouseholdPdf(Integer unitLevel, IndividualZcbdbTreeNode root, String tableName,
                                                 String tableUnit,
                                                 Date tableDate,

                                                 java.util.Date formDateFrom,
                                                 java.util.Date formDateTo ,
                                                 String jldw) throws DocumentException, IOException {
        // 创建内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 初始化文档（A4纸，纵向）
        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); // 横向布局
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        // 设置页脚区域
        writer.setBoxSize("footer", new Rectangle(36, 36, 559, 50));

        // 添加页脚
        writer.setPageEvent(new HeaderFooter());

        // 打开文档
        document.open();

        // 1. 添加标题
        Paragraph title = new Paragraph(tableName, titleFont);
        title.setAlignment(Element.ALIGN_CENTER); // 居中对齐
        document.add(title);

        // 添加空行分隔
        document.add(new Paragraph("\n"));


//        ///////////
        //创建一个三列的表格用于放置在左右内容
        PdfPTable infoTable = new PdfPTable(3);
        infoTable.setWidthPercentage(80);//宽度占满页面
        infoTable.setSpacingAfter(2);//与下方表格的间隔

        //设置列宽比例
        float[] infoTableWidths={3f,3f,2f};
        infoTable.setWidths(infoTableWidths);

        //左侧内容：制表单位
        Paragraph leftInfo = new Paragraph("制表单位："+tableUnit,infoFont);
        PdfPCell leftCell = new PdfPCell(leftInfo);
        leftCell.setBorder(Rectangle.NO_BORDER); // 去除边框
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 左对齐
        infoTable.addCell(leftCell);
        //中侧内容:资产入账时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String rightContent =
                " 资产入账时间：" + sdf.format(formDateFrom) + "至" + sdf.format(formDateTo);
        Paragraph midInfo = new Paragraph(rightContent, infoFont);
        PdfPCell midCell = new PdfPCell(midInfo);
        midCell.setBorder(Rectangle.NO_BORDER); // 去除边框
        midCell.setHorizontalAlignment(Element.ALIGN_CENTER); // 右对齐
        infoTable.addCell(midCell);
        //右侧内容：(单位：台件、元/万元）
        Paragraph rightInfo = new Paragraph("(单位：台件、"+jldw+"）",infoFont);
        PdfPCell rightCell = new PdfPCell(rightInfo);
        rightCell.setBorder(Rectangle.NO_BORDER); // 去除边框
        rightCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 左对齐
        infoTable.addCell(rightCell);
        //将信息表格添加到文档
        document.add(infoTable);
//        //////




        // 2. 创建表格（12列：单位名称、期初数(数量/金额)、本期增加数(购置/校内转入/其它/合计)、本期减少数(报废报损/校内转出/其它/合计)、期末数）
        PdfPTable table = new PdfPTable(12);
        table.setWidthPercentage(95); // 表格宽度占页面百分比

        // 定义列宽
        float[] columnWidths = {180f, 43f, 110f, 100f, 100f, 100f, 110f, 100f, 100f, 100f,110f, 110f};
        table.setWidths(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_CENTER); // 表格居中

        // 3. 添加表头
        addHeaderCells(table);

        // 4. 递归添加树形结构数据到表格
        addNodeToTable(table, root, 0, unitLevel,jldw);

        // 将表格添加到文档
        document.add(table);

        // 添加说明文字
        Paragraph note = new Paragraph("注：房屋、土地、构筑物分别按建筑面积、使用权面积、构筑物计量数进行统计。", smallFont);
        note.setAlignment(Element.ALIGN_LEFT);
        document.add(note);

        // 添加制表日期和制表人
        Paragraph footerPara = new Paragraph("制表日期：" + new SimpleDateFormat("yyyy/MM/dd").format(tableDate) +
                " 制表人：admin", smallFont);
        footerPara.setAlignment(Element.ALIGN_RIGHT);
        document.add(footerPara);

        // 关闭文档
        document.close();

        return baos.toByteArray();
    }

    /**
     * 添加表头单元格
     */
    private void addHeaderCells(PdfPTable table) {
        // 合并单元格
        //单位名称下面有单位与数量金额
        PdfPCell unitCell = new PdfPCell(new Phrase("单位名称", headerFont));
        unitCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        unitCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        unitCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        unitCell.setColspan(2);
        unitCell.setRowspan(2);
        table.addCell(unitCell);

        // 期初数
        PdfPCell beginCell = new PdfPCell(new Phrase("期初数", headerFont));
        beginCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        beginCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        beginCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        beginCell.setColspan(1);
        beginCell.setRowspan(2);
        table.addCell(beginCell);

        // 本期增加数
        PdfPCell increaseCell = new PdfPCell(new Phrase("本期增加数", headerFont));
        increaseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        increaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        increaseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        increaseCell.setColspan(4);
        table.addCell(increaseCell);

        // 本期减少数
        PdfPCell decreaseCell = new PdfPCell(new Phrase("本期减少数", headerFont));
        decreaseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        decreaseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        decreaseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        decreaseCell.setColspan(4);
        table.addCell(decreaseCell);

        // 期末数
        PdfPCell endCell = new PdfPCell(new Phrase("期末数", headerFont));
        endCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        endCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        endCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        endCell.setColspan(1);
        endCell.setRowspan(2);
        table.addCell(endCell);

        // 二级表头
        table.addCell(new PdfPCell(new Phrase("购置", headerFont)));
        table.addCell(new PdfPCell(new Phrase("校内转入", headerFont)));
        table.addCell(new PdfPCell(new Phrase("其它", headerFont)));
        table.addCell(new PdfPCell(new Phrase("合计", headerFont)));
        table.addCell(new PdfPCell(new Phrase("报废报损", headerFont)));
        table.addCell(new PdfPCell(new Phrase("校内转出", headerFont)));
        table.addCell(new PdfPCell(new Phrase("其它", headerFont)));
        table.addCell(new PdfPCell(new Phrase("合计", headerFont)));
    }

    /**
     * 递归将树形节点添加到表格（带层级缩进）
     * @param table PDF表格
     * @param node 当前节点
     * @param level 节点层级（用于缩进）
     * @param maxLevel 最大层级
     */
    private void addNodeToTable(PdfPTable table, IndividualZcbdbTreeNode node, int level, int maxLevel,String jldw) {
        if (node == null || (maxLevel > 0 && level >= maxLevel)) {
            return;
        }

        // 创建单位名称单元格，合并两行
        Paragraph unitNamePara = new Paragraph(node.getUnitName() + "(" + node.getUnitCode() + ")", contentFont);
        unitNamePara.setFirstLineIndent(10 * level); // 每级缩进10个单位

        PdfPCell unitNameCell = new PdfPCell(unitNamePara);
        unitNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        unitNameCell.setPadding(5);
        unitNameCell.setRowspan(2); // 单位名称跨两行

        table.addCell(unitNameCell);
        // 创建新的单元格，里面是数量，金额，跨两行
        // 第一行数据
        Paragraph firstLine = new Paragraph("数量",contentFont);
        // 第二行数据
        Paragraph secondLine = new Paragraph("金额",contentFont);

        // 创建一个单元格，添加两行数据
        PdfPCell cell = new PdfPCell();
        cell.addElement(firstLine);
        cell.addElement(secondLine);

        // 设置单元格跨两行
        cell.setRowspan(2);

        // 添加合并后的单元格到表格
        table.addCell(cell);




        // 添加数量数据
        addDataRow(table, node, true,jldw);

        // 添加金额行（不需要再添加单位名称单元格）
        addDataRow(table, node, false,jldw);

        // 递归添加子节点
        List<IndividualZcbdbTreeNode> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            for (IndividualZcbdbTreeNode child : children) {
                addNodeToTable(table, child, level + 1, maxLevel,jldw);
            }
        }
    }

    /**
     * 添加数据行（数量行或金额行）
     * @param table PDF表格
     * @param node 当前节点
     * @param isQuantity 是否为数量行
     */
    private void addDataRow(PdfPTable table, IndividualZcbdbTreeNode node, boolean isQuantity,String jldw) {

        BigDecimal[] dataList = isQuantity ? node.getNowDeCount() : node.getNowDeAmount();

        // 检查 dataList 不为 null 且 jldw 为 "万"
        if ((isQuantity)&&dataList != null && "万".equals(jldw)) {
            for (int i = 0; i < dataList.length; i++) {
                if (dataList[i] != null) {
                    // 指定精度和舍入模式，避免除不尽异常
                    dataList[i] = dataList[i].divide(
                            BigDecimal.valueOf(10000),
                            4, // 保留4位小数
                            RoundingMode.HALF_UP // 四舍五入
                    );
                }
            }
        }
        // 为每行创建11个单元格
        for (int i = 0; i < 10; i++) {
            String value = "0";
            if (dataList != null && i < dataList.length) {
                value = dataList[i].toString();
            }

            PdfPCell cell = new PdfPCell(new Phrase(value, contentFont));
            // 设置水平对齐为右对齐
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            // 设置垂直对齐为居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    // 页眉页脚类
    static class HeaderFooter extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("footer");

            // 添加页码
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(String.format("第 %d 页", writer.getPageNumber()), smallFont),
                    rect.getRight() - 18, rect.getBottom() - 18, 0);
        }
    }
}