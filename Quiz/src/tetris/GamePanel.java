package tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel{
	private static final long serialVersionUID = -759001245054709278L;
	private int[][] grid;
	private int width, height;
	private int ts;
	private Color[] col;
	private Color gridcol;
	
	public GamePanel(int tilesize, int height, int width)
	{
		this.width = width;
		this.height = height;
		grid = new int[width][height];
		ts = tilesize;
		col = new Color[2];
		col[0] = new Color(0x69D2E7);
		col[1] = new Color(0xF38630);
		gridcol = new Color(0x66FFFFFF, true);
		setBackground(new Color(0xE0E4CC));
	}
	
	public void update(int[][] grid)
	{
		this.grid = grid;
		repaint();
		System.out.println("update:" + SwingUtilities.isEventDispatchThread());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		System.out.println("paint: "+ SwingUtilities.isEventDispatchThread());
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(grid[i][j] > 0){
					g.setColor(col[grid[i][j] -1]);
					g.fillRect(i * ts, j * ts, ts, ts);
				}
				g.setColor(gridcol);
				g.drawRect(i * ts, j * ts, ts, ts);
			}
		}
	}
}
