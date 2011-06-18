
package platzerworld.kegeln.common.appengine.exception;

/**
 * @author platzerg
 */
public class CookieException extends AppEngineException {
	
	private static final long serialVersionUID = -5856306428375868862L;

	public CookieException(String message) {
		super(message);
	}

	public CookieException(Exception innerException) {
		super(innerException);
	}

}
