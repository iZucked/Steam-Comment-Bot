/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.nomination;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.ActionFactory;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.components.AbstractReportView;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.nomination.NominationAlertReportTransformer;
import com.mmxlabs.lingo.reports.views.nomination.NominationAlertReportTransformer.RowData;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
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
 * @author Simon Goodall & FM
 * 
 */
public class NominationAlertReport extends AbstractReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.views.nomination.NominationAlertReport";

	private GridTableViewer viewer;

	private Font boldFont;
	protected Image pinImage;
	private final NominationAlertReportTransformer transformer = new NominationAlertReportTransformer();

	@Override
	protected Object doSelectionChanged(final TransformedSelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others) {
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
					final List<RowData> transform = transformer.transform(schedule, other, other == pinned);
					transform.forEach(rowData -> {
						rows.add(rowData);
						selectedDataProvider.addExtraData(rowData, other, schedule);

						final Set<Object> equivalents = new HashSet<>();
						if (rowData.slot != null) {
							equivalents.add(rowData.slot);
							if (rowData.slot.getCargo() != null){
								equivalents.add(rowData.slot.getCargo());
							}
						}
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
	public NominationAlertReport() {
		super();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

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

		scheduleColumn = createColumn(sortingSupport, true, "Schedule", rowData -> rowData.scenario, rowData -> rowData.scenario);
		createColumn(sortingSupport, "Cargo", rowData -> rowData.slotId, rowData -> rowData.slotId);
		createColumn(sortingSupport, "From", rowData -> rowData.seller, rowData -> rowData.seller);
		createColumn(sortingSupport, "To", rowData -> rowData.buyer, rowData -> rowData.buyer);
		createColumn(sortingSupport, "CN", rowData -> rowData.cn, rowData -> rowData.cn);
		createColumn(sortingSupport, "Type", rowData -> rowData.type, rowData -> rowData.type);
		createColumn(sortingSupport, "Date", rowData -> Formatters.asLocalDateFormatter.render(rowData.date), rowData -> rowData.date);
		createColumn(sortingSupport, "Nomination Type", rowData -> rowData.nominationType, rowData -> rowData.nominationType);
		createColumn(sortingSupport, "Nominate by", rowData -> Formatters.asLocalDateFormatter.render(rowData.nominateBy), rowData -> rowData.nominateBy);
		createColumn(null, "Remark", rowData -> rowData.nominationComment, rowData -> rowData.nominationComment);

		viewer.getGrid().setLinesVisible(true);

		makeActions();

		postCreate(viewer);
	}

	private GridViewerColumn createColumn(final EObjectTableViewerSortingSupport tv, final String title, final Function<RowData, String> labelProvider,
			final Function<RowData, Comparable> sortFunction) {
		return createColumn(tv, false, title, labelProvider, sortFunction);
	}

	private GridViewerColumn createColumn(final EObjectTableViewerSortingSupport tv, boolean pinColumn, final String title, final Function<RowData, String> labelProvider,
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

					cell.setImage(null);
					if (pinColumn) {
						if (rowData.pinned) {
							cell.setImage(pinImage);
						}
					}
				} else {
					cell.setText("");
					cell.setImage(null);
				}
			}
		});
		if (tv != null) {
			tv.addSortableColumn(viewer, column, column.getColumn());
		}

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
				if (rowData.slot != null) {
					newSelection.add(rowData.slot);
					if (rowData.slot.getCargo() != null) {
						newSelection.add(rowData.slot.getCargo());
					}
				}
			}
		}
		return newSelection;
	}

}