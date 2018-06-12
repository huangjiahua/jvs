package gui;

import repo.RepoAlreadyExistException;
import repo.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class MainPanel {
    private JPanel Main;
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
    private JList list1;
    private JScrollPane Scroll2;
    private JScrollPane scrollPane3;
    private JList list2;
    private JPanel Panel3;
    private JPanel Panel4;
    private JPanel Panel5;
    private JTextField textField1;
    private JPanel Panel6;
    private JPanel Panel7;
    private JButton commitButton;
    private JTextArea textArea2;
    private JScrollPane Scroll3;
    private JEditorPane editorPane1;
    private JScrollPane Scroll1;
    private JLabel freshText;
    private JLabel openText;
    Color DarkBlue = new Color(36, 41, 46);
    Color LighterBlue = new Color(47, 54, 61);

    // file chooser
    JFileChooser chooser = new JFileChooser();
    Path projectPath;

    // repository
    Repository repo = null;



    public MainPanel() {
        editorPane1.setBorder(new LineNumberBorder());

        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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
                projectPath = chooser.getSelectedFile().toPath();
                System.out.println(projectPath);
                try {
                    repo = new Repository(projectPath);
                } catch (FileNotFoundException e1) {
                    System.out.println("Create new");
                    JOptionPane.showConfirmDialog(new Panel(), "未发现可用版本库，是否创建?", "问题",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    try {
                        Repository.initiate(projectPath);
                        repo = new Repository(projectPath);
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
                    JOptionPane.showConfirmDialog(new Panel(), "版本库意外丢失，是否重建?", "问题",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    try {
                        Repository.initiate(projectPath);
                        repo = new Repository(projectPath);
                    } catch (RepoAlreadyExistException e2) {
                        System.out.println("Not possible");
                    } catch (FileNotFoundException e2) {
                        System.out.println("Not possible");
                    }
                }
            }
        });
    }

    private boolean isLoaded() {
        return repo != null;
    }

    private void reset() throws FileNotFoundException {
        if (isLoaded())
            repo = new Repository(projectPath);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPanel");
        frame.setContentPane(new MainPanel().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
