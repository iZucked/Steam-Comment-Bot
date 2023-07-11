package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.Year;
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

import com.mmxlabs.models.lng.fleet.FleetFactory;
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
	private List<VesselGrades> grades;
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

	@SuppressWarnings("null")
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
	
	private Vessel tempMockVessel(String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		return vessel;
	}

	private void updateTable() {
		
		grades = List.of(
				new VesselGrades(
						tempMockVessel("Vessel XXX"),
						Map.of(
								Year.of(2021), "A",
								Year.of(2022), "B",
								Year.of(2023), "C",
								Year.of(2024), "A"
						)
				),
				new VesselGrades(
						tempMockVessel("Vessel YYY"),
						Map.of(
								Year.of(2021), "D",
								Year.of(2022), "B",
								Year.of(2023), "A",
								Year.of(2024), "A"
								)
						),
				new VesselGrades(
						tempMockVessel("Vessel ZZZ"),
						Map.of(
								Year.of(2021), "A",
								Year.of(2022), "-",
								Year.of(2023), "C",
								Year.of(2024), "A",
								Year.of(2026), "D"
								)
						)
		);
		gradesGridTableViewer.setInput(grades);
	}
}
