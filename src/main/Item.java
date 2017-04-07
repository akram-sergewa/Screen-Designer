/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Jama.Matrix;
import javafx.embed.swing.JFXPanel;



public class Item  implements MouseListener, MouseMotionListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 12132111L;
	protected double width = 100;
	protected double height = 50;
	protected double widthFactor = 1;
	protected double heightFactor = 1;
	protected double x = 0;
	protected double y = 0;
	protected String title;
	protected int order;
	protected Rectangle r[];
	protected Rectangle rSelectedCorner;
	protected int rSelectedCornerIndex;
	protected Rectangle rDeform[];
	protected Point2D DeformPointsRelativeToCorners[];
	protected Rectangle rDeformSelectedCorner;
	protected int rDeformSelectedCornerIndex;
	protected Point mPosition;
	protected ShapedWindow sw = null;
	protected int  rotation = 0;
	protected Point lastPosition;
	protected int mSelectedPointIndex;
	protected double scale = 1;
	protected int translateY = 0;
	protected int translateX = 0;
	protected boolean selected = false;	
	private boolean hide = false;
	//private Color color;
	protected boolean autorotation;
	protected double cx;
	protected double cy;
	protected static int TEXT = 0;
	protected static int IMAGE = 1;
	protected static int SPLINE = 2;
	protected static int BRUSH = 3;
	protected int type;
	protected boolean showShadow;
	private int shadowRotation;
	private int shadowDistance;
	private double shadowSize;
	private Color shadowColor;
	private int shadowTransparent;
	private boolean selectedStage2;
	private Matrix a;
	private Matrix b;
	private boolean autoResize = false;
	
	Item (int orderCounter, ShapedWindow sw, int type){
		this.type = type;
		this.lastPosition = new Point(0,0);
		this.sw = sw;
		this.x = sw.getCenter().x;
		this.y = sw.getCenter().y;
		
		this.setShadowDistance(50);
		this.setShadowRotation(50);
		this.setShadowSize(50);
		this.setShadowTransparent(50);
		
//		this.onScreenTextBox = new JTextField();
//		this.onScreenTextBox.setOpaque(false);
		//this.color = label.getForeground();
		setTitle("text");
		setOrder(orderCounter);
		

		
		r = new Rectangle[4];
		rDeform = new Rectangle[4];
		for (int i = 0; i<4; i++){
			rDeform[i] = new Rectangle();
		}
		DeformPointsRelativeToCorners = new Point2D[4];
		for (int i = 0; i<4; i++){
			DeformPointsRelativeToCorners[i] = new Point2D.Double(0.0,0.0);
		}
		
		fixBoundaries();
		
	    

	}
	
	
	public boolean contains (Point mSelectedPoint2){

		double ww = width;
		double hh = height;
		double xx = x;
		double yy = y;
		Rectangle r = new Rectangle();
		
		//fix bounds if the size is in negative
		if (ww <0){
			ww *= -1;
			xx -= ww;
		}
		if (hh <0){
			hh *= -1;
			yy -= hh;
		}
		r.setBounds((int)xx, (int)yy, (int)ww, (int)hh);
		if (r.contains(mSelectedPoint2)){
			return true;
		}
		return false;
	}
	
	public boolean contains (Point mSelectedPoint2, double factor){

		double ww = width;
		double hh = height;
		double xx = x;
		double yy = y;
		Rectangle r = new Rectangle();
		
		//fix bounds if the size is in negative
		if (ww <0){
			ww *= -1;
			xx -= ww;
		}
		if (hh <0){
			hh *= -1;
			yy -= hh;
		}
		xx -= (ww*factor)/2;
		yy -= (hh*factor)/2;
		ww *= factor;
		hh *= factor;
		
		r.setBounds((int)xx, (int)yy, (int)ww, (int)hh);
		if (r.contains(mSelectedPoint2)){
			return true;
		}
		return false;
	}
	
	
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public void setLocation (Point p){

		Point change = new Point();
		change.setLocation(p.getX() - this.getX() ,p.getY() - this.getY());
		
		this.x = p.x;
		this.y = p.y;
		this.cx += change.getX();
		this.cy += change.getY();
		
		
		fixBoundaries();

	}
	
	public void fixCenter(){
	
Rectangle r = new Rectangle ((int)x,(int)y,(int)Math.abs(width),(int)Math.abs(height));
		
		if (width < 0){
			r.setLocation((int)(r.getBounds2D().getX()+width), (int)(r.getBounds2D().getY()));
		}
		if (height < 0){
			r.setLocation((int)(r.getBounds2D().getX()), (int)(r.getBounds2D().getY()+height));
	
		}
		
		//Rectangle r = new Rectangle ((int)x,(int)y,(int)width,(int)height);
		Path2D.Double path = new Path2D.Double();
	    path.append(r, false);
		AffineTransform t = new AffineTransform();
	    t.rotate(Math.toRadians(rotation), cx, cy);
	    path.transform(t);
	    sw.drawPoint(path);
	    
	    int cxAfterRotation = (int) path.getBounds2D().getCenterX();
	    int cyAfterRotation = (int) path.getBounds2D().getCenterY();
	    
	    t = new AffineTransform();
	    t.rotate(Math.toRadians(-rotation),cxAfterRotation ,cyAfterRotation);
	    path.transform(t);

	    
	   
	    
	    
		    if (width < 0){
		    	x = path.getBounds2D().getX()+path.getBounds2D().getWidth();
			    width = -path.getBounds2D().getWidth();
			}else{
				 x = path.getBounds2D().getX();
				 width = path.getBounds2D().getWidth();
			}
		    
			if (height < 0){
				y = path.getBounds2D().getY()+path.getBounds2D().getHeight();
			    height = -path.getBounds2D().getHeight();
		
			}else{
			    y = path.getBounds2D().getY();
			    height = path.getBounds2D().getHeight();
			}
		
	    cx = path.getBounds2D().getCenterX();
	    cy = path.getBounds2D().getCenterY();
	   	    


	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title.length()>10)
			this.title = title.substring(0, 10);
		else
			this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	

	
	public void paint(Graphics g){
       
		
//		if (!showCurve){
//			Graphics2D g2 = (Graphics2D) g;
//		     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		     //fix scaling
//		     
//		     //System.out.println(-sw.getX()*(scale-1));
//		     
//		     
//		     Font font = label.getFont();
//		     FontRenderContext frc = g2.getFontRenderContext();
//		     GlyphVector gv = font.createGlyphVector(frc, getText());
//		     
//		     Shape glyph =  gv.getOutline();
//		     //int fontWidth = (int)(font.getStringBounds(getText(), frc).getWidth());
//		    // int fontHeight = (int)(font.getStringBounds(getText(), frc).getHeight());
//		     AffineTransform at = AffineTransform.getTranslateInstance(x, y+height);
//		     //width = 200;
//		    //at.rotate(Math.toRadians(rotation), width/2,-height/2);
//
//		     at.scale(width/glyph.getBounds2D().getWidth(), height/glyph.getBounds2D().getHeight());
//		     Shape transformedGlyph = at.createTransformedShape(glyph);
//		     Color o = g2.getColor();
//		     g2.setColor(label.getForeground());
//		     
//		     
//		     g2.translate(this.translateX,this.translateY);
//		     g2.rotate(Math.toRadians(rotation),cx*scale,cy*scale);
//
//		     g2.scale(this.scale , this.scale);
//		     
//
//		     g2.fill(transformedGlyph);
//		     
//		     g2.scale(1/this.scale , 1/this.scale);
//		     g2.rotate(-Math.toRadians(rotation), cx*scale,cy*scale);
//		     g2.translate(-this.translateX,-this.translateY);
//		     g2.setColor(o);
//
//		    // g2.rotate(-Math.toRadians(45));
//		     //g2.translate(0,0);
//		     
//		}
//		else {
////			BezierTextPlotter btp = new BezierTextPlotter(mPoints, g);
////			
////			 Platform.runLater(new Runnable() { 
////		            @Override
////		            public void run() {
////		            	try {
////		    				btp.initFX(fxPanel);
////		    				
////		    			} catch (Exception e) {
////		    				// TODO Auto-generated catch block
////		    				e.printStackTrace();
////		    				System.out.println("NOPE");
////		    			}
////		            }
////		        });
//			 
//			 
//			 
//			
//			Graphics2D g2 = (Graphics2D)g;
//		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//		    RenderingHints.VALUE_ANTIALIAS_ON);
//		    g2.rotate(Math.toRadians(rotation), x + width/2, y + height/2);
//		     
//		    // Draw the tangents.
//		    Line2D tangent1 = new Line2D.Double(mPoints[0], mPoints[1]);
//		    Line2D tangent2 = new Line2D.Double(mPoints[2], mPoints[3]);
//		    g2.setPaint(Color.gray);
//		    g2.draw(tangent1);
//		    g2.draw(tangent2);
//		    // Draw the cubic curve.
//		    CubicCurve2D c = new CubicCurve2D.Float();
//		    
////		    Point2D[] tPoints = new Point2D[4];
////			// Cubic curve.
////		
////		    tPoints[0] = mPoints[2];
////		    tPoints[1] = mPoints[3];
////		    tPoints[2] = mPoints[0];
////		    tPoints[3] = mPoints[1];
//			 
//		    c.setCurve(mPoints, 0);
//		    g2.setPaint(Color.black);
//		    g2.draw(c);
//		    
//		    //get font scale factor
//		    FontRenderContext frc = g2.getFontRenderContext();
//		    double factorWidth = width/(label.getFont().getStringBounds(getText(), frc).getWidth());
//		    double factorHeight = height/(label.getFont().getStringBounds(getText(), frc).getHeight());
//		    
//
//		    
////		    int newFontSize = (int) (label.getFont().getSize()*Math.abs(factorWidth));
////		    if (newFontSize ==0)
////		    	newFontSize = 1;
//		    
//		    //Draw string along the cubic line
//		    label.setFont(new Font(label.getFont().getFontName(), label.getFont().getStyle(), label.getFont().getSize() ));
//		    
//		    g2.setStroke(new TextStroke(this.getText(), label.getFont(), factorWidth, factorHeight, (int)width, g));
//		    g2.draw(c);
//		    g2.setStroke(new BasicStroke());
//		 
//		    if (selected){
//		    for (int i = 0; i < mPoints.length; i++) {
//		      // If the point is selected, use the selected color.
//		      if (mPoints[i] == mSelectedPoint)
//		        g2.setPaint(Color.red);
//		      else
//		        g2.setPaint(Color.blue);
//		      // Draw the point.
//		      g2.fill(getControlPoint(mPoints[i]));
//		    }
//		    }
//		     g2.rotate(-Math.toRadians(rotation), x + width/2, y + height/2);
//		}
		
		 
//		if (selected){
//			Graphics2D g2 = (Graphics2D)g;
//		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//		    RenderingHints.VALUE_ANTIALIAS_ON);
//		    
//			g2.setPaint(Color.blue);
//			
//		     
//		     //fixBoundaries();
//		    
//			g2.translate(this.translateX,this.translateY);
//		     g2.rotate(Math.toRadians(rotation),cx*scale,cy*scale);
//
//		     g2.scale(this.scale , this.scale);
//
//			for (int i = 0; i<4;i++){
//				r[i].getCenterX();
//				g2.fillRoundRect((int)r[i].getX(), (int)r[i].getY(), (int)(r[i].width), (int)(r[i].height), 100, 100);
//				//g2.fillRect(r[i].x, r[i].y, r[i].width, r[i].height);
//			}
//			
//		     g2.scale(1/this.scale , 1/this.scale);
//		     g2.rotate(-Math.toRadians(rotation),cx*scale,cy*scale);
//		     g2.translate(-this.translateX,-this.translateY);
//			g2.setPaint(Color.black);
//		}
		

	}
	
	
	public void showTextField(){
		
	}

	public void drawCorners(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    
		g2.setPaint(Color.blue);
		
		for (int i = 0; i<4;i++){
			r[i].getCenterX();
			g2.fillRoundRect((int)r[i].getX(), (int)r[i].getY(), (int)(r[i].width), (int)(r[i].height), 100, 100);
			//g2.fillRect(r[i].x, r[i].y, r[i].width, r[i].height);
		}
		g2.setPaint(Color.black);
	}
	
	public void drawDeformCorners(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    
		g2.setPaint(Color.red);
		
		for (int i = 0; i<4;i++){
			//rDeform[i].getCenterX();
//	g2.fillRoundRect((int)r[i].getX()+(int)rDeform[i].getX(), (int)r[i].getY() + (int)rDeform[i].getY(), (int)(r[i].width), (int)(r[i].height), 100, 100);
			g2.fillRoundRect((int)rDeform[i].getX(), (int)rDeform[i].getY(), (int)(r[i].width), (int)(r[i].height), 100, 100);
			
			//g2.fillRect(r[i].x, r[i].y, r[i].width, r[i].height);
		}
		g2.setPaint(Color.black);
	}
	

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	protected Shape getControlPoint(Point2D p) {
	    // Create a small square around the given point.
	    int side = 4;
	    return new Rectangle2D.Double(
	        p.getX() - side / 2, p.getY() - side / 2,
	        side, side);
	  }

	@Override
	public void mouseDragged(MouseEvent me) {
		  if (!sw.isDrag())
			  return;
		  
		  Point mousePoint = fixMousePositionPrecpective(cx, cy, me.getPoint());
		 Point mousePointRotation = fixMouseRotationPrecpective(cx, cy, me.getPoint());
 
	    if (rSelectedCorner !=null){
	    	//System.out.println("check boxes");
	    	int xx,yy,ww,hh;
	    	//Point2D mousePoint = fixMousePositionPrecpective(x+(width/2), y+(height/2), me.getPoint());
			

	    	xx = (int) (mousePointRotation.getX()-rSelectedCorner.getX());
	    	yy = (int) (mousePointRotation.getY()-rSelectedCorner.getY());
	    	ww = (int) (mousePointRotation.getX()-rSelectedCorner.getX())*-1;
   		 	hh = (int) (mousePointRotation.getY()-rSelectedCorner.getY())*-1;
   		 	//System.out.println("xx yy ww hh "+xx+yy+ww+hh);
   		 	reSize(xx,yy,ww,hh);
   		 	return;
	    	
	    		
	    }
	    
	    if (rDeformSelectedCorner !=null){
	    	//System.out.println("check boxes");
	    	int xx,yy,ww,hh;
	    	//Point2D mousePoint = fixMousePositionPrecpective(x+(width/2), y+(height/2), me.getPoint());
			

	    	xx = (int) (mousePointRotation.getX()-rDeformSelectedCorner.getX());
	    	yy = (int) (mousePointRotation.getY()-rDeformSelectedCorner.getY());
	    	//ww = (int) (mousePointRotation.getX()-rDeformSelectedCorner.getX())*-1;
   		 	//hh = (int) (mousePointRotation.getY()-rDeformSelectedCorner.getY())*-1;
   		 	 
   		 	DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].setLocation(DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getX()+xx, DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getY()+yy);
   		 	
   		 	return;
	    	
	    		
	    }
	    
	    
	    if (sw.getDraggedItem()==this){
			Point newPosition = new Point();
			newPosition.setLocation(this.getX() + mousePoint.getX() - mPosition.getX() ,this.getY() + mousePoint.getY() - mPosition.getY());
			
	    	//p2 = fixMousePositionPrecpective(x+(width/2), y+(height/2), p2);
	    	if (me.isShiftDown()){
	    		rotation += newPosition.getX() - this.lastPosition.getX();
	    		rotation %= 360;
	    		lastPosition.x = newPosition.x;
	    		lastPosition.y = newPosition.y;
	    		return;
	    	}
	    	
			this.setLocation(newPosition);
			//update mPosition
			mPosition = mousePoint;
			if (autorotation)
				rotation = sw.getAutomaticRotation(newPosition, this);
			return;
		}
	    
	  
	  }



	

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		
		this.selected = false;
		this.setSelectedStage2(false);
		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, me.getPoint());
		if (this.contains(mousePoint)){
			if (sw.getSelectedItem() == null){
				sw.setSelectedItem(this);
				selected = true;
			} else if (sw.getSelectedItem() == this){
				this.setSelectedStage2(true);
			}
			
		}
		Point mousePointRotation = fixMouseRotationPrecpective(cx, cy, me.getPoint());
    	sw.drawPoint(mousePointRotation);

