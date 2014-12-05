/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

/**
 * New version of the vertical report, with a separate event per display cell.
 * This requires many cells to be merged, so the behaviour of the columns is no longer
 * independent. 
 * 
 * Needs to be tested against the previous vertical report, to check correctness. Then
 * the previous report class should be replaced with this.
 * 
 * Changes:
 * - Key method #createCalendarCols now returns a list of CalendarColumn objects.
 *   These objects each require an EventProvider (which maps from Date objects to Event [] arrays)
 *   and an EventLabelProvider (which describes how to display an Event object on a particular Date).
 * 
 * Known issues:
 * - Column groups are now missing from the column headers. 
 * 	 Modify CalendarColumn constructor and ReportNebulaGridManager#createNebulaColumns to 
 * 	 create column groups (see previous version of vertical reports for guidance)
 * 
 * - Row headers (grey boxes with dates) are split across different rows. 
 *   Modify new AbstractRenderer()#paint in createPartControl.
 *   
 * - Code is even messier than before.
 *   
 * - Export to Excel format may be broken due to cell merging. Not tested.
 * 
 */

/**
 * Class for providing "vertical" schedule reports. Each row is a calendar day in the schedule; each column typically represents a sequence (series of events) in the schedule.
 * <p/>
 * 
 * Override {@link#getCols(ScheduleSequenceData data)} to modify the columns, and override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to
 * Override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to modify the sequence cell contents.
 * 
 * 
 * @author Simon McGregor
 * 
 */
public abstract class NewAbstractVerticalCalendarReportView extends ViewPart {

	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;
	// protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy");
	/** format for the "date" column */
	protected LNGScenarioModel root = null;
	protected Date[] dates = null;

	protected ReportNebulaGridManager manager;

	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	protected NewAbstractVerticalCalendarReportView(final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super();
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		final FillLayout layout = new FillLayout();
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		gridViewer = new GridTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		gridViewer.setContentProvider(createContentProvider());

		gridViewer.getGrid().setHeaderVisible(true);
		gridViewer.getGrid().setLinesVisible(true);
		// gridViewer.getGrid().setAutoHeight(true);

		gridViewer.getGrid().setRowHeaderVisible(true);

		jobManagerListener = ScenarioViewerSynchronizer.registerView(gridViewer, createElementCollector());

		makeActions();
	}

