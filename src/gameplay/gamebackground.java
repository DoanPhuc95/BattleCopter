/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameplay;

import Client.Client_server;
import Room.AutoResizeIcon;
import battlecopter_project.Bullets;
import battlecopter_project.Enemies;
import battlecopter_project.Explodes;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Doan Phuc
 */
public class gamebackground extends JPanel{
    private Client_server client;
    private ObjectLocation ol;
    private int width,height;
    private JLabel plane[] = new JLabel[2];
    private JLabel point[] = new JLabel[2];
    private ArrayBullets bul_label;
    private ArrayEnemies e_label;
    public gamebackground(int width,int height,ObjectLocation ol, Client_server client)
    {
        this.width=width;
        this.height=height;
        this.ol = ol;
        this.client = client;
        init();
        bul_label = new ArrayBullets(this);
        e_label = new ArrayEnemies(this);
        client.remote_inGame(this);
        ready();
    }
    @Override
    protected void paintComponent(Graphics grphcs) {
        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("assets/airPlanesBackground.png"));
        grphcs.drawImage(icon.getImage(), 0, 0, width, height, null);
        setOpaque(false);
        super.paintComponent(grphcs); //To change body of generated methods, choose Tools | Templates.
    }
    private void init()
    {
        setSize(width, height);setLayout(null);this.setFocusable(true);
        plane[0] = new JLabel();plane[1] = new JLabel();
        point[0] = new JLabel();point[1] = new JLabel();
        point[0].setBounds(0, 0, 40, 30);point[1].setBounds(388, 0, 40, 30);
        point[0].setForeground(new Color(238, 238, 238));point[0].setFont(new java.awt.Font("Sylfaen", 1, 35));
        point[1].setForeground(new Color(238, 238, 238));point[1].setFont(new java.awt.Font("Sylfaen", 1, 35));
        point[0].setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        point[1].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        setPlanesLocation();
        AutoResizeIcon.setIcon(plane[0], 0);AutoResizeIcon.setIcon(plane[1], 1);
        add(plane[0]);add(plane[1]);add(point[0]);add(point[1]);
    }
    private void ready()
    {
        JLabel lb = new JLabel();
        lb.setBounds(190, 200, 60, 60);
        lb.setBackground(new Color(0, 191, 255));
        lb.setForeground(new Color(238, 238, 238));lb.setFont(new java.awt.Font("Sylfaen", 1, 35));
        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);add(lb);
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                int t =3;
                for(int i = 0 ; i <3; i++)
                {
                    try {
                        lb.setText(""+t);repaint();Thread.sleep(1000);t--;
                        if(t==2) client.getTransferMethod().SEND("LETGO");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(gamebackground.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                remove(lb); repaint();
                addKeyListener(new TAdapter(client));
            }
        });
        x.start();
    }
    
    private void setPlanesLocation()
    {
        for(int i = 0;i < 2; i++)
        {
            if(ol.isDead(i))
                plane[i].setVisible(false);
            else plane[i].setBounds(ol.getX(i),ol.getY(i),50,50);
            point[i].setText(""+ol.getPoint(i));
        }
    }
    
    public void animation(ObjectLocation ol,Bullets but, Enemies e,Explodes explo)
    {
        Thread x1 = shoot(but),x2 = createEnemy(e);
        this.ol=ol;
        setPlanesLocation();    
        repaint();
        try {
            x1.join();x2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(gamebackground.class.getName()).log(Level.SEVERE, null, ex);
        }
        x1.start();x2.start();
        explo.animations(this);
        if(ol.isLost()){
            JOptionPane.showMessageDialog(null, "GAME OVER");
            client.gameover();
        }
    }
    private Thread shoot(Bullets bullets)
    {
        Thread x1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int t = bul_label.size();
                if(bullets.size()>=bul_label.size())
                {
                    for(int i = 0 ; i < t;i++)
                    {
                        BulletLabel ltemp = bul_label.get(i);
                        ltemp.setLocation(bullets.get(i).getX(), bullets.get(i).getY());
                        if(!ltemp.isShowing()) ltemp.showing();    
                    }
                    for(int i=0;i<bullets.size()-t;i++)
                    {
                        BulletLabel bl = new BulletLabel(bullets.get(i+t).getX(), bullets.get(i+t).getY());
                        bl.showing();bul_label.add(bl);add(bl);
                    }
                }
                else
                {
                    for(int i =  0 ; i< bullets.size(); i++)
                    {
                        bul_label.get(i).setLocation(bullets.get(i).getX(), bullets.get(i).getY());
                        if(!bul_label.get(i).isShowing()) bul_label.get(i).showing();
                    }
                    if(bul_label.size()>30&&30>=bullets.size())
                        while(bul_label.size()>30)
                        {
                            remove(bul_label.get(30));
                            bul_label.remove(30);
                        }
                    for(int i = bullets.size();i<bul_label.size();i++)
                        bul_label.get(i).hidden();
                }
                repaint();
            }
        });
        return x1;
    }
    
    private Thread createEnemy(Enemies e)
    {
        Thread x1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int t = e_label.size();
                if(e.size()>e_label.size())
                {
                    for(int i = 0 ; i < t;i++)
                    {
                        EnemyLabel ltemp = e_label.get(i);
                        ltemp.setLocation(e.get(i).getX(), e.get(i).getY());
                        if(e.get(i).isDead()) ltemp.hidden();
                        else ltemp.showing();
                    }
                    for(int i=0;i<e.size()-t;i++)
                    {
                        if(!e.get(i).isDead())
                        {
                            EnemyLabel el = new EnemyLabel(e.get(i+t).getX(), e.get(i+t).getY());
                            el.showing();e_label.add(el);add(el);
                        }
                    }
                }
                else
                {
                    for(int i =  0 ; i< e.size(); i++)
                    {
                        e_label.get(i).setLocation(e.get(i).getX(), e.get(i).getY());
                        if(!e.get(i).isDead()) e_label.get(i).showing();
                        else e_label.get(i).hidden();
                    }
                    if(e_label.size()>30&&30>=e.size())
                        while(e_label.size()>25)
                        {
                            remove(e_label.get(25));
                            e_label.remove(25);
                        }
                    for(int i = e.size();i<e_label.size();i++)
                        e_label.get(i).hidden();
                }
                repaint();
            }
        });
        return x1;
    }
    
    class ArrayBullets extends ArrayList<BulletLabel>
    {
        public ArrayBullets(gamebackground g)
        {
            for(int i =0 ; i< 20;i++)
            {
                BulletLabel label = new BulletLabel(0,0);
                add(label);g.add(label);
            }
        }
    }
    class ArrayEnemies extends ArrayList<EnemyLabel>
    {
        public ArrayEnemies(gamebackground g)
        {
            for(int i =0 ; i < 10;i++)
            {
                EnemyLabel e = new EnemyLabel(0, 0);
                add(e);g.add(e);
            }
        }
    }
}
