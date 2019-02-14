/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.ValidationGroup;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Flattened {@link ITreeContentProvider} for {@link IStatus} object hierarchies.
 * 
 * @author Simon Goodall
 * 
 */
public class GroupedValidationStatusContentProvider implements ITreeContentProvider {

	Map<Object, Object> parentsMap = new HashMap<>();

	@Override
	public void dispose() {
		parentsMap.clear();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		parentsMap.clear();
	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Map) {
			@SuppressWarnings("unchecked")
			final Map<ScenarioModelRecord, IStatus> map = (Map<ScenarioModelRecord, IStatus>) inputElement;
			final List<Object> values = new ArrayList<>(map.size());
			for (final Map.Entry<ScenarioModelRecord, IStatus> entry : map.entrySet()) {
				if (entry.getValue() != null) {
					values.add(entry);
					parentsMap.put(entry, inputElement);
				}
			}
			return values.toArray();

		} else if (inputElement instanceof IStatus) {
			final IStatus status = (IStatus) inputElement;
			if (status.isMultiStatus()) {
				final List<IStatus> ret = expand(status);

				final Map<ValidationGroup, List<IStatus>> l = ret.stream() //
						.collect(Collectors.groupingBy(GroupedValidationStatusContentProvider::getGroup));
				final List<Node> nodes = new LinkedList<>();
				for (final Map.Entry<ValidationGroup, List<IStatus>> e : l.entrySet()) {
					final Node node = new Node();
					node.group = e.getKey();
					node.status = e.getValue();
					if (node.group == defaultGroup) {
						node.desc = "General";
					} else {
						node.desc = node.group.toString();
					}
					parentsMap.put(node, status);
					nodes.add(node);
				}
				return nodes.toArray();
			} else {
				return new Object[] { status };
			}
			// } else if (inputElement instanceof IStatus) {
			// final IStatus status = (IStatus) inputElement;
			//
			// if (status.isMultiStatus()) {
			// final List<Object> ret = new LinkedList<>();
			// for (final IStatus c1 : status.getChildren()) {
			// parentsMap.put(c1, inputElement);
			// if (c1.isMultiStatus()) {
			// for (final Object c2 : getChildren(c1)) {
			//
			// ret.add(c2);
			// }
			// } else {
			// ret.add(c1);
			// }
			// }
			// return ret.toArray();
			// } else {
			// return new Object[] { status };
			// }
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		// if (parentElement instanceof Map) {
		// @SuppressWarnings("unchecked")
		// final Map<ScenarioModelRecord, IStatus> map = (Map<ScenarioModelRecord, IStatus>) parentElement;
		// final List<Object> values = new ArrayList<>(map.size());
		// for (final Map.Entry<ScenarioModelRecord, IStatus> entry : map.entrySet()) {
		// if (entry.getValue() != null) {
		// values.add(entry);
		// parentsMap.put(entry, parentElement);
		// }
		// }
		// return values.toArray();
		// }
		if (parentElement instanceof Map.Entry) {
			@SuppressWarnings("unchecked")
			final Map.Entry<ScenarioModelRecord, IStatus> entry = (Map.Entry<ScenarioModelRecord, IStatus>) parentElement;
			parentsMap.put(entry.getValue(), entry);
			return getChildren(entry.getValue());
		}
		if (parentElement instanceof Node) {
			final Node node = (Node) parentElement;
			for (Object o : node.status) {
				parentsMap.put(o, node);
			}
			return node.status.toArray();
		}
		if (parentElement instanceof IStatus) {
			final IStatus status = (IStatus) parentElement;
			if (status.isMultiStatus()) {
				final List<IStatus> ret = expand(status);

				final Map<ValidationGroup, List<IStatus>> l = ret.stream() //
						.collect(Collectors.groupingBy(GroupedValidationStatusContentProvider::getGroup));
				final List<Node> nodes = new LinkedList<>();
				for (final Map.Entry<ValidationGroup, List<IStatus>> e : l.entrySet()) {
					final Node node = new Node();
					node.group = e.getKey();
					node.status = e.getValue();
					if (node.group == defaultGroup) {
						node.desc = "General";
					} else {
						node.desc = node.group.toString();
					}
					nodes.add(node);
						parentsMap.put(node, status);
				}
				return nodes.toArray();
			} else {
				return new Object[] { status };
			}
		}

		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {

		return parentsMap.get(element);
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>) element;
			return !map.isEmpty();
		}
		if (element instanceof Map.Entry) {
			return true;
		}
		if (element instanceof Node) {
			return true;
		}
		if (element instanceof IStatus) {
			return ((IStatus) element).isMultiStatus();
		}

		return false;
	}

	private List<IStatus> expand(final IStatus status) {
		if (status.isMultiStatus()) {
			final List<IStatus> ret = new LinkedList<>();
			for (final IStatus c1 : status.getChildren()) {
				if (c1.isMultiStatus()) {
					for (final IStatus c2 : expand(c1)) {
						ret.add(c2);
					}
				} else {
					ret.add(c1);
				}
			}
			return ret;
		} else {
			return Collections.singletonList(status);
		}
	}

	public class Node {
		public List<IStatus> status;
		public ValidationGroup group;
		public String desc;

	};

	private static ValidationGroup urgentGroup = new ValidationGroup("Urgent", Short.MIN_VALUE);
	private static ValidationGroup defaultGroup = new ValidationGroup("General", (short) 0);

	private static ValidationGroup getGroup(final IStatus status) {

		final boolean isUrgent = status.getSeverity() >= IStatus.ERROR;
		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus dcs = (IDetailConstraintStatus) status;
			final ValidationGroup tag = dcs.getTag();
			if (tag != null) {
				if (isUrgent && !tag.isIgnoreError()) {
					return urgentGroup;
				}
				return tag;
			}

		}

		return isUrgent ? urgentGroup : defaultGroup;
	}
}
