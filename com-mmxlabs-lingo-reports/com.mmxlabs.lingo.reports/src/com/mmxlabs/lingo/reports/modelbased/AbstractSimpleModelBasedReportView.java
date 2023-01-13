/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.modelbased.ColumnGenerator.ColumnInfo;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.WrappedSelectionProvider;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

public abstract class AbstractSimpleModelBasedReportView<M> extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private ScenarioComparisonService scenarioComparisonService;

	protected @NonNull TransformedSelectedDataProvider currentSelectedDataProvider = new TransformedSelectedDataProvider(null);

	protected GridTableViewer viewer;

	private final Class<M> modelClass;

	private IModelBasedSelectionMapper<M> selectionMapper;

	protected PackGridTableColumnsAction packColumnsAction;

	protected CopyGridToHtmlClipboardAction copyTableAction;

	private EObjectTableViewerFilterSupport filterSupport;
	protected FilterField filterField;

	protected ColumnInfo columnInfo;

	public AbstractSimpleModelBasedReportView(final Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	protected abstract BiConsumer<ViewerCell, Field> createStyler();

	protected abstract List<M> transform(final ISelectedDataProvider selectedDataProvider);

	private final boolean enableSorting = true;
	protected final boolean enableFilter = true;

	@Override
	public void createPartControl(final Composite parent) {

		final Composite container = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		if (enableFilter) {
			filterField = new FilterField(container);
		}

		viewer = new GridTableViewer(container);
		viewer.getGrid().setLayoutData(new GridData(GridData.FILL_BOTH));

		ColumnViewerToolTipSupport.enableFor(viewer);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.setContentProvider(new ArrayContentProvider());

		viewer.getGrid().setHeaderVisible(true);

		if (enableSorting) {
			sortingSupport = new EObjectTableViewerSortingSupport();
			viewer.setComparator(sortingSupport.createViewerComparer());
		}
		if (enableFilter) {
			this.filterSupport = new EObjectTableViewerFilterSupport(viewer, viewer.getGrid());
			viewer.addFilter(filterSupport.createViewerFilter());
			filterField.setFilterSupport(filterSupport);
		}
		final BiConsumer<ViewerCell, Field> styler = createStyler();
		this.columnInfo = ColumnGenerator.createColumns(viewer, sortingSupport, filterSupport, modelClass, styler);

		makeActions();

		selectionMapper = createSelectionMapper(viewer);

		scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);
		scenarioComparisonService.addListener(scenarioComparisonServiceListener);

		scenarioComparisonService.triggerListener(scenarioComparisonServiceListener, true);
		packColumnsAction.run();

		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);

		selectionChanged(null, service.getSelection());

		// Wrap the viewer to map internal data to external data for the rest of the application.
		final ISelectionProvider wrappedSelectionProvider = new WrappedSelectionProvider(selectionMapper::adaptSelectionToExternal);
		viewer.addSelectionChangedListener(e -> wrappedSelectionProvider.setSelection(e.getSelection()));
		getSite().setSelectionProvider(wrappedSelectionProvider);
	}

	protected DefaultModelBasedSelectionMapper<M> createSelectionMapper(Viewer lviewer) {
		return new DefaultModelBasedSelectionMapper<>(modelClass, lviewer);
	}

	protected void makeActions() {
		if (enableFilter) {
			getViewSite().getActionBars().getToolBarManager().add(filterField.getContribution());
		}

		packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		copyTableAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), false, null, null);

		setCopyForegroundColours(true);
		setCopyBackgroundColours(true);

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);

		getViewSite().getActionBars().updateActionBars();
	}

	private final ISelectedScenariosServiceListener scenarioComparisonServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final TransformedSelectedDataProvider newSelectedDataProvider = new TransformedSelectedDataProvider(selectedDataProvider);

			setCurrentSelectedDataProvider(newSelectedDataProvider);

			ViewerHelper.setInput(viewer, block, transform(newSelectedDataProvider));
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			ViewerHelper.refresh(viewer, true);
		}

	};

	private EObjectTableViewerSortingSupport sortingSupport;

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (GridTableViewer.class.isAssignableFrom(adapter)) {
			return (T) viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return (T) viewer.getGrid();
		}

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createHTMLFor(scenarioComparisonServiceListener, viewer.getGrid(), true));
		}
		
		return super.getAdapter(adapter);
	}

	@Override
	public void dispose() {

		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		scenarioComparisonService.removeListener(scenarioComparisonServiceListener);

		super.dispose();
	}

	protected void setCurrentSelectedDataProvider(@NonNull final TransformedSelectedDataProvider newSelectedDataProvider) {
		currentSelectedDataProvider = newSelectedDataProvider;
	}

	@Override
	public void selectionChanged(final MPart part, final Object selectionObject) {

		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
			if (e3Part instanceof ScenarioServiceNavigator) {
				return;
			}
		}

		ISelection selection = SelectionHelper.adaptSelection(selectionObject);
		selection = selectionMapper.adaptSelectionToInternal(selection);

		viewer.setSelection(selection, true);
	}

	protected void setCopyForegroundColours(final boolean showForegroundColours) {
		if (copyTableAction != null) {
			copyTableAction.setShowForegroundColours(showForegroundColours);
		}
	}

	protected void setCopyBackgroundColours(final boolean showBackgroundColours) {
		if (copyTableAction != null) {
			copyTableAction.setShowBackgroundColours(showBackgroundColours);
		}
	}
}
