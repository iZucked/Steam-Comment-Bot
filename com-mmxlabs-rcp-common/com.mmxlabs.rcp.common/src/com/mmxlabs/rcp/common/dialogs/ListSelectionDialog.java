/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;

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
	private ILabelProvider labelProvider;
	private GroupedElementProvider contentProvider;
	private Object input;
	private List<Pair<String, CellLabelProvider>> columns = new LinkedList<Pair<String, CellLabelProvider>>();
	private LinkedList<ILabelProvider> groupStack = new LinkedList<ILabelProvider>();
	private Object[] initialSelection = new Object[0];
	private Object[] result;

	private class GroupedElementProvider implements ITreeContentProvider {
		private IStructuredContentProvider delegate;
		private List<ILabelProvider> groupers = new LinkedList<ILabelProvider>();

		private Map<Object, E> originalToViewer = new HashMap<Object, E>();
		private Map<E, Object> viewerToOriginal = new HashMap<E, Object>();
		private class E {
			public final Object value;
			public final G parent;

			public E(final G parent, final Object value) {
				this.parent = parent;
				this.value = value;
			}
		}

		private class G {
			public final String name;
			public final Object[] children;
			public Object parent;

			public G(Object parent, final String name, final List<Object> inputs, final List<ILabelProvider> stack) {
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
		public GroupedElementProvider(IStructuredContentProvider contentProvider) {
			this.delegate = contentProvider;
		}

		@Override
		public void dispose() {
			delegate.dispose();
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			originalToViewer.clear();
			viewerToOriginal.clear();
			delegate.inputChanged(viewer, oldInput, newInput);
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			final G top = new G(inputElement, "Top", Arrays.asList(delegate.getElements(inputElement)), groupers);
			return top.children;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof G) {
				final G parent = (G) parentElement;
				return parent.children;
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof E) {
				return ((E) element).parent;
			} else if (element instanceof G) {
				return ((G) element).parent;
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof G;
		}

		public Object[] getViewerElements(final Object[] inputElements) {
			Object[] result = new Object[inputElements.length];
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
				public void removeListener(ILabelProviderListener listener) {
					labelProvider.removeListener(listener);
				}

				@Override
				public boolean isLabelProperty(Object element, String property) {
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
				public void addListener(ILabelProviderListener listener) {
					labelProvider.addListener(listener);
				}

				@Override
				public String getText(Object element) {
					if (element instanceof E) {
						return labelProvider.getText(((E) element).value);
					} else if (element instanceof G) {
						return ((G) element).name;
					} else {
						return labelProvider.getText(element);
					}
				}

				@Override
				public Image getImage(Object element) {
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
			return new ColumnLabelProvider() {
				private Object unwrap(final Object element) {
					if (element instanceof E) {
						return ((E) element).value;
					} else if (element instanceof G) {
						return null;
					}
					return element;
				}
				
				@Override
				public Font getFont(Object element) {
					Object a = unwrap(element);
					return a == null? null: clp.getFont(a);
				}

				@Override
				public Color getBackground(Object element) {
					Object a = unwrap(element);
					return a == null? null: clp.getBackground(a);
				}

				@Override
				public Color getForeground(Object element) {
					Object a = unwrap(element);
					return a == null? null: clp.getBackground(a);
				}

				@Override
				public Image getImage(Object element) {
					Object a = unwrap(element);
					return a == null? super.getImage(element): clp.getImage(a);
				}

				@Override
				public String getText(Object element) {
					if (element instanceof G) {
						if (isFirstColumn)
							return ((G) element).name;
						else
							return "";
					} else {
						return clp.getText(unwrap(element));
					}
				}

				@Override
				public Image getToolTipImage(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipImage(object): clp.getToolTipImage(a);
				}

				@Override
				public String getToolTipText(Object element) {
					Object a = unwrap(element);
					return a == null? super.getToolTipText(element): clp.getToolTipText(a);
				}

				@Override
				public Color getToolTipBackgroundColor(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipBackgroundColor(object): clp.getToolTipBackgroundColor(a);
				}

				@Override
				public Color getToolTipForegroundColor(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipForegroundColor(object): clp.getToolTipForegroundColor(a);
				}

				@Override
				public Font getToolTipFont(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipFont(object): clp.getToolTipFont(a);
				}

				@Override
				public Point getToolTipShift(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipShift(object): clp.getToolTipShift(a);

				}

				@Override
				public boolean useNativeToolTip(Object object) {
					Object a = unwrap(object);
					return a == null? super.useNativeToolTip(object): clp.useNativeToolTip(a);
				}

				@Override
				public int getToolTipTimeDisplayed(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipTimeDisplayed(object): clp.getToolTipTimeDisplayed(a);
				}

				@Override
				public int getToolTipDisplayDelayTime(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipDisplayDelayTime(object): clp.getToolTipDisplayDelayTime(a);
				}

				@Override
				public int getToolTipStyle(Object object) {
					Object a = unwrap(object);
					return a == null? super.getToolTipStyle(object): clp.getToolTipStyle(a);
				}

				@Override
				public void addListener(ILabelProviderListener listener) {
					clp.addListener(listener);
				}

				@Override
				public void dispose() {
					clp.dispose();
				}

				@Override
				public boolean isLabelProperty(Object element, String property) {
					Object a = unwrap(element);
					return a == null? false : clp.isLabelProperty(a, property);
				}

				@Override
				public void removeListener(ILabelProviderListener listener) {
					clp.removeListener(listener);
				}				
			};
		}

		public Object[] getInputElements(final Object[] checkedElements) {
			final LinkedList<Object> result = new LinkedList<Object>();
			
			for (final Object o : checkedElements) {
				if (o instanceof E) {
					final Object original = viewerToOriginal.get(o);
					if (original != null) result.add(original);
				}
			}
			
			return result.toArray();
		}
	}

	public ListSelectionDialog(final Shell parentShell, final Object input, final IStructuredContentProvider contentProvider, final ILabelProvider labelProvider) {
		super(parentShell);
		this.input = input;
//		if (contentProvider instanceof ITreeContentProvider) {
//			this.contentProvider = contentProvider;
//			this.labelProvider = labelProvider;
//		} else {
			final GroupedElementProvider gep = new GroupedElementProvider(contentProvider);
			this.contentProvider = gep;
			this.labelProvider = gep.wrapLabelProvider(labelProvider);
//		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite box = (Composite) super.createDialogArea(parent);

		final Composite inner = new Composite(box, SWT.NONE);
		inner.setLayout(new GridLayout(1, false));
		// Create view within inner.
		viewer = new CheckboxTreeViewer(inner, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		if (columns.size() > 0) {
			for (final Pair<String, CellLabelProvider> column : columns) {
				final TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
				tvc.getColumn().setText(column.getFirst());
				tvc.setLabelProvider(column.getSecond());
				// tvc.getColumn().pack();
			}
			viewer.getTree().setHeaderVisible(true);
			viewer.getTree().setLinesVisible(true);
		}
		

		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setInput(input);
		viewer.setCheckedElements(contentProvider.getViewerElements(initialSelection ));
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
		result = contentProvider.getInputElements(viewer.getCheckedElements());
		super.okPressed();
	}

	@Override
	public void create() {
		super.create();
		getShell().setText(title);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(480,640);
	}

	/**
	 * Add a grouping to any hierarchy displayed; the given label provider will be used to get a string for each input object; all inputs with the same string will be grouped under a "virtual" element
	 * with that name
	 * 
	 * @param labelProvider
	 */
	public void groupBy(final ColumnLabelProvider labelProvider) {
		((GroupedElementProvider)contentProvider).groupers.add(labelProvider);
	}

	/**
	 * Add a column to the table.
	 * 
	 * @param title
	 * @param columnLabelProvider
	 */
	public void addColumn(final String title, final ColumnLabelProvider columnLabelProvider) {
		this.columns.add(new Pair<String, CellLabelProvider>(title, contentProvider.wrapColumnLabelProvider(columnLabelProvider, columns.isEmpty())));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setInitialSelections(Object[] array) {
		initialSelection = array;
	}

	public Object[] getResult() {
		return result;
	}
}
