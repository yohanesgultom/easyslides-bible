package me.gultom.easyslides;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BibleReaderTest {

    @Test
    public void test_readPinyinBible() {
        try {
            File file = new File("SHENGJING_PINYIN_BIBLE.mdb");
            BibleReader bibleReader = new BibleReader(file);
            bibleReader.print(System.out, 100);
            assert true;
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void test_readIndonesianBible() {
        try {
            String filename = "ESHB-AlkitabIndonesia-Revised.mdb";
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());
            BibleReader bibleReader = new BibleReader(file);
            bibleReader.print(System.out, 100);
            assert true;
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
