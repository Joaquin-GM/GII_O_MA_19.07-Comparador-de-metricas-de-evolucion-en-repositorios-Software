package exceptions;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class MetricsServiceException extends ApplicationException {
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * There is already a metric profile with the same name.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public static final int METRICP_ROFILE_NAME_IN_USE = 1;

	public static final int IMPORT_ERROR = 2;
	
	public static final int EXPORT_ERROR = 3;
	
	public static final int CORRUPTED = 4;
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public MetricsServiceException(int errorCode) {
		super(errorCode);
	}


	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public MetricsServiceException(String message) {
		super(message);
	}


	public MetricsServiceException() {}


	public MetricsServiceException(int errorCode, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(errorCode, cause, enableSuppression, writableStackTrace);
	}


	public MetricsServiceException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}


	public MetricsServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


	public MetricsServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetricsServiceException(Throwable cause) {
		super(cause);
	}


	@Override
	protected void generateMessage() {
		switch (code) {
		case METRICP_ROFILE_NAME_IN_USE:
			message = "The name is already in use for another profile.";
			break;
		case IMPORT_ERROR:
			message = "An error occurred during the import";
			break;
		case EXPORT_ERROR:
			message = "An error occurred during the export";
			break;
		case CORRUPTED:
			message = "File corrupted";
			break;
		default:
			message = "Unknown MetricsService error";
			break;
		}
	}
}
