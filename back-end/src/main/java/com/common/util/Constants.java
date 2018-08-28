package com.common.util;

/**
 * Created by eric on 2016-5-16.
 */
public class Constants {
    //系统默认一页记录数
    public static final int PAGE_SIZE = 10;
    public static final String IS_DELETED = "1";
    public static final String DELETE_DEFAULT = "0";
    public static final String []DEAL_TYPE =  {"collect","like","read","download","comment"};//collect、like、read
    public static final String DEFUAL_TYPE = "default";
    public static final String DEFAULT_STATUS = "1";
    public static final String TEXT_FILE_TYPE = ",.doc,.xls,.ppt,";//需要特殊处理的文档类型
    public static final String VIDEO_FILE_TYPE = ",.mp4,.swf,.flv,.avi,.mpg,.wmv,";//需要转码的文档类型
    public static final String DEAL_STATUS = "deal";//已处理--包含视频转码和文档转换
    public static final String TEXT_DEAL_STATUS = "text";//文档未处理
    public static final String VIDEO_DEAL_STATUS = "video";//视频未处理
    public static final String NO_UPLOAD_FLAG = "10000";//未上传
    public static final String NO_UPLOAD_RES = "10001";//视频资源未上传
    public static final String NO_UPLOAD_PIC = "10010";//图片未上传
    public static final String UPLOAD_FLAG = "10011";//已上传
    public static final String OPNE_FLAG= "1";//上架
    public static final String NOT_OPNE_FLAG= "0";//未上架
    public static final String VISIT_AUTH= "11";//读写权限

    public static final String OTHER_RES_DOWNLOAD= "1";//是从第三方下载资源
}
