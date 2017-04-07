/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;
 
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
 
public class ColorChooser extends JPanel
                              implements ChangeListener, Serializable {
 
    protected JColorChooser tcc;
    protected JLabel banner;
 
    public ColorChooser() {
        super(new BorderLayout());
 
        //Set up the banner at the top of the window
        banner = new JLabel("Welcome to the Tutorial Zone!",
                            JLabel.CENTER);
        banner.setForeground(Color.yellow);
        banner.setBackground(Color.blue);
        banner.setOpaque(true);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setPreferredSize(new Dimension(100, 65));
// 
        //Set up color chooser for setting text color
        tcc = new JColorChooser(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Text Color"));
 
        add(tcc, BorderLayout.CENTER);
        
    }
 
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        banner.setForeground(newColor);
    }
    
    public Color getColor(){
    	return tcc.getColor();
    }
 


 

}