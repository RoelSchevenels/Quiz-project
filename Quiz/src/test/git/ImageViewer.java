package test.git;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ImageViewer extends JFrame{
	public ImageViewer(BufferedImage image)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel imgLabel = new JLabel(new ImageIcon(image));
		add(imgLabel);
		pack();
		setVisible(true);
	}
	
	public ImageViewer(String filename) throws IOException
	{
		this(ImageIO.read(new File(filename)));		
	}
	
	public ImageViewer(ImageIcon image)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(image);
		add(imgLabel);
		pack();
		setVisible(true);
	}
}
