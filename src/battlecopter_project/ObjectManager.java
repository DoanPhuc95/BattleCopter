/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecopter_project;

import gameplay.ObjectLocation;
import gameplay.TAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Doan Phuc
 */
public class ObjectManager implements ActionListener{
    private int plane_action[];
    private RoomManager rm;
    private ObjectLocation ol;
    private Timer timer;
    private Timer timerRandom;
    private Enemies enemies;
    private Bullets bullets;
    private Explodes explo;
    
    public ObjectManager(RoomManager rm)
    {
        plane_action = new int[2];
        ol = new ObjectLocation();
        enemies = new Enemies();
        bullets = new Bullets();
        explo = new Explodes();
        this.rm = rm;
    }
    
    public void start()
    {
        if(timer == null)
        {
            timer = new Timer(15,this);
            timer.start();
        }
        if(timerRandom == null)
        {
            timerRandom = new Timer(2500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    random();
                }
            });
            timerRandom.start();
        }
    }
    public void stop()
    {
        if(timer != null)
            if(timer.isRunning())
            {
                timer.stop();
                timer = null;
            }
        if(timerRandom != null)
            if(timerRandom.isRunning())
            {
                timerRandom.stop();
                timerRandom = null;
            }
    }
    
    public ObjectLocation getLocations()
    {
        return ol;
    }
    
    public void rq_animation(int plane, int action)
    {
        if(ol.isDead(plane)) plane_action[0]= 0;
        else plane_action[plane]=action;
    }
    
    private void translate(int plane)
    {
        if(plane_action[plane]== TAdapter.LEFT)
            if(ol.getX(plane)>5)
                ol.setX(plane, ol.getX(plane)-1);
        if(plane_action[plane]== TAdapter.RIGHT)
            if(ol.getX(plane)<375)
                ol.setX(plane, ol.getX(plane)+1);
        if(plane_action[plane]== TAdapter.UP)
            if(ol.getY(plane)>20)
                ol.setY(plane, ol.getY(plane)-1);
        if(plane_action[plane]== TAdapter.DOWN)
            if(ol.getY(plane)<395)
                ol.setY(plane, ol.getY(plane)+1);
        if(plane_action[plane]==TAdapter.FIRE)
            fire(plane);
    }
    
    private void fire(int plane)
    {
        if(bullets.size()>0)
            if(ol.getX(plane)+22==bullets.get(bullets.size()-1).getX()&&
                ol.getY(plane)-30<bullets.get(bullets.size()-1).getY()) return;
        Bullet bul = new Bullet(plane, ol.getX(plane), ol.getY(plane));
        bullets.add(bul);
        Thread x = new Thread(new Runnable() {
            @Override
            public void run() {
                long begin,timep;
                while(bul.getY()>20)
                {
                    bul.translate();
                    begin = Calendar.getInstance().getTimeInMillis();
                    Explode exp = bul.check(enemies);
                    if(exp != null){
                        explo.add(exp); ol.updatePoint(plane); break;
                    }
                    timep = Calendar.getInstance().getTimeInMillis()-begin;
                    try{ 
                        if(timep<15)
                            Thread.sleep(15-timep);
                    }catch (InterruptedException ex)
                    {
                        Logger.getLogger(ObjectManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                bullets.remove(bul);
            }
        });
        x.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        translate(0);translate(1);
        rm.SEND_both("ANIMATION");
        rm.SEND_both_location();
        rm.SEND_both(bullets);
        rm.SEND_both(enemies);
        rm.SEND_both(explo);
        explo.clear();
        if(ol.isLost()) rm.stopGame();
    }
    
    private void random()
    {
        Thread x = new Thread(new ThreadRandom(enemies,ol,explo,rm.getLevel()));
        x.start();
    }
}