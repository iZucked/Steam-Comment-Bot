/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.views;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;

public abstract class DetailPropertiesView extends ViewPart {

	protected GridTreeViewer viewer;
	private ISelectionListener selectionListener;

	private final String category;
	private final String helpContextId;

	private boolean showUnitsInColumn;
	private Action packColumnsAction;

	protected DetailPropertiesView(@NonNull final String category) {
		this(category, null, true);
	}

	protected DetailPropertiesView(@NonNull final String category, @Nullable final String helpContextId, final boolean showUnitsInSeparateColumn) {
		this.category = category;
		this.helpContextId = helpContextId;
		showUnitsInColumn = showUnitsInSeparateColumn;
	}

	@Override
	public void createPartControl(final Composite parent) {

		viewer = new GridTreeViewer(parent);

		// Set defaults
		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		viewer.setContentProvider(createContentProvider());

		ColumnViewerToolTipSupport.enableFor(viewer);

		// TODO: Units presentation options probably need to be refactored and customisable at the concrete instance.
		boolean showDimensionedValue = !showUnitsInColumn;
		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Attribute");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createLabelProvider(DetailPropertyColumnType.ATTRIBUTE_WITH_DIMENSIONS, gvc));
			showDimensionedValue = false;
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Value");
			gvc.getColumn().setWidth(100);

			gvc.setLabelProvider(createLabelProvider(showDimensionedValue ? DetailPropertyColumnType.DIMENSIONED_VALUE : DetailPropertyColumnType.VALUE, gvc));
		}
		if (showUnitsInColumn) {
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Units");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createLabelProvider(DetailPropertyColumnType.UNIT, gvc));
		}

		// Hook up selection listener
		selectionListener = createSelectionChangedListener();
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(selectionListener);

		makeActions();

		// Hook up help if available
		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		}

		// Initial selection
		{
			updateFromSelection();
		}

	}

	protected void updateFromSelection() {
		selectionListener.selectionChanged(null, getSite().getPage().getSelection());
	}

	protected void makeActions() {
		packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);
		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	@Override
	public void dispose() {
		removeAdapters();

		if (selectionListener != null) {
			getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(selectionListener);
		}
		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	protected ISelectionListener createSelectionChangedListener() {
		return new ISelectionListener() {

			@Override
			public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
				if (part instanceof PropertySheet) {
					// Ignore
					return;
				}

				removeAdapters();
				final Collection<?> adaptSelection = adaptSelection(selection);
				viewer.setInput(adaptSelection);
				hookAdapters(adaptSelection);
				if (packColumnsAction != null) {
					packColumnsAction.run();
				}

			}

		};
	}

	protected void refresh() {
		selectionListener.selectionChanged(null, getSite().getPage().getSelection());
	}

	protected CellLabelProvider createLabelProvider(final DetailPropertyColumnType columnType, final GridViewerColumn gvc) {
		return new DetailPropertyLabelProvider(columnType);
	}

	protected ITreeContentProvider createContentProvider() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		return new DetailPropertyContentProvider(adapterFactory, category);
	}

	protected Collection<?> adaptSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			return structuredSelection.toList();
		}
		return Collections.emptySet();

	}

	protected void hookAdapters(final Collection<?> adaptSelection) {

	}

	protected void removeAdapters() {

	}

}
