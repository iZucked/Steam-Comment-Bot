/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.EventObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
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
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.validation.IStatusProvider;
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
	
	protected final Set<String> allMnemonics = new HashSet<String>();

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
	 * @since 3.1
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
		super(parent, style);
		this.validationSupport = createValidationSupport();
		this.filterSupport = new EObjectTableViewerFilterSupport(this);
		ColumnViewerToolTipSupport.enableFor(this);
	}

	/**
	 * @since 4.0
	 */
	protected EObjectTableViewerValidationSupport createValidationSupport() {
		return new EObjectTableViewerValidationSupport(this) {

			@Override
			public EObject getElementForValidationTarget(EObject source) {
				return getElementForNotificationTarget(source);
			}
		};
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final Object... pathObjects) {
		// final EMFPath path = new CompiledEMFPath(getClass().getClassLoader(), true, pathObjects);
		return addColumn(columnName, renderer, manipulator, new EMFPath(true, pathObjects));
	}
	
	public void setColumnMnemonics(final GridColumn column, final List<String> mnemonics) {
		column.setData(EObjectTableViewer.COLUMN_MNEMONICS, mnemonics);		
		for (String string: mnemonics) {
			allMnemonics.add(string);
		}
	}
	
	protected String uniqueMnemonic(final String mnemonic) {
		String result = mnemonic;
		int suffix = 2;
		while (allMnemonics.contains(result)) {
			result = mnemonic + suffix++;
		}
		return result;
	}
	
	protected List<String> makeMnemonics(final String columnName) {
		LinkedList<String> result = new LinkedList<String>();
		
		result.add(uniqueMnemonic(columnName.toLowerCase().replace(" ", "")));
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
		result.add(uniqueMnemonic(initials.toLowerCase()));
		
		return result;
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
		
		setColumnMnemonics(tColumn, makeMnemonics(columnName));

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
		final GridTableViewer viewer = this;

		final GridViewerColumn column = new GridViewerColumn(viewer, SWT.NONE);
		final GridColumn tColumn = column.getColumn();

		tColumn.setMoveable(true);
		tColumn.setText(columnName);
		tColumn.pack();

		setColumnMnemonics(tColumn, makeMnemonics(columnName));

		if (sortable) {
			sortingSupport.addSortableColumn(viewer, column, tColumn);
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
	}

	@Override
	public Control getControl() {
		return getGrid();
	}

	/**
	 * @since 3.1
	 */
	public void init(final IStructuredContentProvider contentProvider, final CommandStack commandStack) {
		final GridTableViewer viewer = this;
		final Grid table = viewer.getGrid();

		currentCommandStack = commandStack;
		if (currentCommandStack != null) {
			currentCommandStack.addCommandStackListener(commandStackListener);
		}

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
						@Override
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
				// removeAdapters();
				currentElements.clear();
				final Object[] elements = contentProvider.getElements(inputElement);
				for (final Object o : elements) {
					if (o instanceof EObject) {
						currentElements.add((EObject) o);
						// updateObjectExternalNotifiers((EObject) o);
					}
				}

				if (inputElement != null) {
					// Perform initial validation
					validationSupport.processStatus(false);
				}

				return elements;
			}
		});

		viewer.setComparator(sortingSupport.createViewerComparer());

		addFilter(filterSupport.createViewerFilter());
	}

	private EReference currentReference;

	/**
	 * @since 3.1
	 */
	public void init(final AdapterFactory adapterFactory, final CommandStack commandStack, final EReference... path) {
		init(new IStructuredContentProvider() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							// o.eAdapters().add(adapter);
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

	/**
	 * @since 4.0
	 */
	public EObjectTableViewerValidationSupport getValidationSupport() {
		return validationSupport;
	}

	public EObject getElementForNotificationTarget(EObject source) {
		while (!(currentElements.contains(source)) && ((source = source.eContainer()) != null))
			;
		return source;
	}

	/**
	 * @since 4.0
	 */
	public HashSet<EObject> getCurrentElements() {
		return currentElements;
	}

	/**
	 * @since 4.0
	 */
	public EObjectTableViewerFilterSupport getFilterSupport() {
		return filterSupport;
	}

	public void setStatusProvider(IStatusProvider statusProvider) {
		validationSupport.setStatusProvider(statusProvider);
	}

	/**
	 * @since 4.0
	 */
	public EObjectTableViewerSortingSupport getSortingSupport() {
		return sortingSupport;
	}
}