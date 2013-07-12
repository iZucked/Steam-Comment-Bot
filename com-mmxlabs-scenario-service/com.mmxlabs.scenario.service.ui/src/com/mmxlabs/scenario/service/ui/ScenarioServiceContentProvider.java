/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.SaveablesProvider;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.internal.ScenarioInstanceSavable;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider implements IAdaptable {

	private final class InternalSaveablesProvider extends SaveablesProvider {
		@Override
		public Saveable[] getSaveables() {
			return saveablesMap.keySet().toArray(new Saveable[0]);
		}

		@Override
		public Saveable getSaveable(final Object element) {
			for (final Map.Entry<Saveable, ScenarioInstance> e : saveablesMap.entrySet()) {
				if (e.getValue() == element) {
					return e.getKey();
				}
			}
			return null;
		}

		@Override
		public Object[] getElements(final Saveable saveable) {
			if (saveablesMap.containsKey(saveable)) {
				return new Object[] { saveablesMap.get(saveable) };
			}
			return null;
		}

		public void fireOpened(final Saveable... models) {
			fireSaveablesOpened(models);
		}
	}

	private boolean showScenarioServices = true;
	private boolean showScenarioInstances = true;
	private boolean showFolders = true;
	private boolean showMetadata = false;
	private boolean showArchivedElements = false;
	private boolean showHiddenElements = false;

	private final Map<Object, Boolean> filteredElements = new WeakHashMap<Object, Boolean>();

	Map<Saveable, ScenarioInstance> saveablesMap = new HashMap<Saveable, ScenarioInstance>();
	private InternalSaveablesProvider provider;

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
					final ScenarioInstanceSavable savable = new ScenarioInstanceSavable(scenarioInstance);
					saveablesMap.put(savable, scenarioInstance);
					if (provider != null) {
						provider.fireOpened(savable);
					}
				}
			} else if (e instanceof Folder) {
				if (isShowFolders()) {
					filtered = !mayBeShow;
				}
			} else if (e instanceof Metadata) {
				filtered = !isShowMetadata();
			} else if (e instanceof ScenarioModel) {
				filtered = !isShowScenarioServices();
			} else if (e instanceof IScenarioService || e instanceof ScenarioService) {
				filtered = !isShowScenarioServices();
			} else if (e instanceof ScenarioServiceRegistry) {
				filtered = false;
			} else if (e instanceof ScenarioFragment) {
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

	public boolean isShowMetadata() {
		return showMetadata;
	}

	public void setShowMetadata(final boolean showMetadata) {
		this.showMetadata = showMetadata;
	}

	public boolean isShowHiddenElements() {
		return showHiddenElements;
	}

	public void setShowHiddenElements(final boolean showHiddenElements) {
		this.showHiddenElements = showHiddenElements;
	}

	/**
	 * @since 4.1
	 */
	@Override
	public Object getAdapter(final Class adapter) {

		if (SaveablesProvider.class.isAssignableFrom(adapter)) {

			if (provider == null) {
				provider = new InternalSaveablesProvider();
			}
			return provider;
		}

		return null;
	}
}
