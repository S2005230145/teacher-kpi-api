package utils.parse;


import jakarta.inject.Singleton;
import models.table.ParseResult;
import models.table.TableData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Singleton
public class ExcelParser {
    private static final Logger logger = LoggerFactory.getLogger(ExcelParser.class);

    public ParseResult parseExcel(InputStream inputStream, String fileName) {
        try {
            Workbook workbook = createWorkbook(inputStream, fileName);
            if (workbook == null) {
                return ParseResult.failure("不支持的Excel格式: " + fileName);
            }

            return parseWorkbook(workbook, fileName);
        } catch (Exception e) {
            logger.error("解析Excel文件失败", e);
            return ParseResult.failure("解析失败: " + e.getMessage());
        }
    }

    private Workbook createWorkbook(InputStream inputStream, String fileName) throws Exception {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (lowerFileName.endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        }
        return null;
    }

    private ParseResult parseWorkbook(Workbook workbook, String fileName) {
        List<TableData> tables = new ArrayList<>();
        int numberOfSheets = workbook.getNumberOfSheets();

        logger.info("Excel文件包含 {} 个工作表", numberOfSheets);

        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            TableData tableData = parseSheet(sheet, fileName, i);

            if (tableData != null && tableData.getRowCount() > 0) {
                tables.add(tableData);
                logger.info("解析工作表 {}: {}", sheet.getSheetName(), tableData);
            }
        }

        try {
            workbook.close();
        } catch (Exception e) {
            logger.warn("关闭Workbook失败", e);
        }

        return ParseResult.success(
                String.format("成功解析 %d 个表格", tables.size()),
                fileName,
                "EXCEL",
                tables
        );
    }

    private TableData parseSheet(Sheet sheet, String fileName, int sheetIndex) {
        TableData tableData = new TableData(fileName, sheet.getSheetName(), sheetIndex);

        Iterator<Row> rowIterator = sheet.iterator();
        boolean isFirstRow = true;
        int rowCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<String> rowData = parseRow(row);

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

            rowCount++;
        }

        logger.debug("工作表 {} 解析完成，共 {} 行", sheet.getSheetName(), rowCount);
        return tableData;
    }

    private List<String> parseRow(Row row) {
        List<String> rowData = new ArrayList<>();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            rowData.add(getCellValueAsString(cell));
        }

        return rowData;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                try {
                    return getCellValueAsString(evaluateFormulaCell(cell));
                } catch (Exception e) {
                    return cell.getCellFormula();
                }

            case BLANK:
                return "";

            default:
                return "";
        }
    }

    private Cell evaluateFormulaCell(Cell cell) {
        Workbook workbook = cell.getSheet().getWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return evaluator.evaluateInCell(cell);
    }

    private boolean isRowEmpty(List<String> rowData) {
        return rowData.stream().allMatch(cell -> cell == null || cell.trim().isEmpty());
    }
}
