/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Curve extends JInternalFrame implements MouseListener, MouseMotionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 private int[] xs = { 100, 300, 500, 520 , 500, 300, 100, 80, 100};

  private int[] ys = { 100, 80, 100, 350 , 600, 620, 600, 350, 100};
  
  private List<Integer> px = new ArrayList<Integer>();
  private List<Integer> py = new ArrayList<Integer>();
  private GeneralPath path;
  static int openFrameCount = 0;
  static final int xOffset = 30, yOffset = 30;
  

  private int dragIndex = NOT_DRAGGING;

  private final static int NEIGHBORHOOD = 15;

  private final static int NOT_DRAGGING = -1;
  
  private MainWindow mw = null;

//  public static void main(String[] args) {
//	  
//    //(new Curve(null)).setVisible(true);
//  }
//  
  public void run (){
	  //(new Curve()).setVisible(true);
	  this.setVisible(true);
  }

  Curve(MainWindow mw) {
	  super("Screen Shape", 
              true, //resizable
              false, //closable
              false, //maximizable
              false);//iconisiable
	  
	this.mw = mw;
	for (int x:xs){
    	px.add(x);
    }
    for (int y:ys){
    	py.add(y);
    }
    
    setSize(600, 700);
    addMouseListener(this);
    addMouseMotionListener(this);
    
    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.NORTH);
    
    JButton btnClose = new JButton("close");
    btnClose.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent arg0) {
    		setVisible(false);
    	}
    });
    panel.add(btnClose);
