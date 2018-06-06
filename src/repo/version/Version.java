package repo.version;

import comparator.DirectoryComparator;
import repo.Commitment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * 一个文件版本的类
 */
public class Version {
    /**
     * 目录
     */
    private VersionInventory inventory;

    /**
     * 每个变化文件的集合
     */
    private HashMap<String, FileChanges> files = new HashMap<>();
    private Path path;


    public Version(String s, Path path) {
        this.path = Paths.get(path.toString(), s);
        inventory = new VersionInventory(this.path);

        for (String str : inventory.getChangedFilesName()) {
            files.put(str, new FileChanges(this.path, str));
        }
    }

    public Version(Commitment commitment, DirectoryComparator dirComparator, Path path) {
        this.path = Paths.get(path.toString(), commitment.versionName);
        inventory = new VersionInventory(commitment, this.path);

        for (String name : inventory.getChangedFilesName()) {
            FileChanges f = new FileChanges(name, dirComparator.getSingleChanges(name), this.path);
            files.put(name, f);
        }
    }

    public void write() {
        try {
            Files.createDirectory(path);
            inventory.write();
            for (FileChanges fc : files.values()) {
                fc.write();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