//    	 try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	 public void mousePressed(MouseEvent me) {
		if (sw.isDrag())
	    	return;
		
	    rSelectedCorner = null;
	    rDeformSelectedCorner = null;
	    sw.setDraggedItem(null);
	    Point mousePoint = fixMousePositionPrecpective(cx, cy, me.getPoint());
	    Point mousePointRotation = fixMouseRotationPrecpective(cx, cy, me.getPoint());

	    
	    
	    
	    for (int i = 0; i <4;i++){
    	
		    
	    	if (r[i].contains(mousePointRotation)){
	    		rSelectedCorner = r[i];
	    		rSelectedCornerIndex = i;
	    		
	    		sw.setDrag(true);
	    		return;
	    	}
	    }
	    
	    for (int i = 0; i <4;i++){
    	
		    
	    	if (rDeform[i].contains(mousePointRotation)){
	    		rDeformSelectedCorner = rDeform[i];
	    		rDeformSelectedCornerIndex = i;
	    		
	    		sw.setDrag(true);
	    		return;
	    	}
	    }
	    
	    
		if(this.contains(mousePointRotation))
	    {
			sw.setDrag(true);
			mPosition = mousePoint;
			sw.setDraggedItem(this);
			lastPosition.setLocation(this.getX() + mousePoint.getX() - mPosition.getX() ,this.getY() + mousePoint.getY() - mPosition.getY());

			//rotation += 15;
			
			return;
	    }


	}
	

	Point fixMousePositionPrecpective(double cx,double cy,Point e)
	{
		float theta = (float)Math.toRadians(-rotation);
		Point p = new Point();
		e.x -= this.translateX;
		e.y -= this.translateY;
		//p.x = (int) ((Math.cos(theta) * (e.x-cx)) - (Math.sin(theta) * (e.y-cy)) + cx);
		//p.y = (int) ((Math.sin(theta) * (e.x-cx)) + (Math.cos(theta) * (e.y-cy)) + cy);
		e.x /= this.scale;
		e.y /= this.scale;
		//p.x -= this.translateX;
		//p.y -= this.translateY;

	  return e;
	}
	
	Point fixMouseRotationPrecpective (double cx,double cy,Point e)
	{
		float theta = (float)Math.toRadians(-rotation);
		Point p = new Point();
		Point ee = (Point) e.clone();
		ee.x -= this.translateX;
		ee.y -= this.translateY;
		p.x = (int) ((Math.cos(theta) * (ee.x-(this.cx*scale))) - (Math.sin(theta) * (ee.y-(this.cy*scale))) + this.cx*scale);
		p.y = (int) ((Math.sin(theta) * (ee.x-(this.cx*scale))) + (Math.cos(theta) * (ee.y-(this.cy*scale))) + this.cy*scale);
		p.x /= this.scale;
		p.y /= this.scale;
		
		//p.x -= this.translateX;
		//p.y -= this.translateY;
	
	  return p;
	}
	

	
	@Override
	public void mouseReleased(MouseEvent arg0) {
			rSelectedCorner = null;
			fixCenter();
			fixBoundaries();
		
	}

	public double getWidthFactor() {
		return widthFactor;
	}

	public void setWidthFactor(double widthFactor) {
		this.widthFactor = widthFactor;
	}

	public double getHeightFactor() {
		return heightFactor;
	}

	public void setHeightFactor(double heightFactor) {
		this.heightFactor = heightFactor;
	}


	
	
	//update the new values to the current coordinates
	public void reSize(int xx, int yy, int ww, int hh){
		//get the location of the curve points on the sides
		
		
		switch (rSelectedCornerIndex){
    	
    	case 0:

    		x += xx;
    		y += yy;
    		width += ww;
    		height += hh;

    		
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;

    		
    		break;
    	case 1:
    		//x += xx;
    		y += yy;
    		width -= ww;
    		height += hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	case 3:

    		x += xx;
    		//y += yy;
    		width += ww;
    		height -= hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	case 2:

    		//x += xx;
    		//y += yy;
    		width -= ww;
    		height -= hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	}

		fixBoundaries();
		


	}

	public void fixBoundaries() {
		int margin = (int)(10/scale);

		r[0] = new Rectangle((int)x-margin, (int)y-margin, margin, margin);
	    r[1] = new Rectangle((int)x+(int)width, (int)y-margin,margin, margin);
	    r[2] = new Rectangle((int)x+(int)width,(int) y+(int)height,margin, margin);
	    r[3] = new Rectangle((int)x-margin, (int)y+(int)height,margin, margin); 
	    

		//fix deform coreners by setting their real values from normal scale corners and realtiveValue to scale corners
		rDeform[0].setBounds((int)((double)r[0].getX() +  ((double)DeformPointsRelativeToCorners[0].getX())*width), (int)((double)r[0].getY()  + ((double)DeformPointsRelativeToCorners[0].getY())*height), (int)r[0].getWidth(), (int)r[0].getHeight());
		rDeform[1].setBounds((int)((double)r[1].getX() + ((double)DeformPointsRelativeToCorners[1].getX())*width), (int)((double)r[1].getY() + ((double)DeformPointsRelativeToCorners[1].getY())*height), (int)r[1].getWidth(), (int)r[1].getHeight());
		rDeform[2].setBounds((int)((double)r[2].getX() + ((double)DeformPointsRelativeToCorners[2].getX())*width), (int)((double)r[2].getY() + ((double)DeformPointsRelativeToCorners[2].getY())*height), (int)r[2].getWidth(), (int)r[2].getHeight());
		rDeform[3].setBounds((int)((double)r[3].getX()+ ((double)DeformPointsRelativeToCorners[3].getX())*width), (int)((double)r[3].getY() + ((double)DeformPointsRelativeToCorners[3].getY())*height), (int)r[3].getWidth(), (int)r[3].getHeight());
		
		
	    
		
	}
	

	public void fixDeformBoundaries() {
		int margin = (int)(10/scale);
		
		//since the deform points can alter the size, here the scale corners are fixed 
		//by measuring the actual size and resetting scaling corners and the deform relative points
		double w1 = Math.abs((double)rDeform[1].x - (double)rDeform[0].x -(double)margin);
		double w2 = Math.abs((double)rDeform[2].x - (double)rDeform[3].x -(double)margin);
		double w3 = Math.abs((double)rDeform[1].x - (double)rDeform[3].x -(double)margin);
		double w4 = Math.abs((double)rDeform[2].x - (double)rDeform[0].x -(double)margin);
		double w5 = Math.abs((double)rDeform[1].x - (double)rDeform[2].x -(double)margin);
		double w6 = Math.abs((double)rDeform[3].x - (double)rDeform[0].x -(double)margin);

		double h1 = Math.abs((double)rDeform[2].y - (double)rDeform[1].y -(double)margin);
		double h2 = Math.abs((double)rDeform[3].y - (double)rDeform[0].y -(double)margin);
		double h3 = Math.abs((double)rDeform[2].y - (double)rDeform[0].y -(double)margin);
		double h4 = Math.abs((double)rDeform[3].y - (double)rDeform[1].y -(double)margin);
		double h5 = Math.abs((double)rDeform[2].y - (double)rDeform[3].y -(double)margin);
		double h6 = Math.abs((double)rDeform[1].y - (double)rDeform[0].y -(double)margin);
		
		double realWidth, realHeight;
		
		if (w1>w2 && w1>w3 && w1>w4 && w1>w5 && w1>w6)
			realWidth = w1;
		else if (w2>w3 && w2>w4 && w2>w5 && w2>w6)
			realWidth = w2;
		else if (w3>w4 && w3>w5 && w3>w6)
			realWidth = w3;
		else if (w4>w5 && w4>w6)
			realWidth = w4;
		else if (w5>w6)
			realWidth = w5;
		else 
			realWidth = w6;
		
		if (h1>h2 && h1>h3 && h1>h4 && h1>h5 && h1>h6)
			realHeight = h1;
		else if (h2>h3 && h2>h4 && h2>h5 && h2>h6)
			realHeight = h2;
		else if (h3>h4 && h3>h5 && h3>h6)
			realHeight = h3;
		else if (h4>h5 && h4>h6)
			realHeight = h4;
		else if (h5>h6)
			realHeight = h5;
		else 
			realHeight = h6;
		

		

		width = (double)realWidth;
		height = (double)realHeight;
		
		double yCorrection, xCorrection;
		if (rDeform[0].getY()<rDeform[1].getY() && rDeform[0].getY()<rDeform[2].getY() && rDeform[0].getY()<rDeform[3].getY()){
			yCorrection = rDeform[0].getY()+margin  - y;
		}else if (rDeform[1].getY()<rDeform[2].getY() && rDeform[1].getY()<rDeform[3].getY()){
			yCorrection = rDeform[1].getY()+margin  - y;
		}else if ( rDeform[2].getY()<rDeform[3].getY()){
			yCorrection = rDeform[2].getY()+margin  - y;
		}else {
			yCorrection = rDeform[3].getY()+margin  - y;
		}
		
		if (rDeform[0].getX()<rDeform[1].getX() && rDeform[0].getX()<rDeform[2].getX() && rDeform[0].getX()<rDeform[3].getX()){
			xCorrection = rDeform[0].getX()+margin  - x;
		}else if (rDeform[1].getX()<rDeform[2].getX() && rDeform[1].getX()<rDeform[3].getX()){
			xCorrection = rDeform[1].getX()+margin  - x;
		}else if ( rDeform[2].getX()<rDeform[3].getX()){
			xCorrection = rDeform[2].getX()+margin  - x;
		}else {
			xCorrection = rDeform[3].getX()+margin  - x;
		}
	
		y += yCorrection;
		x += xCorrection;
		
//		if (rDeform[0].getX()<rDeform[3].getX()){
//			x = rDeform[0].getX()+margin;
//		}else{
//			x = rDeform[3].getX()+margin;
//		}
		
		r[0] = new Rectangle((int)x-margin, (int)y-margin, margin, margin);
	    r[1] = new Rectangle((int)x+(int)width, (int)y-margin,margin, margin);
	    r[2] = new Rectangle((int)x+(int)width,(int) y+(int)height,margin, margin);
	    r[3] = new Rectangle((int)x-margin, (int)y+(int)height,margin, margin); 
	    
	    
		DeformPointsRelativeToCorners[0].setLocation( (rDeform[0].x-r[0].getX())/width, (rDeform[0].y-r[0].getY())/height);
		DeformPointsRelativeToCorners[1].setLocation( (rDeform[1].x-r[1].getX())/width, (rDeform[1].y-r[1].getY())/height);
		DeformPointsRelativeToCorners[2].setLocation( (rDeform[2].x-r[2].getX())/width, (rDeform[2].y-r[2].getY())/height);
		DeformPointsRelativeToCorners[3].setLocation( (rDeform[3].x-r[3].getX())/width, (rDeform[3].y-r[3].getY())/height);
		//System.out.println(DeformPointsRelativeToCorners[0]);
		//DeformPointsRelativeToCorners[1].setLocation(DeformPointsRelativeToCorners[1].getX()*width/realWidth, DeformPointsRelativeToCorners[1].getY()*height/realHeight);
		//DeformPointsRelativeToCorners[2].setLocation(DeformPointsRelativeToCorners[2].getX()*width/realWidth, DeformPointsRelativeToCorners[2].getY()*height/realHeight);
		//DeformPointsRelativeToCorners[3].setLocation(DeformPointsRelativeToCorners[3].getX()*width/realWidth, DeformPointsRelativeToCorners[3].getY()*height/realHeight);
		
		
		//reset deform points
		rDeform[0].setBounds((int)((double)r[0].getX() + ((double)DeformPointsRelativeToCorners[0].getX())*width), (int)((double)r[0].getY() + ((double)DeformPointsRelativeToCorners[0].getY())*height), (int)r[0].getWidth(), (int)r[0].getHeight());
		rDeform[1].setBounds((int)((double)r[1].getX() + ((double)DeformPointsRelativeToCorners[1].getX())*width), (int)((double)r[1].getY() + ((double)DeformPointsRelativeToCorners[1].getY())*height), (int)r[1].getWidth(), (int)r[1].getHeight());
		rDeform[2].setBounds((int)((double)r[2].getX() + ((double)DeformPointsRelativeToCorners[2].getX())*width), (int)((double)r[2].getY() + ((double)DeformPointsRelativeToCorners[2].getY())*height), (int)r[2].getWidth(), (int)r[2].getHeight());
		rDeform[3].setBounds((int)((double)r[3].getX() + ((double)DeformPointsRelativeToCorners[3].getX())*width), (int)((double)r[3].getY() + ((double)DeformPointsRelativeToCorners[3].getY())*height), (int)r[3].getWidth(), (int)r[3].getHeight());
		

		
//		rDeform[0].setBounds((int)(r[0].getX()+DeformPointsRelativeToCorners[0].x), (int)(r[0].getY()+DeformPointsRelativeToCorners[0].y), (int)r[0].getWidth(), (int)r[0].getHeight());
//		rDeform[1].setBounds((int)(r[1].getX()+DeformPointsRelativeToCorners[1].x), (int)(r[1].getY()+DeformPointsRelativeToCorners[1].y), (int)r[1].getWidth(), (int)r[1].getHeight());
//		rDeform[2].setBounds((int)(r[2].getX()+DeformPointsRelativeToCorners[2].x), (int)(r[2].getY()+DeformPointsRelativeToCorners[2].y), (int)r[2].getWidth(), (int)r[2].getHeight());
//		rDeform[3].setBounds((int)(r[3].getX()+DeformPointsRelativeToCorners[3].x), (int)(r[3].getY()+DeformPointsRelativeToCorners[3].y), (int)r[3].getWidth(), (int)r[3].getHeight());
//		
		
	}

	public int getTranslateY() {
		return translateY;
	}

	public void setTranslateY(int translateY) {
		this.translateY = translateY;
	}

	public int getTranslateX() {
		return translateX;
	}

	public void setTranslateX(int translateX) {
		this.translateX = translateX;
	}

	public void setScale(double scale) {
		this.scale = scale;
		
	}


	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}

	public void setAutorotation(boolean autorotation) {
		this.autorotation = autorotation;
		
	}
	
	public boolean isAutorotation (){
		return autorotation;
	}
	
	public void setShapedWindows(ShapedWindow sw){
		this.sw = sw;
	}
	
	public int getType(){
		return this.type;
	}
	public String getTypeName(){
		if (type == Item.BRUSH){
			return "Brush";
		}
		else if (type == Item.SPLINE){
			return "Spline";
		}
		else if (type == Item.IMAGE){
			return "image";
		}
		else if (type == Item.TEXT){
			return "text";
		}
		return "unknown";
	}
	
	public void setType(int type){
		this.type = type;
	}

	public int getWidth() {
		return (int) this.width;
	}

