package models.system;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 配置
 */
@Entity
@Table(name = "v1_system_config_template")
public class ParamTemplate extends Model implements Serializable {
    private static final long serialVersionUID = 1885841224604019893L;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "config_key")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String key;

    @Column(name = "config_value")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String value;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "tab_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tabName;

    @Column(name = "enable")
    public boolean enable;

    @Column(name = "content_type")
    public int contentType;

    @Column(name = "is_encrypt")
    public boolean isEncrypt;

    @Column(name = "update_time")
    public long updateTime;

    public static Finder<Long, ParamTemplate> find = new Finder<>(ParamTemplate.class);

    public void setId(long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getNote() {
        return note;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
