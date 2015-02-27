/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.lingo.reports.views.vertical.providers.EventProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
 * 
 * @See https://docs.google.com/a/minimaxlabs.com/document/d/1X9x3sHstBUS9F6zUTV_iAWBGFEs-9qolrkqTiQRjRJ8/edit#
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
public abstract class AbstractVerticalCalendarReportView extends ViewPart {

	public static final String ID = "com.mmxlabs.lingo.reports.verticalreport";
	
	protected GridTableViewer gridViewer;
	private ScenarioViewerSynchronizer jobManagerListener;

	protected ReportNebulaGridManager manager;

	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	protected AbstractVerticalCalendarReportView(final AbstractVerticalReportVisualiser verticalReportVisualiser) {
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
		
		final CopyGridToHtmlClipboardAction copyToClipboardAction = new CopyGridToHtmlClipboardAction(gridViewer.getGrid(), true);
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
			public Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final LNGScenarioModel rootObject, final boolean isPinned) {
				return Lists.newArrayList(rootObject);
			}

			@Override
			public void endCollecting() {

			}
		};
	}

	protected ReportNebulaGridManager createContentProvider() {
		manager = new ReportNebulaGridManager(this, verticalReportVisualiser);
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
		gridViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		verticalReportVisualiser.dispose();

		super.dispose();
	}

	//
	/**
	 * Should return the text associated with an individual event on a given day.
	 * 
	 * @param eventColumnLabelProvider
	 */
	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final EventProvider eventProvider, final String title) {
		return createColumn(labeller, eventProvider, title, (GridColumn) null);
	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final EventProvider eventProvider, final String name, @Nullable final GridColumnGroup columnGroup) {
		final GridColumn column = new GridColumn(columnGroup, SWT.NONE);
		return createColumn(labeller, eventProvider, name, column);

	}

	protected GridViewerColumn createColumn(final ColumnLabelProvider labeller, final EventProvider eventProvider, final String name, @Nullable final GridColumn column) {
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
	 * This method is used by subclasses to create the columns required by the report. This is called every time the input data changes. Existing columns will have been disposed prior to calling this
	 * method.
	 * 
	 * @param data
	 */
	protected abstract List<CalendarColumn> createCalendarCols(final ScheduleSequenceData data);
}
