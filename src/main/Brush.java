/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.AlphaComposite;
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
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.shape.Path;



public class Brush  extends Item implements MouseListener, MouseMotionListener, Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 123452111L;
	private JLabel label;
	private static int WIDTH = 100;
	private static int HEIGHT = 50;
	
	protected Point2D[] mPoints;
	protected Point2D mSelectedPoint;
	private Point mPosition;
	private GeneralPath path;
	
	
	private int mSelectedPointIndex;
	private Point previousPoint = null;
	private Color c;
	private int curveWidth;
//	private int[] px;
//	private int[] py;
//	
	private List<Integer> px = new ArrayList<Integer>();
	  private List<Integer> py = new ArrayList<Integer>();
	  static final int xOffset = 30, yOffset = 30;
	  

	  private int dragIndex = NOT_DRAGGING;
	private Point lastRealPosition;
	private boolean recordDrawing = false;

	  private final static int NEIGHBORHOOD = 15;

	  private final static int NOT_DRAGGING = -1;
	  
	  private BufferedImage brush = null;
	private BufferedImage OriginalBrush;
	
	//private Color color;
	
	Brush (ShapedWindow sw,String title, int type){
		
		super(0, sw, type);
		this.setTitle(title);
		this.c = Color.black;
		this.curveWidth = 10;

		this.type = type;
		this.lastPosition = new Point(0,0);
		this.sw = sw;
		this.x = sw.getCenter().x;
		this.y = sw.getCenter().y;
		
		try {
			brush =  ImageIO.read(new File("Resources/brush.png"));
		
			
			width = brush.getWidth();
			height = brush.getHeight();
					
			
			//make a copy of brush to keep the original shape
			OriginalBrush = new BufferedImage(brush.getWidth(), brush.getHeight(), BufferedImage.TYPE_INT_ARGB);

			Graphics g = OriginalBrush.createGraphics();
			g.drawImage(brush, 0, 0, brush.getWidth(), brush.getHeight(), null);
			g.dispose();			
	            

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		brush = changeBrushColor(brush, this.c);
		
		
		//this.color = label.getForeground();

	 
	    mSelectedPoint = null;
	      cx  = x + (width/2);
	    cy = y + (height/2);

	}
	
	
	private BufferedImage changeBrushColor(BufferedImage img, Color color) {
//		Color redColor = Color.red;
		int red   = color.getRed();
		int green = color.getGreen();
		int blue  = color.getBlue();

		  BufferedImage buff = new BufferedImage(
				  img.getWidth(),
				  img.getHeight(),
                  BufferedImage.TYPE_INT_ARGB
          );
		  

          Graphics2D g = buff.createGraphics();
          g.drawImage(img, 0, 0, null);

          for(int x = 0; x < img.getWidth(); x++) {
              for(int y = 0; y < img.getHeight(); y++) {
                  Color c = new Color(img.getRGB(x, y), true);
                  
                  int alpha = c.getAlpha();

                buff.setRGB(x, y, new Color(red, green, blue, alpha).getRGB());
              }
          }
        
		return buff;
	}


	@Override
	public void setLocation (Point p){
		
		
		Point change = new Point();
		change.setLocation(p.getX() - this.getX() ,p.getY() - this.getY());
		
		Point pLeft = new Point();
		Point pRight = new Point();
		pLeft.setLocation(x, ((mPoints[0].getY()-y)/height));
		pRight.setLocation(x, ((mPoints[3].getY()-y)/height));
		
		
		this.x = p.x;
		this.y = p.y;
		this.cx += change.getX();
		this.cy += change.getY();
		
		mPoints[0].setLocation(mPoints[0].getX() + change.getX(), mPoints[0].getY() + change.getY());
		mPoints[1].setLocation(mPoints[1].getX() + change.getX(), mPoints[1].getY() + change.getY());
		mPoints[2].setLocation(mPoints[2].getX() + change.getX(), mPoints[2].getY() + change.getY());
		mPoints[3].setLocation(mPoints[3].getX() + change.getX(), mPoints[3].getY() + change.getY());
		
		fixBoundaries();
		
		pLeft.setLocation(x, y+(pLeft.getY()*height));
		pRight.setLocation(x, y+(pRight.getY()*height));
	    
		
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	

	@Override
	public void paint(Graphics g){
      
		super.paint(g);
		
//		if (rotation != 0){
//			rotateBrush();
//		}
		
		Graphics2D g2 = (Graphics2D) g;
	     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	   
	     g2.translate(this.translateX,this.translateY);
	     g2.rotate(Math.toRadians(rotation),cx*scale,cy*scale);
	     g2.scale(this.scale , this.scale);
	     
	     Color o = g2.getColor();
	     g2.setColor(c);
	     
	     
	     brush = changeBrushColor(brush, this.c);
	     for (int i = 0;i<px.size();i++){
	    	 //g2.fillOval(px.get(i), py.get(i), 3, 3);
	    	 g2.drawImage(this.brush, px.get(i), py.get(i), null);
	     }
//	    if (px.isEmpty()){
//	    	return;
//	    }
//	     
//	    path = new GeneralPath();
//	    path.moveTo(px.get(0), py.get(0));
//	    
//	    
//	    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
//        g2.setStroke(dashed);
//        
//	    for (int i = 1; i+1<px.size();i+=2){
//		    path.curveTo(px.get(i), py.get(i), px.get(i), py.get(i), px.get(i+1), py.get(i+1));
//		    if (selected){
//		    	 g2.drawLine(px.get(i-1), py.get(i-1), px.get(i), py.get(i));
//				 g2.drawLine(px.get(i), py.get(i), px.get(i+1), py.get(i+1));
//				 
//		    }
//		   
//	    }
	  
	    
//	    //do rotation
//	    AffineTransform at = new AffineTransform();
//	    at.rotate(rotation);
//	  //  path = (GeneralPath) path.createTransformedShape(at);
	    
	  g2.setStroke(new BasicStroke());

	    
	    if (showShadow){
	    	g2.setColor(getShadowColor());
	    	Shape shadow = createShadow(path);
			 float st = (100f-(float)getShadowTransparent())/100f;
			 g2.setComposite(AlphaComposite.SrcOver.derive(st));
			 g2.fill(shadow);
			 g2.setColor(c);
		     g2.setComposite(AlphaComposite.SrcOver);
	    }
//	    g2.setColor(c);
//
//	    Stroke pathStroke = new BasicStroke(curveWidth);
//	    g2.setStroke(pathStroke);
//	    g2.draw(path);
//	    
//	    g2.setStroke(new BasicStroke());
//	     if (selected){
//	    	 for (int i = 0; i < px.size(); i++) {
//	    	      if (i % 2 == 0)
//	    	        g.setColor(Color.RED);
//	    	      else
//	    	        g.setColor(Color.BLACK);
//	    	      g.fillOval(px.get(i) - 6, py.get(i) - 6, 12, 12);
//	    	    }
//	     }
	     
	     
	     g2.scale(1/this.scale , 1/this.scale);
	     g2.rotate(-Math.toRadians(rotation), cx*scale,cy*scale);
	     g2.translate(-this.translateX,-this.translateY);
	     g2.setColor(o);
	     
	     
//	     //update x and y for brush
//	     x = path.getBounds2D().getX();
//	     y = path.getBounds2D().getY();
	     

	}



	
	@Override
	  public void mouseDragged(MouseEvent e) {
		
		 if (sw.getCurrentBrush() == this && recordDrawing){
			 px.add(e.getX()+5-brush.getWidth()/2);
			 py.add(e.getY()+32-brush.getWidth()/2);
		 }
		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, e.getPoint());
//
//	    if (dragIndex != NOT_DRAGGING){
//	    	
//	    	px.set(dragIndex, (int) mousePoint.getX());
//		    py.set(dragIndex, (int) mousePoint.getY());
//	    	  return;
//	    }
//	    
//
//	    
//	    
//	    
	    if (!sw.isDrag())
			  return;
	    
	    if (sw.getDraggedItem()==this){
			Point change = new Point();
			
			change.setLocation(mousePoint.getX() - lastPosition.getX() ,mousePoint.getY() - lastPosition.getY());
			lastPosition.setLocation(mousePoint.getX(),mousePoint.getY());
			

			
			
	    	//p2 = fixMousePositionPrecpective(x+(width/2), y+(height/2), p2);
	    	if (e.isShiftDown()){
	    		rotation += e.getX() - this.lastRealPosition.getX();
	    		rotation %= 360;
	    		this.lastRealPosition = e.getPoint();
	    		return;
	    	}
			
           for (int i = 0; i < px.size(); i++) {
				px.set(i,  (px.get(i) + (int)change.getX()) );
				py.set(i,  (py.get(i) + (int)change.getY()) );

			}
//           cx += (int)change.getX();
//           cy += (int)change.getY();
	    	
			//update mPosition
			
			return;
		}
	    
	  }


	

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
//		if (sw.getCurrentBrush() == this){
//			brush
//		}
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, me.getPoint());

			
			if (me.getClickCount() == 2) {
			    sw.setCurrentBrush(null);
			    this.setSelected(false);
			    if (sw.getSelectedItem() == this){
			    	sw.setSelectedItem(null);
			    	 //update center
				    
//				    cx = path.getBounds2D().getCenterX();
//				    cy = path.getBounds2D().getCenterY();
			    }
			 
			    return;
			    
			  }
			
			if (sw.getSelectedItem() == this)
			if (sw.getCurrentBrush() != this){
				sw.setSelectedItem(null);
			}
			
