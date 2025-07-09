package com.zswb.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

/**
 * 生成pdf文件到本地某个文件夹。
 */
public class PdfGenerator {

    public static void main(String[] args) {
        try {
            // 生成简单文本PDF
            generateSimpleTextPdf("C:\\CodeProgram\\AssetManagementSystemCloud\\utils\\src\\main\\resources\\pdfDocument\\simple_text.pdf");

            // 生成包含中文字符的PDF
            generateChineseTextPdf("C:\\CodeProgram\\AssetManagementSystemCloud\\utils\\src\\main\\resources\\pdfDocument\\chinese_text.pdf");

            // 从HTML生成PDF
            //generatePdfFromHtml("html_content.pdf");

            System.out.println("PDF文件生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成简单文本PDF
     */
    public static void generateSimpleTextPdf(String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        document.add(new Paragraph("Hello, World!"));
        document.close();
    }

    /**
     * 生成包含中文字符的PDF
     */
    public static void generateChineseTextPdf(String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // 使用系统字体
        BaseFont bfChinese = BaseFont.createFont(
                "C:/Windows/Fonts/simsun.ttc,0", // Windows系统宋体
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );

        // 或者使用Linux/Mac字体
        // BaseFont bfChinese = BaseFont.createFont(
        //     "/usr/share/fonts/truetype/wqy/wqy-microhei.ttc,0",
        //     BaseFont.IDENTITY_H,
        //     BaseFont.EMBEDDED
        // );

        Font chineseFont = new Font(bfChinese, 12, Font.NORMAL);

        document.add(new Paragraph("你好，这是一个包含中文的PDF文档。", chineseFont));
        document.add(new Paragraph("这是第二行中文文本示例。", chineseFont));

        document.close();
    }

    /**
     * 从HTML生成PDF
     */
//    public static void generatePdfFromHtml(String filePath) throws DocumentException, IOException {
//        Document document = new Document();
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
//
//        document.open();
//
//        // HTML内容
//        String htmlContent = "<html><body>" +
//                "<h1 style='color:blue;'>HTML转PDF示例</h1>" +
//                "<p style='font-size:16px;'>这是一个从HTML转换而来的PDF文档。</p>" +
//                "<p style='font-size:16px;'>中文内容测试：你好，世界！</p>" +
//                "</body></html>";
//
//        // 设置中文字体
//        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//
//        // 转换HTML为PDF
//        XMLWorkerHelper.getInstance().parseXHtml(
//                writer,
//                document,
//                new ByteArrayInputStream(htmlContent.getBytes("UTF-8")),
//                null,
//                new ByteArrayInputStream(("<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /></head><body></body></html>").getBytes("UTF-8")),
//                new com.itextpdf.tool.xml.net.FileRetrieve() {
//                    @Override
//                    public void processFromHref(String s, com.itextpdf.tool.xml.net.ReadingProcessor readingProcessor) throws IOException {
//                        // 处理外部资源引用
//                        if (s != null && !s.isEmpty()) {
//                            try (InputStream is = new URL(s).openStream()) {
//                                readingProcessor.process(is);
//                            } catch (MalformedURLException e) {
//                                System.err.println("URL格式错误: " + s);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void processFromStream(InputStream inputStream, com.itextpdf.tool.xml.net.ReadingProcessor readingProcessor) throws IOException {
//                        // 使用传入的InputStream而不是System.in
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            baos.write(buffer, 0, bytesRead);
//                        }
//                        readingProcessor.process(new ByteArrayInputStream(baos.toByteArray()));
//                    }
//                },
//                new com.itextpdf.tool.xml.XMLWorkerFontProvider() {
//                    @Override
//                    public Font getFont(final String fontname, String encoding, float size, final int style) {
//                        // 确保中文正常显示
//                        return new Font(bfChinese, size, style);
//                    }
//                }
//        );
//
//        document.close();
//    }
}