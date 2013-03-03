package Protocol.submits;

public class TetrisSubmit extends Submit {
	private static final long serialVersionUID = 1201029080815867510L;
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
