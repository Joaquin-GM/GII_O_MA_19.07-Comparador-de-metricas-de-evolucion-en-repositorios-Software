package gui.views.connectionforms;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import app.RepositoryDataSourceService;
import datamodel.RepositorySourceType;
import datamodel.User;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 *
 */
public class ConnectionInfoComponent extends Div {

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
		rds.addConnectionChangedEventListener(event -> update(event.getConnectionTypeAfter()));
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

	public void update(EnumConnectionType connectionType) {
		switch (connectionType) {
		case NOT_CONNECTED:
			userAvatar.setVisible(false);
			userAvatar.setSrc("");
			
			connectionInfoLabel.setText("No connection to " + repositorySourceType);
			
			/*
			if (repositorySource.equals("GitLab")) {
				connectionInfoLabel.setText("No connection to GitLab");
			} else if (repositorySource.equals("GitHub")) {
				connectionInfoLabel.setText("No connection to GitHub");
			}
			*/
			
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
				userAvatar.setSrc((user.getAvatarUrl() != null)?user.getAvatarUrl():"");
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
