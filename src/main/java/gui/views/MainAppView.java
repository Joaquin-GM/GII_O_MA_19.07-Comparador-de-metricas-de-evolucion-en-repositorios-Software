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

	private Label appNameLabel = new Label("Evolution Metrics Gauge 2");

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
		header.setClassName("app-header");

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
				"https://github.com/Joaquin-GM/GII_O_MA_19.07-Comparador-de-metricas-de-evolucion-en-repositorios-Software/wiki");
		helpLink.setTarget("_blank");
		helpLink.add(helpButton);

		HorizontalLayout firstHeaderLayout = new HorizontalLayout(brandingImage, appNameLabel, logoImage);
		firstHeaderLayout.setClassName("first-header-layout");
		
		VerticalLayout connectionButtonLayout = new VerticalLayout(
				new HorizontalLayout(connectionButtonGitLab, connectionButtonGitHub, helpLink));
		connectionButtonLayout.addClassName("connection-buttons");
		connectionButtonLayout.setWidth("auto");
		// VerticalLayout connectionButtonLayout = new VerticalLayout(new
		// HorizontalLayout(connectionButtonGitLab, helpLink));

		connectionButtonLayout.setAlignItems(Alignment.END);
		// connectionButtonLayout.setWidth("40%");
		HorizontalLayout headerHLayout = new HorizontalLayout(firstHeaderLayout,connectionButtonLayout);
		headerHLayout.addClassName("header-layout");
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
		versionLabel.setText("Versión: 2.1.0");
		versionLabel.setId("Version");
		footerLeftSection.add(versionLabel);

		HorizontalLayout keyLayout = new HorizontalLayout();
		Div keyContainer = new Div();
		keyLayout.add(keyContainer);
		keyLayout.setWidth("30%");
		keyLayout.setMinWidth("215px");
		keyContainer.setWidth("100%");

		Div keyTitle = new Div();
		keyTitle.setText("Leyenda: ");
		keyTitle.setClassName("bold-label");
		keyContainer.add(keyTitle);
		
		keyContainer.add(generateKeyLabels("I:", " Issues"));
		keyContainer.add(generateKeyLabels("T:", " Tiempo"));
		keyContainer.add(generateKeyLabels("C:", " Commits"));
		keyContainer.add(generateKeyLabels("IC:", " Integración continua"));
		keyContainer.add(generateKeyLabels("DC:", " Despliegue continuo"));
		
		keyContainer.setId("Key");

		footerRightSection.add(keyLayout);
		
		HorizontalLayout criteriaLayout = new HorizontalLayout();
		Div criteriaContainer = new Div();
		criteriaLayout.add(criteriaContainer);
		criteriaLayout.setWidth("70%");
		criteriaLayout.setMinWidth("485px");
		keyContainer.setWidth("100%");

		Div criteriaTitle = new Div();
		Span criteriaTitleSpan = new Span();
		criteriaTitleSpan.setText("Criterios de evaluación");
		criteriaTitleSpan.setClassName("bold-label");
		criteriaTitle.add(criteriaTitleSpan);
		Span criteriaExplanation = new Span();
		criteriaExplanation.setText(" (verde valores deseados, rojo no deseados):");
		criteriaTitle.add(criteriaExplanation);
		criteriaContainer.add(criteriaTitle);
		
		Div firstCriteriaContainer = new Div();
		firstCriteriaContainer.setText("Se buscan valores superiores a umbral inferior");
		firstCriteriaContainer.setClassName("criteria-container");
		Span firstCriteriaContainerSpan = new Span();
		firstCriteriaContainerSpan.setText(" (>Q1):");
		firstCriteriaContainerSpan.setClassName("bold-label");
		firstCriteriaContainer.add(firstCriteriaContainerSpan);
		firstCriteriaContainer.setWidth("100%");
		Div firstCriteriaMetrics = new Div();
		firstCriteriaMetrics.setText(" I1, I3, IC1, IC2, IC3, DC1, DC2");
		firstCriteriaMetrics.setWidth("100%");
		firstCriteriaMetrics.setClassName("criteria-metrics bold-label");
		criteriaContainer.add(firstCriteriaContainer);
		criteriaContainer.add(firstCriteriaMetrics);
		
		Div secondCriteriaContainer = new Div();
		secondCriteriaContainer.setText("Se buscan valores entre umbrales");
		secondCriteriaContainer.setClassName("criteria-container");
		Span secondCriteriaContainerSpan = new Span();
		secondCriteriaContainerSpan.setText(" (>Q1,<Q3):");
		secondCriteriaContainerSpan.setClassName("bold-label");
		secondCriteriaContainer.add(secondCriteriaContainerSpan);
		secondCriteriaContainer.setWidth("100%");
		Div secondCriteriaMetrics = new Div();
		secondCriteriaMetrics.setText(" I2, TI1, TC1, TC2, TC3, C1");
		secondCriteriaMetrics.setWidth("100%");
		secondCriteriaMetrics.setClassName("criteria-metrics bold-label");
		criteriaContainer.add(secondCriteriaContainer);
		criteriaContainer.add(secondCriteriaMetrics);
		
		// criteriaContainer.add(generateKeyLabels("Se buscan valores superiores a umbral inferior (Q1):", " I1, I3, IC1, IC2, IC3, DC1, DC2"));
		// criteriaContainer.add(generateKeyLabels("Se buscan valores entre umbrales (>Q1,<Q3):", " I2, TI1, TC1, TC2, TC3, C1"));
		
		criteriaContainer.setId("Criteria");

		footerRightSection.add(criteriaLayout);

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
		titleSpan.setClassName("bold-label leyend-key");
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
