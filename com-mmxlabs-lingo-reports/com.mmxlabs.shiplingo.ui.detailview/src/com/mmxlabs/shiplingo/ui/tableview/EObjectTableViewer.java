/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import scenario.fleet.VesselClass;
import scenario.port.Canal;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * A TableViewer which displays a list of EObjects using the other classes in
 * this package. Factored out of EObjectEditorViewerPane so that it can be used
 * in dialogs without causing trouble, and without import/export buttons.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectTableViewer extends TableViewer {
	private static final String COLUMN_RENDERER = "COLUMN_RENDERER";
	private static final String COLUMN_PATH = "COLUMN_PATH";

	private final ArrayList<TableColumn> columnSortOrder = new ArrayList<TableColumn>();
	private boolean sortDescending = false;

	private final LinkedList<Pair<EMFPath, ICellRenderer>> cellRenderers = new LinkedList<Pair<EMFPath, ICellRenderer>>();

	private boolean shouldEditCell = false;

	/**
	 * A set containing the elements currently being displayed, which is used by
	 * #adapter to determine which row to refresh when a notification comes in
	 */
	final HashSet<EObject> currentElements = new HashSet<EObject>();

	/**
	 * A one-element list referring to the EObject which contains all the
	 * display elements. #adapter uses this to determine when a notification
	 * comes on the top-level object, and so all the contents should be
	 * refreshed (e.g. after an import)
	 */
	EObject currentContainer;

	public EObjectTableViewer(Composite parent) {
		super(parent);
	}

	public EObjectTableViewer(Table table) {
		super(table);
	}

	public EObjectTableViewer(Composite parent, int style) {
		super(parent, style);
	}

	public void init(final AdapterFactory adapterFactory,
			final EReference... path) {
		final TableViewer viewer = this;
		final Table table = viewer.getTable();
		setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				if (inputElement == null)
					return new Object[0];
				return ((List) inputElement).toArray();
			}
		});

		final Listener mouseDownListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
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
			public void handleEvent(final Event event) {
				setShouldEdit(true);
				final TableItem[] selection = table.getSelection();

				if (selection.length != 1) {
					return;
				}

				final TableItem item = table.getSelection()[0];

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
			public void widgetDisposed(final DisposeEvent e) {
				table.removeListener(SWT.MouseDown, mouseDownListener);
				table.removeListener(SWT.MeasureItem, measureListener);
				table.removeListener(SWT.MouseDoubleClick, doubleClickListener);
				dispose();
			}
		});

		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				removeAdapters();
				currentElements.clear();
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							o.eAdapters().add(adapter);
							currentElements.addAll((EList) object);
							for (final EObject newElement : currentElements) {
								updateObjectExternalNotifiers(newElement);
							}
							currentContainer = o;
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

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(
			final String columnName, final T manipulatorAndRenderer,
			final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer,
				manipulatorAndRenderer, path);
	}

	protected void removeAdapters() {
		if (currentContainer != null) {
			if (currentContainer.eAdapters().contains(adapter)) {
				currentContainer.eAdapters().remove(adapter);
			}
		}

		for (final Notifier n : externalReferences.keySet()) {
			n.eAdapters().remove(externalAdapter);
		}

		externalReferences.clear();
		externalNotifiersByObject.clear();
	}

	protected boolean refreshOrGiveUp() {
		refresh();
		return false;
	}
	
	private final Adapter externalAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(final Notification msg) {
			if (!msg.isTouch()) {
				final Object notifier = msg.getNotifier();
				// redraw all objects listening to this notifier
				final Set<EObject> changed = externalReferences.get(notifier);
				if (changed != null) {
					// wait to refresh if there is an import happening
					// elsewhere. then refresh everything.
					if (refreshOrGiveUp()) {
						return;
					}
//					if (ImportUI.isImporting()) {
//						ImportUI.refresh(EObjectTableViewer.this);
//						return;
//					}
					for (final EObject e : changed) {
						EObjectTableViewer.this.update(e, null);
					}
				}
			}
		}
	};

	final EContentAdapter adapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);

			if (refreshOrGiveUp()) {
				return;
			}
			
