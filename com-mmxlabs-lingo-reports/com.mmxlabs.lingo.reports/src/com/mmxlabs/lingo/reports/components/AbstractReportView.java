/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Equality;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.ScenarioResult;

public abstract class AbstractReportView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private ScenarioComparisonService selectedScenariosService;

	protected abstract Object doSelectionChanged(final TransformedSelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others);

	protected @NonNull TransformedSelectedDataProvider currentSelectedDataProvider = new TransformedSelectedDataProvider(null);

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					clearInputEquivalents();
					currentSelectedDataProvider = new TransformedSelectedDataProvider(selectedDataProvider);

					final Object newInput = doSelectionChanged(currentSelectedDataProvider, currentSelectedDataProvider.getPinnedScenarioResult(),
							currentSelectedDataProvider.getOtherScenarioResults());

					ViewerHelper.setInput(getViewer(), true, newInput);
				}
			};

			ViewerHelper.runIfViewerValid(getViewer(), block, r);
		}
	};

	protected final ICellRenderer containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String render(final Object object) {
			ScenarioResult instance = currentSelectedDataProvider.getScenarioResult(object);
			if (instance != null) {
				return instance.getModelRecord().getName();
			}
			return null;
		}
	};

	private final HashMap<Object, Object> equivalents = new HashMap<Object, Object>();
	private final HashSet<Object> contents = new HashSet<Object>();

	public void setInputEquivalents(final Object input, final Collection<? extends Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			if (o != null) {
				equivalents.put(o, input);
			}
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	protected EObjectTableViewer createEObjectTableViewer(final Composite container, final int style) {
		final EObjectTableViewer viewer = new EObjectTableViewer(container, style) {

			@Override
			protected List<?> getSelectionFromWidget() {

				final List<?> list = super.getSelectionFromWidget();

				return adaptSelectionFromWidget(list);
			}

			/**
			 * Modify @link {AbstractTreeViewer#getTreePathFromItem(Item)} to adapt items before returning selection object.
			 */
			@Override
			protected TreePath getTreePathFromItem(Item item) {
				final LinkedList<Object> segments = new LinkedList<>();
				while (item != null) {
					final Object segment = item.getData();
					Assert.isNotNull(segment);
					segments.addFirst(segment);
					item = getParentItem(item);
				}
				final List<?> l = adaptSelectionFromWidget(segments);

				return new TreePath(l.toArray());
			}
		};

		if (handleSelections()) {
			viewer.setComparer(createMappedElementComparer());
		}
		return viewer;
	}

	protected GridTableViewer createGridTableViewer(final Composite container, final int style) {
		final GridTableViewer viewer = new GridTableViewer(container, style) {

			@Override
			protected List<?> getSelectionFromWidget() {

				final List<?> list = super.getSelectionFromWidget();

				return adaptSelectionFromWidget(list);
			}
		};

		if (handleSelections()) {
			viewer.setComparer(createMappedElementComparer());
		}
		return viewer;
	}

	protected void postCreate(final Viewer viewer) {
		getSite().setSelectionProvider(viewer);
		if (handleSelections()) {

			final ESelectionService service = getSite().getService(ESelectionService.class);
			service.addPostSelectionListener(this);
		}

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);
		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	protected IElementComparer createMappedElementComparer() {
		return new IElementComparer() {
			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(Object a, Object b) {
				if (!contents.contains(a) && equivalents.containsKey(a)) {
					a = equivalents.get(a);
				}
				if (!contents.contains(b) && equivalents.containsKey(b)) {
					b = equivalents.get(b);
				}
				return Equality.isEqual(a, b);
			}
		};
	}

	protected abstract Viewer getViewer();

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(getViewer());
	}

	@Override
	public void selectionChanged(final MPart part, final Object selectedObject) {
		{
			final IWorkbenchPart view = SelectionHelper.getE3Part(part);

			if (view == this) {
				return;
			}
			if (view instanceof PropertySheet) {
				return;
			}
		}

		// Convert selection
		final ISelection selection = SelectionHelper.adaptSelection(selectedObject);
		ViewerHelper.setSelection(getViewer(), true, selection, true);
	}

	protected boolean handleSelections() {
		return false;
	}

	@Override
	public void dispose() {

		clearInputEquivalents();

		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}

	/**
	 * Process the array of elements before they are returned to e.g. map input equivalents
	 * 
	 * @param result
	 */
	protected void processInputs(final Object[] result) {

	}

	/**
	 * 
	 * Callback to convert the raw data coming out of the table into something usable externally. This is useful when the table data model is custom for the table rather from the real data model.
	 * 
	 */
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return selection;
	}
}
