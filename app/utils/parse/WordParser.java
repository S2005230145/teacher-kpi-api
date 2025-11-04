package utils.parse;


import io.ebean.DB;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.v3.Content;
import models.school.kpi.v3.Element;
import models.school.kpi.v3.Indicator;
import models.table.ParseResult;
import models.table.TableData;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WordParser {
    private static final Logger logger = LoggerFactory.getLogger(WordParser.class);

    public ParseResult parseWord(InputStream inputStream, String fileName) {
        try {
            String lowerFileName = fileName.toLowerCase();
            if (lowerFileName.endsWith(".docx")) {
                return parseDocx(inputStream, fileName);
            } else if (lowerFileName.endsWith(".doc")) {
                return parseDoc(inputStream, fileName);
            } else {
                return ParseResult.failure("不支持的Word格式: " + fileName);
            }
        } catch (Exception e) {
            logger.error("解析Word文件失败", e);
            return ParseResult.failure("解析失败: " + e.getMessage());
        }
    }

    private ParseResult parseDocx(InputStream inputStream, String fileName) throws Exception {
        List<TableData> tables = new ArrayList<>();

        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFTable> wordTables = document.getTables();
            logger.info("DOCX文件包含 {} 个表格", wordTables.size());

            for (int i = 0; i < wordTables.size(); i++) {
                XWPFTable table = wordTables.get(i);
                TableData tableData = parseXWPFTable(table, fileName, i);

                if (tableData != null && tableData.getRowCount() > 0) {
                    tables.add(tableData);
                    this.DataInject(tableData);

                    logger.info("解析表格 {}: {}", i, tableData);
                }
            }
        }



        return ParseResult.success(
                String.format("成功解析 %d 个表格", tables.size()),
                fileName,
                "WORD_DOCX",
                tables
        );
    }

    private ParseResult parseDoc(InputStream inputStream, String fileName) throws Exception {
        List<TableData> tables = new ArrayList<>();

        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            // 对于.doc文件，使用文本分析的方法
            Range range = document.getRange();
            String text = range.text();

            tables = parseTablesFromText(text, fileName);
            logger.info("DOC文件解析完成，找到 {} 个表格", tables.size());
        }

        return ParseResult.success(
                String.format("成功解析 %d 个表格", tables.size()),
                fileName,
                "WORD_DOC",
                tables
        );
    }

    private TableData parseXWPFTable(XWPFTable table, String fileName, int tableIndex) {
        TableData tableData = new TableData(fileName, null, tableIndex);

        List<XWPFTableRow> rows = table.getRows();
        boolean isFirstRow = true;

        for (XWPFTableRow row : rows) {
            List<String> rowData = parseXWPFTableRow(row);

            // 跳过空行
            if (isRowEmpty(rowData)) {
                continue;
            }

            if (isFirstRow) {
                tableData.setHeaders(rowData);
                isFirstRow = false;
            } else {
                tableData.addRow(rowData);
            }
        }

        return tableData;
    }

    private List<String> parseXWPFTableRow(XWPFTableRow row) {
        List<String> rowData = new ArrayList<>();
        List<XWPFTableCell> cells = row.getTableCells();

        for (XWPFTableCell cell : cells) {
            String text = cell.getText().trim();
            rowData.add(text);
        }

        return rowData;
    }

    private List<TableData> parseTablesFromText(String text, String fileName) {
        List<TableData> tables = new ArrayList<>();
        String[] lines = text.split("\r\n|\n");

        List<List<String>> currentTable = new ArrayList<>();
        int tableIndex = 0;
        boolean inTable = false;

        for (String line : lines) {
            if (isPotentialTableRow(line)) {
                if (!inTable) {
                    inTable = true;
                    currentTable = new ArrayList<>();
                }
                currentTable.add(parseTableRow(line));
            } else {
                if (inTable && currentTable.size() >= 2) {
                    TableData tableData = createTableFromData(currentTable, fileName, tableIndex);
                    tables.add(tableData);
                    tableIndex++;
                }
                inTable = false;
                currentTable = new ArrayList<>();
            }
        }

        // 处理文件末尾的表格
        if (inTable && currentTable.size() >= 2) {
            TableData tableData = createTableFromData(currentTable, fileName, tableIndex);
            tables.add(tableData);
        }

        return tables;
    }

    private boolean isPotentialTableRow(String line) {
        return line.contains("\t") || line.matches(".*\\s{2,}.*");
    }

    private List<String> parseTableRow(String line) {
        if (line.contains("\t")) {
            String[] cells = line.split("\t");
            List<String> rowData = new ArrayList<>();
            for (String cell : cells) {
                rowData.add(cell.trim());
            }
            return rowData;
        } else {
            String[] cells = line.split("\\s{2,}");
            List<String> rowData = new ArrayList<>();
            for (String cell : cells) {
                rowData.add(cell.trim());
            }
            return rowData;
        }
    }

    private TableData createTableFromData(List<List<String>> tableData, String fileName, int tableIndex) {
        TableData table = new TableData(fileName, null, tableIndex);

        if (!tableData.isEmpty()) {
            table.setHeaders(tableData.get(0));
            for (int i = 1; i < tableData.size(); i++) {
                table.addRow(tableData.get(i));
            }
        }

        return table;
    }

    private boolean isRowEmpty(List<String> rowData) {
        return rowData.stream().allMatch(cell -> cell == null || cell.trim().isEmpty());
    }


    public static String[] splitFirstOccurrenceWithIndexOf(String input) {
        if (input == null || input.isEmpty()) {
            return new String[]{input, ""};
        }

        int parenthesisIndex = input.indexOf('（');
        int parenthesisIndexOther= input.indexOf('(');
        int digitIndex = findFirstDigitIndex(input);

        // 找到最早出现的位置
        int splitIndex = -1;

        if (parenthesisIndex != -1 && digitIndex != -1) {
            splitIndex = Math.min(parenthesisIndex, digitIndex);
        } else if (parenthesisIndex != -1) {
            splitIndex = parenthesisIndex;
        } else if (digitIndex != -1) {
            splitIndex = digitIndex;
        }else if(parenthesisIndexOther != -1){
            splitIndex=parenthesisIndexOther;
        }

        if (splitIndex != -1) {
            String firstPart = input.substring(0, splitIndex);
            String secondPart = input.substring(splitIndex); // 包含分隔符

            return new String[]{firstPart, secondPart};
        }

        return new String[]{input, ""};
    }

    /**
     * {
     *     "success": true,
     *     "message": "成功解析 2 个表格",
     *     "fileName": "test.docx",
     *     "fileType": "WORD_DOCX",
     *     "totalTables": 2,
     *     "tables": [
     *         {
     *             "fileName": "test.docx",
     *             "sheetName": null,
     *             "tableIndex": 0,
     *             "headers": [
     *                 "评价指标",
     *                 "评价要素",
     *                 "评 价 内 容",
     *                 "评 价 标 准",
     *                 "初 始 得 分（100分）",
     *                 "最 终 得 分（100分）"
     *             ],
     *             "rows": [
     *                 [
     *                     "一   二   三级   级   级",
     *                     "",
     *                     "",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "师德师风（优、合格、不合格）",
     *                     "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",
     *                     "是否合格，不设分值",
     *                     "7.0",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "教育教学常规30-40 分",
     *                     "1.出勤情况",
     *                     "病假,事假,迟到,旷课,政治学习,教研活动,学校会议及其他集体活动出勤情况",
     *                     "",
     *                     "14.0",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.课时工作量",
     *                     "完成标准课时工作量,代课,特殊课时等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.班主任等工作",
     *                     "担任班主任,年段长,教研组长,少先队总辅导员,团委书记及中层以上干部工作",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "4.课堂教学",
     *                     "制定教学计划,组织教学,课堂管理,教学理念,课堂实效,德育渗透,现代教育技术手段与学科融合等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "5.学生发展指导",
     *                     "作业批改,个性化辅导,心理辅导,学生综合素质评定,成长档案记录,帮扶学困生（特殊生）生涯规划指导,五育并举指导,学习行为习惯培养等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "6.家校联系",
     *                     "家长会,家访,家教指导,家长学校培训等",
     *                     "",
     *                     "",
     *                     ""
     *                 ]
     *             ],
     *             "columnCount": 6,
     *             "rowCount": 10
     *         }
     *     ]
     * }
     *
     */
    private void DataInject(TableData tableData){
        List<Indicator> indicatorList=new ArrayList<>();
        List<Element> elementList=new ArrayList<>();
        List<String> contentNameList=new ArrayList<>();
        List<Content> contentList=new ArrayList<>();

        Transaction transaction= DB.beginTransaction();

        List<String> tmpContentName=new ArrayList<>();
        tableData.getRows().stream().filter(data->data.size()>=5).toList().subList(1,tableData.getRows().size()).forEach(data->{
            if(!data.get(0).isEmpty()&&!data.get(0).isBlank()){
                Indicator indicator = new Indicator();
                String[] strings = splitFirstOccurrenceWithIndexOf(data.get(0));
                indicator.setIndicatorName(strings[0]);
                indicator.setSubName(strings[1]);
                logger.info(indicator.toString());
                indicatorList.add(indicator);
            }
            if(data.size()==5){
                Element element = new Element();
                element.setElement(null);
                element.setCriteria(data.get(3));
                element.setType(0);
                elementList.add(element);

                tmpContentName.add(data.get(1));
            }else{
                String fullName = String.join("@#$", tmpContentName);
                tmpContentName.clear();
                if(!fullName.isEmpty()) contentNameList.add(fullName);

                Element element = new Element();
                element.setElement(data.get(1));
                element.setCriteria(data.get(3));
                element.setType(0);
                elementList.add(element);

                contentNameList.add(data.get(2));
            }
        });
        try{
            DB.saveAll(indicatorList);
        }catch (Exception e){
            logger.info(String.valueOf(e));
        }

    }

    /**
     * 查找第一个数字的位置
     */
    private static int findFirstDigitIndex(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
