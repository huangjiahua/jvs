package repo.version;

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
}
