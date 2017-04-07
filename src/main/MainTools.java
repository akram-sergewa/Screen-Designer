/*
 * Screen designer
 * project for a Msc in advanced computing
 * University of Bristol 
 * Akram Sergewa
 */

package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JEditorPane;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainTools extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean shadow;

	private Main internalFrame;
	
	private String currentFName;
	
	 static int openFrameCount = 0;
	    static final int xOffset = 30, yOffset = 30;

		private JButton btnAddImage;

		private ImageIcon btnAddImageIcon;
		private ImageIcon btnAddImageIconPressed;
		
		private ImageIcon btnAddSplineIcon;
		private ImageIcon btnAddSplineIconPressed;
		
		private ImageIcon btnAddTextIcon;
		private ImageIcon btnAddTextIconPressed;
		
		private ImageIcon btnGridIcon;
		private ImageIcon btnGridIconPressed;
		
		private ImageIcon btnBrushIcon;
		private ImageIcon btnBrushIconPressed;
		
		private ImageIcon btnScreenIcon;
		private ImageIcon btnScreenIconPressed;
		private JButton btnAddText;
		private JButton btnAddSpline;
		private JButton btnGrid;
		private JButton btnScreen;
		private JPanel panel;
		private JButton btnBrush;
		private JLabel hoverLabel;
		
	    
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
////		EventQueue.invokeLater(new Runnable() {
////			public void run() {
////				try {
////					TextEditor frame = new TextEditor();
////					frame.setVisible(true);
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
////			}
////		});
//	}

	/**
	 * Create the frame.
	 */
	public MainTools(Main Main) {
		super("TOOLS", 
				  false, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		
		this.internalFrame = Main;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//btnAdd = new JButton("add");

		BufferedImage btnAddImageImage = null;
		BufferedImage btnAddImageImagePressed = null;
		BufferedImage btnAddTextImage = null;
		BufferedImage btnAddTextImagePressed = null;
		BufferedImage btnAddSplineImage = null;
		BufferedImage btnAddSplineImagePressed = null;
		BufferedImage btnGridImage = null;
		BufferedImage btnGridImagePressed = null;
		BufferedImage btnScreenImage = null;
		BufferedImage btnScreenImagePressed = null;
		BufferedImage btnBrushImage = null;
		BufferedImage btnBrushImagePressed = null;
		try {
			btnAddImageImage = ImageIO.read(new File("Resources/image_icon.jpg"));
			btnAddImageImagePressed = ImageIO.read(new File("Resources/image_icon2.jpg"));
			
			btnAddTextImage = ImageIO.read(new File("Resources/text_icon.jpg"));
			btnAddTextImagePressed = ImageIO.read(new File("Resources/text_icon2.jpg"));
			
			btnAddSplineImage = ImageIO.read(new File("Resources/spline_icon.jpg"));
			btnAddSplineImagePressed = ImageIO.read(new File("Resources/spline_icon2.jpg"));
			
			btnGridImage = ImageIO.read(new File("Resources/grid_icon.jpg"));
			btnGridImagePressed = ImageIO.read(new File("Resources/grid_icon2.jpg"));
			
			btnScreenImage = ImageIO.read(new File("Resources/screen_icon.jpg"));
			btnScreenImagePressed = ImageIO.read(new File("Resources/screen_icon2.jpg"));
			
			btnBrushImage = ImageIO.read(new File("Resources/brush_icon.jpg"));
			btnBrushImagePressed = ImageIO.read(new File("Resources/brush_icon2.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("can't find image");
			e1.printStackTrace();
		}
		

		btnAddImageIcon = new ImageIcon(btnAddImageImage);
		btnAddImageIconPressed = new ImageIcon(btnAddImageImagePressed);
		
		btnAddSplineIcon = new ImageIcon(btnAddSplineImage);
		btnAddSplineIconPressed = new ImageIcon(btnAddSplineImagePressed);
		
		btnAddTextIcon = new ImageIcon(btnAddTextImage);
		btnAddTextIconPressed = new ImageIcon(btnAddTextImagePressed);
		
		btnGridIcon = new ImageIcon(btnGridImage);
		btnGridIconPressed = new ImageIcon(btnGridImagePressed);
		
		btnScreenIcon = new ImageIcon(btnScreenImage);
		btnScreenIconPressed = new ImageIcon(btnScreenImagePressed);
		
		btnBrushIcon = new ImageIcon(btnBrushImage);
		btnBrushIconPressed = new ImageIcon(btnBrushImagePressed);
		
		panel = new JPanel(){
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
		panel.setBounds(0, 0, 371, 573);
		contentPane.add(panel);
		panel.setLayout(null);
		
		this.hoverLabel = new JLabel();
		hoverLabel.setBackground(Color.ORANGE);
		hoverLabel.setForeground(Color.white);
		hoverLabel.setOpaque(true);
		
		panel.add(hoverLabel);
		
		btnAddImage = new JButton(btnAddImageIcon);
		btnAddImage.setBounds(25, 180, 140, 140);
		panel.add(btnAddImage);
		btnAddImage.setBorder(null);
		btnAddImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnAddImage.getX()+me.getX(),btnAddImage.getY()+me.getY()-20,100,20);
            		hoverLabel.setVisible(true);
            		hoverLabel.setText("Add Image");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		
		btnAddText = new JButton(btnAddTextIcon);
		btnAddText.setBounds(25, 333, 140, 140);
		panel.add(btnAddText);
		btnAddText.setBorder(null);
		btnAddText.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnAddText.getX()+me.getX(),btnAddText.getY()+me.getY()-20,100,20);
            		hoverLabel.setVisible(true);
            		hoverLabel.setText("Add Text");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		
		btnScreen = new JButton(btnScreenIcon);
		btnScreen.setBounds(25, 27, 140, 140);
		panel.add(btnScreen);
		btnScreen.setBorder(null);
		btnScreen.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnScreen.getX()+me.getX(),btnScreen.getY()+me.getY()-20,100,20);
            		hoverLabel.setVisible(true);
            		hoverLabel.setText("Edit screen");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		

		
		btnAddSpline = new JButton(btnAddSplineIcon);
		btnAddSpline.setBounds(177, 27, 140, 140);
		panel.add(btnAddSpline);
		btnAddSpline.setBorder(null);
		btnAddSpline.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnAddSpline.getX()+me.getX(),btnAddSpline.getY()+me.getY()-20,100,20);
            		hoverLabel.setVisible(true);
            		
            		hoverLabel.setText("Add Spline");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		
		btnGrid = new JButton(btnGridIcon);
		btnGrid.setBounds(177, 177, 140, 140);
		panel.add(btnGrid);
		btnGrid.setBorder(null);
		btnGrid.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnGrid.getX()+me.getX()-80,btnGrid.getY()+me.getY()-20,150,20);
            		hoverLabel.setVisible(true);
            		hoverLabel.setText("Enable/Disable Grid");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		
		btnBrush = new JButton((Icon) btnBrushIcon);
		btnBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalFrame.addBrush();
			}
		});
		btnBrush.setBorder(null);
		btnBrush.setBounds(177, 333, 140, 140);
		btnBrush.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
            	 if (SwingUtilities.isRightMouseButton(me)){
					
            		hoverLabel.setBounds(btnBrush.getX()+me.getX(),btnBrush.getY()+me.getY()-20,100,20);
            		hoverLabel.setVisible(true);
            		hoverLabel.setText("Add Brush");
            		hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            	 }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
            	hoverLabel.setVisible(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            	this.mouseClicked(me);
            }

            
        });
		
		panel.add(btnBrush);
		
		btnGrid.addMouseListener(new MouseAdapter() {
			  public void mousePressed(MouseEvent e) {
				  btnGrid.setIcon((Icon) btnGridIconPressed);
				  }

				  public void mouseReleased(MouseEvent e) {
					  btnGrid.setIcon((Icon) btnGridIcon);
				  }
				});
		
				btnGrid.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						internalFrame.showHideGrid();
					
						
					}
				});
		
		btnAddSpline.addMouseListener(new MouseAdapter() {
			  public void mousePressed(MouseEvent e) {
				  btnAddSpline.setIcon((Icon) btnAddSplineIconPressed);
				  }

				  public void mouseReleased(MouseEvent e) {
					  btnAddSpline.setIcon((Icon) btnAddSplineIcon);
				  }
				});
		
				btnAddSpline.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						internalFrame.addSpline();
					
						
					}
				});
		
		btnScreen.addMouseListener(new MouseAdapter() {
			  public void mousePressed(MouseEvent e) {
				  btnScreen.setIcon((Icon) btnScreenIconPressed);
				  }

				  public void mouseReleased(MouseEvent e) {
					  btnScreen.setIcon((Icon) btnScreenIcon);
				  }
				});
		
				btnScreen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						internalFrame.showCurve();
					
						
					}
				});
		
		btnAddText.addMouseListener(new MouseAdapter() {
			  public void mousePressed(MouseEvent e) {
				  btnAddText.setIcon((Icon) btnAddTextIconPressed);
				  }

				  public void mouseReleased(MouseEvent e) {
					  btnAddText.setIcon((Icon) btnAddTextIcon);
				  }
				});
		
				btnAddText.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						internalFrame.addText();
					
						
					}
				});
		
		
		
		//listeners
		btnAddImage.addMouseListener(new MouseAdapter() {
			  public void mousePressed(MouseEvent e) {
				  btnAddImage.setIcon((Icon) btnAddImageIconPressed);
				  }

				  public void mouseReleased(MouseEvent e) {
					  btnAddImage.setIcon((Icon) btnAddImageIcon);
				  }
				});

		btnAddImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				internalFrame.addImage();
			
				
			}
		});
		
		
		
		

	}
	

	
	


	
	
	
	public void run(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
//				try {
//					setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		});
	}
}
