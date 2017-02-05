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
public class Bullet {
    private int[] loc;
    private int plane;
    public Bullet(int plane,int x,int y)
    {
        this.plane = plane;
        loc = new int[2];
        loc[0]=x+22;loc[1]=y;
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
    
    public void translate()
    {
        loc[1]-=3;
    }
    
    public Explode check(Enemies e)
    {
        Explode ex = null;
        int t=0;
        while(e.size()>t)
        {
            int dx = e.get(t).getX(),dy=e.get(t).getY();
            if(((getX()>dx&&getX()<dx+70)||(getX()+20>dx&&getX()<dx+50))&&(getY()<=dy+70))
            {
                e.get(t).dead();
                e.remove(e.get(t));
                ex = new Explode(dx,dy);
                break;
            }
            else t++;
        }
        return ex;
    }
}
