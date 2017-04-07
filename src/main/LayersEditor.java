/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;

import java.awt.Color;
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

public class LayersEditor extends JInternalFrame{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private Curve curve = null;
	private ShapedWindow sw;
	private TextEditor textEditor;
	private JTable table;
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
	public LayersEditor(Main internalFrame) {
		 super("Layers", 
				 false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
	 
		 this.internalFrame = internalFrame;
		 initialize();
		 
		 
		 
		// setBackground(Color.GRAY.brighter());

	        //...Then set the window size or call pack...
	        setSize(348,330);
	 
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
	

	public void updateTable(List <Item> items){
		String[] columnNames = {"Name"};

		Object[][] data = {
		};

			DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

		layers = null;
		layers = new HashMap<Integer,Item>();
		int counter = 0;
		
		for (Item i:items){
			layers.put(counter, i);
			counter++;
			if (i.isHide()){
				
				Object[] data0 = 
				        {i.getTitle()+" ☼"};
				tableModel.addRow(data0);
				
			}else{
				Object[] data0 = 
			        {i.getTitle()};
			tableModel.addRow(data0);
			}
			
			
		}
		table.setModel(tableModel);
		
	
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
		
		
		table = new JTable();
		table.setForeground(Color.WHITE);
		table.setBackground(Color.DARK_GRAY);
		table.setBounds(17, 42, 266, 109);
		panel.add(table);
		
	      
			//table selection listener
			ListSelectionModel cellSelectionModel = table.getSelectionModel();
				

				
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
		      public void valueChanged(ListSelectionEvent arg0) {
		        String selectedData = null;

		        int[] selectedRow = table.getSelectedRows();
		        int[] selectedColumns = table.getSelectedColumns();
		        
		        
		        //unselected items
		        for (Item i: sw.getItems()){
		        	i.setSelected(false);
		        	i.setSelectedStage2(false);
		        }
		        sw.setSelectedItem(null);
		        
  
		        //set selected item
		        for (int i = 0; i < selectedRow.length; i++) {
		        	sw.getItems().get(selectedRow[i]).setSelected(true);
		        	sw.setSelectedItem(sw.getItems().get(selectedRow[i]));
		        	
		        	internalFrame.setSelectedItemInEditor(sw.getItems().get(selectedRow[i]));

//		          for (int j = 0; j < selectedColumns.length; j++) {
//		            selectedData = (String) table.getValueAt(selectedRow[i], selectedColumns[j]);
//		          }
		        }

//		        System.out.println("Selected: " + selectedData);
		        sw.repaint();
		      }

			

		    });
				
	
				
				JButton btnAddImage = new JButton(new ImageIcon(btnAddImageImage));
				btnAddImage.setBounds(178, 227, 40, 40);
				btnAddImage.setBorder(null);
				
				panel.add(btnAddImage);
				
				
				
				btnAddImage.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addImage();
					}
				});
				
			
				
						JButton btnDelete = new JButton(new ImageIcon(btnDeleteImage));
						btnDelete.setBounds(25, 227, 40, 40);
						btnDelete.setBorder(null);
						panel.add(btnDelete);
						
						JButton btnEdit = new JButton(new ImageIcon(btnEditImage));
						btnEdit.setBounds(25, 166, 40, 40);
						btnEdit.setBorder(null);
						panel.add(btnEdit);

						
						JButton btnUp = new JButton("");
						btnUp.setBounds(291, 42, 29, 30);
						panel.add(btnUp);
						btnUp.setIcon(new ImageIcon(LayersEditor.class.getResource("/javax/swing/plaf/metal/icons/sortUp.png")));
						
						JButton btnAddText = new JButton(new ImageIcon(btnAddTextImage));
						btnAddText.setBounds(178, 166, 40, 40);
						panel.add(btnAddText);
						btnAddText.setBorder(null);
						
						JButton btnDown = new JButton("");
						btnDown.setBounds(291, 121, 29, 30);
						panel.add(btnDown);
						btnDown.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								int selected = table.getSelectedRow();
								if (selected >= table.getRowCount()-1)
									return;
								
								Item currentItem = layers.get(selected);
								Item otherItem = sw.getItems().get(selected+1);
								sw.getItems().set(selected+1, currentItem);
								sw.getItems().set(selected, otherItem);
								updateTable(sw.getItems());
								table.setRowSelectionInterval(selected+1, selected+1);
							}
						});
						btnDown.setIcon(new ImageIcon(LayersEditor.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
						
						JButton btnAddSpline = new JButton(new ImageIcon(btnAddSplineImage));
						btnAddSpline.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								internalFrame.addSpline();
							}
						});
						btnAddSpline.setBounds(230, 166, 40, 40);
						btnAddSpline.setBorder(null);
						panel.add(btnAddSpline);
						
						JButton btnAddBrush = new JButton((Icon) new ImageIcon(btnAddBrushImage));
						btnAddBrush.setBorder(null);
						btnAddBrush.setBounds(230, 227, 40, 40);
						panel.add(btnAddBrush);
						btnAddBrush.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								addBrush();
								
							}
						});
						btnAddText.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								addText();
							}
						});
						btnUp.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								int selected = table.getSelectedRow();
								if (selected <= 0)
									return;
								
								Item currentItem = layers.get(selected);
								Item otherItem = sw.getItems().get(selected-1);
								sw.getItems().set(selected-1, currentItem);
								sw.getItems().set(selected, otherItem);
								updateTable(sw.getItems());
								table.setRowSelectionInterval(selected-1, selected-1);

							}
						});
						btnEdit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								int selected = table.getSelectedRow();
								Item currentItem = layers.get(selected);
								
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

	protected void addSpline() {
		sw.setCurrentSpline(null);
		sw.setCurrentBrush(null);
		//if (sw.getCurrentSpline() == null && sw.getCurrentBrush() == null){
			System.out.println("add spline");
			Spline s = new Spline(sw, "spline "+splineCounter++,Item.SPLINE);
			items.add(0,s);
			sw.setCurrentSpline(s);
			updateTable(sw.getItems());
			findItemInTable(s);
		
		
	}
	
	protected void addBrush() {
//		if (sw.getCurrentSpline() == null && sw.getCurrentBrush() == null){
		sw.setCurrentSpline(null);
		sw.setCurrentBrush(null);
			System.out.println("add Brush");
			Brush b = new Brush(sw, "Brush "+splineCounter++,Item.BRUSH);
			items.add(0,b);
			sw.setCurrentBrush(b);
			updateTable(sw.getItems());
			findItemInTable(b);
			
		
	}

	protected void showTextEditor() {
		internalFrame.showTextEditor();
		
	}
	
	protected void showEmptyTextEditor() {
		internalFrame.showEmptyTextEditor();
		
	}

	protected void addImage() {
		sw.setCurrentSpline(null);
		sw.setCurrentBrush(null);
		//put sw behind to show the new dialog 
		internalFrame.setShapedWindowBehaind();
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "image files", "jpg", "png", "bmp", "gif");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this image: " +
	            
	       chooser.getSelectedFile().getAbsolutePath());
	    }else{
	    	internalFrame.setShapedWindowOnTop();
	    	return;
	    }
	    
	    //bring back sw to the top
	    internalFrame.setShapedWindowOnTop();
	    
	    
	    //do the saving

	    String path = chooser.getSelectedFile().getAbsolutePath();	
	    
    	Image image = new Image(path, sw, Item.IMAGE);
    	image.setTitle(chooser.getSelectedFile().getName());
    	//items.add(image);
		sw.addItem(image);
		internalFrame.createCustomWindow();
		//updateTable();

		
	}

	protected void applyItemChanges(JLabel label, boolean showCurve, boolean autorotation) {
		int selected = table.getSelectedRow();
		
		//update JLabel
		if (layers.get(selected).getType() == Item.IMAGE)
			return;
		//System.out.println("autorotation "+autorotation);
		Text currentItemText= (Text) layers.get(selected);
		currentItemText.setLabel(label);
		currentItemText.setShowCurve(showCurve);
		currentItemText.setAutorotation(autorotation);
		//currentItemText.resetSize();
		
			currentItemText.setTitle(currentItemText.getText());
		System.out.println(currentItemText.getTitle());
		currentItemText = null;
		
		updateTable(sw.getItems());
		table.setRowSelectionInterval(selected,selected);

//		textEditor.setText("");
		
	}
	public void applyImageChanges(boolean showShadow, boolean autorotation, int shadowDistance, int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent, boolean autoResize) {
		int selected = table.getSelectedRow();
		
		//update JLabel
//		if (layers.get(selected).getType() == Item.TEXT)
//			return;
//		
//		Image currentItemImage= (Image) layers.get(selected);
		Item currentItem = layers.get(selected);
		currentItem.setShowShadow(showShadow);
		currentItem.setAutorotation(autorotation);
		currentItem.setShadowDistance(shadowDistance);
		currentItem.setShadowRotation(shadowRotation);
		currentItem.setShadowSize(shadowSize);
		currentItem.setShadowColor(shadowColor);
		currentItem.setShadowTransparent(shadowTransparent);
		currentItem.setAutoResize(autoResize);
		//very important to update
		if (currentItem.type == Item.IMAGE){
			Image ii = (Image) currentItem;
			ii.update();
		}
			
		currentItem = null;
		
		updateTable(sw.getItems());
		table.setRowSelectionInterval(selected,selected);
		
	}

	public void applySplineChanges(Color splineColor, int curveWidth, boolean showShadow, boolean autorotation, int shadowDistance, int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent) {
		int selected = table.getSelectedRow();
		
		//update JLabel
//		if (layers.get(selected).getType() == Item.TEXT)
//			return;
//		
//		Image currentItemImage= (Image) layers.get(selected);
		
		Item currentItem = layers.get(selected);
		
		if (currentItem.getType() != Item.SPLINE){
			System.out.println("item is not spline");
			return;
		}
			
		Spline cSpline = (Spline) currentItem;
		cSpline.setColor(splineColor);
		cSpline.setShowShadow(showShadow);
		cSpline.setAutorotation(autorotation);
		cSpline.setShadowDistance(shadowDistance);
		cSpline.setShadowRotation(shadowRotation);
		cSpline.setShadowSize(shadowSize);
		cSpline.setShadowColor(shadowColor);
		cSpline.setShadowTransparent(shadowTransparent);
		cSpline.setCurveWidth(curveWidth);
		cSpline = null;
		
		updateTable(sw.getItems());
		table.setRowSelectionInterval(selected,selected);
		
	}

	public void applyBrushChanges(Color brushColor, int curveWidth, boolean showShadow, boolean autorotation, int shadowDistance, int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent) {
		int selected = table.getSelectedRow();
		
		//update JLabel
//		if (layers.get(selected).getType() == Item.TEXT)
//			return;
//		
//		Image currentItemImage= (Image) layers.get(selected);
		
		Item currentItem = layers.get(selected);
		
		if (currentItem.getType() != Item.BRUSH){
			System.out.println("item is not brush");
			return;
		}
		System.out.println("apply brush changes");
		Brush cBrush = (Brush) currentItem;
		cBrush.setColor(brushColor);
		cBrush.setShowShadow(showShadow);
		cBrush.setAutorotation(autorotation);
		cBrush.setShadowDistance(shadowDistance);
		cBrush.setShadowRotation(shadowRotation);
		cBrush.setShadowSize(shadowSize);
		cBrush.setShadowColor(shadowColor);
		cBrush.setShadowTransparent(shadowTransparent);
		cBrush.setCurveWidth(curveWidth);
		cBrush = null;
		
		updateTable(sw.getItems());
		table.setRowSelectionInterval(selected,selected);
		
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
	
	//find item in the table and make it selected
	public void findItemInTable(Item i){
		int counter = 0;
		for (int key: layers.keySet()) {
			if (i == layers.get(key)){
				table.setRowSelectionInterval(counter,counter);
				return;
			}
			counter++;
		}
	}

	public void unselectedIntTable() {
		table.clearSelection();
		
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
		sw.setCurrentSpline(null);
		sw.setCurrentBrush(null);
		showEmptyTextEditor();
		
	}


	public void deleteCurrentItem() {

		int [] selected = table.getSelectedRows();
		if (selected.length == 0){
			return;
		}
		for (int s:selected){
			System.out.println("delete "+s);
			Item i = layers.get(s);
			//remove Item
			
			sw.removeItem(i);
			layers.remove(s);
			
			//fix current splines and brushes bugs
			sw.setCurrentBrush(null);
			sw.setCurrentSpline(null);
		}
		
		internalFrame.createCustomWindow();
		//sw.reload();
		//updateTable();
	}


	public void updateTable() {
		updateTable(items);
		
	}


	public Spline getSelectedSpline() {
		int [] selected = table.getSelectedRows();
		if (selected.length == 0){
			return null;
		}
		for (int s:selected){
			System.out.println("delete "+s);
			Item i = layers.get(s);
			if (i.getType() == Item.SPLINE){
				return (Spline) i;
			}
		}
		return null;
	}



}
