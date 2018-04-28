package me.gultom.easyslides;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class BibleWriterTest {

    private BibleWriter bibleWriter;
    private String root;

    /**
     * Test parsing HTML bible from WordProject.org
     * And write it as EasySlides bible (.mdb)
     */
    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        this.root = classLoader.getResource("pn").getPath();
        String title = "Sheng Jing Pinyin Bible Version";
        String translation = "Sheng Jing";
        String language = "Chinese Pinyin";
        String copyright1 = "Raw data provided by WordProject.org - Non-Commercial use only";
        String copyright2 = "Converted by Yohanes Gultom <yohanes.gultom@gmail.com>";
        this.bibleWriter = new BibleWriter(this.root, title, translation, language, copyright1, copyright2);
    }

    @Ignore
    @Test
    public void test_default() {
        File bibleFile = new File("SHENGJING_PINYIN_BIBLE.mdb");
        try {
            this.bibleWriter.write(bibleFile);
            assert true;
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
        }
    }

    /**
     * Test parsing books from HTML bible from WordProject.org
     */
    @Test
    public void test_parseBooks() {
        File indexFile = Paths.get(this.root, "index.htm").toFile();
        try {
            List<String> books = this.bibleWriter.parseBooks(indexFile);
            assert books.size() == 66;
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }

    /**
     * Test parsing verses from HTML bible from WordProject.org
     */
    @Test
    public void test_parseVerses() {
        File chapterFile = Paths.get(this.root, "01", "1.htm").toFile();
        try {
            List<String> verses = this.bibleWriter.parseVerses(chapterFile);
            assert verses.size() == 31; // Chuàngshìjì Zhāng 1 (Genesis Chapter 1)
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
