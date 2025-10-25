package Enum.teacher;

public enum PlanStatus {
    DRAFT("草稿"),
    ACTIVE("进行中"),
    COMPLETED("已完成"),
    ARCHIVED("已归档");

    private final String description;

    PlanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
