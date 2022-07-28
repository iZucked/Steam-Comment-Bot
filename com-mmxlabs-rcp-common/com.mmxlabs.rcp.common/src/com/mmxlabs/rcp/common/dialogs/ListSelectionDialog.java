/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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

/**
 * A list picking dialog which is a bit more flexible than the RCP-provided
 * default
 * 
 * Additional features include having extra columns, tree support, and having a
 * quick filter of the list.
 * 
 * TODO selection management, filtering, sensible size on opening, collapse
 * unneeded groups.
 * 
 * @author hinton
 * 
 */
public class ListSelectionDialog extends Dialog {
	private String title = "Select elements";
	private String message = "Choose some elements";
	private TreeViewer viewer;
	private final ILabelProvider labelProvider;
	private final GroupedElementProvider contentProvider;
	private final Object input;
	private final List<Pair<String, CellLabelProvider>> columns = new LinkedList<>();
	private final HashSet<Object> allSelectedElements = new HashSet<>();
	private final HashSet<Object> filteredElements = new HashSet<>();
	private Object[] initialSelection = new Object[0];
	private Object[] dialogResult;
	protected boolean multiSelect = true;

	public ColumnLabelProvider wrapColumnLabelProvider(final ColumnLabelProvider clp, final boolean isFirstColumn) {
		return new WrappedColumnLabelProvider(clp, isFirstColumn);
	}

