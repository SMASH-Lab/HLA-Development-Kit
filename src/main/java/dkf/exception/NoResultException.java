package dkf.exception;

public class NoResultException extends RuntimeException {
	
private static final long serialVersionUID = 1L;
	
	public NoResultException(String message) {
		super(message);
	}
	
	public NoResultException(String message, Throwable cause) {
		super(message, cause);
	}
    
	public NoResultException(Throwable cause) {
		super(cause);
	} 
	

}