//	public void repaint(Graphics g) {
//		paint(g);
//	}

	public void setShowShadow(boolean showShadow) {
		this.showShadow = showShadow;
		
	}

	public int getShadowRotation() {
		return shadowRotation;
	}

	public void setShadowRotation(int shadowRotation) {
		this.shadowRotation = shadowRotation;
	}

	public int getShadowDistance() {
		return shadowDistance;
	}

	public void setShadowDistance(int shadowDistance) {
		this.shadowDistance = shadowDistance;
	}
	
	public double getShadowSize() {
		return shadowSize;
	}

	public void setShadowSize(double shadowSize) {
		this.shadowSize = shadowSize;
	}
	
	public Shape createShadow(Shape s){
		   
		int theta = (int) (shadowRotation *3.6);
		int shadowAngleX =  (int) ((shadowDistance * Math.sin(theta)));// - ( ((width*shadowSize/50)-width)/2) );
		int shadowAngleY =  (int) ((shadowDistance * Math.cos(theta)));// - ( ((height*shadowSize/50)-height)/2) );
		//shadowX += (width * shadowSize/50);
		//shadowY += (height * shadowSize/50);

		AffineTransform at = new AffineTransform(); //.getTranslateInstance(shadowX-x, shadowY-y);
		double scalar = (shadowSize/50);
		int correctX = (int) ((x*scalar)-x);
		int correctY = (int) ((y*scalar)-y);
		//at.translate(shadowX, shadowY);
		//fix translation after scaling, get it to zero
		//at.translate(-(x)*(shadowSize/50), -(y)*(shadowSize/50));
		
		//set new translation
		
		at.translate(-x*scalar, -y*scalar);
		int centerX = ((int) width/2) + (int) x -(int)(width*scalar/2);
		int centerY = ((int) height/2) + (int)	y -(int)(height*scalar/2);
		at.translate(centerX, centerY);
		at.scale(scalar, scalar);

		//at.translate(centerX - (scalar*(int)width), centerY - (scalar*(int)height));
		at.translate(shadowAngleX, shadowAngleY);

	    Shape shadow = at.createTransformedShape(s);
	   
	    return shadow;
	}
	



	public Color getShadowColor() {
		return shadowColor;
	}


	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}


	public int getShadowTransparent() {
		return shadowTransparent;
	}


	public void setShadowTransparent(int shadowTransparent) {
		this.shadowTransparent = shadowTransparent;
	}


	public boolean isSelectedStage2() {
		return selectedStage2;
	}


	public void setSelectedStage2(boolean selectedStage2) {
		this.selectedStage2 = selectedStage2;
	}
	

	
