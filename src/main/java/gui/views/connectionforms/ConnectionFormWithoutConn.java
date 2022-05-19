package gui.views.connectionforms;

import com.vaadin.flow.component.icon.VaadinIcon;

import app.RepositoryDataSourceService;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionFormWithoutConn extends ConnectionFormTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 8537961583258938107L;

	private static final String TAB_NAME = "Don't connect";
	
	private static final String DESCRIPTION = "In this way you can only review reports already created. You will not be able to add new repositories, nor calculate metrics.";
	
	private static final VaadinIcon BUTTON_ICON = VaadinIcon.ARROW_CIRCLE_RIGHT;
	
	private static final String BUTTON_TEXT = "Proceed";
	
	public ConnectionFormWithoutConn(RepositorySourceType repositorySourceType) {
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
	protected void addFormElements() {}

	@Override
	public void clearFields() {}

	@Override
	public boolean isValid() {return true;}

	@Override
	protected void connect() throws RepositoryDataSourceException {
		RepositoryDataSource rds = RepositoryDataSourceService.getInstance();
		if (!rds.getConnectionType(getRepositorySource()).equals(EnumConnectionType.NOT_CONNECTED))
			rds.disconnect(getRepositorySource());
	}

}
