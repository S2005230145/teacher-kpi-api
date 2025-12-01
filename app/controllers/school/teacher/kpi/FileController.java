package controllers.school.teacher.kpi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import controllers.BaseAdminSecurityController;
import jakarta.inject.Inject;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FileController extends BaseAdminSecurityController {

    private static String path;

    private static final String osName = System.getProperty("os.name").toLowerCase();

    @Inject
    Config config;

    //获取文件信息
    public CompletionStage<Result> getFileList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            if (osName.contains("win")) {
                path=config.getString("fileUpload.windows");
            }else {
                path=config.getString("fileUpload.linux");
            }

            String folderPath=jsonNode.findPath("folderPath").asText();

            File folder=new File(path,folderPath);
            if (!folder.exists() || !folder.isDirectory()) {
                return okCustomJson(CODE40001,"文件夹不存在或不是目录");
            }
            File[] files = folder.listFiles();
            if(files==null) return okCustomJson(CODE40001,"该文件夹没有文件");

            List<Map<String,Object>> mpList=new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Arrays.stream(files).forEach(file -> {
                Map<String,Object> mp=new HashMap<>();
                mp.put("fileName",file.getName());
                mp.put("parentFileName",file.getParentFile().toString());
                mp.put("isDirectory",file.isDirectory());
                mp.put("isFile",file.isFile());
                mp.put("canRead",file.canRead());
                mp.put("canWrite:",file.canWrite());
                mp.put("canExecute",file.canExecute());
                mp.put("isHidden",file.isHidden());
                mp.put("lastModified",sdf.format(new Date(file.lastModified())));
                mp.put("size",file.length()+ " bytes");
                mpList.add(mp);
            });

            ObjectNode result = Json.newObject();
            result.put(CODE,CODE200);
            result.set("list",Json.toJson(mpList));
            result.put("totalSpace",folder.getTotalSpace()+ " bytes");
            result.put("usableSpace",folder.getUsableSpace()+ " bytes");
            result.put("freeSpace",folder.getFreeSpace()+ " bytes");
            return ok(result);
        });
    }

    //添加文件
    public CompletionStage<Result> addFile(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            if (osName.contains("win")) {
                path=config.getString("fileUpload.windows");
            }else {
                path=config.getString("fileUpload.linux");
            }

            Http.MultipartFormData<play.libs.Files.TemporaryFile> multipartFormData = request.body().asMultipartFormData();
            List<Http.MultipartFormData.FilePart<Files.TemporaryFile>> files = multipartFormData.getFiles();
            String folderPath = multipartFormData.asFormUrlEncoded().containsKey("folderPath")?multipartFormData.asFormUrlEncoded().get("folderPath")[0]:null;
            if(folderPath==null) return okCustomJson(CODE40001,"没有文件路径");

            File folder=new File(path,folderPath);
            if (!folder.exists()) {
                return okCustomJson(CODE40001,"该目录不存在");
            }
            files.forEach(file->{
                File destination = new File(folder, file.getFilename());
                file.getRef().copyTo(destination.toPath(), false);
                this.setFilePermissions777(destination.toPath());
            });

            return okCustomJson(CODE200,"文件上传成功");
        });
    }

    //工具
    private void setFilePermissions777(Path filePath) {
        try {
            // 检查文件是否存在
            if (!java.nio.file.Files.exists(filePath)) {
                System.out.println("文件设置失败");
                return;
            }

            // 设置777权限
            Set<PosixFilePermission> permissions = new HashSet<>();

            // 所有者权限：读、写、执行
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);

            // 组权限：读、写、执行
            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.GROUP_WRITE);
            permissions.add(PosixFilePermission.GROUP_EXECUTE);

            // 其他用户权限：读、写、执行
            permissions.add(PosixFilePermission.OTHERS_READ);
            permissions.add(PosixFilePermission.OTHERS_WRITE);
            permissions.add(PosixFilePermission.OTHERS_EXECUTE);

            java.nio.file.Files.setPosixFilePermissions(filePath, permissions);

        } catch (UnsupportedOperationException | IOException e) {
            System.out.println(e);
        }
    }
}
