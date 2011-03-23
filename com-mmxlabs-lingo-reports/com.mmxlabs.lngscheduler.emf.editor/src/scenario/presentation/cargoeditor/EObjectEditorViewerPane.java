/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPage;

import scenario.presentation.ScenarioEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * A viewerpane which displays a list of EObjects in a table and lets you edit
 * them
 * 
 * @author hinton
 * 
 */
public class EObjectEditorViewerPane extends ViewerPane {
	private final ScenarioEditor part;
	private TableViewer viewer;

	public EObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.part = part;
	}

	@Override
	public Viewer createViewer(final Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		return viewer;
	}

	public void addColumn(final String columnName,
			final ICellRenderer renderer, final ICellManipulator manipulator,
			final Object... pathObjects) {
		// create a column

		final EMFPath path = new EMFPath(true, pathObjects);

		final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(columnName);
		column.getColumn().pack();
		column.getColumn().setResizable(true);

		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return renderer.render(path.get((EObject) element));
			}
		});

		column.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(final Object element, final Object value) {
				// a value has come out of the celleditor and is being set on
				// the element.
				manipulator.setValue(path.get((EObject) element), value);
				viewer.refresh();
			}

			@Override
			protected Object getValue(final Object element) {
				return manipulator.getValue(path.get((EObject) element));
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final CellEditor result = manipulator.getCellEditor(viewer.getTable(), path.get((EObject) element));
				return result;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return manipulator.canEdit(path.get((EObject) element));
			}
		});
	}

	public void init(final List<EReference> path,
			final AdapterFactory adapterFactory) {

		// TODO figure out why this doesn't work
		getToolBarManager().add(new Action("Pack Columns") {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					final TableColumn[] columns = viewer.getTable()
							.getColumns();
					for (TableColumn c : columns) {
						c.pack();
					}
				}
			}
		});

		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.height = 18;
			}
		});

		
		//TODO somewhere inside this, we need to make ourselves listen
		// for deep changes. not sure how
		
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {
			
			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							return ((EList) object).toArray();
						}
						if (object instanceof EObject) {
							o = (EObject) object;
						}
					}

					return new Object[] { o };
				}
				return new Object[] {};
			}
		});
	}

	@Override
	protected void requestActivation() {
		super.requestActivation();
		part.setCurrentViewerPane(this);
	}

	public TableViewer getViewer() {
		return viewer;
	}
}
