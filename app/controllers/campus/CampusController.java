package controllers.campus;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Transaction;
import models.campus.Campus;
import models.user.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CampusController extends Controller {

    /**
     * 获取所有校区信息
     */
    public CompletionStage<Result> getAllCampus(){
        return CompletableFuture.supplyAsync(() -> {
            List<Campus> campusList = Campus.find.all();
            return ok(Json.toJson(campusList));
        });
    }

    /**
     * 新增校区
     */
    public CompletionStage<Result> addCampus(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Campus campus = Json.fromJson(jsonNode, Campus.class);
            try(Transaction transaction = User.find.db().beginTransaction()){
                campus.save();
                transaction.commit();
            }catch (Exception e){
                return ok("添加失败: "+e);
            }
            return ok("添加成功");
        });
    }

    /**
     * 删除校区
     */
    public CompletionStage<Result> deleteCampus(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Campus campus = Json.fromJson(jsonNode, Campus.class);
            try(Transaction transaction = User.find.db().beginTransaction()){
                campus.delete();
                transaction.commit();
            }catch (Exception e){
                return ok("删除失败: "+e);
            }
            return ok("删除成功");
        });
    }

    /**
     * 修改校区
     */
    public CompletionStage<Result> updateCampus(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Campus campus = Json.fromJson(jsonNode, Campus.class);
            try(Transaction transaction = User.find.db().beginTransaction()){
                campus.update();
                transaction.commit();
            }catch (Exception e){
                return ok("修改失败: "+e);
            }
            return ok("修改成功");
        });
    }


}
