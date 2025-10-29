package models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.EscapeHtmlSerializer;

@Data
@Entity
@Table(name = "tk_v1_user")
public class User extends Model {
    // 唯一标识(最终ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "username")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "password")
    @JsonIgnore
    public String password;

    @Column(name = "role_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public Long roleId;

    public static Finder<Long, User> find =
            new Finder<>(User.class);
}
