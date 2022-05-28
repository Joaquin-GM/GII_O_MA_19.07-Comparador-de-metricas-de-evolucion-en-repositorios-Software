package gui.views.addrepositoryform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;

import app.MetricsService;
import app.RepositoriesCollectionService;
import datamodel.Repository;
import datamodel.RepositorySourceType;
import exceptions.ApplicationException;
import exceptions.RepositoriesCollectionServiceException;
import exceptions.RepositoryDataSourceException;

public abstract class AddRepositoryFormTemplate implements AddRepositoryForm{
	
	public class FormElement {
		private String name;
		private Component component;
	
		public FormElement(String name, Component component) {
			this.name = name;
			this.component = component;
		}
	
		public String getName() {
			return name;
		}
	
		public Component getComponent() {
			return component;
		}
	}

	private static final long serialVersionUID = 7001361983934286617L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddRepositoryFormTemplate.class);
	
	private Set<AddedSuccessfulListener> listeners = new HashSet<>();
	
	private Tab tab = new Tab();
	private FormLayout form = new FormLayout();
	private Label description = new Label();
	private Button button = new Button();
	private Label result = new Label();
	private Div page = new Div();

	protected AddRepositoryFormTemplate(String tabName, String description, RepositorySourceType repositorySourceType) {
		this.tab.setLabel(tabName);

		this.form.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP),
				new ResponsiveStep("200px", 1, LabelsPosition.TOP));
		this.description.setText(description);
		this.form.add(this.description);
		
		addFormElements(repositorySourceType);
		
		this.result.setText("");

		this.button.setIcon(new Icon(VaadinIcon.PLUS));
		this.button.setText("Add");
		this.button.addClickListener(event -> addRepository(repositorySourceType));
		this.form.add(this.button);
		
		this.result.setWidthFull();
		this.form.add(this.result);
		
		this.page.add(form);
	}

	private void addRepository(RepositorySourceType repositorySourceType) {
		try {
			Repository repository = getRepositoryFromForms(repositorySourceType);
			if(repository != null) {
				LOGGER.error("entro");
				RepositoriesCollectionService.getInstance().addRepository(repository);	
				LOGGER.error("he añadido el repository");
				MetricsService.getMetricsService().obtainAndEvaluateRepositoryMetrics(repository);
				LOGGER.error("despues de obtainAndEvaluateRepositoryMetrics");
				listeners.forEach(l -> l.onAddedSuccessful(repository));
				result.setText("Project added correctly");
				result.setClassName("");				
			} else {
				String errorMessage = "No project selected.";
				getResult().setClassName("errorMessage");
				getResult().setText(errorMessage);
				getResult().setTitle(errorMessage);
			}
		} catch (ApplicationException e) {
			String errorMessage = "";
			if (e instanceof RepositoryDataSourceException) {
				if (((RepositoryDataSourceException)e).getErrorCode() == RepositoryDataSourceException.REPOSITORY_NOT_FOUND){
					errorMessage = "Project not found. It doesn't exists or may be inaccessible due to your connection level.";
				}
			} else if (e instanceof RepositoriesCollectionServiceException) {
				if (((RepositoriesCollectionServiceException)e).getErrorCode() == RepositoriesCollectionServiceException.REPOSITORY_ALREADY_EXISTS){
					errorMessage = "The project already exists.";
				}
			}
			getResult().setClassName("errorMessage");
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		} catch (Exception e) {
			LOGGER.error("error aqui!!!!!!!!!");
			LOGGER.error("" + e.getMessage());
			LOGGER.error("" + e.toString());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			LOGGER.error(exceptionAsString);
			String errorMessage = "An error has occurred. Please, contact the application administrator.";
			getResult().setClassName("errorMessage");
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		}
	}
	
	/**
	 * Gets the tab.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the tab
	 */
	public Tab getTab() {
		return tab;
	}

	/**
	 * Gets the form.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the form
	 */
	public FormLayout getForm() {
		return form;
	}

	/**
	 * Gets the description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the description
	 */
	public Label getDescription() {
		return description;
	}

	/**
	 * Gets the button.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * Gets the result.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the result
	 */
	public Label getResult() {
		return result;
	}

	/**
	 * Gets the page.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the page
	 */
	public Div getPage() {
		return page;
	}

	protected abstract void addFormElements(RepositorySourceType repositorySourceType);

	protected abstract Repository getRepositoryFromForms(RepositorySourceType repositorySourceType) throws ApplicationException;

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.IConnForm#addConnectionSuccessfulListener(gui.views.connectionForms.IConnForm.IConnectionSuccessfulListener)
	 */
	@Override
	public void addAddedSuccessfulListener(AddedSuccessfulListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.IConnForm#removeConnectionSuccessfulListener(gui.views.connectionForms.IConnForm.IConnectionSuccessfulListener)
	 */
	@Override
	public void removeAddedSuccessfulListener(AddedSuccessfulListener listener) {
		listeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see gui.views.addrepositoryform.AddRepositoryForm#clearResult()
	 */
	@Override
	public void clearMessage() {
		this.result.setText("");
	}
}