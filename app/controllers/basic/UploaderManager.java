package controllers.basic;

import ch.qos.logback.core.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import controllers.BaseAdminController;
import controllers.BaseSecurityController;
import models.admin.ShopAdmin;
import models.user.User;
import myannotation.Json1MParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import play.Logger;
import play.libs.Files;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class UploaderManager extends BaseAdminController {

    Logger.ALogger logger = Logger.of(UploaderManager.class);
    public static final String BUCKET = "files-1309392630";
    public static String IMG_URL_PREFIX = "https://files-1309392630.cos.ap-guangzhou.myqcloud.com/";
    public static final int THUMB_WIDTH = 400;
    public static final int THUMB_HEIGHT = 400;
    private static final int AVATAR_WEIGHT = 80;
    private static final int AVATAR_HEIGHT = 80;
    private static final double THUMB_QUALITY = 0.8;
    private static final String BASE_IMG_PREFIX = "data:image/jpeg;base64,";
    private static final String RENAME_FILE_PREFIX = "upload_";
    private static final String TEMP_FOLDER = "/tmp/upload/";


    /**
     * @api {POST} /v2/tk/upload/ 上传
     * @apiName upload
     * @apiGroup System
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Success 200){string} url 保存的地址
     * @apiSuccess (Error 40003) {int}code 40003 上传失败
     */
    public CompletionStage<Result> upload(Http.Request request) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> uploadFile = body.getFile("file");
        return CompletableFuture.supplyAsync(() -> {
//            ShopAdmin admin = businessUtils.getUserIdByAuthToken2(request);
//            if (null == admin) return unauth403();
            try {
                if (null == uploadFile) return okCustomJson(CODE500, "上传文件失败，请重试");
                String fileName = uploadFile.getFilename();
                Files.TemporaryFile file = uploadFile.getRef();
                String today = dateUtils.getToday();
                String targetFileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);

                String key = "org" + "/" + today + "/" + targetFileName;
                mkTempDirIfNotExist();
                String destPath = TEMP_FOLDER + targetFileName;
                file.copyTo(Paths.get(destPath), true);
                return uploadToOss(destPath, key);
            } catch (Exception e) {
                return okCustomJson(40003, "reason:" + e.getMessage());
            }
        });
    }

    private void mkTempDirIfNotExist() {
        File tempFile = new File(TEMP_FOLDER);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
    }

    /**
     * @api {POST} /v2/tk/upload2/ 上传
     * @apiName upload
     * @apiGroup System
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Success 200){string} url 保存的地址
     * @apiSuccess (Error 40003) {int}code 40003 上传失败
     */
    public CompletionStage<Result> upload2(Http.Request request) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> uploadFile = body.getFile("file");
        return CompletableFuture.supplyAsync(() -> {
            User admin = businessUtils.getUserIdByAuthToken2(request);
            if (null == admin) return unauth403();
            try {
                if (null == uploadFile) return okCustomJson(CODE500, "上传文件失败，请重试");
                String fileName = uploadFile.getFilename();
                Files.TemporaryFile file = uploadFile.getRef();
                String today = dateUtils.getToday();
                String key = 1 + "/" + today + "/" + fileName;
                mkTempDirIfNotExist();
                String destPath = TEMP_FOLDER + fileName;
                file.copyTo(Paths.get(destPath), true);
                return uploadToOss(destPath, key);
            } catch (Exception e) {
                return okCustomJson(40003, "reason:" + e.getMessage());
            }
        });
    }


    public Result uploadToOss(String localFilePath, String key) {
        // 1 传入获取到的临时密钥 (tmpSecretId, tmpSecretKey, sessionToken)
        String tmpSecretId = businessUtils.getCOSSecretId();
        String tmpSecretKey = businessUtils.getCOSSecretKey();
        COSCredentials cred = new BasicCOSCredentials(tmpSecretId, tmpSecretKey);
        // 2 设置 bucket 的地域
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分
        //COS_REGION 参数：配置成存储桶 bucket 的实际地域，例如 ap-beijing，更多 COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        // 指定要上传的文件
        File localFile = new File(localFilePath);
        // 指定文件将要存放的存储桶
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
//        String etag = putObjectResult.getETag();
        ObjectNode node = Json.newObject();
        node.put("code", 200);
        node.put("url", IMG_URL_PREFIX + key);
        return ok(node);
    }

    /**
     * @api {POST}   /v2/tk/upload_base64/  03上传base64
     * @apiName uploadBase64
     * @apiGroup System
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Success 200){string} imgUrl 上传返回的地址
     * @apiSuccess (Error 40003) {int}code 40003 上传失败
     */
    @BodyParser.Of(Json1MParser.class)
    public CompletionStage<Result> uploadBase64(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            String bytes = requestNode.findPath("data").asText();
            int index = bytes.indexOf(BASE_IMG_PREFIX);
            if (index >= 0) {
                String sub = bytes.substring(BASE_IMG_PREFIX.length());
                byte[] decoderBytes = Base64.getDecoder().decode(sub);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoderBytes);
                String key = "poster_" + System.currentTimeMillis() + ".jpeg";
                String imgUrl = uploadToOss(byteArrayInputStream, key);
                ObjectNode node = Json.newObject();
                node.put("code", 200);
                node.put("imgUrl", imgUrl);
                return ok(node);
            }
            return okCustomJson(CODE500, "上传发生异常，请稍后再试");
        });
    }

    public String uploadToOss(ByteArrayInputStream buffer, String key) {
        String accessId = businessUtils.getCOSSecretId();
        String secretKey = businessUtils.getCOSSecretKey();
        COSCredentials cred = new BasicCOSCredentials(accessId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(10);
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        objectMetadata.setContentType("image/png");
        com.qcloud.cos.model.PutObjectRequest putObjectRequest =
                new com.qcloud.cos.model.PutObjectRequest(BUCKET, key, buffer, objectMetadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        try {
            cosclient.putObject(putObjectRequest);
            return IMG_URL_PREFIX + key;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            cosclient.shutdown();
        }
        return "";
    }

}
