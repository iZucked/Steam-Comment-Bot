/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioModel;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	/**
	 * Flag indicating whether this content provider returns the whole tree, or only down to {@link Container} elements
	 */
	private boolean showOnlyContainers = false;

	public ScenarioServiceContentProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}

	@Override
	public Object[] getElements(final Object object) {

		return getChildren(object);
	}

	@Override
	public Object[] getChildren(final Object object) {

		final Object[] elements;
		if (object instanceof ScenarioServiceRegistry) {
			final ScenarioServiceRegistry scenarioServiceRegistry = (ScenarioServiceRegistry) object;
			elements = scenarioServiceRegistry.getScenarioServices().toArray();
		} else if (object instanceof IScenarioService) {
			final IScenarioService scenarioService = (IScenarioService) object;
			elements = scenarioService.getServiceModel().getElements().toArray();
		} else {
			elements = super.getChildren(object);
		}

		// Problem - if there is only one - but then add another, the parent in not in the tree!
		// We need to record something so the notification can force a full refresh of the viewer.

		// // Skip root node if there is only one item
		if (elements.length == 1 && super.getParent(object) == null) {
			return getChildren(elements[0]);
		}

		return filter(elements);
	}

	/**
	 * Filter the input array of elements based on configuration flags and return the same or new array of objects.
	 * 
	 * @param elements
	 * @return
	 */
	private Object[] filter(final Object[] elements) {

		if (showOnlyContainers) {
			// Allow Objects down to the Container
			final List<Object> c = new ArrayList<Object>(elements.length);
			for (final Object e : elements) {
				if (e instanceof Container) {
					c.add(e);
				} else if (e instanceof ScenarioModel) {
					c.add(e);
				} else if (e instanceof IScenarioService) {
					c.add(e);
				} else if (e instanceof ScenarioServiceRegistry) {
					c.add(e);
				}
			}
			return c.toArray();
		}

		return elements;
	}

	@Override
	public boolean hasChildren(final Object object) {
		return getChildren(object).length > 0;
	}

	public boolean isShowOnlyContainers() {
		return showOnlyContainers;
	}

	public void setShowOnlyContainers(final boolean showOnlyContainers) {
		this.showOnlyContainers = showOnlyContainers;
	}
}
