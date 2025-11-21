package models.campus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_campus")
public class Campus extends Model {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id; // 校区主键ID，自增长

    @Column(name = "campus_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String campusName; // 校区名称，如：北京总部校区、上海分校

    @Column(name = "address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String address; // 校区详细地址

    @Column(name = "phone")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phone; // 校区联系电话

    @Column(name = "principal")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String principal; // 校区负责人姓名

    @Column(name = "capacity")
    public Integer capacity; // 校区可容纳学生人数

    @Column(name = "establish_date")
    public Date establishDate; // 校区成立日期

    @Column(name = "status")
    public Integer status; // 校区状态：1-正常运营，2-暂停运营，3-已关闭

    @Column(name = "create_time")
    public Date createTime; // 记录创建时间

    @Column(name = "update_time")
    public Date updateTime; // 记录最后更新时间

    // JPA查询器
    public static Finder<Long, Campus> find = new Finder<>(Campus.class);
}