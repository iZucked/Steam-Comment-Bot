/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	/**
	 * Flag indicating whether this content provider returns the whole tree, or only down to {@link Container} elements
	 */
	private boolean showOnlyContainers = false;

	private final Map<Object, Boolean> filteredElements = new WeakHashMap<Object, Boolean>();
	
	public ScenarioServiceContentProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}

	@Override
	public Object[] getElements(final Object object) {

		return getChildren(object);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		filteredElements.clear();
		super.inputChanged(viewer, oldInput, newInput);
	}

	@Override
	public Object[] getChildren(final Object object) {

		final Object[] elements;
		if (object instanceof ScenarioServiceRegistry) {
			final ScenarioServiceRegistry scenarioServiceRegistry = (ScenarioServiceRegistry) object;
			elements = scenarioServiceRegistry.getScenarioModel().getScenarioServices().toArray();
		} else if (object instanceof IScenarioService) {
			final IScenarioService scenarioService = (IScenarioService) object;
			elements = scenarioService.getServiceModel().getElements().toArray();
		} else {
			elements = super.getChildren(object);
		}

		// Problem - if there is only one - but then add another, the parent in not in the tree!
		// We need to record something so the notification can force a full refresh of the viewer.

		// // Skip root node if there is only one item
		if (elements.length == 1 && elements[0] instanceof ScenarioService) {
			filteredElements.put(elements[0], true);
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
				} else {
					filteredElements.put(e, true);
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

	@Override
	public void notifyChanged(final Notification arg0) {
		super.notifyChanged(arg0);

		Object notifier = arg0.getNotifier();
		if (filteredElements.containsKey(notifier)) {
			viewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	}
	
	
}
