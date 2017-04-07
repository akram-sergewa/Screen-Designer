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
import javax.swing.JScrollPane;

public class SplineEditor extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JCheckBox chckbxShadow;
	private JCheckBox chckbxAutorotation;
	private JSlider sliderShadowSize;
	private JSlider sliderCurveWidth;
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
		private JButton btnSplineColor;
		private double curveWidth;
		private int shadowDistance;
		private int shadowAngle;
		private int ShadowTransparency;
		private double shadowSize;
		private Color splineColor;
		private JButton btnCreateText;
		
		
		
		
	    
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
	public SplineEditor(Main Main) {
		super("Document #" + (++openFrameCount), 
				  false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		
		this.internalFrame = Main;
		this.shadowColor = Color.BLACK;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//updateOutput();
				internalFrame.applySplineChanges(getSplineColor(), sliderCurveWidth.getValue(), isShowShadow(), isAutorotation(),sliderDistance.getValue(),  sliderRotation.getValue(), sliderShadowSize.getValue(), shadowColor, sliderTransparent.getValue());
				setVisible(false);
			}
		});
		btnApply.setBounds(25, 269, 97, 25);
		contentPane.add(btnApply);
		
	
		
	
		
		chckbxShadow = new JCheckBox("shadow");
		chckbxShadow.setBounds(309, 75, 113, 25);
		contentPane.add(chckbxShadow);
		
		chckbxAutorotation = new JCheckBox("autorotation");
		chckbxAutorotation.setBounds(309, 118, 113, 25);
		contentPane.add(chckbxAutorotation);
		
		sliderDistance = new JSlider();
		sliderDistance.setBounds(79, 80, 200, 26);
		sliderDistance.setValue(0);
		contentPane.add(sliderDistance);
		
		sliderRotation = new JSlider();
		sliderRotation.setBounds(79, 123, 200, 26);
		sliderRotation.setValue(0);
		contentPane.add(sliderRotation);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnClose.setBounds(153, 269, 97, 25);
		contentPane.add(btnClose);
		
		JLabel lblDistance = new JLabel("Distance");
		lblDistance.setBounds(12, 84, 56, 16);
		contentPane.add(lblDistance);
		
		JLabel lblRotation = new JLabel("Angle");
		lblRotation.setBounds(12, 127, 56, 16);
		contentPane.add(lblRotation);
		
		sliderShadowSize = new JSlider();
		sliderShadowSize.setValue(50);
		sliderShadowSize.setBounds(79, 162, 200, 26);
		contentPane.add(sliderShadowSize);
		
		
		
		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(12, 166, 56, 16);
		contentPane.add(lblSize);
		
		JButton btnShadowColor = new JButton("Shadow color");
		btnShadowColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.showColorChooser(ColorEditor.SHADOWCOLOR, Item.SPLINE);
			}
		});
		btnShadowColor.setBounds(272, 269, 113, 25);
		contentPane.add(btnShadowColor);
		
		lblTransparent = new JLabel("transparent");
		lblTransparent.setBounds(12, 205, 56, 16);
		contentPane.add(lblTransparent);
		
		sliderTransparent = new JSlider();
		sliderTransparent.setValue(60);
		sliderTransparent.setBounds(79, 201, 200, 26);
		contentPane.add(sliderTransparent);
		
		btnSplineColor = new JButton("Spline Color");
		btnSplineColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.showColorChooser(ColorEditor.SPLINECOLOR, Item.SPLINE);

			}
		});
		btnSplineColor.setBounds(309, 179, 113, 25);
		contentPane.add(btnSplineColor);
		
		JLabel lblWidth = new JLabel("CurveWidth");
		lblWidth.setBounds(0, 44, 68, 16);
		contentPane.add(lblWidth);
		
		sliderCurveWidth = new JSlider();
		sliderCurveWidth.setMaximum(20);
		sliderCurveWidth.setValue(1);
		sliderCurveWidth.setBounds(79, 40, 200, 26);
		contentPane.add(sliderCurveWidth);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(328, 269, 2, 2);
		contentPane.add(scrollPane);
		
		btnCreateText = new JButton("Create Text");
		btnCreateText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				internalFrame.showTextEditorForSpline(internalFrame.getSelectedSpline());
			}
		});
		btnCreateText.setBounds(89, 324, 215, 25);
		contentPane.add(btnCreateText);
		
		

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


	
	public boolean isAutorotation(){
		return chckbxAutorotation.isSelected();
	}
	
	public void setAutorotation(boolean autorotation){
		chckbxAutorotation.setSelected(autorotation);
	}

	public void setShadowColor(Color color) {
		this.shadowColor = color;
		
	}

	public double getCurveWidth() {
		return curveWidth;
	}

	public void setCurveWidth(int curveWidth) {
		this.curveWidth = curveWidth;
		this.sliderCurveWidth.setValue(curveWidth);
		
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

	public Color getSplineColor() {
		return splineColor;
	}

	public void setSplineColor(Color splineColor) {
		this.splineColor = splineColor;
	}
}
