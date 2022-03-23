package gui.views.addrepositoryform;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import gui.customcomponents.DialogHeader;
import org.claspina.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class AddRepositoryDialog extends Dialog {

	private static final long serialVersionUID = -2348702400211722166L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddRepositoryDialog.class);

	private List<AddRepositoryForm> addRepositoryForms = new ArrayList<>();
	
	public AddRepositoryDialog() {
		try {
			createConnectionForms();

			Tabs tabs = new Tabs();
			tabs.setOrientation(Orientation.VERTICAL);
			tabs.setWidth("30%");

			Div formsPages = new Div();
			formsPages.setWidth("70%");

			for (AddRepositoryForm form : addRepositoryForms) {
				tabs.add(form.getTab());
				formsPages.add(form.getPage());
			}

			tabs.addSelectedChangeListener(event -> {
				for (AddRepositoryForm form : addRepositoryForms) {
					if (form.getTab() == event.getSource().getSelectedTab())
						form.getPage().setVisible(true);
					else {
						form.getPage().setVisible(false);
						form.clearFields();
						form.clearMessage();
					}
				}
			});
			
			addOpenedChangeListener(event -> {
				if(event.isOpened()) {
					addRepositoryForms.forEach(connForm -> connForm.getPage().setVisible(false));
					tabs.setSelectedTab(addRepositoryForms.get(0).getTab());
					addRepositoryForms.get(0).getPage().setVisible(true);				
				}
			});
			
			addDialogCloseActionListener(event -> {
				addRepositoryForms.forEach(f -> {
					f.clearFields();
					f.clearMessage();
					});
				close();
			});
			
			HorizontalLayout connFormsHLayout = new HorizontalLayout(tabs, formsPages);
			connFormsHLayout.setSizeFull();


			DialogHeader dialogHeader = new DialogHeader("Add projects");
			dialogHeader.addCloseListener(e -> close());

			VerticalLayout root = new VerticalLayout(dialogHeader.headerLayout, connFormsHLayout);
			
			add(root);

			setWidth("600px");
			setHeight("295px");
		} catch (Exception e) {
			LOGGER.error("" + e.getMessage());
			ConfirmDialog.createError()
			.withCaption("Error")
			.withMessage("An error has occurred. Please, contact the application administrator.")
			.withOkButton()
			.open();
		}
	}

	private void createConnectionForms() {
		addRepositoryForms.add(new AddRepositoryFormByUsername());
		addRepositoryForms.add(new AddRepositoryFormByGroup());
		addRepositoryForms.add(new AddRepositoryFormByURL());
	}

}
