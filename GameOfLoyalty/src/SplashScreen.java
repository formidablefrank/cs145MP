import java.awt.*;
import javax.swing.*;

public class SplashScreen implements java.io.Serializable
{
    private Frame frame = new Frame();
    private Panel panel = new Panel();
    BoardGUI gui1 = new BoardGUI();
    public void go()
    {
        frame.setVisible(true);
        frame.setContentPane(panel);
        try{Thread.sleep(2000);}
        catch(Exception ex){}
        frame.dispose();
        gui1.go();
    }
    
    class Frame extends JFrame
    {
        private Image icon = new ImageIcon("source/icon.png").getImage();
        
        public Frame()
        {
            super("Welcome!");
            this.setSize(405,328);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setIconImage(icon);
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setSize(405,328);
            this.setLocationRelativeTo(null);
        }
    }
    
    class Panel extends JPanel
    {
        Image bgimg = new ImageIcon("source/splash.png").getImage();
        
        public void paintComponent(Graphics g)
        {
            g.drawImage(bgimg,0,0,this);
        }
    }
}