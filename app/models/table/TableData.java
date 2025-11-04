package models.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TableData {
    private String fileName;
    private String sheetName;
    private int tableIndex;
    private List<String> headers;
    private List<List<String>> rows;

    public TableData() {
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public TableData(String fileName, String sheetName, int tableIndex) {
        this();
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.tableIndex = tableIndex;
    }

    public void addRow(List<String> row) {
        this.rows.add(row);
    }

    // Getters and Setters
    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("sheetName")
    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @JsonProperty("tableIndex")
    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    @JsonProperty("headers")
    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @JsonProperty("rows")
    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    @JsonProperty("rowCount")
    public int getRowCount() {
        return rows.size();
    }

    @JsonProperty("columnCount")
    public int getColumnCount() {
        if (headers != null && !headers.isEmpty()) {
            return headers.size();
        }
        return rows.isEmpty() ? 0 : rows.get(0).size();
    }
}
