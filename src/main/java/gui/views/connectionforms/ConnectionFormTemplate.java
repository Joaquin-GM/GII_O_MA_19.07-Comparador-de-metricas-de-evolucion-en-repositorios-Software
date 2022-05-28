package gui.views.connectionforms;

import java.io.Serializable;
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

import app.RepositoryDataSourceService;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;

/**
 * 
 * @author Joaquin Garcia Molina - Joaquin-GM
 *
 */
public abstract class ConnectionFormTemplate implements ConnectionForm {
	
	
	public class FormElement implements Serializable {
		private static final long serialVersionUID = -9073432172154725623L;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFormTemplate.class);
	
	private Set<IConnectionSuccessfulListener> listeners = new HashSet<>();
	
	private Tab tab = new Tab();
	private FormLayout form = new FormLayout();
	private Label description = new Label();
	private Button button = new Button();
	private Label result = new Label();
	private Div page = new Div();
	
	private RepositorySourceType repositorySourceType;
	
	public RepositorySourceType getRepositorySource() {
		return repositorySourceType;
	}

	protected ConnectionFormTemplate(String tabName, String description, VaadinIcon buttonIcon, String buttonText, RepositorySourceType repositorySourceType) {
		this.repositorySourceType = repositorySourceType;
		this.tab.setLabel(tabName);

		this.form.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP),
				new ResponsiveStep("200px", 1, LabelsPosition.TOP));
		this.description.setText(description);
		this.form.add(this.description);
		
		addFormElements();
		
		this.result.setText("");

		if(buttonIcon != null)
			this.button.setIcon(new Icon(buttonIcon));
		this.button.setText(buttonText);
		this.button.addClickListener(event -> {
			LOGGER.info("EN EL EVENT LISTENER!!!!");
			LOGGER.info(repositorySourceType.toString());
			onConnectButtonClick(repositorySourceType);
		});
		this.form.add(this.button);
		
		this.result.setWidthFull();
		this.form.add(this.result);
		
		this.page.add(form);
	}

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private void onConnectButtonClick(RepositorySourceType repositorySourceType) {
		try {
			if (isValid()) {
				LOGGER.info("XXX");
				LOGGER.info(repositorySourceType.toString());
				LOGGER.info(RepositoryDataSourceService.getInstance().getConnectionType(repositorySourceType).toString());
				connect();
				LOGGER.info("DESPUES DE CONNECT!!!");
				listeners.forEach(l -> l.onConnectionSuccessful(RepositoryDataSourceService.getInstance().getConnectionType(repositorySourceType)));
				LOGGER.info("YYYYYYYYYYYYY");
			}
		} catch (RepositoryDataSourceException e) {
			String errorMessage = "";
			if (e.getErrorCode() == RepositoryDataSourceException.ALREADY_CONNECTED)
				errorMessage = "Connection failure: A connection already exists";
			else if (e.getErrorCode() == RepositoryDataSourceException.CONNECTION_ERROR)
				errorMessage = "Connection failure: Unable to establish a connection";
			else if (e.getErrorCode() == RepositoryDataSourceException.LOGIN_ERROR)
				errorMessage = "Login failure: incorrect credentials";
			else
				errorMessage = "An error has occurred. Please, contact the application administrator.";
			getResult().setClassName("errorMessage");
			getResult().setText(errorMessage);
			getResult().setTitle(errorMessage);
		} catch (Exception e) {
			LOGGER.info(repositorySourceType.toString());
			LOGGER.error("Error aqui!!!! " + e.getMessage());
			LOGGER.error("Error aqui!!!! " + e.toString());
			
			String exception = "";
		    for (StackTraceElement s : e.getStackTrace()) {
		        exception = exception + s.toString() + "\n\t\t";
		    }
			
			LOGGER.error(exception);
			
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

	protected abstract void addFormElements();

	protected abstract void connect() throws RepositoryDataSourceException;

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.IConnForm#addConnectionSuccessfulListener(gui.views.connectionForms.IConnForm.IConnectionSuccessfulListener)
	 */
	@Override
	public void addConnectionSuccessfulListener(IConnectionSuccessfulListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see gui.views.connectionForms.IConnForm#removeConnectionSuccessfulListener(gui.views.connectionForms.IConnForm.IConnectionSuccessfulListener)
	 */
	@Override
	public void removeConnectionSuccessfulListener(IConnectionSuccessfulListener listener) {
		listeners.remove(listener);
	}
}