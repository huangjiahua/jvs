package comparator;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DirectoryComparator {
    private HashMap<String, SingleFileChanges> fileChanges = new HashMap<>();

    public DirectoryComparator(HashMap<String, File> last, HashMap<String, File> curr) {
       // 第一次查询
        for (String name : curr.keySet()) {
            if (last.containsKey(name)) {
                FileComparator fc = new FileComparator(last.get(name), curr.get(name));
                fileChanges.put(name, new SingleFileChanges(fc, name));
            } else {
                ArrayList<Pair<Character, String>> temp = getAll(FileComparator.ADD, curr.get(name));
                fileChanges.put(name, new SingleFileChanges(temp, name));
            }
        }

        // 第二次查询
        for (String name : last.keySet()) {
            if (!curr.containsKey(name)) {
                ArrayList<Pair<Character, String>> temp = getAll(FileComparator.DELETE, last.get(name));
                fileChanges.put(name, new SingleFileChanges(temp, name, true));
            }
        }

        // test --------------
        for (Map.Entry<String, SingleFileChanges> e : fileChanges.entrySet()) {
            System.out.println(e.getKey());
            System.out.print(e.getValue());
        }

        // test --------------

    }

    public SingleFileChanges getSingleChanges(String name) {
        return fileChanges.get(name);
    }


    private static ArrayList<Pair<Character, String>> getAll(char token, File f) {
        ArrayList<Pair<Character, String>> ret = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null)
                ret.add(new Pair<>(token, line));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
