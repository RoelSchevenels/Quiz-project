package tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel{
	private static final long serialVersionUID = -759001245054709278L;
	private int[][] grid;
	private int width, height;
	private int ts;
	private Color[] col;
	
	public GamePanel(int tilesize, int height, int width)
	{
		System.out.printf("%d wide, %d high\n",width,height);
		this.width = width;
		this.height = height;
		grid = new int[width][height];
		ts = tilesize;
		col = new Color[2];
		col[0] = new Color(0x69D2E7);
		col[1] = new Color(0xF38630);
		setBackground(new Color(0xE0E4CC));
		setSize(ts*width, ts*height);
	}
	
	public void update(int[][] grid)
	{
		this.grid = grid;
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(grid[i][j] > 0){
					g.setColor(col[grid[i][j] -1]);
					g.fillRect(i * ts, j * ts, ts, ts);
				}
			}
		}
	}
}
