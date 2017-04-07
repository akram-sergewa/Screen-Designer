/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.Icon;

public class itemEditor extends JInternalFrame{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private Curve curve = null;
	private ShapedWindow sw;
	private TextEditor textEditor;
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
    private JTextField itemTitle;
    private Item selectedItem = null;
    private JTextField textFieldWidth;
    private JTextField textFieldHeight;
	//private Item currentItem = null;
	private JCheckBox chckbxHide;
	private JLabel labelType;
	private JLabel lblType;
	private JLabel lblWidth;
	private JLabel lblHeight;

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
	public itemEditor(Main internalFrame) {
		 super("editor", 
				 false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
	 
		 this.internalFrame = internalFrame;
		 initialize();
		 
		 
		 
		// setBackground(Color.GRAY.brighter());

	        //...Then set the window size or call pack...
	        setSize(348,208);
	 
	        //Set the window's location.
	        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
	        
	        items = internalFrame.getItems();
	        
	        this.sw = internalFrame.getShapedWindow();
	        System.out.println(sw);

//		textEditor = new TextEditor();
//		textEditor.run();
//		initialize();
//		
//		this.curve = new Curve(this);
//		curve.run();
//		//this.setVisible(true);
		
		
		
	}
	

	
//	public void updateTable(){
//		String[] columnNames = {"Name"};
//		Object[][] data = {};
//		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
//
//		for (int key: layers.keySet()) {
//			Object[] data0 = {layers.get(key)};
//			tableModel.addRow(data0);
//		}
//		table.setModel(tableModel);
//		
//	}


	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		getContentPane().setLayout(null);
		
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
		panel.setBounds(-12, 0, 370, 323);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		BufferedImage btnAddImageImage = null;
		BufferedImage btnAddTextImage = null;
		BufferedImage btnAddSplineImage = null;
		BufferedImage btnAddBrushImage = null;
		BufferedImage btnEditImage = null;
		BufferedImage btnDeleteImage = null;
		
		try {
			btnAddImageImage = ImageIO.read(new File("Resources/image_icon_small.jpg"));
			btnAddTextImage = ImageIO.read(new File("Resources/text_icon_small.jpg"));
			btnAddSplineImage = ImageIO.read(new File("Resources/spline_icon_small.jpg"));
			btnEditImage = ImageIO.read(new File("Resources/edit_icon_small.jpg"));
			btnAddBrushImage = ImageIO.read(new File("Resources/brush_icon_small.jpg"));

			btnDeleteImage = ImageIO.read(new File("Resources/delete_icon_small.jpg"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("can't find image");
			e1.printStackTrace();
		}
				
			
				
						JButton btnDelete = new JButton(new ImageIcon(btnDeleteImage));
						btnDelete.setBounds(290, 101, 40, 40);
						btnDelete.setBorder(null);
						panel.add(btnDelete);
						
						JButton btnEdit = new JButton(new ImageIcon(btnEditImage));
						btnEdit.setBounds(229, 101, 40, 40);
						btnEdit.setBorder(null);
						panel.add(btnEdit);
						
						itemTitle = new JTextField();
						itemTitle.setBounds(25, 46, 162, 22);
						panel.add(itemTitle);
						itemTitle.setColumns(10);
						
						itemTitle.addKeyListener(new KeyListener(){

							@Override
							public void keyPressed(KeyEvent arg0) {
								if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
									selectedItem.setTitle(itemTitle.getText());
									internalFrame.updateTable();
								}
								
							}

							@Override
							public void keyReleased(KeyEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void keyTyped(KeyEvent arg0) {
								// TODO Auto-generated method stub
								
							}
							
						});
						
						chckbxHide = new JCheckBox("Hide");
						chckbxHide.setBackground(Color.DARK_GRAY);
						chckbxHide.setForeground(Color.ORANGE);
						chckbxHide.setOpaque(false);
						chckbxHide.setBounds(206, 45, 113, 25);
						panel.add(chckbxHide);
						
						chckbxHide.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent arg0) {
								selectedItem.setHide(chckbxHide.isSelected());
								sw.repaint();
								internalFrame.updateTable();
								
							}
							
						});
						
						labelType = new JLabel("Type");
						labelType.setForeground(Color.ORANGE);
						labelType.setBounds(93, 13, 56, 16);
						panel.add(labelType);
						
						lblWidth = new JLabel("width");
						lblWidth.setForeground(Color.WHITE);
						lblWidth.setBounds(25, 93, 79, 16);
						panel.add(lblWidth);
						
						lblHeight = new JLabel("height");
						lblHeight.setForeground(Color.WHITE);
						lblHeight.setBounds(25, 125, 79, 16);
						panel.add(lblHeight);
						
						textFieldWidth = new JTextField();
						textFieldWidth.setBackground(Color.DARK_GRAY);
						textFieldWidth.setForeground(Color.ORANGE);
						textFieldWidth.setColumns(10);
						textFieldWidth.setBounds(106, 90, 73, 22);
						panel.add(textFieldWidth);
						
