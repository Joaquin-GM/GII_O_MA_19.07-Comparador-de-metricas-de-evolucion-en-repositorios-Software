package gui.views.addrepositoryform;

import com.vaadin.flow.component.textfield.TextField;

import app.RepositoryDataSourceService;
import datamodel.Repository;
import exceptions.RepositoryDataSourceException;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class AddRepositoryFormByURL extends AddRepositoryFormTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 9046389447154105535L;

	private static final String TAB_NAME = "By URL";
	
	private static final String DESCRIPTION = "Enter the URL of the project you want to add.";
	
	private TextField urlTextField;
	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param tabName
	 * @param description
	 * @param buttonIcon
	 * @param buttonText
	 */
	public AddRepositoryFormByURL() {
		super(
				TAB_NAME, 
				DESCRIPTION 
		);
		addAddedSuccessfulListener(x -> clearFields());
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryForm#clearFields()
	 */
	@Override
	public void clearFields() {
		urlTextField.clear();
		urlTextField.setInvalid(false);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#addFormElements()
	 */
	@Override
	protected void addFormElements() {
		this.urlTextField = new TextField();
		urlTextField.setPlaceholder("Project URL");
		urlTextField.setWidthFull();
		urlTextField.setRequired(true);
		getForm().add(urlTextField);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#createRepository()
	 */
	@Override
	protected Repository getRepositoryFromForms() throws RepositoryDataSourceException {
		return RepositoryDataSourceService.getInstance().getRepository(urlTextField.getOptionalValue().orElse(""));
	}
}
