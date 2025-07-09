package com.zswb.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfGenerator2 {

    // 用于存储表格数据，实际可从数据库、Excel 等读取，这里模拟数据
    static class AssetData {
        String useUnit;
        String assetNo;
        String assetName;
        String model;
        String financeCategory;
        String businessType;
        int originalQty;
        double originalAmount;
        int changeQty;
        double changeAmount;
        String obtainDate;
        String changeDate;
        String changeReason;

        public AssetData(String useUnit, String assetNo, String assetName, String model, String financeCategory, String businessType, int originalQty, double originalAmount, int changeQty, double changeAmount, String obtainDate, String changeDate, String changeReason) {
            this.useUnit = useUnit;
            this.assetNo = assetNo;
            this.assetName = assetName;
            this.model = model;
            this.financeCategory = financeCategory;
            this.businessType = businessType;
            this.originalQty = originalQty;
            this.originalAmount = originalAmount;
            this.changeQty = changeQty;
            this.changeAmount = changeAmount;
            this.obtainDate = obtainDate;
            this.changeDate = changeDate;
            this.changeReason = changeReason;
        }
    }

    public static void main(String[] args) {
        try {
            // 生成带表格的 PDF
            generateAssetTablePdf("C:\\CodeProgram\\AssetManagementSystemCloud\\utils\\src\\main\\resources\\pdfDocument\\asset_report.pdf");
            System.out.println("PDF 文件生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成带资产变动表格的 PDF
     * @param filePath PDF 保存路径
     * @throws DocumentException 文档操作异常
     * @throws IOException  IO 异常
     */
    public static void generateAssetTablePdf(String filePath) throws DocumentException, IOException {
        // 创建文档，设置页面大小为 A4
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // 设置中文字体，这里以 Windows 系统宋体为例，其他系统按需替换路径
        BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font titleFont = new Font(baseFont, 16, Font.BOLD); // 标题字体
        Font headerFont = new Font(baseFont, 12, Font.BOLD); // 表头字体
        Font contentFont = new Font(baseFont, 12, Font.NORMAL); // 内容字体

        // 添加标题
        Paragraph title = new Paragraph("中国大学变动总账", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // 添加制表单位和日期信息
        Paragraph infoPara = new Paragraph("制表单位：中国大学 (00)  资产变动日期：2025-01-01 至 2025-07-09", contentFont);
        infoPara.setAlignment(Element.ALIGN_RIGHT);
        document.add(infoPara);
        document.add(Chunk.NEWLINE); // 换行

        // 创建表格，根据实际列数设置，这里和样例一致是 13 列
        PdfPTable table = new PdfPTable(13);
        table.setWidthPercentage(100); // 表格宽度占满页面
        // 设置列宽，可根据实际需求调整，这里简单均分，也可自定义每列宽度数组
        float[] columnWidths = new float[13];
        for (int i = 0; i < 13; i++) {
            columnWidths[i] = 1f;
        }
        table.setWidths(columnWidths);

        // 定义表头数据
        String[] headers = {"使用单位", "资产编号", "资产名称", "型号/规格", "财政大类", "业务类型",
                "原数量", "原金额(元)", "变动数量", "变动金额(元)", "取得日期", "变动日期", "变动原因/转入单位"};
        // 添加表头行
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // 表头背景色
            table.addCell(cell);
        }

        // 模拟表格数据，实际可从数据库等获取
        List<AssetData> dataList = getMockData();
        // 添加数据行
        for (AssetData data : dataList) {
            table.addCell(new PdfPCell(new Paragraph(data.useUnit, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.assetNo, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.assetName, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.model, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.financeCategory, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.businessType, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(data.originalQty), contentFont)));
            table.addCell(new PdfPCell(new Paragraph(String.format("%.2f", data.originalAmount), contentFont)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(data.changeQty), contentFont)));
            table.addCell(new PdfPCell(new Paragraph(String.format("%.2f", data.changeAmount), contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.obtainDate, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.changeDate, contentFont)));
            table.addCell(new PdfPCell(new Paragraph(data.changeReason, contentFont)));
        }

        // 将表格加入文档
        document.add(table);

        document.close();
    }

    /**
     * 模拟获取表格数据，实际可从数据库、Excel 等读取
     * @return 资产变动数据列表
     */
    private static List<AssetData> getMockData() {
        List<AssetData> dataList = new ArrayList<>();
        // 模拟几条数据，和样例格式尽量贴近
        dataList.add(new AssetData("机电工程学院现代产品设计与制造技术", "202500394", "笔记本电脑", "111/111", "设备", "资产报废", 1, 10000.00, 1, -10000.00, "2025/06/20", "2025/07/02", "因设备老化，无法正常使用，做资产报废处理"));
        dataList.add(new AssetData("机电工程学院现代产品设计与制造技术", "202500172", "笔记本电脑（便携式计算机）", "213/4", "设备", "公物交物", 1, 4444.00, 1, 4444.00, "2025/04/27", "2025/06/04", "国际合作与交流处交存（港澳台合作）"));
        dataList.add(new AssetData("机电工程学院现代产品设计与制造技术", "202500005", "笔记本电脑（便携式计算机）", "*/#", "设备", "公物仓入仓", 1, 6000.00, 1, 6000.00, "2025/03/21", "2025/03/25", "机电工程学院现代产品设计与制造技术（1231001）"));
        return dataList;
    }
}