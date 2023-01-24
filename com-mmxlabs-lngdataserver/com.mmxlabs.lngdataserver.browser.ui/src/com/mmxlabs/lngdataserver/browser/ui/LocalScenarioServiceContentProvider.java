/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;

public class LocalScenarioServiceContentProvider extends AdapterFactoryContentProvider implements IAdaptable {

	private boolean showScenarioServices = true;
	private boolean showScenarioInstances = true;
	private boolean showFolders = true;
	private boolean showArchivedElements = false;
	private boolean showHiddenElements = false;
	private boolean showReadOnlyElements = true;

	private final Map<Object, Boolean> filteredElements = new WeakHashMap<>();

	private boolean showRemoteServices = false;

	public LocalScenarioServiceContentProvider() {
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
	public Object getParent(final Object object) {
		if (object instanceof ScenarioService) {
			final ScenarioService scenarioService = (ScenarioService) object;
			return scenarioService.getScenarioModel();
		} else if (object instanceof Container) {
			final Container container = (Container) object;
			return container.getParent();
		}
		return super.getParent(object);
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
		} else if (object instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) object;
			final List<Object> l = new LinkedList<>();
			l.addAll(scenarioInstance.getElements());
			return l.toArray();
		} else if (object instanceof Container) {
			final Container container = (Container) object;
			return container.getElements().toArray();
		} else {

			elements = super.getChildren(object);
		}

		// Problem - if there is only one - but then add another, the parent in not in the tree!
		// We need to record something so the notification can force a full refresh of the viewer.

		// // Skip root node if there is only one item
		if (false && elements.length == 1 && elements[0] instanceof ScenarioService) {
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

		final List<Object> c = new ArrayList<>(elements.length);

		// Allow Objects down to the Container
		for (final Object e : elements) {

			boolean filtered = true;

			boolean mayBeShow = false;
			if (e instanceof Container) {
				final Container container = (Container) e;
				if (container.isHidden() && isShowHiddenElements()) {
					mayBeShow = true;
				} else if (container.isHidden()) {
					mayBeShow = false;
				} else if (!container.isHidden()) {
					mayBeShow = true;
				} else if (container.isArchived() || isShowArchivedElements()) {
					mayBeShow = true;
				} else if (!container.isArchived()) {
					mayBeShow = true;
				}
			}
			if (e instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) e;
				if (isShowScenarioInstances()) {
					filtered = !mayBeShow;
				}
			} else if (e instanceof Folder) {
				if (isShowFolders()) {
					filtered = !mayBeShow;
				}
			} else if (e instanceof ScenarioModel) {
				filtered = !isShowScenarioServices();
			} else if (e instanceof IScenarioService || e instanceof ScenarioService) {
				ScenarioService ss = null;
				if (e instanceof IScenarioService) {
					ss = ((IScenarioService) e).getServiceModel();
				} else {
					ss = (ScenarioService) e;
				}

				boolean visible = isShowScenarioServices();
				if (visible && !showRemoteServices) {
					visible = ss.isLocal();
				}
				filtered = !visible;
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

		// Filtered elements are not present in tree viewer, but we may still
		// need to show child entries - force a full refresh rather than a
		// partial update
		final Object notifier = arg0.getNotifier();
		if (filteredElements.containsKey(notifier)) {
			ViewerHelper.refresh(viewer, false);
		}
	}

	public boolean isShowScenarioServices() {
		return showScenarioServices;
	}

	public void setShowScenarioServices(final boolean showScenarioServices) {
		this.showScenarioServices = showScenarioServices;
	}

	public boolean isShowScenarioInstances() {
		return showScenarioInstances;
	}

	public void setShowScenarioInstances(final boolean showScenarioInstances) {
		this.showScenarioInstances = showScenarioInstances;
	}

	public boolean isShowFolders() {
		return showFolders;
	}

	public void setShowFolders(final boolean showFolders) {
		this.showFolders = showFolders;
	}

	public boolean isShowHiddenElements() {
		return showHiddenElements;
	}

	public void setShowHiddenElements(final boolean showHiddenElements) {
		this.showHiddenElements = showHiddenElements;
	}

	/**
	 */
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		return adapter.cast(null);
	}

	/**
	 */
	public boolean isShowReadOnlyElements() {
		return showReadOnlyElements;
	}

	/**
	 */
	public void setShowReadOnlyElements(final boolean showReadOnlyElements) {
		this.showReadOnlyElements = showReadOnlyElements;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
