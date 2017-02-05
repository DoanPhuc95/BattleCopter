/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Doan Phuc
 */
public class ConnectToSerVerFrame extends JFrame{
    public ConnectToSerVerFrame()
    {
        init();
    }
    
    private void init()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null); setResizable(false);
        setSize(300, 500); setLocationRelativeTo(null);
        
        JLabel lab1 = new JLabel("Battle Copters"); lab1.setBounds(0, 0, 300, 80); 
        lab1.setBackground(new Color(0, 191, 255));lab1.setOpaque(true);
        lab1.setForeground(new Color(238, 238, 238));lab1.setFont(new java.awt.Font("Sylfaen", 1, 35));
        lab1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);add(lab1);
        
        JLabel lab3 = new JLabel("Server"); lab3.setBounds(40, 140, 70, 12);
        lab3.setFont(new java.awt.Font("Aria", 0, 12));add(lab3);
        
        Server_txt.setBounds(40, 160, 220, 40);Server_txt.setFont(new java.awt.Font("Verdana", 0, 20));add(Server_txt);
        
        JLabel lab4 = new JLabel("Port"); lab4.setBounds(40, 220, 70, 12);
        lab4.setFont(new java.awt.Font("Aria", 0, 12));add(lab4);
        
        Port_txt.setBounds(40, 240, 220, 40);Port_txt.setFont(new java.awt.Font("Verdana", 0, 20));add(Port_txt);
        Connectbt.setUI(new StyledButtonUI());
        Connectbt.setBounds(40, 315, 220, 50);Connectbt.setBackground(new java.awt.Color(0x5858FA)); add(Connectbt);
        Connectbt.setForeground(new Color(238, 238, 238));
        Connectbt.setFont(new java.awt.Font("Verdana", 1, 15));
        
        Connectbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ConnectServer();}});
        Server_txt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ConnectServer();}});
        Port_txt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ConnectServer();}});
    }
    
    private void ConnectServer()
    {
        try {
            Socket SOCK = new Socket(InetAddress.getByName(Server_txt.getText()), Integer.parseInt(Port_txt.getText()));
            System.out.println("ket noi thanh cong");
            Client_server cli_ser = new Client_server(SOCK);
            Thread x = new Thread(cli_ser);
            x.start();
            new Login_frame(cli_ser).setVisible(true);
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(ConnectToSerVerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
    
    private JTextField Port_txt = new JTextField("7777"); 
    private JTextField Server_txt = new JTextField("127.0.0.1");
    private JButton Connectbt = new JButton("Connect");
    
    public static void main(String[] args) {
        new ConnectToSerVerFrame().setVisible(true);
    }
}
