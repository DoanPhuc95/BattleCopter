package Lobby;

import Room.AutoResizeIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ProfilePanel extends JPanel{
	private static final int width = 200;
	private static final int height = 500;
	
	public ProfilePanel(){
            super();
            this.setLayout(null);
            this.setPreferredSize(new Dimension(width, height));


            TitledBorder border = new TitledBorder(new LineBorder(new Color(0, 51, 51), 3, true), "INFORMATION", TitledBorder.CENTER, TitledBorder.TOP, null, null);
            border.setTitleFont(new Font("Lucida Bright", Font.BOLD | Font.ITALIC, 20));
            border.setTitleColor(new Color(204,150,0));
            this.setBorder(border);
            
            JLabel label1 = new JLabel();
            label1.setBounds(10, 60, 180, 220); AutoResizeIcon.setIcon(label1, "assets/cup-512.jpg");
            add(label1);
            
            JLabel label4 = new JLabel();
            label4.setBounds(10, 290+25, 180, 500-40-290);AutoResizeIcon.setIcon(label4, "assets/rankings-512.png");
            add(label4);
            
            /////test1
            JLabel label2 = new JLabel();label2.setForeground(new Color(204, 150, 0));
            label2.setBounds(10, 30, 180, 40); add(label2);
            label2.setHorizontalAlignment((int) CENTER_ALIGNMENT);
            label2.setFont(new Font("Aria", 1, 40));
            
            JLabel label3 = new JLabel();
            label3.setBounds(10, 265, 180, 40); add(label3);
            label3.setHorizontalAlignment((int) CENTER_ALIGNMENT); 
            label3.setFont(new Font("Aria", 1, 40));
            /////
	}
}
