package repo.version;

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
    private HashMap<String, FileChanges> files;

}
