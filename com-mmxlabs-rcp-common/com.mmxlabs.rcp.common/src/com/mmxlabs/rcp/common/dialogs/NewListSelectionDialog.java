/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
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

/**
 * A list picking dialog which is a bit more flexible than the RCP-provided default
 * 
 * Additional features include having extra columns, tree support, and having a quick filter of the list.
 * 
 * TODO selection management, filtering, sensible size on opening, collapse unneeded groups.
 * 
 * @author Simon McGregor
 * 
 */
public class NewListSelectionDialog extends Dialog {
	private String title = "Select elements";
	private String message = "Choose some elements";
	private CheckboxTreeViewer viewer;
	private ViewerComparator comparator = null;
	private final ILabelProvider labelProvider;
	private final SimpleGroupedElementProvider contentProvider;
	private final Object input;
	private final List<Pair<String, CellLabelProvider>> columns = new LinkedList<Pair<String, CellLabelProvider>>();
	private final HashSet<Object> allSelectedElements = new HashSet<Object>();
	private final HashSet<Object> filteredElements = new HashSet<Object>();
	private Object[] dialogResult;

	// TODO: use eclipse TreeNode and TreeNodeContentProvider classes?
	private class TreeNode {
		final Object data;
		// not final, because we mess with these on expansion
		List<TreeNode> children;
		TreeNode parent;

		TreeNode(final TreeNode parent, final Object data) {
			this.parent = parent;
			this.data = data;
			this.children = new ArrayList<TreeNode>();
		}

		/**
		 * Creates a new tree node populated with children created from a list of objects.
		 * 
		 * @param parent
		 * @param data
		 * @param children
		 */
		TreeNode(final TreeNode parent, final Object data, final List<Object> children) {
			this(parent, data);
			for (final Object child : children) {
				this.children.add(new TreeNode(this, child));
			}
		}

		Object[] getChildData() {
			final int n = children.size();
			final Object[] result = new Object[n];

			for (int i = 0; i < n; i++) {
				result[i] = children.get(i);
			}

			return result;
		}

	}

	private class LookupTreeContentProvider implements ITreeContentProvider {
		protected TreeNode tree;
		final Map<Object, TreeNode> lookup = new HashMap<Object, TreeNode>(); // we associate viewer objects with TreeNodes

		public void associateNodes(final TreeNode node, final Map<Object, TreeNode> map) {
			map.put(node.data, node);
			for (final TreeNode child : node.children) {
				associateNodes(child, map);
			}
		}

		protected TreeNode makeTree(final Object newInput) {
			return tree;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			tree = makeTree(newInput);
			lookup.clear();
			associateNodes(tree, lookup);
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			// TODO Auto-generated method stub
			// return null;
			return tree.getChildData();
		}

		@Override
		public Object[] getChildren(final Object parentElement) {
			final TreeNode node = lookup.get(parentElement);
			if (node != null) {
				return node.getChildData();
			}
			return null;
		}

		@Override
		public Object getParent(final Object element) {
			final TreeNode node = lookup.get(element);
			if (node != null && node.parent != null) {
				return node.parent.data;
			}
			return null;
		}

		@Override
		public boolean hasChildren(final Object element) {
			final TreeNode node = lookup.get(element);
			if (node != null) {
				return node.children.size() > 0;
			}
			return false;
		}

	}

	private class SimpleGroupedElementProvider extends LookupTreeContentProvider {
		final IStructuredContentProvider delegate;
		final List<ILabelProvider> groupings = new LinkedList<ILabelProvider>();

		/**
		 * 
		 * @param provider
		 * @param groupings
		 *            The groups, at each hierarchical level, to display contents under.
		 */
		SimpleGroupedElementProvider(final IStructuredContentProvider provider) {
			delegate = provider;
			// this.groupings = groupings;
		}

		@Override
		public void dispose() {
			delegate.dispose();
			super.dispose();
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			delegate.inputChanged(viewer, newInput, newInput);
			super.inputChanged(viewer, newInput, newInput);
		}

		@Override
		protected TreeNode makeTree(final Object inputObject) {
			// create the root node with the list of all tree elements as its children
			final TreeNode tree = new TreeNode(null, null, Arrays.asList(delegate.getElements(inputObject)));
			// expand it according to the groupings we were given
			recursivelyExpandNode(tree, groupings, 0);
			return tree;
		}

