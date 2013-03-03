package Protocol.submits;

public class IdentifiedSubmit extends Submit {
	/**
	 * @author Roel
	 */
	private static final long serialVersionUID = -2568659756651757831L;
	private int connectionId;
	
	public void setConnectionId(int id)
	{
		this.connectionId = id;
	}
	
	public int getConnectionId()
	{
		return connectionId;
	}
}
