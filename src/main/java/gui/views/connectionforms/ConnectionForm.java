package gui.views.connectionforms;

import java.io.Serializable;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;

import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public interface ConnectionForm extends Serializable{
	
	public interface IConnectionSuccessfulListener {
		void onConnectionSuccessful(EnumConnectionType connectionType);
	}
	
	void clearFields();
	boolean isValid();
	Tab getTab();
	Div getPage();
	
	void addConnectionSuccessfulListener(IConnectionSuccessfulListener listener);
	void removeConnectionSuccessfulListener(IConnectionSuccessfulListener listener);
	
}
