package Enum.teacher;

/**
 * 师德师风等级枚举
 */
public enum MoralEthicsLevel {
    EXCELLENT("优"),
    QUALIFIED("合格"),
    UNQUALIFIED("不合格");

    private final String description;

    MoralEthicsLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
