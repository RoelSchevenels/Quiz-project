package tetris;

import java.util.Arrays;

public class Piece {
	private static final int[][][] tetrominoes = {	
		{{0,1},{1,1},{2,1},{3,1}},	// I
     	{{0,0},{0,1},{1,1},{1,0}},	// O
     	{{0,1},{1,1},{2,1},{2,0}},	// L
     	{{0,1},{1,1},{2,1},{0,0}},	// J
     	{{1,0},{2,0},{0,1},{1,1}},	// S
     	{{0,0},{1,0},{1,1},{2,1}},	// Z
     	{{1,0},{0,1},{1,1},{2,1}}	// T
	};
	private int[][] shape;
	private int x;
	private int y;
	private int player;
	
	public Piece(int x, int y, int player)
	{
		shape = tetrominoes[(int)(Math.random()*tetrominoes.length)];	// Willekeurige vorm kiezen
		this.x = x;
		this.y = y;
		this.player = player;
		System.out.printf("Piece created at (%d,%d)\n", x, y);
	}
	
	public int[][] apply(int[][] grid) 	//Zichelf toevoegen aan grid
	{
		grid = deepcopy(grid);
		for(int i=0;i<shape.length;i++){
			grid[x+shape[i][0]][y+shape[i][1]] = player; 
		}
		return grid;
	}
	
	public void move(int xmove, int ymove)
	{
		this.x += xmove;
		this.y += ymove;
	}
	
	public void setShape(int[][] shape)
	{
		this.shape = shape;
	}
	
	public boolean predict(int[][] grid, int xdiff, int ydiff)
	{
		int x = this.x + xdiff;
		int y = this.y + ydiff;
		
		for(int i=0;i<shape.length;i++){
			if(x + shape[i][0] >= grid.length || x + shape[i][0] < 0 || y + shape[i][1] >= grid[0].length || y + shape[i][1]< 0){
				return true;
			}
			if(grid[x+shape[i][0]][y+shape[i][1]] != 0){
				return true;
			}
		}
		return false;
	}
	
	public Piece rotate()
	{
		int dimension = deepmax(this.shape);
		int[][] newshape = new int[shape.length][shape[0].length];
		
		for (int i = 0; i<shape.length; i++){	//blocks
			int x = shape[i][0];
			int y = shape[i][1];
			newshape[i][0] = dimension-y;
			newshape[i][1] = x;
		}
		
		Piece p = new Piece(this.x, this.y,this.player);
		p.setShape(newshape);
		for(int[] j: newshape)
			System.out.print(j[0] + "," + j[1] + "   ");
		System.out.println();
		return p;
	}
	
	private int deepmax(int[][] array)
	{
		int max = 0;
		for(int[] a: array)
			for(int b: a)
				max = (b > max) ? b : max;
		return max;
	}
	
	private int[][] deepcopy(int[][] original) {
	    int[][] copy = new int[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        copy[i] = Arrays.copyOf(original[i], original[i].length);
	    }
	    return copy;
	}
}
