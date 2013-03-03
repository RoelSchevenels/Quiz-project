 package Protocol.submits;

public class TetrisSubmit extends IdentifiedSubmit {
	/**
	 * @author Roel
	 */
	private static final long serialVersionUID = -2695430118916850507L;
	private char movement;

	public char getMovement()
	{
		return movement;
	}
	
	public void setMovement(char movement)
	{
		this.movement = movement;
	}

	public TetrisSubmit(char movement)
	{
		super();
		this.movement = movement;
	}
}