//	public Point getPointDeform(Point p){
//		
//		int[] xs = { 75, 300, 500, 550 , 600, 350, 50, 35, 75};
//		int[] ys = { 50, 55, 60, 400, 600, 650, 700, 335, 50};
//		  double closestDistance = -1;
//		  int closestIndex = 0;
//		  
//		  for (int i = 0; i < xs.length; i++){
//			  if (closestDistance == -1){
//				  closestDistance = distance(p , new Point(xs[i], ys[i]));
//				}else{
//					if (distance(p , new Point(xs[i], ys[i])) < closestDistance){
//						closestDistance = distance(p , new Point(xs[i], ys[i]));
//						closestIndex = i;
//					}
//				}
//		  }
//
//			
//		  if (closestDistance < 100){
//			  System.out.println("closestDistance "+closestDistance);
//			  if (p.x>xs[closestIndex]){
//				  p.x -= closestDistance;
//			  }else{
//				  p.x += closestDistance;
//
//			  }
//			  if (p.y>ys[closestIndex]){
//				  p.y -= closestDistance;
//			  }else{
//				  p.y += closestDistance;
//
//			  }
//		  }
//		
//		
//		return p;
//	}
	
	
	public void updateMatrix(Point q[]){
		double[][] matrixIValue = {
				{1 , 0 , 0 , 0},
				{-1 , 1 , 0 , 0},
				{-1 , 0 , 0 , 1},
				{1 , -1 , 1 , -1}
		};
		Matrix matrixI =new Matrix(matrixIValue);

		
		double [] pxValues = {(double)q[0].x, (double)q[1].x, (double)q[2].x, (double)q[3].x};
		double [] pyValues = {(double)q[0].y, (double)q[1].y, (double)q[2].y, (double)q[3].y};
		
		Matrix px = new Matrix(pxValues, 1);
		Matrix py = new Matrix(pyValues, 1);
		
		this.a = matrixI.times(px.transpose());
		this.b = matrixI.times(py.transpose());
	}
	
	public Point getPointInQuadrilateral(Point p){
		//find factors for each side
		double l = (p.x - x)/width;
		double m = (p.y - y)/height;
		
		
//		double[][] matrixValue = {
//				{1 , 0 , 0 , 1},
//				{1 , 1 , 0 , 0},
//				{1 , 1 , 1 , 1},
//				{1 , 0 , 1 , 0}
//		};
//		Matrix matrix = new Matrix(matrixValue);
//		//Matrix matrixI = matrix.inverse();
//		
//		double[][] matrixIValue = {
//				{1 , 0 , 0 , 0},
//				{-1 , 1 , 0 , 0},
//				{-1 , 0 , 0 , 1},
//				{1 , -1 , 1 , -1}
//		};
//		Matrix matrixI =new Matrix(matrixIValue);
//
//		
//		double [] pxValues = {(double)q[0].x, (double)q[1].x, (double)q[2].x, (double)q[3].x};
//		double [] pyValues = {(double)q[0].y, (double)q[1].y, (double)q[2].y, (double)q[3].y};
//		
//		Matrix px = new Matrix(pxValues, 1);
//		Matrix py = new Matrix(pyValues, 1);
		
//		Matrix a = matrixI.times(px.transpose());
//		Matrix b = matrixI.times(py.transpose());


		int xx = (int) (a.get(0,0) + a.get(1,0)*l + a.get(2,0)*m + a.get(3,0)*l*m);
		int yy = (int) (b.get(0,0) + b.get(1,0)*l + b.get(2,0)*m + b.get(3,0)*l*m);
		
		
		return new Point(xx,yy);
		
	}
	
	
	public Point getPointInQuadrilateralOld(Point p, Point q[]){
		//find factors for each side
		double l = (p.x - x)/width;
		double m = (p.y - y)/height;
		
		
		double[][] matrixValue = {
				{1 , 0 , 0 , 1},
				{1 , 1 , 0 , 0},
				{1 , 1 , 1 , 1},
				{1 , 0 , 1 , 0}
		};
		Matrix matrix = new Matrix(matrixValue);
		//Matrix matrixI = matrix.inverse();
		
		double[][] matrixIValue = {
				{1 , 0 , 0 , 0},
				{-1 , 1 , 0 , 0},
				{-1 , 0 , 0 , 1},
				{1 , -1 , 1 , -1}
		};
		Matrix matrixI =new Matrix(matrixIValue);

		
		double [] pxValues = {(double)q[0].x, (double)q[1].x, (double)q[2].x, (double)q[3].x};
		double [] pyValues = {(double)q[0].y, (double)q[1].y, (double)q[2].y, (double)q[3].y};
		
		Matrix px = new Matrix(pxValues, 1);
		Matrix py = new Matrix(pyValues, 1);
		
		Matrix a = matrixI.times(px.transpose());
		Matrix b = matrixI.times(py.transpose());


		int xx = (int) (a.get(0,0) + a.get(1,0)*l + a.get(2,0)*m + a.get(3,0)*l*m);
		int yy = (int) (b.get(0,0) + b.get(1,0)*l + b.get(2,0)*m + b.get(3,0)*l*m);
		
		
		return new Point(xx,yy);
		
	}
	
	
	  public static Point lineIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		  double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		  if (denom == 0.0) { // Lines are parallel.
		     return null;
		  }
		  double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		  double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
		    if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
		        // Get the intersection point.
		        return new Point((int) (x1 + ua*(x2 - x1)), (int) (y1 + ua*(y2 - y1)));
		    }

		  return null;
		  }
	  
	  public static double distance(Point p1, Point p2){
		  return  Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y)  );
	  }
	  
	  public int getHeight(){
		  return (int) height;
	  }


	public boolean isHide() {
		return hide;
	}


	public void setHide(boolean hide) {
		this.hide = hide;
	}
	public boolean isAutoResize() {
		return this.autoResize ;
	}

	public void setAutoResize(boolean b) {
		this.autoResize = b;
	}  
	 


	
}
