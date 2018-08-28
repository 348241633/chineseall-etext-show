package com.common.util;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Created by lego-jspx01 on 2018/8/24.
 */
public class StorageUtil {
    private static final transient Log log = LogFactory.getLog(StorageUtil.class);
    private static StorageUtil INSTANCE = null;
    private static final String blobEndpoint = "obs.cn-north-1.myhwclouds.com";
    private static final String queueEndpoint = "obs.cn-north-1.myhwclouds.com";
    private static final String tableEndpoint = "obs.cn-north-1.myhwclouds.com";
    private static final String accountName = "";
    private static final String accountKey = "";
    private static final String endpointsProtocol = "http";

    private StorageUtil(){}
    public static StorageUtil getInstance(){
        if(INSTANCE == null) INSTANCE = new StorageUtil();
        return INSTANCE;
    }

    public static CloudBlobContainer getBlobContainer(String containerName){
        try {
            String blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                            + "BlobEndpoint=%s;"
                            + "QueueEndpoint=%s;"
                            + "TableEndpoint=%s;"
                            + "AccountName=%s;"
                            + "AccountKey=%s",
                    endpointsProtocol, blobEndpoint,
                    queueEndpoint, tableEndpoint,
                    accountName, accountKey);

            CloudStorageAccount account = CloudStorageAccount.parse(blobStorageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference(containerName);
            // Create a permissions object.
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
            // Include public access in the permissions object.
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
            // Set the permissions on the container.
            container.uploadPermissions(containerPermissions);
            container.createIfNotExists();
            return container;
        }catch(Exception e){
            return null;
        }
    }


    /**
     * 上传本地文件
     * @param tempFile
     * @param objectkey
     * @return
     * @throws IOException
     */
    public static void uploadFileToStorage(CloudBlobContainer container,String objectkey,String tempFile)  throws Exception{
        CloudBlockBlob blob = container.getBlockBlobReference(objectkey);
        blob.uploadFromFile(tempFile);
    }

    /**
     * 上传文件
     * @param tempFile
     * @param objectkey
     * @return
     * @throws IOException
     */
    public static void uploadFileToStorage(CloudBlobContainer container,String objectkey,File tempFile,long filesize)  throws Exception{
        CloudBlockBlob blob = container.getBlockBlobReference(objectkey);
        InputStream io = new FileInputStream(tempFile);
        blob.upload(io, filesize);
    }



    /**
     * 下载到本地文件
     * @param container
     * @param objectkey
     * @return
     */
    public static void downloadObsClientFile(CloudBlobContainer container,String objectkey,String filePath)throws Exception{
        CloudBlockBlob blob = container.getBlockBlobReference(objectkey);
        blob.downloadToFile(filePath);
    }

    /**
     * 获取文件输入流
     * @param container
     * @param objectkey
     * @return
     */
    public static void downloadObsClientFile(CloudBlobContainer container,String objectkey,OutputStream outStream)throws Exception{
        CloudBlockBlob blob = container.getBlockBlobReference(objectkey);
        blob.download(outStream);
    }


    /**
     * 获取到临时访问url
     * @param container
     * @param objectkey
     * @return
     */
    public static String getBlobUrl(CloudBlobContainer container,String objectkey){
        try{
            SharedAccessBlobPolicy policy = new SharedAccessBlobPolicy();
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            calendar.setTime(new Date());
            policy.setSharedAccessStartTime(calendar.getTime());
            calendar.add(Calendar.HOUR, 8);
            policy.setSharedAccessExpiryTime(calendar.getTime());
            // SAS grants READ access privileges
            policy.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ));
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
            // Private blob-container with no access for anonymous users
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.OFF);
            container.uploadPermissions(containerPermissions);
            String sas = container.generateSharedAccessSignature(policy,null);
            CloudBlockBlob blob = container.getBlockBlobReference(objectkey);
            String blobUri = blob.getUri().toString();
            return blobUri + "?"+ sas;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
