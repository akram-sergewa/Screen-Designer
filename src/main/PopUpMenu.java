/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuActionListener implements ActionListener {
	
	private MainWindow mw;

	MenuActionListener(MainWindow mw){
		this.mw = mw;
	}

	public void actionPerformed(ActionEvent e) {
		
		  //	System.out.println("Selected: " + e.getActionCommand());
		
		
		if (e.getActionCommand().equals("Add Text")){
			mw.showTextEditor();
		}else if (e.getActionCommand().equals("Add Image")){
			mw.addImage();
		}else if (e.getActionCommand().equals("Add Spline")){
			mw.addSpline();
		}else if (e.getActionCommand().equals("Background color")){
			mw.showBackgroundColor();
		}else if (e.getActionCommand().equals("Show/Hide Grid")){
			mw.showHideGrid();
		}
	
	

	}


}


public class PopUpMenu extends JPopupMenu {
	JMenuItem anItem;
	private MainWindow mw;
	
    public PopUpMenu(MainWindow mw){
    	this.mw = mw;
    	
    	JMenuItem addText = new JMenuItem("Add Text");
    	addText.addActionListener(new MenuActionListener(mw));
        add(addText);
        
        JMenuItem addImage = new JMenuItem("Add Image");
        addImage.addActionListener(new MenuActionListener(mw));
        add(addImage);
        
        JMenuItem addSpline = new JMenuItem("Add Spline");
        addSpline.addActionListener(new MenuActionListener(mw));
        add(addSpline);
        
        JMenuItem changeColor = new JMenuItem("Background color");
        changeColor.addActionListener(new MenuActionListener(mw));
        add(changeColor);
        
        JMenuItem grid = new JMenuItem("Show/Hide Grid");
        grid.addActionListener(new MenuActionListener(mw));
        add(grid);
    }
}
