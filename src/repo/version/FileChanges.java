package repo.version;

import comparator.SingleFileChanges;
import javafx.util.Pair;
import repo.InformationFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileChanges extends InformationFile {
    private ArrayList<Pair<Character, String>> lines = new ArrayList<>();
    private final static String text = "maxLines = ";

    /**
     * 通过一个文件来初始化所有属性内容
     *
     * @param f 用于初始化的文件
     */
    public FileChanges(File f) {
        super(f);
    }


    public FileChanges(Path path, String str) {
        super(path);
        file = Paths.get(path.toString(), str).toFile();
        String line;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            line = in.readLine();
            singleAttributes.put("maxLines", line.substring(text.length()));
            while ((line = in.readLine()) != null) {
                lines.add(new Pair<>(line.charAt(0), line.substring(1)));
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileChanges(String name, SingleFileChanges singleChanges, Path path) {
        super(path);
        file = Paths.get(path.toString(), name).toFile();
        lines = singleChanges.getChanges();
    }


    public int getMaxLines() {
        return lines.size();
    }

    public void write() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println(text + lines.size());
            for (Pair<Character, String> p : lines) {
                out.println(p.getKey() + p.getValue());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
