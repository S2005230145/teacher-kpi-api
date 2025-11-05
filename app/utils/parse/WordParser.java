package utils.parse;


import io.ebean.DB;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.v3.Content;
import models.school.kpi.v3.Element;
import models.school.kpi.v3.Indicator;
import models.school.kpi.v3.KPI;
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
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class WordParser {
    private static final Logger logger = LoggerFactory.getLogger(WordParser.class);

    private static String ruleTitle;

    public static void setRuleTitle(String ruleTitle) {
        WordParser.ruleTitle = ruleTitle;
    }

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

    private void DataInject(TableData tableData){
        List<Indicator> addIndicatorList=new ArrayList<>();
        //临时存储
        List<Element> tmpElementList=new ArrayList<>();
        List<Element> addElementList=new ArrayList<>();

        List<Content> addContentList=new ArrayList<>();

        Transaction transaction= DB.beginTransaction();

        //临时存储
        List<String> tmpContentName=new ArrayList<>();
        AtomicInteger lastIdx= new AtomicInteger(0);//上一次定位
        AtomicInteger currentIdx= new AtomicInteger(0);//定位

        AtomicInteger lastIdxOther= new AtomicInteger(0);//上一次定位
        AtomicInteger currentIdxOther= new AtomicInteger(0);//定位
        List<List<String>> list = tableData.getRows().stream().filter(data -> data.size() >= 5).toList();
        list.subList(1,list.size()).forEach(data->{
            if(!data.get(0).isEmpty()&&!data.get(0).isBlank()){
                Indicator indicator = new Indicator();
                String[] strings = splitFirstOccurrenceWithIndexOf(data.get(0));
                indicator.setIndicatorName(strings[0]);
                indicator.setSubName(strings[1]);
                addIndicatorList.add(indicator);
                lastIdx.set(currentIdx.get());
            }
            if(data.size()==5){
                if(!data.get(2).isEmpty()&&!data.get(2).isBlank()){
                    Element element = new Element();
                    element.setElement(null);
                    element.setCriteria(data.get(2));
                    element.setType(0);
                    tmpElementList.add(element);
                }
                tmpContentName.add(data.get(1));
            }else{
                String fullName = String.join("@#$", tmpContentName);
                tmpContentName.clear();
                if(!fullName.isEmpty()){
                    Content content1 = new Content();
                    content1.setContent(fullName);
                    Element element1 = tmpElementList.get(lastIdx.get());
                    element1.setContentList(List.of(content1));
                    addIndicatorList.get(lastIdx.get()).setElementList(List.of(element1));
                    tmpElementList.remove(lastIdx.get());
                    currentIdx.incrementAndGet();
                }else if(!data.get(0).isEmpty()&&!data.get(0).isBlank()){
                    ArrayList<Element> elements = new ArrayList<>(tmpElementList);
                    addIndicatorList.get(lastIdxOther.get()).setElementList(elements);
                    lastIdxOther.set(currentIdxOther.getAndIncrement());
                    tmpElementList.clear();
                    currentIdx.incrementAndGet();
                }

                Element element = new Element();
                element.setElement(data.get(1));
                element.setCriteria(data.get(3).isBlank()?null:data.get(3));
                element.setType(0);
                Content content = new Content();
                content.setContent(data.get(2));
                element.setContentList(List.of(content));
                tmpElementList.add(element);

                if(data.equals(list.get(list.size()-1))){
                    ArrayList<Element> elements = new ArrayList<>(tmpElementList);
                    addIndicatorList.get(addIndicatorList.size()-1).setElementList(elements);
                    tmpElementList.clear();
                }
            }
        });
        //存储
        KPI kpi=new KPI();
        kpi.setTitle(ruleTitle);
        try{
            kpi.save();
        }catch (Exception e){
            logger.info("kpi报错：{}", String.valueOf(e));
            transaction.rollback();
        }
        addIndicatorList.forEach(indicator -> {
            indicator.setKpiId(kpi.getId());
        });
        try{
            DB.saveAll(addIndicatorList);
        }catch (Exception e){
            logger.info("indicator报错：{}", String.valueOf(e));
            transaction.rollback();
        }
        addIndicatorList.forEach(indicator->{
            indicator.getElementList().forEach(element->{
                element.setIndicatorId(indicator.getId());
                element.setType(0);
                addElementList.add(element);
            });
        });
        try{
            DB.saveAll(addElementList);
        }catch (Exception e){
            logger.info("element报错：{}", String.valueOf(e));
            transaction.rollback();
        }
        addElementList.forEach(element->{
            element.getContentList().forEach(content -> {
                content.setElementId(element.getId());
                addContentList.add(content);
            });
        });
        try{
            DB.saveAll(addContentList);
            transaction.commit();
        }catch (Exception e){
            logger.info("content报错：{}", String.valueOf(e));
            transaction.rollback();
        }
        logger.info("==================");
        logger.info("已存入数据库");
        logger.info("==================");
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
