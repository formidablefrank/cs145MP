import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class BoardGUI implements Serializable
{
    public static JLabel status = new JLabel("Let's Play! Click the board to start playing.",SwingConstants.RIGHT);
    Frame frame = new Frame();
    static Board gui = new Board();
    static Settings set = new Settings();
    public static int aiDiff = 5;
    public void go()
    {
        frame.setVisible(true);
    }
    
    class Frame extends JFrame
    {
        private Menu menu = new Menu();
        private Image icon = new ImageIcon("source/icon.png").getImage();
        private TabPane tab = new TabPane();
        
        public Frame()
        {
            super("Game of Loyalty");
            this.setSize(600,600);
            this.setLocationRelativeTo(null);
            this.setAlwaysOnTop(true);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setResizable(false);
            this.setIconImage(icon);
            this.setJMenuBar(menu);
            this.add(tab,BorderLayout.CENTER);
            this.add(status,BorderLayout.SOUTH);           
        }
        
        
        class Menu extends JMenuBar
        {
            private JMenu menus[] = new JMenu[3];
            private String strmenus[] = {"New","About","Exit"};
            private JMenuItem menuitems[] = new JMenuItem[4];
            private String strmenuitems[] = {"Game vs. Computer","About Game of Loyalty..","Get more games online..","Exit game"};
            
            public Menu()
            {
                for(int i=0;i<strmenus.length;i++)
                {
                    menus[i] = new JMenu(strmenus[i]);
                    menus[i].setMnemonic(strmenus[i].charAt(0));
                    this.add(menus[i]);
                }
                
                for(int i=0;i<strmenuitems.length;i++)
                {
                    menuitems[i] = new JMenuItem(strmenuitems[i]);
                    menuitems[i].setMnemonic(strmenuitems[i].charAt(0));
                    menuitems[i].addActionListener(new MenuListener());
                    if(i==0)menus[0].add(menuitems[i]);
                    else if(i==1||i==2)menus[1].add(menuitems[i]);
                    else if(i==3)menus[2].add(menuitems[i]);
                }
            }
            
            class MenuListener implements ActionListener
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(e.getSource() == menuitems[3])
                    {
                        int res = JOptionPane.showConfirmDialog(null,"Do you really want to exit?","Confirm Exit",JOptionPane.OK_CANCEL_OPTION);
                        if(res == JOptionPane.OK_OPTION) System.exit(0);
                    }
                    else if(e.getSource() == menuitems[2])
                    {
                        try
                        {
                            String url = "http://games.brothersoft.com";
                            Desktop.getDesktop().browse(java.net.URI.create(url));
                        }
                        catch (Exception ex) { JOptionPane.showMessageDialog(frame,"System error, cannot open desktop browser.!","Fatal error!",JOptionPane.ERROR_MESSAGE); }
                    }
                    else if(e.getSource() == menuitems[1])
                    {
                        JOptionPane.showMessageDialog(frame,"The Game of Loyalty v1.0\nCreated by bosz.frank_07\nAll rights reserved.\nfb.com/bosz.frank07","About Game of Loyalty..",JOptionPane.INFORMATION_MESSAGE,new icon(icon));
                    }
                    else if(e.getSource() == menuitems[0])
                    {
                        gui.removeAll();
                        gui.moves = 0;
                        Arbiter.isWin = false;
                        gui.initialize();
                        gui.revalidate();
                        gui.repaint();
                        tab.setSelectedComponent(gui);
                    }
                }
            }
        }
        
        class TabPane extends JTabbedPane
        {
            private String strpanels[] = {"Board","Settings","Help","Statistics","Help"};
            private JPanel panels[] = {gui,set,new Help()};
            
            public TabPane()
            {
                this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                this.setTabPlacement(JTabbedPane.BOTTOM);
                this.setForeground(Color.blue.brighter());
                this.setBackground(Color.black);
                
                for(int i=0;i<panels.length;i++)
                {
                    this.add(strpanels[i],panels[i]);
                }
            }
            
            class Help extends JPanel
            {
                private String strhelp[] = {
                    String.format("The BOARD\n\n- The game board is an 8x8 block of squares.\n"
                        + "- The upper half (top two rows) is the initial territory of the computer opponent.\n"
                        + "- The lower half (bottom two rows) is the initial territory of the player.\n\n"
                        + "1. Game pieces\n- There are a total of 16 playable pieces for each player.\n"
                        + "- 6 Loyal, 4 Passionate, 3 Wise, 2 Strong, 1 Flag.\n"
                        + "- Locations of these pieces are decided randomly, but should be within the bounds of their initial territory\n\n"
                        + "2. Concealed opponent pieces\n"
                        + "- Opponent pieces are concealed all the time (except at the end of the game).\n"
                        + "- The arbiter (the one who decides which piece wins against another) does not tell the pieces being played by the computer.\n"
                        + "- The computer player, likewise, does not have any knowledge of the human pieces in play."),
                    
                    String.format("The PIECES\n\n- There are five distinct pieces in the game:"
                        + "\n\n1.Strong Warrior -> S\n"
                        + "- strongest of all, yet is overtaken by the loyalty of a friend\n"
                        + "- defeats everyone except Loyal Friend; draw against Strong Warrior\n\n"
                        + "2. Wise Oracle -> W\n"
                        + "- wisest of all; outsmarts anyone  but proves no match to the strength of a warrior\n- defeats Passionate Soldier, Loyal Friend and Flag Bearer; draw against Wise Oracle\n\n"
                        + "3.Passionate Soldier -> P\n"
                        + "- most passionate of them all to the point that no man, friend or enemy, can stop him\n"
                        + "- defeats Loyal Friend and Flag Bearer; draw against Passionate Soldier\n\n"
                        + "4.Loyal Friend -> L\n"
                        + "- most loyal of all; not so skillful yet demonstrates a loyalty so genuine that the strongest submits in awe to him\n"
                        + "- defeats Flag Bearer and Strong Warrior; draw against Loyal Friend\n\n"
                        + "5.Flag Bearer -> F\n"
                        + "- as long as he is standing, the war isn't over.\n"
                        + "- defeated by anyone; if Flag Bearer is against another Flag Bearer, the attacking piece (and the attacking player) wins."),
                    
                    String.format("The GAME PLAY\n\n"
                        + "- The game starts with the human player moving an owned piece one block from its location, as long as it is a valid direction (up, down, left, right)\n"
                        + "- Players take turn in moving their owned pieces\n"
                        + "- A player decides to attack an opponent piece by moving an owned piece in the same block of the opponent piece"),
                    
                    String.format("For WINNING\n\n"
                        + "- When a human player captures the flag (defeats the Flag Bearer) of the computer, human wins.\n"
                        + "- When there are only two pieces left in the board (only Flag Bearers remaining), computer wins.\n"
                        + "- When the computer has only one piece left (the Flag Bearer), the computer automatically wins.\n\n\n"
                        + "Game CHEATS\n\n"
                        + "- You can reveal the flag by pressing 'R' in the keyboard.\n"
                        + "- You can also reveal all the pieces of opponent by pressing 'A' in the keyboard.\n"
                        + "- Make your pieces superior (i.e. it cannot be defeated by any opponent's piece) by pressing 'S' in the keyboard.\n")};
                private Text gamehelp[] = {new Text(strhelp[0]),new Text(strhelp[1]),new Text(strhelp[2]),new Text(strhelp[3])};
                
                public Help()
                {
                    for(int i=0;i<strhelp.length;i++)
                        this.add(gamehelp[i]);
                }
                
                public void paintComponent(Graphics g)
                {
                    g.setColor(Color.black);
                    g.fillRect(0,0,getWidth(),getHeight());
                    status.setText("Do you need help?");
                }
                
                class Text extends JPanel
                {
                    private JTextArea txt = new JTextArea(13,19);
                    private JScrollPane scroll = new JScrollPane(txt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    private Insets txtinset = new Insets(15,15,15,15);
                    
                    public Text(String s){txt.setText(s);}
                    
                    public void paintComponent(Graphics g)
                    {
                        this.add(scroll);
                        txt.setDisabledTextColor(Color.black);
                        txt.setMargin(txtinset);
                        txt.setWrapStyleWord(true);
                        txt.setLineWrap(true);
                        txt.setEditable(false);
                        txt.setEnabled(false);
                    }
                }
            }
        }
        
        class icon implements Icon
        {
            private int width;
            private int height;
            private Image iconset;
            
            public int getIconWidth() {return width;}
            public int getIconHeight() {return height;}
            public icon(Image i) {iconset=i; width=i.getHeight(rootPane); height=i.getHeight(rootPane);}
            
            public void paintIcon(Component c, Graphics g, int x, int y)
            {
                g.drawImage(iconset, x, y, rootPane);
            }
        }
    }
}