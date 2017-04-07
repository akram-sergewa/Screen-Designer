/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JSlider;

public class ImageEditor extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JCheckBox chckbxShadow;
	private JCheckBox chckbxAutorotation;
	private JSlider sliderShadowSize;
	private boolean shadow;

	private Main internalFrame;
	
	private String currentFName;

	private JSlider sliderDistance;	
	private JSlider sliderRotation;
	
	 static int openFrameCount = 0;
	    static final int xOffset = 30, yOffset = 30;
		private Color shadowColor;
		private JButton btnClose;
		private JLabel lblTransparent;
		private JSlider sliderTransparent;
		private int shadowDistance;
		private int shadowAngle;
		private int ShadowTransparency;
		private int shadowSize;
		private JCheckBox chckbxAutoresize;
	    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TextEditor frame = new TextEditor();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public ImageEditor(Main Main) {
		super("Document #" + (++openFrameCount), 
				  false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		
		this.internalFrame = Main;
		this.shadowColor = Color.BLACK;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//updateOutput();
				internalFrame.applyImageChanges(isShowShadow(), isAutorotation(),sliderDistance.getValue(),  sliderRotation.getValue(), sliderShadowSize.getValue(), shadowColor, sliderTransparent.getValue(), chckbxAutoresize.isSelected());
				setVisible(false);
			}
		});
		btnApply.setBounds(34, 200, 97, 25);
		contentPane.add(btnApply);
		
	
		
	
		
		chckbxShadow = new JCheckBox("shadow");
		chckbxShadow.setBounds(313, 22, 113, 25);
		contentPane.add(chckbxShadow);
		
		chckbxAutorotation = new JCheckBox("autorotation");
		chckbxAutorotation.setBounds(313, 65, 113, 25);
		contentPane.add(chckbxAutorotation);
		
		sliderDistance = new JSlider();
		sliderDistance.setBounds(79, 22, 200, 26);
		sliderDistance.setValue(20);
		contentPane.add(sliderDistance);
		
		sliderRotation = new JSlider();
		sliderRotation.setBounds(79, 65, 200, 26);
		sliderRotation.setValue(20);
		contentPane.add(sliderRotation);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnClose.setBounds(154, 200, 97, 25);
		contentPane.add(btnClose);
		
		JLabel lblDistance = new JLabel("Distance");
		lblDistance.setBounds(12, 26, 56, 16);
		contentPane.add(lblDistance);
		
		JLabel lblRotation = new JLabel("Angle");
		lblRotation.setBounds(12, 69, 56, 16);
		contentPane.add(lblRotation);
		
		sliderShadowSize = new JSlider();
		sliderShadowSize.setValue(50);
		sliderShadowSize.setBounds(79, 104, 200, 26);
		contentPane.add(sliderShadowSize);
		
		
		
		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(12, 108, 56, 16);
		contentPane.add(lblSize);
		
		JButton btnShadowColor = new JButton("Shadow color");
		btnShadowColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.showColorChooser(ColorEditor.SHADOWCOLOR, Item.IMAGE);
			}
		});
		btnShadowColor.setBounds(273, 200, 113, 25);
		contentPane.add(btnShadowColor);
		
		lblTransparent = new JLabel("transparent");
		lblTransparent.setBounds(12, 147, 56, 16);
		contentPane.add(lblTransparent);
		
		sliderTransparent = new JSlider();
		sliderTransparent.setValue(60);
		sliderTransparent.setBounds(79, 143, 200, 26);
		contentPane.add(sliderTransparent);
		
		chckbxAutoresize = new JCheckBox("autoresize");
		chckbxAutoresize.setBounds(313, 105, 113, 25);
		contentPane.add(chckbxAutoresize);
		
		

	}
	
	protected boolean isShowShadow() {
		// TODO Auto-generated method stub
		return chckbxShadow.isSelected();
	}

	
	


	
	
	
	public void run(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
//				try {
//					setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		});
	}
	
	public String getCurrentFName (){
		return currentFName;
	}

	public boolean isShowCurve() {
		return chckbxShadow.isSelected();
	}

	public void setShowCurve(boolean showCurve) {
		chckbxShadow.setSelected(showCurve);;
	}
	
	public boolean isAutorotation(){
		return chckbxAutorotation.isSelected();
	}
	
	public void setAutorotation(boolean autorotation){
		chckbxAutorotation.setSelected(autorotation);
	}


	public int getShadowDistance() {
		return shadowDistance;
	}

	public void setShadowDistance(int shadowDistance) {
		this.shadowDistance = shadowDistance;
		this.sliderDistance.setValue(shadowDistance);
	}

	public int getShadowAngle() {
		return shadowAngle;
	}

	public void setShadowAngle(int shadowAngle) {
		this.shadowAngle = shadowAngle;
		this.sliderRotation.setValue(shadowAngle);
	}

	public int getShadowTransparency() {
		return ShadowTransparency;
	}

	public void setShadowTransparency(int shadowTransparency) {
		this.ShadowTransparency = shadowTransparency;
		this.sliderTransparent.setValue(shadowTransparency);
	}

	public double getShadowSize() {
		return shadowSize;
	}

	public void setShadowSize(int shadowSize) {
		this.shadowSize = shadowSize;
		this.sliderShadowSize.setValue(shadowSize);
	}
	
	public Color getShadowColor() {
		return shadowColor;
	}
	public void setShadowColor(Color shadowColor){
		this.shadowColor = shadowColor;
	}

	public void setAutoResize(boolean autoResize) {
		chckbxAutoresize.setSelected(autoResize);
		
	}
}
