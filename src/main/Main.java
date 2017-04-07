/*
 * Screen designer, main file
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;
 
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.awt.*;
 

public class Main extends JFrame
                               implements ActionListener, Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel mouseBar;
	private JLabel scaleBarWidth;
	private JLabel scaleBarHeight;
	private JLabel help;
	private JLabel help2;
	private long startTime = System.currentTimeMillis();
	// ... do something ...
	
	JDesktopPane desktop;
    MainWindow mw;
    ColorEditor ce;
    TextEditor tx;
    Curve curve;
    ImageEditor im;
    SplineEditor sp;
    BrushEditor br;

    MainTools mt;
    LayersEditor le;
    itemEditor ie;
    TextEditorForSpline txs;
    
    Dimension screenSize;
	private boolean showHelp = true;
    
 
    public Main() {
        //super("Main");
 
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0,
                screenSize.width,
                screenSize.height);
        

//        setBounds(inset, inset,
//                  screenSize.width  - inset*2,
//                  screenSize.height - inset*2);
 
        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        mouseBar = new JLabel("");
        mouseBar.setFont(new Font("Arial", Font.ITALIC, 20));
//      mouseBar.setBounds(mw.getShapedWindow().getX()+mw.getShapedWindow().getWidth()/2,mw.getShapedWindow().getY()+mw.getShapedWindow().getHeight()/2,50,50);
		desktop.add(mouseBar);
		
		scaleBarWidth = new JLabel("width :");
		scaleBarWidth.setFont(new Font("Arial", Font.ITALIC, 20));
		desktop.add(scaleBarWidth);
		
		scaleBarHeight= new JLabel("height :");
		scaleBarHeight.setFont(new Font("Arial", Font.ITALIC, 20));
		desktop.add(scaleBarHeight);
		 
        help= new JLabel();
        help.setFont(new Font("Arial", Font.ITALIC, 20));
		desktop.add(help);
		help.setBounds(500,50,100,50);
		
		help2= new JLabel("to turn off help, click on help menue then show/hide help");
		help2.setBounds((int) (screenSize.getWidth()/2-200), 40, 400, 20);
		desktop.add(help2);
 
		
        setContentPane(desktop);
        setJMenuBar(createMenuBar());
       
        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        
        createFrame(); //create first "window"

		
        
        addWindowStateListener(new WindowStateListener() {
        	   public void windowStateChanged(WindowEvent we) {
        	    // mw.setState(Frame.ICONIFIED);
        	     mw.setStateShapedWindow(we.getNewState());
        	   }
        	});
        
        
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time to run "+estimatedTime);

    }
 
    protected JMenuBar createMenuBar() {
    	
        JMenuBar menuBar = new JMenuBar();
 
        //Set up the lone menu.
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);
        
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_D);
        menuBar.add(edit);
        
        JMenu tools = new JMenu("tools");
        tools.setMnemonic(KeyEvent.VK_D);
        menuBar.add(tools);
        
        JMenu helpMenu = new JMenu("help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);
 
 
        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("save");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("SaveAs");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("saveAs");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Open");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("open");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
 
        //Set up the second menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);
       
        
        menuItem = new JMenuItem("delete");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("delete");
        menuItem.addActionListener(this);
        edit.add(menuItem);
        
        menuItem = new JMenuItem("Add text");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_T, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("Add text");
        menuItem.addActionListener(this);
        tools.add(menuItem);
        
        menuItem = new JMenuItem("Add brush");
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_B, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("Add brush");
        menuItem.addActionListener(this);
        tools.add(menuItem);
        
        menuItem = new JMenuItem("Add spline");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("Add spline");
        menuItem.addActionListener(this);
        tools.add(menuItem);
        
        menuItem = new JMenuItem("Add image");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("Add image");
        menuItem.addActionListener(this);
        tools.add(menuItem);
        
        menuItem = new JMenuItem("show/hide help");
        menuItem.setActionCommand("show/hide help");
        menuItem.addActionListener(this);
        helpMenu.add(menuItem);
 
        return menuBar;
    }
 
    //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) { //new
        	desktop.remove(mw);
        	desktop.remove(tx);
        	desktop.remove(ce);
        	desktop.remove(curve);
        	
        	mw.removeShapedWindow();
        	mw =  null;
        	tx = null;
        	ce = null;
        	curve = null;
            createFrame();
        } 
        else if ("save".equals(e.getActionCommand())){
        	mw.setItems(mw.getShapedWindow().getItems());
        	mw.removeShapedWindow();
        	JFileChooser chooser = new JFileChooser();
    	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
    	        "txt files", "txt");
    	    chooser.setFileFilter(filter);
    	    int returnVal = chooser.showSaveDialog(null);
    	    if(returnVal == JFileChooser.APPROVE_OPTION) {
    	       System.out.println("You chose to save this file: " +
    	            
    	       chooser.getSelectedFile().getAbsolutePath());
    	    }
    	    
    	    //do the saving
    	    String path = chooser.getSelectedFile().getAbsolutePath();		
        	FileHandler fh = new FileHandler();
        	fh.saveSession(path, this);
        }
        else if ("open".equals(e.getActionCommand())) {
        	JFileChooser chooser = new JFileChooser();
    	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
    	        "txt files", "txt");
    	    chooser.setFileFilter(filter);
    	    int returnVal = chooser.showOpenDialog(null);
    	    if(returnVal == JFileChooser.APPROVE_OPTION) {
    	       System.out.println("You chose to save this file: " +
    	            
    	       chooser.getSelectedFile().getAbsolutePath());
    	    }
    	    
    	    //do the saving
    	    String path = chooser.getSelectedFile().getAbsolutePath();		
        	FileHandler fh = new FileHandler();
        	try {
				Main temp = fh.loadSession(path);
				mw = temp.getMainWindow();
				tx = temp.getTextEditor();
				curve = temp.getCurve();
				ce = temp.getColorEditor();
				mw.setInternalFrame(this);
				ce.setInternalFrame(this);
				mw.insertSwInItems();
				
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        }
        else if ("saveAs".equals(e.getActionCommand())) {
        	mw.saveAsImage();
        }
        else if ("Add text".equals(e.getActionCommand())) {
        	addText();
        }
        else if ("Add spline".equals(e.getActionCommand())) {
        	addSpline();
        }
        else if ("Add brush".equals(e.getActionCommand())) {
        	addBrush();
        }
        else if ("Add image".equals(e.getActionCommand())) {
        	addImage();
        }
        
        else if ("delete".equals(e.getActionCommand())) {
        	deleteItem();
        } else if ("show/hide help".equals(e.getActionCommand())) {
        	if (showHelp){
        		showHelp = false;
        		help.setVisible(false);
        		help2.setVisible(false);
        	}else{
        		showHelp = true;
        		help.setVisible(true);
        		help.setText("");
        		help2.setVisible(true);
        	}
        }
        else { //quit
            quit();
        }
    }
 
    private void deleteItem() {
		le.deleteCurrentItem();
		ie.setSelectedItem(null);
		
	}


	//Create a new internal frame.
    protected void createFrame() {
    	
//        MyInternalFrame frame = new MyInternalFrame();
//        frame.setVisible(true); //necessary as of 1.3
//        desktop.add(frame);
    	
//    	JFrame test = new JFrame();
//    	test.setSize(500, 500);
//    	test.setVisible(true);
        
    	//
    	//paintScale();
        mw = new MainWindow(this);
        mw.setVisible(true);
        mw.setBounds(this.getWidth()-mw.getWidth()-20,0 , mw.getWidth(), mw.getHeight());
        desktop.add(mw);
        
        le = new LayersEditor(this);
        le.setVisible(true);
        le.setBounds(this.getWidth()-le.getWidth()-20,mw.getHeight() , le.getWidth(), le.getHeight());
        desktop.add(le);
        
        ie = new itemEditor(this);
        ie.setVisible(true);
        ie.setBounds(this.getWidth()-ie.getWidth()-20,mw.getHeight()+le.getHeight() , ie.getWidth(), ie.getHeight());
        desktop.add(ie);
        
        
        
        
		
		ce = new ColorEditor(this);
		//ce.setVisible(true);

		//ce.setBounds(0,tx.getY()+tx.getHeight()+20, ce.getWidth(), ce.getHeight());
	    ce.run();
	    desktop.add(ce);
	    
	    
	    
	    tx = new TextEditor(this);
		tx.run();
		//tx.setVisible(true);
		tx.setBounds(this.getWidth()-tx.getWidth()-20,mw.getHeight()+20 , tx.getWidth(), tx.getHeight());
		desktop.add(tx);
		
		txs = new TextEditorForSpline(this);
		txs.run();
		//tx.setVisible(true);
		txs.setBounds(this.getWidth()-txs.getWidth()-20,mw.getHeight()+20 , txs.getWidth(), txs.getHeight());
		desktop.add(txs);
		
		
		im = new ImageEditor(this);
		im.run();
		im.setBounds(this.getWidth()-tx.getWidth()-20,mw.getHeight()+25 , tx.getWidth(), tx.getHeight());
		desktop.add(im);
	        
		
		sp = new SplineEditor(this);
		sp.run();
		sp.setBounds(this.getWidth()-tx.getWidth()-20,mw.getHeight()+25 , sp.getWidth(), sp.getHeight());
		desktop.add(sp);
		
		br = new BrushEditor(this);
		br.run();
		br.setBounds(this.getWidth()-tx.getWidth()-20,mw.getHeight()+35 , br.getWidth(), br.getHeight());
		desktop.add(br);
		
		
		mt = new MainTools(this);
		mt.run();
		mt.setVisible(true);
		mt.setBounds(10,40 , mt.getWidth(), mt.getHeight());
		
		

		desktop.add(mt);
		
		curve = new Curve(mw); 
		curve.run();
		curve.setVisible(false);
		curve.setBounds(30,60 , curve.getWidth(), curve.getHeight());
		desktop.add(curve);
		
		mw.setTextEditor(tx);
		mw.setCurve(curve);
        
//        try {
//            frame.setSelected(true);
//        } catch (java.beans.PropertyVetoException e) {}
    }
 
    //Quit the application.
    protected void quit() {
        System.exit(0);
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
       // JFrame.setDefaultLookAndFeelDecorated(true);
 
        //Create and set up the window.
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Display the window.
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	public void addItem(JLabel label, boolean curve, boolean autorotation, int type) {
		mw.addItem(label, curve, autorotation, type);
		
	}

	public void showColorChooser(int request, int type) {
		ce.setRequest(request, type);
		ce.setVisible(true);
	}

	public void setTextColor(Color color) {
		tx.setTextColor(color);
		
	}
	
	public void setBackgroundColor(Color color) {
		mw.setBackgroundColor(color);
		
	}
	

	public void showTextEditor(JLabel label, boolean isCurve, boolean isAutorotation) {
		System.out.println("showTextEditor");
		tx.setText(label);
		tx.setShowCurve(isCurve);
		tx.setAutorotation(isAutorotation);
		tx.setVisible(true);
	}
	
	public void showTextEditor(JLabel label, boolean isCurve, boolean isAutorotation, Color color, int shadowDistance, int shadowAngle, int shadowTransparency, double shadowSize, Color shadowColor, boolean autoResize) {
		tx.setText(label);
		tx.setShowCurve(isCurve);
		tx.setAutorotation(isAutorotation);

		tx.setShadowColor(shadowColor);
		tx.setColor(color);
		tx.setShadowAngle(shadowAngle);
		tx.setShadowDistance(shadowDistance);
		tx.setShadowSize((int)shadowSize);
		tx.setShadowTransparency(shadowTransparency);
		tx.setAutoResize(autoResize);
		tx.setVisible(true);
			}
	
	
	public void showTextEditor() {

		tx.setVisible(true);
			}

	public void showImageEditor(boolean isAutorotation, int shadowDistance, int shadowAngle, int shadowTransparency, double shadowSize, Color shadowColor, boolean autoResize) {
		System.out.println("showImageEditor");
		im.setAutorotation(isAutorotation);
		
		im.setShadowColor(shadowColor);
		im.setShadowAngle(shadowAngle);
		im.setShadowDistance(shadowDistance);
		im.setShadowSize((int)shadowSize);
		im.setShadowTransparency(shadowTransparency);
		im.setAutoResize(autoResize);
		im.setVisible(true);
		
		if (showHelp)
		setHelp("Edit images, choose an image layer then click APPLY");

	}
	
	
	

	public void applyItemChanges(JLabel label, boolean showCurve, boolean autorotation) {
		le.applyItemChanges(label, showCurve, autorotation);
		
	}
	public MainWindow getMainWindow(){
		return mw;
	}
	public TextEditor getTextEditor(){
		return tx;
	}
	public Curve getCurve(){
		return curve;
	}
	public ColorEditor getColorEditor(){
		return ce;
	}

	public void applyImageChanges(boolean showShadow, boolean autorotation, int shadowDistance, int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent, boolean autoResize) {
		le.applyImageChanges(showShadow, autorotation, shadowDistance, shadowRotation, shadowSize, shadowColor, shadowTransparent, autoResize);
		
	}
	
	public void applySplineChanges(Color splineColor, int curveWidth, boolean showShadow, boolean autorotation, int shadowDistance, int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent) {
		le.applySplineChanges(splineColor, curveWidth, showShadow, autorotation, shadowDistance, shadowRotation, shadowSize, shadowColor, shadowTransparent);
		
	}
	

	public void setShadowColor(Color color, int type) {
		if (type == Item.IMAGE)
			im.setShadowColor(color);
		else if (type == Item.SPLINE)
			sp.setShadowColor(color);
	}
	
	public void setSplineColor(Color color, int type) {
		sp.setSplineColor(color);
	}

	public void showSplineEditor(boolean isAutorotation, Color splineColor, int curveWidth, int shadowDistance, int shadowAngle, int shadowTransparency, double shadowSize, Color shadowColor) {
		System.out.println("showSplineEditor");
		
		sp.setAutorotation(isAutorotation);
		sp.setShadowColor(shadowColor);
		sp.setSplineColor(splineColor);
		sp.setCurveWidth(curveWidth);
		sp.setShadowAngle(shadowAngle);
		sp.setShadowDistance(shadowDistance);
		sp.setShadowSize((int)shadowSize);
		sp.setShadowTransparency(shadowTransparency);
		sp.setVisible(true);
		
		
	}

	public void hideCurve() {
		curve.setVisible(false);
		
	}
	
	public void showCurve() {
		if (showHelp )
			setHelp("Make your changes on the screen using the screen window, add node by clicking, remove node by holding shift and clicking");
	
		curve.setVisible(true);
	}

	public void addImage() {
		if (showHelp )
			setHelp("Add an image from your computer, to edit, choose image layer and click on the edit button then click APPLY");
	
		le.addImage();
		}

	public void addText() {
		le.addText();
		if (showHelp )
			setHelp("Add texts using the text window. To edit, choose a text layer and use the text window to make the changes then click APPLY");
	}

	public void addSpline() {
		le.addSpline();
		if (showHelp )
		setHelp("Click on the screen to start drawing a curve, double click to exit");
	}

	public void showHideGrid() {
		mw.showHideGrid();
		if (showHelp )
			setHelp("Show and Hide the grid");
		
	}

	public void createCustomWindow() {
		mw.createCustomWindow();
		
	}

	public void updateTable(List<Item> items2) {
		le.updateTable(items2);
		
	}

	public void findItemInTable(Item selectedItem) {
		le.findItemInTable(selectedItem);
		
	}

	public void unselectedIntTable() {
		le.unselectedIntTable();
	}

	public void setShapedWindowBehaind() {
		mw.setShapedWindowBehind();
		
	}

	public void setShapedWindowOnTop() {
		mw.setShapedWindowOnTop();
		
	}

	public ShapedWindow getShapedWindow() {
		return mw.getShapedWindow();
	}

	public List<Item> getItems() {
		return mw.getItems();
	}

	public void showBrushEditor(boolean autorotation, Color color, int curveWidth, int shadowDistance,
			int shadowAngle, int shadowTransparency, double shadowSize, Color shadowColor) {

	System.out.println("showSplineEditor");
		
		br.setAutorotation(autorotation);
		br.setShadowColor(shadowColor);
		br.setBrushColor(color);
		br.setCurveWidth(curveWidth);
		br.setShadowAngle(shadowAngle);
		br.setShadowDistance(shadowDistance);
		br.setShadowSize((int)shadowSize);
		br.setShadowTransparency(shadowTransparency);
		br.setVisible(true);
		
		
		
	}

	public void applyBrushChanges(Color brushColor, int value, boolean showShadow, boolean autorotation, int shadowDistance,
			int shadowRotation, int shadowSize, Color shadowColor, int shadowTransparent) {

		le.applyBrushChanges(brushColor, value, showShadow, autorotation, shadowDistance, shadowRotation, shadowSize, shadowColor, shadowTransparent);
		
	}

	public void setBrushColor(Color color, int type) {
		br.setBrushColor(color);

	}
	
	



	public void updateScaleBar(int w, int h){
		int x = ((int) screenSize.getWidth() /2)-50;
		int y = (((int) screenSize.getHeight() + h) /2)-50;

		mouseBar.setBounds(x, y, 150, 50);
		scaleBarWidth.setBounds(x, y+50, 150, 50);
		scaleBarWidth.setText("width :"+w);
		
		y = ((int) screenSize.getHeight() /2)-50;
		x = (((int) screenSize.getWidth() -w) /2)-150;
		
		scaleBarHeight.setBounds(x, y, 150, 50);
		scaleBarHeight.setText("height :"+h);

	}
	


	public void updateMouseBarText(String text) {
		mouseBar.setText(text);
		
	}

	public void addBrush() {
		if (showHelp )
			setHelp("start painting by dragging on the screen, edit by choosing the brush layer then click on edit then APPLY");
	
		
		
		le.addBrush();
		
	}

	public void setSelectedItemInEditor(Item item) {
		ie.setSelectedItem(item);
		
	}

	public void updateInfoWindow() {
		ie.updateWindow();
		
	}

	public void updateTable() {
		le.updateTable();
		
	}
	
	public Point getCenter(){
		return new Point((int)(screenSize.getWidth()/2),(int)(screenSize.getHeight()/2));
	}

	public void showTextEditorForSpline(Spline spline) {
		txs.setSpline(spline);
		txs.setShowCurve(true);
		txs.setVisible(true);
		
	}

	public void addTextToSpline(JLabel label, boolean showCurve, boolean autorotation, int type, Spline spline) {
		mw.addTextToSpline(label, showCurve, autorotation, type, spline);
		
	}

	public Spline getSelectedSpline() {
		
		return le.getSelectedSpline();
	}

	
	public void setHelp(String text){
		
		help.setText(text);
		help.setBounds((int) (screenSize.getWidth()/2-(text.length()*5)), 20, text.length()*10, 20);
	
}
	
	public void setShowHelp(boolean i){
		help.setVisible(i);
		help2.setVisible(i);
		help.setText("");
	}

	public void showEmptyTextEditor() {
		System.out.println("showTextEditor");
		tx.setText(new JLabel());
		tx.setShowCurve(false);
		tx.setAutorotation(false);
		tx.setVisible(true);
		
	}

}
