package gui.views.connectionforms;

import org.claspina.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import app.RepositoryDataSourceService;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;
import repositorydatasource.RepositoryDataSource;
import repositorydatasource.RepositoryDataSource.EnumConnectionType;

public class CloseConnectionDialog extends Dialog {

	private static final long serialVersionUID = -3169215633646184159L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseConnectionDialog.class);
	
	private RepositorySourceType repositorySourceType;
	
	public RepositorySourceType getRepositorySource() {
		return repositorySourceType;
	}

	private ConnectionInfoComponent connectionInfoComponentGitLab;
	
	private ConnectionDialog connectionFormDialog;
	
	private RepositoryDataSource rds = RepositoryDataSourceService.getInstance();
	
	private Button closeConnectionButton = new Button();
	
	private Button closeDialogButton = new Button("Close", new Icon(VaadinIcon.CLOSE), event -> close());

	public CloseConnectionDialog(RepositorySourceType repositorySourceType) {
		this.repositorySourceType = repositorySourceType;
		
		connectionInfoComponentGitLab = new ConnectionInfoComponent(repositorySourceType);
		connectionFormDialog = new ConnectionDialog(repositorySourceType);
		
		addOpenedChangeListener(event ->{
			if(event.isOpened()) {
				if (rds.getConnectionType(repositorySourceType).equals(EnumConnectionType.NOT_CONNECTED)) {
					closeConnectionButton.setText("Connect");
					closeConnectionButton.setIcon(VaadinIcon.CONNECT.create());
				} else {
					closeConnectionButton.setText("Close connection");
					closeConnectionButton.setIcon(VaadinIcon.UNLINK.create());
				}							
			}
		});
		
		closeConnectionButton.addClickListener(event ->  
		{
			if(rds.getConnectionType(repositorySourceType) != EnumConnectionType.NOT_CONNECTED) {
				try {
					rds.disconnect(repositorySourceType);
				} catch (RepositoryDataSourceException e) {
					ConfirmDialog.createError()
					.withCaption("Error")
					.withMessage("An error has occurred. Please, contact the application administrator.")
					.withOkButton()
					.open();
				}
			}
			close();
			connectionFormDialog.open();
		});
		HorizontalLayout buttonsLayout = new HorizontalLayout(closeConnectionButton, closeDialogButton);
		VerticalLayout vLayout = new VerticalLayout(connectionInfoComponentGitLab, buttonsLayout);
		add(vLayout);
	}

}
