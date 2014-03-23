import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    private ServerSocket serverSocket;
    private Socket connection;
    private final LinkedList<Connection> connections;
    private final int port, limit;
    private String message;
    
    protected class Connection implements Runnable{
        private final Socket connection;        
        private ObjectInputStream serverInput;
        private ObjectOutputStream serverOutput;
        private String username;
        
        public Connection(Socket s){
            this.connection = s;
        }
        
        public boolean ifUserExists(String user){
            for(Connection con: connections){
                if (con.username!= null && con.username.equals(user)){
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void run(){
            try{
                this.serverOutput = new ObjectOutputStream(connection.getOutputStream());
                serverOutput.flush();
                this.serverInput = new ObjectInputStream(connection.getInputStream());
                System.out.println("Got I/O streams");
                Scanner input = new Scanner(System.in);
                try{
                    this.username = (String) serverInput.readObject();
                    System.out.println("CLIENT: " + username + " from " + connection.toString());
                    processMessage("USER!@#" + username);
                    do{
                        //message = input.next();
                        //serverOutput.writeObject(message);
                        //serverOutput.flush();
                        try{
                            message = (String) serverInput.readObject();
                            System.out.println("CLIENT " + username + ": " + message);
                            processMessage(message);
                        }
                        catch(ClassNotFoundException e){
                            e.printStackTrace();
                        }
                        catch(IOException e)
                        {
                            System.out.println("Client " + username + " closed");
                            connections.remove(this);
                            message = "TERMINATE";
                        }
                    } while(!message.equals("TERMINATE"));
                    processMessage("TERMINATE!@#" + this.username);
                }
                catch(ClassNotFoundException e){
                    System.out.println("Class def not found");
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                finally{
                    serverInput.close();
                    serverOutput.close();
                    connection.close();
                    connections.remove(this);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        
        private void processMessage(String message) throws IOException{
            String[] command = message.split("!@#");
            switch(command[0]){
                case "USER":{
                    //user is online
                    for(Connection conn: connections){
                        conn.serverOutput.writeObject("USER!@#" + command[1]);
                        conn.serverOutput.flush();
                    }
                    break;
                }
                case "TERMINATE":{
                    for(Connection conn: connections){
                        conn.serverOutput.writeObject("TERMINATE!@#" + command[1]);
                        conn.serverOutput.flush();
                    }
                    break;
                }
                case "STATUS":{
                    for(Connection conn: connections){
                        conn.serverOutput.writeObject("STATUS!@#" + command[1] + "!@#" +command[2]);
                        conn.serverOutput.flush();
                    }
                    break;
                }
                case "LIST":{
                    String temp = "LIST!@#";
                    for(Connection conn: connections){
                        if (!conn.username.equals(command[1])){
                            temp = temp.concat(conn.username+"\n");
                        }
                    }
                    for(Connection conn: connections){
                        if(conn.username.equals(command[1])){
                            conn.serverOutput.writeObject(temp);
                            conn.serverOutput.flush();
                            break;
                        }
                    }
                    break;
                }
                case "SEND":{
                    String dummyRepList = command[2];
                    String trueRepList = "";
                    //determine online receipients
                    for (Connection conn: connections){
                        if (dummyRepList.contains(conn.username)){
                            dummyRepList = dummyRepList.replace(conn.username, "");
                            trueRepList += conn.username + ";";
                        }
                    }
                    //offline receipients
                    System.out.println("DummyList: " + dummyRepList);
                    //online receipients
                    System.out.println("TrueList: " + trueRepList);
                    //send failed receipients to client
                    for (Connection conn: connections){
                        if (conn.username.equals(command[1])){
                            serverOutput.writeObject("FAILED!@#" + dummyRepList);
                            serverOutput.flush();
                            break;
                        }
                    }
                    //send message to online receipients
                    for(Connection conn: connections){
                        if(trueRepList.contains(conn.username)){
                            conn.serverOutput.writeObject("SEND!@#" + command[1] + "!@#" + trueRepList + "!@#" + command[3]);
                            conn.serverOutput.flush();
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public Server(int port, int limit){
        this.port = port;
        this.limit = limit;
        connections = new LinkedList();
    }
    
    public void runServer(){
        try{
            serverSocket = new ServerSocket(this.port, this.limit);
            System.out.println("Waiting for connection:");
            try{
                do{
                    connection = serverSocket.accept();
                    Connection cxn = new Connection(connection);
                    connections.add(cxn);
                    Thread thread = new Thread(cxn);
                    thread.start();
                }while(true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Server closed");
            }
            finally{            
                serverSocket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    protected class GameRoom{
        String name;
        int slot;
        int status;
        
        public GameRoom(String n, int s){
            this.name = n;
            this.slot = s;
        }
        
        @Override
        public String toString(){
            return this.name + "," + this.slot;
        }
    }
}
