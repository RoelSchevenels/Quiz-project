package Protocol.submits;

public class TetrisStartSubmit extends Submit{
	/**
	 * @author Roel
	 */
	private static final long serialVersionUID = 6820216682689456904L;
	private String player;
	private int pieces;
	
	public TetrisStartSubmit(String player, int pieces)
	{
		this.player = player;
		this.pieces = pieces;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public void setPlayer(String player)
	{
		this.player = player;
	}
	
	public int getPieces()
	{
		return pieces;
	}
	
	public void setPieces(int pieces)
	{
		this.pieces = pieces;
	}
}
