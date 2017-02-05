/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecopter_project;

import java.util.HashMap;

/**
 *
 * @author Doan Phuc
 */
public class RoomManager {
    static public int white = 0;
    static public int black = 1;
    private int player1_color;
    private SocketID player1;
    private HashMap<SocketID, SocketID> enemy;
    private ObjectManager obj_manager;
    private int level ;
    
    public RoomManager(SocketID SOCKID)
    {
        this.player1 = SOCKID;
        player1_color = white;
        enemy = new HashMap<SocketID, SocketID>();
        obj_manager  = new ObjectManager(this);
    }
    public void setColorforPlayer1(int i)
    {
        player1_color = i;
    }
    public int getColorPlayer1()
    {
        return player1_color;
    }
    
    public void setLevel(int lv)
    {
        level = lv;
    }
    public int getLevel()
    {
        return level;
    }
    
    public SocketID getBoss()
    {
        return player1;
    }
    
    private boolean isFullSlot()
    {
        return enemy.size()==2;
    }
    
    public boolean joinGame(SocketID player2)
    {
        if(isFullSlot()) return false;
        enemy.put(player1, player2);
        enemy.put(player2, player1);
        return true;
    }
    public int outRoom(SocketID SOCK) // return so nguoi trong room sau khi da thoat
    {
        if(isFullSlot())
        {
            player1 = enemy.get(SOCK);
            enemy.clear();
            return 1;
        }
        return 0;
    }
    public SocketID getEnemy(SocketID SOCK)
    {
        return enemy.get(SOCK);
    }
    
    public void SEND_both(String s)
    {
        player1.SEND(s);
        if(getEnemy(player1)!=null) getEnemy(player1).SEND(s);
    }
    public void SEND_both(Object o)
    {
        player1.SEND(o);
        if(getEnemy(player1)!=null) getEnemy(player1).SEND(o);
    }
    public void SEND_both_color()
    {
        player1.SEND(player1_color);
        if(getEnemy(player1)!=null) getEnemy(player1).SEND((player1_color+1)%2);
    }
    public void SEND_both_location()
    {
        if(getEnemy(player1) !=null )
        {
            player1.SEND(obj_manager.getLocations());
            getEnemy(player1).SEND(obj_manager.getLocations());
        }
    }
    
    public void startGame()
    {
        obj_manager.start();
    }
    public void stopGame()
    {
        obj_manager.stop();
        obj_manager = new ObjectManager(this);
    }
    
    public void do_action(SocketID SOCK, int action)
    {
        if(SOCK == player1) obj_manager.rq_animation(player1_color, action);
        else obj_manager.rq_animation((player1_color+1)%2, action);
    }
}
