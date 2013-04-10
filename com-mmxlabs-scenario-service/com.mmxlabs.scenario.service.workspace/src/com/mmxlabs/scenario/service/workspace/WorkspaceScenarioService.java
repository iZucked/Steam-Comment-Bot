/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.workspace;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class WorkspaceScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(WorkspaceScenarioService.class);

	/**
	 * A workspace listener to maintain the EMF Model state.
	 * 
	 */
	private final class workspaceChangeListener implements IResourceChangeListener {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {

			final IResourceDelta parentDelta = event.getDelta();
			if (parentDelta != null) {
				processDelta(parentDelta);
			}
		}

		private void processDelta(final IResourceDelta delta) {

			if (delta.getKind() == IResourceDelta.REMOVED) {

				// Recurse first, then remove
				for (final IResourceDelta d : delta.getAffectedChildren()) {
					processDelta(d);
				}

				final IResource resource = delta.getResource();
				final Container container = mapWorkspaceToModel.get(resource);
				if (container != null) {

					// Remove node from parent.
					if (container.getParent() != null) {
						container.getParent().getElements().remove(container);
					}

					// Remove mapping
					mapWorkspaceToModel.remove(resource);
				}
			} else if (delta.getKind() == IResourceDelta.ADDED) {

				// Add then recurse

				final IResource resource = delta.getResource();

				if (resource.getType() == IResource.FILE) {

					final Container container = mapWorkspaceToModel.get(resource.getParent());
					try {
						createScenarioInstance(container, resource);
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
					}
				} else if (resource.getType() == IResource.FOLDER || resource.getType() == IResource.PROJECT) {

					final Container container = resource.getType() == IResource.PROJECT ? getServiceModel() : mapWorkspaceToModel.get(resource.getParent());

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

	private final Map<String, ScenarioInstance> instanceMap = new HashMap<String, ScenarioInstance>();

	private final Map<IResource, Container> mapWorkspaceToModel = new HashMap<IResource, Container>();

	private workspaceChangeListener workspaceChangeListener;

	public WorkspaceScenarioService() {
		super("Workspace (for testing)");
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final String scenarioServiceID = d.get("component.id").toString();

		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		mapWorkspaceToModel.put(root, getServiceModel());
		scanForScenarios(scenarioServiceID, root, getServiceModel());

		workspaceChangeListener = new workspaceChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(workspaceChangeListener);
	}

	public void stop(final ComponentContext context) {

		modelService = null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(workspaceChangeListener);
		workspaceChangeListener = null;
	}

	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceService = ScenarioServiceFactory.eINSTANCE.createScenarioService();

		return serviceService;
	}

	public void scanForScenarios(final String scenarioServiceID, final IContainer workspaceContainer, final Container modelContainer) {
		if (workspaceContainer.isAccessible()) {
			try {
				for (final IResource r : workspaceContainer.members()) {
					if (r.getType() == IResource.FILE) {
						try {
							createScenarioInstance(modelContainer, r);
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
						}
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

		final Folder folder = ScenarioServiceFactory.eINSTANCE.createFolder();
		modelContainer.getElements().add(folder);
		folder.setName(r.getName());

		mapWorkspaceToModel.put(r, folder);

		return folder;
	}

	private void createScenarioInstance(final Container container, final IResource r) throws IOException {

		ScenarioInstance scenarioInstance = null;

		final URI resourceURI = URI.createPlatformResourceURI(r.getFullPath().toString(), true);
		final URI manifestURI = URI.createURI("archive:" + resourceURI.toString() + "!/" + "MANIFEST.xmi");

		if (r.getName().endsWith("sc2")) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

			final Resource manifestResource = resourceSet.createResource(manifestURI);
			manifestResource.load(null);

			final Manifest manifest = (Manifest) manifestResource.getContents().get(0);

			scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setAdapters(new HashMap<Class<?>, Object>());
			scenarioInstance.setUuid(manifest.getUUID());

			for (final String uris : manifest.getModelURIs()) {
				URI uri = URI.createURI(uris);
				if (uri.isRelative()) {
					uri = uri.resolve(manifestURI);
				}
				scenarioInstance.getSubModelURIs().add(uri.toString());
			}

			scenarioInstance.getDependencyUUIDs().addAll(manifest.getDependencyUUIDs());

			final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();

			metadata.setContentType(manifest.getScenarioType());
			scenarioInstance.setMetadata(metadata);

		}

		if (scenarioInstance != null) {
			scenarioInstance.getAdapters().put(IScenarioService.class, this);
			scenarioInstance.setName(r.getName());
			mapWorkspaceToModel.put(r, scenarioInstance);
			instanceMap.put(scenarioInstance.getUuid(), scenarioInstance);
			container.getElements().add(scenarioInstance);
		}
	}

	@Override
	public void delete(final Container instance) {
		for (final IResource resource : mapWorkspaceToModel.keySet()) {
			if (mapWorkspaceToModel.get(resource) == instance) {
				try {
					resource.delete(true, null);
				} catch (CoreException e) {
					log.error("Could not delete " + resource, e);
				}
				return;
			}
		}
	}

	@Override
	public ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) throws IOException {
		IResource containerResource = null;
		for (final Map.Entry<IResource, Container> e : mapWorkspaceToModel.entrySet()) {
			if (e.getValue() == container) {
				containerResource = e.getKey();
				break;
			}
		}

		if (containerResource == null) {
			throw new IllegalStateException("Container has no IResource mapping");
		}
		
		final String uuid = UUID.randomUUID().toString();
		final URI resourceURI = URI.createPlatformResourceURI(containerResource.getFullPath().toString() + "/" + uuid + ".lingo", true);
		final URI manifestURI = URI.createURI("archive:" + resourceURI.toString() + "!/MANIFEST.xmi");

		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
		manifest.setUUID(uuid);

		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		resourceSet.createResource(manifestURI).getContents().add(manifest);

		for (final ScenarioInstance dependency : dependencies) {
			manifest.getDependencyUUIDs().add(dependency.getUuid());
		}

		final WorkspaceJob wsJob = new WorkspaceJob("Create archive") {
			@Override
			public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
				int index = 0;
				for (final EObject subModel : models) {
					final URI subModelURI = URI.createURI("archive:" + resourceURI.toString() + "!/" + subModel.eClass().getName() + "-" + Integer.toString(index++) + ".xmi");
					manifest.getModelURIs().add(subModelURI.deresolve(manifestURI).toString());
					try {
						modelService.store(subModel, subModelURI);
					} catch (final IOException e1) {
						log.error("Error storing submodel", e1);
					}
				}

				try {
					manifest.eResource().save(null);
				} catch (final IOException e1) {
					log.error("Error saving manifest", e1);
				}
				return Status.OK_STATUS;
			}
		};

		wsJob.schedule();
		try {
			wsJob.join();
		} catch (final InterruptedException e1) {
			log.error("Interrupted waiting for ws job to finish", e1);
		}

		// by this point we should have detected the new scenario and loaded it as a consequence of the listener above
		ScenarioInstance scenarioInstance = getScenarioInstance(uuid);
		// Create instance element and attach.
		load(scenarioInstance);
		save(scenarioInstance);
		return scenarioInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scenario.service.IScenarioService#save(com.mmxlabs.scenario.service.model.ScenarioInstance)
	 */
	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		final EObject instance = scenarioInstance.getInstance();
		if (instance == null) {
			return;
		}
		for (final String uris : scenarioInstance.getSubModelURIs()) {
			final IModelInstance modelInstance = modelService.getModel(resolveURI(uris));
			if (modelInstance != null) {
				modelInstance.save();
			}
		}
	}

	@Override
	public URI resolveURI(String uriString) {
		return URI.createURI(uriString);
	}
}
