package gui.views;

import java.io.IOException;
import java.io.InputStream;

import org.claspina.confirmdialog.ButtonType;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.StreamResource;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
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
	 * @throws IOException 
	 */
	public FileExportFormDialog(InputStream inputStream, String header, String message, String defaultFilename) {
		DownloadLink downloadLink= new DownloadLink(defaultFilename, inputStream);
		downloadLink.downloadButton.addClickListener(e -> close());
		
		withCaption(header);
		withIcon(DIALOG_DEFAULT_ICON_FACTORY.getQuestionIcon());
		withMessage(message);
		withCancelButton();
		withButton(downloadLink);
		getButton(ButtonType.CANCEL).setIcon(null);
	}

	private static class DownloadLink extends Anchor {

		private static final long serialVersionUID = 8876419840558440068L;

		private static final String BUTTON_TEXT = "Download";

		private Button downloadButton = new Button(BUTTON_TEXT, VaadinIcon.DOWNLOAD.create());

		public DownloadLink(String filename, InputStream in) {
			downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			setHref(getStreamResource(filename, in));
			getElement().setAttribute("download", true);
			add(downloadButton);
		}

		private StreamResource getStreamResource(String filename, InputStream in) {
			return new StreamResource(filename, () -> {
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
