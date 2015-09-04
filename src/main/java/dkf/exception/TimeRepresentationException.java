package dkf.exception;

public class TimeRepresentationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TimeRepresentationException(String message){
		super(message);
	}
	
	public TimeRepresentationException(String message, Throwable cause){
		super(message, cause);
	}
    
	public TimeRepresentationException(Throwable cause){
		super(cause);
	} 

}
