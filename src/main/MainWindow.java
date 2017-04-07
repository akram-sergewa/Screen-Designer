/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

public class MainWindow extends JInternalFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField textFieldWidth;
	private Curve curve = null;
	private ShapedWindow sw;
	private TextEditor textEditor;
	private JTextField textFieldHeight;
	private HashMap<Integer, Item> layers = null;
	private int orderCounter = 0;
	public static int PATH_SHAPE = 0;
	public static int CIRCLE_SHAPE = 1;
	public static int SQUARE_SHAPE = 2;
	private int windowShape;
	static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    private Main internalFrame;
    private List <Item> items;
    private int imageCounter = 0;
    private int splineCounter = 0;
	//private Item currentItem = null;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainWindow window = new MainWindow();
//					window.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//	}

	/**
	 * Create the application.
	 */
	public MainWindow(Main internalFrame) {
		 super("Main", 
	              false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
	 
		 this.internalFrame = internalFrame;
		 
		 this.sw = new ShapedWindow(this);
		 initialize();
		 

		 
	        //...Then set the window size or call pack...
	        setSize(348,330);
	 
	        //Set the window's location.
	        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
	        
	        items = new ArrayList<Item>();

//		textEditor = new TextEditor();
//		textEditor.run();
//		initialize();
//		
//		this.curve = new Curve(this);
//		curve.run();
//		//this.setVisible(true);
		
		
		
	}
	
	public void updateText(List<Integer> t){
//		textFieldX.setText(String.valueOf(t.get(0)));
//		textFieldY.setText(String.valueOf(t.get(1)));
		textFieldWidth.setText(String.valueOf(t.get(2)));
		textFieldHeight.setText(String.valueOf(t.get(3)));

	}

	public void createCustomWindow(){
//		javax.swing.JFrame test = new JFrame();
//		test.setBounds(0,0,500,500);
//		test.setVisible(true);
		
		if (sw != null){
			sw.closeWindow();
		}else{
			sw = new ShapedWindow(this);
		}
		
		sw.setItems(this.items);
		sw.setCenter(internalFrame.getCenter());
//		for (Item i : sw.getItems()){
//			System.out.println(i.getText());
//		}
		
	    
	    double w = 0;
	    double h = 0;
	    
	    try  
	    {  
	      w = Double.parseDouble(textFieldWidth.getText());
	      h = Double.parseDouble(textFieldHeight.getText());
	    }  
	    catch(NumberFormatException nfe)  
	    {  
	        
	    }  
	    sw.updatePath(curve.getPath(),w ,h ); 
	    sw.updateShapeType(windowShape);
	    
	    //String text = HTMLEditor.getText();
	    //sw.updateText(text);
	    sw.run();
	    
	    internalFrame.updateScaleBar(sw.getWidth(), sw.getHeight());
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		getContentPane().setLayout(null);
		
		BufferedImage colorImage = null;
		BufferedImage saveImageImage = null;
		BufferedImage applyImage = null;
		try {
			colorImage = ImageIO.read(new File("Resources/color_icon_small.jpg"));
			saveImageImage = ImageIO.read(new File("Resources/saveImage_icon_small.jpg"));
			applyImage = ImageIO.read(new File("Resources/apply_icon_small.jpg"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("can't find image");
			e1.printStackTrace();
		}
		
		
		 //Group the radio buttons.
	      ButtonGroup group = new ButtonGroup();
		
		JPanel panel = new JPanel(){
			@Override
			 public void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D) g;
               Color color1 = new Color(50,50,50);
               Color color2 = new Color(85,85,85);
               int w = getWidth()*2;
               int h = getHeight()*2;
               

               GradientPaint gp = new GradientPaint(
                   0, 0, color1, w, h, color2);
               g2d.setPaint(gp);
               g2d.fillRect(0, 0, w, h);
           }
		};
		panel.setBounds(0, 0, 356, 319);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblWidth = new JLabel("width");
		lblWidth.setForeground(Color.WHITE);
		lblWidth.setBounds(25, 40, 56, 16);
		panel.add(lblWidth);
		
		textFieldWidth = new JTextField();
		textFieldWidth.setBackground(Color.DARK_GRAY);
		textFieldWidth.setForeground(Color.ORANGE);
		textFieldWidth.setBounds(80, 37, 75, 22);
		panel.add(textFieldWidth);
		textFieldWidth.setColumns(10);
		
		textFieldHeight = new JTextField();
		textFieldHeight.setBackground(Color.DARK_GRAY);
		textFieldHeight.setForeground(Color.ORANGE);
		textFieldHeight.setBounds(80, 73, 75, 22);
		panel.add(textFieldHeight);
		textFieldHeight.setColumns(10);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setForeground(Color.WHITE);
		lblHeight.setBounds(25, 76, 56, 16);
		panel.add(lblHeight);
		
		
		
		JButton btnApply = new JButton(new ImageIcon(applyImage));
		btnApply.setBounds(79, 221, 40, 40);
		btnApply.setBorder(null);
		panel.add(btnApply);
		
		JRadioButton rdbtnSpline = new JRadioButton("Spline");
		rdbtnSpline.setForeground(Color.ORANGE);
		rdbtnSpline.setOpaque(false);
		rdbtnSpline.setBounds(197, 37, 127, 25);
		panel.add(rdbtnSpline);
		rdbtnSpline.setSelected(true);
		
		rdbtnSpline.addItemListener(new ItemListener() {
		   public void itemStateChanged(ItemEvent e) {  
		  	 if (e.getStateChange() == 1){
		  		 windowShape = PATH_SHAPE;
		  		 sw.updateShapeType(windowShape);
		  		 createCustomWindow();
		  	 }
		     
		   }           
		});
		group.add(rdbtnSpline);
		
		JRadioButton radioButton = new JRadioButton("circle");
		radioButton.setForeground(Color.ORANGE);
		radioButton.setOpaque(false);
		radioButton.setBounds(197, 72, 127, 25);
		panel.add(radioButton);
		radioButton.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {  
	        	 if (e.getStateChange() == 1){
	        		 windowShape = CIRCLE_SHAPE;
	        		 sw.updateShapeType(windowShape);
	        		 createCustomWindow();
	        	 }
	           
	         }           
	      });
		group.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("square");
		radioButton_1.setForeground(Color.ORANGE);
		radioButton_1.setOpaque(false);
		radioButton_1.setBounds(197, 107, 127, 25);
		panel.add(radioButton_1);
		radioButton_1.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {  
	        	 if (e.getStateChange() == 1){
	        		 windowShape = SQUARE_SHAPE;
	        		 sw.updateShapeType(windowShape);
	        		 createCustomWindow();
	        	 }
	           
	         }           
	      });
		group.add(radioButton_1);
		
		JButton button = new JButton(new ImageIcon(saveImageImage));
		button.setBorder(null);
		button.setBounds(131, 221, 40, 40);
		panel.add(button);
		
		JCheckBox chckbxShowGrid = new JCheckBox("show grid");
		chckbxShowGrid.setForeground(Color.ORANGE);
		chckbxShowGrid.setOpaque(false);
		chckbxShowGrid.setBounds(199, 143, 113, 25);
		panel.add(chckbxShowGrid);
		
		
	
		
		
		JButton btnColor = new JButton(new ImageIcon(colorImage));
		btnColor.setBorder(null);
		btnColor.setBounds(184, 221, 40, 40);
		panel.add(btnColor);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showBackgroundColor();
			}
		});
		chckbxShowGrid.addActionListener(new ActionListener() {
		      	public void actionPerformed(ActionEvent arg0) {
		      		
		      			sw.setShowGrid(chckbxShowGrid.isSelected());
		      			sw.repaint();
		      		
		      	}
		      });
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsImage();
			}
		});
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createCustomWindow();
			}
		});
	   
		
		
		
	}

	
	protected void showHideGrid(){
			sw.setShowGrid(!sw.isShowGrid());
			sw.repaint();

	}
	protected void showBackgroundColor() {
		internalFrame.showColorChooser(ColorEditor.BACKGROUNDCOLOR, -1);
		
	}

	protected void saveAsImage() {
		//start save dialog
		JFileChooser chooser = new JFileChooser();
	       setShapedWindowBehind();

	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "png files", "png");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to save this file: " +
	       chooser.getSelectedFile().getAbsolutePath());
	    }else{
	    	setShapedWindowOnTop();
	    	return;
	    }
	    
	    //do the saving
	    String path = chooser.getSelectedFile().getAbsolutePath();			    
	    
		BufferedImage im = new BufferedImage(sw.getWidth(), sw.getHeight(), BufferedImage.TYPE_INT_ARGB);
  		sw.paint(im.getGraphics());

		for (int pX =0; pX <im.getWidth();pX++){
			for (int pY = 0; pY < im.getHeight(); pY++){
				
				if (!sw.getShape().contains(new Point(pX,pY))){
					int alpha = 0; 
					int red   = 0;
					int green = 0;
					int blue  = 0;

					int argb = alpha << 24 + red << 16 + green << 8 + blue;
					System.out.println(pX+" "+pY);
					im.setRGB(pX, pY, argb);
				}
			}
		}
		if (!path.contains(".png")){
			path = path+".png";
		}
  		try {
			ImageIO.write(im, "PNG", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
  		setShapedWindowOnTop();
		
	}

	
	
	public void updateHeightText(int height) {
		textFieldHeight.setText(Integer.toString(height));
		
	}

	public void updateWidthText(int width) {
		textFieldWidth.setText(Integer.toString(width));
		
	}
	
	public void setTextEditor (TextEditor textEditor){
		this.textEditor = textEditor;
	}

	public void setCurve (Curve curve){
		this.curve = curve;
		createCustomWindow();
	}

	public void setStateShapedWindow(int newState) {
		if (newState == 7){
			sw.setState(Frame.ICONIFIED);
		}else{
			sw.setState(Frame.NORMAL);
		}
		sw.setState(newState);
		
	}
	
	public void addItem (JLabel label, boolean curve, boolean autorotation, int type){
		if (type == Item.TEXT){
			Text newText = new Text(label, orderCounter++, sw, type);
			
				newText.setTitle(newText.getText());	
			
			//newItem.setText(pt1+100+pt2+textEditor.getText());
			//newItem.setLabel();
			//newItem.resetSize(sw.getFrc());
			newText.setShowCurve(curve);
			newText.setAutorotation(autorotation);
			sw.addItem(newText);
			createCustomWindow();
		}
		
	
	}
	
	
	
	public ShapedWindow getShapedWindow(){
		return this.sw;
	}

	public void removeShapedWindow() {
		//remove sw refrence from all items
		for (Item i:sw.getItems()){
			i.setShapedWindows(null);
		}
		sw.closeWindow();
		sw = null;
		
	}

	public void setShapedWindow(ShapedWindow sw) {
		this.sw = sw;
		
	}

	public List <Item> getItems() {
		if (items == null){
		}
		return items;
	}

	public void setItems(List <Item> items) {
		this.items = items;
	}

	public void insertSwInItems() {
		if (sw == null){
			createCustomWindow();
		}
		for (Item i: items){
			i.setShapedWindows(sw);
		}
		
	}
	
	public void setInternalFrame(Main internalFrame){
		this.internalFrame = internalFrame;
	}

	public void setBackgroundColor(Color color) {
		sw.setBackgroundColor(color);
		
	}

	public void showTextEditor() {
		internalFrame.showTextEditor();
		
	}

	public void addImage() {
		internalFrame.addImage();
		
	}

	public void addSpline() {
		internalFrame.addSpline();
		
	}

	public void updateTable(List<Item> items2) {
		internalFrame.updateTable(items2);
		
	}

	public void findItemInTable(Item selectedItem) {
		internalFrame.findItemInTable(selectedItem);
		
	}

	public void unselectedIntTable() {
		internalFrame.unselectedIntTable();
		
	}

	public void setShapedWindowBehind() {
		sw.setAlwaysOnTop(false);
		
	}

	public void setShapedWindowOnTop() {
		sw.setAlwaysOnTop(true);
		
	}

	public void updateMouseBarText(String text) {
		internalFrame.updateMouseBarText(text);
		
	}

	public void mainToFront() {
		internalFrame.toFront();
		
	}

	public void updateInfoWindow() {
		internalFrame.updateInfoWindow();
		
	}

	public void addTextToSpline(JLabel label, boolean showCurve, boolean autorotation, int type, Spline spline) {
		Text newText = new Text(label, orderCounter++, sw, type);
		
		newText.setTitle(newText.getText());	
	
		//newItem.setText(pt1+100+pt2+textEditor.getText());
		//newItem.setLabel();
		//newItem.resetSize(sw.getFrc());
		newText.setShowCurve(showCurve);
		newText.setAutorotation(autorotation);
		newText.setSpline(spline);
		sw.addItem(newText);
		createCustomWindow();
		
	}

	public void setMainWindowState(int newState) {
		internalFrame.setState(newState);
		
	}

	public void showHelp(boolean b) {
		internalFrame.setShowHelp(b);
		
	}

	public void setHelp(String string) {
		internalFrame.setHelp("");
		
	}

}
