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

public class TextEditorForSpline extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel output;
	private JComboBox fontList;
	private JComboBox fontSize;
	private JEditorPane textBox;
	private JComboBox fontStyle;
	private JCheckBox chckbxCurve;
	private JCheckBox chckbxAutorotation;

	private Main internalFrame;
	private Spline spline = null;
	
	private String currentFName;
	private int currentFStyle;
	private int currentFSize;
	//private boolean showCurve;
	
	 private static final int[] FONT_STYLE_CODES =
		    {
		        Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC
		    };
	 private static final int[] fontSizes = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60};
	 static int openFrameCount = 0;
	    static final int xOffset = 30, yOffset = 30;
	    private JButton button;
	    private JButton button_1;
		private Color color;
		private JButton button_2;
		private JButton btnMore;
		private Color shadowColor;
		private int shadowDistance;
		private Object sliderDistance;
		private int shadowAngle;
		private int ShadowTransparency;
		private int shadowSize;
		private boolean autoResize;
	    
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
////		EventQueue.invokeLater(new Runnable() {
////			public void run() {
////				try {
////					TextEditor frame = new TextEditor();
////					frame.setVisible(true);
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
////			}
////		});
//	}

	/**
	 * Create the frame.
	 */
	public TextEditorForSpline(Main Main) {
		super("Document #" + (++openFrameCount), 
				  false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		
		this.internalFrame = Main;
		this.color = Color.black;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textBox = new JEditorPane();
		textBox.setBounds(34, 61, 355, 61);
		contentPane.add(textBox);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//updateOutput();
				internalFrame.applyItemChanges(getLabel(), isShowCurve(), isAutorotation());
				setVisible(false);
			}
		});
		btnApply.setBounds(34, 200, 97, 25);
		contentPane.add(btnApply);
		
		fontList = new JComboBox();
		fontList.setBounds(34, 26, 182, 22);
		contentPane.add(fontList);
		
		fontSize = new JComboBox();
		fontSize.setBounds(228, 26, 75, 22);
		contentPane.add(fontSize);
		
		output = new JLabel("New label");
		output.setVisible(false);
		output.setBounds(34, 149, 56, 16);
		contentPane.add(output);
		
		fontStyle = new JComboBox();
		fontStyle.setBounds(314, 26, 75, 22);
		contentPane.add(fontStyle);
		
		
		String[] fontStyles =
			    {
			        "PLAIN", "BOLD", "ITALIC", "BOLD | ITALIC"
			    };
		for (String s:fontStyles)
			fontStyle.addItem(s);
		
		for (int i:fontSizes)
			fontSize.addItem(i);
		
		int defaultFont = -1;
		String fonts[] = 
			      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		for ( int i = 0; i < fonts.length; i++ )
		{
			fontList.addItem(fonts[i]);
			if (defaultFont == -1 && fonts[i].contains("Arial"))
				defaultFont = i;
		}
		
		if (defaultFont == -1)
			defaultFont = 0;
		fontList.setSelectedIndex(defaultFont);
		fontSize.setSelectedIndex(18);
		
		chckbxCurve = new JCheckBox("curve");
		chckbxCurve.setBounds(290, 131, 113, 25);
		chckbxCurve.setVisible(false);
		chckbxCurve.setSelected(true);
		contentPane.add(chckbxCurve);
		
		button = new JButton("Add");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.addTextToSpline(getLabel(), isShowCurve(), isAutorotation(), Item.TEXT, spline);
				setVisible(false);
			}
		});
		button.setBounds(140, 200, 97, 25);
		contentPane.add(button);
		
		button_1 = new JButton("Color");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.showColorChooser(ColorEditor.TEXTCOLOR, Item.TEXT);
			}
		});
		button_1.setBounds(254, 200, 97, 25);
		contentPane.add(button_1);
		
		chckbxAutorotation = new JCheckBox("autorotation");
		chckbxAutorotation.setBounds(290, 166, 113, 25);
		contentPane.add(chckbxAutorotation);
		
		button_2 = new JButton("Close");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		button_2.setBounds(34, 238, 97, 25);
		contentPane.add(button_2);
		
		btnMore = new JButton("More");
		btnMore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.showImageEditor(chckbxAutorotation.isSelected(), getShadowDistance(), getShadowAngle(), getShadowTransparency(), getShadowSize(), getShadowColor(), isAutoResize());
			}

			
		});
		btnMore.setBounds(140, 238, 97, 25);
		contentPane.add(btnMore);
		
		JLabel lblAddTextTo = new JLabel("Add Text to spline");
		lblAddTextTo.setBounds(34, 0, 144, 16);
		contentPane.add(lblAddTextTo);
		
		

	}
	
	private void updateOutput(){
		output.setText(textBox.getText());
		//String fontName = fontList.gets
		currentFStyle = FONT_STYLE_CODES[fontStyle.getSelectedIndex()];
		currentFSize = (int) fontSize.getSelectedItem();
		currentFName = (String)fontList.getSelectedItem();
		Font font = new Font(currentFName, currentFStyle, currentFSize);
		//output.setFont(new Font(fontList.getSelectedItem(), FONT_STYLE_CODES[fontStyle.getSelectedIndex()], fontSize.getSelectedItem()));
		output.setFont(font);
		output.setForeground(color);
	}
	
	public void setText(String s){
		textBox.setText(s);
	}
	
	public String getText(){
		return output.getText();
	}
	
	public void setText(JLabel label){
		
		String fonts[] = 
			      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		int fontIndex = 0;
		for (int i=0; i<fonts.length;i++){
			if (label.getFont().getName().equals(fonts[i])){
				fontIndex = i;
				break;
			}		
		}
		
		System.out.println("setText "+ label.getText());
		textBox.setText(label.getText());
		fontList.setSelectedIndex(fontIndex);
		fontSize.setSelectedIndex((int) label.getFont().getSize()-2);
		repaint();
		
	}
	
	public JLabel getLabel(){
		updateOutput();
		JLabel label = new JLabel();
		label.setFont(new Font(currentFName, currentFStyle, currentFSize));
		label.setText(textBox.getText());
		label.setForeground(color);
		return label;
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
		return chckbxCurve.isSelected();
	}

	public void setShowCurve(boolean showCurve) {
		chckbxCurve.setSelected(showCurve);;
	}
	
	public boolean isAutorotation(){
		return chckbxAutorotation.isSelected();
	}
	
	public void setAutorotation(boolean autorotation){
		chckbxAutorotation.setSelected(autorotation);
	}

	public void setTextColor(Color color) {
		this.color = color;
		getLabel().setForeground(color);
		output.setForeground(color);
		
	}
	
	public void setShadowColor(Color color) {
		this.shadowColor = color;
		
	}

	public int getShadowDistance() {
		return shadowDistance;
	}

	public void setShadowDistance(int shadowDistance) {
		this.shadowDistance = shadowDistance;
		//this.sliderDistance.setValue(shadowDistance);
	}

	public int getShadowAngle() {
		return shadowAngle;
	}

	public void setShadowAngle(int shadowAngle) {
		this.shadowAngle = shadowAngle;
		//this.sliderRotation.setValue(shadowAngle);
	}

	public int getShadowTransparency() {
		return ShadowTransparency;
	}

	public void setShadowTransparency(int shadowTransparency) {
		this.ShadowTransparency = shadowTransparency;
		//this.sliderTransparent.setValue(shadowTransparency);
	}

	public double getShadowSize() {
		return shadowSize;
	}

	public void setShadowSize(int shadowSize) {
		this.shadowSize = shadowSize;
		//this.sliderShadowSize.setValue(shadowSize);
	}
	
	public Color getShadowColor() {
		return shadowColor;
	}



	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setAutoResize(boolean autoResize) {
		this.autoResize = autoResize;
		
	}
	
	private boolean isAutoResize() {
		return this.autoResize;
	}

	public Spline getSpline() {
		return spline;
	}

	public void setSpline(Spline spline) {
		this.spline = spline;
	}
}
