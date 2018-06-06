package repo;

import java.util.ArrayList;

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
