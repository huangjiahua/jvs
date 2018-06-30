package repo;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 这是一个抽象类，用于给各种根据版本库文件中的文本文件生成的管理数据的类作为超类的类。
 * 其中包含了用于查找单一属性的哈希表和用于查找复合属性的哈希表。
 */
public abstract class InformationFile {
    /**
     * 包含单条信息的单属性
     */
    protected HashMap<String, String> singleAttributes = new HashMap<>();
    /**
     * 包含多条信息的属性
     */
    protected HashMap<String, ArrayList<String>> multiAttributes = new HashMap<>();
    /**
     * 源文件
     */
    protected File file;

    /**
     * 通过一个文件来初始化所有属性内容
     * @param f 用于初始化的文件
     */
    public InformationFile(File f) {
        file = f;
        // ...
    }

    public InformationFile(Path f) {
    }

}
