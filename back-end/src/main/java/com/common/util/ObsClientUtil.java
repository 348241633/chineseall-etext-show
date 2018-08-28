package com.common.util;

/**
 * Created by eric on 2015-10-29.
 */


import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ObsClientUtil {
    private static final transient Log log = LogFactory.getLog(ObsClientUtil.class);
    private static ObsClientUtil INSTANCE = null;
    private static final String endPoint = "obs.cn-north-1.myhwclouds.com";//sh_chineseall
    private static final String ak = "LDIQJU08F4OJWCFP6EUO";
    private static final String sk = "b6bq5BtWlkMndUCCnaB7eC14K1oM9X8YzUWKnWvp";
    private static final String bucketName = "*** Provide your Secret Key ***";
    private ObsClientUtil(){}
    public static ObsClientUtil getInstance(){
        if(INSTANCE == null) INSTANCE = new ObsClientUtil();
        return INSTANCE;
    }
    /**
     * 连接obs
     * @return
     */
    public static ObsClient getObsClient(){
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        config.setHttpsOnly(false);
        ObsClient obsClient = new ObsClient(ak, sk, config);
        return obsClient;
    }


    public static void setBucketAcl(ObsClient obsClient,String bucketName){
        obsClient.setBucketAcl(bucketName, AccessControlList.REST_CANNED_PUBLIC_READ);
    }

    /**
     * 关闭obs对象
     * @return
     */
    public static void closeObsClient(ObsClient obsClient){
        try {
            obsClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param tempFile
     * @param objectkey
     * @return
     * @throws IOException
     */
    public static void uploadToObsClient(String bucketName,String objectkey,File tempFile)  throws IOException{
        ObsClient obsClient = ObsClientUtil.getObsClient();
        obsClient.putObject(bucketName, objectkey, tempFile);
        ObsClientUtil.closeObsClient(obsClient);
    }

    public static void uploadFileToObsClient(ObsClient obsClient,String bucketName,String objectkey,File tempFile)  throws IOException{
        obsClient.putObject(bucketName, objectkey, tempFile);
    }


    /**
     * 上传网络流
     */
    public static Integer uploadInputStreamToObsClient(String bucketName,String objectkey, String oldurl) {
        InputStream inputStream = null;
        ObsClient obsClient = null;
        Integer length = 0;
        try{
            obsClient = ObsClientUtil.getObsClient();
            URL url = new URL(oldurl);
            HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
            length = httpconn.getContentLength();
            inputStream = url.openStream();
            log.info(length+ ",===================>>>>>"+oldurl);
            obsClient.putObject(bucketName, objectkey, inputStream);
        }catch(FileNotFoundException ee){
            log.error(oldurl+" 没有对应的文件内容");
        }catch(Exception e){
            log.error(oldurl+" 对应文件上传到obs上异常");
        }finally {
            try{
                ObsClientUtil.closeObsClient(obsClient);
                if(null!=inputStream)
                    inputStream.close();
            }catch (Exception e){
                log.error(" ===  关闭流异常:"+e.getMessage());
            }
        }
        return length;
    }

    /**
     * 上传网络流
     */
    public static int uploadInputStreamToObsClient(ObsClient obsClient,String bucketName,String objectkey, String oldurl) {
        InputStream inputStream = null;
        try{
            URL url = new URL(oldurl);
            HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
            long length = httpconn.getContentLength();
            log.info(length+ ",===================>>>>>"+oldurl);
            inputStream = url.openStream();
            obsClient.putObject(bucketName, objectkey, inputStream);
            return 1;
        }catch(FileNotFoundException ee){
            log.error(oldurl+" 没有对应的文件内容");
            return 0;
        }catch(Exception e){
            log.error(oldurl+" 对应文件上传到obs上异常");
            return 0;
        }finally {
            try{
                if(null!=inputStream)
                    inputStream.close();
            }catch (Exception e){
                log.error(" ===  关闭流异常:"+e.getMessage());
            }
        }
    }

    /**
     * 上传文件流
     */
    public static void uploadToObsClient(ObsClient obsClient,String bucketName,String objectkey, String localFileUrl) throws IOException{
        FileInputStream fis = new FileInputStream(new File(localFileUrl));
        obsClient.putObject(bucketName, objectkey, fis);

    }

    /**
     * 分段上传文件
     */
    public static void multipartUploadToObsClient(String bucketName,String objectkey, String localFileUrl) throws IOException{
        ObsClient obsClient = ObsClientUtil.getObsClient();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final File largeFile = new File(localFileUrl);
        // 初始化分段上传任务
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectkey);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);
        final String uploadId = result.getUploadId();
        // 每段上传100MB
        long partSize = 100 * 1024 * 1024l;
        long fileSize = largeFile.length();
        // 计算需要上传的段数
        long partCount = fileSize % partSize == 0 ? fileSize / partSize : fileSize / partSize + 1;
        final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<PartEtag>());
        // 执行并发上传段
        for (int i = 0; i < partCount; i++){
            // 分段在文件中的起始位置
            final long offset = i * partSize;
            // 分段大小
            final long currPartSize = (i + 1 == partCount) ? fileSize - offset : partSize;
            // 分段号
            final int partNumber = i + 1;
            executorService.execute(new Runnable(){
                @Override
                public void run(){
                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setObjectKey(objectkey);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setFile(largeFile);
                    uploadPartRequest.setPartSize(currPartSize);
                    uploadPartRequest.setOffset(offset);
                    uploadPartRequest.setPartNumber(partNumber);
                    UploadPartResult uploadPartResult;
                    try{
                        uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                        System.out.println("Part#" + partNumber + " done\n");
                        partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                    }catch (ObsException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        // 等待上传完成
        executorService.shutdown();
        while (!executorService.isTerminated()){
            try{
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        // 合并段
        Collections.sort(partEtags, new Comparator<PartEtag>(){
            @Override
            public int compare(PartEtag o1, PartEtag o2){
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, objectkey, uploadId, partEtags);
        obsClient.completeMultipartUpload(completeMultipartUploadRequest);
        ObsClientUtil.closeObsClient(obsClient);
    }

     /**
     * 获取文件输入流
     * @param bucketName
     * @param objectkey
     * @return
     */
    public static InputStream downloadObsClientFile(String bucketName,String objectkey){
        ObsClient obsClient = ObsClientUtil.getObsClient();
        ObsObject obsObject = obsClient.getObject(bucketName, objectkey);
        return obsObject.getObjectContent();
    }

    /**
     * 获取文件输入流
     * @param bucketName
     * @param objectkey
     * @return
     */
    public static InputStream downloadObsClientFile(ObsClient obsClient,String bucketName,String objectkey){
        ObsObject obsObject = obsClient.getObject(bucketName, objectkey);
        return obsObject.getObjectContent();
    }

    /**
     * 获取ObsClient上文件临时访问链接
     * @param objectkey
     * @return
     */
    public static String getObsClientPrivateUrl(String bucketName,String objectkey) {
        int expireSeconds = 3600 * 8;
        try {
            ObsClient obsClient = ObsClientUtil.getObsClient();
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            request.setBucketName(bucketName);
            request.setObjectKey(objectkey);
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            System.out.println("\t" + response.getSignedUrl());
            obsClient.close();
            return response.getSignedUrl();
        }catch (IOException e){
            return objectkey;
        }
    }

    /**
     * 获取ObsClient上文件临时访问链接
     * @param objectkey
     * @return
     */
    public static String getObsClientPrivateUrl(ObsClient obsClient,String bucketName,String objectkey) {
        int expireSeconds = 3600 * 8;
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName(bucketName);
        request.setObjectKey(objectkey);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        System.out.println("\t" + response.getSignedUrl());
        return response.getSignedUrl();
    }

    /**
     * 获取ObsClient上图片访问链接
     * @param objectkey
     * @return
     */
    public static String getObsClientPublicUrl(String bucketName,String objectkey){
        String url = "https://" + bucketName + ".obs.myhwclouds.com/" + objectkey;
        return url;
    }

    /**
     * 删除Obs對象
     * @param filePath
     */
    public static void deteleObsClientobject(ObsClient obsClient,String filePath,String bucketName){
        obsClient.deleteObject(bucketName, filePath);
    }

    /**
     * 遍历桶中对象
     * @param obsClient
     * @param bucketName
     */
    public static void showAllFiles(ObsClient obsClient,String bucketName){
        ObjectListing result = obsClient.listObjects(bucketName);
        for(ObsObject obsObject : result.getObjects()) {
            System.out.println("\t" + obsObject.getObjectKey());
            System.out.println("\t" + obsObject.getOwner());
        }
    }


}
