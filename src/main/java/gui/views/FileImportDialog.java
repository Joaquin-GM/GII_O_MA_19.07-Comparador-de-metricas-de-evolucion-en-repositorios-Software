package gui.views;

import java.io.InputStream;
import java.util.HashSet;

import org.claspina.confirmdialog.ConfirmDialog;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import app.listeners.FileUploadEvent;
import app.listeners.Listener;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class FileImportDialog extends ConfirmDialog {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -1745391895139222527L;
	
	private Button importButton = new Button("Import", VaadinIcon.UPLOAD.create());
	private MemoryBuffer buffer = new MemoryBuffer();
	private Upload upload = new Upload(buffer);
	private Label message = new Label();

	private HashSet<Listener<FileUploadEvent>> fileUploadEventListeners = new HashSet<Listener<FileUploadEvent>>();
	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param header
	 * @param acceptedFileTypes
	 * @param fileUploadEventListener
	 */
	public FileImportDialog() {
		super();
		upload.setWidthFull();
		
		importButton.addClickListener(event -> {
			if (buffer.getInputStream() != null) {
				notifyFileUploadEventListeners(buffer.getFileName(), buffer.getInputStream());
				message.setText("");
			} else {
				message.setText("* File required");
			}
		});
		
		upload.addSucceededListener(event -> {
			message.setText("");
			message.add(new Html("<span>File uploaded:<br>" + event.getFileName() + "</span>"));
			
		});
		upload.addFailedListener(event -> {
			message.setText("Error while uploading the file.");
		});
		
		withMessage(new VerticalLayout(upload, message));
		withCancelButton();
		withButton(importButton);
	}
	
	public FileImportDialog withAcceptedFileTypes(String... string) {
		upload.setAcceptedFileTypes(string);
		return this;
	}
	
	public FileImportDialog withUploadListener(Listener<FileUploadEvent> listener) {
		addFileUploadEventListeners(listener);
		return this;
	}
	
	public boolean addFileUploadEventListeners(Listener<FileUploadEvent> listener) {
		return fileUploadEventListeners.add(listener);
	}

	public boolean removeFileUploadEventListeners(Listener<FileUploadEvent> listener) {
		return fileUploadEventListeners.remove(listener);
	}
	
	private void notifyFileUploadEventListeners(String fileName, InputStream inputStream) {
		fileUploadEventListeners.forEach(l -> l.on(new FileUploadEvent(fileName, inputStream)));
	}

	public FileImportDialog withCaption(String caption) {
		super.withCaption(caption);
		return this;
	}

}
