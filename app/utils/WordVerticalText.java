package utils;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

public class WordVerticalText {
    /**
     * 将文本按单词纵向排列，空格保持横向
     */
    public static Paragraph createWordVerticalParagraph(String text, PdfFont font, float fontSize) {
        Paragraph paragraph = new Paragraph();
        paragraph.setPadding(0);
        paragraph.setMargin(0);
        paragraph.setTextAlignment(TextAlignment.CENTER);

        // 按空格分割单词
        String[] words = text.split(" ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            // 处理每个单词（纵向排列）
            if (!word.isEmpty()) {
                // 添加纵向单词
                paragraph.add(createVerticalWord(word, font, fontSize));
            }

            // 不是最后一个单词时添加空格分隔
            if (i < words.length - 1) {
                paragraph.add(new Text(" ").setFontSize(fontSize)); // 横向空格
            }
        }

        return paragraph;
    }

    /**
     * 创建单个单词的纵向排列
     */
    private static Paragraph createVerticalWord(String word, PdfFont font, float fontSize) {
        Paragraph wordParagraph = new Paragraph();
        wordParagraph.setPadding(0);
        wordParagraph.setMargin(0);
        wordParagraph.setTextAlignment(TextAlignment.CENTER);

        // 将单词的每个字符纵向排列
        char[] chars = word.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            wordParagraph.add(new Text(String.valueOf(chars[j]))
                    .setFont(font)
                    .setFontSize(fontSize));

            if (j < chars.length - 1) {
                wordParagraph.add(new Text("\n"));
            }
        }

        return wordParagraph;
    }
}
