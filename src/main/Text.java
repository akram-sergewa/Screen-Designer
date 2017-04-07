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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.shape.Path;



public class Text  extends Item implements MouseListener, MouseMotionListener, Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 12132111L;
	private JLabel label;
	private static int WIDTH = 100;
	private static int HEIGHT = 50;
	
	private boolean showCurve = false;
	protected Point2D[] mPoints;
	protected Point2D mSelectedPoint;
	private Point mPosition;
	
	
	private int mSelectedPointIndex;
	private boolean showEditBox = false;
	private Spline spline = null;
	
	//private Color color;
	
	Text (JLabel label, int orderCounter, ShapedWindow sw, int type){
		super(orderCounter, sw, type);

		this.type = type;
		this.label = label;
		this.lastPosition = new Point(0,0);
		this.sw = sw;
		this.x = sw.getCenter().x;
		this.y = sw.getCenter().y;
		//this.color = label.getForeground();
		label = new JLabel();
		label.setBounds((int)x, (int)y, (int)WIDTH, (int)HEIGHT);
		setTitle("text");
		setOrder(orderCounter);
		
		
		

		mPoints = new Point2D[4];
		// Cubic curve.
	
		mPoints[0] = new Point2D.Double(x, y);
		mPoints[1] = new Point2D.Double(x+50, y+25);
		mPoints[2] = new Point2D.Double(x+100, y-25);
		mPoints[3] = new Point2D.Double(x+150, y);
		
		mPoints[0] = new Point2D.Double(x, y+(height/2));
		mPoints[1] = new Point2D.Double(x+(width/3), y+((height/10)));
		mPoints[2] = new Point2D.Double(x+(width*2/3), y-((height/10)));
 		mPoints[3] = new Point2D.Double(x+width, y+(height/2));
		
	
		 
	 
	    mSelectedPoint = null;
	   // r = new Rectangle[4];
	    Graphics2D g2 = (Graphics2D) sw.getGraphics();
	     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	     FontRenderContext frc = g2.getFontRenderContext();
		 resetSize(frc);
	    fixBoundaries();
	    cx  = x + (width/2);
	    cy = y + (height/2);

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
	
//	public void fixCenter(){
//	
//		
//		Rectangle r = new Rectangle ((int)x,(int)y,(int)width,(int)height);
//		Path2D.Double path = new Path2D.Double();
//	    path.append(r, false);
//		AffineTransform t = new AffineTransform();
//	    t.rotate(Math.toRadians(rotation), cx, cy);
//	    path.transform(t);
//	    sw.drawPoint(path);
//	    
//	    int cxAfterRotation = (int) path.getBounds2D().getCenterX();
//	    int cyAfterRotation = (int) path.getBounds2D().getCenterY();
//	    
//	    t = new AffineTransform();
//	    t.rotate(Math.toRadians(-rotation),cxAfterRotation ,cyAfterRotation);
//	    path.transform(t);
//
//	    
//	    x = path.getBounds2D().getX();
//	    y = path.getBounds2D().getY();
//	    width = path.getBounds2D().getWidth();
//	    height = path.getBounds2D().getHeight();
//	    cx = path.getBounds2D().getCenterX();
//	    cy = path.getBounds2D().getCenterY();
//	    
//
//	    
////	    
//
////	    Point p = new Point ();
////	    p.setLocation(cxAfterRotation, cyAfterRotation);
////	    sw.drawPoint(p);
////	    sw.drawPoint(path);
//////	    int cxReal = x + (width/2);
//////		int cyReal = y + (height/2); 
//////		cx = cxAfterRotation;
//////		cy = cyAfterRotation;
////	    
////	    System.out.println("cxAfterRotation "+cxAfterRotation+" cx "+cx+" cyAfterRotation "+ cyAfterRotation+ " cy " + cy);
////
////	    
////	    //get real center for current box
////	    cx = x + (width/2);
////	    cy = y + (height/2); 
////		x -= cxAfterRotation-cx;
////		y -= cyAfterRotation-cy;
////		cx = cxAfterRotation;
////		cy = cyAfterRotation;
////		
//	    
//	    
//	    
//		//cx = cxNew;
//		//cy = cyNew;
//	}

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
//		try {
//			Test.paint(g, getFont());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (!showCurve){
			Graphics2D g2 = (Graphics2D) g;
		     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		     //fix scaling
		     
		     //System.out.println(-sw.getX()*(scale-1));
		     
		     
		     Font font = label.getFont();
		     FontRenderContext frc = g2.getFontRenderContext();
		     
		     		     
		     List<String> texts = stringLines(getText());
		     Area glyph = new Area();
		     double lineHeight = font.getLineMetrics(getText(), frc).getHeight()/2;
		     double currentLineHeight = 0.0;
		     int counter = 0;
		     for (String text:texts){
		    	 GlyphVector gv = font.createGlyphVector(frc, text);
		    	 Area lineShape = new Area(gv.getOutline());
		    	 AffineTransform att = AffineTransform.getTranslateInstance(lineShape.getBounds2D().getX(), lineShape.getBounds2D().getY() + lineShape.getBounds2D().getHeight() + currentLineHeight );
		    	 if (counter++<texts.size()-1)
		    		 currentLineHeight += lineShape.getBounds2D().getHeight();
		    	 Shape transformedGlyph = att.createTransformedShape(lineShape);
		    	 glyph.add(new Area(transformedGlyph));
		    	 System.out.println(glyph.getBounds2D());
		     }
		     System.out.println(currentLineHeight);
	    	 AffineTransform att = AffineTransform.getTranslateInstance(0, -currentLineHeight);

	    	 glyph =new Area(att.createTransformedShape(glyph));
//		     GlyphVector gv = font.createGlyphVector(frc, getText());
//		     GlyphVector gv2 = font.createGlyphVector(frc, "sssssss");

		     //LineBreakMeasurer
		     
		     
//		     
//		     Shape glyphs =  gv.getOutline();
//		     Shape glyphs2 =  gv2.getOutline();
//
//		     Area glyph = new Area(glyphs);
//		     
//		     Area a2 = new Area(glyphs2);
//		     glyph.add(a2);
		     
		     AffineTransform at = AffineTransform.getTranslateInstance(x, y+height);
		     at.scale(width/glyph.getBounds2D().getWidth(), height/glyph.getBounds2D().getHeight());
//		     at.shear(1, 0);
		     Shape transformedGlyph = at.createTransformedShape(glyph);
		     Color o = g2.getColor();
		     g2.setColor(label.getForeground());
		     
		     
//		   //check screen corners
		     if (isAutoResize()){
		    	 Point screenCenter = new Point((int)sw.getShape().getBounds2D().getCenterX(),(int)sw.getShape().getBounds2D().getCenterY());
			     if (width>20){
			    	 Point rZero = fixMousePositionPrecpective(cx,cy,r[0].getLocation());
			    	 Point rTwo = fixMousePositionPrecpective(cx,cy,r[2].getLocation());
			    	 while (!sw.getShape().contains(rZero)){
			    			System.out.println((screenCenter.getX()-rZero.getX())/100);
			    			if (rZero.getX()<screenCenter.getX()){
			    				x += (int) ((screenCenter.getX()-rZero.getX())/100);
				    			y += (int) ((screenCenter.getY()-rZero.getY())/100);
				    			width -=(int)((screenCenter.getX()-rZero.getX())/100);
			    			}else{
			    				x -= (int) ((screenCenter.getX()-rZero.getX())/100);
				    			y -= (int) ((screenCenter.getY()-rZero.getY())/100);
				    			width +=(int)((screenCenter.getX()-rZero.getX())/100);
			    			}
			    			
			    			
			    			
			    			fixBoundaries();
			    			rZero = fixMousePositionPrecpective(cx,cy,r[0].getLocation());
			    			
			    			if (width<20)
			    				break;			
			    		}
			    	 
			    	 while (!sw.getShape().contains(rTwo)){
			    			System.out.println((screenCenter.getX()-rTwo.getX())/100);
			    			x += (int) ((screenCenter.getX()-rTwo.getX())/100);
			    			y += (int) ((screenCenter.getY()-rTwo.getY())/100);
			    			width +=(int)((screenCenter.getX()-rTwo.getX())/100);
			    			fixBoundaries();
			    			rTwo = fixMousePositionPrecpective(cx,cy,r[2].getLocation());
			    			
			    			if (width<20)
			    				break;			
			    		}
			    	 
			    	 
			     } 
		     }
//		     
	    		
	    		
	    	fixBoundaries();
		     
		     int margin = (int)(10/scale);
		 
		     Point[] q = new Point[4];
		     q[0] = new Point((int) rDeform[0].getX()+margin,(int) rDeform[0].getY()+margin);
		     q[1] = new Point((int) rDeform[1].getX(),(int) rDeform[1].getY()+margin);
		     q[2] = new Point((int) rDeform[2].getX(),(int) rDeform[2].getY());
		     q[3] = new Point((int) rDeform[3].getX()+margin,(int) rDeform[3].getY());
		     updateMatrix(q);

		     GeneralPath transformedGlyphNew = null;
     
		     double flattness = 1.0;
		     PathIterator it = new FlatteningPathIterator( transformedGlyph.getPathIterator( null ), flattness );
		        float points[] = new float[6];
		     
		        while (!it.isDone()){
		        	
		        	int type = it.currentSegment( points );
		        	 Point p = new Point();
		        	p.setLocation(points[0], points[1]);
				    p = getPointInQuadrilateral(p);


					
				     
				    
		        	 if (transformedGlyphNew == null){
					    	transformedGlyphNew = new GeneralPath();
					    	transformedGlyphNew.moveTo(p.x, p.y);
					    	
					    }

		        		 
		        		 
		        	switch( type ){
		            case PathIterator.SEG_MOVETO:
		            	//System.out.println("SEG_MOVETO");
		            	transformedGlyphNew.moveTo(p.x, p.y);
		            	
		            case PathIterator.SEG_LINETO:
		            	//System.out.println("SEG_LINETO");

		            	transformedGlyphNew.lineTo(p.x, p.y);
		            	
		            case PathIterator.SEG_CLOSE:
		            	//System.out.println("SEG_CLOSE");


		        	}

		        	it.next();
		        }
		        
		        transformedGlyphNew.closePath();
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     double fontFactor = width/glyph.getBounds2D().getWidth();
		     Font font2 = new Font(font.getName(), font.getStyle(), (int) (font.getSize()*fontFactor));
		     

		     
		     
		     g2.translate(this.translateX,this.translateY);
		     g2.rotate(Math.toRadians(rotation),cx*scale,cy*scale);

		     g2.scale(this.scale , this.scale);
		     
		     
		     //if selected and the text is to be edited
		     if (showEditBox){
		    	 sw.updateAndShowEditingTextBox((int)x, (int)y, (int)width, (int)height, font2);
		    	
		     }else{
		    	  
		    	 if (showShadow){
						//draw shadow
			    	Color c = g2.getColor();
			    	g2.setColor(getShadowColor());
					 Shape shadow = createShadow(transformedGlyphNew);
					 float st = (100f-(float)getShadowTransparent())/100f;
					 g2.setComposite(AlphaComposite.SrcOver.derive(st));
					 g2.fill(shadow);
					 g2.setColor(c);
				     g2.setComposite(AlphaComposite.SrcOver);
				     //System.out.println("shadow");

					}
			     
		    	 g2.setColor(label.getForeground());
			     //g2.fill(transformedGlyph);
			     g2.fill(transformedGlyphNew);

			     
			    
			     
			     if (selected){
						drawCorners(g);
				    	//System.out.println("draw stage1");

					}
			     else if (isSelectedStage2()){
				    	drawDeformCorners(g); 
				    	//System.out.println("draw stage2");
				    	
				     }
		     }
		     
		  
		     
		     g2.scale(1/this.scale , 1/this.scale);
		     g2.rotate(-Math.toRadians(rotation), cx*scale,cy*scale);
		     g2.translate(-this.translateX,-this.translateY);
		     g2.setColor(o);

		     
		}
		else if (spline != null) {
		
			Graphics2D g2 = (Graphics2D)g;
		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
		    RenderingHints.VALUE_ANTIALIAS_ON);
		    g2.rotate(Math.toRadians(rotation), x + width/2, y + height/2);
		     
		    // Draw the tangents.
		    Line2D tangent1 = new Line2D.Double(mPoints[0], mPoints[1]);
		    Line2D tangent2 = new Line2D.Double(mPoints[2], mPoints[3]);
		    g2.setPaint(Color.gray);

		    // Draw the cubic curve.
		    CubicCurve2D c = new CubicCurve2D.Float();

			 
		    c.setCurve(mPoints, 0);
		    g2.setPaint(Color.black);
		   // g2.draw(c);
		    
		    //get font scale factor
		    FontRenderContext frc = g2.getFontRenderContext();
		    resetSize(frc);
		    double factorWidth = width/(label.getFont().getStringBounds(getText(), frc).getWidth());
		    double factorHeight = height/(label.getFont().getStringBounds(getText(), frc).getHeight());
		    

		    
		    //Draw string along the cubic line
		    label.setFont(new Font(label.getFont().getFontName(), label.getFont().getStyle(), label.getFont().getSize() ));
		    //Shape s = new CurvedText(this.getText(), label.getFont(), factorWidth, factorHeight, (int)width, g).createCurvedText(spline.getPath());
		    Shape s = createCurvedText(spline.getPath(),frc, label.getFont(), factorWidth);

		    g2.fill(s);

	
		     g2.rotate(-Math.toRadians(rotation), x + width/2, y + height/2);
		}
		
	
		

	}

	
	public boolean isShowCurve() {
		return showCurve;
	}

	public void setShowCurve(boolean showCurve) {
		this.showCurve = showCurve;
	}

	

	@Override
	public void mouseDragged(MouseEvent me) {
		  if (!sw.isDrag())
			  return;
		  
		  Point mousePoint = fixMousePositionPrecpective(cx, cy, me.getPoint());
		 Point mousePointRotation = fixMouseRotationPrecpective(cx, cy, me.getPoint());
 
	    if (mSelectedPoint != null) {

	    	
	    	if (!contains((Point) mousePoint)){
	    		return;
	    	}
	    	
	    	if (mSelectedPointIndex == 0 || mSelectedPointIndex == 3){
	    		//Only move in the Y axis for the points on the sides
	    		Point p = new Point();
	    		p.setLocation(mSelectedPoint.getX(), mousePoint.getY());
	    		mSelectedPoint.setLocation(p);
	    		return;
	    	}
	    	
	      mSelectedPoint.setLocation(mousePoint);
	     
	      //paint();
	      return;
	    }
	    if (rSelectedCorner !=null){
	    	//System.out.println("check boxes");
	    	int xx,yy,ww,hh;
		

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
	    	double xx,yy;
	    	//Point2D mousePoint = fixMousePositionPrecpective(x+(width/2), y+(height/2), me.getPoint());
			

	    	xx = ((double)mousePointRotation.getX()-(double)rDeformSelectedCorner.getX())/width;
	    	yy = ((double)mousePointRotation.getY()-(double)rDeformSelectedCorner.getY())/height;


	    	//System.out.println("xx "+xx+" yy "+yy);

	    	DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].setLocation(DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getX()+xx, DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getY()+yy);

   		 fixBoundaries();
   		fixDeformBoundaries();
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
			
			if (autorotation){
				//System.out.println("autorotation "+autorotation);
				rotation = sw.getAutomaticRotation(newPosition, this);
	
			}
			return;
		}
	    
	  
	  }



	

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		
		//this.showEditBox  = false;

		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, me.getPoint());
		 if (this.contains(mousePoint)){
			 
			 	if (me.getClickCount() == 2) {
				    sw.setEditingTextBoxText(this.getText());
				    this.showEditBox  = true;
				    return;
				    
				  }
			 
				if (sw.getSelectedItem() == null){
					//System.out.println("stage1");

					sw.setSelectedItem(this);
					selected = true;
				} else if (sw.getSelectedItem() == this){
					if (this.isSelectedStage2()){
						this.setSelectedStage2(false);
						this.setSelected(true);
					}else{
						this.setSelectedStage2(true);
						this.setSelected(false);
					}
					
				}
				
			}else{
				//System.out.println("not in");
				this.selected = false;

				if (sw.getSelectedItem() == this){
					//System.out.println("remove");

					sw.setSelectedItem(null);
					//if it was selectedStage2 in the previous click
					//apply the changes on the text
					if (isSelectedStage2()){
						setSelectedStage2(false);
					}
					
					if ( this.showEditBox){
						//calculate difference in text size before and after editing
						double factor = (double)sw.getEditingTextBoxTexT().length() / (double)getText().length();
						//System.out.println("factor "+factor);
						
						//apply changes
						System.out.println(sw.getEditingTextBoxTexT());
						this.setText(sw.getEditingTextBoxTexT());
						this.setTitle(getText());
						this.width *= factor;
						this.height *= factor;
						fixBoundaries();
					}
				}
				
				this.showEditBox  = false;
			}
