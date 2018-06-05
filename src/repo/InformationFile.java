package repo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 一个包含信息文件所有属性的类
 */
public class InformationFile {
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

    /**
     * 更新（写入）文件
     */
    public void update() {
        // ...
    }




}
