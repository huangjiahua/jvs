package repo;

import java.io.File;

public class RepoInventory extends InformationFile {

    /**
     * 通过一个文件来初始化所有属性内容
     *
     * @param f 用于初始化的文件
     */
    public RepoInventory(File f) {
        super(f);
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
}
