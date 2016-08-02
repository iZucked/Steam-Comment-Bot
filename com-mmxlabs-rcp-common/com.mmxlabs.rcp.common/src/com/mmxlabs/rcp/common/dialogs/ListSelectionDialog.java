/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
 * A list picking dialog which is a bit more flexible than the RCP-provided default
 * 
 * Additional features include having extra columns, tree support, and having a quick filter of the list.
 * 
 * TODO selection management, filtering, sensible size on opening, collapse unneeded groups.
 * 
 * @author hinton
 * 
 */
public class ListSelectionDialog extends Dialog {
	private String title = "Select elements";
	private String message = "Choose some elements";
	private CheckboxTreeViewer viewer;
	private final ILabelProvider labelProvider;
	private final GroupedElementProvider contentProvider;
	private final Object input;
	private final List<Pair<String, CellLabelProvider>> columns = new LinkedList<Pair<String, CellLabelProvider>>();
	private final HashSet<Object> allSelectedElements = new HashSet<Object>();
	private final HashSet<Object> filteredElements = new HashSet<Object>();
	private Object[] initialSelection = new Object[0];
	private Object[] dialogResult;

	private class GroupedElementProvider implements ITreeContentProvider {
		private final IStructuredContentProvider delegate;
		private final List<ILabelProvider> groupers = new LinkedList<ILabelProvider>();

		private final Map<Object, E> originalToViewer = new HashMap<Object, E>();
		private final Map<E, Object> viewerToOriginal = new HashMap<E, Object>();

		private G lastTop;
		private Object lastInput;

		// Row element??
		private class E {
			public final Object value;
			public final G parent;

			public E(final G parent, final Object value) {
				this.parent = parent;
				this.value = value;
			}
		}

		// Tree node grouping?
		private class G {
			public final String name;
			public final Object[] children;
			public Object parent;

			public G(final Object parent, final String name, final List<Object> inputs, final List<ILabelProvider> stack) {
				this.name = name;
				this.parent = parent;
				if (stack.isEmpty()) {
					children = new Object[inputs.size()];
					int i = 0;
					for (final Object o : inputs) {
						final E e = new E(this, o);
						children[i++] = e;
						originalToViewer.put(o, e);
						viewerToOriginal.put(e, o);
					}
				} else {
					final ILabelProvider grouper = stack.iterator().next();
					final Map<String, List<Object>> subGroups = collect(inputs, grouper);
					final G[] children_ = new G[subGroups.size()];
					int i = 0;

					final List<ILabelProvider> subStack = stack.size() == 1 ? new LinkedList<ILabelProvider>() : stack.subList(1, stack.size() - 1);
					for (final Map.Entry<String, List<Object>> group : subGroups.entrySet()) {
						children_[i++] = new G(this, group.getKey(), group.getValue(), subStack);
					}
					if (children_.length == 1) {
						children = children_[0].children;
					} else {
						children = children_;
					}
				}
			}

			private Map<String, List<Object>> collect(final List<Object> inputs, final ILabelProvider grouper) {
				final TreeMap<String, List<Object>> out = new TreeMap<String, List<Object>>();

				for (final Object o : inputs) {
					final String s = grouper.getText(o);
					List<Object> group = out.get(s);
					if (group == null) {
						group = new LinkedList<Object>();
						out.put(s, group);
					}
					group.add(o);
				}

				return out;
			}
		}

		/**
		 * @param contentProvider
		 */
		public GroupedElementProvider(final IStructuredContentProvider contentProvider) {
			this.delegate = contentProvider;
		}

		@Override
		public void dispose() {
			delegate.dispose();
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			lastTop = null;
			lastInput = null;
			originalToViewer.clear();
			viewerToOriginal.clear();
			delegate.inputChanged(viewer, oldInput, newInput);
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement == lastInput) {
				return lastTop.children;
			} else {
				final G top = new G(inputElement, "Top", Arrays.asList(delegate.getElements(inputElement)), groupers);
				lastInput = inputElement;
				lastTop = top;
				return top.children;
			}
		}

		@Override
		public Object[] getChildren(final Object parentElement) {
			if (parentElement instanceof G) {
				final G parent = (G) parentElement;
				return parent.children;
			}
			return new Object[0];
		}

		@Override
		public Object getParent(final Object element) {
			if (element instanceof E) {
				return ((E) element).parent;
			} else if (element instanceof G) {
				return ((G) element).parent;
			}
			return null;
		}

		@Override
		public boolean hasChildren(final Object element) {
			return element instanceof G;
		}

		public Object[] getViewerElements(final Object[] inputElements) {
			final Object[] result = new Object[inputElements.length];
			int k = 0;
			for (final Object i : inputElements) {
				result[k++] = originalToViewer.get(i);
			}
			return result;
		}

