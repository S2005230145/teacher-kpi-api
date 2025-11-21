package controllers.campus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.DB;
import io.ebean.Transaction;
import models.campus.Campus;
import models.department.Department;
import models.user.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
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
            ObjectNode node = play.libs.Json.newObject();
            node.put("code", 200);
            node.set("list", Json.toJson(campusList));
            return ok(Json.toJson(node));
        });
    }

    /**
     * 新增校区
     */
    public CompletionStage<Result> addCampus(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Campus campus = Json.fromJson(jsonNode, Campus.class);
            try(Transaction transaction = DB.beginTransaction()){
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
            //查询该校区的所有部门
            List<Department> departmentList = Department.find.query().where().eq("campusId", campus.getId()).findList();
            //查询该校区的所有的老师
            List<User> userList = new ArrayList<>();
            for(Department department : departmentList){
                List<User> users = User.find.query().where().eq("departmentId", department.getId()).findList();
                userList.addAll(users); // 添加所有用户到列表中
            }
            try(Transaction transaction = DB.beginTransaction()){
                // 先删除教师
                for(User user : userList) {
                    user.delete();
                }
                // 再删除部门
                for(Department department : departmentList) {
                    department.delete();
                }
                // 最后删除校区
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
            try(Transaction transaction = DB.beginTransaction()){
                campus.update();
                transaction.commit();
            }catch (Exception e){
                return ok("修改失败: "+e);
            }
            return ok("修改成功");
        });
    }


}
