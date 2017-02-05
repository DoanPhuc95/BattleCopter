/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Room;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Doan Phuc
 */
public class AutoResizeIcon {
    public static void setIcon(JLabel label, String fileName)
    {
        try {
            BufferedImage image = ImageIO.read(AutoResizeIcon.class.getClassLoader().getResource(fileName));
            int x =label.getSize().width;
            int y =label.getSize().height;
            int ix =image.getWidth();
            int iy =image.getHeight();
            int dx=0;
            int dy=0;
            if(x /y > ix /iy){
                dy=y;
                dx=dy*ix /iy;
            }else{
                dx=x;
                dy=dx*iy/ix;
            }
            ImageIcon icon = new ImageIcon(image.getScaledInstance(dx, dy, BufferedImage.SCALE_SMOOTH));
            label.setIcon(icon);
        } catch (IOException ex) {
            Logger.getLogger(AutoResizeIcon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void setIcon(JLabel label, int i)
    {
        try {
            BufferedImage image = ImageIO.read(AutoResizeIcon.class.getClassLoader().getResource(AutoResizeIcon.Icon[i]));
            int x =label.getSize().width;
            int y =label.getSize().height;
            int ix =image.getWidth();
            int iy =image.getHeight();
            int dx=0;
            int dy=0;
            if(x /y > ix /iy){
                dy=y;
                dx=dy*ix /iy;
            }else{
                dx=x;
                dy=dx*iy/ix;
            }
            ImageIcon icon = new ImageIcon(image.getScaledInstance(dx, dy, BufferedImage.SCALE_SMOOTH));
            label.setIcon(icon);
        } catch (IOException ex) {
            Logger.getLogger(AutoResizeIcon.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(AutoResizeIcon.Icon[i]);
        }
    }
    static public int black = 1;
    static public int white = 0;
    static public String[] Icon={"assets/PLANE8N.png","assets/PLANE2N.png","assets/B2.png","assets/PLANE1NN.png",
        "assets/bang.png"};
}
