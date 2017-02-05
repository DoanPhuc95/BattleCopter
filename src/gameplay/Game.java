/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay;

import Client.Client_server;
import javax.swing.JFrame;

/**
 *
 * @author Doan Phuc
 */
public class Game extends JFrame{
    private ObjectLocation ol;
    private Client_server client;
    private int color;
    private String name;
    public Game(Client_server client,int color, String name, ObjectLocation ol)
    {
        this.client=client;
        this.color= color;
        this.name= name;
        this.ol = ol;
        init();
        client.remote_Game(this);
    }
    
    private void init()
    {
        setSize(430 , 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        add(new gamebackground(this.getSize().width,this.getSize().height,ol, client));
    }
}
