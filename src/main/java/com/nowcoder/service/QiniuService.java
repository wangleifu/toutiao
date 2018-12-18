package com.nowcoder.service;

import com.google.gson.Gson;
import com.nowcoder.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 七牛云存储空间集成
 *
 * @author wangleifu
 * @create 2018-12-18 14:25
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    /**
     * 构造一个带指定Zone对象的配置类
     */
    Configuration cfg = new Configuration(Zone.zone0());
    /**
     * ...其他参数参考类注释
     */
    UploadManager uploadManager = new UploadManager(cfg);
    /**
     * 生成上传凭证，然后准备上传
     */
    String accessKey = "4c6BlpLL" +
            "wnXU6lLkWZXXaoyU6JTdMjZpI1D4GKUw";
    String secretKey = "2jY1W_qrxwOWrZz7uStTeDNWxglzc15E9nG4YJ-y";
    String bucket = "nowcoder";

    private String getUpToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.uploadToken(bucket);
    }

    /**
     * 保存图片到七牛云服务器
     * @param file  上传上来的图片文件
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {
        try {
            // 找的后缀名前的“.”
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            // 找到后缀名
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 判断后缀名是否符合要求
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;


            // 调用put方法上传
            Response response = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            if (putRet.key == null)
            {
                logger.error("七牛云上传异常：" + putRet.toString());
                return null;
            }
            return ToutiaoUtil.QINIU_DOMAIN_PREFIX + putRet.key;
        } catch (QiniuException e) {
            logger.error("七牛异常" + e.getMessage());
            return null;
        }
    }
}
