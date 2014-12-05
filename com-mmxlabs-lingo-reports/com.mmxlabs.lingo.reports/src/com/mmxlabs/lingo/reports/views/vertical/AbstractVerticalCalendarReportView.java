/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

/**
 * Class for providing "vertical" schedule reports. Each row is a calendar day in the schedule; each column typically represents a sequence (series of events) in the schedule.
 * <p/>
 * 
 * Override {@link#getCols(ScheduleSequenceData data)} to modify the columns, and override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to
 * Override {@link#getEventText(Date date, Event event)} and / or {@link#getEventText(Date date, Event [] events)} to modify the sequence cell contents.
 * 
 * @author Simon McGregor
 * 
 */
public abstract class AbstractVerticalCalendarReportView extends ViewPart {

	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;
//	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy");
	/** format for the "date" column */
	protected LNGScenarioModel root = null;
	protected Date[] dates = null;
	protected final HashMap<Event, Object> vesselsByEvent = new HashMap<>();
	protected AbstractVerticalReportVisualiser verticalReportVisualiser;

	protected AbstractVerticalCalendarReportView(AbstractVerticalReportVisualiser verticalReportVisualiser) {
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
			public void endCollecting() {

			}

			@Override
			public Collection<? extends Object> collectElements(final LNGScenarioModel rootObject, final boolean isPinned) {
				final ArrayList<LNGScenarioModel> result = new ArrayList<LNGScenarioModel>();
				result.add(rootObject);
				return result;
			}

			@Override
			public void beginCollecting() {

			}
		};
	}

	protected IStructuredContentProvider createContentProvider() {
		return new IStructuredContentProvider() {

			@Override
			public void dispose() {
				dates = null;
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

					if (gridViewer.getGrid().getItemCount() >= 2) {
						gridViewer.getGrid().getItem(2).setRowSpan(2, 2);
					}
				}
			}

			protected void setCols(final ScheduleSequenceData data) {
				// clear the grid columns; we will have to replace them with vessels from the new scenario
				for (final GridColumn column : gridViewer.getGrid().getColumns()) {
					column.dispose();
				}

				if (root != null) {
					createCols(data);
				}

				// regenerate the map linking events to vessels (or vessel classes if the event is handled by a chartered-in vessel)
				vesselsByEvent.clear();
				if (data != null && data.vessels != null) {
					for (final Sequence sequence : data.vessels) {
						// check to see if we are dealing with a specific vessel from the fleet or a nameless chartered-in vessel
						final Object vesselOrClass = sequence.isSpotVessel() ? sequence.getVesselClass() : sequence.getVesselAvailability().getVessel();

						if (vesselOrClass != null) {
							for (final Event event : sequence.getEvents()) {
								vesselsByEvent.put(event, vesselOrClass);
							}
						}
					}
				}

				gridViewer.refresh();

			}

			private void setRows(final ScheduleSequenceData data) {
				dates = VerticalReportUtils.getUTCDaysBetween(data.start, data.end).toArray(new Date[0]);
			}

			@Override
			/**
			 * Returns the list of calendar days for this report.
			 */
			public Object[] getElements(final Object inputElement) {
				return dates;
			}

		};
	}

	protected void setData(final ScheduleSequenceData data) {
	}

	/**
	 * Override this method to control the columns in the vertical report.
	 * 
	 * @param data
	 */

	abstract protected void createCols(ScheduleSequenceData data);

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		verticalReportVisualiser.dispose();

		super.dispose();
	}

	protected GridViewerColumn createEventColumn(final EventProvider eventProvider, final String title) {
		return createColumn(new EventColumnLabelProvider(eventProvider, verticalReportVisualiser), title);
	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final String title) {
		return createColumn(labeller, title, (GridColumn) null);
	}

	protected GridViewerColumn createEventColumn(final EventProvider eventProvider, final String name, final GridColumnGroup columnGroup) {
		return createColumn(new EventColumnLabelProvider(eventProvider, verticalReportVisualiser), name, columnGroup);
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

}
