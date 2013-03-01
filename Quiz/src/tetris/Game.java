package tetris;

import java.awt.Dimension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

public class Game{
	private GamePanel panel;
	private JPanel parentPanel;
	private int[][] grid;
	private int player;
	private int pieces;
	private int interval;
	private Piece p;
	private boolean playing;
	private ExecutorService ex;
	
	public Game(JPanel parentPanel)
	{
		grid = new int[10][20];
		player = 1;
		interval = 500;
		playing = false;
		ex = Executors.newCachedThreadPool();
		
		this.parentPanel = parentPanel;
		makeGUI();
	}
	
	public void makeGUI()
	{
		int margin = 10;
		int maxwidth = parentPanel.getWidth() - margin;
		int maxheight = parentPanel.getHeight() - margin;
		int xtiles = 10;
		int ytiles = 20;
		int tilesize = Math.min((maxwidth/xtiles), (maxheight/ytiles));
		panel = new GamePanel(tilesize,ytiles,xtiles);
		panel.setPreferredSize(new Dimension(xtiles*tilesize,ytiles*tilesize));
		parentPanel.add(panel);
		System.out.printf("w:%d h:%d\n",panel.getWidth(),panel.getHeight());
	}

	private class Core implements Runnable{
		@Override
		public void run()
		{
			while(playing){
				try {
					Thread.sleep(interval);
					tick();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void tick()
		{
			if(p.predict(grid, 0, 1)){
				grid = p.apply(grid);
				clearLines();
				pieces--;
				if(pieces == 0)
					playing = false;
				else
					p = new Piece(3,2,player);
			}else{
				p.move(0,1);
				panel.update(p.apply(grid));
			}
		}
	}
	
	public void left()
	{
		if(playing && !p.predict(grid, -1, 0)){
			p.move(-1, 0);
			panel.update(p.apply(grid));
		}
	}
	
	public void right()
	{
		if(playing && !p.predict(grid, 1, 0)){
			p.move(1, 0);
			panel.update(p.apply(grid));
		}
	}
	public void down()
	{
		if(playing && !p.predict(grid, 0, 1)){
			p.move(0, 1);
			panel.update(p.apply(grid));
		}
	}
	public void rotate()
	{
		int[] kicks = {0, -1, 1};	// De relatieve posities waarnaar geduwd kan worden als het niet past
		for(int i=0;i<3;i++){
			if(!p.rotate().predict(grid, kicks[i], 0)){
				p = p.rotate();		
				panel.update(p.apply(grid));
				break;
			}
		}
		
	}
	
	public void clearLines()
	{
		for(int line=0;line<grid[0].length;line++) {
			boolean full = true;
			for (int col=0;col<grid.length;col++) {
				if(grid[col][line] == 0){
					full = false;
					break;
				}
			}
			if(full){
				for (int col=0;col<grid.length;col++) {
					for (int i=line;i>0;i--) {
						grid[col][i] = grid[col][i-1];
					}
				}
				panel.update(grid);
			}
		}
	}
	
	private boolean setPlayer(int player)
	{
		if(player == 1 || player == 2){
			this.player = player;
			return true;
		}else{
			System.out.println("Ongeldig spelersnummer");
			return false;
		}
	}
	public void start(String player, int pieces)
	{
		int num = Integer.parseInt(player.substring(6));
		if(setPlayer(num)){
			this.pieces = pieces;
			playing = true;
			p = new Piece(3,2,num);
			ex.execute(new Core());
		}
	}
}