//			
//		if (sw.getCurrentBrush() == this){
//			if (this.previousPoint == null){
//				px.add((int) mousePoint.getX());
//				py.add((int) mousePoint.getY());
//			}else{
//				px.add(((int)mousePoint.getX()+(int)previousPoint.x) /2);
//				py.add(((int)mousePoint.getY()+(int)previousPoint.y) /2);
//				
//				px.add((int) mousePoint.getX());
//				py.add((int) mousePoint.getY());
//			}
//			previousPoint = mousePoint;
//			
//			
//		}else{
//			 int minDistance = Integer.MAX_VALUE;
//			    for (int i = 0; i < px.size(); i++) {
//			      int deltaX = px.get(i) - me.getX();
//			      int deltaY = py.get(i) - me.getY();
//			      int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
//			      if (distance < minDistance) {
//			        minDistance = distance;
//			      }
//			    }
//			    if (minDistance > NEIGHBORHOOD){
//			    	 if (sw.getSelectedItem() == this){
//					    	sw.setSelectedItem(null);
//					    }
//			    	 return;
//			    }
//			     
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
	 public void mousePressed(MouseEvent e) {
		if (!selected)
			return;
		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, e.getPoint());
		 
		 if (sw.getCurrentBrush() == this){
			 this.recordDrawing  = true;
			 return;
		 }
		 
		 
		 sw.setDrag(true);
			sw.setDraggedItem(this);
			lastPosition = new Point();
			lastPosition = mousePoint;
			lastRealPosition = e.getPoint();
