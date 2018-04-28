package me.gultom.easyslides;

import com.healthmarketscience.jackcess.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleWriter {

    private String root;
    private String title;
    private String language;
    private String translation;
    private String copyright1;
    private String copyright2;

    public BibleWriter(String root, String title, String language, String translation, String copyright1, String copyright2) {
        this.root = root;
        this.title = title;
        this.language = language;
        this.translation = translation;
        this.copyright1 = copyright1;
        this.copyright2 = copyright2;
    }

    public List<String> parseBooks(File file) throws IOException {
        List<String> books = new ArrayList<>();
        Document doc = Jsoup.parse(file, "UTF-8");
        Elements elements = doc.select(".ym-g50 li a");
        for (Element el : elements) {
            books.add(el.text());
        }
        return books;
    }

    public void write(File file) throws IOException, SQLException {
        // setup MDB database
        Database db = DatabaseBuilder.create(Database.FileFormat.V2000, file);
        Table table = new TableBuilder("BIBLE")
                .addColumn(new ColumnBuilder("BOOK").setSQLType(Types.INTEGER))
                .addColumn(new ColumnBuilder("CHAPTER").setSQLType(Types.INTEGER))
                .addColumn(new ColumnBuilder("VERSE").setSQLType(Types.INTEGER))
                .addColumn(new ColumnBuilder("BIBLETEXT").setSQLType(Types.CLOB))
                .toTable(db);

        // header
        table.addRow(0, 0, 0, this.title);
        table.addRow(0, 0, 1, this.translation);
        table.addRow(0, 0, 2, this.language);
        table.addRow(0, 0, 3, this.copyright1);
        table.addRow(0, 0, 4, this.copyright2);

        // books
        File indexFile = Paths.get(this.root, "index.htm").toFile();
        List<String> books = this.parseBooks(indexFile);
        for (int i = 0; i < books.size(); i++) {
            table.addRow(0, 10, i + 1, books.get(i));
        }

        // verses
        for (int i = 1; i <= books.size(); i++) {
            String bookNo = StringUtils.leftPad(String.valueOf(i), 2, "0");
            File bookDir = Paths.get(this.root, bookNo).toFile();
            File[] chapterFiles = bookDir.listFiles();
            for (int j = 1; j < chapterFiles.length; j++) {
                System.out.println(String.format("%s chapter %d ..", books.get(i - 1), j));
                String chapterFilename = String.format("%d.htm", j);
                File chapterFile = Paths.get(this.root, bookNo, chapterFilename).toFile();
                List<String> verses = this.parseVerses(chapterFile);
                for (int k = 0; k < verses.size(); k++) {
                    String verse = verses.get(k);
                    table.addRow(i, j, k + 1, verse);
                }
            }
        }
    }

    public List<String> parseVerses(File chapterFile) throws IOException {
        List<String> verses = new ArrayList<>();
        Document doc = Jsoup.parse(chapterFile, "UTF-8");
        Element textBody = doc.select("#textBody p").get(0);
        Pattern pattern = Pattern.compile("^\\d+ (.*?)$");
        Matcher matcher;
        String verse;
        for (String v : textBody.html().split("<br>")) {
            verse = Jsoup.parse(v).text();
            matcher = pattern.matcher(verse);
            if (matcher.find()) {
                verse = matcher.group(1);
                verses.add(verse);
            }
        }
        return verses;
    }
}