	public ListSelectionDialog(final Shell parentShell, final Object input, final IStructuredContentProvider contentProvider, final ILabelProvider labelProvider) {
		super(parentShell);
		this.input = input;

		final GroupedElementProvider gep = new GroupedElementProvider(contentProvider);
		this.contentProvider = gep;
		this.labelProvider = gep.wrapLabelProvider(labelProvider);
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

		final List<String> filters = new ArrayList<>();

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

				if (viewer instanceof CheckboxTreeViewer treeViewer) {

					// remove from allSelectedElements everything which is still showing
					// in the viewer
					allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray())));
					// add to allSelectedElements everything showing and selected in the viewer
					allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(treeViewer.getCheckedElements())));
					// re-filter the viewer
					treeViewer.refresh();
					// and select only those elements which are in allSelectedElements
					treeViewer.setCheckedElements(contentProvider.getViewerElements(allSelectedElements.toArray()));
				}
			}
		});

		int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL;
		if (multiSelect) {
			style |= SWT.MULTI | SWT.CHECK;
			viewer = new CheckboxTreeViewer(inner, style);
		} else {
			viewer = new TreeViewer(inner, style);
		}
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				filteredElements.remove(element);
				if (filters.isEmpty()) {
					return true;
				}
				if (contentProvider.hasChildren(element)) {
					return true;
				}
				final String t = getFilterableText(element);

				for (final String f : filters) {
					if (t.contains(f)) {
						return true;
					}
				}
				filteredElements.add(element);
				return false;
			}
		});

		if (!columns.isEmpty()) {
			for (final Pair<String, CellLabelProvider> column : columns) {
				final TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
				final ColumnLabelProvider provider = (ColumnLabelProvider) column.getSecond();

				final ReversibleViewerComparator sorter = new ReversibleViewerComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object arg0, final Object arg1) {
						// FIXME: SG 29/07/2016 - log file reported NPE here!
						return provider.getText(arg0).compareTo(provider.getText(arg1));
					}
				});

				tvc.getColumn().setText(column.getFirst());
				tvc.setLabelProvider(column.getSecond());
				tvc.getColumn().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						viewer.setComparator(sorter.select());
						viewer.refresh();
					}
				});
			}
			viewer.getTree().setHeaderVisible(true);
			viewer.getTree().setLinesVisible(true);
		}

		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setInput(input);

		if (multiSelect && viewer instanceof CheckboxTreeViewer treeViewer) {
			treeViewer.setCheckedElements(contentProvider.getViewerElements(initialSelection));
			treeViewer.addCheckStateListener(new ICheckStateListener() {
				@Override
				public void checkStateChanged(final CheckStateChangedEvent event) {
					// propagate check marks
				}
			});
		} else {
			viewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					okPressed();
				}
			});
		}
		viewer.expandAll();
		for (final TreeColumn column : viewer.getTree().getColumns()) {
			column.pack();
		}

		return box;
	}

	@Override
	protected void okPressed() {
		if (multiSelect && viewer instanceof CheckboxTreeViewer treeViewer) {

			// Remove everything shown in the viewer from allSelectedElements
			allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray())));
			// Add everything selected in the viewer from allSelectedElements
			allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(treeViewer.getCheckedElements())));

			// Return the contents of allSelectedElements, sorted alphabetically
			dialogResult = allSelectedElements.toArray();
		} else {
			dialogResult = contentProvider.getInputElements(viewer.getStructuredSelection().toArray());
		}

		final Comparator<Object> c = new Comparator<Object>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Object arg0, final Object arg1) {
				// No compare method for Pair objects (consider adding to base class?)
				final Pair<String, ?> p0 = (Pair<String, ?>) arg0;
				final Pair<String, ?> p1 = (Pair<String, ?>) arg1;

				return p0.getFirst().compareTo(p1.getFirst());
			}
		};
		Arrays.sort(dialogResult, c);
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
	 * Add a grouping to any hierarchy displayed; the given label provider will be
	 * used to get a string for each input object; all inputs with the same string
	 * will be grouped under a "virtual" element with that name
	 * 
	 * @param labelProvider
	 */
	public void groupBy(final ColumnLabelProvider labelProvider) {
		contentProvider.groupers.add(labelProvider);
	}

	/**
	 * Add a column to the table.
	 * 
	 * @param title
	 * @param columnLabelProvider
	 * @return
	 */
	public CellLabelProvider addColumn(final String title, final ColumnLabelProvider columnLabelProvider) {
		final CellLabelProvider result = wrapColumnLabelProvider(columnLabelProvider, columns.isEmpty());
		this.columns.add(new Pair<>(title, result));
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
	 * Returns the text which the filter box searches through per display row
	 * element. Search is done on a trimmed string, so concatenating fields with
	 * whitespace will allow multiple fields to be searched.
	 * 
	 * @param element
	 * @return
	 */
	protected String getFilterableText(final Object element) {
		return labelProvider.getText(element).toLowerCase();
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		if (multiSelect) {
			createButton(parent, IDialogConstants.SELECT_ALL_ID, "Select All", false);
			createButton(parent, IDialogConstants.DESELECT_ALL_ID, "Select None", false);
		}
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
		if (viewer instanceof CheckboxTreeViewer treeViewer) {
			final TreeItem[] items = treeViewer.getTree().getItems();
			for (int i = 0; i < items.length; i++) {
				final Object element = items[i].getData();
				treeViewer.setSubtreeChecked(element, false);
			}
		}
	}

	private void selectAll() {
		if (viewer instanceof CheckboxTreeViewer treeViewer) {
			final TreeItem[] items = treeViewer.getTree().getItems();
			for (int i = 0; i < items.length; i++) {
				final Object element = items[i].getData();
				treeViewer.setSubtreeChecked(element, true);
			}
		}
	}

	public class WrappedColumnLabelProvider extends ColumnLabelProvider {
		private final ColumnLabelProvider clp;
		private final boolean isFirstColumn;

		public WrappedColumnLabelProvider(final ColumnLabelProvider clp, final boolean isFirstColumn) {
			this.clp = clp;
			this.isFirstColumn = isFirstColumn;
		}

		public ColumnLabelProvider getWrapped() {
			return clp;
		}

		public Object unwrap(final Object element) {
			if (element instanceof GroupedElementProvider.E) {
				return ((GroupedElementProvider.E) element).value;
			} else if (element instanceof GroupedElementProvider.G) {
				return null;
			}
			return element;
		}

		@Override
		public Font getFont(final Object element) {
			final Object a = unwrap(element);
			return a == null ? null : clp.getFont(a);
		}

		@Override
		public Color getBackground(final Object element) {
			final Object a = unwrap(element);
			return a == null ? null : clp.getBackground(a);
		}

		@Override
		public Color getForeground(final Object element) {
			final Object a = unwrap(element);
			return a == null ? null : clp.getBackground(a);
		}

		@Override
		public Image getImage(final Object element) {
			final Object a = unwrap(element);
			return a == null ? super.getImage(element) : clp.getImage(a);
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof GroupedElementProvider.G) {
				if (isFirstColumn) {
					return ((GroupedElementProvider.G) element).name;
				} else {
					return "";
				}
			} else {
				return clp.getText(unwrap(element));
			}
		}

		@Override
		public Image getToolTipImage(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipImage(object) : clp.getToolTipImage(a);
		}

		@Override
		public String getToolTipText(final Object element) {
			final Object a = unwrap(element);
			return a == null ? super.getToolTipText(element) : clp.getToolTipText(a);
		}

		@Override
		public Color getToolTipBackgroundColor(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipBackgroundColor(object) : clp.getToolTipBackgroundColor(a);
		}

		@Override
		public Color getToolTipForegroundColor(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipForegroundColor(object) : clp.getToolTipForegroundColor(a);
		}

		@Override
		public Font getToolTipFont(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipFont(object) : clp.getToolTipFont(a);
		}

		@Override
		public Point getToolTipShift(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipShift(object) : clp.getToolTipShift(a);

		}

		@Override
		public boolean useNativeToolTip(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.useNativeToolTip(object) : clp.useNativeToolTip(a);
		}

		@Override
		public int getToolTipTimeDisplayed(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipTimeDisplayed(object) : clp.getToolTipTimeDisplayed(a);
		}

		@Override
		public int getToolTipDisplayDelayTime(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipDisplayDelayTime(object) : clp.getToolTipDisplayDelayTime(a);
		}

		@Override
		public int getToolTipStyle(final Object object) {
			final Object a = unwrap(object);
			return a == null ? super.getToolTipStyle(object) : clp.getToolTipStyle(a);
		}

		@Override
		public void addListener(final ILabelProviderListener listener) {
			clp.addListener(listener);
		}

		@Override
		public void dispose() {
			clp.dispose();
		}

		@Override
		public boolean isLabelProperty(final Object element, final String property) {
			final Object a = unwrap(element);
			return a == null ? false : clp.isLabelProperty(a, property);
		}

		@Override
		public void removeListener(final ILabelProviderListener listener) {
			clp.removeListener(listener);
		}

	}

	public class ReversibleViewerComparator extends ViewerComparator {
		int direction = 0;

		final ViewerComparator vc;

		public ReversibleViewerComparator(final ViewerComparator vc) {
			this.vc = vc;
		}

		public ViewerComparator select() {
			if (direction == 1) {
				direction = -1;
			} else {
				direction = 1;
			}
			return this;
		}

		@Override
		public int compare(final Viewer viewer, final Object a, final Object b) {
			return vc.compare(viewer, a, b) * direction;
		}
	}

}
