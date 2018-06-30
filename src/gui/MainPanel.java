package gui;

import comparator.SingleFileChanges;
import javafx.util.Pair;
import repo.Commitment;
import repo.RepoAlreadyExistException;
import repo.Repository;
import repo.version.Version;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class MainPanel {
    public JPanel Main;
    private JPanel UpBar;
    private JPanel BarLeft;
    private JPanel Left;
    private JPanel HistoryBar;
    private JPanel ProjectBar;
    private JPanel HIstoryPanel;
    private JPanel ProjectPanel;
    private JPanel CenterPanel;
    private JTextArea textArea1;
    private JPanel Panel2;
    private JPanel Panel1;
    private JLabel HistoryTextLabel;
    private JList historiesList;
    private JScrollPane Scroll2;
    private JList changedFiles;
    private JPanel Panel3;
    private JPanel Panel4;
    private JPanel Panel5;
    private JTextField textField1;
    private JPanel Panel6;
    private JPanel Panel7;
    private JButton commitButton;
    private JTextArea textArea2;
    private JScrollPane Scroll3;
    private JScrollPane Scroll1;
    private JLabel freshText;
    private JLabel openText;
    private JLabel projectDir;
    private JTextPane textPane1;
    private JPopupMenu menu1 = new JPopupMenu();
    private JPopupMenu menu2 = new JPopupMenu();
    private JMenuItem revert = new JMenuItem("回到该版本");
    private JMenuItem viewChanges = new JMenuItem("查看");
    Color DarkBlue = new Color(36, 41, 46);
    Color LighterBlue = new Color(47, 54, 61);
    Color LightGreen = new Color(230, 255, 237);
    Color LightPink = new Color(255, 238, 240);

    // file chooser
    JFileChooser chooser = new JFileChooser();
    Path projectPath;

    // repository
    Repository repo = null;
    SingleFileChanges[] changedFilesNames;
    Version[] historiesNames;

    private long time = -1;




    public MainPanel() {

        textPane1.setBorder(new LineNumberBorder());
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        textField1.setText("请总结");
        textArea2.setText("请具体描述");
        textField1.setForeground(Color.GRAY);
        textArea2.setForeground(Color.GRAY);




        viewChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = changedFiles.getSelectedIndex();
                loadEditor(changedFilesNames[i]);
            }

        });
        menu1.add(viewChanges);
        menu2.add(revert);
        HistoryBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = e.getPoint();
                if (HistoryBar.contains(p)) {
                    HistoryBar.setBackground(LighterBlue);
                }
                if (p.x < 10 || p.y < 10 || p.x > 132 || p.y > 65)
                    HistoryBar.setBackground(DarkBlue);
            }
        });
        ProjectBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = e.getPoint();
                if (ProjectBar.contains(p)) {
                    ProjectBar.setBackground(LighterBlue);
                }
                if (p.x < 10 || p.y < 10 || p.x > 132 || p.y > 65)
                    ProjectBar.setBackground(DarkBlue);
            }
        });
        ProjectBar.addMouseMotionListener(new MouseMotionAdapter() {
        });
        ProjectBar.addMouseListener(new MouseAdapter() {
            /**
             * 打开版本库
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                chooser.showOpenDialog(new JFrame());
                try {
                    projectPath = chooser.getSelectedFile().toPath();
                } catch (NullPointerException npe) {
                    return ;
                }
                System.out.println(projectPath);
                try {
                    repo = new Repository(projectPath);
                    time = projectPath.toFile().lastModified();
                    setupRepository();
                } catch (FileNotFoundException e1) {
                    System.out.println("Create new");
                    int i = JOptionPane.showConfirmDialog(Main, "未发现可用版本库，是否创建?", "问题",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    try {
                        if (i == 0) {
                            Repository.initiate(projectPath);
                            repo = new Repository(projectPath);
                            time = projectPath.toFile().lastModified();
                        }
                    } catch (RepoAlreadyExistException e2) {
                        System.out.println("Not possible");
                    } catch (FileNotFoundException e2) {
                        System.out.println("Not possible");
                    }
                }
            }



        });
        HistoryBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    reset();
                } catch (FileNotFoundException e1) {
                    int pane = JOptionPane.showConfirmDialog(Main, "版本库意外丢失，是否重建?", "问题",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    try {
                        Repository.initiate(projectPath);
                        repo = new Repository(projectPath);
                        time = projectPath.toFile().lastModified();
                    } catch (RepoAlreadyExistException e2) {
                        System.out.println("Not possible");
                    } catch (FileNotFoundException e2) {
                        System.out.println("Not possible");
                    }
                }
            }
        });
        changedFiles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 3 && changedFiles.getSelectedIndices().length == 1)
                    menu1.show(changedFiles, e.getX(), e.getY());
            }
        });

        Style def = textPane1.getStyledDocument().addStyle(null, null);
        StyleConstants.setFontFamily(def, "verdana");
        StyleConstants.setFontSize(def, 12);
        Style normal = textPane1.addStyle("normal", def);
        Style s = textPane1.addStyle("red", normal);
        Style g = textPane1.addStyle("green", normal);
        StyleConstants.setBackground(s, LightPink);
        StyleConstants.setBackground(g, LightGreen);
        textPane1.setParagraphAttributes(normal, true);
        historiesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 3 && historiesList.getSelectedIndices().length == 1)
                    menu2.show(historiesList, e.getX(), e.getY());
            }
        });
        revert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(Main, "是否确定回到此版本?", "问题",
                        JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (i == 0) {
                    System.out.println("Revert here");
                    int j = historiesList.getSelectedIndex();
                    Version v = historiesNames[j];
                    repo.backTo(v.getID());
                    try {
                        reset();
                        textField1.setText("回到 " + v.getSummary());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                checkAvailable();
                if (textField1.getText().equals("请总结")) {
                    textField1.setText("");
                    textField1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                checkAvailable();
                if (textField1.getText().equals("")) {
                    textField1.setText("请总结");
                    textField1.setForeground(Color.GRAY);
                }
            }
        });
        textArea2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                checkAvailable();
                if (textArea2.getText().equals("请具体描述")) {
                    textArea2.setText("");
                    textArea2.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                checkAvailable();
                if (textArea2.getText().equals("")) {
                    textArea2.setText("请具体描述");
                    textArea2.setForeground(Color.GRAY);
                }
            }
        });
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkAvailable())
                    return ;
                String error = "";
                boolean flag = true;
                int[] selected = changedFiles.getSelectedIndices();
                if (selected.length == 0) {
                    flag = false;
                    error += "请选择更改文件\n";
                }
                String summary = textField1.getText();
                if (summary.equals("请总结")) {
                    flag = false;
                    error += "请输入总结";
                }
                String description = textArea2.getText();
                if (flag == true) {
                    // 提交
                    int i = JOptionPane.showConfirmDialog(Main, "确认提交?", "提示",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (i == 0) {
                        ArrayList<String> filesToChange = new ArrayList<>();
                        ArrayList<String> filesToDelete = new ArrayList<>();
                        for (int j : selected) {
                            SingleFileChanges temp = changedFilesNames[j];
                            if (temp.getDeleteAll())
                                filesToDelete.add(temp.toString());
                            else
                                filesToChange.add(temp.toString());
                        }
                        Commitment com = new Commitment(summary, description, filesToChange,
                                filesToDelete);
                        repo.commit(com);
                        try {
                            reset();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showConfirmDialog(Main, error, "问题",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                }

            }
        });
        changedFiles.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                checkAvailable();
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
            }
        });
        historiesList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                checkAvailable();
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
            }
        });
    }

    private void loadEditor(SingleFileChanges changedFilesName) {
        textPane1.setText("");
        for (Pair<Character, String> p : changedFilesName.getChanges()) {
            String content = p.getKey() + p.getValue();
            String color;
            switch (p.getKey()) {
                case '+': color = "green"; break;
                case '-': color = "red"; break;
                default: color = "normal";
            }
            try {
                textPane1.getDocument().insertString(textPane1.getDocument().getLength(),
                        content + "\n", textPane1.getStyle(color));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkAvailable() {
        if (isLoaded()) {
            File dir = projectPath.toFile();
            File r = Paths.get(projectPath.toString(), Repository.directoryName).toFile();
            if (!dir.exists()) {
                JOptionPane.showConfirmDialog(Main, "文件目录意外丢失，即将重置!", "问题",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                reset(1);
                return false;
            }
            if (!r.exists()) {
                int i = JOptionPane.showConfirmDialog(Main, "版本库意外丢失，是否重建?", "问题",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (i == 0) {
                    try {
                        Repository.initiate(projectPath);
                        reset();
                    } catch (RepoAlreadyExistException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
            long curr = dir.lastModified();
            if (curr != time) {
                JOptionPane.showConfirmDialog(Main, "文件已经修改，将立即刷新", "问题",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                try {
                    reset();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 将版本库信息显示在各列表中
     */
    private void setupRepository() {
        changedFilesNames = repo.getChangedList();
        historiesNames = repo.getHistories();
        changedFiles.setVisibleRowCount(changedFilesNames.length);
        changedFiles.setListData(changedFilesNames);
        System.out.println("\n\n" + changedFilesNames.length + "\n\n");
        historiesList.setListData(historiesNames);
        projectDir.setText("     " + projectPath.toAbsolutePath().toString());
        textPane1.setText("");
        textField1.setText("请总结");
        textArea2.setText("请详细描述");
    }

    private synchronized boolean isLoaded() {
        return repo != null;
    }

    private void reset() throws FileNotFoundException {
        if (isLoaded()) {
            repo = new Repository(projectPath);
            time = projectPath.toFile().lastModified();
            setupRepository();
        }

    }

    private void reset(int i) {
        repo = null;
        historiesList.setListData(new String[0]);
        changedFiles.setListData(new String[0]);
        textArea2.setText("请详细描述");
        textField1.setText("请总结");
        textPane1.setText("");
        time = -1;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JVS");
        frame.setContentPane(new MainPanel().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
