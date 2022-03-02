/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 * Class to provide content data for the scenario selector tree view control.
 * 
 * @author Simon McGregor
 * 
 */
public class ScenarioContentProvider implements ITreeContentProvider {
	private List<ScenarioService> services;

	public ScenarioContentProvider() {
	}

	@Override
	public void dispose() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		services = (List<ScenarioService>) newInput;
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		if (services.size() == 1) {
			return getChildren(services.get(0));
		} else {
			final List<ScenarioService> localServices = new LinkedList<>();
			for (final ScenarioService ss : services) {
				if (ss.isLocal()) {
					localServices.add(ss);
				}
			}
			if (localServices.size() == 1) {
				return getChildren(localServices.get(0));
			} else {
				return localServices.toArray();
			}
		}
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		final LinkedList<Object> result = new LinkedList<Object>();
		if (parentElement instanceof Container) {
			for (final Object element : ((Container) parentElement).getElements()) {
				if (element instanceof ScenarioInstance) {
					result.add(element);
				} else if (element instanceof Container) {
					if (!((Container) element).getElements().isEmpty()) {
						result.add(element);
					}
				}

			}
		}

		return result.toArray();
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (!(element instanceof Container)) {
			return false;
		}
		final Container container = (Container) element;
		return !(container.getElements().isEmpty());
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

}