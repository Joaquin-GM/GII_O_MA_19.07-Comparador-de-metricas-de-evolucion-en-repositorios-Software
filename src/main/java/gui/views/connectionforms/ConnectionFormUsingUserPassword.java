package gui.views.connectionforms;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import app.RepositoryDataSourceService;
import exceptions.RepositoryDataSourceException;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionFormUsingUserPassword extends ConnectionFormTemplate {

	private static final long serialVersionUID = -4845217735632848653L;

	private static final String TAB_NAME = "Username and password";
	
	private static final String DESCRIPTION = "In this way you can access your public and private repositories and other public repositories.";
	
	private static final VaadinIcon BUTTON_ICON = VaadinIcon.CONNECT;
	
	private static final String BUTTON_TEXT = "Connect";

	private Label usernameLabel;
	private TextField usernameField;
	
	private Label passwordLabel;
	private PasswordField passwordField;

	public ConnectionFormUsingUserPassword() {
		super(
				TAB_NAME, 
				DESCRIPTION, 
				BUTTON_ICON, 
				BUTTON_TEXT
		);
	}

	@Override
	protected void addFormElements() {
		this.usernameLabel = new Label("Username");
		this.usernameField = new TextField();
		this.passwordLabel = new Label("Password");
		this.passwordField = new PasswordField();
		usernameLabel.setWidthFull();
		usernameField.setWidthFull();
		passwordLabel.setWidthFull();
		passwordField.setWidthFull();
		getForm().addFormItem(usernameField, usernameLabel);
		getForm().addFormItem(passwordField, passwordLabel);
	}

	@Override
	public void clearFields() {
		usernameField.clear();
		passwordField.clear();
		usernameField.setInvalid(false);
		passwordField.setInvalid(false);
		getResult().setText("");
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (usernameField.isEmpty()) {
			usernameField.setErrorMessage("Field required");
			usernameField.setInvalid(true);
			isValid = false;
		} else {
			usernameField.setInvalid(false);
		}
		
		if (passwordField.isEmpty()) {
			passwordField.setErrorMessage("Field required");
			passwordField.setInvalid(true);
			isValid = false;
		} else {
			passwordField.setInvalid(false);
		}
		return isValid;
	}

	@Override
	protected void connect() throws RepositoryDataSourceException {
		RepositoryDataSourceService.getInstance().connect(usernameField.getValue(), passwordField.getValue());
	}
}
