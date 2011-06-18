
package platzerworld.kegeln.common.appengine.exception;

/**
 * @author platzerg
 */
public class AppEngineException extends Exception {
	
	private static final long serialVersionUID = -5428600165482531506L;

	public AppEngineException(String message) {
		super(message);
	}
	
	public AppEngineException(Exception innerException) {
		super(innerException);
	}
	
}
