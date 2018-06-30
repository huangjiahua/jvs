package repo.version;

import repo.Commitment;
import repo.InformationFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 管理一个版本目录，其中包含了每个版本相较于上一个版本的改变的文件的索引。
 */
public class VersionInventory extends InformationFile {
    private final static String fileName = "inventory.txt";
    private final static String[] text = {
            "Summary = ",
            "Description = ",
            "changedFiles = {",
            "};"
    };


    public VersionInventory(Path path) {
        super(path);
        file = Paths.get(path.toString(), fileName).toFile();
        String line;
        String summary = "";
        String description = "";
        ArrayList<String> changedFiles = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            line = in.readLine();
            summary = line.substring(text[0].length());
            System.out.println("Summary: " + summary);
            line = in.readLine();
            description = line.substring(text[1].length());
            while (!(line = in.readLine()).equals("changedFiles = {"))
                description += '\n' + line;
            System.out.println("Description: " + description);

            while (!(line = in.readLine()).equals("};")) {
                changedFiles.add(line);
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        multiAttributes.put("changedFiles", changedFiles);
        singleAttributes.put("summary", summary);
        singleAttributes.put("description", description);


    }

    public VersionInventory(Commitment commitment, Path path) {
        super(path);
        file = Paths.get(path.toString(), fileName).toFile();
        singleAttributes.put("summary", commitment.summary);
        singleAttributes.put("description", commitment.description);

        ArrayList<String> files = (ArrayList<String>)commitment.filesToChange.clone();
        for (String s : commitment.filesToDelete)
            files.add(s);

        multiAttributes.put("changedFiles", files);
    }

    public static void initiate(Path org) {
        String s = org.toString();
        Path path = Paths.get(s, fileName);
        try {
            File f = path.toFile();
            f.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(f));
            for (String line : text)
                out.println(line);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过一个文件来初始化所有属性内容
     *
     * @param f 用于初始化的文件
     */
    public VersionInventory(File f) {
        super(f);
    }

    public String getSummary() {
        return singleAttributes.get("summary");
    }

    public String getDescription() { return singleAttributes.get("description"); }

    public Iterable<String> getChangedFilesName() {
        return multiAttributes.get("changedFiles");
    }


    public void write() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println(text[0] + singleAttributes.get("summary"));
            out.println(text[1] + singleAttributes.get("description"));

            out.println(text[2]);
            for (String s : multiAttributes.get("changedFiles"))
                out.println(s);

            out.println(text[3]);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
