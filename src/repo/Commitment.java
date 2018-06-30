package repo;

import java.util.ArrayList;

/**
 *用与向版本库Repository类提交的数据结构，其中包含
 *了需要更改的文件名、需要删除的文件名、总结、描述。
 */
public class Commitment {
    public ArrayList<String> filesToChange;
    public ArrayList<String> filesToDelete;
    public String summary;
    public String description;
    public String versionName = null;

    public Commitment(String summary, String description, ArrayList<String> filesToChange,
                      ArrayList<String> filesToDelete) {
        this.summary = summary;
        this.description = description;
        this.filesToChange = filesToChange;
        this.filesToDelete = filesToDelete;
    }

    public void setVersionName(String s) {
        versionName = s;
    }

}
