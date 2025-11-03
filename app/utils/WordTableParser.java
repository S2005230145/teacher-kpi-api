package utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class WordTableParser {
    /**
     * 解析 .docx 格式的 Word 表格
     */
    public static List<List<List<String>>> parseDocxTables(String filePath) {
        List<List<List<String>>> allTables = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // 获取所有表格
            List<XWPFTable> tables = document.getTables();

            for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
                XWPFTable table = tables.get(tableIndex);
                List<List<String>> tableData = new ArrayList<>();

                // 遍历行
                for (XWPFTableRow row : table.getRows()) {
                    List<String> rowData = new ArrayList<>();

                    // 遍历单元格
                    for (XWPFTableCell cell : row.getTableCells()) {
                        String cellText = cell.getText().trim();
                        rowData.add(cellText);
                    }

                    tableData.add(rowData);
                }

                allTables.add(tableData);
                System.out.println("表格 " + (tableIndex + 1) + " 解析完成，共 " +
                        tableData.size() + " 行 " + (tableData.isEmpty() ? 0 : tableData.get(0).size()) + " 列");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTables;
    }

    /**
     * 解析 .doc 格式的 Word 表格（旧格式）
     */
    public static List<List<List<String>>> parseDocTables(String filePath) {
        List<List<List<String>>> allTables = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             HWPFDocument document = new HWPFDocument(fis)) {

            Range range = document.getRange();
            TableIterator tableIterator = new TableIterator(range);

            int tableIndex = 0;
            while (tableIterator.hasNext()) {
                Table table = tableIterator.next();
                List<List<String>> tableData = new ArrayList<>();

                for (int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) {
                    TableRow row = table.getRow(rowIndex);
                    List<String> rowData = new ArrayList<>();

                    for (int cellIndex = 0; cellIndex < row.numCells(); cellIndex++) {
                        TableCell cell = row.getCell(cellIndex);
                        String cellText = cell.text().trim();
                        rowData.add(cellText);
                    }

                    tableData.add(rowData);
                }

                allTables.add(tableData);
                tableIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allTables;
    }
}
