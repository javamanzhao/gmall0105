package com.zzhao.gmall.manage.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 * @date 2019/11/5 0005上午 11:23
 */
public class PmsUploadUtil {

    private static final String CONF_FILENAME = Thread.currentThread().getContextClassLoader().getResource("tracker.conf").getPath();

    private static TrackerClient trackerClient;

    static {
        try {
            ClientGlobal.init(CONF_FILENAME);
            TrackerGroup trackerGroup = ClientGlobal.g_tracker_group;
            trackerClient = new TrackerClient(trackerGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String uploadImage(MultipartFile multipartFile) {
        String path = "http://192.168.245.129";
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient storageClient = null;
        try {
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            storageClient = new StorageClient(trackerServer, storageServer);
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String exName = originalFilename.substring(index + 1);
            String[] uploadInfos = storageClient.upload_file(bytes, exName, null);
            for (String uploadInfo : uploadInfos) {
                path += "/" + uploadInfo;
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (storageServer != null) {
                try {
                    storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (storageClient != null) {
                try {
                    storageClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }
}
