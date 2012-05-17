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
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	private boolean showScenarioServices = true;
	private boolean showScenarioInstances = true;
	private boolean showFolders = true;
	private boolean showMetadata = false;
	private boolean showArchivedElements = false;

	private final Map<Object, Boolean> filteredElements = new WeakHashMap<Object, Boolean>();

	public ScenarioServiceContentProvider() {
		super(ScenarioServiceComposedAdapterFactory.getAdapterFactory());
	}

	@Override
	public Object[] getElements(final Object object) {

		return getChildren(object);
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
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

		final List<Object> c = new ArrayList<Object>(elements.length);

		// Allow Objects down to the Container
		for (final Object e : elements) {

			boolean filtered = true;

			if (e instanceof ScenarioInstance) {
				if (isShowScenarioInstances()) {
					ScenarioInstance instance = (ScenarioInstance) e;
					if (!instance.isArchived() || isShowArchivedElements())
						filtered = false;
				}
			} else if (e instanceof Folder) {
				filtered = !isShowFolders();
			} else if (e instanceof Metadata) {
				filtered = !isShowMetadata();
			} else if (e instanceof ScenarioModel) {
				filtered = !isShowScenarioServices();
			} else if (e instanceof IScenarioService) {
				filtered = !isShowScenarioServices();
			} else if (e instanceof ScenarioServiceRegistry) {
				filtered = false;
			} else {
				filtered = true;
			}

			if (filtered) {
				filteredElements.put(e, true);
			} else {
				c.add(e);
			}
		}
		return c.toArray();
	}

	@Override
	public boolean hasChildren(final Object object) {
		return getChildren(object).length > 0;
	}

	public boolean isShowArchivedElements() {
		return showArchivedElements;
	}

	public void setShowArchivedElements(final boolean showArchivedElements) {
		this.showArchivedElements = showArchivedElements;
	}

	@Override
	public void notifyChanged(final Notification arg0) {
		super.notifyChanged(arg0);

		// Filtered elements are not present in tree viewer, but we may still need to show child entries - force a full refresh rather than a partial update
		final Object notifier = arg0.getNotifier();
		if (filteredElements.containsKey(notifier)) {
			viewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

	public boolean isShowScenarioServices() {
		return showScenarioServices;
	}

	public void setShowScenarioServices(boolean showScenarioServices) {
		this.showScenarioServices = showScenarioServices;
	}

	public boolean isShowScenarioInstances() {
		return showScenarioInstances;
	}

	public void setShowScenarioInstances(boolean showScenarioInstances) {
		this.showScenarioInstances = showScenarioInstances;
	}

	public boolean isShowFolders() {
		return showFolders;
	}

	public void setShowFolders(boolean showFolders) {
		this.showFolders = showFolders;
	}

	public boolean isShowMetadata() {
		return showMetadata;
	}

	public void setShowMetadata(boolean showMetadata) {
		this.showMetadata = showMetadata;
	}
}
