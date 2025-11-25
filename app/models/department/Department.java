package models.department;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.campus.Campus;
import myannotation.EscapeHtmlSerializer;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_department")
public class Department extends Model {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date updateTime;

    @Transient
    public Campus campus;

    // JPA查询器
    public static Finder<Long, Department> find = new Finder<>(Department.class);
}