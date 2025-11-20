package controllers.department;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.ExpressionList;
import io.ebean.Query;
import io.ebean.Transaction;
import models.campus.Campus;
import models.department.Department;
import models.user.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DepartmentController extends Controller {

    /*
        @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "department_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String departmentName;

    @Column(name = "department_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String departmentCode;

    @JoinColumn(name = "campus_id")
    public Long campusId;

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;

    @Column(name = "create_time")
    public Date createTime;

    @Column(name = "update_time")
    public Date updateTime;
     */

    /**
     * 获取所有部门信息
     */
    public CompletionStage<Result> getAllDepartment(){
        return CompletableFuture.supplyAsync(() -> {
            List<Department> departmentList = Department.find.query().where().findList();
            ObjectNode node = play.libs.Json.newObject();
            node.put("code", 200);
            node.set("list", Json.toJson(departmentList));
            return ok(Json.toJson(node));
        });
    }

    /**
     * 获取所有部门信息（搜索）
     */
    public CompletionStage<Result> getAllDepartmentBySearch(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        Department department = Json.fromJson(jsonNode, Department.class);
        return CompletableFuture.supplyAsync(() -> {
            Query<Department> query = Department.find.query();

            if(department != null){
                // 添加搜索条件
                if (department.departmentName != null && !department.departmentName.isEmpty()) {
                    query.where().icontains("departmentName", department.departmentName);
                }
                if (department.departmentCode != null && !department.departmentCode.isEmpty()) {
                    query.where().icontains("departmentCode", department.departmentCode);
                }
                if (department.description != null && !department.description.isEmpty()) {
                    query.where().icontains("description", department.description);
                }
            }
            List<Department> departmentList = query.findList();
            departmentList.forEach(s1 -> {
                s1.campus = Campus.find.byId(s1.campusId);
            });
            ObjectNode result = play.libs.Json.newObject();
            result.put("code", 200);
            result.set("list", Json.toJson(departmentList));
            return ok(result);
        });
    }




    /**
     * 新增部门
     */
    public CompletionStage<Result> addDepartment(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Department department = Json.fromJson(jsonNode, Department.class);
            try(Transaction transaction = Department.find.db().beginTransaction()){
                department.save();
                transaction.commit();
            }catch (Exception e){
                return ok("添加失败: "+e);
            }
            return ok("添加成功");
        });
    }

    /**
     * 删除部门
     */
    public CompletionStage<Result> deleteDepartment(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
           Department department = Json.fromJson(jsonNode, Department.class);
           try(Transaction transaction = Department.find.db().beginTransaction()){
               department.delete();
               transaction.commit();
           }catch (Exception e){
               return ok("删除失败: "+e);
           }
           return ok("删除成功");
        });
    }

    /**
     * 修改部门信息
     */
    public CompletionStage<Result> updateDepartment(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            Department department = Json.fromJson(jsonNode, Department.class);
            try(Transaction transaction = Department.find.db().beginTransaction()){
                department.update();
                transaction.commit();
            }catch (Exception e){
                return ok("修改失败: "+e);
            }
            return ok("修改成功");
        });
    }




}
