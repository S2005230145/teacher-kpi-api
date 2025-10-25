package Enum.teacher;

public enum AssessmentGrade {
    EXCELLENT("优秀"),
    GOOD("良好"),
    QUALIFIED("合格"),
    UNQUALIFIED("不合格");

    private final String description;

    AssessmentGrade(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
