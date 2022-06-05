package gui.views;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.claspina.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.componentfactory.Tooltip;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import app.MetricsService;
import app.RepositoriesCollectionService;
import app.RepositoryDataSourceService;
import datamodel.Repository;
import datamodel.RepositorySourceType;
import exceptions.RepositoryDataSourceException;
import metricsengine.EvaluationResult;
import metricsengine.Measure;
import metricsengine.Metric;
import metricsengine.MetricConfiguration;
import metricsengine.MetricsResults;
import metricsengine.numeric_value_metrics.MetricAverageDaysBetweenCommits;
import metricsengine.numeric_value_metrics.MetricAverageDaysToCloseAnIssue;
import metricsengine.numeric_value_metrics.MetricChangeActivityRange;
import metricsengine.numeric_value_metrics.MetricCommitsPerIssue;
import metricsengine.numeric_value_metrics.MetricDaysBetweenFirstAndLastCommit;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfJobs;
import metricsengine.numeric_value_metrics.MetricJobsLastYear;
import metricsengine.numeric_value_metrics.MetricPeakChange;
import metricsengine.numeric_value_metrics.MetricPercentageClosedIssues;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfReleases;
import metricsengine.numeric_value_metrics.MetricReleasesLastYear;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfIssues;
import metricsengine.numeric_value_metrics.MetricTotalNumberOfJobTypes;
import metricsengine.numeric_value_metrics.ProjectEvaluation;
import metricsengine.values.IValue;
import metricsengine.values.NumericValue;
import metricsengine.values.ValueUncalculated;

/**
 * @author Miguel Ángel León Bardavío - mlb0029
 * @author Joaquin Garcia Molina - Joaquin-GM
 */
public class RepositoriesGrid extends Grid<Repository> {

