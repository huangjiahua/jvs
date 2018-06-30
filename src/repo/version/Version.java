package repo.version;

import comparator.DirectoryComparator;
import repo.Commitment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * 管理每一个版本信息，包含了这个版本的id，这个版本的目录 VersionInventory，和用于管理这个版本里所有文件的哈希表。
 */
public class Version {
    /**
     * 目录
     */
    private VersionInventory inventory;

    private String id = null;

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

    public String getSummary() {
        return inventory.getSummary();
    }

    public String toString() {
        return inventory.getSummary();
    }

    public String getID() {
        return id;
    }

    public void setID(String s) {
        id = s;
    }
}
