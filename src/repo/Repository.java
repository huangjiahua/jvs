package repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * 版本库
 */
public class Repository {

    private static final String directoryName = "jvs";
    private static final String lastFilesDirName = "lastFiles";
    private Path path;

    /**
     * 根据文件目录创建版本库
     */
    public Repository(Path org) throws FileNotFoundException {
        path = Paths.get(org.toAbsolutePath().toString(), directoryName);
        if (!Files.exists(path))
            throw new FileNotFoundException(".jvs not found");

        // 创建目录项
        this.inventory = new RepoInventory(path);

        // 创建历史项
        this.histories = new Histories(this.inventory, path);

        // 创建之前文件项
        Path lastFilesPath = Paths.get(path.toString(), lastFilesDirName);
        File lastFilesDir  = lastFilesPath.toFile();
        for (String fileName : lastFilesDir.list()) {
            Path filePath = Paths.get(lastFilesPath.toString(), fileName);
            if (!Files.isDirectory(filePath)) {
                System.out.println(filePath);
                lastFiles.put(fileName, filePath.toFile());
            }
        }


        // 创建当前文件项
        File currentFilesDir = org.toFile();
        for (String fileName : currentFilesDir.list()) {
            Path filePath = Paths.get(org.toString(), fileName);
            try {
                if (!Files.isDirectory(filePath) && !Files.isHidden(filePath)) {
                    System.out.println(filePath);
                    currentFiles.put(fileName, filePath.toFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 初始化版本库
     */
    public static void initiate(Path org) throws RepoAlreadyExistException {
        String s = org.toAbsolutePath().toString();
        Path dir = Paths.get(s, directoryName);
        // 创建目录
        try {
            Files.createDirectory(dir);
        } catch (IOException e) {
            throw new RepoAlreadyExistException();
        }

        // 创建inventory
        RepoInventory.initiate(dir);

        // 创建lastFiles
        Path lastFilesDir = Paths.get(dir.toString(), lastFilesDirName);
        try {
            Files.createDirectory(lastFilesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建histories
        Histories.initiate(dir);

    }

    /**
     * 删除版本库
     */
    public static void removeRepository() {
        // ...
    }


    /**
     * 版本库目录
     */
    private RepoInventory inventory;

    /**
     * 当前文件
     */
    private HashMap<String, File> currentFiles = new HashMap<>();

    /**
     * 上一版文件
     */
    private HashMap<String, File> lastFiles = new HashMap<>();

    /**
     * 历史记录
     */
    private Histories histories;

    public static void main(String[] args) {


        try {
            Repository r = new Repository(Paths.get("test"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
