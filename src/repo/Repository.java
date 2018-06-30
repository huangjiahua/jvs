package repo;

import comparator.DirectoryComparator;
import comparator.SingleFileChanges;
import repo.version.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

/**
 * 1. 管理版本库的类。其中包含了一个RepoInventory来管理目录，一个Histories来管理历史数据，一个哈希表来管理当前的文件，
 * 一个哈希表来管理上一版文件。
 * 2. 包含静态方法用于新建版本库。
 */
public class Repository {

    public static final String directoryName = ".jvs";
    private static final String lastFilesDirName = "lastFiles";
    private Path path, originalPath;
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

    /**
     * 文件比较
     */
    private DirectoryComparator dirComparator;

    private Path lastPath;



    /**
     * 根据文件目录创建版本库
     */
    public Repository(Path org) throws FileNotFoundException {
        originalPath = org;
        path = Paths.get(org.toAbsolutePath().toString(), directoryName);
        if (!Files.exists(path))
            throw new FileNotFoundException(".jvs not found");

        // 创建目录项
        this.inventory = new RepoInventory(path);

        // 创建历史项
        this.histories = new Histories(this.inventory, path);

        // 创建之前文件项
        Path lastFilesPath = Paths.get(path.toString(), lastFilesDirName);
        lastPath = lastFilesPath;
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
                    if (fileName.charAt(0) == '.')
                        continue;
                    System.out.println(filePath);
                    currentFiles.put(fileName, filePath.toFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 比较文件
        dirComparator = new DirectoryComparator(lastFiles, currentFiles);

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

    public void commit(Commitment commitment) {
        Random ran = new Random();
        String newName;
        while (inventory.checkExist((newName = Integer.toString(Math.abs(ran.nextInt())))))
            ;
        System.out.println(newName);
        commitment.setVersionName(newName);

        inventory.update(commitment);

        histories.update(commitment, dirComparator);

        try {
            Path temp = Paths.get(path.toString(), "histories", newName, lastFilesDirName);
            Files.createDirectory(temp);
            for (String s : lastFiles.keySet())
                Files.copy(lastFiles.get(s).toPath(), Paths.get(temp.toString(), s));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : commitment.filesToDelete) {
            try {
                Files.delete(lastFiles.get(s).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String s : commitment.filesToChange) {
            try {
                if (lastFiles.containsKey(s))
                    Files.delete(lastFiles.get(s).toPath());
                Files.copy(currentFiles.get(s).toPath(), Paths.get(lastPath.toString(), s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除版本库
     */
    public static void removeRepository() {
        // ...
    }

    /**
     * 版本回退
     * @param version
     * @return
     */
    public boolean backTo(String version) {
        if (!histories.existVersion(version)) return false;

        if (histories.isLatest(version)) {
            for (File f : currentFiles.values()) {
                try {
                    Files.delete(f.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : lastFiles.keySet()) {
                Path newPath = Paths.get(originalPath.toString(), s);
                try {
                    Path temp = lastFiles.get(s).toPath();
                    if(!Files.isHidden(temp) && !Files.isDirectory(temp)) {
                        Files.copy(temp, newPath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            String nextVersion = histories.getNextVersion(version);
            Path target = Paths.get(path.toString(), "histories", nextVersion, "lastFiles");
            File dir = target.toFile();
            String[] filesName = dir.list();
            for (File f : currentFiles.values()) {
                try {
                    Files.delete(f.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : filesName) {
                Path oldPath = Paths.get(target.toString(), s);
                Path newPath = Paths.get(originalPath.toString(), s);
                try {

                    if(!Files.isHidden(oldPath) && !Files.isDirectory(oldPath)) {
                        if (Files.exists(newPath))
                            Files.delete(newPath);
                        Files.copy(oldPath, newPath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }


    public SingleFileChanges[] getChangedList() {
        return dirComparator.getChangedList();
    }

    public static void main(String[] args) {
        try {
            Repository r = new Repository(Paths.get("test"));
            r.backTo("133818106");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Version[] getHistories() {
        return histories.getVersions();
    }
}
