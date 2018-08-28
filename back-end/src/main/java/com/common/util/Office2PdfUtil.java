package com.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jodconverter.JodConverter;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;

import java.io.File;
import java.io.InputStream;

/**
 * Created by lego-jspx01 on 2018/7/21.
 */
public class Office2PdfUtil {
    private static final Log log = LogFactory.getLog(Office2PdfUtil.class);
    public static OfficeManager getOfficeManager(String officeHome){
       return LocalOfficeManager.builder().officeHome(officeHome).install().build();
    }

    public static void startOfficeManager(OfficeManager officeManager){
        try {
            officeManager.start();
        }catch (OfficeException e) {
            log.error("=====officeManager start error:"+e.getMessage());
            e.printStackTrace();
        }
    }
    public static void closeOfficeManager(OfficeManager officeManager){
        try {
            log.info("=====officeManager stop==============");
            if(officeManager.isRunning()){
                officeManager.stop();
            }
        }catch (OfficeException e) {
            log.error("=====officeManager stop error:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public static int doc2PDF(OfficeManager officeManager,InputStream sourceSteam, String destFile) {
        try {
            // 输出文件目录
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().exists();
            }
            if(!officeManager.isRunning()){
                officeManager.start();
            }
            System.out.println("================doc2PDF============isRunning status=================>>>>" + officeManager.isRunning());
            // 转换文档到pdf
            JodConverter.convert(sourceSteam).as(DefaultDocumentFormatRegistry.DOC).to(outputFile).execute();
        }  catch (OfficeException e) {
            log.error("=====doc2PDF error:"+e.getMessage());
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public static int ppt2PDF(OfficeManager officeManager,InputStream sourceSteam, String destFile) {
        try {
            // 输出文件目录
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().exists();
            }
            if(!officeManager.isRunning()){
                officeManager.start();
            }
            System.out.println("===========ppt2PDF=================isRunning status=================>>>>" + officeManager.isRunning());
            // 转换文档到pdf
            JodConverter.convert(sourceSteam).as(DefaultDocumentFormatRegistry.PPT).to(outputFile).execute();
        }  catch (OfficeException e) {
            log.error("=====ppt2PDF error:" + e.getMessage());
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public static int xls2PDF(OfficeManager officeManager,InputStream sourceSteam, String destFile) {
        try {
            // 输出文件目录
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().exists();
            }
            if(!officeManager.isRunning()){
                officeManager.start();
            }
            System.out.println("===========xls2PDF=================isRunning status=================>>>>" + officeManager.isRunning());
            // 转换文档到pdf
            JodConverter.convert(sourceSteam).as(DefaultDocumentFormatRegistry.XLS).to(outputFile).execute();
        }  catch (OfficeException e) {
            log.error("=====xls2PDF error:" + e.getMessage());
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public static int office2PDF(String sourceFile, String destFile) {
        try {
            // 源文件目录
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                System.out.println("源文件不存在！");
                return -1;
            }
            // 输出文件目录
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().exists();
            }
            // 转换文档到pdf
            JodConverter.convert(inputFile).to(outputFile).execute();
        }  catch (OfficeException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int xls2xlsx(OfficeManager officeManager,String sourceFile, String destFile) {
        try {
            // 源文件目录
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                System.out.println("源文件不存在！");
                return -1;
            }
            // 输出文件目录
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().exists();
            }
            // 转换文档到xlsx
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            converter.convert(inputFile, outputFile, DefaultDocumentFormatRegistry.XLSX);
        }  catch (OfficeException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

}