		/**
		 * Takes a TreeNode object with a list of ordinary objects as children.
		 * 
		 * Given a label provider, categorises the children by label and then replaces the node's children with a list of categories (each a TreeNode with its own list of children).
		 */
		void expandNode(final TreeNode node, final ILabelProvider labeller) {
			final Map<String, TreeNode> groups = new TreeMap<String, TreeNode>();

			// attach each Object to a labelled node (creating one if it doesn't already exist)
			for (final TreeNode child : node.children) {
				final String label = labeller.getText(child.data);
				TreeNode groupNode = groups.get(label);
				if (groupNode == null) {
					groupNode = new TreeNode(node, label);
					groups.put(label, groupNode);
				}
				// move the child into the appropriate group node
				groupNode.children.add(child);
				child.parent = groupNode;
			}
			// replace the node's children with a list of labelled nodes
			node.children = new ArrayList<TreeNode>(groups.values());
		}

		/**
		 * Calls expandNode recursively to expand an entire tree's worth of nodes. Uses a depth parameter for clarity and to avoid gotchas with list modification.
		 */
		void recursivelyExpandNode(final TreeNode node, final List<ILabelProvider> labellers, final int depth) {
			// terminate recursion if we have run out of labeller levels
			if (depth >= labellers.size()) {
				return;
			}

			// expand the node using the appropriate labeller
			expandNode(node, labellers.get(depth));

			// and recurse over the node's children
			for (final Object childNode : node.children) {
				recursivelyExpandNode((TreeNode) childNode, labellers, depth + 1);
			}
		}

	}

	public NewListSelectionDialog(final Shell parentShell, final Object input, final IStructuredContentProvider contentProvider, final ILabelProvider labelProvider) {
		super(parentShell);
		this.input = input;

		final SimpleGroupedElementProvider gep = new SimpleGroupedElementProvider(contentProvider);
		this.contentProvider = gep;
		this.labelProvider = labelProvider;
		// this.labelProvider = gep.wrapLabelProvider(labelProvider);
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

				/*
				 * // remove from allSelectedElements everything which is still showing // in the viewer
				 * allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray()))); // add to allSelectedElements everything showing and selected in the
				 * viewer allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(viewer.getCheckedElements()))); // re-filter the viewer viewer.refresh(); // and select only those
				 * elements which are in allSelectedElements viewer.setCheckedElements(contentProvider.getViewerElements(allSelectedElements.toArray()));
				 */
			}
		});
		viewer = new CheckboxTreeViewer(inner, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		if (comparator != null) {
			viewer.setComparator(comparator);
		}
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				filteredElements.remove(element);
				if (filters.isEmpty())
					return true;
				if (contentProvider.hasChildren(element))
					return true;
				final String t = labelProvider.getText(element).toLowerCase();

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
				tvc.getColumn().setText(column.getFirst());
				tvc.setLabelProvider(column.getSecond());
				// tvc.getColumn().pack();
			}
			viewer.getTree().setHeaderVisible(true);
			viewer.getTree().setLinesVisible(true);
		}

		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setInput(input);
		/*
		 * viewer.setCheckedElements(contentProvider.getViewerElements(initialSelection));
		 */
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
		/*
		 * // Remove everything shown in the viewer from allSelectedElements allSelectedElements.retainAll(Arrays.asList(contentProvider.getInputElements(filteredElements.toArray()))); // Add
		 * everything selected in the viewer from allSelectedElements allSelectedElements.addAll(Arrays.asList(contentProvider.getInputElements(viewer.getCheckedElements())));
		 */

		// Return the contents of allSelectedElements, sorted alphabetically
		dialogResult = allSelectedElements.toArray();

		final Comparator<Object> c = new Comparator<Object>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Object arg0, final Object arg1) {
				// No compare method for Pair objects (consider adding to base class?)
				final Pair<String, ?> p0 = (Pair<String, ?>) arg0;
				final Pair<String, ?> p1 = (Pair<String, ?>) arg1;

				return ((String) p0.getFirst()).compareTo((String) p1.getFirst());
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
		contentProvider.groupings.add(labelProvider);
	}

	/**
	 * Add a column to the table.
	 * 
	 * @param title
	 * @param columnLabelProvider
	 */
	public void addColumn(final String title, final ColumnLabelProvider columnLabelProvider) {
		this.columns.add(new Pair<String, CellLabelProvider>(title, columnLabelProvider));
		// this.columns.add(new Pair<String, CellLabelProvider>(title, contentProvider.wrapColumnLabelProvider(columnLabelProvider, columns.isEmpty())));
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
	}

	public Object[] getResult() {
		return dialogResult;
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

	/**
	 */
	public void setComparator(final ViewerComparator vc) {
		comparator = vc;
	}
}
