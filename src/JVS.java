import gui.MainPanel;

import javax.swing.*;

public class JVS {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JVS");
        frame.setContentPane(new MainPanel().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
