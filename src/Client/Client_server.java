/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Lobby.LobbyUI;
import Room.RoomUI;
import battlecopter_project.Bullets;
import battlecopter_project.Enemies;
import battlecopter_project.Explodes;
import com.google.gson.Gson;
import gameplay.Game;
import gameplay.ObjectLocation;
import gameplay.gamebackground;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Doan Phuc
 */
public class Client_server implements Runnable{
    private TransferMethod transferMethod;
    private String choosedRoom = null;
    public boolean isIngame = false;
    public Client_server(Socket SOCK)
    {
        this.SOCK = SOCK;
        transferMethod = new TransferMethod(SOCK);
    }
    public void remote_roomUI(RoomUI roomUI)
    {
        this.roomUI = roomUI;
    }
    public void remote_lobby(LobbyUI Lobby)
    {
        this.Lobby = Lobby;
    }
    public void remote_SignUp_frame(SignUp_frame signUp_frame)
    {
        this.signUp_frame = signUp_frame;
    }
    public void remote_Login_frame(Login_frame login_frame)
    {
        this.login_frame = login_frame;
    }
    public void remote_Game(Game game)
    {
        this.game = game;
    }
    public void remote_inGame(gamebackground ingame)
    {
        this.ingame = ingame;
    }
    public String getchoosedRoom()
    {
        return choosedRoom;
    }
    public void setChoosedRoom(String s)
    {
        choosedRoom = s;
    }
    public TransferMethod getTransferMethod()
    {
        return transferMethod;
    }
    
    public boolean isWaittingServer()
    {
        return waitting;
    }
    
    public void setNotWaitting()
    {
        waitting = false;
    }
    
    public void setWaitting()
    {
        waitting = true;
        while(isWaittingServer())System.out.print("");
    }
    public void setClientName(String s)
    {
        client_name =s;
    }
    public String getName()
    {
        return client_name;
    }
    public void update_room() throws IOException
    {
        int ii = transferMethod.RECEIVE_i();
        listRoom.clear();
        for(int i=0;i<ii;i++)
            listRoom.add(transferMethod.RECEIVE_s());
        setNotWaitting();
    }
    
    public void setEnableJoinButton(boolean active)
    {
        Lobby.setEnableJoinButton(active);
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String REQUIRE = transferMethod.RECEIVE_s();
                if(REQUIRE== null) return;
//                System.out.println(REQUIRE);
                switch(REQUIRE)
                {
                    case "LOGIN_SUCCESS":
                        login_frame.LoginSuccess(true);
                    case "UPDATE_ROOM":
                        update_room();
                        while(Lobby == null) System.out.print("");
                        Lobby.update_list_room();
                        break;
                    case "CREATE_SUCCESS":
                        setNotWaitting();
                        Lobby.openRoomFrame(0,true);
                        break;
                    case "JOIN_SUCCESS":
                        int temp = transferMethod.RECEIVE_i();
                        int lv = transferMethod.RECEIVE_i();
                        setNotWaitting();
                        Lobby.openRoomFrame(temp,lv,false);
                        break;
                    case "SIGN_UP_SUCCESS":
                        signUp_frame.SignUpSuccess(true); 
                        setNotWaitting();
                        break;
                    case "SIGN_UP_FALSE":
                        signUp_frame.SignUpSuccess(false);
                    case "LOGIN_FALSE":
                        login_frame.LoginSuccess(false);
                    case "CREATE_FALSE":
                    case "JOIN_FALSE": 
                        setNotWaitting();
                        break;
                    case "JOIN_ROOM":
                        roomUI.setEnemyInfo(transferMethod.RECEIVE_s());
                        break;
                    case "OUT_ROOM":
                        OUT_ROOM();
                        break;
                    case "CHANGE_COLOR":
                        setNotWaitting();
                        roomUI.setIcon(transferMethod.RECEIVE_i());
                        break;
                    case "LEVEL":
                        roomUI.updateLevel(transferMethod.RECEIVE_i());
                        break;
                    case "READY":
                        roomUI.setStartButtonEnable(transferMethod.RECEIVE_i());
                        break;
                    case "START":
                        String start_json = transferMethod.RECEIVE_s();
                        setNotWaitting();
                        START_GAME(start_json);
                        break;
                    case "ANIMATION":
                        String plane=transferMethod.RECEIVE_s(),bullets=transferMethod.RECEIVE_s();
                        String enemies=transferMethod.RECEIVE_s();String explodes = transferMethod.RECEIVE_s();
                        DO_ANIMATION(plane,bullets,enemies,explodes);
                    default: break;
                }
            } catch (IOException ex) {}
        }
    }
    private void OUT_ROOM()
    {
        if(isIngame== true)
        {
            isIngame = false;
            roomUI.setVisible(true);
            game.dispose();
            game = null;
        }
        roomUI.setEnemyInfo(null);
        roomUI.checkBoss(true);
    }
    
    private void START_GAME(String s)
    {
        Gson gson = new Gson();
        ObjectLocation ol = gson.fromJson(s, ObjectLocation.class);
        roomUI.PlayGame(ol);
        isIngame = true;
    }
    public void gameover()
    {
        game.dispose();
        roomUI.setVisible(true);
    }
    public void logout()
    {
        if(Lobby != null) Lobby.dispose();
        Lobby = null;
    }
    
    private void DO_ANIMATION(String plane,String bullets,String enemies,String explodes)
    {
        Gson gson = new Gson();
        ObjectLocation ol = gson.fromJson(plane,ObjectLocation.class);
        Bullets bs = gson.fromJson(bullets,Bullets.class);
        Enemies e = gson.fromJson(enemies,Enemies.class);
        Explodes explo = gson.fromJson(explodes, Explodes.class);
        ingame.animation(ol,bs,e,explo);
    }
    
    public ArrayList listRoom = new ArrayList();
    private LobbyUI Lobby;
    private SignUp_frame signUp_frame;
    private Login_frame login_frame;
    private RoomUI roomUI;
    private boolean waitting = true;
    private Socket SOCK;
    private String client_name;
    private Game game;
    private gamebackground ingame;
}
