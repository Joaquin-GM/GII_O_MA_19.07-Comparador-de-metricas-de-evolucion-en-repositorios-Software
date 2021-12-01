package gui.views.addrepositoryform;

import java.io.Serializable;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;

import datamodel.Repository;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public interface AddRepositoryForm extends Serializable{
	
	public interface AddedSuccessfulListener {
		void onAddedSuccessful(Repository repository);
	}
	
	void clearFields();
	void clearMessage();
	Tab getTab();
	Div getPage();
	
	void addAddedSuccessfulListener(AddedSuccessfulListener listener);
	void removeAddedSuccessfulListener(AddedSuccessfulListener listener);
	
}
