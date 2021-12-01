package exceptions;

public class MetricsEngineException extends ApplicationException {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 9072602878295042955L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public MetricsEngineException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public MetricsEngineException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MetricsEngineException() {
	}

	public MetricsEngineException(int errorCode, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(errorCode, cause, enableSuppression, writableStackTrace);
	}

	public MetricsEngineException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public MetricsEngineException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MetricsEngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetricsEngineException(Throwable cause) {
		super(cause);
	}

	@Override
	protected void generateMessage() {
		// TODO Auto-generated method stub
		
	}
}