						textFieldHeight = new JTextField();
						textFieldHeight.setForeground(Color.ORANGE);
						textFieldHeight.setBackground(Color.DARK_GRAY);
						textFieldHeight.setColumns(10);
						textFieldHeight.setBounds(106, 119, 73, 22);
						panel.add(textFieldHeight);
						
						lblType = new JLabel("TYPE :");
						lblType.setForeground(Color.WHITE);
						lblType.setBounds(25, 13, 56, 16);
						panel.add(lblType);
						btnEdit.addActionListener(new ActionListener() {
							

							public void actionPerformed(ActionEvent e) {
								Item currentItem = selectedItem;
								
								if (currentItem.getType() == Item.TEXT){
									Text currentItemText = (Text)currentItem;
									internalFrame.showTextEditor(currentItemText.getLabel(),currentItemText.isShowCurve(),currentItemText.isAutorotation(), currentItemText.getColor(), currentItemText.getShadowDistance(), currentItemText.getShadowRotation(), currentItemText.getShadowTransparent(), currentItemText.getShadowSize(), currentItemText.getShadowColor(), currentItemText.isAutoResize());

								}
								else if (currentItem.getType() == Item.IMAGE){
									Image currentItemImage = (Image)currentItem;
									internalFrame.showImageEditor(currentItemImage.isAutorotation(),currentItemImage.getShadowDistance(), currentItemImage.getShadowRotation(), currentItemImage.getShadowTransparent(), currentItemImage.getShadowSize(), currentItemImage.getShadowColor(), currentItemImage.isAutoResize());
									//imageEditor.setAutorotation(currentItemImage.isAutorotation());
								}
								else if (currentItem.getType() == Item.SPLINE){
									Spline cSpline = (Spline)currentItem;
									
									internalFrame.showSplineEditor(cSpline.isAutorotation(), cSpline.getColor(), cSpline.getCurveWidth(), cSpline.getShadowDistance(), cSpline.getShadowRotation(), cSpline.getShadowTransparent(), cSpline.getShadowSize(), cSpline.getShadowColor());
									//imageEditor.setAutorotation(currentItemImage.isAutorotation());
								}
								else if (currentItem.getType() == Item.BRUSH){
									Brush cBrush = (Brush)currentItem;
									
									internalFrame.showBrushEditor(cBrush.isAutorotation(), cBrush.getColor(), cBrush.getCurveWidth(), cBrush.getShadowDistance(), cBrush.getShadowRotation(), cBrush.getShadowTransparent(), cBrush.getShadowSize(), cBrush.getShadowColor());
									//imageEditor.setAutorotation(currentItemImage.isAutorotation());
								}
								

							}
						});
						btnDelete.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								deleteCurrentItem();
								
							}
						});
		
		
		
		
	}

	
	protected void showHideGrid(){
			sw.setShowGrid(!sw.isShowGrid());
			sw.repaint();

	}

	

	protected void showTextEditor() {
		internalFrame.showTextEditor();
		
	}

	
	

	public void setTextEditor (TextEditor textEditor){
		this.textEditor = textEditor;
	}

	public void setCurve (Curve curve){
		this.curve = curve;
		internalFrame.createCustomWindow();
	}

	public void setStateShapedWindow(int newState) {
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
			internalFrame.createCustomWindow();
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
			internalFrame.createCustomWindow();
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

	public void addText() {
		showTextEditor();
		
	}


	public void deleteCurrentItem() {

		
//			Item i = selectedItem;
//			//remove Item
//			
//			sw.removeItem(i);
//			layers.remove(s);
//			
//			//fix current splines and brushes bugs
//			sw.setCurrentBrush(null);
//			sw.setCurrentSpline(null);
//		
//		
//		internalFrame.createCustomWindow();
//		//sw.reload();
		//updateTable();
	}



	public Item getSelectedItem() {
		return selectedItem;
	}



	public void setSelectedItem(Item selectedItem) {
		this.selectedItem = selectedItem;
		updateWindow();
	}



	public void updateWindow() {
		if (selectedItem == null)
			return;
		
		itemTitle.setText(selectedItem.getTitle());
		textFieldHeight.setVisible(false);
		lblHeight.setVisible(false);
		
		chckbxHide.setSelected(selectedItem.isHide());
		labelType.setText(selectedItem.getTypeName());

		if (selectedItem.getType() == Item.BRUSH){
			
			Brush brush = (Brush) selectedItem;
			
			lblWidth.setText("brush size");
			textFieldWidth.setText(brush.getCurveWidth()+"");
		}else if (selectedItem.getType() == Item.SPLINE){
			Spline spline = (Spline) selectedItem;
			textFieldWidth.setText(spline.getCurveWidth()+"");
			lblWidth.setText("curve size");
		}else{
			lblWidth.setText("width");
			textFieldWidth.setText(Integer.toString(selectedItem.getWidth()));
			textFieldHeight.setText(Integer.toString(selectedItem.getHeight()));
			textFieldHeight.setVisible(true);
			lblHeight.setVisible(true);
		}
		
		

	}
}
