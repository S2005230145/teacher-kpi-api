package service;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import models.table.ParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.parse.ExcelParser;
import utils.parse.WordParser;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import play.libs.Files.TemporaryFile;

@Singleton
@Slf4j
public class FileParseService {

    private final ExcelParser excelParser;
    private final WordParser wordParser;

    @Inject
    public FileParseService(ExcelParser excelParser, WordParser wordParser) {
        this.excelParser = excelParser;
        this.wordParser = wordParser;
    }

    public ParseResult parseFile(File file, String fileName) {
        log.info("开始解析文件: {}", fileName);

        if (!file.exists()) {
            return ParseResult.failure("文件不存在: " + fileName);
        }

        if (!isSupportedFile(fileName)) {
            return ParseResult.failure("不支持的文件格式: " + fileName);
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            String lowerFileName = fileName.toLowerCase();

            if (lowerFileName.endsWith(".xlsx") || lowerFileName.endsWith(".xls")) {
                return excelParser.parseExcel(inputStream, fileName);
            } else {
                return wordParser.parseWord(inputStream, fileName);
            }

        } catch (Exception e) {
            log.error("文件解析失败", e);
            return ParseResult.failure("文件解析失败: " + e.getMessage());
        }
    }

    public ParseResult parseTemporaryFile(TemporaryFile tempFile, String fileName) {
        return parseFile(tempFile.path().toFile(), fileName);
    }

    public boolean isSupportedFile(String fileName) {
        if (fileName == null) {
            return false;
        }

        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".xlsx") ||
                lowerFileName.endsWith(".xls") ||
                lowerFileName.endsWith(".docx") ||
                lowerFileName.endsWith(".doc");
    }

    public String getFileType(String fileName) {
        if (fileName == null) {
            return "UNKNOWN";
        }

        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".xlsx") || lowerFileName.endsWith(".xls")) {
            return "EXCEL";
        } else if (lowerFileName.endsWith(".docx")) {
            return "WORD_DOCX";
        } else if (lowerFileName.endsWith(".doc")) {
            return "WORD_DOC";
        } else {
            return "UNKNOWN";
        }
    }
}