//		 
//		if (sw.getSelectedItem() == null && this.contains(mousePoint)){
//			sw.setSelectedItem(this);
//			selected = true;
//		}
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
		
	    mSelectedPoint = null;
	    rSelectedCorner = null;
	    sw.setDraggedItem(null);
	    rDeformSelectedCorner = null;

	    Point mousePoint = fixMousePositionPrecpective(cx, cy, me.getPoint());
	    Point mousePointRotation = fixMouseRotationPrecpective(cx, cy, me.getPoint());


	    
	    
	    if (selected){
	    	for (int i = 0; i <4;i++){
	        	
			    
		    	if (r[i].contains(mousePointRotation)){
		    		rSelectedCorner = r[i];
		    		rSelectedCornerIndex = i;
		    		
		    		sw.setDrag(true);
		    		return;
		    	}
		    }
	    }
	    else if (isSelectedStage2()){
	    	for (int i = 0; i <4;i++){
	        	
			    
		    	if (rDeform[i].contains(mousePointRotation)){
		    		rDeformSelectedCorner = rDeform[i];
		    		rDeformSelectedCornerIndex = i;
		    		
		    		sw.setDrag(true);
		    		return;
		    	}
		    }
	    }
	    
	    
	    

	    
	    for (int i = 0; i < mPoints.length; i++) {
	      Shape s = getControlPoint(mPoints[i]);
	      
	      if (s.contains(mousePoint)) {
	        mSelectedPoint = mPoints[i];
	        sw.setDrag(true);
	        this.mSelectedPointIndex = i;
	        return;
	      }
	    }
	  
		if(sw.getSelectedItem() == this)
	    {
			sw.setDrag(true);
			mPosition = mousePoint;
			sw.setDraggedItem(this);
			lastPosition.setLocation(this.getX() + mousePoint.getX() - mPosition.getX() ,this.getY() + mousePoint.getY() - mPosition.getY());

			//rotation += 15;
			
			return;
	    }


	}
	

		
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
			rSelectedCorner = null;
			rDeformSelectedCorner = null;
			fixCenter();
			fixBoundaries();
		
	}

	
	public void resetSize(FontRenderContext frc) {
		 Font font = label.getFont();
		//measure number of lines and decide height
			int numLines = stringLines(label.getText()).size();
	     width = (int)(font.getStringBounds(getText(), frc).getWidth());
	     height = (int)(font.getStringBounds(getText(), frc).getHeight()*numLines);
	     
	    //update curve points to fit the size
	     mPoints[0].setLocation(x,y+(height/2));
	     mPoints[1].setLocation(x+(width/3),y+(height/3));
	     mPoints[2].setLocation(x+(width*2/3),y-(height/3));
	     mPoints[3].setLocation(x+width,y+(height/2));
		
	}
	
	
	
	@Override
	public void reSize(int xx, int yy, int ww, int hh){
		//update the new values to the current coordinates
		//get the location of the curve points on the sides
	
		double pLeftFactor = (double) ((mPoints[0].getY()-y))/(double) height;
		double pRightFactor = (double) ((mPoints[3].getY()-y))/(double) height;
		double pOneFactorY = (double) ((mPoints[1].getY()-y))/(double) height;
		double pOneFactorX = (double) ((mPoints[1].getX()-x))/(double) width;
		double pTwoFactorY = (double) ((mPoints[2].getY()-y))/(double) height;
		double pTwoFactorX = (double) ((mPoints[2].getX()-x))/(double) width;

	
		
		switch (rSelectedCornerIndex){
    	
    	case 0:
    		//System.out.println("case "+rSelectedCornerIndex);

    		x += xx;
    		y += yy;
    		width += ww;
    		height += hh;

    		
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		
    		
    		
    		break;
    	case 1:
    		//System.out.println("case "+rSelectedCornerIndex);
    		//x += xx;
    		y += yy;
    		width -= ww;
    		height += hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	case 3:
    		//System.out.println("case "+rSelectedCornerIndex);

    		x += xx;
    		//y += yy;
    		width += ww;
    		height -= hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	case 2:
    		//System.out.println("case "+rSelectedCornerIndex);

    		//x += xx;
    		//y += yy;
    		width -= ww;
    		height -= hh;
    		rSelectedCorner.x += xx;
    		rSelectedCorner.y += yy;
    		break;
    	}
		
		
		
		
				
		
		//fix the location of the curve points 
		
		mPoints[0].setLocation(x, y+(pLeftFactor*(double)height));
		mPoints[3].setLocation(x+width, y+(pRightFactor*(double)height));
		mPoints[1].setLocation(x+(pOneFactorX*(double)width), y+(pOneFactorY*(double)height));
		mPoints[2].setLocation(x+(pTwoFactorX*(double)width), y+(pTwoFactorY*(double)height));
		
		
		fixBoundaries();
		
//		for (int i=0;i<4;i++){
//			mPoints[i].setLocation(mPoints[i].getX()+(xx*(i+1))+(ww/(4-i)), mPoints[i].getY()+(yy/(i+1))+(hh/(4-i)));
//			//mPoints[i].setLocation( mPoints[i].getX()+mPoints[i].getX()*(factorX/(i+1)) , mPoints[i].getY()+mPoints[i].getY()*(factorY/(i+1)) );
//		}

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
		return label.getForeground();
	}

	public void setColor(Color color) {
		label.setForeground(color);
	}
	
	public List<String> stringLines(String text){
		List<String> s = new ArrayList<String>();
		String temp = "";
		
		for (int i = 0; i<text.length();i++){
			if (text.charAt(i) != '\n' && text.charAt(i) != '\r' && i <text.length()-1){
				temp =  temp + text.charAt(i);
			}else{
				temp =  temp + text.charAt(i);
				s.add(temp);
				temp = "";
			}
		}
		
		return s;
	}


	public Spline getSpline() {
		return spline;
	}


	public void setSpline(Spline spline) {
		this.spline = spline;
	}


	public void resetSize() {

		Graphics2D g2 = (Graphics2D)sw.getGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    RenderingHints.VALUE_ANTIALIAS_ON);
	    FontRenderContext frc = g2.getFontRenderContext();
	    resetSize(frc);
		
	}
	
	  public Shape createCurvedText( Shape shape, FontRenderContext frc, Font font, double factorWidth) {
		//based on JHLabs source code http://www.jhlabs.com/

	        
	        font = new Font(font.getFontName(), font.getStyle(), (int) (font.getSize()*Math.abs(factorWidth)));
	        GlyphVector glyphVector = font.createGlyphVector(frc, getText());
	        AffineTransform t = new AffineTransform();
	        
	 
	        GeneralPath result = new GeneralPath();
	        int FLATNESS = 1;
	        PathIterator iterator = new FlatteningPathIterator( shape.getPathIterator( null ), FLATNESS );
	        float nodes[] = new float[6];
	        float moveNode_x = 0, moveNode_y = 0;
	        float lastNode_x = 0, lastNode_y = 0;
	        float thisNode_x = 0, thisNode_y = 0;
	        int iterationType = 0;
	        float next = 0;
	        int currentChar = 0;
	        int glyphCount = glyphVector.getNumGlyphs();
	        boolean stretchToFit = false;
	        boolean rotate = false;
	        boolean repeat = false;
	 
	        if ( glyphCount == 0 )
	            return result;
	 
	        float factor = stretchToFit ? measurePathLength( shape )/(float)glyphVector.getLogicalBounds().getWidth() : 1.0f;
	        float nextAdvance = 0;
	 
	        //Move start of drawing glyphs
	        float pathLength = (float) (measurePathLength( shape )*0.5);
	        float pathFragmentNoText = pathLength - (float)glyphVector.getLogicalBounds().getWidth() - 5.00f;
	        
	       

	        
	        float lengthBehind = 0;
	         
	        while ( lengthBehind < pathFragmentNoText && !iterator.isDone() ) {
	            iterationType = iterator.currentSegment( nodes );
	            switch( iterationType ){
	            case PathIterator.SEG_MOVETO:
	                moveNode_x = lastNode_x = nodes[0];
	                moveNode_y = lastNode_y = nodes[1];
	                
	                result.moveTo( moveNode_x, moveNode_y );
	                nextAdvance = glyphVector.getGlyphMetrics( currentChar ).getAdvance() * 0.5f;
	                next = nextAdvance;
	               
	                break;
	 
	            case PathIterator.SEG_CLOSE:
	                nodes[0] = moveNode_x;
	                nodes[1] = moveNode_y;
	                // Fall into....
	                

	 
	            case PathIterator.SEG_LINETO:
	                thisNode_x = nodes[0];
	                thisNode_y = nodes[1];
	                float dx = thisNode_x-lastNode_x;
	                float dy = thisNode_y-lastNode_y;
	                lengthBehind += (float)Math.sqrt( dx*dx + dy*dy );
	                 
	                lastNode_x = thisNode_x;
	                lastNode_y = thisNode_y;
	                

	                 
	                break;
	            }
	            iterator.next();
	        }
	         
	        while ( currentChar < glyphCount && !iterator.isDone() ) {
	            iterationType = iterator.currentSegment( nodes );
	            switch( iterationType ){
	            case PathIterator.SEG_MOVETO:
	                moveNode_x = lastNode_x = nodes[0];
	                moveNode_y = lastNode_y = nodes[1];
	                result.moveTo( moveNode_x, moveNode_y );
	                nextAdvance = glyphVector.getGlyphMetrics( currentChar ).getAdvance() * 0.5f;
	                next = nextAdvance;
	                break;
	 
	            case PathIterator.SEG_CLOSE:
	                nodes[0] = moveNode_x;
	                nodes[1] = moveNode_y;
	 
	            case PathIterator.SEG_LINETO:
	                thisNode_x = nodes[0];
	                thisNode_y = nodes[1];
	                float dx = thisNode_x-lastNode_x;
	                float dy = thisNode_y-lastNode_y; 
	                float distance = (float)Math.sqrt( dx*dx + dy*dy ); 
	                if ( distance >= next ) { 
	                    float r = 1.0f/distance; 
	                    float angle = (float)Math.atan2( dy, dx ); 
	                    while ( currentChar < glyphCount && distance >= next ) { 
	                    	Shape glyph = glyphVector.getGlyphOutline( currentChar );
	                        Point2D point = glyphVector.getGlyphPosition(currentChar);
	                        float px = (float)point.getX(); 
	                        float py = (float)point.getY();
	                        float x = lastNode_x + next*dx*r;  
	                        float y = lastNode_y + next*dy*r;
	                        float advance = nextAdvance;
	                        nextAdvance = currentChar < glyphCount-1 ? glyphVector.getGlyphMetrics(currentChar+1).getAdvance() * 0.5f : 0;
	                        t.setToTranslation( x, y );
	                        if(rotate){
	                            t.rotate ( angle );
	                            t.rotate( Math.toRadians(180) );
	                        }else{
	                            t.rotate( angle );
	                        }
	                        t.translate( -px-advance, -py );                     
	                        result.append( t.createTransformedShape( glyph ), false );
	                        next += (advance+nextAdvance) * factor;
	                        currentChar++;
	                        if ( repeat )
	                            currentChar %= glyphCount;
	                    }
	                }
	                next -= distance;
	                lastNode_x = thisNode_x;
	                lastNode_y = thisNode_y;
	                break;
	            }
	            iterator.next();
	        }
	 
	        return result;
	    }
	 
	    public float measurePathLength( Shape shape ) {
	    	//based on JHLabs source code http://www.jhlabs.com/

	    	int FLATNESS = 1;
	        PathIterator iterator = new FlatteningPathIterator( shape.getPathIterator( null ), FLATNESS );
	        float nodes[] = new float[6];
	        float moveNode_x = 0, moveNode_y = 0;
	        float lastNode_x = 0, lastNode_y = 0;
	        float thisNode_x = 0, thisNode_y = 0;
	        int iterationType = 0;
	        float total = 0;
	 
	        while ( !iterator.isDone() ) {
	            iterationType = iterator.currentSegment( nodes );
	            switch( iterationType ){
	            case PathIterator.SEG_MOVETO:
	                moveNode_x = lastNode_x = nodes[0];
	                moveNode_y = lastNode_y = nodes[1];
	                break;
	 
	            case PathIterator.SEG_CLOSE:
	                nodes[0] = moveNode_x;
	                nodes[1] = moveNode_y;
	                // Fall into....
	 
	            case PathIterator.SEG_LINETO:
	                thisNode_x = nodes[0];
	                thisNode_y = nodes[1];
	                float dx = thisNode_x-lastNode_x;
	                float dy = thisNode_y-lastNode_y;
	                total += (float)Math.sqrt( dx*dx + dy*dy );
	                lastNode_x = thisNode_x;
	                lastNode_y = thisNode_y;
	                break;
	            }
	            iterator.next();
	        }
	 
	        return total;
	    }
	    

}
