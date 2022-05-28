package gui.views.connectionforms;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import gui.customcomponents.DialogHeader;
import gui.views.addrepositoryform.AddRepositoryFormByGroup;
import gui.views.addrepositoryform.AddRepositoryFormByURL;
import gui.views.addrepositoryform.AddRepositoryFormByUsername;

import org.claspina.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

import datamodel.RepositorySourceType;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionDialog extends Dialog {

	private static final long serialVersionUID = -2348702400211722166L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionDialog.class);

	private RepositorySourceType repositorySourceType;

	public RepositorySourceType getRepositorySource() {
		return repositorySourceType;
	}

	private List<ConnectionForm> connectionForms = new ArrayList<>();

	public ConnectionDialog(RepositorySourceType repositorySourceType) {
		this.repositorySourceType = repositorySourceType;

		try {
			createConnectionForms();

			Tabs tabs = new Tabs();
			tabs.setOrientation(Orientation.VERTICAL);
			tabs.setWidth("30%");

			Div forms = new Div();
			forms.setWidth("70%");

			for (ConnectionForm iConnForm : connectionForms) {
				tabs.add(iConnForm.getTab());
				forms.add(iConnForm.getPage());
				iConnForm.addConnectionSuccessfulListener(c -> {
					connectionForms.forEach(connForm -> connForm.clearFields());
					close();
				});
			}

			tabs.addSelectedChangeListener(event -> {
				for (ConnectionForm iConnForm : connectionForms) {
					if (iConnForm.getTab() == event.getSource().getSelectedTab())
						iConnForm.getPage().setVisible(true);
					else {
						iConnForm.getPage().setVisible(false);
						iConnForm.clearFields();
					}
				}
			});

			addOpenedChangeListener(event -> {
				if (event.isOpened()) {
					connectionForms.forEach(connForm -> connForm.getPage().setVisible(false));
					tabs.setSelectedTab(connectionForms.get(0).getTab());
					connectionForms.get(0).getPage().setVisible(true);
				}
			});

			DialogHeader dialogHeader = new DialogHeader("Set a connection with " + repositorySourceType.toString());
			dialogHeader.addCloseListener(e -> close());

			HorizontalLayout connFormsHLayout = new HorizontalLayout(tabs, forms);
			connFormsHLayout.setSizeFull();

			VerticalLayout root = new VerticalLayout(dialogHeader.headerLayout, connFormsHLayout);

			add(root);

			setWidth("550px");
			setHeight("375px");
			setCloseOnEsc(false);
			setCloseOnOutsideClick(false);
		} catch (Exception e) {
			LOGGER.error("Error initializing ConnectionDialog" + e.getMessage());
			ConfirmDialog.createError().withCaption("Error")
					.withMessage("An error has occurred. Please, contact the application administrator.").withOkButton()
					.open();
		}
	}

	private void createConnectionForms() {
		LOGGER.info("--------createConnectionForms-----------");
		LOGGER.info(repositorySourceType.toString());
		
		if (repositorySourceType.equals(RepositorySourceType.GitLab)) {
			ConnectionForm userPasswordConnForm = new ConnectionFormUsingUserPassword(repositorySourceType);
			connectionForms.add(userPasswordConnForm);

			ConnectionForm paTokenConnForm = new ConnectionFormUsingPAToken(repositorySourceType);
			connectionForms.add(paTokenConnForm);

			ConnectionForm publicConnForm = new ConnectionFormUsingPublicConn(repositorySourceType);
			connectionForms.add(publicConnForm);

			ConnectionForm noConnForm = new ConnectionFormWithoutConn(repositorySourceType);
			connectionForms.add(noConnForm);
			
		} else if (repositorySourceType.equals(RepositorySourceType.GitHub)) {
			// Conection with username and password deprecated for GitHub
			ConnectionForm paTokenConnForm = new ConnectionFormUsingPAToken(repositorySourceType);
			connectionForms.add(paTokenConnForm);

			ConnectionForm publicConnForm = new ConnectionFormUsingPublicConn(repositorySourceType);
			connectionForms.add(publicConnForm);

			ConnectionForm noConnForm = new ConnectionFormWithoutConn(repositorySourceType);
			connectionForms.add(noConnForm);
		}
	
	}

}
