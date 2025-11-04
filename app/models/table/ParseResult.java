package models.table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ParseResult {
    private boolean success;
    private String message;
    private String fileName;
    private String fileType;
    private List<TableData> tables;

    public ParseResult() {
        this.tables = new ArrayList<>();
    }

    public ParseResult(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public void addTable(TableData table) {
        this.tables.add(table);
    }

    // Getters and Setters
    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("fileType")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @JsonProperty("tables")
    public List<TableData> getTables() {
        return tables;
    }

    public void setTables(List<TableData> tables) {
        this.tables = tables;
    }

    @JsonProperty("totalTables")
    public int getTotalTables() {
        return tables.size();
    }

    // Static factory methods
    public static ParseResult success(String message, String fileName, String fileType, List<TableData> tables) {
        ParseResult result = new ParseResult();
        result.setSuccess(true);
        result.setMessage(message);
        result.setFileName(fileName);
        result.setFileType(fileType);
        result.setTables(tables);
        return result;
    }

    public static ParseResult failure(String message) {
        return new ParseResult(false, message);
    }
}
