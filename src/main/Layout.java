/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

import javafx.scene.shape.Line;

public class Layout implements Serializable{
	private double rotation;
	private Rectangle rect;
	private Point center;
	
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	
	
}
