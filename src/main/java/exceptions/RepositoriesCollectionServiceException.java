package exceptions;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class RepositoriesCollectionServiceException extends ApplicationException {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 3304238974846329299L;

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public static final int REPOSITORY_ALREADY_EXISTS = 1;
	
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public static final int NOT_EXIST_REPOSITORY = 2;
	
	public static final int IMPORT_ERROR = 3;
	
	public static final int EXPORT_ERROR = 4;
	
	public static final int CORRUPTED = 5;
	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public RepositoriesCollectionServiceException(int errorCode) {
		super(errorCode);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public RepositoriesCollectionServiceException(String message) {
		super(message);
	}

	public RepositoriesCollectionServiceException() {
	}

	public RepositoriesCollectionServiceException(int errorCode, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(errorCode, cause, enableSuppression, writableStackTrace);
	}

	public RepositoriesCollectionServiceException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public RepositoriesCollectionServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RepositoriesCollectionServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoriesCollectionServiceException(Throwable cause) {
		super(cause);
	}

	@Override
	protected void generateMessage() {
		switch (code) {
		case REPOSITORY_ALREADY_EXISTS:
			message = "The repository already exists";
			break;
		case NOT_EXIST_REPOSITORY:
			message = "The repository doesn't exists";
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
			message = "Unknown RepositoryDataSource error";
			break;
		}
	}
}
