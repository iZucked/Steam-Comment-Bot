/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.internal.Activator;

/**
 * Adapted from MarkersViewColumnsDialog in Eclipse code:
 * 
 * This was introduced as a fix to Bug 231081 and related, as an effort to combine the columns and preference dialogs into one. It should be noted that the class can be re-used or turned into a tool
 * for column viewers in general, but with some modifications. See example attached at the end of this class
 * 
 * @since 3.7
 * 
 * @author Hitesh Soliwal
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * 
 */
public abstract class ColumnConfigurationDialog extends TrayDialog {

	private static int RESET_ID = IDialogConstants.CLIENT_ID + 1;

	/** The list contains columns that are currently visible in viewer */
	private List<Object> visible;

	/** The list contains columns that are note shown in viewer */
	private List<Object> nonVisible;

	private TableViewer visibleViewer, nonVisibleViewer;

	private Button upButton, downButton;

	private Button toVisibleBtt, toNonVisibleBtt;

	private Label widthLabel;
	private Text widthText;

	private Point tableLabelSize;

	private final Comparator<Object> comparator = new Comparator<Object>() {

		@Override
		public int compare(final Object arg0, final Object arg1) {
			final IColumnInfoProvider infoProvider = doGetColumnInfoProvider();
			return infoProvider.getColumnIndex(arg0) - infoProvider.getColumnIndex(arg1);
		}

	};

	final List<CheckboxInfoManager> checkboxInfo = new ArrayList<>();

	/*
	 * private Set<String> rowCheckBoxStore; private String[] rowCheckBoxStrings;
	 * 
	 * private Set<String> diffCheckBoxStore; private String[] diffCheckBoxStrings;
	 */

	/**
	 * Create a new instance of the receiver.
	 * 
	 * @param parentShell
	 */
	public ColumnConfigurationDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		// don't create a Cancel button
		final Button button = createButton(parent, RESET_ID, "Reset to defaults", false);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				// hack: reset the visible / non-visible columns to the default states
				setColumnsObjs(doGetColumnUpdater().resetColumnStates());
				
				// and refresh the table viewers
				visibleViewer.refresh();
				nonVisibleViewer.refresh();

