package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.joda.time.LocalDate;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class that makes the relevant API calls to the nebula Grid widget viewer framework.
 * 
 * The grid widget assumes a data model based on rows, which in our case have to correspond to <Date, Int> pairs indicating a date and an event index.
 * 
 * This class has to create the relevant content model expected by the Grid framework, calculate which Grid cells are merged in every column, pass this information on to the widget, create relevant
 * column formatting objects, and hook them up to the widget.
 * 
 * @author mmxlabs
 * 
 */
public class ReportNebulaGridManager implements IStructuredContentProvider {
	private Pair<Date, Integer>[] nebulaElements;
	private List<CalendarColumn> calendarColumns;
	private List<GridViewerColumn> nebulaColumns;
	private final AbstractVerticalReportVisualiser verticalReportVisualiser;
	private final Map<LocalDate, Integer> rowCache = new HashMap<>();
	private final AbstractVerticalCalendarReportView verticalReport;

	protected LNGScenarioModel root = null;

	public ReportNebulaGridManager(AbstractVerticalCalendarReportView verticalReport, AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this.verticalReport = verticalReport;
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	public Event[] getLogicalCellContents(final LocalDate date, final CalendarColumn column) {
		return column.getProvider().getEvents(date);
	}

	public int getNumRowsRequired(final LocalDate date, final CalendarColumn column) {
		final Event[] contents = getLogicalCellContents(date, column);
		if (contents != null && contents.length > 1) {
			return contents.length;
		}
		return 1;
	}

	public int getNumRowsRequired(final LocalDate date) {

		if (rowCache.containsKey(date)) {
			return rowCache.get(date);
		}

		int result = 1;
		for (final CalendarColumn column : calendarColumns) {
			final int num = getNumRowsRequired(date, column);
			if (num > result) {
				result = num;
			}
		}
		rowCache.put(date, result);
		return result;
	}

	@Override
	public void dispose() {
		nebulaElements = null;
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		final ScheduleSequenceData data;
		root = null;
		rowCache.clear();
		verticalReportVisualiser.inputChanged();
		if (newInput != null) {
			// svso.getCollectedElements in this case returns a singleton list containing the root object
			final IScenarioViewerSynchronizerOutput svso = (IScenarioViewerSynchronizerOutput) newInput;
			for (final Object element : svso.getCollectedElements()) {
				root = (LNGScenarioModel) element;
			}

			// extract the relevant data from the root object
			data = new ScheduleSequenceData(root, verticalReportVisualiser);
		} else {
			data = new ScheduleSequenceData(null, verticalReportVisualiser);
		}

		verticalReport.setData(data);
		// setup table columns and rows
		setCols(data);
		setRows(data);
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return nebulaElements;
	}

	protected void setCols(final ScheduleSequenceData data) {
		// clear the grid columns; we will have to replace them with vessels from the new scenario
		for (final GridColumn column : verticalReport.gridViewer.getGrid().getColumns()) {
			column.dispose();
		}
		if (root != null) {
			calendarColumns = verticalReport.createCalendarCols(data);
		} else {
			if (calendarColumns != null) {
				calendarColumns.clear();
			}
		}
		createNebulaColumns();
	}

	@SuppressWarnings("unchecked")
	private void setRows(final ScheduleSequenceData data) {
		final List<Pair<LocalDate, Integer>> result = new LinkedList<>();

		final LocalDate[] allDates = VerticalReportUtils.getUTCDaysBetween(data.start, data.end).toArray(new LocalDate[0]);
		for (final LocalDate date : allDates) {
			final int numRowsRequired = getNumRowsRequired(date);
			for (int i = 0; i < numRowsRequired; i++) {
				result.add(new Pair<>(date, i));
			}
		}

		nebulaElements = result.toArray(new Pair[0]);
	}

	private void createNebulaColumns() {
		nebulaColumns = new ArrayList<>();
		if (calendarColumns != null) {
			for (final CalendarColumn column : calendarColumns) {
				if (column.getColumnGroup() != null) {
					nebulaColumns.add(verticalReport.createColumn(column.createColumnLabelProvider(this), column.getTitle(), column.getColumnGroup()));
				} else {
					nebulaColumns.add(verticalReport.createColumn(column.createColumnLabelProvider(this), column.getTitle()));
				}
			}
		}
	}

	public void updateCell(final ViewerCell cell) {
		final GridItem item = (GridItem) cell.getItem();
		final int i = cell.getColumnIndex();

		final DataVisualizer dv = verticalReport.gridViewer.getGrid().getDataVisualizer();

		@SuppressWarnings("unchecked")
		final Pair<LocalDate, Integer> pair = (Pair<LocalDate, Integer>) item.getData();
		final LocalDate date = pair.getFirst();
		final int index = pair.getSecond();

		final int totalRows = getNumRowsRequired(date);

		// we may need to merge the cells in the visualiser
		// if there are fewer events in this column than
		// in another column, the last event in this column
		// will have to take up additional rows
		final CalendarColumn column = calendarColumns.get(i);
		final int cellRows = getNumRowsRequired(date, column);
		if (index == cellRows - 1) {
			dv.setRowSpan(item, i, totalRows - cellRows);
		}
	}

	public LNGScenarioModel getRoot() {
		return root;
	}

}
