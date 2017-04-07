/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String fileName;
	private BufferedImage image;
	private BufferedImage[] imageChunks;
	private boolean shouldDraw = false;
	private BufferedImage imageToDraw;
	private Shape shadow;
	private Point[] q = new Point[4];

	Image(String path, ShapedWindow sw, int type) {
		super(0, sw, Item.IMAGE);
		height = 100;
		try {
			image = ImageIO.read(new File(path));
			double ratio = (double)image.getWidth()/(double)image.getHeight();
			System.out.println("width "+ image.getWidth()+"ratio "+ratio+"height "+image.getHeight());
			width = ratio * height;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
		
		
	    fixBoundaries();
	    cx  = x + (width/2);
	    cy = y + (height/2);
	    
	    update();
	    
	}
	
	@Override
	public void paint(Graphics g){
		
		//super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
	     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	     
		 //AffineTransform at = AffineTransform.getTranslateInstance(x, y+height); 
	     
	     g2.translate(this.translateX,this.translateY);
	     g2.rotate(Math.toRadians(rotation),cx*scale,cy*scale);
	     g2.scale(this.scale , this.scale);
	   
	  
		     if (showShadow){
					//draw shadow
		    	Color c = g2.getColor();
		    	g2.setColor(getShadowColor());
		    	
		    	 int margin = (int)(10/scale);

		         //Point[] q = new Point[4];
			     q[0] = new Point((int) rDeform[0].getX()+margin,(int) rDeform[0].getY()+margin);
			     q[1] = new Point((int) rDeform[1].getX(),(int) rDeform[1].getY()+margin);
			     q[2] = new Point((int) rDeform[2].getX(),(int) rDeform[2].getY());
			     q[3] = new Point((int) rDeform[3].getX()+margin,(int) rDeform[3].getY());
			     
						 
				 float st = (100f-(float)getShadowTransparent())/100f;
				 g2.setComposite(AlphaComposite.SrcOver.derive(st));
				 
				 Rectangle r = new Rectangle((int)x, (int)y, (int)Math.abs(width), (int)Math.abs(height));
		    	 GeneralPath p = new GeneralPath();
		    	 p.moveTo(q[0].x, q[0].y);
		    	 p.lineTo(q[1].x, q[1].y);
		    	 p.lineTo(q[2].x, q[2].y);
		    	 p.lineTo(q[3].x, q[3].y);
		    	 p.lineTo(q[0].x, q[0].y);
		    	 p.closePath();
				 this.shadow = createShadow(p);
				 
				 g2.fill(this.shadow);
				 
				 
				 g2.setColor(c);
			     g2.setComposite(AlphaComposite.SrcOver);
			     
				}

		 		g.drawImage(this.imageToDraw, (int)x, (int)y, (int) width, (int) height, null);

	     
		if (selected){
			drawCorners(g);
		}
		 else if (isSelectedStage2()){
		    	drawDeformCorners(g); 
		    	//System.out.println("draw stage2");
		    	
		     }

		
	     g2.scale(1/this.scale , 1/this.scale);
	     g2.rotate(-Math.toRadians(rotation), cx*scale,cy*scale);
	     g2.translate(-this.translateX,-this.translateY);
	     
	}
	
	public void update(){

			         imageToDraw = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB); //Image array to hold image chunks
			         Graphics cg = imageToDraw.createGraphics();
			         Graphics2D cg2 = (Graphics2D) cg;
				     cg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				    

			        
			         int margin = (int)(10/scale);

			         //Point[] q = new Point[4];
				     q[0] = new Point((int) rDeform[0].getX()+margin,(int) rDeform[0].getY()+margin);
				     q[1] = new Point((int) rDeform[1].getX(),(int) rDeform[1].getY()+margin);
				     q[2] = new Point((int) rDeform[2].getX(),(int) rDeform[2].getY());
				     q[3] = new Point((int) rDeform[3].getX()+margin,(int) rDeform[3].getY());

				     
				     
				     
				     BufferedImage resized = new BufferedImage((int)width, (int)height, image.getType());
				     Graphics2D gimage = resized.createGraphics();
				     gimage.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				         RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				     gimage.drawImage(image, 0, 0, (int)width, (int)height, 0, 0, image.getWidth(),
				         image.getHeight(), null);
				     gimage.dispose();
				     //mage = resized;
				     
				     
				     
				     updateMatrix(q);
			         for (int xx = 0; xx < width; xx++) {
			             for (int yy = 0; yy < height; yy++) {
			                 //Initialize the image array with image chunks
			            	 
			            	 
			            	 Point p = getPointInQuadrilateral(new Point((int)x + xx,(int)y+yy));
			 	    		
			 	    	
			 	    		
			            	  
			            	 cg2.drawImage(resized, p.x-(int)x, p.y-(int)y , p.x+1-(int)x , p.y+1-(int)y , xx , yy , xx+1, yy+1, null);
		            	 
			             }
			         }

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		
		

		 Point mousePoint = fixMouseRotationPrecpective(cx, cy, me.getPoint());
		 if (this.contains(mousePoint)){
				if (sw.getSelectedItem() == null){
					//System.out.println("stage1");

					sw.setSelectedItem(this);
					selected = true;
				} else if (sw.getSelectedItem() == this){
					//System.out.println("stage2");
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

						//fixBoundaries();

					}
				}
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
	    	double xx,yy;
	    	//Point2D mousePoint = fixMousePositionPrecpective(x+(width/2), y+(height/2), me.getPoint());
			

	    	xx = ((double)mousePointRotation.getX()-(double)rDeformSelectedCorner.getX())/width;
	    	yy = ((double)mousePointRotation.getY()-(double)rDeformSelectedCorner.getY())/height;


	    	//System.out.println("xx "+xx+" yy "+yy);
	    	//ww = (int) (mousePointRotation.getX()-rDeformSelectedCorner.getX())*-1;
   		 	//hh = (int) (mousePointRotation.getY()-rDeformSelectedCorner.getY())*-1;
	    	DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].setLocation(DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getX()+xx, DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].getY()+yy);
//   		 	DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].x += xx;
//   		 	DeformPointsRelativeToCorners[rDeformSelectedCornerIndex].y += yy;
   		 fixBoundaries();
   		fixDeformBoundaries();
   		update();
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
public void mousePressed(MouseEvent me) {
	if (sw.isDrag())
   	return;
	
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
   
   
   if(sw.getSelectedItem() == this)
   {
		sw.setDrag(true);
		mPosition = mousePoint;
		sw.setDraggedItem(this);
		lastPosition.setLocation(this.getX() + mousePoint.getX() - mPosition.getX() ,this.getY() + mousePoint.getY() - mPosition.getY());

		//rotation += 15;
		
		return;
   }


 
//	if(this.contains(mousePointRotation))
//   {
//		sw.setDrag(true);
//		mPosition = mousePoint;
//		sw.setDraggedItem(this);
//		lastPosition.setLocation(this.getX() + mousePoint.getX() - mPosition.getX() ,this.getY() + mousePoint.getY() - mPosition.getY());
//
//		//rotation += 15;
//		
//		return;
//   }


}


	

@Override
public void mouseReleased(MouseEvent arg0) {
	if (rSelectedCorner != null || rDeformSelectedCorner!= null)
		update();
	
		rSelectedCorner = null;
		rDeformSelectedCorner = null;
		fixCenter();
		fixBoundaries();
	
}
}
