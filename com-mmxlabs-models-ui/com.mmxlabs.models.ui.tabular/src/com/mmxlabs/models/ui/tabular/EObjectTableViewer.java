/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItemDataVisualizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.renderers.AlternatingRowCellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * A TableViewer which displays a list of EObjects using the other classes in this package. Factored out of EObjectEditorViewerPane so that it can be used in dialogs without causing trouble, and
 * without import/export buttons.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectTableViewer extends GridTreeViewer {

	public static final String COLUMN_PATH = "COLUMN_PATH";
	public static final String COLUMN_RENDERER = "COLUMN_RENDERER";
	public static final String COLUMN_COMPARABLE_PROVIDER = "COLUMN_COMPARABLE_PROVIDER";
	public static final String COLUMN_MANIPULATOR = "COLUMN_MANIPULATOR";
	public static final String COLUMN_RENDERER_AND_PATH = "COLUMN_RENDERER_AND_PATH";

	/**
	 */
	public static final String COLUMN_SORT_PATH = "COLUMN_SORT_PATH";

	private CommandStack currentCommandStack;
	private final CommandStackListener commandStackListener = new CommandStackListener() {

		@Override
		public void commandStackChanged(final EventObject event) {

			// TODO: This is fairly coarse grained check -perhaps we should check the mostRecentCommand result and check to see if it contains the container - OR contained elements?

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!getControl().isDisposed()) {
						doCommandStackChanged();
					}
				}
			});
		}
	};

	protected IColorProvider delegateColourProvider;

	protected IToolTipProvider delegateToolTipProvider;

	private final EObjectTableViewerSortingSupport sortingSupport = new EObjectTableViewerSortingSupport();

	final HashSet<EObject> objectsToUpdate = new HashSet<EObject>();
	boolean waitingForUpdate = false;
	final Runnable updateRunner = new Runnable() {
		@Override
		public void run() {
			synchronized (objectsToUpdate) {
				for (final EObject o : objectsToUpdate) {
					EObjectTableViewer.this.update(o, null);
					// EObjectTableViewer.this.updateObjectExternalNotifiers(o);

					// consider refresh?
				}
				objectsToUpdate.clear();
				waitingForUpdate = false;
			}
		}
	};

	/**
	 */
	protected void doCommandStackChanged() {
		refresh();
	}

	private final LinkedList<Pair<EMFPath, ICellRenderer>> cellRenderers = new LinkedList<Pair<EMFPath, ICellRenderer>>();

	/**
	 * A one-element list referring to the EObject which contains all the display elements. #adapter uses this to determine when a notification comes on the top-level object, and so all the contents
	 * should be refreshed (e.g. after an import)
	 */
	EObject currentContainer;

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
	 * Call this method if {@link #init(AdapterFactory, CommandStack, EReference...)} is overridden
	 * 
	 */
	public void setCurrentContainerAndContainment(final EObject currentContainer, final EReference currentReference) {
		this.currentContainer = currentContainer;
		this.currentReference = currentReference;
	}

	/**
	 * A set containing the elements currently being displayed, which is used by #adapter to determine which row to refresh when a notification comes in
	 */
	private final HashSet<EObject> currentElements = new HashSet<EObject>();

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

	private final EObjectTableViewerValidationSupport validationSupport;
	private final EObjectTableViewerFilterSupport filterSupport;

	public boolean isDisplayValidationErrors() {
		return displayValidationErrors;
	}

	public void setDisplayValidationErrors(final boolean displayValidationErrors) {
		this.displayValidationErrors = displayValidationErrors;
	}

	public EObjectTableViewer(final Composite parent, final int style) {
		// The default super class constructor creates a data visualizer with
		// explicit background & foreground; this interferes with the
		// default checking in the AlternatingRowCellRenderer.
		// Here we make sure that our EObjectTableViewer objects are
		// initialised with null settings for the cell background colour.
		super(new Grid(new GridItemDataVisualizer(null, null, null), parent, style));
		this.validationSupport = createValidationSupport();
		this.filterSupport = new EObjectTableViewerFilterSupport(this, this.getGrid());
		ColumnViewerToolTipSupport.enableFor(this);
		GridViewerHelper.configureLookAndFeel(this);
	}

	/**
	 */
	protected EObjectTableViewerValidationSupport createValidationSupport() {
		return new EObjectTableViewerValidationSupport(this) {

			@Override
			public EObject getElementForValidationTarget(final EObject source) {
				return getElementForNotificationTarget(source);
			}
		};
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final ETypedElement... pathObjects) {
		// final EMFPath path = new CompiledEMFPath(getClass().getClassLoader(), true, pathObjects);
		return addColumn(columnName, renderer, manipulator, new EMFPath(true, pathObjects));
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final EMFPath path) {

		// create a column
		final GridTreeViewer viewer = this;

		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		final GridColumn tColumn = column.getColumn();
		tColumn.setHeaderRenderer(new ColumnHeaderRenderer());

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
		tColumn.setData(COLUMN_COMPARABLE_PROVIDER, renderer);
		tColumn.setData(COLUMN_PATH, path);
		tColumn.setData(COLUMN_MANIPULATOR, manipulator);

		filterSupport.createColumnMnemonics(tColumn, columnName);

		column.setLabelProvider(new EObjectTableViewerColumnProvider(this, renderer, path));

		column.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected boolean canEdit(final Object element) {
				return (lockedForEditing == false) && (manipulator != null) && manipulator.canEdit(path.get((EObject) element));
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {

				final CellEditor cellEditor = manipulator.getCellEditor(viewer.getGrid(), path.get((EObject) element));
				if (cellEditor != null) {
					// Hook our control listener into the cell editor
					// Note - SG: This looks like we could have used a CellEditorActionHandler, but I could not get it working, so lifted the salient pieces.
					final Control control = cellEditor.getControl();

					// Remove existing listener references.
					control.removeListener(SWT.Activate, controlListener);
					control.removeListener(SWT.Deactivate, controlListener);

					controlToEditor.put(control, cellEditor);

					control.addListener(SWT.Activate, controlListener);
					control.addListener(SWT.Deactivate, controlListener);
				}
				return cellEditor;
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
		sortingSupport.addSortableColumn(viewer, column, tColumn);

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
		final GridTreeViewer viewer = this;

		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		final GridColumn tColumn = column.getColumn();
		tColumn.setHeaderRenderer(new ColumnHeaderRenderer());

		tColumn.setMoveable(true);
		tColumn.setText(columnName);
		tColumn.pack();

		filterSupport.createColumnMnemonics(tColumn, columnName);

		if (sortable) {
			sortingSupport.addSortableColumn(viewer, column, tColumn);
		}
		column.getColumn().setCellRenderer(createCellRenderer());

		return column;
	}

	/**
	 */
	protected GridCellRenderer createCellRenderer() {
		return new AlternatingRowCellRenderer();
	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}

	public void dispose() {
		// removeAdapters();
		cellRenderers.clear();

		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(commandStackListener);
			currentCommandStack = null;
		}

		currentContainer = null;
		// externalReferences.clear();
		// externalNotifiersByObject.clear();
		currentElements.clear();
		sortingSupport.clearColumnSortOrder();

		validationSupport.dispose();

		for (final Widget control : controlToEditor.keySet()) {
			control.removeListener(SWT.Activate, controlListener);
			control.removeListener(SWT.Deactivate, controlListener);
		}

		controlToEditor.clear();
	}

	@Override
	public Control getControl() {
		return getGrid();
	}

	/**
	 */
	public void init(final ITreeContentProvider contentProvider, final CommandStack commandStack) {
		final GridTreeViewer viewer = this;
		final Grid grid = viewer.getGrid();

		currentCommandStack = commandStack;
		if (currentCommandStack != null) {
			currentCommandStack.addCommandStackListener(commandStackListener);
		}

		// table.setRowHeaderVisible(true);
		// table.setRowHeaderRenderer(new NoIndexRowHeaderRenderer());

		// This appears to do nothing in the Nebula Grid case.
		// See Grid#setItemHeight() instead
		final Listener measureListener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.height = 18;
			}
		};

		grid.addListener(SWT.MeasureItem, measureListener);

		grid.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				grid.removeListener(SWT.MeasureItem, measureListener);
				dispose();
			}
		});

		setWrappedContentProvider(contentProvider);
		viewer.setComparator(sortingSupport.createViewerComparer());

		addFilter(filterSupport.createViewerFilter());
	}

	public void setWrappedContentProvider(final ITreeContentProvider contentProvider) {
		final GridTreeViewer viewer = this;
		viewer.setContentProvider(

				new ITreeContentProvider() {

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

						if (oldInput == null) {
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!viewer.getControl().isDisposed()) {
										if (viewer instanceof GridTableViewer) {
											for (final GridColumn tc : ((GridTableViewer) viewer).getGrid().getColumns()) {
												tc.pack();
											}
										} else if (viewer instanceof GridTreeViewer) {
											for (final GridColumn tc : ((GridTreeViewer) viewer).getGrid().getColumns()) {
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
						// removeAdapters();
						currentElements.clear();
						final Object[] elements = contentProvider.getElements(inputElement);
						for (final Object o : elements) {
							if (o instanceof EObject) {
								currentElements.add((EObject) o);
								// updateObjectExternalNotifiers((EObject) o);
							}
						}

						validationSupport.getValidationErrors().clear();
						if (inputElement != null) {
							// Perform initial validation
							validationSupport.processStatus(false);
						}

						return elements;
					}

					@Override
					public Object[] getChildren(final Object parentElement) {

						final Object[] elements = contentProvider.getChildren(parentElement);
						for (final Object o : elements) {
							if (o instanceof EObject) {
								currentElements.add((EObject) o);
							}
						}

						return elements;
					}

					@Override
					public Object getParent(final Object element) {
						return contentProvider.getParent(element);
					}

					@Override
					public boolean hasChildren(final Object element) {
						return contentProvider.hasChildren(element);
					}
				});

	}

	private EReference currentReference;

	/**
	 */
	public void init(final AdapterFactory adapterFactory, final CommandStack commandStack, final EReference... path) {
		init(new ITreeContentProvider() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							setCurrentContainerAndContainment(o, ref);
							return ((EList) object).toArray();
						}
						if (object instanceof EObject) {
							o = (EObject) object;
						} else if (object == null) {
							return new Object[] {};
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

			@Override
			public Object[] getChildren(final Object parentElement) {
				return null;
			}

			@Override
			public boolean hasChildren(final Object element) {
				return false;
			}

			@Override
			public Object getParent(final Object element) {
				return null;
			}

		}, commandStack);
	}

	protected boolean refreshOrGiveUp() {
		refresh();
		return false;
	}

	public void removeColumn(final GridViewerColumn column) {
		sortingSupport.removeSortableColumn(column.getColumn());
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

	public IToolTipProvider getToolTipProvider() {
		return delegateToolTipProvider;
	}

	public void setToolTipProvider(final IToolTipProvider delegateToolTipProvider) {
		this.delegateToolTipProvider = delegateToolTipProvider;
	}

	/**
	 */
	public EObjectTableViewerValidationSupport getValidationSupport() {
		return validationSupport;
	}

	public EObject getElementForNotificationTarget(EObject source) {
		// Add assert to catch issues during development - normally shouldn't get a null object here unless overriding methods have bugs.
		assert (source != null);
		while (source != null && !(currentElements.contains(source)) && ((source = source.eContainer()) != null))
			;
		return source;
	}

	/**
	 */
	public HashSet<EObject> getCurrentElements() {
		return currentElements;
	}

	/**
	 */
	public EObjectTableViewerFilterSupport getFilterSupport() {
		return filterSupport;
	}

	public void setStatusProvider(final IStatusProvider statusProvider) {
		validationSupport.setStatusProvider(statusProvider);
	}

	/**
	 */
	public EObjectTableViewerSortingSupport getSortingSupport() {
		return sortingSupport;
	}

	private final Map<Widget, CellEditor> controlToEditor = new HashMap<>();

	/**
	 * Control listener used to listen for cell editor activate and deactivate events.
	 * 
	 */
	private class ControlListener implements Listener {
		@Override
		public void handleEvent(final Event event) {
			switch (event.type) {
			case SWT.Activate:
				cellEditorActivated(event.widget, controlToEditor.get(event.widget));
				break;
			case SWT.Deactivate:
				cellEditorDeactivated(event.widget, controlToEditor.get(event.widget));
			default:
				break;
			}
		}
	}

	private final ControlListener controlListener = new ControlListener();

	/**
	 * Overrideable method to indicate the given cell editor has activated. This can be used to e.g. disable global actions.
	 * 
	 * @param widget
	 * @param cellEditor
	 */
	protected void cellEditorActivated(final Widget widget, final CellEditor cellEditor) {
	}

	/**
	 * Overrideable method to indicate the given cell editor has deactivated. This can be used to e.g. enable global actions.
	 * 
	 * @param widget
	 * @param cellEditor
	 */
	protected void cellEditorDeactivated(final Widget widget, final CellEditor cellEditor) {
	}
}