				// Refresh check box state.
				refreshCheckboxes();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}

		});
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Initialise visible /non-visible columns.
	 * 
	 * @param columnObjs
	 */
	// NOTE: this method is annoying because the column indices are not properly synchronised with
	// getColumnIndex() in the info provider.

	public void setColumnsObjs(final Object[] columnObjs) {
		final IColumnInfoProvider columnInfo = doGetColumnInfoProvider();
		final IColumnUpdater updater = doGetColumnUpdater();
		final List<Object> visible = getVisible();
		final List<Object> nonVisible = getNonVisible();
		visible.clear();
		nonVisible.clear();
		Object data = null;
		for (int i = 0; i < columnObjs.length; i++) {
			data = columnObjs[i];
			if (data == null) {
				continue;
			}
			// final int index = columnInfo.getColumnIndex(data);
			if (columnInfo.isColumnVisible(data)) {
				updater.setColumnVisible(data, true);
				// updater.setColumnIndex(data, visible.size());
				// updater.setColumnIndex(data, index);
				visible.add(data);
			} else {
				updater.setColumnVisible(data, false);
				// updater.setColumnIndex(data, nonVisible.size());
				// updater.setColumnIndex(data, index);
				nonVisible.add(data);
			}
		}
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		final Composite dialogArea = (Composite) super.createDialogArea(parent);

		dialogArea.setLayout(new GridLayout(1, true));

		initializeDialogUnits(dialogArea);

		// createMessageArea(dialogArea).setLayoutData(
		// new GridData(SWT.FILL, SWT.NONE, true, false));

		createDialogContentArea(dialogArea).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		applyDialogFont(dialogArea);

		return dialogArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.views.markers.ViewerSettingsAndStatusDialog# createDialogContentArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogContentArea(final Composite dialogArea) {
		final Composite composite = new Composite(dialogArea, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createInvisibleTable(composite);
		createMoveButtons(composite);
		createVisibleTable(composite);
		createUpDownBtt(composite);

		for (final CheckboxInfoManager manager : checkboxInfo) {
			createCheckBoxes(composite, manager);
		}
		// createWidthArea(composite);
		final Object element = visibleViewer.getElementAt(0);
		if (element != null)
			visibleViewer.setSelection(new StructuredSelection(element));
		visibleViewer.getTable().setFocus();
		return composite;
	}

	private Control createCheckBoxes(final Composite parent, final CheckboxInfoManager manager) {
		final Composite composite = new Composite(parent, SWT.NONE);
		if (manager != null && manager.options != null && manager.options.length > 0) {
			final GridLayout compositeLayout = new GridLayout();
			compositeLayout.marginHeight = 0;
			compositeLayout.marginWidth = 0;
			composite.setLayout(compositeLayout);
			composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

			final Label label = new Label(composite, SWT.NONE);
			label.setText(manager.title);
			final Set<String> store = manager.store.get();
			for (final OptionInfo option : manager.options) {
				final Button button = new Button(composite, SWT.CHECK);
				option.button = button;
				button.setText(option.label);
				button.setSelection(store.contains(option.id));
				button.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(final Event event) {
						if (store.contains(option.id)) {
							store.remove(option.id);
						} else {
							store.add(option.id);
						}
					}
				});
			}
		}
		return composite;
	}

	/**
	 * The Up and Down button to change column ordering.
	 * 
	 * @param parent
	 */
	Control createUpDownBtt(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		final Composite bttArea = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bttArea.setLayout(layout);
		bttArea.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));
		upButton = new Button(bttArea, SWT.PUSH);
		upButton.setText(JFaceResources.getString("ConfigureColumnsDialog_up")); //$NON-NLS-1$
		upButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleUpButton(event);
			}
		});
		setButtonLayoutData(upButton);
		((GridData) upButton.getLayoutData()).verticalIndent = tableLabelSize.y;
		upButton.setEnabled(false);

		downButton = new Button(bttArea, SWT.PUSH);
		downButton.setText(JFaceResources.getString("ConfigureColumnsDialog_down")); //$NON-NLS-1$
		downButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleDownButton(event);
			}
		});
		setButtonLayoutData(downButton);
		downButton.setEnabled(false);
		return bttArea;
	}

	/**
	 * Create the controls responsible to display/edit column widths.
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createWidthArea(final Composite parent) {

		final Label dummy = new Label(parent, SWT.NONE);
		dummy.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1));

		final Composite widthComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		widthComposite.setLayout(gridLayout);
		widthComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		widthLabel = new Label(widthComposite, SWT.NONE);
		// widthLabel.setText(MarkerMessages.MarkerPreferences_WidthOfShownColumn);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		widthLabel.setLayoutData(gridData);

		widthText = new Text(widthComposite, SWT.BORDER);
		widthText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(final VerifyEvent e) {
				if (e.character != 0 && e.keyCode != SWT.BS && e.keyCode != SWT.DEL && !Character.isDigit(e.character)) {
					e.doit = false;
				}
			}
		});

		gridData = new GridData();
		gridData.widthHint = convertWidthInCharsToPixels(5);
		widthText.setLayoutData(gridData);
		widthText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				updateWidth();
			}
		});
		// setWidthEnabled(false);
		return widthText;
	}

	/*
	 * private void setWidthEnabled(boolean enabled) { widthLabel.setEnabled(enabled); widthText.setEnabled(enabled); }
	 */

	/**
	 * Creates the table that lists out visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createVisibleTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label label = new Label(composite, SWT.NONE);
		label.setText("Enabled Columns");

		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI);

		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Enabled Columns");
		final Listener columnResize = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				column.setWidth(table.getClientArea().width);
			}
		};
		table.addListener(SWT.Resize, columnResize);

		visibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(visibleViewer);
		visibleViewer.setLabelProvider(doGetLabelProvider());
		visibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		visibleViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				handleVisibleSelection(event.getSelection());
			}
		});
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToNonVisibleButton(event);
			}
		});
		visibleViewer.setInput(getVisible());
		return table;
	}

	/**
	 * Creates the table that lists out non-visible columns in the viewer
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createInvisibleTable(final Composite parent) {

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);

		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label label = new Label(composite, SWT.NONE);
		label.setText("Disabled Columns");
		applyDialogFont(label);
		tableLabelSize = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = convertWidthInCharsToPixels(20);
		data.heightHint = table.getItemHeight() * 15;
		table.setLayoutData(data);

		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Disabled Columns");
		final Listener columnResize = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				column.setWidth(table.getClientArea().width);
			}
		};
		table.addListener(SWT.Resize, columnResize);

		nonVisibleViewer = new TableViewer(table);
		ColumnViewerToolTipSupport.enableFor(nonVisibleViewer);

		nonVisibleViewer.setLabelProvider(doGetLabelProvider());
		nonVisibleViewer.setContentProvider(ArrayContentProvider.getInstance());
		nonVisibleViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				handleNonVisibleSelection(event.getSelection());
			}
		});
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToVisibleButton(event);
			}
		});
		nonVisibleViewer.setInput(getNonVisible());

		nonVisibleViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				if (e1 instanceof ColumnBlock && e2 instanceof ColumnBlock) {
					final ColumnBlock b1 = (ColumnBlock) e1;
					final ColumnBlock b2 = (ColumnBlock) e2;
					return b1.blockName.compareTo(b2.blockName);
				}
				return super.compare(viewer, e1, e2);
			}
		});
		return table;
	}

	/**
	 * Creates buttons for moving columns from non-visible to visible and vice-versa
	 * 
	 * @param parent
	 * @return {@link Control}
	 */
	Control createMoveButtons(final Composite parent) {
		// create the manager and bind to a widget
		final LocalResourceManager resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		final ImageDescriptor leftImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_backward.gif");
		final Image leftImage = resManager.createImage(leftImageDescriptor);
		final ImageDescriptor rightImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_forward.gif");
		final Image rightImage = resManager.createImage(rightImageDescriptor);

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout compositeLayout = new GridLayout();
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

		final Composite bttArea = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bttArea.setLayout(layout);
		bttArea.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));

		toVisibleBtt = new Button(bttArea, SWT.PUSH);
		toVisibleBtt.setText("Show");
		toVisibleBtt.setImage(rightImage);
		setButtonLayoutData(toVisibleBtt);
		((GridData) toVisibleBtt.getLayoutData()).verticalIndent = tableLabelSize.y;
		toVisibleBtt.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToVisibleButton(event);
			}
		});
		toVisibleBtt.setEnabled(false);

		toNonVisibleBtt = new Button(bttArea, SWT.PUSH);
		toNonVisibleBtt.setText("Hide");
		toNonVisibleBtt.setImage(leftImage);
		setButtonLayoutData(toNonVisibleBtt);

		toNonVisibleBtt.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				handleToNonVisibleButton(event);
			}
		});
		toNonVisibleBtt.setEnabled(false);

		return bttArea;
	}

	/**
	 * Handles a selection change in the viewer that lists out the non-visible columns
	 * 
	 * @param selection
	 */
	void handleNonVisibleSelection(final ISelection selection) {
		final Object[] nvKeys = ((IStructuredSelection) selection).toArray();
		if (selection != null && selection.isEmpty() == false) {
			visibleViewer.setSelection(null);
		}
		toVisibleBtt.setEnabled(nvKeys.length > 0);
		if (visibleViewer.getControl().isFocusControl() && getVisible().size() <= 1) {
			/*
			 * handleStatusUdpate(IStatus.INFO, MarkerMessages.MarkerPreferences_AtLeastOneVisibleColumn);
			 */
		} else {
			// handleStatusUdpate(IStatus.INFO, getDefaultMessage());

		}
	}

	/**
	 * Handles a selection change in the viewer that lists out the visible columns. Takes care of various enablements.
	 * 
	 * @param selection
	 */
	void handleVisibleSelection(final ISelection selection) {
		final List<?> selVCols = ((IStructuredSelection) selection).toList();
		final List<Object> allVCols = getVisible();
		toNonVisibleBtt.setEnabled(selVCols.size() > 0 && allVCols.size() > selVCols.size());
		if (selection != null && selection.isEmpty() == false) {
			nonVisibleViewer.setSelection(null);
		}

		final IColumnInfoProvider infoProvider = doGetColumnInfoProvider();
		boolean moveDown = !selVCols.isEmpty(), moveUp = !selVCols.isEmpty();
		final Iterator<?> iterator = selVCols.iterator();
		while (iterator.hasNext()) {
			final Object columnObj = iterator.next();
			if (!infoProvider.isColumnMovable(columnObj)) {
				moveUp = false;
				moveDown = false;
				break;
			}
			final int i = allVCols.indexOf(columnObj);
			if (i == 0) {
				moveUp = false;
				if (!moveDown) {
					break;
				}
			}
			if (i == (allVCols.size() - 1)) {
				moveDown = false;
				if (!moveUp) {
					break;
				}
			}
		}
		upButton.setEnabled(moveUp);
		downButton.setEnabled(moveDown);

		// final boolean edit = selVCols.size() == 1 ? infoProvider.isColumnResizable(selVCols.get(0)) : false;
		// setWidthEnabled(edit);
		/*
		 * if (edit) { int width = infoProvider.getColumnWidth(selVCols.get(0)); widthText.setText(Integer.toString(width)); } else { widthText.setText(""); //$NON-NLS-1$ }
		 */
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e
	 *            event from the button click
	 */
	void handleDownButton(final Event e) {
		final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
		final Object[] selVCols = selection.toArray();
		final List<Object> allVCols = getVisible();
		final IColumnUpdater updater = doGetColumnUpdater();
		for (int i = selVCols.length - 1; i >= 0; i--) {
			final Object colObj = selVCols[i];
			final int visibleIndex = allVCols.indexOf(colObj);

			updater.swapColumnPositions(colObj, allVCols.get(visibleIndex + 1));
			// updater.setColumnIndex(colObj, visibleIndex + 1);
			allVCols.remove(visibleIndex);
			allVCols.add(visibleIndex + 1, colObj);
		}
		visibleViewer.refresh();
		handleVisibleSelection(selection);
	}

	/**
	 * Applies to visible columns, and handles the changes in the order of columns
	 * 
	 * @param e
	 *            event from the button click
	 */
	void handleUpButton(final Event e) {
		final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
		final Object[] selVCols = selection.toArray();
		final List<Object> allVCols = getVisible();
		final IColumnUpdater updater = doGetColumnUpdater();
		for (int i = 0; i < selVCols.length; i++) {
			final Object colObj = selVCols[i];
			final int visibleIndex = allVCols.indexOf(colObj);
			updater.swapColumnPositions(colObj, allVCols.get(visibleIndex - 1));
			// updater.setColumnIndex(colObj, index - 1);
			allVCols.remove(visibleIndex);
			allVCols.add(visibleIndex - 1, colObj);
		}
		visibleViewer.refresh();
		handleVisibleSelection(selection);
	}

	/**
	 * Moves selected columns from non-visible to visible state
	 * 
	 * @param e
	 *            event from the button click
	 */
	void handleToVisibleButton(final Event e) {
		final IStructuredSelection selection = (IStructuredSelection) nonVisibleViewer.getSelection();
		final List<?> selVCols = selection.toList();
		final List<Object> nonVisible = getNonVisible();
		nonVisible.removeAll(selVCols);

		final List<Object> list = getVisible();
		list.addAll(selVCols);
		Collections.sort(list, comparator);

		updateVisibility(selVCols, true);
		// updateIndices(getVisible());
		// updateIndices(getNonVisible());

		visibleViewer.refresh();
		visibleViewer.setSelection(selection);
		nonVisibleViewer.refresh();
		handleVisibleSelection(selection);
		handleNonVisibleSelection(nonVisibleViewer.getSelection());

	}

	/**
	 * Moves selected columns from visible to non-visible state
	 * 
	 * @param e
	 *            event from the button click
	 */
	protected void handleToNonVisibleButton(final Event e) {
		/*
		 * if (visibleViewer.getControl().isFocusControl() && getVisible().size() <= 1) { handleStatusUdpate(IStatus.INFO, MarkerMessages.MarkerPreferences_AtLeastOneVisibleColumn); return; }
		 */
		final IStructuredSelection selection = (IStructuredSelection) visibleViewer.getSelection();
		final List<?> selVCols = selection.toList();
		getVisible().removeAll(selVCols);
		getNonVisible().addAll(selVCols);

		Collections.sort(getNonVisible(), comparator);

		updateVisibility(selVCols, false);
		// updateIndices(getVisible());
		// updateIndices(getNonVisible());

		nonVisibleViewer.refresh();
		nonVisibleViewer.setSelection(selection);
		visibleViewer.refresh();
		handleVisibleSelection(visibleViewer.getSelection());
		handleNonVisibleSelection(nonVisibleViewer.getSelection());
	}

	/*
	 * void updateIndices(List list) { ListIterator iterator = list.listIterator(); IColumnUpdater updater = doGetColumnUpdater(); while (iterator.hasNext()) {
	 * //updater.setColumnIndex(iterator.next(), iterator.previousIndex()); } }
	 */

	void updateVisibility(final List<?> list, final boolean visibility) {
		final IColumnUpdater updater = doGetColumnUpdater();
		final Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			updater.setColumnVisible(iterator.next(), visibility);
		}
	}

	protected void performDefaults() {
		refreshViewers();
		// super.performDefaults();
	}

	/**
	 * Updates the UI based on values of the variable
	 */
	void refreshViewers() {
		if (nonVisibleViewer != null) {
			nonVisibleViewer.refresh();
		}
		if (visibleViewer != null) {
			visibleViewer.refresh();
		}
	}

	/**
	 * @return List of visible columns
	 */
	public List<Object> getVisible() {
		if (visible == null) {
			visible = new ArrayList<Object>();
		}

		Collections.sort(visible, comparator);
		return visible;
	}

	/**
	 * @return List of non-visible columns
	 */
	public List<Object> getNonVisible() {
		if (nonVisible == null) {
			nonVisible = new ArrayList<>();
		}
		Collections.sort(nonVisible, comparator);
		return nonVisible;
	}

	/**
	 * An adapter class to {@link ITableLabelProvider}
	 * 
	 */
	public class TableLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		// @Override
		// public Image getColumnImage(final Object element, final int columnIndex) {
		// return null;
		// }
		//
		// @Override
		// public String getColumnText(final Object element, final int columnIndex) {
		// return getColumnText(element, columnIndex);
		// }
		//
		// @Override
		// public String getToolTipText(Object element) {
		// return super.getToolTipText(element);
		// }
		//
		// @Override
		// public void update(ViewerCell cell) {
		// // TODO Auto-generated method stub
		//
		// }
	}

	/**
	 * Internal helper to @see {@link ColumnConfigurationDialog#getLabelProvider()}
	 */
	IBaseLabelProvider doGetLabelProvider() {
		return getLabelProvider();
	}

	/**
	 * The tables-columns need to be displayed appropriately. The supplied column objects are adapted to text and image as dictacted by this {@link ITableLabelProvider}
	 */
	protected abstract IBaseLabelProvider getLabelProvider();

	/**
	 * Internal helper to @see {@link ColumnConfigurationDialog#getColumnInfoProvider()}
	 */
	IColumnInfoProvider doGetColumnInfoProvider() {
		return getColumnInfoProvider();
	}

	/**
	 * To configure the columns we need further information. The supplied column objects are adapted for its properties via {@link IColumnInfoProvider}
	 */
	protected abstract IColumnInfoProvider getColumnInfoProvider();

	/**
	 * Internal helper to @see {@link ColumnConfigurationDialog#getColumnUpdater()}
	 */
	IColumnUpdater doGetColumnUpdater() {
		return getColumnUpdater();
	}

	/**
	 * To configure properties/order of the columns is achieved via {@link IColumnUpdater}
	 */
	protected abstract IColumnUpdater getColumnUpdater();

	/**
	 * 
	 */
	private void updateWidth() {
		try {
			final int width = Integer.parseInt(widthText.getText());
			final Object data = ((IStructuredSelection) visibleViewer.getSelection()).getFirstElement();
			if (data != null) {
				final IColumnUpdater updater = getColumnUpdater();
				updater.setColumnWidth(data, width);
			}
		} catch (final NumberFormatException ex) {
			// ignore
		}
	}

	/**
	 * Update various aspects of a columns from a viewer such {@link TableViewer}
	 */
	public interface IColumnInfoProvider {

		/**
		 * Get corresponding index for the column
		 * 
		 * @param columnObj
		 */
		public int getColumnIndex(Object columnObj);

		/**
		 * Get the width of the column
		 * 
		 * @param columnObj
		 */
		public int getColumnWidth(Object columnObj);

		/**
		 * Returns true if the column represented by parameters is showing in the viewer
		 * 
		 * @param columnObj
		 */
		public boolean isColumnVisible(Object columnObj);

		/**
		 * Returns true if the column represented by parameters is configured as movable
		 * 
		 * @param columnObj
		 */
		public boolean isColumnMovable(Object columnObj);

		/**
		 * Returns true if the column represented by parameters is configured as resizable
		 * 
		 * @param columnObj
		 */
		public boolean isColumnResizable(Object columnObj);

	}

	public static abstract class ColumnInfoAdapter implements IColumnInfoProvider {

		@Override
		public boolean isColumnMovable(final Object columnObj) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isColumnResizable(final Object columnObj) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getColumnWidth(final Object columnObj) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	/**
	 * Update various aspects of a columns from a viewer such {@link TableViewer}
	 */
	public interface IColumnUpdater {

		/**
		 * Set the column represented by parameters as visible
		 * 
		 * @param columnObj
		 * @param visible
		 */
		public void setColumnVisible(Object columnObj, boolean visible);

		/**
		 * Dummy method - more a result of symmetry
		 * 
		 * @param columnObj
		 * @param movable
		 */
		public void setColumnMovable(Object columnObj, boolean movable);

		/**
		 * Call back to notify change in the index of the column represented by columnObj
		 * 
		 * @param columnObj
		 * @param index
		 */
		public void setColumnIndex(Object columnObj, int index);

		/**
		 * Call back to notify change in the index of the column represented by columnObj
		 * 
		 * @param columnObj
		 * @param index
		 */
		public void swapColumnPositions(Object columnObj1, Object columnObj2);

		/**
		 * Dummy method - more a result of symmetry
		 * 
		 * @param columnObj
		 * @param resizable
		 */
		public void setColumnResizable(Object columnObj, boolean resizable);

		/**
		 * Call back to notify change in the width of the column represented by columnObj
		 * 
		 * @param columnObj
		 * @param newWidth
		 */
		public void setColumnWidth(Object columnObj, int newWidth);

		Object[] resetColumnStates();

	}

	public static abstract class ColumnUpdaterAdapter implements IColumnUpdater {

		@Override
		public void setColumnMovable(final Object columnObj, final boolean movable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setColumnIndex(final Object columnObj, final int index) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setColumnResizable(final Object columnObj, final boolean resizable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setColumnWidth(final Object columnObj, final int newWidth) {
			// TODO Auto-generated method stub

		}

	}

	// //////////////////////////////////////////////////////////////////////////////////
	/**
	 * Ignore the class below as it is simply meant to test the above. I intend to retain this for a while.
	 */
	public static class TestData {

		final Object key;

		final int keyIndex;

		int newIndex, width;

		public boolean visibility, movable, resizable;

		public TestData(final Object key, final int currIndex, final boolean moveable) {
			this.key = key;
			this.keyIndex = currIndex;
			this.movable = moveable;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + keyIndex;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TestData)) {
				return false;
			}
			final TestData other = (TestData) obj;
			if (key == null) {
				if (other.key != null) {
					return false;
				}
			} else if (!key.equals(other.key)) {
				return false;
			}
			if (keyIndex != other.keyIndex) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return key.toString();
		}

		public static ColumnConfigurationDialog getDialog(final Shell shell, final TestData[] colums) {
			final ColumnConfigurationDialog dialog = new ColumnConfigurationDialog(shell) {

				@Override
				protected IColumnInfoProvider getColumnInfoProvider() {
					return getInfoProvider(colums);
				}

				@Override
				protected IBaseLabelProvider getLabelProvider() {
					return new TableLabelProvider();
				}

				@Override
				protected IColumnUpdater getColumnUpdater() {
					return getUpdater(colums);
				}
			};
			dialog.setColumnsObjs(colums);
			return dialog;
		}

		private static IColumnUpdater getUpdater(final TestData[] data) {
			return new IColumnUpdater() {

				@Override
				public void setColumnWidth(final Object columnObj, final int newWidth) {
					((TestData) columnObj).width = newWidth;
				}

				@Override
				public void setColumnVisible(final Object columnObj, final boolean visible) {
					((TestData) columnObj).visibility = visible;
				}

				@Override
				public void setColumnResizable(final Object columnObj, final boolean resizable) {

				}

				@Override
				public void setColumnMovable(final Object columnObj, final boolean movable) {
					((TestData) columnObj).movable = movable;

				}

				@Override
				public void setColumnIndex(final Object columnObj, final int index) {
					((TestData) columnObj).newIndex = index;
				}

				@Override
				public void swapColumnPositions(final Object columnObj1, final Object columnObj2) {
					// TODO Auto-generated method stub

				}

				@Override
				public Object[] resetColumnStates() {
					return new Object[0];
				}
			};
		}

		private static IColumnInfoProvider getInfoProvider(final TestData[] colData) {
			return new IColumnInfoProvider() {

				@Override
				public boolean isColumnVisible(final Object columnObj) {
					return ((TestData) columnObj).visibility;
				}

				@Override
				public boolean isColumnResizable(final Object columnObj) {
					return ((TestData) columnObj).resizable;
				}

				@Override
				public boolean isColumnMovable(final Object columnObj) {
					return ((TestData) columnObj).movable;
				}

				@Override
				public int getColumnWidth(final Object columnObj) {
					return ((TestData) columnObj).width;
				}

				@Override
				public int getColumnIndex(final Object columnObj) {
					return ((TestData) columnObj).newIndex;
				}
			};
		}

		private static TestData[] genData(final int count) {
			final String[] cols = new String[count];
			for (int i = 0; i < cols.length; i++) {
				cols[i] = new String("Column-" + (i + 1)); //$NON-NLS-1$
			}
			final Random random = new Random();

			final boolean[] visibility = new boolean[cols.length];
			Arrays.fill(visibility, true);
			int ranInt = random.nextInt() % cols.length;
			for (int i = 0; i < ranInt; i++) {
				visibility[random.nextInt(ranInt)] = false;
			}

			final boolean[] resizable = new boolean[cols.length];
			Arrays.fill(resizable, true);
			ranInt = random.nextInt() % cols.length;
			for (int i = 0; i < ranInt; i++) {
				resizable[random.nextInt(ranInt)] = false;
			}

			final boolean[] movable = new boolean[cols.length];
			Arrays.fill(movable, true);
			ranInt = random.nextInt() % cols.length;
			for (int i = 0; i < ranInt; i++) {
				movable[random.nextInt(ranInt)] = false;
			}

			final int[] widths = new int[cols.length];
			Arrays.fill(widths, 100);
			return TestData.generateColumnsData(cols, visibility, resizable, movable, widths);
		}

		public static TestData[] generateColumnsData(final Object[] keys, final boolean[] visibility, final boolean[] resizable, final boolean[] movable, final int[] widths) {
			final TestData[] colData = new TestData[keys.length];
			int m = 0, n = 0;
			for (int i = 0; i < colData.length; i++) {
				final TestData data = new TestData(keys[i], i, movable[i]);
				data.visibility = visibility[i];
				data.resizable = resizable[i];
				data.movable = movable[i];
				data.width = widths[i];
				if (data.visibility) {
					data.newIndex = m++;
				} else {
					data.newIndex = n++;
				}
				colData[i] = data;
			}
			return colData;
		}

		/**
		 * Demo
		 * 
		 * @param args
		 */
		public static void main(final String[] args) {
			final Display display = new Display();
			final Shell shell = new Shell(display);
			shell.setLayout(new FillLayout());
			final ColumnConfigurationDialog dialog = getDialog(shell, genData(100));
			dialog.open();
			shell.dispose();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();

		}

	}

	public void addCheckBoxInfo(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
		checkboxInfo.add(new CheckboxInfoManager(title, options, store));
	}

	public static class OptionInfo {
		public final String id;
		public final String label;
		public Button button = null;

		public OptionInfo(String id, String label) {
			this.id = id;
			this.label = label;
		}

		public static Collection<? extends String> getIds(OptionInfo[] options) {
			List<String> result = new LinkedList<>();
			for (OptionInfo option : options) {
				result.add(option.id);
			}
			return result;
		}
	}

	static class CheckboxInfoManager {

		final String title;
		final OptionInfo[] options;
		final Supplier<Set<String>> store;
		final Button button = null;

		public CheckboxInfoManager(final String title, final OptionInfo[] options, final Supplier<Set<String>> store) {
			this.title = title;
			this.options = options;
			this.store = store;
		}

	}

	protected void refreshCheckboxes() {
		for (final CheckboxInfoManager manager : checkboxInfo) {
			final Set<String> store = manager.store.get();
			for (final OptionInfo option : manager.options) {
				final Button button = option.button;
				if (button != null && !button.isDisposed()) {
					button.setSelection(store.contains(option.id));
				}
			}
		}
	}
}
