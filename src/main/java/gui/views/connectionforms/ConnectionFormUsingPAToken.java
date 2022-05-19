package gui.views.connectionforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;

import app.RepositoryDataSourceService;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;

/**
 * @author Miguel Ángel León Bardavío - mlb0029	 	 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public class ConnectionFormUsingPAToken extends ConnectionFormTemplate {
	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029	 
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static final long serialVersionUID = 8537961583258938107L;

	private static final String TAB_NAME = "Personal Access Token";
	
	private static final String DESCRIPTION = "In this way you can access your public and private repositories and other public repositories.";
	
	private static final VaadinIcon BUTTON_ICON = VaadinIcon.CONNECT;
	
	private static final String BUTTON_TEXT = "Connect";
	
	private Label tokenLabel;
	private PasswordField tokenField;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFormUsingPAToken.class);
	
	public ConnectionFormUsingPAToken(RepositorySourceType repositorySourceType) {
		super(
				TAB_NAME, 
				DESCRIPTION, 
				BUTTON_ICON, 
				BUTTON_TEXT,
				repositorySourceType
				);
	}

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.AConnForm#addFormElements()
	 */
	@Override
	protected void addFormElements() {
		tokenLabel = new Label("Personal Access Token");
		tokenField = new PasswordField();
		tokenLabel.setWidthFull();
		tokenField.setWidthFull();
		tokenField.setRequired(true);
		getForm().addFormItem(tokenField, tokenLabel);
	}

	@Override
	public void clearFields() {
		tokenField.clear();
		tokenField.setInvalid(false);
		getResult().setText("");
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (tokenField.isEmpty()) {
			tokenField.setErrorMessage("Field required");
			tokenField.setInvalid(true);
			isValid = false;
		} else {
			tokenField.setInvalid(false);
		}
		return isValid;
	}

	@Override
	protected void connect() throws RepositoryDataSourceException {
		LOGGER.info("-- connect en ConnectionFormUsingPAToken --");
		LOGGER.info(tokenField.getValue());
		LOGGER.info(getRepositorySource().toString());
		RepositoryDataSourceService.getInstance().connect(tokenField.getValue(), getRepositorySource());
	}

}
