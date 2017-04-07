/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;
 
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.font.FontRenderContext;


import static java.awt.GraphicsDevice.WindowTranslucency.*;
 
public class ShapedWindow extends JFrame implements MouseListener, MouseMotionListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 45612378941231241L;
	
	private static int width = 500;
	private static int height = 500;
	private JLabel label;
	private boolean drag = false;
	private Point mPosition;
	private Item draggedItem = null;
	private List <Item> items;;
	private MainWindow mw;
	private int gridFactor = 150;
	
	private boolean showTl = false;
	
	private JFrame sw = null;
	
	private static GeneralPath path = null;
	
	private String text = "";
	private int windowShape;
	private double scale = 1;
	private int translateX = 0;
	private int translateY = 0;
	private Point meLast;
	private boolean showGrid;
	private Item selectedItem = null;
	private Color color;
	
	private List<Shape> grid= new ArrayList<Shape>();
	private List<Layout> layoutSections = new ArrayList<Layout>();
	private JTextField editingTextBox;
	
	private Spline currentSpline = null;
	private Brush currentBrush = null;
	private boolean showPopUpMenu = false;
	
	private int x;
	private int y;

	private MouseEvent e;


	
//	@Override
//	public void repaint() {
//		super.repaint();
//		tl.repaint();
//	}
//	
//	@Override
//	public void repaint(){
//		super.repaint();
//		for (int i = items.size()-1; i>=0; i--){
//			items.get(i).repaint(getGraphics());	
//		}
//		
//	}
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		//if (showTl)
		//tl.paint(g);
		//generateGrid();
				
				
		 BufferedImage combined = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_INT_ARGB); //Image array to hold image chunks
         Graphics cg = combined.createGraphics();
         super.paint(cg);
         if (showGrid)
				drawGrid(cg);	
         
        
		for (int i = items.size()-1;i>=0;i--){
			items.get(i).setTranslateX(translateX);
			items.get(i).setTranslateY(translateY);
			items.get(i).setScale(scale);
			if (!items.get(i).isHide())
			items.get(i).paint(cg);
			//this.add(items.get(i));	
		}
		
		g.drawImage(combined, 0, 0, null);
		
		 if (isShowPopUpMenu()){
        	 PopUpMenu menu = new PopUpMenu(mw);
		     menu.show(e.getComponent(), e.getX(), e.getY());        	// g.drawImage(combined, 0, 0, null);
		     
		    
		     
         }
		
		//combined = null;
		//cg = null;
    }
	
	public void updatePath (GeneralPath p,double w, double h){
		 
		 AffineTransform Tx = new AffineTransform();
		 if (w != 0 && h != 0){
			 Tx.scale(w/p.getBounds2D().getWidth(), h/p.getBounds2D().getHeight()); 
		 }
	     Tx.translate(p.getBounds2D().getMinX()*-1, p.getBounds2D().getMinY()*-1);    // S3: final translation
	     
	     //Tx.rotate(theta);                  // S2: rotate around anchor
	     //Tx.translate(-anchorx, -anchory);  // S1: translate anchor to origin
		 
		 Shape fillRect = new Rectangle2D.Double(0, 0, p.getBounds2D().getWidth(), p.getBounds2D().getHeight());
	     //fillRect = inv.createTransformedShape(fillRect);
		 p = (GeneralPath) p.createTransformedShape(Tx);
		 p.closePath();
		 path = p;
		 

		 
		 width = (int) p.getBounds2D().getWidth();
		 height = (int) p.getBounds2D().getHeight();
	}
    public ShapedWindow(MainWindow mainWindow) {
        //super("ShapedWindow");
        mw = mainWindow;
        
        sw = this;
        sw.setAlwaysOnTop (true);
        //sw = new JFrame();
    	sw.addMouseListener(this);
     	sw.addMouseMotionListener(this); 
     	
     	//add popup listener
     //	addMouseListener(new PopClickListener(this));

     	
     	//get items list from mw
     	items = mw.getItems();
     	this.color = Color.darkGray;
     	
//     	this.editingTextBox = new JTextField(){
//     		@Override
//     		public void paint(Graphics g){
//     			
//     		}
//     	};
     	//editing textbox on screen
     	editingTextBox = new JTextField();
     	this.editingTextBox.setOpaque(false);
     	this.editingTextBox.setVisible(false);
     	this.editingTextBox.setForeground(getContrastColor(color));
     	sw.add(editingTextBox);
     	
//     	 sw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
//         sw.setExtendedState(JFrame.MAXIMIZED_BOTH);
//         sw.setUndecorated(true);
     	
     	//tl = new TextLineDemo();
    }
 