	protected void makeActions() {
		final PackGridTableColumnsAction packColumnsAction = PackActionFactory.createPackColumnsAction(gridViewer);
		final CopyGridToClipboardAction copyToClipboardAction = CopyToClipboardActionFactory.createCopyToClipboardAction(gridViewer);
		copyToClipboardAction.setRowHeadersIncluded(true);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyToClipboardAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyToClipboardAction);
	}

	private IScenarioInstanceElementCollector createElementCollector() {
		return new IScenarioInstanceElementCollector() {

			@Override
			public void beginCollecting() {

			}

			@Override
			public Collection<? extends Object> collectElements(final LNGScenarioModel rootObject, final boolean isPinned) {
				final List<LNGScenarioModel> result = new ArrayList<LNGScenarioModel>();
				result.add(rootObject);
				return result;
			}

			@Override
			public void endCollecting() {

			}
		};
	}

	protected IStructuredContentProvider createContentProvider() {
		manager = new ReportNebulaGridManager();
		return manager;
	}

	/**
	 * Default implementation of setData() method: do nothing when the relevant data is set for this report. Override this method to perform any custom processing (e.g. setup) when data is provided to
	 * this report.
	 * 
	 * @param data
	 */
	protected void setData(final ScheduleSequenceData data) {
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		verticalReportVisualiser.dispose();

		super.dispose();
	}

	/**
	 * Should return the text associated with an individual event on a given day.
	 * 
	 * @param eventColumnLabelProvider
	 */
	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String title) {
		return createColumn(labeller, title, (GridColumn) null);
	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String name, final GridColumnGroup columnGroup) {
		final GridColumn column = new GridColumn(columnGroup, SWT.NONE);
		return createColumn(labeller, name, column);

	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String name, final GridColumn column) {
		final GridViewerColumn result;
		if (column == null) {
			result = new GridViewerColumn(gridViewer, SWT.NONE);
		} else {
			result = new GridViewerColumn(gridViewer, column);
		}
		result.setLabelProvider(labeller);
		result.getColumn().setText(name);
		result.getColumn().pack();

		return result;

	}

	/**
	 * Class that makes the relevant API calls to the nebula Grid widget viewer framework.
	 * 
	 * The grid widget assumes a data model based on rows, which in our case have to correspond to <Date, Int> pairs indicating a date and an event index.
	 * 
	 * This class has to create the relevant content model expected by the Grid framework, calculate which Grid cells are merged in every column, pass this information on to the widget, create
	 * relevant column formatting objects, and hook them up to the widget.
	 * 
	 * @author mmxlabs
	 * 
	 */
	protected class ReportNebulaGridManager implements IStructuredContentProvider {
		private Pair<Date, Integer>[] nebulaElements;
		private List<CalendarColumn> calendarColumns;
		private List<GridViewerColumn> nebulaColumns;

		public Event[] getLogicalCellContents(final Date date, final CalendarColumn column) {
			return column.getProvider().getEvents(date);
		}

		public int getNumRowsRequired(final Date date, final CalendarColumn column) {
			final Event[] contents = getLogicalCellContents(date, column);
			if (contents != null && contents.length > 1) {
				return contents.length;
			}
			return 1;
		}

		public int getNumRowsRequired(final Date date) {
			int result = 1;
			for (final CalendarColumn column : calendarColumns) {
				final int num = getNumRowsRequired(date, column);
				if (num > result) {
					result = num;
				}
			}
			return result;
		}

		@Override
		public void dispose() {
			nebulaElements = null;
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (newInput != null) {
				// svso.getCollectedElements in this case returns a singleton list containing the root object
				final IScenarioViewerSynchronizerOutput svso = (IScenarioViewerSynchronizerOutput) newInput;
				for (final Object element : svso.getCollectedElements()) {
					root = (LNGScenarioModel) element;
				}
				// extract the relevant data from the root object
				final ScheduleSequenceData data = new ScheduleSequenceData(root);
				setData(data);
				// setup table columns and rows
				setCols(data);
				setRows(data);

			}
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			// TODO Auto-generated method stub
			return nebulaElements;
		}

		protected void setCols(final ScheduleSequenceData data) {
			// clear the grid columns; we will have to replace them with vessels from the new scenario
			for (final GridColumn column : gridViewer.getGrid().getColumns()) {
				column.dispose();
			}

			if (root != null) {
				calendarColumns = createCalendarCols(data);
			}

			createNebulaColumns();

			gridViewer.refresh();

		}

		@SuppressWarnings("unchecked")
		private void setRows(final ScheduleSequenceData data) {
			final List<Pair<Date, Integer>> result = new LinkedList<>();

			final Date[] allDates = VerticalReportUtils.getGMTDaysBetween(data.start, data.end).toArray(new Date[0]);
			for (final Date date : allDates) {
				for (int i = 0; i < getNumRowsRequired(date); i++) {
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
						nebulaColumns.add(createColumn(column.createColumnLabelProvider(this), column.getTitle(), column.getColumnGroup()));
					} else {
						nebulaColumns.add(createColumn(column.createColumnLabelProvider(this), column.getTitle()));
					}
				}
			}
		}

		void updateCell(final ViewerCell cell) {
			final GridItem item = (GridItem) cell.getItem();
			final int i = cell.getColumnIndex();

			final DataVisualizer dv = gridViewer.getGrid().getDataVisualizer();

			@SuppressWarnings("unchecked")
			final Pair<Date, Integer> pair = (Pair<Date, Integer>) item.getData();
			final Date date = pair.getFirst();
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

	}

	/**
	 * Override this method to control the columns in the vertical report.
	 * 
	 * @param data
	 */
	protected abstract List<CalendarColumn> createCalendarCols(final ScheduleSequenceData data);
}
