/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.SaveablesProvider;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.ui.internal.ScenarioInstanceSavable;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceComposedAdapterFactory;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider implements IAdaptable {

	private final @NonNull Set<IScenarioService> listeningScenarioServices = new HashSet<>();
	private final @NonNull IScenarioServiceListener scenarioServiceListener = new IScenarioServiceListener() {

		@Override
		public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			// Remove possible reference to loaded data model
			filteredElements.remove(scenarioInstance.getInstance());
			cleanUp(scenarioInstance);
		}

		public void cleanUp(final ScenarioInstance scenarioInstance) {
			if (saveablesMap.values().contains(scenarioInstance)) {
				for (final Map.Entry<ScenarioInstanceSavable, ScenarioInstance> e : saveablesMap.entrySet()) {
					if (e.getValue() == scenarioInstance) {
						final ScenarioInstanceSavable saveable = e.getKey();
						saveablesMap.remove(saveable);
						if (provider != null) {
							saveable.setDeleted();
							provider.fireDirtyChange(saveable);
							provider.fireClosed(saveable);
						}
						return;
					}
				}
			}
		}

		@Override
		public void onPreScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			cleanUp(scenarioInstance);
		}

		@Override
		public void onPostScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceSave(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}

		@Override
		public void onPostScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

		}
	};

	private final class InternalSaveablesProvider extends SaveablesProvider {

		boolean disposed = false;

		@Override
		public void dispose() {
			disposed = true;
			super.dispose();
		}

		@Override
		public Saveable[] getSaveables() {
			return saveablesMap.keySet().toArray(new Saveable[0]);
		}

		@Override
		public Saveable getSaveable(final Object element) {
			for (final Map.Entry<ScenarioInstanceSavable, ScenarioInstance> e : saveablesMap.entrySet()) {
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
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!disposed) {
						fireSaveablesOpened(models);
						fireSaveablesClosed(models);
					}
				}
			});

		}

		public void fireClosed(final Saveable... models) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!disposed) {
						fireSaveablesDirtyChanged(models);
						fireSaveablesClosed(models);
					}
				}
			});
		}

		public void fireDirtyChange(final Saveable... models) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!disposed) {
						fireSaveablesDirtyChanged(models);
					}
				}
			});
		}
	}

	private boolean showScenarioServices = true;
	private boolean showScenarioInstances = true;
	private boolean showFolders = true;
	private boolean showMetadata = false;
	private boolean showArchivedElements = false;
	private boolean showHiddenElements = false;
	private boolean showReadOnlyElements = true;
	private boolean showOnlyCapsImport = false;
	private boolean showOnlyCapsForking = false;

	private final Map<Object, Boolean> filteredElements = new WeakHashMap<Object, Boolean>();

	private final Map<ScenarioInstanceSavable, ScenarioInstance> saveablesMap = new HashMap<>();
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
					if (!saveablesMap.values().contains(scenarioInstance)) {

						final IScenarioService scenarioService = scenarioInstance.getScenarioService();
						if (scenarioService != null) {
							// Register a scenario service listener if required to clean up the saveables.
							if (listeningScenarioServices.contains(scenarioService) == false) {
								scenarioService.addScenarioServiceListener(scenarioServiceListener);
								listeningScenarioServices.add(scenarioService);
							}
							final ScenarioInstanceSavable savable = new ScenarioInstanceSavable(scenarioInstance);
							saveablesMap.put(savable, scenarioInstance);
							if (provider != null) {
								provider.fireOpened(savable);
							}
						}
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
				ScenarioService ss = null;
				if (e instanceof IScenarioService) {
					ss = ((IScenarioService) e).getServiceModel();
				} else {
					ss = (ScenarioService) e;
				}

				boolean visible = isShowScenarioServices();
				if (visible && isShowOnlyCapsImport()) {
					visible = ss.isSupportsImport();
				}
				if (visible && isShowOnlyCapsForking()) {
					visible = ss.isSupportsForking();
				}

				filtered = !visible;
			} else if (e instanceof ScenarioServiceRegistry) {
				filtered = false;
			} else if (e instanceof ScenarioFragment) {
				filtered = false;
			} else {
				filtered = true;
			}
			if (e instanceof ModelReference) {
				// Ignore!
			} else if (filtered) {
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
	 */
	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (SaveablesProvider.class.isAssignableFrom(adapter)) {

			if (provider == null) {
				provider = new InternalSaveablesProvider();
			}
			return adapter.cast(provider);
		}

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

	/**
	 */
	public boolean isShowOnlyCapsImport() {
		return showOnlyCapsImport;
	}

	/**
	 */
	public void setShowOnlyCapsImport(final boolean showCapsImport) {
		this.showOnlyCapsImport = showCapsImport;
	}

	/**
	 */
	public boolean isShowOnlyCapsForking() {
		return showOnlyCapsForking;
	}

	/**
	 */
	public void setShowOnlyCapsForking(final boolean showCapsForking) {
		this.showOnlyCapsForking = showCapsForking;
	}

	@Override
	public void dispose() {
		while (!listeningScenarioServices.isEmpty()) {
			final IScenarioService ss = listeningScenarioServices.iterator().next();
			listeningScenarioServices.remove(ss);
			ss.removeScenarioServiceListener(scenarioServiceListener);
		}
		super.dispose();
	}
}
