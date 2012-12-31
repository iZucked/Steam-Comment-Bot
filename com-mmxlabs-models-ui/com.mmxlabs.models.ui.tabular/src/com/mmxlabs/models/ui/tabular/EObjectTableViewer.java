/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.tabular.filter.FilterUtils;
import com.mmxlabs.models.ui.tabular.filter.IFilter;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * A TableViewer which displays a list of EObjects using the other classes in this package. Factored out of EObjectEditorViewerPane so that it can be used in dialogs without causing trouble, and
 * without import/export buttons.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectTableViewer extends GridTableViewer {
	private final static Logger log = LoggerFactory.getLogger(EObjectTableViewer.class);

	protected static final String COLUMN_PATH = "COLUMN_PATH";
	protected static final String COLUMN_RENDERER = "COLUMN_RENDERER";
	protected static final String COLUMN_MANIPULATOR = "COLUMN_MANIPULATOR";
	protected static final String COLUMN_MNEMONICS = "COLUMN_MNEMONICS";
	protected static final String COLUMN_RENDERER_AND_PATH = "COLUMN_RENDERER_AND_PATH";

	protected IColorProvider delegateColourProvider;

	protected IStatusProvider statusProvider;

	protected IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
			final HashSet<Object> updates = new HashSet<Object>();
			for (final Map.Entry<Object, IStatus> entry : validationErrors.entrySet()) {
				if (!entry.getValue().isOK())
					updates.add(entry.getKey());
			}

			validationErrors.clear();

			processStatus(status, true);
		}
	};

	final HashSet<EObject> objectsToUpdate = new HashSet<EObject>();
	boolean waitingForUpdate = false;
	final Runnable updateRunner = new Runnable() {
		@Override
		public void run() {
			synchronized (objectsToUpdate) {
				for (final EObject o : objectsToUpdate) {
					EObjectTableViewer.this.update(o, null);
					EObjectTableViewer.this.updateObjectExternalNotifiers(o);

					// consider refresh?
				}
				objectsToUpdate.clear();
				waitingForUpdate = false;
			}
		}
	};

	final MMXContentAdapter adapter = new MMXContentAdapter() {
		@Override
		protected void missedNotifications(final List<Notification> notifications) {
			for (final Notification notification : new ArrayList<Notification>(notifications)) {
				if (!notification.isTouch()) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!getControl().isDisposed())
								refresh();
						}
					});
					return;
				}
			}
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			// System.err.println(notification);
			if (notification.isTouch() == false) {
				if (notification.getEventType() == Notification.REMOVING_ADAPTER)
					return;
				// this is a change, so we have to refresh.
				// ideally we just want to update the changed object, but we
				// get the notification from
				// somewhere below, so we need to go up
				EObject source = (EObject) notification.getNotifier();
				// if (currentElements.contains(source))
				// return;
				if (source == currentContainer) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							refresh();
						}
					});
					return;
				}
				source = getElementForNotificationTarget(source);
				if (source != null) {
					synchronized (objectsToUpdate) {
						objectsToUpdate.add(source);
						if (!waitingForUpdate) {
							waitingForUpdate = true;
							Display.getDefault().asyncExec(updateRunner);
						}
					}
					return;
				}
			}
		}
	};

	public EObject getElementForNotificationTarget(EObject source) {
		while (!(currentElements.contains(source)) && ((source = source.eContainer()) != null))
			;
		return source;
	}

	private final LinkedList<Pair<EMFPath, ICellRenderer>> cellRenderers = new LinkedList<Pair<EMFPath, ICellRenderer>>();

	private final ArrayList<GridColumn> columnSortOrder = new ArrayList<GridColumn>();

	/**
	 * A one-element list referring to the EObject which contains all the display elements. #adapter uses this to determine when a notification comes on the top-level object, and so all the contents
	 * should be refreshed (e.g. after an import)
	 */
	EObject currentContainer;

	/**
	 * Overridding sort order of objects. Any change in column sort order will set this back to null.
	 */
	private List<Object> fixedSortOrder = null;

	/**
	 * @return the currentContainer
	 */
	public EObject getCurrentContainer() {
		return currentContainer;
	}

	public EReference getCurrentContainment() {
		return currentReference;
	}

	/**
	 * A set containing the elements currently being displayed, which is used by #adapter to determine which row to refresh when a notification comes in
	 */
	final HashSet<EObject> currentElements = new HashSet<EObject>();

	private final IMMXAdapter externalAdapter = new MMXAdapterImpl() {

		protected void missedNotifications(final List<Notification> missed) {
			for (final Notification n : new ArrayList<Notification>(missed)) {
				if (n != null) {
					reallyNotifyChanged(n);
				}
			}
		}

		@Override
		public void reallyNotifyChanged(final Notification msg) {
			if (!msg.isTouch()) {
				final Object notifier = msg.getNotifier();
				// redraw all objects listening to this notifier
				final Set<EObject> changed = externalReferences.get(notifier);
				if (changed != null) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							// wait to refresh if there is an import happening
							// elsewhere. then refresh everything.
							if (refreshOrGiveUp()) {
								return;
							}

							for (final EObject e : changed) {
								EObjectTableViewer.this.update(e, null);
							}

						}
					});
				}
			}
		}
	};

	private final Map<EObject, Set<Notifier>> externalNotifiersByObject = new HashMap<EObject, Set<Notifier>>();

	private final Map<Notifier, Set<EObject>> externalReferences = new HashMap<Notifier, Set<EObject>>();

	private boolean sortDescending = false;

	private IFilter filter = null;

	protected boolean lockedForEditing = false;

	/**
	 * @return True if editing is currently disabled on this table.
	 */
	public boolean isLocked() {
		return lockedForEditing;
	}

	/**
	 * @param Set
	 *            to true if editing should be disabled on this table.
	 */
	public void setLocked(final boolean lockedForEditing) {
		this.lockedForEditing = lockedForEditing;
		if (!getControl().isDisposed()) {
			refresh(true);
		}
	}

	private boolean displayValidationErrors = true;

	final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	public boolean isDisplayValidationErrors() {
		return displayValidationErrors;
	}

	public void setDisplayValidationErrors(final boolean displayValidationErrors) {
		this.displayValidationErrors = displayValidationErrors;
	}

	public EObjectTableViewer(final Composite parent, final int style) {
		super(parent, style);

		ColumnViewerToolTipSupport.enableFor(this);
	}

	public void setFilterString(final String filterString) {
		if (filterString.isEmpty()) {
			filter = null;
		}
		final FilterUtils utils = new FilterUtils();
		filter = utils.parseFilterString(filterString);
		refresh(false);
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final Object... pathObjects) {
		// final EMFPath path = new CompiledEMFPath(getClass().getClassLoader(), true, pathObjects);
		return addColumn(columnName, renderer, manipulator, new EMFPath(true, pathObjects));
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final EMFPath path) {

		// create a column
		final GridTableViewer viewer = this;

		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		final GridColumn tColumn = column.getColumn();

		{
			final Pair<EMFPath, ICellRenderer> pathAndRenderer = new Pair<EMFPath, ICellRenderer>(path, renderer);
			cellRenderers.add(pathAndRenderer);
			tColumn.setData(COLUMN_RENDERER_AND_PATH, pathAndRenderer);
		}

		tColumn.setMoveable(true);
		tColumn.setText(columnName);
		tColumn.pack();
		// tColumn.setResizable(true);

		// store the renderer here, so that we can use it in sorting later.
		tColumn.setData(COLUMN_RENDERER, renderer);
		tColumn.setData(COLUMN_PATH, path);
		tColumn.setData(COLUMN_MANIPULATOR, manipulator);
		{
			final List<String> mnems = new LinkedList<String>();
			tColumn.setData(COLUMN_MNEMONICS, mnems);
			mnems.add(columnName.toLowerCase().replace(" ", ""));
			String initials = "";
			boolean ws = true;
			for (int i = 0; i < columnName.length(); i++) {
				final char c = columnName.charAt(i);
				if (Character.isWhitespace(c)) {
					ws = true;
				} else {
					if (ws) {
						initials += c;
					}
					ws = false;
				}
			}
			mnems.add(initials.toLowerCase());
		}

		// GridViewerEditor.create(viewer, new ColumnViewerEditorActivationStrategy(viewer) {
		// long timer = 0;
		//
		// /*
		// * (non-Javadoc)
		// *
		// * @see org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy#isEditorActivationEvent(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
		// */
		// @Override
		// protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
		// final long fireTime = System.currentTimeMillis();
		// final boolean activate = (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION)
		// || ((event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED) && (event.keyCode == SWT.F2) && ((fireTime - timer) > 500)); // this is a hack; for some reason without
		// // this we get loads of keydown events.
		// timer = fireTime;
		// return activate;
		// }
		//
		// }, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR | ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK);

		columnSortOrder.add(tColumn);

		column.setLabelProvider(new EObjectTableViewerColumnProvider(this, renderer, path));

		column.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected boolean canEdit(final Object element) {
				return (lockedForEditing == false) && (manipulator != null) && manipulator.canEdit(path.get((EObject) element));
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				return manipulator.getCellEditor(viewer.getGrid(), path.get((EObject) element));
			}

			@Override
			protected Object getValue(final Object element) {
				return manipulator.getValue(path.get((EObject) element));
			}

			@Override
			protected void setValue(final Object element, final Object value) {
				// a value has come out of the celleditor and is being set on
				// the element.
				if (lockedForEditing) {
					return;
				}
				manipulator.setValue(path.get((EObject) element), value);
				refresh();
			}
		});

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {

				// Sort order changed - clear fixed ordering
				fixedSortOrder = null;

				if (columnSortOrder.get(0) == tColumn) {
					sortDescending = !sortDescending;
				} else {
					sortDescending = false;
					columnSortOrder.get(0).setSort(SWT.NONE);
					columnSortOrder.remove(tColumn);
					columnSortOrder.add(0, tColumn);
				}
				tColumn.setSort(sortDescending ? SWT.UP : SWT.DOWN);
				viewer.refresh(false);
			}
		});

		column.getColumn().setCellRenderer(createCellRenderer());

		return column;
	}

	/**
	 * Create a "simple" column linked to the {@link EObjectTableViewer}. A {@link CellLabelProvider} must be set on the column by the caller. If the column is sortable, then a {@link EMFPath} and
	 * {@link ICellRenderer} will need to be set as in the {@link #addColumn(String, ICellRenderer, ICellManipulator, EMFPath)} method.
	 * 
	 * @param columnName
	 * @return
	 */
	public GridViewerColumn addSimpleColumn(final String columnName, final boolean sortable) {

		// create a column
		final GridTableViewer viewer = this;

		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		final GridColumn tColumn = column.getColumn();

		tColumn.setMoveable(true);
		tColumn.setText(columnName);
		tColumn.pack();
		// tColumn.setResizable(true);

		{
			final List<String> mnems = new LinkedList<String>();
			tColumn.setData(COLUMN_MNEMONICS, mnems);
			mnems.add(columnName.toLowerCase().replace(" ", ""));
			String initials = "";
			boolean ws = true;
			for (int i = 0; i < columnName.length(); i++) {
				final char c = columnName.charAt(i);
				if (Character.isWhitespace(c)) {
					ws = true;
				} else {
					if (ws) {
						initials += c;
					}
					ws = false;
				}
			}
			mnems.add(initials.toLowerCase());
		}

		// GridViewerEditor.create(viewer, new ColumnViewerEditorActivationStrategy(viewer) {
		// long timer = 0;
		//
		// /*
		// * (non-Javadoc)
		// *
		// * @see org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy#isEditorActivationEvent(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
		// */
		// @Override
		// protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
		// final long fireTime = System.currentTimeMillis();
		// final boolean activate = (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION)
		// || ((event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED) && (event.keyCode == SWT.F2) && ((fireTime - timer) > 500)); // this is a hack; for some reason without
		// // this we get loads of keydown events.
		// timer = fireTime;
		// return activate;
		// }
		//
		// }, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR | ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK);

		if (sortable) {
			columnSortOrder.add(tColumn);

			column.getColumn().addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {

					// Sort order changed - clear fixed ordering
					fixedSortOrder = null;

					if (columnSortOrder.get(0) == tColumn) {
						sortDescending = !sortDescending;
					} else {
						sortDescending = false;
						columnSortOrder.get(0).setSort(SWT.NONE);
						columnSortOrder.remove(tColumn);
						columnSortOrder.add(0, tColumn);
					}
					tColumn.setSort(sortDescending ? SWT.UP : SWT.DOWN);
					viewer.refresh(false);
				}
			});
		}
		column.getColumn().setCellRenderer(createCellRenderer());

		return column;
	}

	/**
	 * @since 2.0
	 */
	protected GridCellRenderer createCellRenderer() {
		return new AlternatingRowCellRenderer();
	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}

	public void dispose() {
		removeAdapters();
		cellRenderers.clear();

		currentContainer = null;
		externalReferences.clear();
		externalNotifiersByObject.clear();
		currentElements.clear();
		columnSortOrder.clear();

		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
		}
	}

	@Override
	public Control getControl() {
		return getGrid();
	}

	public void init(final IStructuredContentProvider contentProvider) {
		final GridTableViewer viewer = this;
		final Grid table = viewer.getGrid();

		table.setRowHeaderVisible(true);
		table.setRowHeaderRenderer(new NoIndexRowHeaderRenderer());

		// This appears to do nothing in the Nebula Grid case.
		// See Grid#setItemHeight() instead
		final Listener measureListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.height = 18;
			}
		};

		table.addListener(SWT.MeasureItem, measureListener);

		table.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				table.removeListener(SWT.MeasureItem, measureListener);
				dispose();
			}
		});

		viewer.setContentProvider(

		new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				if (oldInput == null) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if (!viewer.getControl().isDisposed()) {
								if (viewer instanceof GridTableViewer) {
									for (final GridColumn tc : ((GridTableViewer) viewer).getGrid().getColumns()) {
										tc.pack();
									}
								}
							}
						}
					});
				}

				contentProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				contentProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				removeAdapters();
				currentElements.clear();
				final Object[] elements = contentProvider.getElements(inputElement);
				for (final Object o : elements) {
					if (o instanceof EObject) {
						currentElements.add((EObject) o);
						updateObjectExternalNotifiers((EObject) o);
					}
				}

				if (statusProvider != null && inputElement != null) {
					// Perform initial validation
					processStatus(statusProvider.getStatus(), false);
				}

				return elements;
			}
		});

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				// If there is a fixed sort order use that.
				if (fixedSortOrder != null) {
					final int idx1 = fixedSortOrder.indexOf(e1);
					final int idx2 = fixedSortOrder.indexOf(e2);
					return idx1 - idx2;
				}

				final Iterator<GridColumn> iterator = columnSortOrder.iterator();
				int comparison = 0;
				while (iterator.hasNext() && (comparison == 0)) {
					final GridColumn column = iterator.next();
					final ICellRenderer renderer = (ICellRenderer) column.getData(COLUMN_RENDERER);
					final EMFPath path = (EMFPath) column.getData(COLUMN_PATH);

					if (path != null) {

						final Object v1 = path.get((EObject) e1);
						final Object v2 = path.get((EObject) e2);

						final Comparable left = renderer.getComparable(v1);
						final Comparable right = renderer.getComparable(v2);
						if (left == null) {
							return -1;
						} else if (right == null) {
							return 1;
						} else {
							comparison = left.compareTo(right);
						}
					} else {
						final Comparable left = renderer.getComparable(e1);
						final Comparable right = renderer.getComparable(e2);
						if (left == null) {
							return -1;
						} else if (right == null) {
							return 1;
						} else {
							comparison = left.compareTo(right);
						}
					}
				}
				return sortDescending ? -comparison : comparison;
			}
		});

		addFilter(new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				if (filter == null) {
					return true;
				}

				/**
				 * This map contains representations of each column for this object, both the real value and the display value.
				 */
				final Map<String, Pair<?, ?>> attributes = new HashMap<String, Pair<?, ?>>();
				// this could probably be much faster
				for (final GridColumn column : getGrid().getColumns()) {
					final ICellRenderer renderer = (ICellRenderer) column.getData(COLUMN_RENDERER);
					final EMFPath path = (EMFPath) column.getData(COLUMN_PATH);
					if (path == null)
						continue;
					final Object fieldValue = path.get((EObject) element);
					final Object filterValue = renderer.getFilterValue(fieldValue);
					final Object renderValue = renderer.render(fieldValue);

					final List<String> mnemonics = (List<String>) column.getData(COLUMN_MNEMONICS);
					for (final String m : mnemonics) {
						attributes.put(m, new Pair<Object, Object>(filterValue, renderValue));
					}
				}

				return filter.matches(attributes);

			}
		});
	}

	private EReference currentReference;

	public void init(final AdapterFactory adapterFactory, final EReference... path) {
		init(new IStructuredContentProvider() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							o.eAdapters().add(adapter);
							currentContainer = o;
							currentReference = ref;
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

			@Override
			public void dispose() {

			}

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}
		});
	}

	protected boolean refreshOrGiveUp() {
		refresh();
		return false;
	}

	protected void removeAdapters() {
		adapter.disable();
		externalAdapter.disable();
		if (currentContainer != null) {
			currentContainer.eAdapters().remove(adapter);
		}
		for (final Notifier n : externalReferences.keySet()) {
			n.eAdapters().remove(externalAdapter);
		}

		externalReferences.clear();
		externalNotifiersByObject.clear();

		// TODO put this into IMMXAdapter#enable(boolean)
		adapter.enable();
		externalAdapter.enable();
	}

	private void updateObjectExternalNotifiers(final EObject object) {
		final Set<Notifier> dropNotifiers = new HashSet<Notifier>();
		final Set<Notifier> addNotifiers = new HashSet<Notifier>();
		Set<Notifier> notifiers = externalNotifiersByObject.get(object);

		// look at the existing notifiers for this object, and disassociate them
		if (notifiers != null) {
			for (final Notifier notifier : notifiers) {
				final Set<EObject> references = externalReferences.get(notifier);
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
			final Iterable<Pair<Notifier, List<Object>>> newNotifiers = pathAndRenderer.getSecond().getExternalNotifiers(pathAndRenderer.getFirst().get(object));
			for (final Pair<Notifier, List<Object>> notifierAndFeatures : newNotifiers) {
				// get the notifier we are interested in
				final Notifier n = notifierAndFeatures.getFirst();
				if (n == null) {
					log.debug(pathAndRenderer + " has provided a null notifier for " + object);
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
			if (n != null) {
				n.eAdapters().remove(externalAdapter);
			}
		}

		for (final Notifier n : addNotifiers) {
			if (n != null) {
				n.eAdapters().add(externalAdapter);
			}
		}
	}

	/**
	 * @return names that can be used in the filter (see {@link #setFilterString(String)})
	 */
	public Map<String, List<String>> getColumnMnemonics() {
		final Map<String, List<String>> ms = new TreeMap<String, List<String>>();

		for (final GridColumn column : getGrid().getColumns()) {
			final List<String> mnemonics = (List<String>) column.getData(COLUMN_MNEMONICS);
			ms.put(column.getText(), mnemonics);
		}

		return ms;
	}

	/**
	 * Get possible (unfiltered) values for the column with the given name
	 * 
	 * @param columnName
	 * @return
	 */
	public Set<String> getDistinctValues(final String columnName) {
		final TreeSet<String> result = new TreeSet<String>();

		for (final GridColumn column : getGrid().getColumns()) {
			if (column.getText().equals(columnName)) {
				final ICellRenderer renderer = (ICellRenderer) column.getData(COLUMN_RENDERER);
				final EMFPath path = (EMFPath) column.getData(COLUMN_PATH);
				for (final EObject element : currentElements) {
					result.add(renderer.render(path.get(element)));
				}
				return result;
			}
		}

		return result;
	}

	public void removeColumn(final GridViewerColumn column) {
		columnSortOrder.remove(column.getColumn());
		final Pair<EMFPath, ICellRenderer> pathAndRenderer = (Pair<EMFPath, ICellRenderer>) column.getColumn().getData(COLUMN_RENDERER_AND_PATH);
		cellRenderers.remove(pathAndRenderer);
		column.getColumn().dispose();
	}

	public IColorProvider getColourProvider() {
		return delegateColourProvider;
	}

	public void setColourProvider(final IColorProvider delegateColourProvider) {
		this.delegateColourProvider = delegateColourProvider;
	}

	public Map<Object, IStatus> getValidationErrors() {
		return validationErrors;
	}

	public IStatusProvider getStatusProvider() {
		return statusProvider;
	}

	public void setStatusProvider(final IStatusProvider statusProvider) {
		this.statusProvider = statusProvider;
		statusProvider.addStatusChangedListener(statusChangedListener);
	}

	/**
	 * @since 2.0
	 */
	protected void processStatus(final IStatus status, final boolean update) {
		if (status == null)
			return;
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				processStatus(s, update);
			}
		}
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus detailConstraintStatus = (IDetailConstraintStatus) status;
			if (!status.isOK()) {
				updateObject(getElementForNotificationTarget(detailConstraintStatus.getTarget()), status, update);

				for (final EObject e : detailConstraintStatus.getObjects()) {
					updateObject(getElementForNotificationTarget(e), status, update);
				}
			}
		}
	}

	/**
	 * @since 2.0
	 */
	protected void updateObject(final EObject object, final IStatus status, final boolean update) {
		if (object != null) {
			setStatus(object, status);
			if (update) {
				update(object, null);
			}
		}
	}

	/**
	 * @since 2.0
	 */
	protected void setStatus(final Object e, final IStatus s) {
		final IStatus existing = validationErrors.get(e);
		if (existing == null || s.getSeverity() > existing.getSeverity()) {
			validationErrors.put(e, s);
		}
	}

	/**
	 * @since 2.0
	 */
	public List<Object> getFixedSortOrder() {
		return fixedSortOrder;
	}

	/**
	 * Set a predefined sort order to override current column sort order. This will be overridden if the column sort order changes.
	 * 
	 * @since 2.0
	 */
	public void setFixedSortOrder(final List<Object> fixedSortOrder) {
		this.fixedSortOrder = fixedSortOrder;
	}
}