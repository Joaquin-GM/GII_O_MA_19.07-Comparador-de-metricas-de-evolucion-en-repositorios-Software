package gui.views.connectionforms;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import app.RepositoryDataSourceService;
import app.listeners.ConnectionChangedEvent;
import datamodel.RepositorySourceType;
import datamodel.User;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;
import repositorydatasource.RepositoryDataSourceUsingGitlabAPI;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionInfoComponent extends Div {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionInfoComponent.class);

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	private static final long serialVersionUID = 5985569310011263808L;

	private Image userAvatar = new Image();

	private Label connectionInfoLabel = new Label();

	private RepositorySourceType repositorySourceType;

	public RepositorySourceType getRepositorySource() {
		return repositorySourceType;
	}

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 */
	public ConnectionInfoComponent(RepositorySourceType repositorySourceType) {
		this.repositorySourceType = repositorySourceType;

		RepositoryDataSourceService rds = RepositoryDataSourceService.getInstance();
		rds.addConnectionChangedEventListener(event -> update(event)

		);
		
		update(rds.getConnectionType(repositorySourceType));
		
		userAvatar.setWidth("50px");
		userAvatar.setHeight("50px");
		userAvatar.setAlt("User Avatar");
		HorizontalLayout hLayout = new HorizontalLayout(userAvatar, connectionInfoLabel);
		hLayout.setVerticalComponentAlignment(Alignment.CENTER, userAvatar, connectionInfoLabel);
		hLayout.setWidthFull();
		add(hLayout);
	}

	/**
	 * Gets the userAvatar.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the userAvatar
	 */
	public Image getUserAvatar() {
		return userAvatar;
	}

	/**
	 * Gets the connectionInfo.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @return the connectionInfo
	 */
	public Label getConnectionInfo() {
		return connectionInfoLabel;
	}

	/**
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param event ConnectionChangedEvent
	 */
	public void update(ConnectionChangedEvent event) {
		if (event.getRepositorySourceType() == repositorySourceType) {
			update(event.getConnectionTypeAfter());
		}
	}
	
	public void update(EnumConnectionType connectionType) {
		switch (connectionType) {
		case NOT_CONNECTED:
			userAvatar.setVisible(false);
			userAvatar.setSrc("");
			connectionInfoLabel.setText("No connection to " + repositorySourceType);
			break;
		case CONNECTED:
			userAvatar.setVisible(false);
			userAvatar.setSrc("");
			connectionInfoLabel.setText("Using a public connection");
			break;
		case LOGGED:
			try {
				User user = RepositoryDataSourceService.getInstance().getCurrentUser(repositorySourceType);
				userAvatar.setVisible(true);
				userAvatar.setSrc((user.getAvatarUrl() != null) ? user.getAvatarUrl() : "");
				connectionInfoLabel.setText("Connected as: " + user.getUsername());
			} catch (RepositoryDataSourceException e) {
				userAvatar.setVisible(false);
				userAvatar.setSrc("");
				connectionInfoLabel.setText("Connected as: [ERROR]");
			}
			break;
		}
	}
}
