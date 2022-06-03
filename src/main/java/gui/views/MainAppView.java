package gui.views;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;

import datamodel.RepositorySourceType;

import org.claspina.confirmdialog.ConfirmDialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import exceptions.GUIException;
import gui.views.connectionforms.CloseConnectionDialog;
import gui.views.connectionforms.ConnectionInfoComponent;

@StyleSheet("site.css")
@Route("")
@PageTitle("Evolution Metrics Gauge")
public class MainAppView extends VerticalLayout implements PageConfigurator {

	private static final long serialVersionUID = -8176239269004450857L;

	/**
	 * To know if it is the start of the application or a page refresh. Since a page
	 * refresh creates a new instance of the components.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	private static boolean IS_INITIALIZED = false;

	private Div header = new Div();

	private Image brandingImage = new Image("images/logoUBU.jpg", "Logo UBU");

	private Image logoImage = new Image("images/LOGOAPP.png", "Logo Evolution Metrics Gauge");

	private Label appNameLabel = new Label("Evolution Metrics Gauge v2");

	private Button connectionButtonGitLab = new Button();
	
	private Button connectionButtonGitHub = new Button();

	private Anchor helpLink = new Anchor();

	private ConnectionInfoComponent connectionInfoComponentGitLab = new ConnectionInfoComponent(
			RepositorySourceType.GitLab);
	
	private ConnectionInfoComponent connectionInfoComponentGitHub = new ConnectionInfoComponent(
			RepositorySourceType.GitHub);

	private CloseConnectionDialog closeConnectionFormDialogGitLab = new CloseConnectionDialog(
			RepositorySourceType.GitLab);
	
	private CloseConnectionDialog closeConnectionFormDialogGitHub = new CloseConnectionDialog(
			RepositorySourceType.GitHub);

	private Div content = new Div();

	private Div footer = new Div();

	private RepositoriesListView repositoriesListView = new RepositoriesListView();

	public MainAppView() {
		try {
			setSizeFull();
			setUpHeader();
			setUpContent();
			setUpFooter();
			add(header, content, footer);
			/*
			 * if (!IS_INITIALIZED) connectionFormDialog.open();
			 */
			MainAppView.IS_INITIALIZED = true;
			ConfirmDialog.setButtonDefaultIconsVisible(false);
		} catch (Exception e) {
			Exception guiEx = new GUIException(GUIException.MAIN_INITIALIZATION_ERROR, e);// To log, don't throw
			getElement().removeAllChildren();
			getElement().appendChild(
					new Span("Error occurred, contact the administrator." + " Error Message: " + guiEx.getMessage())
							.getElement());
		}
	}

	private void setUpHeader() {
		header.setHeight("200px");
		header.setWidthFull();

		brandingImage.setHeight("106px");
		logoImage.setHeight("106px");

		appNameLabel.addClassName("appName");
		appNameLabel.setWidth("540px");

		connectionButtonGitLab.setIcon(connectionInfoComponentGitLab);
		connectionButtonGitLab.setMinHeight("60px");
		connectionButtonGitLab.addClickListener(e -> closeConnectionFormDialogGitLab.open());
		connectionButtonGitLab.setId("connectionInfoButtonGitLab");

		connectionButtonGitHub.setIcon(connectionInfoComponentGitHub);
		connectionButtonGitHub.setMinHeight("60px");
		connectionButtonGitHub.addClickListener(e -> closeConnectionFormDialogGitHub.open());
		connectionButtonGitHub.setId("connectionInfoButtonGitHub");

		Icon questionIcon = VaadinIcon.QUESTION.create();
		questionIcon.setSize("37px");
		Button helpButton = new Button(questionIcon);
		helpButton.setHeight("60px");
		helpButton.setWidth("60px");
		helpLink.setHref(
				"https://gitlab.com/mlb0029/comparador-de-metricas-de-evolucion-en-repositorios-software/wikis/home");
		helpLink.setTarget("_blank");
		helpLink.add(helpButton);

		VerticalLayout connectionButtonLayout = new VerticalLayout(
				new HorizontalLayout(connectionButtonGitLab, connectionButtonGitHub, helpLink));
		// VerticalLayout connectionButtonLayout = new VerticalLayout(new
		// HorizontalLayout(connectionButtonGitLab, helpLink));

		connectionButtonLayout.setAlignItems(Alignment.END);
		connectionButtonLayout.setWidth("40%");
		HorizontalLayout headerHLayout = new HorizontalLayout(brandingImage, appNameLabel, logoImage,
				connectionButtonLayout);
		header.getElement().appendChild(headerHLayout.getElement());
	}

	/**
	 * Configures app's main section.
	 */
	private void setUpContent() {
		content.setMinHeight("70%");
		content.setWidthFull();
		content.getElement().appendChild(repositoriesListView.getElement());
	}

	/**
	 * Configures app's footer. Added a key section containing information of the naming of the metrics.
	 */
	private void setUpFooter() {
		footer.setHeight("15%");
		footer.setWidthFull();

		HorizontalLayout footerHLayout = new HorizontalLayout();
		Div footerLeftSection = new Div();
		Div footerRightSection = new Div();
		footerLeftSection.addClassName("footer-left-section");
		footerRightSection.addClassName("footer-right-section");
		footerHLayout.add(footerLeftSection);
		footerHLayout.add(footerRightSection);

		footerLeftSection.setWidth("50%");
		footerRightSection.setWidth("50%");

		VerticalLayout namesContainer = new VerticalLayout();
		Div authorNameLabel = new Div();
		Div tutorNameLabel = new Div();
		authorNameLabel.setText("Autor: Joaquín García Molina");
		authorNameLabel.setId("Author");
		tutorNameLabel.setText("Tutor: Carlos López");
		tutorNameLabel.setId("Tutor");
		namesContainer.add(authorNameLabel);
		namesContainer.add(tutorNameLabel);
		footerLeftSection.add(namesContainer);

		Div versionLabel = new Div();
		versionLabel.addClassName("version-container");
		versionLabel.setText("Versión: 1.0.0");
		versionLabel.setId("Version");
		footerLeftSection.add(versionLabel);

		HorizontalLayout keyLayout = new HorizontalLayout();
		Div keyContainer = new Div();
		Div keyButtonContainer = new Div();
		keyLayout.add(keyContainer);
		keyLayout.add(keyButtonContainer);
		keyContainer.setWidth("80%");
		keyButtonContainer.setWidth("20%");

		Div keyTitle = new Div();
		keyTitle.setText("Leyenda: ");
		keyContainer.add(keyTitle);
		
		keyContainer.add(generateKeyLabels("I:", " Issues"));
		keyContainer.add(generateKeyLabels("T:", " Tiempo"));
		keyContainer.add(generateKeyLabels("C:", " Commits"));
		keyContainer.add(generateKeyLabels("IC:", " Integración continua"));
		keyContainer.add(generateKeyLabels("DC:", " Despliegue continuo"));
		
		keyContainer.setId("Key");
		keyButtonContainer.setId("keyButtonContainer");

		footerRightSection.add(keyLayout);

		footerHLayout.setWidthFull();
		footer.getElement().appendChild(footerHLayout.getElement());
	}

	/**
	 * Aux method to generate the key labels.
	 * 
	 * @param title
	 * @param text
	 * @return
	 */
	Div generateKeyLabels(String title, String text) {
		Div labelContainer = new Div();
		Span titleSpan = new Span();
		Span tTextSpan = new Span();
		titleSpan.setText(title);
		titleSpan.setClassName("bold-label");
		tTextSpan.setText(text);
		labelContainer.add(titleSpan);
		labelContainer.add(tTextSpan);
		return labelContainer;
	}

	/**
	 * Configures metadata and the favIcon.
	 */
	@Override
	public void configurePage(InitialPageSettings settings) {
		settings.addMetaTag("og:title", "Evolution Metrics Gauge");
		settings.addMetaTag("og:type", "Repository analytics");
		settings.addMetaTag("og:url",
				"https://github.com/Joaquin-GM/GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software/");
		settings.addMetaTag("og:image", "https://evolution-metrics-v2.herokuapp.com/images/LOGOAPP.png");

		settings.addLink("shortcut icon", "frontend/icons/favicon.ico");
		settings.addFavIcon("icon", "frontend/icons/favicon.ico", "192x192");
	}
}
