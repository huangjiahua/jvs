package repo;

import java.io.File;
import java.util.HashMap;

/**
 * 版本库
 */
public class Repository {

    /**
     * 初始化版本库
     */
    public static void initiateRepository() {
        // ...
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


}
