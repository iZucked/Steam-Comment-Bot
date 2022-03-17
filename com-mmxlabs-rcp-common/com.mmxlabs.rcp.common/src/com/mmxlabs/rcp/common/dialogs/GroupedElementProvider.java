/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.dialogs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class GroupedElementProvider implements ITreeContentProvider {
	private final IStructuredContentProvider delegate;
	public final List<ILabelProvider> groupers = new LinkedList<>();

	private final Map<Object, E> originalToViewer = new HashMap<>();
	private final Map<E, Object> viewerToOriginal = new HashMap<>();

	private G lastTop;
	private Object lastInput;

	// Row element??
	public class E {
		public final Object value;
		public final G parent;

		public E(final G parent, final Object value) {
			this.parent = parent;
			this.value = value;
		}
	}

	// Tree node grouping?
	public class G {
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

				final List<ILabelProvider> subStack = stack.size() == 1 ? new LinkedList<>() : stack.subList(1, stack.size() - 1);
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
			final TreeMap<String, List<Object>> out = new TreeMap<>();

			for (final Object o : inputs) {
				final String s = grouper.getText(o);
				List<Object> group = out.computeIfAbsent(s, l -> new LinkedList<>());
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

	public Object[] getInputElements(final Object[] checkedElements) {
		final List<Object> result = new LinkedList<>();

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