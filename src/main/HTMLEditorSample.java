/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */
package main;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.HTMLEditor;

public class HTMLEditorSample {
	
	private static HTMLEditor htmlEditor;

    private static void initAndShowGUI() {
        JFrame frame = new JFrame("HTML Editor");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        //frame.setSize(600, 400);
        Rectangle r = new Rectangle();
        r.setBounds(1200, 600, 600, 400);
        frame.setBounds(r);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                htmlEditor = new HTMLEditor();
                Scene scene = new Scene(htmlEditor);
                fxPanel.setScene(scene);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }
    public void run(){
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }
    
    public String getText(){
    	return htmlEditor.getHtmlText();
    }
    public void setText(String s){
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
              //javaFX operations should go here
            	htmlEditor.setHtmlText(s);
            }
       });
    	
    }
}