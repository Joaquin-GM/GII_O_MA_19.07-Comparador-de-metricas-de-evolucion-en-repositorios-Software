package app.listeners;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Event that triggers when a file has been uploaded.
 * 
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class FileUploadEvent implements Serializable, Event {

	private static final long serialVersionUID = -4113645939457570245L;

	private String fileName;

	private InputStream inputStream;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param fileName Filename.
	 * @param inputStream Input stream.
	 */
	public FileUploadEvent(String fileName, InputStream inputStream) {
		this.fileName = fileName;
		this.inputStream = inputStream;
	}

	/**
	 * Gets the file name.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the input stream.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the input stream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

}
