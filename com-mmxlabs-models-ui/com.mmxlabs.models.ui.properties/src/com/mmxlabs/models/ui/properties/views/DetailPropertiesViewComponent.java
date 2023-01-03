/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.views;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
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
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.application.TogglePinSelection;

public abstract class DetailPropertiesViewComponent {

	protected GridTreeViewer viewer;

	public GridTreeViewer getViewer() {
		return viewer;
	}

	protected boolean pinSelection = false;

	@TogglePinSelection
	public boolean togglePinState() {
		pinSelection = !pinSelection;
		return pinSelection;
	}

	private Options options;

	@PostConstruct
	public void createPartControl(final Composite parent, Options options) {

		this.options = options;
		viewer = new GridTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		// Set defaults
		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		viewer.setContentProvider(createContentProvider());

		ColumnViewerToolTipSupport.enableFor(viewer);

		// TODO: Units presentation options probably need to be refactored and customisable at the concrete instance.
		boolean showDimensionedValue = !options.showUnitsInColumn;
		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Attribute");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createLabelProvider(DetailPropertyColumnType.ATTRIBUTE_WITH_DIMENSIONS, gvc));
			showDimensionedValue = false;
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Value");
			gvc.getColumn().setWidth(100);

			gvc.setLabelProvider(createLabelProvider(showDimensionedValue ? DetailPropertyColumnType.DIMENSIONED_VALUE : DetailPropertyColumnType.VALUE, gvc));
		}
		if (options.showUnitsInColumn) {
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Units");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createLabelProvider(DetailPropertyColumnType.UNIT, gvc));
		}

		// Hook up help if available
		if (options.helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), options.helpContextId);
		}
	}

	@PreDestroy
	public void dispose() {
		removeAdapters();
	}

	@Focus
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	protected CellLabelProvider createLabelProvider(final @NonNull DetailPropertyColumnType columnType, final @NonNull GridViewerColumn gvc) {
		return new DetailPropertyLabelProvider(columnType);
	}

	protected ITreeContentProvider createContentProvider() {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		return new DetailPropertyContentProvider(adapterFactory, options.category);
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
