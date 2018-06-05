package repo.version;

import repo.InformationFile;

import java.io.File;

public class VersionInventory extends InformationFile {
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

    public Iterable<String> getChangedFilesName() {
        return multiAttributes.get("changedFiles");
    }
}
