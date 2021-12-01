package gui.views.connectionforms;

import java.util.ArrayList;
import java.util.List;

import org.claspina.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionDialog extends Dialog {

	private static final long serialVersionUID = -2348702400211722166L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionDialog.class);

	private List<ConnectionForm> connectionForms = new ArrayList<>();
	
	public ConnectionDialog() {
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
				if(event.isOpened()) {
					connectionForms.forEach(connForm -> connForm.getPage().setVisible(false));
					tabs.setSelectedTab(connectionForms.get(0).getTab());
					connectionForms.get(0).getPage().setVisible(true);				
				}
			});
			
			HorizontalLayout connFormsHLayout = new HorizontalLayout(tabs, forms);
			connFormsHLayout.setSizeFull();
			add(connFormsHLayout);

			setWidth("550px");
			setHeight("345px");
			setCloseOnEsc(false);
			setCloseOnOutsideClick(false);
		} catch (Exception e) {
			LOGGER.error("Error initializing ConnectionDialog" + e.getMessage());
			ConfirmDialog.createError()
			.withCaption("Error")
			.withMessage("An error has occurred. Please, contact the application administrator.")
			.withOkButton()
			.open();
		}
	}

	private void createConnectionForms() {

		ConnectionForm userPasswordConnForm = new ConnectionFormUsingUserPassword();
		connectionForms.add(userPasswordConnForm);

		ConnectionForm paTokenConnForm = new ConnectionFormUsingPAToken();
		connectionForms.add(paTokenConnForm);

		ConnectionForm publicConnForm = new ConnectionFormUsingPublicConn();
		connectionForms.add(publicConnForm);

		ConnectionForm noConnForm = new ConnectionFormWithoutConn();
		connectionForms.add(noConnForm);
	}

}
