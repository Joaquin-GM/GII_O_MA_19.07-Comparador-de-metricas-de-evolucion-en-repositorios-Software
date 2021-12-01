package exceptions;

public class GUIException extends ApplicationException{

	public static final int MAIN_INITIALIZATION_ERROR = 1;
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 4205120991101002141L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public GUIException(int errorCode) {
		super(errorCode);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public GUIException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public GUIException() {}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GUIException(int errorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(errorCode, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 * @param cause
	 */
	public GUIException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public GUIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 * @param cause
	 */
	public GUIException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param cause
	 */
	public GUIException(Throwable cause) {
		super(cause);
	}

	@Override
	protected void generateMessage() {
		switch (code) {
		case MAIN_INITIALIZATION_ERROR:
			message = "Main view initialization error.";
			break;
		default:
			break;
		}
	}
}
