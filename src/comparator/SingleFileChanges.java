package comparator;

import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SingleFileChanges {
    private ArrayList<Pair<Character, String>> changes;
    private String fileName;
    private boolean deleteAll = false;

    public SingleFileChanges(FileComparator fc, String s) {
        changes = fc.getChanges();
        fileName = s;
    }

    public SingleFileChanges(ArrayList<Pair<Character, String>> changes, String name) {
        this.changes = changes;
        this.fileName = name;
    }

    public SingleFileChanges(ArrayList<Pair<Character, String>> changes, String name, boolean deleteAll) {
        this(changes, name);
        this.deleteAll = deleteAll;
    }
    public void write(Path path) {
        File f = Paths.get(path.toString(), fileName).toFile();
        try {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.println("maxLines = " + changes.size());
            for (Pair<Character, String> p : changes) {
                out.println(p.getKey() + p.getValue());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return fileName;
    }

    public ArrayList<Pair<Character, String>> getChanges() {
        return changes;
    }

    public boolean getDeleteAll() {
        return deleteAll;
    }
}
