/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bosz.frank_07
 */
public class ServerDriver {
    public static void main(String[] args){
        Server s = new Server(12321, 100);
        s.runServer();
    }
}