//    public static void main(String[] args) {
//    	 int[] xs = { 75, 150, 300, 375 , 400};
//    	 int[] ys = { 250, 100, 350, 250, 380};
//    	 List<Integer> px = new ArrayList<Integer>();
//    	 List<Integer> py = new ArrayList<Integer>();
//    	 for (int i = 0; i<xs.length; i++){
//    		 px.add(xs[i]);
//    		 py.add(ys[i]);
//    	 }
//    	  
//    	  path = new GeneralPath();
//    	    
//    	    path.moveTo(px.get(0), py.get(0));
//    	    
//    	    for (int i = 1; i+1<px.size();i+=2){
//    	    	path.curveTo(px.get(i), py.get(i), px.get(i), py.get(i), px.get(i+1), py.get(i+1));	
//    	    }
//
//    	    
//    	    
//        ShapedWindow test = new ShapedWindow(null);
//        test.updatePath(path, 500,500);
//    	test.run();
//    }
    
    public void closeWindow(){
    	sw.dispose();
    }
    
    public void updateText(String s){
    	text = s;
    }
    
	public void run() {
	
		addWindowStateListener(new WindowStateListener() {
     	   public void windowStateChanged(WindowEvent we) {
     	    // mw.setState(Frame.ICONIFIED);
     	     mw.setMainWindowState(we.getNewState());
     	   }
     	});
		
		// Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported = 
            gd.isWindowTranslucencySupported(TRANSLUCENT);
 
        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
            System.exit(0);
        }
 
        //If translucent windows aren't supported, 
        //create an opaque window.
        if (!isTranslucencySupported) {
            System.out.println(
                "Translucency is not supported, creating an opaque window");
        }
 
        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            

			@Override
            public void run() {
            	
            	

            	
            	sw.setLayout(new GridBagLayout());
            	
                 // It is best practice to set the window's shape in
                 // the componentResized method.  Then, if the window
                 // changes size, the shape will be correctly recalculated.
            		sw.addComponentListener(new ComponentAdapter() {
                        // Give the window an elliptical shape.
                        // If the window is resized, the shape is recalculated here.
                        @Override
                        public void componentResized(ComponentEvent e) {
                       	 if (windowShape == mw.PATH_SHAPE){
                             	setShape(path);
                       	 }
                       	 else if (windowShape == mw.CIRCLE_SHAPE){
                       		 setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight())); 
                       	 }else{
                       		 setShape(new Rectangle(0,0,getWidth(), getHeight()));
                       	 }

                        	
                        }
                    });
            	
            	
            		
            	sw.setUndecorated(true);
            	sw.setSize(width,height);
            	
            	sw.setLocationRelativeTo(null);
                 //setBounds(100, 100, WIDTH, HEIGHT);
            	sw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 
 
                // Set the window to 70% translucency, if supported.
                if (isTranslucencySupported) {
                    sw.setOpacity(1f);
                }
                
                //JPanel panel = new JPanel();
                JPanel panel = new JPanel() {
                	@Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        Color color1 = color;
                        Color color2 = color1.darker();
                        int w = getWidth()*2;
                        int h = getHeight()*2;
                        

                        GradientPaint gp = new GradientPaint(
                            0, 0, color1, 0, h, color2);
                        g2d.setPaint(gp);
                        g2d.fillRect(0, 0, w, h);
                    }

                };
                
                panel.setLayout(new FlowLayout());
                panel.setBounds(0, 0, sw.getWidth(), sw.getHeight());
                //panel.setBackground(Color.darkGray);
                panel.setBackground(color);

       
                
                //panel.add(labels);
                //sw.getContentPane().setBackground(Color.black);
                sw.getContentPane().add(panel);
                
                
                   sw.getContentPane().setLayout(null);

                
                
                
 
                // Display the window.
                sw.setVisible(true);
                mw.updateTable(items);
                mw.updateHeightText(getHeight());
            	mw.updateWidthText(getWidth());
            	
            	generateGrid();
            	int counter = 0;
            	for (Shape pp:grid){
            		//System.out.println(counter++);
        			distanceToPath(pp);
        		}

                
            }
        });
        
          
		
	}
	
	private void drawGrid(Graphics cg){
		Graphics2D cg2 = (Graphics2D) cg;
		cg2.setColor(Color.GRAY);

		for (Shape s:grid){
			cg2.draw(s);
	
		}
		cg2.setColor(Color.BLACK);
	}
	

	
	
	private void generateGrid(){
		
			int heightIterations = getHeight()/5;
			int widthIterations = getWidth()/5;
			int counter = 0;
			GeneralPath p = (GeneralPath) path.clone();
			
			if (windowShape == mw.CIRCLE_SHAPE){
				Shape circle = (new Ellipse2D.Double(0,0,getWidth(),getHeight()));
				p = new GeneralPath();
          	    p.append(circle, false);
			}else if (windowShape == mw.SQUARE_SHAPE){
	          		Shape square = (new Rectangle(0,0,getWidth(),getHeight()));
	          		p = new GeneralPath();
	          	    p.append(square, false);
			}
			AffineTransform Tx = new AffineTransform();
			Tx.scale((p.getBounds2D().getWidth()-gridFactor)/p.getBounds2D().getWidth(), (p.getBounds2D().getHeight()-gridFactor)/p.getBounds2D().getHeight());
			Tx.translate(gridFactor/(2*(p.getBounds2D().getWidth()-gridFactor)/p.getBounds2D().getWidth()),gridFactor/(2*(p.getBounds2D().getHeight()-gridFactor)/p.getBounds2D().getHeight()));
			 
			// System.out.println(heightIterations+" "+widthIterations);

			 grid = new ArrayList<Shape>();

			while (widthIterations>1 && heightIterations>1 && counter<10){

				widthIterations--;
				heightIterations--;
				counter++;
				 p = (GeneralPath) p.createTransformedShape(Tx);
				 double cX = p.getBounds2D().getCenterX();
				 double cY = p.getBounds2D().getCenterY();
				
				 
				 grid.add(p);
			
		}
	}
	
	
	@Override
	public void mouseDragged(MouseEvent me) {
		//scroll
		
		for (int i = 0; i<items.size();i++){
	    	items.get(i).mouseDragged(me);
	    }
		 if (me.isShiftDown() && getSelectedItem()==null){
		    	
		    	x += (int)(me.getX() - meLast.getX());
		    	y += (int)(me.getY() - meLast.getY());
		    	sw.setBounds(x, y, width, height);
		  		System.out.println(meLast +" " +me.getPoint());

		  		//meLast.setLocation(me.getPoint().getX(), me.getPoint().getY());
		    }
		
//		for (Item i:items){
//
//			i.mouseDragged(me);
//			
//		}
		
//		if (!drag)
//		    if (me.isControlDown()){
//		    	Point newPosition = new Point();
//		  		newPosition.setLocation(me.getX() - meLast.getX() ,me.getY() - meLast.getY());
//		  		meLast.setLocation(me.getPoint());
//		    	translateX += newPosition.getX();// - this.lastPosition.getX();
//		    	translateY += newPosition.getY();// - this.lastPosition.getY();
//		    }else if (me.isShiftDown()){
//		    	Point newPosition = new Point();
//		  		newPosition.setLocation(me.getX() - meLast.getX() ,me.getY() - meLast.getY());
//		  		meLast.setLocation(me.getPoint());
//		    	scale += 0.01*(newPosition.getX() + newPosition.getY());// - this.lastPosition.getX();
//		    }
		
		repaint();
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("x "+e.getY()+" y "+e.getY());
		for (int i = 0; i<items.size();i++){
	    	items.get(i).mouseMoved(e);
	    }
		
		mw.updateMouseBarText("x: "+e.getX() + " y: " + e.getY());
		
		
		
		//check if a spline is beign drawin
		if (currentBrush != null){
			Toolkit toolkit = Toolkit.getDefaultToolkit();

			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("Resources/brush_cursor.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Point hotSpot = new Point(0,0);
			Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Sword");
			this.setCursor(cursor);
			
			 //this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
//		else if (getSelectedItem() != null){
//			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
//		}
		else{
			
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//check if there is a spline to be drawn
//		if (currentSpline != null){
//			return;
//		}
		
		mw.mainToFront();
		
		
		
		 if (SwingUtilities.isLeftMouseButton(e)){
			 //System.out.println("left click");
			 //setSelectedItem(null);
			// setCurrentSpline(null);
		     //setCurrentBrush(null);
			 for (int i = 0; i<items.size();i++){
			    	items.get(i).mouseClicked(e);
			    }
			 
		 }
		
		//Check if textEditor was enabled and edited
		
		//setSelectedItem(null);
		
		
//		//check if there is no selected item
//		boolean itemSelected = false;
//		for (int i = 0; i<items.size();i++){
//	    	if (items.get(i).isSelected()){
//	    		itemSelected = true;
//	    		break;
//	    	}
//	    }
//		if (itemSelected == false){
//			this.setSelectedItem(null);
//		}
		
		//check if there is any item enabling editing box
		boolean enableTextBox = false;
		for (int i = 0; i<items.size();i++){
			if (getSelectedItem()==items.get(i)){
				if (items.get(i).isSelectedStage2()){
					enableTextBox = true;
					break;
				}
			}
	    }
		if (enableTextBox == false){
			hideEditingTextBox();
		}
		
//		for (Item i:items){
//			i.mouseClicked(e);
//		}
		
		//find selected item in table
		if (getSelectedItem() != null){
			mw.findItemInTable(selectedItem);
		}else{
			mw.unselectedIntTable();
		}
		
		mw.setHelp("");
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		for (int i = 0; i<items.size();i++){
	    	items.get(i).mouseEntered(e);
	    }

		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		for (int i = 0; i<items.size();i++){
	    	items.get(i).mouseExited(e);
	    }

		
	}
	

	 
	@Override
	public void mousePressed(MouseEvent e) {
		
//		 if (SwingUtilities.isRightMouseButton(e)){
//			 System.out.println("right click");
//			 return;
//		 }
		 
		for (int i = 0; i<items.size();i++){
			if (getSelectedItem()==items.get(i)){
		    	items.get(i).mousePressed(e);
			}
	    }
		
//		for (Item i:items){
//			if (getSelectedItem()==i){
//				i.mousePressed(e);
//			}
//		}
		
		meLast = new Point();
		meLast.setLocation(e.getPoint().getX(), e.getPoint().getY());
			
		//repaint();
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (isShowPopUpMenu()){
			setShowPopUpMenu(false);
		}
		
		 if (e.isPopupTrigger()){
			 setShowPopUpMenu(true);
			 this.e = e;
			 return;
	        }else{
	        	setShowPopUpMenu(false);
	        }
		 
	
		for (int i = 0; i<items.size();i++){
			if (getSelectedItem()==items.get(i)){
				items.get(i).mouseReleased(e);
			}
	    }
		
	
		 
		
		
//		for (Item i:items){
//			if (getSelectedItem()==i){
//				i.mouseReleased(e);
//			}
//			
//		}
		drag = false;	
		repaint();
		
		//update info window
		mw.updateInfoWindow();
	}
	public void addItem (Item i){
		items.add(0,i);
		//sw.add(i);

		//Item it = items.get(i);
//		JFXPanel fxPanel = new JFXPanel ();
//		this.add(fxPanel);
//		Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Test.initFX(fxPanel);
//            }
//       });
	}
	public List <Item> getItems(){
		return items;
	}
	public void reload (){
//		this.setVisible(false); //this will close frame i.e. NewJFrame
//
//		new JFrame().setVisible(true); // Now this will open NewJFrame for you again and will also get refreshed
//		for (Item i:items){
//        	sw.getContentPane().add(i.getLabel());
//        }
		sw.setVisible(false);
		SwingUtilities.updateComponentTreeUI(sw);
		sw.invalidate();
		sw.validate();
		sw.repaint();
		sw.setVisible(true);
		//System.out.println("number of items "+items.size());

	}
	public Point getCenter(){
		Point p = new Point();
		p.setLocation(path.getBounds2D().getCenterX(), path.getBounds2D().getCenterY());
		//System.out.println("center "+p);
		return p;
	}
	public Point getCorner(){
		Point p = new Point();
		p.setLocation(this.getX(), this.getY());
		return p;
	}

	public void removeItem(Item i){
		if (i.type == Item.TEXT){
			Text txt = (Text) i;
			sw.getContentPane().remove(txt.getLabel());
			items.remove(i);
		}
		items.remove(i);
	}
	public FontRenderContext getFrc() {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) this.getGraphics();
	    return g2.getFontRenderContext();
	     
	}
	
	public boolean isDrag(){
		return drag;
	}
	public void setDrag(boolean d){
		drag = d;
	}
	public Item getDraggedItem() {
		return draggedItem;
	}
	public void setDraggedItem(Item draggedItem) {
		this.draggedItem = draggedItem;
	}
	public void updateShapeType(int windowShape) {
		this.windowShape = windowShape;
		
	}
	public int getGridFactor() {
		return gridFactor;
	}
	public void setGridFactor(int gridFactor) {
		this.gridFactor = gridFactor;
	}
	public void drawPoint(Point p){
//		Graphics2D g2 = (Graphics2D) this.getGraphics();
//	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//	    RenderingHints.VALUE_ANTIALIAS_ON);
//	    g2.setColor(Color.red);
//	    g2.fillOval(p.x, p.y, 5, 5);
//	    g2.setColor(Color.black);
//	    try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	}
	public void drawPoint(Path2D.Double p){
//		Graphics2D g2 = (Graphics2D) this.getGraphics();
//	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//	    RenderingHints.VALUE_ANTIALIAS_ON);
//	    g2.setColor(Color.red);
//	    g2.draw((Shape) p);
//	   // g2.fillOval(p.x, p.y, 5, 5);
//	    g2.setColor(Color.black);
//	    try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	}
	
	
	public void drawQuad(Point[] q){
//		Graphics2D g2 = (Graphics2D) this.getGraphics();
//	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//	    RenderingHints.VALUE_ANTIALIAS_ON);
//	    GeneralPath p = new GeneralPath();
//	    p.moveTo(q[0].x, q[0].y);
//	    p.lineTo(q[1].x, q[1].y);
//	    p.lineTo(q[2].x, q[2].y);
//	    p.lineTo(q[3].x, q[3].y);
//	    p.lineTo(q[0].x, q[0].y);
//	    
//	    
//	    g2.setColor(Color.yellow);
//	    g2.draw(p);
//	   // g2.fillOval(p.x, p.y, 5, 5);
//	    g2.setColor(Color.black);
//	    try {	
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	}
	Point fix_point(float cx,float cy,Point e)
	{
		Point p = new Point();

		p.x /= this.scale;
		p.y /= this.scale;
		p.x -= this.translateX;
		p.y -= this.translateY;

	  return p;
	}
	public void setShowGrid(boolean showGrid){
		this.showGrid = showGrid;
	}
	public boolean isShowGrid(){
		return this.showGrid;
	}
	public void setSelectedItem(Item item) {
		this.selectedItem  = item;
		
	}
	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		return this.selectedItem;
	}
	public double distanceToPath(Shape s){
		//AffineTransform at = new AffineTransform();
		//path.getPathIterator(at);
		double flattness = 1.0;
		if (windowShape == mw.PATH_SHAPE){
			flattness = 0.1;
			
			
		}
		
        PathIterator it = new FlatteningPathIterator( s.getPathIterator( null ), flattness );
        float points[] = new float[6];
        
        Point previousPoint = new Point();
        Point p = new Point();
        it.currentSegment( points );
        previousPoint.setLocation(points[0], points[1]);
        it.next();
        //drawPoint(previousPoint);
        int counter = 0;
        
        while (!it.isDone()){
        	int type = it.currentSegment( points );
        	
        	p.setLocation(points[0], points[1]);
        	//drawPoint(p);
        	counter++;
        	
        	double rotation = GetAngleOfLineBetweenTwoPoints(previousPoint, p);
        	       	

        	Layout l = new Layout();
        	Point c = new Point();
        	c.setLocation((previousPoint.x+p.x)/2, (previousPoint.y+p.y)/2);
        	l.setCenter(c);
        	l.setRotation(rotation);
        	layoutSections.add(l);
        	previousPoint.setLocation(p);
        	it.next();
        }

		return 1.0;
	}
	
	 public static double GetAngleOfLineBetweenTwoPoints(Point p1, Point p2)
	    {
	        double xDiff = p2.x - p1.x;
	        double yDiff = p2.y - p1.y;
	        return Math.toDegrees(Math.atan2(yDiff, xDiff));
	    }
	 
	 public int getAutomaticRotation(Point p, Item item) {
			
			
			double closest = -1;
			int closestIndex = 0;
			int counter = 0;
			
			int totalRotation = 0;
			int totalpointsInside = 0;
			
			for (Layout l:layoutSections){
				
				double distance = Math.sqrt( (p.x-l.getCenter().x)*(p.x-l.getCenter().x) + (p.y-l.getCenter().y)*(p.y-l.getCenter().y)  );
				
				if (closest == -1)
					closest = distance;
				
				if (distance< closest){
					//System.out.println("closest  " + closest + " index " + closestIndex + " rotation "+l.getRotation());
					closest = distance;
					closestIndex = counter;
					
					
				}
				
//				Rectangle r = null;
				if (Math.abs(item.getWidth()) < 100){
					if (item.contains(l.getCenter(), 1.5)){
						totalRotation += l.getRotation();
						totalpointsInside ++;
					}
				}else{
					if (item.contains(l.getCenter())){
						totalRotation += l.getRotation();
						totalpointsInside ++;
					}
				}

//				if (item.contains(l.getCenter())){
//					totalRotation += l.getRotation();
//					totalpointsInside ++;
//				}
				
				counter++;
			}
			
			if (totalpointsInside>0){
				//System.out.println("total points");
				totalRotation /= totalpointsInside;
				return (int) totalRotation;
			}
			
			drawPoint(layoutSections.get(18).getCenter());
			return (int) layoutSections.get(closestIndex).getRotation();
		}
	public void setItems(List<Item> items) {
		this.items = items;
		
	}
	public void setBackgroundColor(Color color) {
		this.color = color;
		//System.out.println(this.color);
		
	}
	
	public void updateAndShowEditingTextBox(int x, int y, int width, int height, Font font2){
		editingTextBox.setBounds(x, y, width, height);
		editingTextBox.setFont(font2);
		editingTextBox.setVisible(true);
	}
	
	public void hideEditingTextBox(){
		editingTextBox.setVisible(false);
	}

	public String getEditingTextBoxTexT() {
		// TODO Auto-generated method stub
		return editingTextBox.getText();
	}
	
	public void setEditingTextBoxText(String text){
		editingTextBox.setText(text);
	}
	
	public static Color getContrastColor(Color color) {
		  double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		  return y >= 128 ? Color.black : Color.white;
		}

	public Spline getCurrentSpline() {
		return currentSpline;
	}

	public void setCurrentSpline(Spline currentSpline) {
		this.currentSpline = currentSpline;
	}


	public boolean isShowPopUpMenu() {
		return showPopUpMenu;
	}

	public void setShowPopUpMenu(boolean showPopUpMenu) {
		this.showPopUpMenu = showPopUpMenu;
	}

	public Brush getCurrentBrush() {
		return currentBrush;
	}

	public void setCurrentBrush(Brush currentBrush) {
		this.currentBrush = currentBrush;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	public void setCenter(Point center) {
		x = (int)(center.getX() - width/2);
		y = (int)(center.getY() - height/2);
		
	}
	 
//	public GeneralPath getWindowShape(){
//		getShape()
//		setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight())); 
//  	 }else{
//   		 setShape(new Rectangle(0,0,getWidth(), getHeight()));
//		if (windowShape == mw.PATH_SHAPE){
//			return path;
//		}else if (windowShape == mw.CIRCLE_SHAPE){
//			
//		}else{
//			
//		}
//		return path;
//	}
}
