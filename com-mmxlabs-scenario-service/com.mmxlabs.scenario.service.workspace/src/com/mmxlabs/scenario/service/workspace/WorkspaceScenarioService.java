/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.workspace;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

public class WorkspaceScenarioService implements IScenarioService {

	private static final Logger log = LoggerFactory.getLogger(WorkspaceScenarioService.class);

	private final QualifiedName qnUUID = new QualifiedName("com.mmxlabs.scenario.service.workspace.WorkspaceScenarioService", "UUID");

	/**
	 * A workspace listener to maintain the EMF Model state.
	 * 
	 */
	private final class workspaceChangeListener implements IResourceChangeListener {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {

			IResourceDelta parentDelta = event.getDelta();
			processDelta(parentDelta);
		}

		private void processDelta(IResourceDelta delta) {

			if (delta.getKind() == IResourceDelta.REMOVED) {

				// Recurse first, then remove
				for (final IResourceDelta d : delta.getAffectedChildren()) {
					processDelta(d);
				}

				final IResource resource = delta.getResource();
				final Container container = mapWorkspaceToModel.get(resource);
				if (container != null) {

					// Remove node from parent.
					((Container) container.eContainer()).getElements().remove(container);

					// Remove mapping
					mapWorkspaceToModel.remove(resource);
				}
			} else if (delta.getKind() == IResourceDelta.ADDED) {

				// Add then recurse

				final IResource resource = delta.getResource();

				if (resource.getType() == IResource.FILE) {

					final Container container = mapWorkspaceToModel.get(resource.getParent());

					createScenarioInstance(container, resource);

				} else if (resource.getType() == IResource.FOLDER || resource.getType() == IResource.PROJECT) {

					final Container container = mapWorkspaceToModel.get(resource.getParent());

					createFolder(container, resource);
				}

				// Recurse
				for (final IResourceDelta d : delta.getAffectedChildren()) {

					processDelta(d);
				}
			} else {

				// TODO - Update resource names

				// Just recurse
				for (final IResourceDelta d : delta.getAffectedChildren()) {

					processDelta(d);
				}
			}

		}
	}

	private IModelService modelService;

	private ScenarioService scenarioService;

	private final Map<String, ScenarioInstance> instanceMap = new HashMap<String, ScenarioInstance>();

	private final Map<IResource, Container> mapWorkspaceToModel = new WeakHashMap<IResource, Container>();

	private workspaceChangeListener workspaceChangeListener;

	public WorkspaceScenarioService() {
	}

	@Override
	public String getName() {
		return "Workspace Scenario Service";
	}

	@Override
	public ScenarioService getServiceModel() {
		return scenarioService;
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final String scenarioServiceID = d.get("component.id").toString();

		scenarioService = initialise();

		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		scanForScenarios(scenarioServiceID, root, scenarioService);

		workspaceChangeListener = new workspaceChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(workspaceChangeListener);
	}

	public void stop(final ComponentContext context) {
		scenarioService = null;
		modelService = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(workspaceChangeListener);
		workspaceChangeListener = null;

	}

	private ScenarioService initialise() {

		final ScenarioService serviceService = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceService.setName(getName());
		serviceService.setDescription(getName());

		// TODO: Hook up a listener to react to model changes and replicate in the workspace.

		return serviceService;
	}

	@Override
	public ScenarioInstance getScenarioInstance(final String uuid) {
		return instanceMap.get(uuid);
	}

	public EditingDomain initEditingDomain() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		//
		return new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	public void scanForScenarios(final String scenarioServiceID) {

	}

	public void scanForScenarios(final String scenarioServiceID, final IContainer workspaceContainer, final Container modelContainer) {

		if (workspaceContainer.isAccessible()) {

			try {
				for (final IResource r : workspaceContainer.members()) {
					if (r.getType() == IResource.FILE) {
						createScenarioInstance(modelContainer, r);
					} else if (r.getType() == IResource.FOLDER || r.getType() == IResource.PROJECT) {

						// Create container,
						final Folder folder = createFolder(modelContainer, r);

						// Recurse
						scanForScenarios(scenarioServiceID, (IContainer) r, folder);

					}
				}
			} catch (final CoreException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private Folder createFolder(final Container modelContainer, final IResource r) {

		System.out.println("Create folder: " + r.getName());

		final Folder folder = ScenarioServiceFactory.eINSTANCE.createFolder();
		modelContainer.getElements().add(folder);
		folder.setName(r.getName());

		mapWorkspaceToModel.put(r, folder);

		return folder;
	}

	private void createScenarioInstance(final Container container, final IResource r) {

		String uuid = null;
		try {
			// Try and restore previous UUID
			uuid = r.getPersistentProperty(qnUUID);
			if (uuid == null) {
				// Otherwise generate a new one.
				uuid = UUID.randomUUID().toString();
				r.setPersistentProperty(qnUUID, uuid);
			}
		} catch (CoreException e) {
			log.error(e.getMessage(), e);
		}

		final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		scenarioInstance.setUuid(uuid);
		scenarioInstance.setName(r.getName());

		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
		// TODO: Set correct content type
		metadata.setContentType("text/xmi");
		scenarioInstance.setMetadata(metadata);

		mapWorkspaceToModel.put(r, scenarioInstance);

		container.getElements().add(scenarioInstance);
	}

	public IModelService getModelService() {
		return modelService;
	}

	public void setModelService(final IModelService modelService) {
		this.modelService = modelService;
	}

	@Override
	public boolean exists(final String uuid) {
		return instanceMap.containsKey(uuid);
	}

	@Override
	public String saveAs(final ScenarioInstance instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScenarioInstance copyTo(final ScenarioInstance from, final int flags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(final ScenarioInstance instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final String uuid) {
		// TODO Auto-generated method stub

	}
}