		/**
		 * @param labelProvider
		 * @return
		 */
		public ILabelProvider wrapLabelProvider(final ILabelProvider labelProvider) {
			return new ILabelProvider() {
				@Override
				public void removeListener(final ILabelProviderListener listener) {
					labelProvider.removeListener(listener);
				}

				@Override
				public boolean isLabelProperty(final Object element, final String property) {
					if (element instanceof E) {
						return labelProvider.isLabelProperty(((E) element).value, property);
					}
					return false;
				}

				@Override
				public void dispose() {
					labelProvider.dispose();
				}

				@Override
				public void addListener(final ILabelProviderListener listener) {
					labelProvider.addListener(listener);
				}

				@Override
				public String getText(final Object element) {
					if (element instanceof E) {
						return labelProvider.getText(((E) element).value);
					} else if (element instanceof G) {
						return ((G) element).name;
					} else {
						return labelProvider.getText(element);
					}
				}

				@Override
				public Image getImage(final Object element) {
					if (element instanceof E) {
						return labelProvider.getImage(((E) element).value);
					} else if (element instanceof G) {
						return null;
					} else {
						return labelProvider.getImage(element);
					}
				}
			};
		}

		public ColumnLabelProvider wrapColumnLabelProvider(final ColumnLabelProvider clp, final boolean isFirstColumn) {
			return new WrappedColumnLabelProvider(clp, isFirstColumn);
		}

		public Object[] getInputElements(final Object[] checkedElements) {
			final LinkedList<Object> result = new LinkedList<Object>();

			for (final Object o : checkedElements) {
				if (o instanceof E) {
					final Object original = viewerToOriginal.get(o);
					if (original != null) {
						result.add(original);
					}
				}
			}

			return result.toArray();
		}
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
				allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray())));
				// add to allSelectedElements everything showing and selected in the viewer
				allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(viewer.getCheckedElements())));
				// re-filter the viewer
				viewer.refresh();
				// and select only those elements which are in allSelectedElements
				viewer.setCheckedElements(contentProvider.getViewerElements(allSelectedElements.toArray()));
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
				if (contentProvider.hasChildren(element))
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

		if (columns.size() > 0) {
			for (final Pair<String, CellLabelProvider> column : columns) {
				final TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);				
				final ColumnLabelProvider provider = (ColumnLabelProvider) column.getSecond();
				
				final ReversibleViewerComparator sorter = new ReversibleViewerComparator(new ViewerComparator() {
					@Override
					public int compare(Viewer viewer, Object arg0, Object arg1) {
						//FIXME: SG 29/07/2016 - log file reported NPE here!
						return provider.getText(arg0).compareTo(provider.getText(arg1));
					}					
				});
				
				tvc.getColumn().setText(column.getFirst());
				tvc.setLabelProvider(column.getSecond());
				tvc.getColumn().addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						viewer.setComparator(sorter.select());
						viewer.refresh();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
					
				});
				// tvc.getColumn().pack();
			}
			viewer.getTree().setHeaderVisible(true);
			viewer.getTree().setLinesVisible(true);
		}

		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setInput(input);
		viewer.setCheckedElements(contentProvider.getViewerElements(initialSelection));
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
		allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray())));
		// Add everything selected in the viewer from allSelectedElements
		allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(viewer.getCheckedElements())));

		// Return the contents of allSelectedElements, sorted alphabetically
		dialogResult = allSelectedElements.toArray();

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
	 * Add a grouping to any hierarchy displayed; the given label provider will be used to get a string for each input object; all inputs with the same string will be grouped under a "virtual" element
	 * with that name
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
		CellLabelProvider result = contentProvider.wrapColumnLabelProvider(columnLabelProvider, columns.isEmpty());
		this.columns.add(new Pair<String, CellLabelProvider>(title, result));
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
	
	public class WrappedColumnLabelProvider extends ColumnLabelProvider {
		final private ColumnLabelProvider clp;
		final private boolean isFirstColumn;
		
		public WrappedColumnLabelProvider(ColumnLabelProvider clp, boolean isFirstColumn) {
			this.clp = clp;
			this.isFirstColumn = isFirstColumn;
		}
		
		public ColumnLabelProvider getWrapped() {
			return clp;
		}

		private Object unwrap(final Object element) {
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
		
		public ReversibleViewerComparator(ViewerComparator vc) {
			this.vc = vc;
		}
		
		public ViewerComparator select() {
			if (direction == 1) {
				direction = -1;
			}
			else {
				direction = 1;
			}
			return this;
		}
		
		@Override
		public int compare(Viewer viewer, Object a, Object b) {
			return vc.compare(viewer, a, b) * direction;
		}
	}
	
}
