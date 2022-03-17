/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;

/**
 * Allows picking columns for the list of visible/non-visible rows
 * @author Farukh Mukhamedov
 *
 */

public class ColumnSelectionDialog extends Dialog {
	
	private String title = "Select elements";
	private String message = "Choose some elements";
	private CheckboxTreeViewer viewer;
	private final ILabelProvider labelProvider;
	private MyContentProvider contentProvider;
	private final Object input;
	private final List<Pair<String, CellLabelProvider>> columns = new LinkedList<Pair<String, CellLabelProvider>>();
	private final HashSet<Object> allSelectedElements = new HashSet<Object>();
	private final HashSet<Object> filteredElements = new HashSet<Object>();
	private Object[] initialSelection = new Object[0];
	private Object[] dialogResult;
	
	List<CellLabelProvider> searchedColumns = new ArrayList<>();
	
	private static class MyContentProvider extends ArrayContentProvider implements ITreeContentProvider{

		@Override
		public Object[] getChildren(Object parentElement) {
			Object[] children = new Object[0];
			return children;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return false;
		}
		
	}
	
	private List<ColumnBlock> getVisibleColumns(List<ColumnBlock> items){
		List<ColumnBlock> result = new ArrayList<>();
		for (final ColumnBlock cb : items) {
			if (cb.getVisible()) {
				result.add(cb);
			}
		}
		return result;
	}
	
	public ColumnSelectionDialog(final Shell parentShell,final Object input) {
		this(parentShell, input, new MyContentProvider(), new LabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((ColumnBlock) element).configurationName;
			}
		});
	}
	
	public ColumnSelectionDialog(final Shell parentShell, final Object input, final MyContentProvider contentProvider, final ILabelProvider labelProvider) {
		super(parentShell);
		this.contentProvider = contentProvider;
		this.labelProvider = labelProvider;
		this.input = input;
	}
	
	public List<ColumnBlock> pick(final Control cellEditorWindow, final List<ColumnBlock> options, final EReference feature) {
		if (options.size() > 0 && options.get(0) == null)
			options.remove(0);

		setTitle("Value Selection");

		setInitialSelections(getVisibleColumns(options).toArray());

		searchedColumns.clear();

		final ColumnLabelProvider nameLabelProvider = new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		};

		final CellLabelProvider nameColumn = addColumn("Name", nameLabelProvider);
		searchedColumns.add(nameColumn);

		if (open() == Window.OK) {
			final Object[] result = getResult();
	
			if (result == null) {
					return null;
			}
				
			final List<ColumnBlock> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add((ColumnBlock) o);
			}
			return resultList;
		}
		return null;
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite box = (Composite) super.createDialogArea(parent);

		final Composite inner = new Composite(box, SWT.NONE);
		inner.setLayout(new GridLayout(2, false));
		// Create view within inner.

		final Label lr = new Label(inner, SWT.NONE);
		lr.setText("Filter:");
		lr.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		final Text tr = new Text(inner, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);

		final List<String> filters = new ArrayList<String>();

		tr.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		tr.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				filters.clear();
				final String match = tr.getText().trim();
				if (!match.isEmpty()) {
					final String[] parts = match.split(",");
					for (final String p : parts) {
						final String p2 = p.toLowerCase().trim();
						if (p2.isEmpty())
							continue;
						filters.add(p2);
					}
				}

				// remove from allSelectedElements everything which is still showing
				// in the viewer
				allSelectedElements.retainAll(Arrays.asList(contentProvider.getElements(filteredElements.toArray())));
				// add to allSelectedElements everything showing and selected in the viewer
				allSelectedElements.addAll(Arrays.asList(contentProvider.getElements(viewer.getCheckedElements())));
				// re-filter the viewer
				viewer.refresh();
				// and select only those elements which are in allSelectedElements
				viewer.setCheckedElements(contentProvider.getElements(allSelectedElements.toArray()));
			}
		});
		viewer = new CheckboxTreeViewer(inner, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		
		viewer.setContentProvider(contentProvider);
		
		viewer.setLabelProvider(labelProvider);
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				filteredElements.remove(element);
				if (filters.isEmpty())
					return true;
				final String t = getFilterableText(element);

				for (final String f : filters) {
					if (t.contains(f))
						return true;
				}
				filteredElements.add(element);
				return false;
			}
		});

		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setInput(input);
		viewer.setCheckedElements(contentProvider.getElements(initialSelection));
		viewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				// propagate check marks
			}
		});
				
		viewer.expandAll();
		for (final TreeColumn column : viewer.getTree().getColumns()) {
			column.pack();
		}

		return box;
	}

	@Override
	protected void okPressed() {
		// Remove everything shown in the viewer from allSelectedElements
		allSelectedElements.retainAll(Arrays.asList(contentProvider.getElements(filteredElements.toArray())));
		// Add everything selected in the viewer from allSelectedElements
		allSelectedElements.addAll(Arrays.asList(contentProvider.getElements(viewer.getCheckedElements())));

		// Return the contents of allSelectedElements, sorted alphabetically
		dialogResult = allSelectedElements.toArray();

		super.okPressed();
	}

	@Override
	public void create() {
		super.create();
		getShell().setText(title);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(480, 640);
	}

	/**
	 * Add a column to the table.
	 * 
	 * @param title
	 * @param columnLabelProvider
	 * @return 
	 */
	public CellLabelProvider addColumn(final String title, final ColumnLabelProvider columnLabelProvider) {
		final CellLabelProvider result = columnLabelProvider;
		this.columns.add(new Pair<String, CellLabelProvider>(title,result));
		return result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setInitialSelections(final Object[] array) {
		initialSelection = array;
	}

	public Object[] getResult() {
		return dialogResult;
	}
	
	/**
	 * Returns the text which the filter box searches through per display row element. 
	 * Search is done on a trimmed string, so concatenating fields with whitespace will allow multiple
	 * fields to be searched. 
	 * 
	 * @param element
	 * @return
	 */
	protected String getFilterableText(final Object element) {
		return labelProvider.getText(element).toLowerCase();
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.SELECT_ALL_ID, "Select All", false);
		createButton(parent, IDialogConstants.DESELECT_ALL_ID, "Select None", false);
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		if (IDialogConstants.SELECT_ALL_ID == buttonId) {
			selectAll();
		} else if (IDialogConstants.DESELECT_ALL_ID == buttonId) {
			deselectAll();
		} else {
			super.buttonPressed(buttonId);
		}
	}

	private void deselectAll() {
		final TreeItem[] items = viewer.getTree().getItems();
		for (int i = 0; i < items.length; i++) {
			final Object element = items[i].getData();
			viewer.setSubtreeChecked(element, false);
		}
	}

	private void selectAll() {
		final TreeItem[] items = viewer.getTree().getItems();
		for (int i = 0; i < items.length; i++) {
			final Object element = items[i].getData();
			viewer.setSubtreeChecked(element, true);
		}
	}

}
