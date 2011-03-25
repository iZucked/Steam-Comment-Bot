/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
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
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;

import scenario.presentation.ScenarioEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;

/**
 * A viewerpane which displays a list of EObjects in a table and lets you edit
 * them
 * 
 * @author hinton
 * 
 */
public class EObjectEditorViewerPane extends ViewerPane {
	private static final String COLUMN_RENDERER = "COLUMN_RENDERER";
	private static final String COLUMN_PATH = "COLUMN_PATH";
	private final ScenarioEditor part;
	private TableViewer viewer;

	private ArrayList<TableColumn> columnSortOrder = new ArrayList<TableColumn>();
	private boolean sortDescending = false;

	public EObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.part = part;
	}

	@Override
	public Viewer createViewer(final Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);

		Action a = new PackTableColumnsAction(viewer);
		getToolBarManager().add(a);

		getToolBarManager().update(true);
		return viewer;
	}

	private boolean shouldEditCell = false;

	/**
	 * A hack to prevent single click editing, which is really annoying and
	 * silly.
	 * 
	 * @return
	 */
	protected boolean getShouldEditCell() {
		return shouldEditCell;
	}

	protected void setShouldEdit(boolean b) {
		shouldEditCell = b;
	}

	public void addColumn(final String columnName,
			final ICellRenderer renderer, final ICellManipulator manipulator,
			final Object... pathObjects) {
		// create a column

		final EMFPath path = new EMFPath(true, pathObjects);

		final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn tColumn = column.getColumn();
		tColumn.setText(columnName);
		tColumn.pack();
		tColumn.setResizable(true);

		// store the renderer here, so that we can use it in sorting later.
		tColumn.setData(COLUMN_RENDERER, renderer);
		tColumn.setData(COLUMN_PATH, path);

		columnSortOrder.add(tColumn);

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
				final CellEditor result = manipulator.getCellEditor(
						viewer.getTable(), path.get((EObject) element));
				return result;
			}

			@Override
			protected boolean canEdit(final Object element) {
				// intercept mouse listener here
				return getShouldEditCell()
						&& manipulator.canEdit(path.get((EObject) element));
			}
		});

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (columnSortOrder.get(0) == tColumn) {
					sortDescending = !sortDescending;
				} else {
					sortDescending = false;
					columnSortOrder.remove(tColumn);
					columnSortOrder.add(0, tColumn);
				}
				viewer.getTable().setSortColumn(tColumn);
				viewer.getTable().setSortDirection(
						sortDescending ? SWT.DOWN : SWT.UP);
				viewer.refresh(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
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
		
		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1,
					final Object e2) {
				int order = 0;
				final Iterator<TableColumn> iterator = columnSortOrder.iterator();
				while (order == 0 && iterator.hasNext()) {
					final TableColumn col = iterator.next();
					//TODO the columnSortOrder could just hold the renderers, avoiding this lookup
					final ICellRenderer renderer = (ICellRenderer) col.getData(COLUMN_RENDERER);
					final EMFPath path = (EMFPath) col.getData(COLUMN_PATH);
					final Comparable c1 = renderer.getComparable(path.get((EObject)e1));
					final Comparable c2 = renderer.getComparable(path.get((EObject)e2));
					order = c1.compareTo(c2);
				}
				
				return sortDescending ? -order : order;
			}
		});

		final Listener mouseDownListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				// alernatively, check here whether click lies in the selected
				// row.
				setShouldEdit(false);
			}
		};

		final Listener measureListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.height = 18;
			}
		};

		final Listener doubleClickListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				setShouldEdit(true);
				TableItem[] selection = table.getSelection();

				if (selection.length != 1) {
					return;
				}

				TableItem item = table.getSelection()[0];

				for (int i = 0; i < table.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						viewer.editElement(item.getData(), i);
						setShouldEdit(false);
						break;
					}
				}
			}
		};

		table.addListener(SWT.MouseDown, mouseDownListener);
		table.addListener(SWT.MeasureItem, measureListener);
		table.addListener(SWT.MouseDoubleClick, doubleClickListener);

		table.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				table.removeListener(SWT.MouseDown, mouseDownListener);
				table.removeListener(SWT.MeasureItem, measureListener);
				table.removeListener(SWT.MouseDoubleClick, doubleClickListener);
			}

		});

		final HashSet<EObject> currentElements = new HashSet<EObject>();

		// TODO somewhere inside this, we need to make ourselves listen
		// for deep changes. not sure how
		final EContentAdapter adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);

				if (notification.isTouch() == false) {
					// this is a change, so we have to refresh.
					// ideally we just want to update the changed object, but we
					// get the notification from
					// somewhere below, so we need to go up
					EObject source = (EObject) notification.getNotifier();
					if (currentElements.contains(source))
						return;
					while (!(currentElements.contains(source))
							&& ((source = source.eContainer()) != null)) {

							viewer.update(source, null);
							// this seems to clear the selection
							return;
						
					}
				}
			}

		};
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				adapter.unsetTarget(adapter.getTarget()); // remove adapter from
															// old input
				currentElements.clear();
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							for (final EObject e : (EList<EObject>) object) {
								e.eAdapters().add(adapter);
								currentElements.add(e);
							}
							return ((EList) object).toArray();
						}
						if (object instanceof EObject) {
							o = (EObject) object;
						}
					}
					currentElements.add(o);
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
