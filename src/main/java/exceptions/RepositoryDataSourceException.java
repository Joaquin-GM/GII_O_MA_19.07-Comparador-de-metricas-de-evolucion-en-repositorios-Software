package exceptions;

/**
 * Manage all Repository Data Source errors.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoryDataSourceException extends ApplicationException {
	
	/**
	 * Serial version.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 7397268774778874932L;

	public static final int CONNECTION_ERROR = 1;
	
	public static final int LOGIN_ERROR = 2;
	
	public static final int ALREADY_CONNECTED = 3;
	
	public static final int ALREADY_DISCONNECTED = 4;
	
	public static final int NOT_CONNECTED = 5;
	
	public static final int REPOSITORY_NOT_FOUND = 6;
	
	public static final int USER_NOT_FOUND = 7;
	
	public static final int GROUP_NOT_FOUND = 8;
	
	/**
	 * Constructor from superclass.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public RepositoryDataSourceException(int errorCode) {
		super(errorCode);
	}

	/**
	 * Constructor from superclass.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public RepositoryDataSourceException(String message) {
		super(message);
	}

	public RepositoryDataSourceException() {}

	public RepositoryDataSourceException(int errorCode, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(errorCode, cause, enableSuppression, writableStackTrace);
	}

	public RepositoryDataSourceException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public RepositoryDataSourceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RepositoryDataSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryDataSourceException(Throwable cause) {
		super(cause);
	}

	@Override
	protected void generateMessage() {
		switch (code) {
		case CONNECTION_ERROR:
			message = "Connection failure: Unable to establish a connection";
			break;
		case LOGIN_ERROR:
			message = "Login failure: incorrect credentials";
			break;
		case ALREADY_CONNECTED:
			message = "Connection failure: A connection already exists";
			break;
		case ALREADY_DISCONNECTED:
			message = "Disconnection failure: Already disconnected";
			break;
		case NOT_CONNECTED:
			message = "Error: There is no connection";
			break;
		case REPOSITORY_NOT_FOUND:
			message = "Repository not found: It doesn't exists or it's inaccessible due to the connection level";
			break;
		case USER_NOT_FOUND:
			message = "User not found";
			break;
		case GROUP_NOT_FOUND:
			message = "Group not found";
			break;
		default:
			message = "Unknown RepositoryDataSource error";
			break;
		}
	}
}
