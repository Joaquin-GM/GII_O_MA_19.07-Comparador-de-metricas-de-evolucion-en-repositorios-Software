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
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class AddRepositoryFormByGroup extends AddRepositoryFormTemplate {

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 4044675763677227398L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AddRepositoryFormByGroup.class);
	
	private static final String TAB_NAME = "By Group";
	
	private static final String DESCRIPTION = "Enter a group name to be able to choose between one of its repositories.";

	private TextField groupNameTextField;
	
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
	public AddRepositoryFormByGroup() {
		super(
				TAB_NAME, 
				DESCRIPTION 
		);
		addAddedSuccessfulListener(x -> {
			repositoryComboBox.clear();
			updateGroupRepositories();
		});
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryForm#clearFields()
	 */
	@Override
	public void clearFields() {
		groupNameTextField.clear();
		groupNameTextField.setInvalid(false);
		repositoryComboBox.clear();
		repositoryComboBox.setInvalid(false);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#addFormElements()
	 */
	@Override
	protected void addFormElements() {
		groupNameTextField = new TextField();
		repositoryComboBox = new ComboBox<Repository>();
		
		groupNameTextField = new TextField();
		groupNameTextField.setWidth("50%");
		groupNameTextField.setPlaceholder("Group ID or groupname");
		groupNameTextField.setClearButtonVisible(true);
		groupNameTextField.addValueChangeListener(event -> updateGroupRepositories());
		groupNameTextField.setRequired(true);
		
		repositoryComboBox = new ComboBox<Repository>();
		repositoryComboBox.setWidth("50%");
		repositoryComboBox.setPlaceholder("Project");
		repositoryComboBox.setAllowCustomValue(false);
		repositoryComboBox.setItemLabelGenerator(repository -> repository.getName());
		repositoryComboBox.setRequired(true);
		
		HorizontalLayout repositorySelectorHLayout = new HorizontalLayout(groupNameTextField, repositoryComboBox);
		repositorySelectorHLayout.setWidthFull();
		getForm().add(repositorySelectorHLayout);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryFormTemplate#connect()
	 */
	@Override
	protected Repository getRepositoryFromForms() {
		return repositoryComboBox.getOptionalValue().orElse(null);
	}
	
	private void updateGroupRepositories() {
		try {
			RepositoryDataSource repositoryDataSource = RepositoryDataSourceService.getInstance();
			RepositoriesCollectionService repositoriesService = RepositoriesCollectionService.getInstance();
			if (!groupNameTextField.isEmpty()) {
				Collection<Repository> repositories = new ArrayList<Repository>();
				repositories = repositoryDataSource.getAllGroupRepositories(groupNameTextField.getValue())
						.stream()
						.filter(r -> !repositoriesService.getRepositories().contains(r))
						.sorted(Repository.getComparatorByName())
						.collect(Collectors.toList());
				repositoryComboBox.setItems(repositories);
				getResult().setText("Group found");
				getResult().setClassName("");
			} else {
				repositoryComboBox.setItems();
				repositoryComboBox.clear();
			}
		} catch (RepositoryDataSourceException e) {
			LOGGER.error("Error on updateGroupRepositories(). Exception occurred: " + e.getMessage());
			String errorMessage = "";
			getResult().setClassName("errorMessage");
			switch (e.getErrorCode()) {
			case RepositoryDataSourceException.GROUP_NOT_FOUND:
				errorMessage = "Group not found. It doesn't exists or may be inaccessible due to your connection level.";
				break;
			default:
				errorMessage = "An error has occurred. Please, contact the application administrator.";
				break;
			}
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		} catch (Exception e) {
			LOGGER.error("Error on updateGroupRepositories(). Exception occurred: " + e.getMessage());
			String errorMessage = "";
			getResult().setClassName("errorMessage");
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		}
	}
}
