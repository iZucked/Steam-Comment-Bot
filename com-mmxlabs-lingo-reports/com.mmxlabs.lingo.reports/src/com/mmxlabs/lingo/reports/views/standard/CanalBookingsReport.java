/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.components.AbstractReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.standard.CanalBookingsReportTransformer.RowData;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.IComparableProvider;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * A report which displays the canal bookings
 * 
 * @author Simon Goodall
 * 
 */
public class CanalBookingsReport extends AbstractReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.standard.CanalBookingsReport";

	private GridTableViewer viewer;

	private Font boldFont;

	private final CanalBookingsReportTransformer transformer = new CanalBookingsReportTransformer();

	@Override
	protected Object doSelectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others) {
		final List<RowData> rows = new LinkedList<>();

		final List<ScenarioResult> scenarios = new LinkedList<>();
		if (pinned != null) {
			scenarios.add(pinned);
		}
		scenarios.addAll(others);

		for (final ScenarioResult other : scenarios) {
			final ScheduleModel other_scheduleModel = other.getTypedResult(ScheduleModel.class);
			if (other_scheduleModel != null) {
				final Schedule schedule = other_scheduleModel.getSchedule();
				if (schedule != null) {
					@NonNull
					final List<RowData> transform = transformer.transform(schedule, other);
					transform.forEach(rowData -> {
						rows.add(rowData);
						addElementMapping(rowData, other);

						final Set<Object> equivalents = new HashSet<>();
						if (rowData.event != null) {
							equivalents.add(rowData.event);
							if (rowData.event instanceof SlotVisit) {
								final SlotVisit slotVisit = (SlotVisit) rowData.event;
								equivalents.add(slotVisit.getSlotAllocation().getCargoAllocation());
								Slot slot = slotVisit.getSlotAllocation().getSlot();
								if (slot != null) {
									equivalents.add(slot);
									equivalents.add(slot.getCargo());
								}
							} else if (rowData.event instanceof VesselEventVisit) {
								final VesselEventVisit vesselEventVisit = (VesselEventVisit) rowData.event;
								equivalents.add(vesselEventVisit.getVesselEvent());
							}
						}
						if (rowData.booking != null) {
							equivalents.add(rowData.booking);
						}

						final Event nextEvent = rowData.event != null ? rowData.event.getNextEvent() : null;
						final Journey nextJourney = nextEvent instanceof Journey ? (Journey) nextEvent : null;
						equivalents.add(nextJourney);
						setInputEquivalents(rowData, equivalents);
					});
				}
			}
		}

		if (rows.isEmpty()) {
			rows.add(new RowData());
		}

		if (scheduleColumn != null) {
			scheduleColumn.getColumn().setVisible((others.size() + (pinned == null ? 0 : 1)) > 1);
		}
		return rows;
	}

	private GridViewerColumn scheduleColumn;

	private PackGridTableColumnsAction packColumnsAction;

	private CopyGridToClipboardAction copyTableAction;

	/**
	 * The constructor.
	 */
	public CanalBookingsReport() {
		// super("com.mmxlabs.lingo.doc.Reports_CanalBookingsReport");
		super();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		{
			final Font systemFont = Display.getDefault().getSystemFont();
			// Clone the font data
			final FontData fd = new FontData(systemFont.getFontData()[0].toString());
			// Set the bold bit.
			fd.setStyle(fd.getStyle() | SWT.BOLD);
			boldFont = new Font(Display.getDefault(), fd);
		}
		viewer = createGridTableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.getGrid().setHeaderVisible(true);

		viewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		viewer.setContentProvider(new ArrayContentProvider());

		viewer.setInput(new ArrayList<>());

		final EObjectTableViewerSortingSupport sortingSupport = new EObjectTableViewerSortingSupport();
		viewer.setComparator(sortingSupport.createViewerComparer());

		scheduleColumn = createColumn(sortingSupport, "Schedule", rowData -> rowData.scheduleName, rowData -> rowData.scheduleName);

		createColumn(sortingSupport, "Pre-booked", rowData -> (rowData.preBooked) ? "Yes" : "No", rowData -> rowData.preBooked);

		createColumn(sortingSupport, "Canal", rowData -> (rowData.routeOption == null) ? "" : rowData.routeOption.toString(),
				rowData -> (rowData.routeOption == null) ? "" : rowData.routeOption.toString());

		createColumn(sortingSupport, "Canal Entry", rowData -> (rowData.entryPoint == null) ? "" : rowData.entryPoint.getName(),
				rowData -> (rowData.entryPoint == null) ? "" : rowData.entryPoint.getName());

		createColumn(sortingSupport, "Date", rowData -> Formatters.asLocalDateFormatter.render(rowData.bookingDate), rowData -> rowData.bookingDate);

		createColumn(sortingSupport, "Event", rowData -> (rowData.event == null) ? "" : rowData.event.name(), rowData -> (rowData.event == null) ? "" : rowData.event.name());

		createColumn(sortingSupport, "Next Slot", rowData -> (rowData.nextSlot == null ? "" : rowData.nextSlot.getName()), rowData -> (rowData.nextSlot == null ? "" : rowData.nextSlot.getName()));

		createColumn(sortingSupport, "Next Slot Port", rowData -> (rowData.nextSlot == null ? "" : rowData.nextSlot.getPort().getName()),
				rowData -> (rowData.nextSlot == null ? "" : rowData.nextSlot.getPort().getName()));

		createColumn(sortingSupport, "Next Slot Window Start", rowData -> (rowData.nextSlot == null ? "" : Formatters.asLocalDateFormatter.render(rowData.nextSlot.getWindowStart())),
				rowData -> (rowData.nextSlot == null ? null : rowData.nextSlot.getWindowStart()));

		createColumn(sortingSupport, "Next Slot Window End",
				rowData -> (rowData.nextSlot == null ? "" : Formatters.asLocalDateFormatter.render(rowData.nextSlot.getWindowStart().plusDays(rowData.nextSlot.getWindowSizeInHours() / 24))),
				rowData -> (rowData.nextSlot == null ? null : rowData.nextSlot.getWindowSizeInHours() / 24));

		createColumn(sortingSupport, "Period", rowData -> rowData.period, rowData -> rowData.period);

		createColumn(sortingSupport, "Notes", rowData -> rowData.notes, rowData -> rowData.notes);

		viewer.getGrid().setLinesVisible(true);

		makeActions();

		postCreate(viewer);
	}

	private GridViewerColumn createColumn(final EObjectTableViewerSortingSupport tv, final String title, final Function<RowData, String> labelProvider,
			final Function<RowData, Comparable> sortFunction) {
		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(column);

		column.getColumn().setText(title);
		column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {

				final Object element = cell.getElement();
				cell.setBackground(null);
				if (element instanceof RowData) {
					final RowData rowData = (RowData) element;
					cell.setText(labelProvider.apply(rowData));

					if (rowData.warn) {
						cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
					}
				} else {
					cell.setText("");
				}
			}
		});
		tv.addSortableColumn(viewer, column, column.getColumn());

		final IComparableProvider provider = (o) -> sortFunction.apply((RowData) o);
		column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, provider);

		column.getColumn().pack();

		return column;
	}

	int getTextWidth(final int minWidth, final String string) {
		final GC gc = new GC(viewer.getControl());
		try {
			gc.setFont(boldFont);
			// 8 taken from sum margins in org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
			return Math.max(minWidth, 10 + gc.textExtent(string).x);
		} finally {
			gc.dispose();
		}
	}

	int getTextHeight(final int minHeight, final String string) {
		final GC gc = new GC(viewer.getControl());
		try {
			gc.setFont(boldFont);
			// 3 taken from sum margins in org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer
			return Math.max(minHeight, 3 + gc.textExtent(string).y);
		} finally {
			gc.dispose();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public void dispose() {
		if (boldFont != null) {
			boldFont.dispose();
			boldFont = null;
		}

		super.dispose();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}

		return super.getAdapter(adapter);
	}

	private void makeActions() {
		packColumnsAction = new PackGridTableColumnsAction(viewer);
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);

		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	@Override
	protected Viewer getViewer() {
		return viewer;
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {

		final List<Object> newSelection = new LinkedList<Object>();
		for (final Object o : selection) {
			if (o instanceof RowData) {
				final RowData rowData = (RowData) o;
				if (rowData.event != null) {
					newSelection.add(rowData.event);
					if (rowData.event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) rowData.event;
						newSelection.add(slotVisit.getSlotAllocation().getSlot());
					} else if (rowData.event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) rowData.event;
						newSelection.add(vesselEventVisit.getVesselEvent());
					}
				}
				if (rowData.booking != null) {
					newSelection.add(rowData.booking);
				}
			}
		}

		return newSelection;
	}

}