import gui.MainPanel;

import javax.swing.*;
import java.awt.*;

public class JVS {
    public static void main(String[] args) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        JFrame frame = new JFrame("JVS");
        frame.setContentPane(new MainPanel().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }
}
