package platzerworld.kegeln.common.appengine.exception;

/**
 * @author platzerg
 */
public class HttpRequestException extends AppEngineException {
	
	private static final long serialVersionUID = 6569433046017847714L;

	public HttpRequestException(String message) {
		super(message);
	}
	
	public HttpRequestException(Exception innerException) {
		super(innerException);
	}

}