//
//
//			//rotation += 15;
//			
			return;
		 
		 
//		    dragIndex = NOT_DRAGGING;
//		    int minDistance = Integer.MAX_VALUE;
//		    int indexOfClosestPoint = -1;
//		    for (int i = 0; i < px.size(); i++) {
//		      int deltaX = (int) (px.get(i) - mousePoint.getX());
//		      int deltaY = (int) (py.get(i) - mousePoint.getY());
//		      int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
//		      if (distance < minDistance) {
//		        minDistance = distance;
//		        indexOfClosestPoint = i;
//		      }
//		    }
//		    if (minDistance > NEIGHBORHOOD){
//		    	//return;
//
//					sw.setDrag(true);
//					sw.setDraggedItem(this);
//					lastPosition = new Point();
//					lastPosition = mousePoint;
//					lastRealPosition = e.getPoint();
//
//
//					//rotation += 15;
//					
//					return;
//			    }
//		   
//
//		    dragIndex = indexOfClosestPoint;
		  }

		  public void mouseReleased(MouseEvent e) {
		    if (dragIndex == NOT_DRAGGING)
		      return;
		    
		    recordDrawing = false;
		    
			 Point mousePoint = fixMouseRotationPrecpective(cx, cy, e.getPoint());

//		    px.set(dragIndex, (int) mousePoint.getX());
//		    py.set(dragIndex, (int) mousePoint.getY());
		      
		    
		    dragIndex = NOT_DRAGGING;
		  }

	public void setText (String t){
		label.setText(t);
	}
	public String getText (){
		return label.getText();
	}
	public JLabel getLabel(){
		return label;
	}
	public void setLabel(JLabel l) {
		this.label = l;
		
	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color color) {
		this.c = color;
		this.brush = changeBrushColor(brush, this.c);
		
	}





	public int getCurveWidth() {
		return curveWidth;
	}


	public void setCurveWidth(int curveWidth) {
		this.curveWidth = curveWidth;
		int w = (int)(width*curveWidth/10);
		int h = (int)(height*curveWidth/10);
		brush = resize(OriginalBrush,w, h );
		//brush = (BufferedImage) brush.getScaledInstance(w*curveWidth, h*curveWidth, BufferedImage.SCALE_SMOOTH);
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
				BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

				Graphics g = newImage.createGraphics();
				g.drawImage(img, 0, 0, newW, newH, null);
				g.dispose();
				return newImage;
				
				}

}
