package comparator;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 用于对两个文件进行比较，比较算法见本文档的算法部分。
 */
public class FileComparator {
    private File file1, file2;
    private ArrayList<Pair<Character, String>> changes;
    private boolean equal = true;

    // data
    public final static char ADD = '+';
    public final static char DELETE = '-';
    public final static char STAY = '=';
    public final static char MODIFY = '/';

    public FileComparator(File a, File b) {
        file1 = a;
        file2 = b;
        ArrayList<String> src = fileToLines(file1);
        ArrayList<String> dst = fileToLines(file2);

        changes = compare(src, dst);
    }


    /**
     * 将文件转换成字符串数组
     * @param f 输入文件
     * @return 输出字符串数组
     */
    private ArrayList<String> fileToLines(File f) {
        ArrayList<String> ret = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                ret.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * 将两个字符串数组进行比较并输出结果
     * @param src 原文件形成的字符串数组
     * @param dst 当前文件形成的字符串数组
     * @return 格式化的文件区别数组
     */
    private ArrayList<Pair<Character, String>> compare(ArrayList<String> src, ArrayList<String> dst) {
        ArrayList<Pair<Character, String>> ret = new ArrayList<>();

        int[][] mat = getLDMat(src, dst);
        int next;

        // test ----------------------------
        /*
        for (int[] n : mat) {
            for (int i : n) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        */
        // test ----------------------------

        String[] s1 = new String[Integer.max(src.size(), dst.size())];
        String[] s2 = new String[s1.length];
        for (int i = 0; i < s1.length; ++i) {
            s1[i] = " ";
            s2[i] = " ";
        }

        for (int i = src.size(), j = dst.size(), k = s1.length - 1;
             (i != 0 || j != 0); --k) {
            if (i == 0) {
                s1[k] = " ";
                s2[k] = dst.get(j-1);
                --j;
                continue;
            }
            if (j == 0) {
                s1[k] = src.get(i-1);
                s2[k] = " ";
                --i;
                continue;
            }
            next = Integer.min(mat[i-1][j-1], Integer.min(mat[i][j-1], mat[i-1][j]));
            if (k == -1) break;
            if (next == mat[i-1][j-1]) {
                s1[k] = src.get(i-1);
                s2[k] = dst.get(j-1);
                --i;
                --j;
            } else if (next == mat[i][j-1]) {
                s2[k] = dst.get(j-1);
                s1[k] = " ";
                --j;
            } else {
                s1[k] = src.get(i-1);
                s2[k] = " ";
                --i;
            }
        }

        for (int i = 0; i < s1.length; ++i) {
            if (s1[i].equals(s2[i])) {
                ret.add(new Pair<>(STAY, s2[i]));
            } else if (s1[i].equals(" ")) {
                ret.add(new Pair<>(ADD, s2[i]));
                equal = false;
            } else if (s2[i].equals(" ")) {
                ret.add(new Pair<>(DELETE, s1[i]));
                equal = false;
            } else {
                ret.add(new Pair<>(MODIFY, s2[i]));
                equal = false;
            }

        }

        // test ----------------------------
        /*
        for (Pair<Character, String> p : ret) {
            System.out.println(p.getKey() + " " + p.getValue());
        }
        */
        // test ----------------------------

        return ret;
    }

    /**
     * 返回LD距离矩阵
     * @param l
     * @param r
     * @return
     */
    private static int[][] getLDMat(ArrayList<String> l, ArrayList<String> r) {
        int[][] ret = new int[l.size() + 1][r.size() + 1];

        // 初始化
        for (int i = 0; i < l.size() + 1; ++i) {
            for (int j = 0; j < r.size() + 1; ++j) {
                if (j == 0) {
                    ret[i][j] = i;
                } else if (i == 0) {
                    ret[i][j] = j;
                } else {
                    ret[i][j] = -1;
                }
            }
        }

        // 计算
        for (int i = 1; i < l.size() + 1; ++i) {
            for (int j = 1; j < r.size() + 1; ++j) {
                if (l.get(i-1).equals(r.get(j-1)))
                    ret[i][j] = ret[i-1][j-1];
                else
                    ret[i][j] = Integer.min(Integer.min(ret[i-1][j-1], ret[i-1][j]), ret[i][j-1]) + 1; // 三个中的最小
            }
        }

        return ret;
    }

    public ArrayList<Pair<Character, String>> getChanges() {
        return changes;
    }

    public boolean equal() { return equal; }

    public static void main(String[] args) {
        /*
        String[] s1 = {"G", "G", "A", "T", "C", "G", "A"};
        String[] s2 = {"G", "A", "A", "T", "T", "C", "A", "G", "T", "T", "A"};
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> r = new ArrayList<>();
        Collections.addAll(l, s1);
        Collections.addAll(r, s2);

        FileComparator.compare(l, r);
        */
    }
}
