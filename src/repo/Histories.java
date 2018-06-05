package repo;

import repo.version.Version;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 所有历史记录
 */
class Histories {
    /**
     * 版本时间线
     */
    private ArrayList<String> timeLines = new ArrayList<>();

    /**
     * 版本对照表
     */
    private HashMap<String, Version> versions = new HashMap<>();

}
