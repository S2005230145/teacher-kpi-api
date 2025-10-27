package models.school.kpi.v2.entity.moral;

import lombok.Getter;

/**
 * 师德师风等级枚举
 */
@Getter
public enum MoralEthicsLevel {
    EXCELLENT("优"),
    QUALIFIED("合格"),
    UNQUALIFIED("不合格");

    private final String description;

    MoralEthicsLevel(String description) {
        this.description = description;
    }

}
