package Protocol;

public class ExceptionResponse extends Response {
	private static final long serialVersionUID = -7275174109536120357L;
	private String exceptionMessage;
	
	public ExceptionResponse(int RequestId, String message) {
		super(RequestId);
		exceptionMessage = message;
	}
	
	//
	//Als er bij een Request een error voordoet
	//Stuurt ge een Response exception Response met wat voor soort exception
	//En een message
	public String getExceptionMessage() {
		return exceptionMessage;
	}
}