//			if (ImportUI.isImporting()) {
//				ImportUI.refreshLater(EObjectTableViewer.this);
//				return;
//			}

			// System.err.println(notification);
			if (notification.isTouch() == false) {
				// this is a change, so we have to refresh.
				// ideally we just want to update the changed object, but we
				// get the notification from
				// somewhere below, so we need to go up
				EObject source = (EObject) notification.getNotifier();
				// if (currentElements.contains(source))
				// return;
				if (source == currentContainer) {
					refresh();
					return;
				}
				while (!(currentElements.contains(source))
						&& ((source = source.eContainer()) != null))
					;
				if (source != null) {
					EObjectTableViewer.this.update(source, null);
					EObjectTableViewer.this
							.updateObjectExternalNotifiers(source); // may have
																	// changed
					// what we need to
					// hook
					// notifications for
					// this seems to clear the selection
					return;
				}
			}
		}
	};

	private final Map<Notifier, Set<EObject>> externalReferences = new HashMap<Notifier, Set<EObject>>();
	private final Map<EObject, Set<Notifier>> externalNotifiersByObject = new HashMap<EObject, Set<Notifier>>();

	private void updateObjectExternalNotifiers(final EObject object) {
		Set<Notifier> dropNotifiers = new HashSet<Notifier>();
		Set<Notifier> addNotifiers = new HashSet<Notifier>();
		Set<Notifier> notifiers = externalNotifiersByObject.get(object);

		// look at the existing notifiers for this object, and disassociate them
		if (notifiers != null) {
			for (final Notifier notifier : notifiers) {
				final Set<EObject> references = externalReferences
						.get(notifier);
				references.remove(object);
				if (references.isEmpty()) {
					dropNotifiers.add(notifier); // we may no longer have any
													// need to be notified by
													// this notifier at all
				}
			}
		} else {
			notifiers = new HashSet<Notifier>();
		}

		// now ask all the cell renderers what we need to watch for this object
		for (final Pair<EMFPath, ICellRenderer> pathAndRenderer : cellRenderers) {
			final Iterable<Pair<Notifier, List<Object>>> newNotifiers = pathAndRenderer
					.getSecond().getExternalNotifiers(
							pathAndRenderer.getFirst().get(object));
			for (final Pair<Notifier, List<Object>> notifierAndFeatures : newNotifiers) {
				// get the notifier we are interested in
				final Notifier n = notifierAndFeatures.getFirst();
				if (n == null) {
					System.err.println(pathAndRenderer
							+ " has given a null notifier for " + object);
					continue;
				}
				notifiers.add(n); // add it to the notifiers for this object

				// relate this object to this notifier, so we can disconnect it
				// in a future update.
				Set<EObject> er = externalReferences.get(n);
				if (er == null) {
					er = new HashSet<EObject>();
					externalReferences.put(n, er);
				}

				if (er.isEmpty()) {
					addNotifiers.add(n); // this notifier had no related
											// objects, so we need to adapt it.
				}

				er.add(object);
			}
		}

		// actually hook up the notifiers
		final Iterator<Notifier> iter = dropNotifiers.iterator();
		while (iter.hasNext()) {
			final Notifier n = iter.next();
			if (addNotifiers.contains(n)) {
				iter.remove();
				addNotifiers.remove(n);
			}
		}

		for (final Notifier n : dropNotifiers) {
			if (n != null)
				n.eAdapters().remove(externalAdapter);
		}

		for (final Notifier n : addNotifiers) {
			if (n != null)
				n.eAdapters().add(externalAdapter);
		}
	}

	/**
	 * A hack to prevent single click editing, which is really annoying and
	 * silly.
	 * 
	 * @return
	 */
	protected boolean getShouldEditCell() {
		return shouldEditCell;
	}

	protected void setShouldEdit(final boolean b) {
		shouldEditCell = b;
	}

	public void addColumn(final String columnName,
			final ICellRenderer renderer, final ICellManipulator manipulator,
			final Object... pathObjects) {
		// create a column
		final TableViewer viewer = this;
		final EMFPath path = new CompiledEMFPath(true, pathObjects);
		cellRenderers.add(new Pair<EMFPath, ICellRenderer>(path, renderer));

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

			@Override
			public Color getBackground(final Object element) {
				final EObject object = (EObject) element;

				if (hasValidationError(object)) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				}

				// TODO hack because this is very slow and canals contain many
				// elements
				if (object instanceof Canal || object instanceof VesselClass)
					return super.getBackground(element);

				final TreeIterator<EObject> iterator = object.eAllContents();
				while (iterator.hasNext()) {
					if (hasValidationError(iterator.next()))
						return Display.getCurrent().getSystemColor(
								SWT.COLOR_RED);
				}

				return super.getBackground(element);
			}

			private boolean hasValidationError(final EObject object) {
				final Resource r = object.eResource();
				if (r == null)
					return false;
				final IFile containingFile = ResourcesPlugin.getWorkspace()
						.getRoot()
						.getFile(new Path(r.getURI().toPlatformString(true)));
				try {
					final IMarker[] markers = containingFile.findMarkers(
							IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
					for (final IMarker marker : markers) {
						final String uri = marker.getAttribute(
								EValidator.URI_ATTRIBUTE, null);
						if (uri == null)
							continue;
						final URI uri2 = URI.create(uri);
						if (uri2.getFragment().equals(r.getURIFragment(object))) {
							return true;
						}
					}
				} catch (final CoreException e) {
					e.printStackTrace();
				}

				return false;
			}

		});

		column.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected void setValue(final Object element, final Object value) {
				// a value has come out of the celleditor and is being set on
				// the element.
				manipulator.setValue(path.get((EObject) element), value);
				refresh();
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
			public void widgetSelected(final SelectionEvent e) {
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
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
	}

	public void dispose() {
		removeAdapters();
		cellRenderers.clear();

		currentElements.clear();
		columnSortOrder.clear();
	}
}