//    addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        System.exit(0);
//      }
//    });
  }

  public void paint(Graphics g) {
	  super.paint(g);
    for (int i = 0; i < px.size(); i++) {
      if (i % 2 == 0)
        g.setColor(Color.RED);
      else
        g.setColor(Color.BLACK);
      g.fillOval(px.get(i) - 6, py.get(i) - 6, 12, 12);
    }
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.black);
    path = new GeneralPath();
    
    path.moveTo(px.get(0), py.get(0));
    
    for (int i = 1; i+1<px.size();i+=2){
    	path.curveTo(px.get(i), py.get(i), px.get(i), py.get(i), px.get(i+1), py.get(i+1));	
    }

    g2d.draw(path);
	
    
    //add(new JButton("I am a Button"));

  }

  public void mousePressed(MouseEvent e) {
    dragIndex = NOT_DRAGGING;
    int minDistance = Integer.MAX_VALUE;
    int indexOfClosestPoint = -1;
    for (int i = 0; i < px.size(); i++) {
      int deltaX = px.get(i) - e.getX();
      int deltaY = py.get(i) - e.getY();
      int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
      if (distance < minDistance) {
        minDistance = distance;
        indexOfClosestPoint = i;
      }
    }
    if (minDistance > NEIGHBORHOOD)
      return;

    dragIndex = indexOfClosestPoint;
  }

  public void mouseReleased(MouseEvent e) {
    if (dragIndex == NOT_DRAGGING)
      return;
    px.set(dragIndex, e.getX());
    py.set(dragIndex, e.getY());
    repaint();
    List <Integer> t = new ArrayList<Integer>();
//    for (int i = 0; i<px.size(); i++){
//    	if (i%2==0){
//    		t.add(px.get(i));
//    	}
//    }
    t.add(px.get(dragIndex));
    t.add(py.get(dragIndex));
    t.add((int) path.getBounds2D().getWidth());
    t.add((int) path.getBounds2D().getHeight());
    
    
    dragIndex = NOT_DRAGGING;
    mw.updateText(t);
    mw.createCustomWindow();
  }
  
  public int[] getxs(){
	  return xs;
  }
  public int[] getys(){
	  return ys;
  }

  public void mouseDragged(MouseEvent e) {
    if (dragIndex == NOT_DRAGGING)
      return;

    if ( e.getY() < 70 || e.getY()>this.getHeight() || e.getX()>this.getWidth() || e.getX()<10)
    	return;
    
    px.set(dragIndex, e.getX());
    py.set(dragIndex, e.getY());
    repaint();
  }

  public void mouseClicked(MouseEvent e) {
	    int minDistance = Integer.MAX_VALUE;
	    int indexOfClosestPoint = -1;

	    for (int i = 0; i < px.size(); i+=2) {
	      int deltaX = px.get(i) - e.getX();
	      int deltaY = py.get(i) - e.getY();
	      int distance = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	      if (distance < minDistance) {
	        minDistance = distance;
	        indexOfClosestPoint = i;
	      }
	    }
	    
	    if (e.isShiftDown()){
	    	if (px.size()<2){
	    		return;
	    	}
			px.remove(indexOfClosestPoint);
			py.remove(indexOfClosestPoint);
			if (indexOfClosestPoint < px.size()){
				px.remove(indexOfClosestPoint);
				py.remove(indexOfClosestPoint);
			} else if (indexOfClosestPoint > 0){
				px.remove(indexOfClosestPoint-1);
				py.remove(indexOfClosestPoint-1);
			}
			repaint();
			return;
		}

	    
	    if (minDistance < NEIGHBORHOOD)
	      return;
	    
	    double ang = Math.atan2( px.get(indexOfClosestPoint) - e.getX(), py.get(indexOfClosestPoint) - e.getY() );
	    
	    int indexOfSecondPoint = indexOfClosestPoint;
	    if (indexOfClosestPoint > 1 && indexOfClosestPoint < px.size()-1){
	    	double ang1 = Math.atan2( px.get(indexOfClosestPoint) - px.get(indexOfClosestPoint-2), py.get(indexOfClosestPoint) - px.get(indexOfClosestPoint-2) );
	    	double ang2 = Math.atan2( px.get(indexOfClosestPoint) - px.get(indexOfClosestPoint+2), py.get(indexOfClosestPoint) - px.get(indexOfClosestPoint+2) );
		    
	    	if (Math.abs(ang1 - ang) < Math.abs(ang2 - ang)){
	    		System.out.println("indexOfSecondPoint - ");
	    		indexOfSecondPoint = indexOfClosestPoint-2;
	    	 }else{
		    	  System.out.println("indexOfSecondPoint + ");
		    	  indexOfSecondPoint = indexOfClosestPoint+2;  
		      }
	    }
	    
	    System.out.println("indexOfClosestPoint " + indexOfClosestPoint);
	    System.out.println("indexOfSecondPoint " + indexOfSecondPoint);
	    
//	    int dX = Math.abs(px.get(indexOfClosestPoint) + px.get(indexOfSecondPoint)) /2;
//	    int dY = Math.abs(py.get(indexOfClosestPoint) + py.get(indexOfSecondPoint)) /2;
	    int dX = Math.abs(e.getX() + px.get(indexOfSecondPoint)) /2;
	    int dY = Math.abs(e.getY() + py.get(indexOfSecondPoint)) /2;
	    if (indexOfSecondPoint > indexOfClosestPoint){
	    	indexOfClosestPoint = indexOfSecondPoint;
	    	System.out.println("change");
	    }else{
	    	dX = Math.abs(e.getX() + px.get(indexOfClosestPoint)) /2;
		    dY = Math.abs(e.getY() + py.get(indexOfClosestPoint)) /2;
	    }
	    px.add(indexOfClosestPoint, e.getX());
	    py.add(indexOfClosestPoint, e.getY());
	    px.add(indexOfClosestPoint+1, dX);
	    py.add(indexOfClosestPoint+1, dY);
	   
	    

	    repaint();
	    
	    
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mouseMoved(MouseEvent e) {
  }

public GeneralPath getPath() {
	 GeneralPath path = new GeneralPath();
	    
	    path.moveTo(px.get(0), py.get(0));
	    
	    for (int i = 1; i+1<px.size();i+=2){
	    	path.curveTo(px.get(i), py.get(i), px.get(i), py.get(i), px.get(i+1), py.get(i+1));	
	    }
	return path;
}


}