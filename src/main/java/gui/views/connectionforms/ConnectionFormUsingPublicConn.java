package gui.views.connectionforms;

import com.vaadin.flow.component.icon.VaadinIcon;

import app.RepositoryDataSourceService;
import exceptions.RepositoryDataSourceException;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionFormUsingPublicConn extends ConnectionFormTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 8537961583258938107L;

	private static final String TAB_NAME = "Public connection";
	
	private static final String DESCRIPTION = "In this way you can only access public repositories.";
	
	private static final VaadinIcon BUTTON_ICON = VaadinIcon.CONNECT;
	
	private static final String BUTTON_TEXT = "Connect";
	
	public ConnectionFormUsingPublicConn() {
		super(
				TAB_NAME, 
				DESCRIPTION, 
				BUTTON_ICON, 
				BUTTON_TEXT);
	}

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.AConnForm#addFormElements()
	 */
	@Override
	protected void addFormElements() {
	}

	@Override
	public void clearFields() {	}

	@Override
	public boolean isValid() {return true; }

	@Override
	protected void connect() throws RepositoryDataSourceException {
		RepositoryDataSourceService.getInstance().connect();
	}

}
