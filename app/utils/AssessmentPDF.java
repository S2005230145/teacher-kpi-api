package utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import models.school.kpi.v3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AssessmentPDF {

    private static PdfFont chineseFont;

    public static byte[] exportToPdf() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 设置中文字体
        setupChineseFont();
        document.setFont(chineseFont);
        document.setFontSize(11);

        // 添加文档内容
        addTitle(document);
        addTableTop(document);
        addItem1(document);
        addItem2(document);

        document.add(new AreaBreak());

        addTitle(document);
        addTableTop2(document);
        addItem3(document);
        addItem4(document);
        addItem5(document);

        document.close();
        return baos.toByteArray();
    }

    public static byte[] v3ExportToPdf(Long userId,Long kpiId) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 设置中文字体
        setupChineseFont();
        document.setFont(chineseFont);
        document.setFontSize(9);

        // 添加文档内容
        //第一页
        v3AddTitle(document,kpiId);
        addTableTop(document);
        List<Indicator> indicatorList = Indicator.find.query().where().eq("kpi_id",kpiId).findList();
        List<Indicator> indicatorListFirst=indicatorList.subList(0,2);//取前两个指标
        List<Element> elementList = Element.find.query().where().in("indicator_id", indicatorList.stream().map(Indicator::getId).toList()).findList();
        List<Content> contentList = Content.find.query().where().in("element_id",elementList.stream().map(Element::getId).toList()).findList();
        List<TeacherElementScore> tes = TeacherElementScore.find.query().where().eq("user_id", userId).findList();

        v3AddItem1(document,indicatorListFirst,elementList,contentList,tes);

        document.add(new AreaBreak());

        //第二页
        v3AddTitle(document,kpiId);
        addTableTop2(document);
        List<Indicator> indicatorListSecond=indicatorList.subList(2,indicatorList.size());

        v3AddItem1(document,indicatorListSecond,elementList,contentList,tes);

        Double score = TeacherKPIScore.find.query().where().eq("user_id", userId).eq("kpi_id", kpiId).setMaxRows(1).findOne().getScore();
        addTotal(document,score);

        document.close();
        return baos.toByteArray();
    }
    /**
     * 初始化中文字体
     */
    private static void setupChineseFont() throws IOException {
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

    private static void addTitle(Document document) {
        // 标题
        Paragraph title = new Paragraph("福清市中小学、中职学校教师绩效考核要点")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setBold()
                .setMarginBottom(5);


        document.add(title);
    }
    private static void v3AddTitle(Document document,Long kpiId) {
        KPI kpi = KPI.find.query().where().eq("id", kpiId).setMaxRows(1).findOne();
        if(kpi==null){
            System.out.println("kpi为null");
            return;
        }
        // 标题
        Paragraph title = new Paragraph(kpi.getTitle())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setBold()
                .setMarginBottom(5);


        document.add(title);
    }

    private static void addTableTop(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("评价指标",1,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评价要素",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评 价 内 容",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评 价 标 准",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("得 分\n（100分）",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell(1,1).add(WordVerticalText.createWordVerticalParagraph("一级   二级   三级",chineseFont,4)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(6));
        document.add(table);
    }

    //1
    private static void addItem1(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("师德\n师风\n\n（优、合格、不合格）",3,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",1,2).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("是否合格，不设置分值",3,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,3,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCellWithIndent("2.未存在教育部《中小学教师违反职业道德行为处理办法(2018年修订)》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",1,2).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("3.未存在《福州市中小学(幼儿园)教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",1,2).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        document.add(table);
    }
    private static void v3AddItem1(Document document,List<Indicator> indicatorList,List<Element> elementList,List<Content> contentList,List<TeacherElementScore> tes) {
        indicatorList.forEach(indicator -> {
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 1})).setWidth(UnitValue.createPercentValue(100));
            List<Element> filterElementList = elementList.stream().filter(v1 -> Objects.equals(v1.getIndicatorId(), indicator.getId())).toList();

            List<Content> filterContentList = new java.util.ArrayList<>(contentList.stream().filter(v1 -> filterElementList.stream().map(Element::getId).toList().contains(v1.getElementId())).toList());
            int cell=0;
            if(filterContentList.stream().anyMatch(str -> str.getContent().contains("、"))){
                cell=filterContentList.size();
            }else{
                cell= filterContentList.stream().map(Content::getElementId).collect(Collectors.toSet()).size();
            }
            int finalCell = cell;
            table.addCell(createCell(indicator.getIndicatorName()+"\n\n"+indicator.getSubName(),cell,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            filterElementList.forEach(element -> {
                Double score = Objects.requireNonNull(tes.stream().filter(v1 -> v1.getElementId() == element.getId()).findFirst().orElse(null)).getScore();
                if(element.getElement()==null){
                    table.addCell(createCellWithIndent(filterContentList.get(0).getContent(),1,2).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    table.addCell(createCell(element.getCriteria(), finalCell,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    table.addCell(createCell(score!=null?score.toString():null, finalCell,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    filterContentList.remove(0);
                    filterContentList.forEach(content -> {
                        table.addCell(createCellWithIndent(content.getContent(),1,2).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    });
                }
                else{
                    table.addCell(createCell(element.getElement(),null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    table.addCell(createCellWithIndent(filterContentList.stream().filter(v1-> Objects.equals(v1.getElementId(), element.getId())).map(Content::getContent).collect(Collectors.joining(",")),null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    table.addCell(createCell(element.getCriteria(),null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                    //TODO分数
                    table.addCell(createCell(score!=null?score.toString():null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                }
            });

            document.add(table);
        });
    }

    private static void addTotal(Document document,Double score){
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 1})).setWidth(UnitValue.createPercentValue(100));

        table.addCell(createCell("总 分   100 分",1,4).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(score!=null?score.toString():null,1,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        document.add(table);
    }

    private static void addItem2(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 2, 2, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("教育教学常规\n\n30-40 分",6,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("1.出勤情况",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("病假、事假、迟到、旷课、政治学习、教研活动、学校会议及其他集体活动出勤情况",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("2.课时工作量",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("完成标准课时工作量、代课、特殊课时等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("3.班主任等工作",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("担任班主任、年段长、教研组长、少先队总辅导员、团委书记及中层以上干部工作",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("4.课堂教学",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("制定教学计划、组织教学、课堂管理、教学理念、课堂实效、德育渗透、现代教育技术手段与学科融合等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("5.学生发展指导",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("作业批改、个性化辅导、心理辅导、学生综合素质评定、成长档案记录、帮扶学困生(特殊生)生涯规划指导、五育并举指导、学习行为习惯培养等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("6.家校联系",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("家长会、家访、家教指导、家长学校培训等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));


        document.add(table);
    }

    private static void addTableTop2(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 3, 1, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("评价指标",1,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评价要素",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评 价 内 容",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("评 价 标 准",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell("得 分\n（100分）",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell(1,1).add(WordVerticalText.createWordVerticalParagraph("一级   二级   三级",chineseFont,6)).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(6));
        document.add(table);
    }

    //2
    private static void addItem3(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 3, 1, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("教育教学业绩\n\n30-45 分",7,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("1.教育教学业绩",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("任教班级学生学业发展情况，任教学科中高考、合格性考试(会考).市质检、期末考试、质量监测等的平均分、及格率、优秀率",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("2.示范引领",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("履行师徒协议及指导成效，承担各级学科研训组、名师工作室工作，担任学科培训班导师，参加“送教送培下乡”活动等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("3.任教班级获奖",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("班(团、队)会活动、单项竞赛获奖、综合性荣誉表彰",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("4.指导学生获奖情况",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("指导学生参加学科竞赛、创新大赛、体育联赛、艺术展演等获奖",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("5.校本课程、综合实践活动",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("参与校本课程研发，指导研究性学习，组织综合实践、社会实践、社团活动，担任课外活动辅导员等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("6.教学团队合作",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("参与教研、集备活动,教案、课件资源共享，参加岗位练兵、培训学习，完成听评课任务等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("7.学生(家长)评价",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("家长会、家访、家教指导、家长学校培训等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        document.add(table);
    }

    private static void addItem4(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 3, 1, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("个人专业发展\n\n5-15 分",4,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("1.承担公开教学活动",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("开设各级学科示范课、观摩课及专题讲座，主持班(团、队)观摩活动、区域交流活动等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("2.撰写教育教学论文",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("撰写学科、德育、党建、管理论文获奖或收入校级以上汇编",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("3.参与教育教学研究",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("参与各级学科、德育、管理的课题研究、专题教研活动、项目式研究活动，负责学科命题,完成继续教育",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("4.个人获奖",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("获得各级教育教学专项表彰、综合性荣誉表彰、公开教学竞赛获奖、名优骨干认定",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        document.add(table);
    }

    private static void addItem5(Document document) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 3, 1, 1})).setWidth(UnitValue.createPercentValue(100));
        table.addCell(createCell("承担急难险重工作\n\n5-10分",2,1).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));


        table.addCell(createCell("1.承担学校行政工作",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("承担学校党建、教研、工会、后勤、安全、宣传、财务等工作，如集备组长、支部书记、党务秘书、安全专干、财务人员、实验组组长、处室干事等",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(createCell("2.承担学校临时性重要任务",null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCellWithIndent("承担创建、评估、迎检、校庆、志愿服务等工作",null,null).setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(createCell(null,null,null).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        document.add(table);
    }

    //工具
    private static Cell createCell(String text,Integer cell,Integer row) {
        if(cell!=null&&row!=null){
            return new Cell(cell,row).add(new Paragraph(text != null ? text : ""));
        }else{
            return new Cell().add(new Paragraph(text != null ? text : ""));
        }
    }

    private static Cell createCellWithIndent(String text,Integer cell,Integer row) {
        if(cell!=null&&row!=null){
            return new Cell(cell,row).add(new Paragraph(text != null ? text : "").setFirstLineIndent(6*2f).setPadding(0));
        }else{
            return new Cell().add(new Paragraph(text != null ? text : "").setFirstLineIndent(6*2f).setPadding(0));
        }
    }
}
