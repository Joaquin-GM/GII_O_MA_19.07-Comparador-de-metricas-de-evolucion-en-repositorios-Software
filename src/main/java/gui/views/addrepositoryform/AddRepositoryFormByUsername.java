package gui.views.addrepositoryform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import app.RepositoriesCollectionService;
import app.RepositoryDataSourceService;
import datamodel.Repository;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class AddRepositoryFormByUsername extends AddRepositoryFormTemplate {

	private static final long serialVersionUID = 244817979729487325L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddRepositoryFormByUsername.class);
	
	private static final String TAB_NAME = "By Username";
	
	private static final String DESCRIPTION = "Enter a username to be able to choose between one of its repositories.";

	private TextField usernameTextField;
	
	private ComboBox<Repository> repositoryComboBox;
	
	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param tabName
	 * @param description
	 * @param buttonIcon
	 * @param buttonText
	 */
	public AddRepositoryFormByUsername(RepositorySourceType repositorySourceType) {
		super(
				TAB_NAME, 
				DESCRIPTION,
				repositorySourceType
		);
		addAddedSuccessfulListener(x -> {
			repositoryComboBox.clear();
			updateUserRepositories(repositorySourceType);
		});
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryForm#clearFields()
	 */
	@Override
	public void clearFields() {
		usernameTextField.clear();
		usernameTextField.setInvalid(false);
		repositoryComboBox.clear();
		repositoryComboBox.setInvalid(false);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#addFormElements()
	 */
	@Override
	protected void addFormElements(RepositorySourceType repositorySourceType) {
		usernameTextField = new TextField();
		usernameTextField.setWidth("50%");
		usernameTextField.setPlaceholder("User ID or username");
		usernameTextField.setClearButtonVisible(true);
		usernameTextField.addValueChangeListener(event -> updateUserRepositories(repositorySourceType));
		usernameTextField.setRequired(true);
		
		repositoryComboBox = new ComboBox<Repository>();
		repositoryComboBox.setWidth("50%");
		repositoryComboBox.setPlaceholder("Project");
		repositoryComboBox.setAllowCustomValue(false);
		repositoryComboBox.setItemLabelGenerator(repository -> repository.getName());
		repositoryComboBox.setRequired(true);
		
		HorizontalLayout repositorySelectorHLayout = new HorizontalLayout(usernameTextField, repositoryComboBox);
		repositorySelectorHLayout.setWidthFull();
		getForm().add(repositorySelectorHLayout);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#connect()
	 */
	@Override
	protected Repository getRepositoryFromForms(RepositorySourceType repositorySourceType) throws RepositoryDataSourceException {
		return repositoryComboBox.getOptionalValue().orElse(null);
	}

	private void updateUserRepositories(RepositorySourceType repositorySourceType) {
		try {
			RepositoryDataSource repositoryDataSource = RepositoryDataSourceService.getInstance();
			RepositoriesCollectionService repositoriesService = RepositoriesCollectionService.getInstance();
			if (!usernameTextField.isEmpty()) {
				Collection<Repository> repositories = new ArrayList<Repository>();
				LOGGER.info("Buscando con el usuario: " + usernameTextField.getValue());
				repositories = repositoryDataSource.getAllUserRepositories(usernameTextField.getValue(), repositorySourceType)
						.stream()
						.filter(r -> !repositoriesService.getRepositories().contains(r))
						.sorted(Repository.getComparatorByName())
						.collect(Collectors.toList());
				repositoryComboBox.setItems(repositories);
				getResult().setText("User found");
				getResult().setClassName("");
			} else {
				repositoryComboBox.setItems();
				repositoryComboBox.clear();
			}
		} catch (RepositoryDataSourceException e) {
			LOGGER.error("Error on updateUserRepositories(). Exception occurred: " + e.getMessage());
			String errorMessage = "";
			getResult().setClassName("errorMessage");
			switch (e.getErrorCode()) {
			case RepositoryDataSourceException.USER_NOT_FOUND:
				errorMessage = "User not found";
				break;
			default:
				errorMessage = "An error has occurred. Please, contact the application administrator.";
				break;
			}
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		} catch (Exception e) {
			LOGGER.error("Error on updateUserRepositories(). Exception occurred: " + e.getMessage());
			String errorMessage = "";
			getResult().setClassName("errorMessage");
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		}
	}
}
