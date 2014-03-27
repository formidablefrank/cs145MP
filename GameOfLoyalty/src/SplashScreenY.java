import java.awt.*;
import javax.swing.*;

public class SplashScreenY implements java.io.Serializable
{
    private Frame frame = new Frame();
    private Panel panel = new Panel();
    BoardGUIY guiY = new BoardGUIY();
    public void go(JButton btn)
    {
        frame.setVisible(true);
        frame.setContentPane(panel);
        try{Thread.sleep(2000);}
        catch(Exception ex){}
        frame.dispose();
        guiY.go(btn);
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