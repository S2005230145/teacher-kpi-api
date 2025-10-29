package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;

@Data
@Entity
@Table(name = "tk_v1_role")
public class Role extends Model {
    // 唯一标识(最终ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "nick_name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String nickName;
}
