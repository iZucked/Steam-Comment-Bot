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

	private GridTreeViewer viewer;
	private ISelectionListener selectionListener;

	private final String category;
	private final String helpContextId;
	
	boolean showUnitsInColumn;

	protected DetailPropertiesView(@NonNull final String category) {
		this(category, null, true);
	}

	protected DetailPropertiesView(@NonNull final String category, @Nullable final String helpContextId, boolean showUnitsInSeparateColumn) {
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

		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Attribute");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createLabelProvider(DetailPropertyColumnType.ATTRIBUTE, gvc));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Value");
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createLabelProvider(showUnitsInColumn ? DetailPropertyColumnType.VALUE: DetailPropertyColumnType.DIMENSIONED_VALUE, gvc));
		}
		if(showUnitsInColumn){
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
			selectionListener.selectionChanged(null, getSite().getPage().getSelection());
		}

	}

	protected void makeActions() {
		final Action packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);
		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	@Override
	public void dispose() {
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
				viewer.setInput(adaptSelection(selection));
			}
		};
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
}