	private static final long serialVersionUID = 1439924355112013872L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoriesGrid.class);

	private static final String NOT_CALCULATED = "NC";

	private Map<Class<? extends Metric>, Label> metricConfigTip = new HashMap<Class<? extends Metric>, Label>();

	/**
	 * Constructor.
	 *
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 */
	public RepositoriesGrid() {
		super(Repository.class);
		initializeGrid();
		MetricsService.getMetricsService()
				.addCurrentMetricProfileChangedEventListener(event -> onMetricProfileChanged());
	}

	private void initializeGrid() {
		Span header = null;
		String headerText;
		String headerTitle;

		setWidthFull();
		setColumns();
		// repositoriesGrid.setHeightByRows(true);

		addComponentColumn(repository -> createRemoveButton(repository)).setKey("removeButtonColumn").setSortable(false)
				.setWidth("5em");

		header = new Span("Project");
		header.setTitle("Project name");
		header.setClassName("custom-header");
		addComponentColumn(r -> createProjectNameLink(r)).setKey("repositoryNameColumn").setWidth("12em")
				.setSortable(true).setComparator(Repository::getName).setHeader(header);

		VerticalLayout headerL = new VerticalLayout();
		headerL.setClassName("custom-sorter");
		header = new Span("Date");
		header.setTitle("Measurement date");
		header.setClassName("sorter-header-with-border");
		headerL.add(header);
		addComponentColumn(r -> {
			String date = getLastMeasurementDate(r);
			Label dateLbl = new Label(date);
			dateLbl.setTitle(date);
			return dateLbl;
		}).setKey("lastMeasurementDateColumn").setHeader(headerL).setSortable(true)
				.setComparator(r -> r.getRepositoryInternalMetrics().getDate()).setWidth("6em")
				.setTextAlign(ColumnTextAlign.CENTER);

		// Process orientation metrics
		headerText = MetricTotalNumberOfIssues.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricTotalNumberOfIssues.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> i1MetricColumn = addMetricColumn("i1MetricColumn", headerText, headerTitle, "",
				MetricTotalNumberOfIssues.class);

		headerText = MetricCommitsPerIssue.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricCommitsPerIssue.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> i2MetricColumn = addMetricColumn("i2MetricColumn", headerText, headerTitle, "",
				MetricCommitsPerIssue.class);

		headerText = MetricPercentageClosedIssues.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricPercentageClosedIssues.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> i3MetricColumn = addMetricColumn("i3MetricColumn", headerText, headerTitle, "%",
				MetricPercentageClosedIssues.class);

		headerText = MetricAverageDaysToCloseAnIssue.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricAverageDaysToCloseAnIssue.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> ti1MetricColumn = addMetricColumn("ti1MetricColumn", headerText, headerTitle, "",
				MetricAverageDaysToCloseAnIssue.class);

		// Time constraints metrics
		headerText = MetricAverageDaysBetweenCommits.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricAverageDaysBetweenCommits.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> tc1MetricColumn = addMetricColumn("tc1MetricColumn", headerText, headerTitle, "",
				MetricAverageDaysBetweenCommits.class);

		headerText = MetricDaysBetweenFirstAndLastCommit.DEFAULT_METRIC_DESCRIPTION.getName();
		headerTitle = MetricDaysBetweenFirstAndLastCommit.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> tc2MetricColumn = addMetricColumn("tc2MetricColumn", headerText, headerTitle, "",
				MetricDaysBetweenFirstAndLastCommit.class);

		headerText = MetricChangeActivityRange.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricChangeActivityRange.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> tc3MetricColumn = addMetricColumn("tc3MetricColumn", headerText, headerTitle, "",
				MetricChangeActivityRange.class);

		headerText = MetricPeakChange.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricPeakChange.DEFAULT_METRIC_DESCRIPTION.getDescription();
		Grid.Column<Repository> c1MetricColumn = addMetricColumn("c1MetricColumn", headerText, headerTitle, "",
				MetricPeakChange.class);

		// CI/CD metrics
		Grid.Column<Repository> ic1MetricColumn = null;
		headerText = MetricTotalNumberOfJobs.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricTotalNumberOfJobs.DEFAULT_METRIC_DESCRIPTION.getDescription();
		ic1MetricColumn = addMetricColumn("ic1MetricColumn", headerText, headerTitle, "",
				MetricTotalNumberOfJobs.class);

		Grid.Column<Repository> ic2MetricColumn = null;
		headerText = MetricJobsLastYear.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricJobsLastYear.DEFAULT_METRIC_DESCRIPTION.getDescription();
		ic2MetricColumn = addMetricColumn("ic2MetricColumn", headerText, headerTitle, "", MetricJobsLastYear.class);

		Grid.Column<Repository> ic3MetricColumn = null;
		headerText = MetricTotalNumberOfJobTypes.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricTotalNumberOfJobTypes.DEFAULT_METRIC_DESCRIPTION.getDescription();
		ic3MetricColumn = addMetricColumn("ic3MetricColumn", headerText, headerTitle, "",
				MetricTotalNumberOfJobTypes.class);

		Grid.Column<Repository> dc1MetricColumn = null;
		headerText = MetricTotalNumberOfReleases.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricTotalNumberOfReleases.DEFAULT_METRIC_DESCRIPTION.getDescription();
		dc1MetricColumn = addMetricColumn("dc1MetricColumn", headerText, headerTitle, "",
				MetricTotalNumberOfReleases.class);

		Grid.Column<Repository> dc2MetricColumn = null;
		headerText = MetricReleasesLastYear.DEFAULT_METRIC_DESCRIPTION.getName().split("-")[0];
		headerTitle = MetricReleasesLastYear.DEFAULT_METRIC_DESCRIPTION.getDescription();
		dc2MetricColumn = addMetricColumn("dc2MetricColumn", headerText, headerTitle, "", MetricReleasesLastYear.class);

		/* Grid.Column<Repository> projectEvaluation = */ addProjectEvalColumn();

		addComponentColumn(repository -> createCalculateButton(repository)).setKey("calculateButtonColumn")
				.setWidth("5em").setTextAlign(ColumnTextAlign.END);
		addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
		setMultiSort(true);

		HeaderRow metricsClassification = prependHeaderRow();

		Div procOrientHeader = new Div(new Span("Process Orientation"));
		procOrientHeader.getStyle().set("text-align", "center");
		procOrientHeader.setSizeFull();
		procOrientHeader.addClassName("metrics-group-header");
		procOrientHeader.addClassName("left-lightgray-border");
		procOrientHeader.addClassName("right-lightgray-border");
		metricsClassification.join(i1MetricColumn, i2MetricColumn, i3MetricColumn).setComponent(procOrientHeader);

		Div timeConstraintsHeader = new Div(new Span("Time Constraints"));
		timeConstraintsHeader.getStyle().set("text-align", "center");
		timeConstraintsHeader.setSizeFull();
		timeConstraintsHeader.addClassName("metrics-group-header");
		timeConstraintsHeader.addClassName("right-lightgray-border");
		metricsClassification.join(ti1MetricColumn, tc1MetricColumn, tc2MetricColumn, tc3MetricColumn, c1MetricColumn)
				.setComponent(timeConstraintsHeader);

		Div CICDHeader = new Div(new Span("CI/CD"));
		CICDHeader.addClassName("metrics-group-header");
		CICDHeader.addClassName("right-lightgray-border");
		Icon questionIcon = VaadinIcon.INFO_CIRCLE_O.create();
		questionIcon.setSize("16px");
		questionIcon.setColor("blue");
		questionIcon.setClassName("info-hover-icon");

		Tooltip tooltip = new Tooltip();
		tooltip.attachToComponent(questionIcon);
		tooltip.setClassName("custom-tooltip");
		tooltip.add(new Paragraph(
				"CI/CD related metrics need authenticated connection of GiLab (username and password or personal access token)"));

		CICDHeader.add(questionIcon, tooltip);

		CICDHeader.getStyle().set("text-align", "center");
		CICDHeader.setSizeFull();
		metricsClassification.join(ic1MetricColumn, ic2MetricColumn, ic3MetricColumn, dc1MetricColumn, dc2MetricColumn)
				.setComponent(CICDHeader);
	}

	private Grid.Column<Repository> addMetricColumn(String key, String headerText, String headerTitle,
			String metricUnits, Class<? extends Metric> metricType) {
		VerticalLayout header = new VerticalLayout();
		
		header.addClassName("metric-sorter");
		if(headerText.equals("I3") || headerText.equals("C1 ") || headerText.equals("DC2")) {
			header.addClassName("border-metric-sorter");
		}

		Label descriptionLabel = new Label(headerText);
		descriptionLabel.setTitle(headerTitle);
		header.add(descriptionLabel);
		header.setPadding(false);
		Label configLabel = new Label("");
		setMetricConfigTip(metricType, configLabel);
		header.add(configLabel);
		metricConfigTip.put(metricType, configLabel);
		header.setAlignItems(Alignment.END);
		Grid.Column<Repository> metricColumn = addComponentColumn(r -> {
			String value = getValueMeasuredForMetric(r, metricType);
			if (value != NOT_CALCULATED)
				value += metricUnits;
			Span span = new Span(value);
			span.setClassName(getClassNameByEvaluation(r, metricType));
			if (value.equals(NOT_CALCULATED))
				span.setTitle("Not calculated");
			return span;
		}).setKey(key).setSortable(true).setComparator(Repository.getComparatorByMetric(metricType)).setHeader(header)
				.setWidth("8em").setTextAlign(ColumnTextAlign.END);
		return metricColumn;
	}

	private Grid.Column<Repository> addProjectEvalColumn() {
		Label descriptionLabel = new Label("Calif.");
		descriptionLabel.setTitle("Percentage of metrics rated as 'good'");
		Grid.Column<Repository> metricColumn = addComponentColumn(r -> {
			String value = getValueMeasuredForMetric(r, ProjectEvaluation.class);
			if (value != NOT_CALCULATED)
				value += "%";
			Span span = new Span(value);
			span.setClassName("metricEvaluation");
			if (value.equals(NOT_CALCULATED))
				span.setTitle("Not calculated");
			return span;
		}).setKey("projectCalification").setSortable(true)
				.setComparator(Repository.getComparatorByMetric(ProjectEvaluation.class)).setHeader(descriptionLabel)
				.setWidth("8em").setTextAlign(ColumnTextAlign.END);
		return metricColumn;
	}

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param metricType
	 * @param configLabel
	 */
	private void setMetricConfigTip(Class<? extends Metric> metricType, Label configLabel) {
		MetricConfiguration metricConfiguration = MetricsService.getMetricsService().getCurrentMetricProfile()
				.getMetricConfigurationByMetric(metricType);
		if (metricConfiguration != null) {
			configLabel.setText(formatStringTwoDecimals(metricConfiguration.getValueMin().getValueString()) + " - "
					+ formatStringTwoDecimals(metricConfiguration.getValueMax().getValueString()));
			configLabel.setTitle("Q1 = " + formatStringTwoDecimals(metricConfiguration.getValueMin().getValueString())
					+ " & Q3 = " + formatStringTwoDecimals(metricConfiguration.getValueMax().getValueString()));
			configLabel.addClassName("MetricConfigTip");
		}
	}

	private void onMetricProfileChanged() {
		for (Entry<Class<? extends Metric>, Label> mct : metricConfigTip.entrySet()) {
			setMetricConfigTip(mct.getKey(), mct.getValue());
		}
	}

	private Button createRemoveButton(Repository repository) {
		Button button = new Button();
		button.setIcon(new Icon(VaadinIcon.TRASH));
		button.getElement().setProperty("title", "Remove project");
		button.addClickListener(event -> {
			try {
				RepositoriesCollectionService.getInstance().removeRepository(repository);
				getDataProvider().refreshAll();
			} catch (Exception e) {
				LOGGER.error("Error deleting a repository. Exception occurred: " + e.getMessage());
				ConfirmDialog.createError().withCaption("Error").withMessage(
						"An error has occurred while deleting the repository. Please, contact the application administrator.")
						.withOkButton().open();
			}
		});
		return button;
	}

	private Div createProjectNameLink(Repository repository) {
		Div div = new Div();
		if (repository.getUrl() != null && repository.getUrl() != "") {
			Anchor repositoryNameLink = new Anchor(repository.getUrl(), repository.getName());
			div.add(repositoryNameLink);
		} else {
			Label repositoryNameLabel = new Label(repository.getName());
			div.add(repositoryNameLabel);
		}
		div.setTitle(repository.getName());
		div.setClassName("project-title");
		return div;
	}

	private Button createCalculateButton(Repository repository) {
		Button button = new Button();
		button.setIcon(new Icon(VaadinIcon.CALC_BOOK));
		button.addClickListener(event -> {
			updateRepositoryInfo(repository);
		});
		return button;
	}

	/**
	 * Description.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @author Joaquin Garcia Molina - Joaquin-GM
	 * @param repository
	 */
	private void updateRepositoryInfo(Repository repository) {
		try {
			if (repository.getRepositoryDataSourceType().equals(RepositorySourceType.GitLab)) {
				RepositoryDataSourceService.getInstance().updateRepository(repository, RepositorySourceType.GitLab);
			} else if (repository.getRepositoryDataSourceType().equals(RepositorySourceType.GitHub)) {
				RepositoryDataSourceService.getInstance().updateRepository(repository, RepositorySourceType.GitHub);
			}
			
			MetricsService.getMetricsService().obtainAndEvaluateRepositoryMetrics(repository);
			getDataProvider().refreshAll();
			ConfirmDialog.createInfo().withCaption("Sucessful").withMessage("Repository metrics achieved")
					.withOkButton().open();
		} catch (RepositoryDataSourceException e) {
			if (e.getErrorCode() == RepositoryDataSourceException.REPOSITORY_NOT_FOUND) {
				LOGGER.warn("Attempt to recalculate metrics from a repository without access.");
				ConfirmDialog.createWarning().withCaption("Error")
						.withMessage("The repository can not be accessed with the current connection.").withOkButton()
						.open();
			} else if(e.getErrorCode() == RepositoryDataSourceException.NOT_CONNECTED) {
				LOGGER.error("An error occurred while obtaining the metrics of the repository. Exception occurred: "
						+ e.getMessage());
				ConfirmDialog.createError().withCaption("Error").withMessage(
						"You are not connected to " + repository.getRepositoryDataSourceType().toString() + ", please check your connection to uptade the repository data.")
						.withOkButton().open();
			} else {
				LOGGER.error("An error occurred while obtaining the metrics of the repository. Exception occurred: "
						+ e.getMessage());
				ConfirmDialog.createError().withCaption("Error").withMessage(
						"An error occurred while obtaining the metrics of the repository. Check the connection with " + repository.getRepositoryDataSourceType().toString() + " and if the problem persists please, contact the application administrator.")
						.withOkButton().open();
			}
		} catch (Exception e) {
			LOGGER.error("An error occurred while obtaining the metrics of the repository. Exception occurred: "
					+ e.getMessage());
			ConfirmDialog.createError().withCaption("Error").withMessage(
					"An error occurred while obtaining the metrics of the repository. Check the connection with " + repository.getRepositoryDataSourceType().toString() + " and if the problem persists please, contact the application administrator.")
					.withOkButton().open();
		}
	}

	private String getLastMeasurementDate(Repository repository) {
		MetricsResults mr = repository.getMetricsResults();
		if (mr == null)
			return ValueUncalculated.VALUE;
		return formatDateShortEs(mr.getLastModificationDate());
	}

	private Measure getMeasureForMetric(Repository repository, Class<? extends Metric> metricType) {
		MetricsResults mr = repository.getMetricsResults();
		if (mr == null)
			return null;
		Measure measure = mr.getMeasureForTheMetric(metricType);
		if (measure == null)
			return null;
		return measure;
	}

	private String getClassNameByEvaluation(Repository repository, Class<? extends Metric> metricType) {
		String classNames = "metricEvaluation ";
		Measure measureForMetric = getMeasureForMetric(repository, metricType);
		
		if (measureForMetric != null) {
			EvaluationResult result = measureForMetric.evaluate();
			switch (result) {
			case GOOD:
				classNames = classNames + " metricEvaluationGood";
				break;
			case WARNING:
				classNames = classNames + " metricEvaluationWarning";
				break;
			case BAD:
				classNames = classNames + " metricEvaluationBad";
				break;
			default:
				break;
			}
		} else {
			classNames = classNames + " metricEvaluationBad";
		}

		if (metricType.toString().equals("class metricsengine.numeric_value_metrics.MetricTotalNumberOfIssues")) {
			classNames = classNames + " left-lightgray-border";
		}
		
		if (metricType.toString().equals("class metricsengine.numeric_value_metrics.MetricPercentageClosedIssues") || 
				metricType.toString().equals("class metricsengine.numeric_value_metrics.MetricPeakChange") || 
				metricType.toString().equals("class metricsengine.numeric_value_metrics.MetricReleasesLastYear")) {
			classNames = classNames + " right-lightgray-border";
		}
		
		return classNames;
	}

	private String getValueMeasuredForMetric(Repository repository, Class<? extends Metric> metricType) {
		Measure measure;
		if (metricType == ProjectEvaluation.class) {
			measure = repository.getProjectEvaluation();
		} else {
			measure = getMeasureForMetric(repository, metricType);
		}
		if (measure == null)
			return NOT_CALCULATED;
		IValue value = measure.getMeasuredValue();
		if (value == null)
			return NOT_CALCULATED;
		if (value instanceof NumericValue) {
			Double d = ((NumericValue) value).doubleValue();
			return formatStringTwoDecimals(d.toString());
		} else if (value instanceof ValueUncalculated)
			return NOT_CALCULATED;
		else
			return value.getValueString();
	}

	/**
	 * Formatea una fecha y devuelve una cadena de texto.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param date Fecha a formatear.
	 * @return Fecha con formato SHORT para España.
	 */
	private String formatDateShortEs(Date date) {
		return SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag("es-ES")).format(date);
	}

	/**
	 * Formatea una cadena numérica en una cadena nuérica con dos decimales.
	 * 
	 * @author Miguel Ángel León Bardavío - mlb0029
	 * @param numberString Número a formatear.
	 * @return
	 */
	private String formatStringTwoDecimals(String numberString) {
		try {
			String valueFormated = "";
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(2);
			if (NumberUtils.isNumber(numberString)) {
				valueFormated = numberFormat.format(Double.parseDouble(numberString));
			}
			return valueFormated;
		} catch (Exception e) {
			LOGGER.error("Error formatting the string: " + numberString + ". Exception occurred: " + e.getMessage());
			ConfirmDialog.createError().withCaption("Error")
					.withMessage("An error occurred while formatting this number: '" + numberString
							+ "'.Please, contact the application administrator.")
					.withOkButton().open();
			return "";
		}
	}
}
