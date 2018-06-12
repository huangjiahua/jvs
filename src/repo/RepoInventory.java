package repo;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RepoInventory extends InformationFile {

    private final static String fileName = "inventory.txt";
    private final static String[] text = {"histories = {",
                                          "};",
                                          "lastFiles = {",
                                          "};"};

    // Data
    /**
     * 通过一个文件来初始化所有属性内容
     *
     * @param f 用于初始化的文件
     */
    public RepoInventory(File f) {
        super(f);
    }

    public RepoInventory(Path path) {
        super(path);
        file = Paths.get(path.toString(), fileName).toFile();
        String line;

        ArrayList<String> historyNames = new ArrayList<>();
        ArrayList<String> lastFiles = new ArrayList<>();
        try {
            FileReader fin = new FileReader(file);
            BufferedReader in = new BufferedReader(fin);
            // Read histories
            in.readLine();
            System.out.println("Load Histories...");
            while (!(line = in.readLine()).equals("};")) {
                System.out.println(line);
                historyNames.add(line);
            }

            // Read last Files
            in.readLine();
            System.out.println("Load LastFiles...");
            while (!(line = in.readLine()).equals("};")) {
                System.out.println(line);
                lastFiles.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        multiAttributes.put("histories", historyNames);
        multiAttributes.put("lastFiles", lastFiles);
    }


    /**
     * 新建一个inventory.txt
     * @param org
     */
    public static void initiate(Path org) {
        String s = org.toString();
        Path path = Paths.get(s, fileName);
        try {
            File file = path.toFile();
            file.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for (String line : text)
                out.println(line);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkExist(String num) {
        for (String s : multiAttributes.get("histories"))
            if (num.equals(s))
                return true;
        return false;
    }

    public Iterable<String> getCurrentFilesName() {
        return multiAttributes.get("currentFiles");
    }

    public Iterable<String> getHistoriesName() {
        return multiAttributes.get("histories");
    }

    public Iterable<String> getLastFilesName() {
        return multiAttributes.get("lastFiles");
    }

    public void update(Commitment commitment) {
        multiAttributes.get("histories").add(commitment.versionName);

        ArrayList<String> temp = multiAttributes.get("lastFiles");
        for (String s : commitment.filesToDelete) {
            if (temp.contains(s))
                temp.remove(s);
        }

        for (String s : commitment.filesToChange) {
            if (!temp.contains(s))
                temp.add(s);
        }

        update();
    }

    private void update() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println(text[0]);
            for (String s : multiAttributes.get("histories"))
                out.println(s);
            out.println(text[1]);

            out.println(text[2]);
            for (String s : multiAttributes.get("lastFiles"))
                out.println(s);
            out.println(text[3]);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
