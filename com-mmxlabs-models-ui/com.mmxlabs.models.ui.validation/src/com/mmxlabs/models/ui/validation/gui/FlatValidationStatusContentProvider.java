/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Flattened {@link ITreeContentProvider} for {@link IStatus} object hierarchies.
 * 
 * @author Simon Goodall
 * 
 */
public class FlatValidationStatusContentProvider implements ITreeContentProvider {

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
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof Map) {
			@SuppressWarnings("unchecked")
			final Map<ScenarioModelRecord, IStatus> map = (Map<ScenarioModelRecord, IStatus>) parentElement;
			final List<Object> values = new ArrayList<>(map.size());
			for (final Map.Entry<ScenarioModelRecord, IStatus> entry : map.entrySet()) {
				if (entry.getValue() != null) {
					values.add(entry);
					parentsMap.put(entry, parentElement);
				}
			}
			return values.toArray();
		}
		if (parentElement instanceof Map.Entry) {
			@SuppressWarnings("unchecked")
			final Map.Entry<ScenarioModelRecord, IStatus> entry = (Map.Entry<ScenarioModelRecord, IStatus>) parentElement;
			parentsMap.put(entry.getValue(), entry);
			return getChildren(entry.getValue());
		}
		if (parentElement instanceof IStatus) {
			final IStatus status = (IStatus) parentElement;
			if (status.isMultiStatus()) {
				final List ret = expand(status);

				return ret.toArray();
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
		if (element instanceof IStatus) {
			return ((IStatus) element).isMultiStatus();
		}

		return false;
	}

	private List<IStatus> expand(IStatus status) {
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
}
