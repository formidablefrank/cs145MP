import java.io.Serializable;
import javax.swing.*;

public class Piece extends JButton implements Serializable
{
    private int position;
    private String name;
    private int owner;
    
    public Piece() {}
    
    public Piece(int position,String name, int owner)
    {
        super(name);
        this.position = position;
        this.name = name;
        this.owner = owner;
    }
    
    public Piece(int position,String name,int owner,Icon icon,Icon iconr)
    {
        super(icon);
        this.name = name;
        this.owner = owner;
        this.position = position;
        this.setRolloverIcon(iconr);
    }

    public int getPosition() { return position; }
    public String getPieceName() { return this.name; }
    public int getOwner() { return this.owner; }
    public void setPosition(int position) {this.position = position;}
    public void setPieceName(String name) {this.name = name;}
}
