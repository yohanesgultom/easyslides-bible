package me.gultom.easyslides;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class BibleReader {
    private Database database;

    public BibleReader(File file) throws IOException {
        this.database = DatabaseBuilder.open(file);
    }

    public void print(PrintStream stream) throws IOException {
        this.print(stream, null);
    }

    public void print(PrintStream stream, Integer limit) throws IOException {
        for (String name : database.getTableNames()) {
            Table table = database.getTable(name);
            int i = 0;
            for(Row row : table) {
                stream.println(row);
                i++;
                if (limit != null && i >= limit) {
                    break;
                }
            }
        }
    }
}
