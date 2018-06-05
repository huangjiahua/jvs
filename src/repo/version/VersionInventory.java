package repo.version;

import repo.InformationFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
            System.out.println("Description: " + description);

            in.readLine();
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


}
