package models.school.kpi.export;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class TeacherPerformancePdfExporter {

    private PdfFont chineseFont;

    public byte[] exportToPdf(TeacherPerformanceExportData data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 设置中文字体
        setupChineseFont();
        document.setFont(chineseFont);

        // 添加文档内容
        addHeader(document, data);
        addTableHeader(document);
        addMoralEvaluation(document, data.getMoralEvaluation());
        addTeachingPerformance(document, data.getTeachingPerformance());
        addProfessionalDevelopment(document, data.getProfessionalDevelopment());
        addSpecialAssignments(document, data.getSpecialAssignments());
        addSummary(document, data);

        document.close();
        return baos.toByteArray();
    }

    /**
     * 初始化中文字体
     */
    private void setupChineseFont() throws IOException {
        try {
            // 方法1: 使用系统字体
            String systemFontPath = "C:/Windows/Fonts/simhei.ttf"; // 黑体
            chineseFont = PdfFontFactory.createFont(systemFontPath, PdfEncodings.IDENTITY_H,PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED, true);
        } catch (Exception e1) {
            try {
                // 方法2: 使用内置字体
                chineseFont = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED, true);
            } catch (Exception e2) {
                try {
                    // 方法3: 使用宋体
                    chineseFont = PdfFontFactory.createFont("SimSun", PdfEncodings.IDENTITY_H,PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED, true);
                } catch (Exception e3) {
                    // 方法4: 使用默认字体（可能不支持中文）
                    System.out.println("无法加载中文字体，使用默认字体");
                    chineseFont = PdfFontFactory.createFont();
                }
            }
        }
    }

    private void addHeader(Document document, TeacherPerformanceExportData data) {
        // 标题
        Paragraph title = new Paragraph("福清市中小学、中职学校教师绩效考核要点")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16)
                .setBold()
                .setMarginBottom(10);



        document.add(title);
    }

    private void addTableHeader(Document document){

    }

    private void addMoralEvaluation(Document document, TeacherPerformanceExportData.MoralEvaluation moral) {
        Paragraph sectionTitle = new Paragraph("一、师德师风（定性评价）")
                .setBold()
                .setFontSize(12)
                .setMarginBottom(10);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 3}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        // 表头
        table.addHeaderCell(createHeaderCell("评价内容"));
        table.addHeaderCell(createHeaderCell("评价标准"));
        table.addHeaderCell(createHeaderCell("评价结果"));

        // 数据行
        table.addCell(createCell("遵守《新时代中小学教师职业行为十项准则》"));
        table.addCell(createCell("是否合格，不设分值"));
        table.addCell(createCell(moral.isFollowTenGuidelines() ? "合格" : "不合格"));

        table.addCell(createCell("无违反职业道德行为"));
        table.addCell(createCell("是否合格，不设分值"));
        table.addCell(createCell(moral.isNoViolation() ? "合格" : "不合格"));

        table.addCell(createCell("无《福州市中小学教师职业行为负面清单》行为"));
        table.addCell(createCell("是否合格，不设分值"));
        table.addCell(createCell(moral.isNoNegativeBehavior() ? "合格" : "不合格"));

        table.addCell(createCell("总体评价"));
        table.addCell(createCell("优秀、合格、不合格"));
        table.addCell(createCell(moral.getEvaluationResult()));

        document.add(sectionTitle);
        document.add(table);

        if (moral.getComments() != null) {
            Paragraph comments = new Paragraph("评语：" + moral.getComments())
                    .setFontSize(10)
                    .setMarginBottom(15);
            document.add(comments);
        }
    }

    private void addTeachingPerformance(Document document,
                                        TeacherPerformanceExportData.TeachingPerformance teaching) {
        Paragraph sectionTitle = new Paragraph("二、教育教学业绩（30-45分）")
                .setBold()
                .setFontSize(12)
                .setMarginBottom(10);

        document.add(sectionTitle);

        // 主要业绩表格
        addTeachingMainPerformance(document, teaching);

        // 教育教学常规表格
        addTeachingRoutine(document, teaching.getTeachingRoutine());
    }

    private void addTeachingMainPerformance(Document document,
                                            TeacherPerformanceExportData.TeachingPerformance teaching) {
        Paragraph subTitle = new Paragraph("教育教学主要业绩")
                .setBold()
                .setFontSize(11)
                .setMarginBottom(5);

        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 3, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        table.addHeaderCell(createHeaderCell("评价要素"));
        table.addHeaderCell(createHeaderCell("评价内容"));
        table.addHeaderCell(createHeaderCell("得分"));

        addTeachingRow(table, "学生学业发展情况",
                "任教班级学生学业发展情况，中高考、合格性考试等",
                teaching.getStudentAcademicDevelopment());

        addTeachingRow(table, "示范引领",
                "履行师徒协议，承担各级学科研训组、名师工作室工作",
                teaching.getDemonstrationLeadership());

        addTeachingRow(table, "班级获奖",
                "班（团、队）会活动、单项竞赛获奖、综合性荣誉表彰",
                teaching.getClassAwards());

        addTeachingRow(table, "指导学生获奖",
                "指导学生参加学科竞赛、创新大赛、体育联赛等获奖",
                teaching.getStudentGuidanceAwards());

        document.add(subTitle);
        document.add(table);
    }

    private void addTeachingRoutine(Document document,
                                    TeacherPerformanceExportData.TeachingPerformance.TeachingRoutine routine) {
        Paragraph subTitle = new Paragraph("教育教学常规（30-40分）")
                .setBold()
                .setFontSize(11)
                .setMarginBottom(5);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 4, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        table.addHeaderCell(createHeaderCell("评价要素"));
        table.addHeaderCell(createHeaderCell("评价内容"));
        table.addHeaderCell(createHeaderCell("得分"));

        addRoutineRow(table, "出勤情况",
                "病假、事假、迟到、旷课、政治学习、教研活动等出勤情况",
                routine.getAttendance());

        addRoutineRow(table, "课时工作量",
                "完成标准课时工作量、代课、特殊课时等",
                routine.getTeachingWorkload());

        addRoutineRow(table, "班主任等工作",
                "担任班主任、年段长、教研组长等工作",
                routine.getClassTeacherWork());

        addRoutineRow(table, "课堂教学",
                "制定教学计划、组织教学、课堂管理、教学实效等",
                routine.getClassroomTeaching());

        document.add(subTitle);
        document.add(table);
    }

    private void addProfessionalDevelopment(Document document,
                                            TeacherPerformanceExportData.ProfessionalDevelopment development) {
        Paragraph sectionTitle = new Paragraph("三、个人专业发展（5-15分）")
                .setBold()
                .setFontSize(12)
                .setMarginBottom(10);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 4, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        table.addHeaderCell(createHeaderCell("评价要素"));
        table.addHeaderCell(createHeaderCell("评价内容"));
        table.addHeaderCell(createHeaderCell("得分"));

        addDevelopmentRow(table, "承担公开教学活动",
                "开设各级学科示范课、观摩课及专题讲座",
                development.getPublicTeachingActivities());

        addDevelopmentRow(table, "撰写教育教学论文",
                "撰写学科、德育、党建、管理论文获奖或收入汇编",
                development.getTeachingPapers());

        addDevelopmentRow(table, "参与教育教学研究",
                "参与各级课题研究、专题教研活动、项目式研究",
                development.getTeachingResearch());

        addDevelopmentRow(table, "个人获奖",
                "获得各级教育教学专项表彰、综合性荣誉表彰",
                development.getPersonalAwards());

        document.add(sectionTitle);
        document.add(table);
    }

    private void addSpecialAssignments(Document document,
                                       TeacherPerformanceExportData.SpecialAssignments assignments) {
        Paragraph sectionTitle = new Paragraph("四、承担急难险重工作（5-10分）")
                .setBold()
                .setFontSize(12)
                .setMarginBottom(10);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 4, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        table.addHeaderCell(createHeaderCell("评价要素"));
        table.addHeaderCell(createHeaderCell("评价内容"));
        table.addHeaderCell(createHeaderCell("得分"));

        addAssignmentRow(table, "承担学校行政工作",
                "承担学校党建、教研、工会、后勤、安全、宣传、财务等工作",
                assignments.getAdministrativeWork());

        addAssignmentRow(table, "承担学校临时性重要任务",
                "承担创建、评估、迎检、校庆、志愿服务等工作",
                assignments.getTemporaryImportantTasks());

        document.add(sectionTitle);
        document.add(table);
    }

    private void addSummary(Document document, TeacherPerformanceExportData data) {
        Paragraph sectionTitle = new Paragraph("考核总结")
                .setBold()
                .setFontSize(12)
                .setMarginBottom(10);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .setWidth(UnitValue.createPercentValue(60))
                .setMarginBottom(15);

        table.addCell(createCell("总分"));
        table.addCell(createCell(data.getTotalScore().toString()));

        table.addCell(createCell("考核等级"));
        table.addCell(createCell(data.getEvaluationLevel()));

        document.add(sectionTitle);
        document.add(table);

        // 添加签名区域
        Paragraph signature = new Paragraph()
                .add("\n\n")
                .add("考核人签名：___________________")
                .add("    日期：___________________")
                .setFontSize(10);

        document.add(signature);
    }

    // 辅助方法
    private Cell createHeaderCell(String text) {
        return new Cell().add(new Paragraph(text).setBold()).setBackgroundColor(new DeviceGray(0.9f));
    }

    private Cell createCell(String text) {
        return new Cell().add(new Paragraph(text != null ? text : ""));
    }

    private void addTeachingRow(Table table, String element, String content, BigDecimal score) {
        table.addCell(createCell(element));
        table.addCell(createCell(content));
        table.addCell(createCell(score != null ? score.toString() : "0"));
    }

    private void addRoutineRow(Table table, String element, String content, BigDecimal score) {
        table.addCell(createCell(element));
        table.addCell(createCell(content));
        table.addCell(createCell(score != null ? score.toString() : "0"));
    }

    private void addDevelopmentRow(Table table, String element, String content, BigDecimal score) {
        table.addCell(createCell(element));
        table.addCell(createCell(content));
        table.addCell(createCell(score != null ? score.toString() : "0"));
    }

    private void addAssignmentRow(Table table, String element, String content, BigDecimal score) {
        table.addCell(createCell(element));
        table.addCell(createCell(content));
        table.addCell(createCell(score != null ? score.toString() : "0"));
    }
}
