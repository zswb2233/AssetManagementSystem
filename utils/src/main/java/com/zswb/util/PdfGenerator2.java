package com.zswb.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.zswb.model.output.zcbdbOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PdfGenerator2 {

    /**
     * 生成带资产变动表格的 PDF
     *
     * @param filePath     PDF 保存路径
     * @param headerName   标题
     * @param formUnit     制表单位
     * @param fromDate     制表日期
     * @param formDateFrom 资产变动起始日期
     * @param formDateTo   资产变动结束日期
     * @param resultOutput 数据
     * @throws DocumentException 文档操作异常
     * @throws IOException       IO 异常
     */
    public static void generateAssetTablePdf(String filePath, String headerName, String formUnit, java.util.Date fromDate,
                                             java.util.Date formDateFrom, java.util.Date formDateTo, Map<String, Object> resultOutput) throws DocumentException, IOException {
        // 创建文档，设置页面大小为 A4
        Document document = new Document(PageSize.A4, 36, 36, 20, 400);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // 设置中文字体
        BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);
        Font titleFont = new Font(baseFont, 20, Font.BOLD); // 标题字体
        Font headerFont = new Font(baseFont, 8, Font.BOLD); // 表头字体
        Font contentFont = new Font(baseFont, 6, Font.NORMAL); // 内容字体
        Font infoFont = new Font(baseFont, 4, Font.NORMAL); // 信息字体

        // 添加标题
        Paragraph title = new Paragraph(headerName, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(8);
        document.add(title);


        //创建一个两列的表格用于放置在左右内容
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);//宽度占满页面
        infoTable.setSpacingAfter(2);//与下方表格的间隔

        //设置列宽比例
        float[] infoTableWidths={6f,4f};
        infoTable.setWidths(infoTableWidths);

        //左侧内容：制表单位
        Paragraph leftInfo = new Paragraph("制表单位："+formUnit,infoFont);
        PdfPCell leftCell = new PdfPCell(leftInfo);
        leftCell.setBorder(Rectangle.NO_BORDER); // 去除边框
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 左对齐
        infoTable.addCell(leftCell);
        //右侧内容:制表日期和资产变动时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String rightContent = "制表日期：" + sdf.format(fromDate) +
                " 资产变动时间：" + sdf.format(formDateFrom) + "至" + sdf.format(formDateTo);
        Paragraph rightInfo = new Paragraph(rightContent, infoFont);
        PdfPCell rightCell = new PdfPCell(rightInfo);
        rightCell.setBorder(Rectangle.NO_BORDER); // 去除边框
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // 右对齐
        infoTable.addCell(rightCell);

        //将信息表格添加到文档
        document.add(infoTable);

        // 获取数据
        List<zcbdbOutput> dataList = (List<zcbdbOutput>) resultOutput.get("records");

        // 创建表格，13 列
        PdfPTable table = new PdfPTable(13);
        table.setWidthPercentage(100); // 表格宽度占满页面

        // 设置列宽
        float[] columnWidths = {2f, 1.5f, 1.5f, 2f, 1f, 1f, 1f, 1f, 2f, 1.5f, 1.5f, 2f, 2f};
        table.setWidths(columnWidths);

        // 设置表格样式
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setHeaderRows(1);

        // 定义表头数据
        String[] headers = { "原使用单位名称", "资产编号", "资产名称", "规格/型号", "财政大类", "业务类型", "原数量", "变动数量", "原金额(元)",
                "变动金额", "变动日期", "变动原因", "转入单位" };

        // 添加表头行
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // 表头背景色
            cell.setBorderWidth(0.5f);
            table.addCell(cell);
        }

        // 处理表格数据，添加到表格

        for (zcbdbOutput data : dataList) {
            // 格式化日期显示
            String formattedDate = data.getChangeDate() != null ? sdf.format(data.getChangeDate()) : "";

            table.addCell(createContentCell(data.getUsingUnitName(), contentFont));
            table.addCell(createContentCell(data.getEquipmentCode(), contentFont));
            table.addCell(createContentCell(data.getEquipmentName(), contentFont));
            table.addCell(createContentCell(data.getSpecification(), contentFont));
            table.addCell(createContentCell(data.getCategoryCode(), contentFont));
            table.addCell(createContentCell(data.getStatus(), contentFont));
            table.addCell(createContentCell(String.valueOf(data.getBeforeCount()), contentFont));
            //变动数量 和 变动金额 显示为 nu 是因为当 data.getChangedCount() 或 data.getChangedAmount()
            // 返回 null 时，代码中的 String.format("%.2f", data.getChangedCount()) 会将 null 格式化为 "nu"。
            table.addCell(createContentCell(String.format("%.2f", data.getChangedCount()), contentFont));
            table.addCell(createContentCell(String.valueOf(data.getAmount()), contentFont));
            table.addCell(createContentCell(String.format("%.2f", data.getChangedAmount()), contentFont));
            table.addCell(createContentCell(formattedDate, contentFont));
            table.addCell(createContentCell(data.getChangeReason(), contentFont));
            table.addCell(createContentCell(data.getTransferInUnitName(), contentFont));
        }

        // 将表格添加到文档
        document.add(table);

        // 关闭文档
        document.close();

        // 处理分页和添加页码
        handlePaginationAndPageNumbers(filePath, headers, headerFont, contentFont, columnWidths);
    }

    /**
     * 创建内容单元格
     */
    private static PdfPCell createContentCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(content != null ? content : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(0.5f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    /**
     * 处理分页和添加页码
     *
     * @param filePath     PDF 文件路径
     * @param headers      表头数据
     * @param headerFont   表头字体
     * @param contentFont  内容字体
     * @param columnWidths 列宽
     * @throws DocumentException 文档操作异常
     * @throws IOException       IO 异常
     */
    private static void handlePaginationAndPageNumbers(String filePath, String[] headers, Font headerFont, Font contentFont, float[] columnWidths) throws DocumentException, IOException {
        // 创建新的 PDF 文档
        Document document = new Document(PageSize.A4, 36, 36, 20, 36);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath.replace(".pdf", "_new.pdf")));

        // 创建事件处理器，处理页眉和页码
        HeaderFooterEvent event = new HeaderFooterEvent(headers, headerFont, contentFont, columnWidths);
        writer.setPageEvent(event);

        document.open();

        // 读取原始 PDF
        PdfReader reader = new PdfReader(filePath);
        int totalPages = reader.getNumberOfPages();
        event.setTotalPages(totalPages);

        // 处理每页
        for (int i = 1; i <= totalPages; i++) {
            document.setPageSize(reader.getPageSize(i));
            document.newPage();
            PdfContentByte canvas = writer.getDirectContent();

            // 添加原始页面内容
            PdfImportedPage page = writer.getImportedPage(reader, i);
            canvas.addTemplate(page, 0, 0);

            // 只有第二页及以后才需要添加固定表头,
            //在onEndPage里已经设置了条件

            event.setCurrentPage(i);
            event.onEndPage(writer, document);


        }

        // 关闭文档
        document.close();
        reader.close();

        // 替换原始文件
        File originalFile = new File(filePath);
        File newFile = new File(filePath.replace(".pdf", "_new.pdf"));
        if (originalFile.exists()) {
            originalFile.delete();
        }
        newFile.renameTo(originalFile);
    }

    /**
     * 页眉页脚事件处理器
     */
    static class HeaderFooterEvent extends PdfPageEventHelper {
        private String[] headers;
        private Font headerFont;
        private Font contentFont;
        private float[] columnWidths;
        private int totalPages;
        private int currentPage;

        public HeaderFooterEvent(String[] headers, Font headerFont, Font contentFont, float[] columnWidths) {
            this.headers = headers;
            this.headerFont = headerFont;
            this.contentFont = contentFont;
            this.columnWidths = columnWidths;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // 只有第二页及以后才添加固定表头
            /*
            if (currentPage > 1) {
                try {
                    // 创建表头表格
                    PdfPTable headerTable = new PdfPTable(13);
                    headerTable.setWidthPercentage(100);
                    headerTable.setWidths(columnWidths);

                    // 关键修改：设置表格的绝对宽度并锁定
                    headerTable.setTotalWidth(PageSize.A4.getWidth() - 72);
                    headerTable.setLockedWidth(true);

                    // 添加表头单元格
                    for (String header : headers) {
                        PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell.setBorderWidth(0.5f);
                        headerTable.addCell(cell);
                    }

                    // 写入表头
                    headerTable.writeSelectedRows(0, -1, 36, PageSize.A4.getHeight() - 30, writer.getDirectContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            */


            // 添加页码，修正页码显示问题
            int displayedPageNum = currentPage; // 当前页
            int displayedTotalPages = totalPages; // 总页数

            // 添加页码
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                    new Paragraph("第 " + displayedPageNum + " 页 / 共 " + displayedTotalPages + " 页", contentFont),
                    PageSize.A4.getWidth() / 2, 30, 0);
        }
    }
}