/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecopter_project;

import gameplay.ObjectLocation;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Doan Phuc
 */
public class ThreadRandom implements Runnable{
    private Enemies enemies;
    private int quantity;
    private ObjectLocation ol;
    private Explodes explo;
    private int level;
    public ThreadRandom(Enemies enemies, ObjectLocation ol, Explodes explo,int level)
    {
        this.enemies = enemies;
        this.ol = ol;
        this.explo = explo;
        this.level=level;
    }
    
    @Override
    public void run() {
        randomQuantity();
        createEnemy();
    }
    
    private int randomInt(int min,int max)
    {
        Random ran = new Random();
        return ran.nextInt(max-min+1)+min;
    }
    
    private void randomQuantity()
    {
        quantity = randomInt(1,level+1);
    }
    private void createEnemy()
    {
        for(int i = 0 ;i < quantity; i++)
        {
            int x = randomInt(5,355);
            int speed = randomInt(10, 20);
            autoTranslate(x, 0, speed);
        }
    }
    
    private void autoTranslate(int x, int y, int speed)
    {
        Enemy e = new Enemy(x, 0, speed);
        enemies.add(e);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while(e.getY()<375)
                {
                    try {
                        e.translate();
                        check(e);
                        Thread.sleep(speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadRandom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(!e.isDead())
                    ol.lost();
                enemies.remove(e);
            }
        });
        th.start();
    }
    
    private void check(Enemy e)
    {
        int x=e.getX(),y=e.getY();
        boolean b = e.isDead();
        for(int plane = 0 ; plane < 2 ; plane++)
            if(!ol.isDead(plane)&&!b)
                if((y<=ol.getY(plane)&&ol.getY(plane)<=y+70)||(y<=ol.getY(plane)+50&&ol.getY(plane)<=y+20))
                    if((x<=ol.getX(plane)&&ol.getX(plane)<=x+70)||(x<=ol.getX(plane)+50&&ol.getX(plane)<=x+20)) 
                    {
                        ol.die(plane);e.dead();
                        explo.add(new Explode(ol.getX(plane), ol.getY(plane)));
                        explo.add(new Explode(e.getX(), e.getY()));
                    }
        if(ol.isDead(0)&&ol.isDead(1)&&!e.isDead()) ol.lost();
    }
}
