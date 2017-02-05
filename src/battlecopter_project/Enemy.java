/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecopter_project;

/**
 *
 * @author Doan Phuc
 */
public class Enemy {
    private int loc[];
    private int speed;
    private boolean dead;
    
    public Enemy(int x,int y,int speed)
    {
        loc = new int[2];
        loc[0]=x;
        loc[1]=y;
        dead = false;
        this.speed =speed;
    }
    
    public void setY(int y)
    {
        loc[1]=y;
    }
    
    public int getX()
    {
        return loc[0];
    }
    
    public int getY()
    {
        return loc[1];
    }
    
    public int speed()
    {
        return speed;
    }
    
    public void translate()
    {
        if(getY()<375)
            loc[1]+=1;
    }
    public void dead()
    {
        dead = true;
    }
    
    public boolean isDead()
    {
        return dead;
    }
}
