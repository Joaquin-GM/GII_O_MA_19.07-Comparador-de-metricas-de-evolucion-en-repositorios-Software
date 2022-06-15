package gui.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.exception.ZeroException;
import org.claspina.confirmdialog.ButtonType;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.StreamResource;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class FileExportFormDialog extends ConfirmDialogWrapper {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = -3024115021992696619L;

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @throws IOException 
	 */
	public FileExportFormDialog(InputStream inputStream, String header, String message, String defaultFilename) {
		DownloadLink downloadLink = new DownloadLink(defaultFilename, inputStream);
		downloadLink.downloadButton.addClickListener(e -> closeDownloadDialog(downloadLink));
		
		withCaption(header);
		withIcon(DIALOG_DEFAULT_ICON_FACTORY.getQuestionIcon());
		withMessage(message);
		withCancelButton();
		withButton(downloadLink);
		getButton(ButtonType.CANCEL).setIcon(null);
	}
	
	/**
	 * Custom close method, gives time to "create" the downloadable file.
	 * If the dialog closes immediately after the download click event, the file may be not created.
	 *
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @throws InterruptedException 
	 */
	public void closeDownloadDialog(DownloadLink downloadLink) {
			try {
				TimeUnit.SECONDS.sleep(1);
				close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Creates the download link for files.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 *
	 */
	private static class DownloadLink extends Anchor {

		private static final long serialVersionUID = 8876419840558440068L;

		private static final String BUTTON_TEXT = "Download";

		private Button downloadButton = new Button(BUTTON_TEXT, VaadinIcon.DOWNLOAD.create());
		
		public Boolean downloading = false;

		public DownloadLink(String filename, InputStream in) {
			downloading = true;
			downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			setHref(getStreamResource(filename, in));
			getElement().setAttribute("download", true);
			add(downloadButton);
		}

		private StreamResource getStreamResource(String filename, InputStream in) {
			return new StreamResource(filename, () -> {
				downloading = false;
				return in;
			});
		}

		/* (non-Javadoc)
		 * @see com.vaadin.flow.component.HasEnabled#setEnabled(boolean)
		 */
		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			downloadButton.setEnabled(enabled);
		}
	}
}
