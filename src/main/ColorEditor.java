/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class ColorEditor extends JInternalFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Main internalFrame;

	 static int openFrameCount = 0;
	 private int request;

	private int type;
	 public static int BACKGROUNDCOLOR = 0;
	 public static int TEXTCOLOR = 1;
	 public static int SHADOWCOLOR = 2;
	 public static int SPLINECOLOR = 3;
	protected static final int BRUSHCOLOR = 4;

	    
	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public ColorEditor(Main internalFrame) {
		super("Document #" + (++openFrameCount), 
	              false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		
		this.internalFrame = internalFrame;
		
		//Create and set up the content pane.
		ColorChooser newContentPane = new ColorChooser();
        newContentPane.setOpaque(true); //content panes must be opaque
        setContentPane(newContentPane);
 
        
        
        JButton btnApply = new JButton("apply");
        btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (request == ColorEditor.TEXTCOLOR){
					internalFrame.setTextColor(newContentPane.getColor());
				}else if (request == ColorEditor.BACKGROUNDCOLOR){
					internalFrame.setBackgroundColor(newContentPane.getColor());
				}else if (request == ColorEditor.SHADOWCOLOR){
					internalFrame.setShadowColor(newContentPane.getColor(), type);
				}else if (request == ColorEditor.SPLINECOLOR){
					internalFrame.setSplineColor(newContentPane.getColor(), type);
				}else if (request == ColorEditor.BRUSHCOLOR){
					internalFrame.setBrushColor(newContentPane.getColor(), type);
				}
				
				setVisible(false);
			}
		});
		
		 JButton btnClose = new JButton("close");
		 btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			
		
		JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnApply);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnClose);

        
//		btnApply_1.setVerticalTextPosition(AbstractButton.CENTER);
//		btnApply_1.setHorizontalTextPosition(AbstractButton.LEADING);
		JPanel bannerPanel = new JPanel(new BorderLayout());
         bannerPanel.add(buttonPane,BorderLayout.CENTER);
     // bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));
		
		newContentPane.add(bannerPanel,BorderLayout.PAGE_END);
		
        
        
	}
	

	
	public void run(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
		 
		        
		 
		        //Display the window.
		        pack();
		        //setVisible(true);
		        
				
			}
		});
	}

	public void setInternalFrame(Main internalFrame){
		this.internalFrame = internalFrame;
	}



	public int getRequest() {
		return request;
	}



	public void setRequest(int request, int type) {
		this.request = request;
		this.type = type;
	}
	
}