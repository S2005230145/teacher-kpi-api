package utils.parse;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.hwpf.HWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordTableTitleExtractor {

    /**
     * 表格标题信息类
     */
    public static class TableTitleInfo {
        private int tableIndex;
        private String titleText;
        private int paragraphIndex;
        private int distanceFromTable; // 距离表格的段落数
        private boolean isAboveTable;  // 是否在表格上方

        public TableTitleInfo(int tableIndex, String titleText, int paragraphIndex,
                              int distanceFromTable, boolean isAboveTable) {
            this.tableIndex = tableIndex;
            this.titleText = titleText;
            this.paragraphIndex = paragraphIndex;
            this.distanceFromTable = distanceFromTable;
            this.isAboveTable = isAboveTable;
        }

        // Getters
        public int getTableIndex() { return tableIndex; }
        public String getTitleText() { return titleText; }
        public int getParagraphIndex() { return paragraphIndex; }
        public int getDistanceFromTable() { return distanceFromTable; }
        public boolean isAboveTable() { return isAboveTable; }

        @Override
        public String toString() {
            return String.format("表格%d: '%s' (段落%d, 距离%d, %s)",
                    tableIndex, titleText, paragraphIndex, distanceFromTable,
                    isAboveTable ? "上方" : "下方");
        }
    }

    /**
     * 从Word文档提取所有表格的标题
     */
    public static List<TableTitleInfo> extractTableTitles(File file, String fileName) {
        List<TableTitleInfo> allTitles = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(file)) {
            if (fileName.toLowerCase().endsWith(".docx")) {
                XWPFDocument document = new XWPFDocument(inputStream);
                allTitles = extractTitlesFromDocx(document);
            } else if (fileName.toLowerCase().endsWith(".doc")) {
                HWPFDocument document = new HWPFDocument(inputStream);
                allTitles = extractTitlesFromDoc(document);
            } else {
                throw new IllegalArgumentException("不支持的Word格式");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取Word文件失败: " + e.getMessage(), e);
        }

        return allTitles;
    }

    /**
     * 从DOCX文档提取表格标题
     */
    private static List<TableTitleInfo> extractTitlesFromDocx(XWPFDocument document) {
        List<TableTitleInfo> titles = new ArrayList<>();
        List<XWPFTable> tables = document.getTables();
        List<IBodyElement> bodyElements = document.getBodyElements();

        for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
            XWPFTable table = tables.get(tableIndex);

            // 查找表格在文档中的位置
            int tablePosition = findTablePositionInBodyElements(bodyElements, table);
            if (tablePosition != -1) {
                // 查找表格上方的标题
                TableTitleInfo aboveTitle = findTitleAboveTableDocx(bodyElements, tablePosition, tableIndex);
                if (aboveTitle != null) {
                    titles.add(aboveTitle);
                }

                // 查找表格下方的标题（可选）
                TableTitleInfo belowTitle = findTitleBelowTableDocx(bodyElements, tablePosition, tableIndex);
                if (belowTitle != null) {
                    titles.add(belowTitle);
                }
            }
        }

        return titles;
    }

    /**
     * 查找表格在文档元素中的位置
     */
    private static int findTablePositionInBodyElements(List<IBodyElement> bodyElements, XWPFTable table) {
        for (int i = 0; i < bodyElements.size(); i++) {
            IBodyElement element = bodyElements.get(i);
            if (element instanceof XWPFTable && element == table) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找表格上方的标题（DOCX）
     */
    private static TableTitleInfo findTitleAboveTableDocx(List<IBodyElement> bodyElements,
                                                          int tablePosition, int tableIndex) {
        // 从表格位置向上查找
        for (int i = tablePosition - 1; i >= 0; i--) {
            IBodyElement element = bodyElements.get(i);

            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                String text = paragraph.getText().trim();

                if (isPotentialTitle(text)) {
                    int distance = tablePosition - i;
                    return new TableTitleInfo(tableIndex, text, i, distance, true);
                }
            }

            // 如果遇到另一个表格，停止搜索
            if (element instanceof XWPFTable) {
                break;
            }
        }

        return null;
    }

    /**
     * 查找表格下方的标题（DOCX）
     */
    private static TableTitleInfo findTitleBelowTableDocx(List<IBodyElement> bodyElements,
                                                          int tablePosition, int tableIndex) {
        // 从表格位置向下查找
        for (int i = tablePosition + 1; i < bodyElements.size(); i++) {
            IBodyElement element = bodyElements.get(i);

            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                String text = paragraph.getText().trim();

                if (isPotentialTitle(text)) {
                    int distance = i - tablePosition;
                    return new TableTitleInfo(tableIndex, text, i, distance, false);
                }
            }

            // 如果遇到另一个表格，停止搜索
            if (element instanceof XWPFTable) {
                break;
            }
        }

        return null;
    }

    /**
     * 从DOC文档提取表格标题（简化实现）
     */
    private static List<TableTitleInfo> extractTitlesFromDoc(HWPFDocument document) {
        List<TableTitleInfo> titles = new ArrayList<>();
        // DOC格式处理较复杂，这里简化实现
        // 实际项目中可能需要更复杂的解析逻辑
        return titles;
    }

    /**
     * 判断文本是否为潜在的标题
     */
    private static boolean isPotentialTitle(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String trimmedText = text.trim();

        // 排除空行和过短的文本
        if (trimmedText.length() < 2) {
            return false;
        }

        // 常见的标题特征
        return hasTitleCharacteristics(trimmedText);
    }

    /**
     * 判断文本是否具有标题特征
     */
    private static boolean hasTitleCharacteristics(String text) {
        // 1. 包含"表"、"表格"、"Table"等关键词
        if (text.matches(".*[表表格]\\s*[0-9一二三四五六七八九十]+.*") ||
                text.toLowerCase().contains("table")) {
            return true;
        }

        // 2. 以数字开头（如"1.2 表格标题"）
        if (text.matches("^\\d+(\\.\\d+)*\\s+.*")) {
            return true;
        }

        // 3. 包含冒号、破折号等标题标志
        if (text.matches(".*[:：\\-—].*")) {
            return true;
        }

        // 4. 文本长度适中（通常标题不会太长）
        if (text.length() > 5 && text.length() < 100) {
            // 检查是否包含完整的句子特征（避免把正文误判为标题）
            return !text.matches(".*[。？！;；]\\s*$");
        }

        return true;
    }
}
