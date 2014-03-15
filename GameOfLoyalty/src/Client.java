import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client{
    private Socket client;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    private final int port;
    private final String host;
    private String message, username;
    private Frame frame;
    private MessageFrame msgFrame;
    
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    public void runClient(){
        try{
            System.out.println("Attempting connection...");
            client = new Socket(host, port);
            System.out.println("Connected to: " + client.getInetAddress().toString());
            clientOutput = new ObjectOutputStream(client.getOutputStream());
            clientOutput.flush();
            clientInput = new ObjectInputStream(client.getInputStream());
            System.out.println("Got I/O streams");
            Scanner input = new Scanner(System.in);
            message = "";
            username = "";
            //get client username
            do{
                username = JOptionPane.showInputDialog(null, "Enter your username:", "Prompt", JOptionPane.INFORMATION_MESSAGE);
            }while(username.equals(""));
            clientOutput.writeObject(username);
            clientOutput.flush();
            
            frame = new Frame(username);
            
            //start transaction
            do{
                try{
                    message = (String) clientInput.readObject();
                    System.out.println("SERVER: " + message);
                    processMessage(message);
                }
                catch(ClassNotFoundException e){
                    System.out.println("SERVER: Can't understand");
                }
                //message = input.next();
                //clientOutput.writeObject(message);
                //clientOutput.flush();
            }while(!message.equals("TERMINATE"));
        }
        catch(EOFException e){
            System.out.println("error");
        }
        catch (IOException e){
            System.out.println("Server closed");
        }
        finally{
            try{                
                clientOutput.writeObject("TERMINATE!@#"+this.username);
                clientOutput.flush();
                client.close();
                clientInput.close();
                clientOutput.close();
            }
            catch(IOException e){
                System.out.println("******");
            }
        }
        System.exit(0);
    }
    
    private void processMessage(String message){
        String[] command = message.split("!@#");
        switch(command[0]){
            case "USER":{
                frame.tab.cp.addText(command[1] + " is up");
                break;
            }
            case "TERMINATE":{
                frame.tab.cp.addText(command[1] + " left");
                break;
            }
            case "STATUS":{
                frame.tab.cp.addText(command[1] + "'s status:\n" + command[2]);
                break;
            }
            case "LIST":{
                try{
                    JOptionPane.showMessageDialog(frame, command[1], "Online Users", JOptionPane.INFORMATION_MESSAGE);}
                catch(Exception e){
                    JOptionPane.showMessageDialog(frame, "No other users online", "Online Users", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            }
            case "FAILED":{
                String[] temp = command[1].split(";");
                String temp2 = "";
                for (String x: temp){
                    if(x.equals("")){
                        continue;
                    }
                    temp2 += x + "\n";
                }
                JOptionPane.showMessageDialog(msgFrame.panel, "The message failed to sent to the following offline users:\n" + temp2, "Message Sent", JOptionPane.ERROR_MESSAGE);
                break;
            }
            case "SEND":{
                String[] temp = command[2].split(";");
                String temp2 = "to ";
                for(String x: temp){
                    temp2 += x + "; ";
                }
                frame.tab.cp.addText(command[1] + "'s message\n" + temp2 + "\n" + command[3]);
            }
        }
    }
    
    private class Frame extends JFrame{
        private JLabel status = new JLabel("status bar", SwingConstants.RIGHT);
        private Menu menu = new Menu();
        private Image icon = new ImageIcon("source/icon.png").getImage();
        private TabPane tab = new TabPane();
        private String username;
        
        public Frame(String username){
            super("TsikaNet - " + username);
            this.username = username;
            this.setSize(300, 500);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setIconImage(icon);
            this.setJMenuBar(menu);
            this.add(tab, BorderLayout.CENTER);
            this.add(status, BorderLayout.SOUTH);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }
        
        class Menu extends JMenuBar{
            private JMenu menus[] = new JMenu[2];
            private String strMenus[] = {"Menu", "About"};
            private JMenuItem menuItems[] = new JMenuItem[5];
            private String strMenuItems[] = {"Send message", "View online users", "Properties", "Exit", "About TsikaNet..."};
            
            public Menu(){
                for(int i=0; i<2; i++){
                    menus[i] = new JMenu(strMenus[i]);
                    menus[i].setMnemonic(strMenus[i].charAt(0));
                    this.add(menus[i]);
                }
                for(int i=0; i<5; i++){
                    menuItems[i] = new JMenuItem(strMenuItems[i]);
                    menuItems[i].setMnemonic(strMenuItems[i].charAt(0));
                    menuItems[i].addActionListener(new MenuListener());
                    if(i<4) menus[0].add(menuItems[i]);
                    else menus[1].add(menuItems[i]);
                }
            }
            
            class MenuListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e){
                    if(e.getSource() == menuItems[0]){
                        msgFrame = new MessageFrame();
                    }
                    else if(e.getSource() == menuItems[1]){
                        try{
                            clientOutput.writeObject("LIST!@#"+username);
                            clientOutput.flush();
                            //message = (String) clientInput.readObject();
                        }
                        catch(IOException ex){
                            JOptionPane.showMessageDialog(frame,"Error getting list. Please try again.","Connection Lost",JOptionPane.ERROR_MESSAGE);
                            System.out.println("Error getting list");
                        }
                    }
                    else if(e.getSource() == menuItems[4]){
                        JOptionPane.showMessageDialog(frame,"TsikaNet v1.0\nFrank Rayo\nCS 145 WFWX\nAll rights reserved.\nabout.me/bosz.frank07","About TsikaNet",JOptionPane.INFORMATION_MESSAGE,new Frame.icon(icon));
                    }
                    else if(e.getSource() == menuItems[2]){
                        JOptionPane.showMessageDialog(frame,"Username: " + username + "\nLocal Address: " + client.getLocalAddress() + ":" + client.getLocalPort() + "\nServer Address: " + client.getInetAddress().toString() + ":" + client.getPort(),"Connection Properties",JOptionPane.INFORMATION_MESSAGE,new Frame.icon(icon));
                    }
                    else if(e.getSource() == menuItems[3]){
                        int res = JOptionPane.showConfirmDialog(frame,"Do you really want to exit?","Confirm exit",JOptionPane.OK_CANCEL_OPTION);
                        if(res == JOptionPane.OK_OPTION){
                            System.exit(0);
                        }
                    }
                }
            }
        }
        
        class TabPane extends JTabbedPane{
            private ChatRoomPanel cp = new ChatRoomPanel();
            private GameRoomPanel gp = new GameRoomPanel();
            
            public TabPane(){
                this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                this.setTabPlacement(JTabbedPane.BOTTOM);
                this.setForeground(Color.BLUE.brighter());
                this.setBackground(Color.black);
                this.add("ChatRoom", cp);
                this.add("GameRooms", gp);
            }
            
            class ChatRoomPanel extends JPanel{
                private JTextArea txt = new JTextArea(21,20);
                private JScrollPane scroll = new JScrollPane(txt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                private Insets txtInset = new Insets(10,10,10,10);
                private BottomPanel bottomPanel = new BottomPanel();
                private String temp;
                
                public ChatRoomPanel()
                {
                    this.add(scroll, BorderLayout.NORTH);
                    this.add(bottomPanel, BorderLayout.SOUTH);
                    txt.setDisabledTextColor(Color.black);
                    txt.setMargin(txtInset);
                    txt.setWrapStyleWord(true);
                    txt.setLineWrap(true);
                    txt.setEditable(false);
                    txt.setEnabled(false);
                }
                
                @Override
                public void paintComponent(Graphics g)
                {
                    this.add(scroll);
                    this.add(bottomPanel, BorderLayout.SOUTH);
                    txt.setDisabledTextColor(Color.black);
                    txt.setMargin(txtInset);
                    txt.setWrapStyleWord(true);
                    txt.setLineWrap(true);
                    txt.setEditable(false);
                    txt.setEnabled(false);
                    txt.setText(temp);
                }
                
                void addText(String s){
                    temp = txt.getText() + s + "\n\n";
                    txt.setText(temp);
                }
                
                class BottomPanel extends JPanel{
                    private JTextField txtStatus = new JTextField(15);
                    private JButton btnStatus = new JButton("Send");
                    private JLabel lblStatus = new JLabel("Status:");

                    public BottomPanel(){
                        this.add(lblStatus, BorderLayout.WEST);
                        this.add(txtStatus, BorderLayout.CENTER);
                        this.add(btnStatus, BorderLayout.EAST);
                        btnStatus.addActionListener(new StatusListener());
                        txtStatus.addKeyListener(new StatusKeyListener());
                        btnStatus.addKeyListener(new StatusKeyListener());
                    }
                    
                    @Override
                    public void paintComponent(Graphics g){
                        this.add(lblStatus, BorderLayout.WEST);
                        this.add(txtStatus, BorderLayout.CENTER);
                        this.add(btnStatus, BorderLayout.EAST);
                    }
                    
                    class StatusListener implements ActionListener{
                        @Override
                        public void actionPerformed(ActionEvent e){
                            if (e.getSource() == btnStatus && !txtStatus.getText().equals("")){
                                try{
                                    clientOutput.writeObject("STATUS!@#"+ username + "!@#" + txtStatus.getText());
                                    clientOutput.flush();
                                    txtStatus.setText("");
                                }
                                catch(IOException ex){
                                    JOptionPane.showMessageDialog(frame,"Error sending status. Please try again.","Connection Lost",JOptionPane.ERROR_MESSAGE);
                                    System.out.println("Error sending status");
                                }
                            }
                        }
                    }
                    
                    class StatusKeyListener extends KeyAdapter{
                        @Override
                        public void keyPressed(KeyEvent e){
                            if(e.getKeyCode() == KeyEvent.VK_ENTER && !txtStatus.getText().equals("")){
                                try{
                                    clientOutput.writeObject("STATUS!@#"+ username + "!@#" + txtStatus.getText());
                                    clientOutput.flush();
                                    txtStatus.setText("");
                                }
                                catch(IOException ex){
                                    JOptionPane.showMessageDialog(frame,"Error sending status. Please try again.","Connection Lost",JOptionPane.ERROR_MESSAGE);
                                    System.out.println("Error sending status");
                                }
                            }
                        }
                    }
                }
            }
            class GameRoomPanel extends JPanel{
                
            }
        }
        
        class icon implements Icon{
            private int width, height;
            private Image iconset;
            
            public icon(Image i){
                iconset = i;
                width = i.getWidth(rootPane);
                height = i.getHeight(rootPane);
            }
            
            public int getIconWidth() {return width;}
            
            public int getIconHeight() {return height;}
            
            public void paintIcon(Component c, Graphics g, int x, int y){
                g.drawImage(iconset, x, y, rootPane);
            }
        }
    }
    
    public class MessageFrame extends JFrame{
        private JPanel panel = new MessagePanel();
        private Image icon = new ImageIcon("source/icon.png").getImage();
        
        public MessageFrame(){
            super("Send message");
            this.setSize(300, 300);
            this.setLocationRelativeTo(frame);
            this.setResizable(false);
            this.setIconImage(icon);
            this.setContentPane(panel);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);
        }
        
        class MessagePanel extends JPanel{
            private JLabel lblMsg = new JLabel("Enter message:");
            private JLabel lblRep = new JLabel("Choose receipients: (Separate with ';')");
            private JLabel lbl = new JLabel("To view online users, see 'Menu'.");
            private JTextField txtRep = new JTextField(23);
            private JTextArea txt = new JTextArea(7, 20);
            private JButton btnSend = new JButton("Send");
            private JScrollPane scroll = new JScrollPane(txt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            private Insets txtInset = new Insets(5, 5, 5, 5);
            
            public MessagePanel(){
                this.add(lblRep);
                this.setBackground(Color.LIGHT_GRAY);
                this.add(txtRep);
                this.add(lblMsg);
                this.add(scroll);
                this.add(btnSend);
                this.add(lbl);
                txt.setMargin(txtInset);
                txt.setWrapStyleWord(true);
                txt.setLineWrap(true);
                txt.setEditable(true);
                txt.setEnabled(true);
                btnSend.addActionListener(new ButtonListener());
            }
            
            class ButtonListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e){
                    if (e.getSource() == btnSend){
                        if (txtRep.getText().equals("") || txt.getText().equals("")){
                            JOptionPane.showMessageDialog(msgFrame.panel, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            try{
                                clientOutput.writeObject("SEND!@#" + username + "!@#" + txtRep.getText() + "!@#" + txt.getText());
                                clientOutput.flush();
                            }
                            catch(IOException ex){
                                JOptionPane.showMessageDialog(msgFrame.panel, "Error sending message. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        txt.setText("");
                    }
                }
            }
        }
    }
}
