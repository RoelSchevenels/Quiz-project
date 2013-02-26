package Protocol.exceptions;

public class IdRangeException extends Exception{
	private static final long serialVersionUID = -7622339220961596358L;
	private String message;
	
	public IdRangeException(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}
