public abstract class Arbiter implements java.io.Serializable
{
    public static boolean isWin = false;
    
    public static void inspect(Piece[][] board,int i0,int j0,int i1, int j1)
    {
        if(board[i0][j0].getPieceName().equals(board[i1][j1].getPieceName()) && !board[i0][j0].getPieceName().equals("F"))
        {
            BoardGUI.status.setText("Tie! Both pieces eliminated.");
            board[i0][j0] = new Piece(10*i0+j0,"",-1);
            board[i1][j1] = new Piece(10*i1+j1,"",-1);
        }
        else if(checkIfWin(board[i0][j0].getPieceName(),board[i1][j1].getPieceName()))
        {
            if(board[i1][j1].getPieceName().equals("F"))
            {
                board[i1][j1].setPosition(-1);
                isWin = true;
                board[i1][j1] = new Piece(10*i1+j1,"",-1);
                Board.swap(i0, j0, i1, j1);
                BoardGUI.status.setText("You win the game in " + (Board.moves+1) + " moves!");
                if(BoardGUI.aiDiff<4)    BoardGUI.EgamesWon++;
                else if(BoardGUI.aiDiff>7)    BoardGUI.DgamesWon++;
                else BoardGUI.MgamesWon++;
            }
            else if(board[i0][j0].getOwner()==1)
            {
                BoardGUI.status.setText("Computer wins.");
                board[i1][j1] = new Piece(10*i1+j1,"",-1);
                Board.swap(i0, j0, i1, j1);
            }
            else
            {
                BoardGUI.status.setText("You win!");
                board[i1][j1] = new Piece(10*i1+j1,"",-1);
                Board.swap(i0, j0, i1, j1);
            }
        }
        else
        {
            if(board[i0][j0].getPieceName().equals("F"))
            {
                board[i0][j0] = new Piece(10*i1+j1,"",1);
                board[i0][j0].setPosition(-1);
                isWin = true;
                BoardGUI.status.setText("You win the game in " + (Board.moves+1) + " moves!");
                if(BoardGUI.aiDiff<4)    BoardGUI.EgamesWon++;
                else if(BoardGUI.aiDiff>7)    BoardGUI.DgamesWon++;
                else BoardGUI.MgamesWon++;
            }
            else if(board[i1][j1].getOwner()==0)
            {
                BoardGUI.status.setText("You win!");
                board[i0][j0] = new Piece(10*i0+j0,"",-1);
            }
            else
            {
                BoardGUI.status.setText("Computer wins.");
                board[i0][j0] = new Piece(10*i0+j0,"",-1);
            }
        }
    }
    
    public static boolean checkIfWin(String a,String b)
    {
        char x = a.charAt(0);
        char y = b.charAt(0);
        switch(x)
        {
            case 'S':
            {
                if(y=='L') return false;
                else    return true;
            }
            case 'W':
            {
                if(y=='S') return false;
                else    return true;
            }
            case 'P':
            {
                return true;
            }
            case 'L':
            {
                if(y=='P' || y=='W')    return false;
                else    return true;
            }
            case 'F':
            {
                if(y=='F') return true;
                else return false;
            }
        }
        return false;
    }
    
    public static void checkFlag(Piece board[][])
    {
        int count=0,r=0,c=0;
        for(int x=0;x<8;x++)
        {
            for(int y=0;y<8;y++)
            {
                if(board[x][y].getOwner()==1)
                {
                    count++;
                    r=x;
                    c=y;
                }
            }
        }
        if(count==1 && board[r][c].getPieceName().equals("F"))
        {
            isWin = true;
            BoardGUI.status.setText("Computer win the game in " + Board.moves + " moves!");
        }
    }
}