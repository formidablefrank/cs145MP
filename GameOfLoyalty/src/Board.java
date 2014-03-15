import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class Board extends JPanel implements Serializable
{
    public static Piece[][] board = new Piece[8][8];
    public static int moves;
    static Piece temp = new Piece(-2,"",-1);
    
    public Board()
    {
        initialize();
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                add(board[x][y]);
                board[x][y].setBackground(Color.gray);
                board[x][y].addActionListener(bListener);
            }
        }
    }
    
    public void initialize()
    {
        for(int x=6;x<7;x++)
        {
            for(int y=0;y<6;y++)
                board[x][y] = new Piece(10*x+y,"L",0,new ImageIcon("source/loyal.png"),new ImageIcon("source/loyalr.png"));
            for(int y=6;y<8;y++)
                board[x][y] = new Piece(10*x+y,"S",0,new ImageIcon("source/strong.png"),new ImageIcon("source/strongr.png"));
        }

        for(int x=7;x<8;x++)
        {
            for(int y=0;y<4;y++)
                board[x][y] = new Piece(10*x+y,"P",0,new ImageIcon("source/passionate.png"),new ImageIcon("source/passionater.png"));
            for(int y=4;y<7;y++)
                board[x][y] = new Piece(10*x+y,"W",0,new ImageIcon("source/wise.png"),new ImageIcon("source/wiser.png"));
            for(int y=7;y<8;y++)
                board[x][y] = new Piece(10*x+y,"F",0,new ImageIcon("source/flag.png"),new ImageIcon("source/flagr.png"));
        }

        for(int x=2;x<6;x++)
            for(int y=0;y<8;y++)
                board[x][y] = new Piece(10*x+y,"",-1);

        for(int x=1;x<2;x++)
        {
            for(int y=0;y<6;y++)
                board[x][y] = new Piece(10*x+y,"L",1,new ImageIcon("source/opp.png"),new ImageIcon("source/oppr.png"));
            for(int y=6;y<8;y++)
                board[x][y] = new Piece(10*x+y,"S",1,new ImageIcon("source/opp.png"),new ImageIcon("source/oppr.png"));
        }
        for(int x=0;x<1;x++)
        {
            for(int y=0;y<4;y++)
                board[x][y] = new Piece(10*x+y,"P",1,new ImageIcon("source/opp.png"),new ImageIcon("source/oppr.png"));
            for(int y=4;y<7;y++)
                board[x][y] = new Piece(10*x+y,"W",1,new ImageIcon("source/opp.png"),new ImageIcon("source/oppr.png"));
            for(int y=7;y<8;y++)
                board[x][y] = new Piece(10*x+y,"F",1,new ImageIcon("source/opp.png"),new ImageIcon("source/oppr.png"));
        }

        Random rand = new Random();
        for(int x=0;x<128;x++)
        {
            int i = rand.nextInt(2);
            int j = rand.nextInt(2);
            int k = rand.nextInt(8);
            int l = rand.nextInt(8);
            int m = rand.nextInt(2)+6;
            int n = rand.nextInt(2)+6;
            swap(i,k,j,l);
            swap(m,k,n,l);
        }

        BoardGUI.status.setText("Let's play!");
    }

    public static void swap(int i0,int j0,int i1,int j1)
    {
        
        temp = board[i0][j0];
        board[i0][j0] = board[i1][j1];
        board[i1][j1] = temp;
    }

    public int focusPiece(Piece i)
    {
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                if(board[x][y].equals(i))
                    return 10*x+y;
            }
        }
        return 0;
    }
    
    public void clear()
    {
        this.removeAll();
        this.validate();
        this.repaint();
    }
    
    public void revealFlag()
    {
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                if(board[x][y].getOwner()==1 && board[x][y].getPieceName().equals("F"))
                {
                    board[x][y].setBackground(Color.white);
                    board[x][y].setIcon(null);
                    board[x][y].setText(board[x][y].getPieceName());
                }
            }
        }
    }

    public void revealAll()
    {
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                if(board[x][y].getOwner()==1)
                {
                    board[x][y].setBackground(Color.white);
                    board[x][y].setIcon(null);
                    board[x][y].setText(board[x][y].getPieceName());
                    if(board[x][y].getPieceName().equals("F"))
                        BoardGUI.status.setText("Computer win the game in " + moves + " moves!");
                }
            }
        }
    }

    public void removeAllListeners()
    {
        for(int x=0;x<8;x++)
            for(int y=0;y<8;y++)
                for(ActionListener al:board[x][y].getActionListeners())
                    board[x][y].removeActionListener(al);
    }

    public void movePieceAI()
    {
        Random rand = new Random();
        String[] directions = {"up","down","left","right"};
        String direction = directions[rand.nextInt(4)];
        boolean isOk = true;
        int x,y;
        do{
            do{
                x = rand.nextInt(8);
                y = rand.nextInt(8);
                if(board[x][y].getOwner()==1)
                {
                    isOk = true;
                }
                else
                {
                    isOk = false;
                }
            }while(isOk == false);

            //ai algorithm for the game
            if(BoardGUI.aiDiff <= 4) //easy
            {
                try
                {
                    if(board[x+1][y].getOwner() == -1) direction = "down";
                    else if(board[x+1][y].getOwner() == 0)
                        if(rand.nextInt(10) <=8 && Arbiter.checkIfWin(board[x][y].getPieceName(),board[x+1][y].getPieceName())) { direction = "down"; }
                    else if(board[x-1][y].getOwner() == 0)
                        if(rand.nextInt(10) <=8 && Arbiter.checkIfWin(board[x][y].getPieceName(),board[x-1][y].getPieceName())) { direction = "up"; }
                    else if(board[x][y+1].getOwner() == 0)
                        if(rand.nextInt(10) <=8 && Arbiter.checkIfWin(board[x][y].getPieceName(),board[x][y+1].getPieceName())) { direction = "right"; }
                    else if(board[x][y-1].getOwner() == 0)
                        if(rand.nextInt(10) <=8 && Arbiter.checkIfWin(board[x][y].getPieceName(),board[x][y-1].getPieceName())) { direction = "left"; }
                }
                catch(Exception e){}
            }
            else if(BoardGUI.aiDiff >= 7) //hard
            {
                try
                {
                    if(board[x+1][y].getOwner() == -1) direction = "down";
                    else if(board[x+1][y].getOwner() == 0)
                        if(Arbiter.checkIfWin(board[x][y].getPieceName(),board[x+1][y].getPieceName())) direction = "down"; 
                    else if(board[x-1][y].getOwner() == 0)
                        if(Arbiter.checkIfWin(board[x][y].getPieceName(),board[x-1][y].getPieceName())) direction = "up"; 
                    else if(board[x][y+1].getOwner() == 0)
                        if(Arbiter.checkIfWin(board[x][y].getPieceName(),board[x][y+1].getPieceName())) direction = "right"; 
                    else if(board[x][y-1].getOwner() == 0)
                        if(Arbiter.checkIfWin(board[x][y].getPieceName(),board[x][y-1].getPieceName())) direction = "left"; 
                    else {}
                }
                catch(Exception e){}
            }
            else //normal
            {
                direction = directions[rand.nextInt(4)];
            }
            try
            {
                if(direction.equals("up"))
                {
                    switch(board[x-1][y].getOwner())
                    {
                        case 0:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            Arbiter.inspect(board,x,y,x-1,y);
                            isOk = true; break;
                        }
                        case 1:
                        {
                            isOk = false; break;
                        }
                        case -1:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            swap(x,y,x-1,y);
                            isOk = true; break;
                        }
                    }
                }
                else if(direction.equals("down"))
                {
                    switch(board[x+1][y].getOwner())
                    {
                        case 0:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            Arbiter.inspect(board,x,y,x+1,y);
                            isOk = true; break;
                        }
                        case 1:
                        {
                            isOk = false; break;
                        }
                        case -1:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            swap(x,y,x+1,y);
                            isOk = true; break;
                        }
                    }
                }
                else if(direction.equals("left"))
                {
                    switch(board[x][y-1].getOwner())
                    {
                        case 0:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            Arbiter.inspect(board,x,y,x,y-1);
                            isOk = true; break;
                        }
                        case 1:
                        {
                            isOk = false; break;
                        }
                        case -1:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            swap(x,y,x,y-1);
                            isOk = true; break;
                        }
                    }
                }
                else if(direction.equals("right"))
                {
                    switch(board[x][y+1].getOwner())
                    {
                        case 0:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            Arbiter.inspect(board,x,y,x,y+1);
                            isOk = true; break;
                        }
                        case 1:
                        {
                            isOk = false; break;
                        }
                        case -1:
                        {
                            BoardGUI.status.setText("Opponent moves piece at: " + x + y + ", " + direction);
                            swap(x,y,x,y+1);
                            isOk = true; break;
                        }
                    }
                }
                else
                {
                    isOk = false;
                }
            }
            catch(Exception e)
            {
                isOk = false;
            }
        }while(isOk == false);
        moves++;
        removeAll();
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        this.setLayout(new GridLayout(8,8,10,10));
        this.addKeyListener(kListener);
        this.requestFocusInWindow();
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());
        removeAllListeners();
        Arbiter.checkFlag(board);
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                add(board[x][y]);
                board[x][y].setBackground(Color.gray);
                board[x][y].addActionListener(bListener);
            }
        }
        if(Arbiter.isWin == true)
        {
            revealAll();
            removeAllListeners();
        }
    }
    
    KeyAdapter kListener = new KeyAdapter()
    {
        public void keyPressed(KeyEvent e)
        {
            if(e.getKeyChar()=='r')
            {
                revealFlag();
                BoardGUI.status.setText("A cheat has been activated. :)");
            }
            else if(e.getKeyChar()=='a')
            {
                revealAll();
                BoardGUI.status.setText("A cheat has been activated. :)");
            }
            else if(e.getKeyChar()=='s')
            {
                for(int x=0;x<8;x++)
                    for(int y=0;y<8;y++)
                        if(board[x][y].getOwner()==1 && !board[x][y].getPieceName().equals("F"))
                            board[x][y].setPieceName("E");
                BoardGUI.status.setText("A cheat has been activated. :)");
            }
        }
    };

    ActionListener bListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            Piece P = (Piece) e.getSource();
            final int pos = focusPiece(P), r = pos/10, c = pos%10;
            if(P.getOwner()==1) BoardGUI.status.setText("You can't move this piece!");
            if(P.getOwner()==0)
            {
                if(r==7 && c!=7 && board[r][c+1].getOwner()==0 && board[r-1][c].getOwner()==0 && board[r][c-1].getOwner()==0 )
                    BoardGUI.status.setText("You can't move this piece!");
                else if(r==7 && c==7 && board[r-1][c].getOwner()==0 && board[r][c-1].getOwner()==0 )
                    BoardGUI.status.setText("You can't move this piece!");
                else
                {
                    BoardGUI.status.setText("Click the position you want to move it to.");
                    removeAllListeners();
                    try
                    {
                        if(board[r-1][c].getOwner() == -1)
                        {
                            board[r-1][c].setBackground(Color.white);
                            board[r-1][c].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        swap(r,c,r-1,c);
                                        board[r][c] = new Piece(10*r+c,"",-1);
                                        repaint();
                                        movePieceAI();
                                    }
                                }
                            );
                        }
                        if(board[r-1][c].getOwner() == 1)
                        {
                            board[r-1][c].setBackground(Color.white);
                            board[r-1][c].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Arbiter.inspect(board,r,c,r-1,c);
                                        removeAll();
                                        repaint();
                                    }
                                }
                            );
                        }
                    }catch(Exception ex){}
                    try{
                        if(board[r+1][c].getOwner() == -1)
                        {
                            board[r+1][c].setBackground(Color.white);
                            board[r+1][c].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        swap(r,c,r+1,c);
                                        board[r][c] = new Piece(10*r+c,"",-1);
                                        repaint();
                                        movePieceAI();
                                    }
                                }
                            );
                        }
                        if(board[r+1][c].getOwner() == 1)
                        {
                            board[r+1][c].setBackground(Color.white);
                            board[r+1][c].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Arbiter.inspect(board,r,c,r+1,c);
                                        removeAll();
                                        repaint();
                                    }
                                }
                            );
                        }
                    }catch(Exception ex){}
                    try{
                        if(board[r][c+1].getOwner() == -1)
                        {
                            board[r][c+1].setBackground(Color.white);
                            board[r][c+1].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        swap(r,c,r,c+1);
                                        board[r][c] = new Piece(10*r+c,"",-1);
                                        repaint();
                                        movePieceAI();
                                    }
                                }
                            );
                        }
                        if(board[r][c+1].getOwner() == 1)
                        {
                            board[r][c+1].setBackground(Color.white);
                            board[r][c+1].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Arbiter.inspect(board,r,c,r,c+1);
                                        removeAll();
                                        repaint();
                                    }
                                }
                            );
                        }
                    }
                        catch(Exception ex){}
                    try{
                        if(board[r][c-1].getOwner() == -1)
                        {
                            board[r][c-1].setBackground(Color.white);
                            board[r][c-1].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        swap(r,c,r,c-1);
                                        board[r][c] = new Piece(10*r+c,"",-1);
                                        repaint();
                                        movePieceAI();
                                    }
                                }
                            );
                        }
                        if(board[r][c-1].getOwner() == 1)
                        {
                            board[r][c-1].setBackground(Color.white);
                            board[r][c-1].addActionListener(
                                new ActionListener()
                                {
                                    public void actionPerformed(ActionEvent e)
                                    {
                                        Arbiter.inspect(board,r,c,r,c-1);
                                        removeAll();
                                        repaint();
                                    }
                                }
                            );
                        }
                    }
                    catch(Exception ex){}
                }
            }
        }
    };
}