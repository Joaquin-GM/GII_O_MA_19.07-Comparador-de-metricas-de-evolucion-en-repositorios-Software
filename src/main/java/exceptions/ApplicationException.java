package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationException extends Exception {
	
	/**
	 * Serial version.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -5078972191684409106L;
	
	/**
	 * Error code.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	protected int code = -1;
	/**
	 * Error message.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	protected String message = "";

	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public ApplicationException() {}

	public ApplicationException(int errorCode, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super("", cause, enableSuppression, writableStackTrace);
		initByErrorCode(errorCode);
	}
	
	public ApplicationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationException(int errorCode, Throwable cause) {
		super("", cause);
		initByErrorCode(errorCode);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param cause
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Use default messages for a code error.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param errorCode
	 */
	public ApplicationException(int errorCode) {
		initByErrorCode(errorCode);
	}
	
	/**
	 * Set the message from the error code,
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	protected void generateMessage() {
		message = "Unknown exception";
	};

	/**
	 * It allows to establish a personalized message.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param message
	 */
	public ApplicationException(String message) {
		this.message = message;
		log();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the error code.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the error code.
	 */
	public int getErrorCode() {
		return code;
	}

	private void initByErrorCode(int errorCode) {
		this.code = errorCode;
		generateMessage();
		log();
	}

	/**
	 * Record the exception in log.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private void log() {
		Logger LOGGER;
		
		if (this.getCause() != null) {
			LOGGER = LoggerFactory.getLogger(this.getCause().getClass());
			LOGGER.error(this.getCause().getMessage());
			
			for (StackTraceElement ste : this.getCause().getStackTrace()) {
				LOGGER.info(ste.toString());
			}
		}
		
		LOGGER = LoggerFactory.getLogger(this.getClass());
		LOGGER.error(this.message);
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			LOGGER.info(ste.toString());
		}
	}
}