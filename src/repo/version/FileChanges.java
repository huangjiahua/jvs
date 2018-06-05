package repo.version;

import javafx.util.Pair;
import repo.InformationFile;

import java.io.File;

public class FileChanges extends InformationFile {
    private Pair<Character, String>[] lines;

    /**
     * 通过一个文件来初始化所有属性内容
     *
     * @param f 用于初始化的文件
     */
    public FileChanges(File f) {
        super(f);
    }


}
