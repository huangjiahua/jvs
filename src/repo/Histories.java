package repo;

import comparator.DirectoryComparator;
import repo.version.Version;
import repo.version.VersionInventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 所有历史记录
 */
class Histories {
    private final static String historiesName = "histories";
    private final static String firstName = "0";
    /**
     * 版本时间线
     */
    private ArrayList<String> timeLines = new ArrayList<>();

    /**
     * 版本对照表
     */
    private HashMap<String, Version> versions = new HashMap<>();

    private Path path;

    public Histories(RepoInventory inventory, Path org) {
        path = Paths.get(org.toString(), historiesName);

        for (String s : inventory.getHistoriesName()) {
            timeLines.add(s);
            versions.put(s, new Version(s, path));
        }


    }

    public static void initiate(Path org) {
        // 创建目录
        String s = org.toString();
        Path dir = Paths.get(s, historiesName);
        try {
            Files.createDirectory(dir);
            // 创建初始版本
            Path firstDir = Paths.get(dir.toString(), firstName);
            Files.createDirectory(firstDir);
            VersionInventory.initiate(firstDir);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update(Commitment commitment, DirectoryComparator dirComparator) {
        Version v = new Version(commitment, dirComparator, path);
        v.write();
    }

    /**
     * 检查当前版本号是否存在
     * @param version
     * @return
     */
    public boolean existVersion(String version) {
        return timeLines.contains(version);
    }

    /**
     * 检查当前版本是否为最新的版本
     * @param version
     * @return
     */
    public boolean isLatest(String version) {
        if (timeLines.isEmpty())
            return false;
        return version.equals(timeLines.get(timeLines.size()-1));
    }

    /**
     * 获取下一个版本
     * @param version
     * @return
     */
    public String getNextVersion(String version) {
        int i = timeLines.indexOf(version);
        if (i != timeLines.size() - 1)
            return timeLines.get(i + 1);
        else
            return null;
    }
}
