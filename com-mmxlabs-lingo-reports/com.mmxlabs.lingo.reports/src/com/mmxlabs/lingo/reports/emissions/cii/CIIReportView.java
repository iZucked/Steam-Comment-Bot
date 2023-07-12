package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportJSONGenerator;
import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportModelV1;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class CIIReportView extends ScenarioInstanceView {

	private GridTableViewer gradesGridTableViewer;
	private Year earliestYear = Year.of(2020);
	private Year latestYear = Year.of(2029);

	private record VesselGrades(Vessel vessel, Map<Year, String> grades) {
	}

	@Override
	public void createPartControl(final Composite parent) {

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).create());
		parent.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		gradesGridTableViewer = new GridTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		gradesGridTableViewer.setContentProvider(ArrayContentProvider.getInstance());

		// create the columns
		createColumns();

		// make lines and header visible
		final Grid grid = gradesGridTableViewer.getGrid();
		grid.setHeaderVisible(true);
		grid.setLinesVisible(true);
		grid.setLayoutData(GridDataFactory.fillDefaults().minSize(0, 0).grab(true, true).create());

		listenToScenarioSelection();
	}

	private void createColumns() {
		createVesselColumn();
		for (Year year = earliestYear; !year.isAfter(latestYear); year = year.plusYears(1)) {
			createYearColumn(year);
		}
	}

	private void createVesselColumn() {
		final GridColumn vesselColumn = new GridColumn(gradesGridTableViewer.getGrid(), SWT.CENTER | SWT.WRAP);
		final GridViewerColumn viewerColumn = new GridViewerColumn(gradesGridTableViewer, vesselColumn);
		GridViewerHelper.configureLookAndFeel(viewerColumn);
		vesselColumn.setWidth(100);
		vesselColumn.setText("Vessel");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof final VesselGrades vesselGrades) {
					return vesselGrades.vessel.getName();
				}
				return element.toString();
			}
		});
	}

	private void createYearColumn(final Year year) {
		final GridColumn column = new GridColumn(gradesGridTableViewer.getGrid(), SWT.CENTER | SWT.WRAP);
		final GridViewerColumn viewerColumn = new GridViewerColumn(gradesGridTableViewer, column);
		GridViewerHelper.configureLookAndFeel(viewerColumn);
		viewerColumn.getColumn().setWidth(50);
		viewerColumn.getColumn().setText(year.toString());
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof final VesselGrades vesselGrades) {
					return vesselGrades.grades.getOrDefault(year, "-");
				}
				return element.toString();
			}
		});
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(gradesGridTableViewer);
	}

	@Override
	protected synchronized void doDisplayScenarioInstance(final @Nullable ScenarioInstance scenarioInstance, final @Nullable MMXRootObject rootObject, @Nullable Object target) {
		if (scenarioInstance == null) {
			this.rootObject = null;
		} else {
			this.rootObject = rootObject;
			setInput(this.rootObject);
		}
	}

	private ScheduleModel getScheduleModel() {
		if (rootObject != null && rootObject instanceof final LNGScenarioModel lngScenarioModel) {
			return ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		}
		return null;
	}

	private void setInput(final @Nullable MMXRootObject mmxRootObject) {
		if (mmxRootObject != null) {
			this.rootObject = mmxRootObject;
			updateTable();
		}
	}

	private void updateTable() {
		final ScheduleModel scheduleModel = getScheduleModel();
		if (scheduleModel == null) {
			return;
		}
		final List<VesselEmissionAccountingReportModelV1> vesselEventEmissionModels = VesselEmissionAccountingReportJSONGenerator.createReportData(scheduleModel, false, "-");

		final Map<Vessel, Map<Year, CumulativeCII>> vesselYearCIIs = new HashMap<>();
		for (final VesselEmissionAccountingReportModelV1 model : vesselEventEmissionModels) {
			final Vessel vessel = model.getVessel();
			vesselYearCIIs.computeIfAbsent(vessel, v -> new HashMap<>());
			final Map<Year, CumulativeCII> vesselCIIs = vesselYearCIIs.get(vessel);

			final LocalDate startDate = model.eventStart.toLocalDate();
			final LocalDate endDate = model.eventStart.toLocalDate();
			if (startDate.getYear() == endDate.getYear()) {
				final Year year = Year.of(startDate.getYear());
				vesselCIIs.computeIfAbsent(year, y -> new CumulativeCII(vessel));
				final CumulativeCII cumulativeCII = vesselCIIs.get(year);
				cumulativeCII.accumulateEventModel(model);
			} else {
				final LocalDate newYearMidPoint = LocalDate.of(startDate.getYear(), 12, 31);
				final double earlyEventLinearInterpolationCoefficient = Duration.between(startDate, newYearMidPoint).toDays() / (double) Duration.between(startDate, endDate).toDays();
				final double lateEventLinearInterpolationCoefficient = 1 - earlyEventLinearInterpolationCoefficient;
				
				final Year startYear = Year.of(startDate.getYear());
				vesselCIIs.computeIfAbsent(startYear, y -> new CumulativeCII(vessel));
				vesselCIIs.get(startYear).accumulateEventModel(model, earlyEventLinearInterpolationCoefficient);

				final Year endYear = Year.of(endDate.getYear());
				vesselCIIs.computeIfAbsent(endYear, y -> new CumulativeCII(vessel));
				vesselCIIs.get(endYear).accumulateEventModel(model, lateEventLinearInterpolationCoefficient);
			}
		}
		
		final List<VesselGrades> grades = new LinkedList<>();
		vesselYearCIIs.forEach((vessel, accumulatedCIIs) -> {
			final Map<Year, String> vesselGradesMap = new HashMap<>();
			accumulatedCIIs.forEach((year, cumulativeCII) -> {
				final String letterGrade = UtilsCII.getLetterGrade(vessel, cumulativeCII.findCII());
				vesselGradesMap.put(year, letterGrade);
			});
			grades.add(new VesselGrades(vessel, vesselGradesMap));
		});

		gradesGridTableViewer.setInput(grades);
	}
}
