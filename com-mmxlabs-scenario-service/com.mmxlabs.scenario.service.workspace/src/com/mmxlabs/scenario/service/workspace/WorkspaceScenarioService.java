/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry;

public class WorkspaceScenarioService implements IScenarioService {

	private static final Logger log = LoggerFactory.getLogger(WorkspaceScenarioService.class);

	/**
	 * A workspace listener to maintain the EMF Model state.
	 * 
	 */
	private final class workspaceChangeListener implements IResourceChangeListener {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {

			final IResourceDelta parentDelta = event.getDelta();
			processDelta(parentDelta);
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

		// TODO: Forbid direct editing of model expect via this class

		return serviceService;
	}

	@Override
	public ScenarioInstance getScenarioInstance(final String uuid) {
		return instanceMap.get(uuid);
	}

	public EditingDomain initEditingDomain(final EObject rootObject, final ScenarioInstance instance) {
		final BasicCommandStack commandStack = new BasicCommandStack() {
			@Override
			public void execute(final Command command) {
				synchronized (rootObject) {
					super.execute(command);
				}
			}
		};

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker = Activator.getDefault().getCommandProviderTracker();

		// Create the editing domain with a special command stack.
		//
		// return new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());

		// create editing domain
		return new CommandProviderAwareEditingDomain(adapterFactory, commandStack, instance, rootObject, commandProviderTracker);

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

		final Folder folder = ScenarioServiceFactory.eINSTANCE.createFolder();
		modelContainer.getElements().add(folder);
		folder.setName(r.getName());

		mapWorkspaceToModel.put(r, folder);

		return folder;
	}

	private void createScenarioInstance(final Container container, final IResource r) {

		ScenarioInstance scenarioInstance = null;

		final URI resourceURI = URI.createPlatformResourceURI(r.getFullPath().toString(), true);
		final URI manifestURI = URI.createURI("archive:" + resourceURI.toString() + "!/" + "MANIFEST.xmi");

		if (r.getName().endsWith("sc2")) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
			
			final Resource manifestResource = resourceSet.createResource(manifestURI);
			final Manifest manifest = (Manifest) manifestResource.getContents().get(0);
			
			scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
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
	
		} else if (r.getName().endsWith("scn")) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
			
			final Resource manifestResource = resourceSet.createResource(manifestURI);
			final com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest manifest = 
					(com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest) manifestResource.getContents().get(0);
			
			scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setUuid(UUID.randomUUID().toString());
			
			for (final Entry entry: manifest.getEntries()) {
				final URI uri = URI.createURI("../" + entry.getRelativePath()).resolve(manifestURI);
				scenarioInstance.getSubModelURIs().add(uri.toString());
			}
			
			final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
			
			metadata.setContentType("old-scenario");
			scenarioInstance.setMetadata(metadata);
		}
		if (scenarioInstance != null) {
			scenarioInstance.setName(r.getName());
			mapWorkspaceToModel.put(r, scenarioInstance);
			instanceMap.put(scenarioInstance.getUuid(), scenarioInstance);
			container.getElements().add(scenarioInstance);
		}
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
	public void delete(final ScenarioInstance instance) {

	}

	@Override
	public void load(final ScenarioInstance instance) throws IOException {
		if (instance.getInstance() != null)
			return;
		final List<EObject> parts = new ArrayList<EObject>();
		final MMXRootObject implementation = MMXCoreFactory.eINSTANCE.createMMXRootObject();

		for (final String uuid : instance.getDependencyUUIDs()) {
			final ScenarioInstance dep = getScenarioInstance(uuid);
			if (dep != null) {
				load(dep);
				if (dep.getInstance() != null) {
					final EObject depInstance = dep.getInstance();
					if (depInstance instanceof MMXRootObject) {
						// this should probably always be true.
						for (final MMXSubModel sub : ((MMXRootObject) depInstance).getSubModels()) {
							implementation.addSubModel(sub.getSubModelInstance());
						}
					} else {
						parts.add(depInstance); // hmm
					}
				}
			}
		}

		// create MMXRootObject and connect submodel instances into it.
		for (final String subModelURI : instance.getSubModelURIs()) {
			// acquire sub models
			final IModelInstance modelInstance = modelService.getModel(URI.createURI(subModelURI));
			if (modelInstance.getModel() instanceof UUIDObject) {
				implementation.addSubModel((UUIDObject) modelInstance.getModel());
			} else {
				parts.add(modelInstance.getModel());
			}
		}
		parts.add(implementation);
		instance.setInstance(implementation);

		final EditingDomain domain = initEditingDomain(implementation, instance);
		instance.getAdapters().put(EditingDomain.class, domain);
		instance.getAdapters().put(BasicCommandStack.class, (BasicCommandStack) domain.getCommandStack());

		modelService.resolve(parts);
	}

	@Override
	public ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) {
		IResource containerResource = null;
		for (final Map.Entry<IResource, Container> e : mapWorkspaceToModel.entrySet()) {
			if (e.getValue() == container) {
				containerResource = e.getKey();
				break;
			}
		}
		
		final String uuid = UUID.randomUUID().toString();
		final URI resourceURI = URI.createPlatformResourceURI(containerResource.getFullPath().toString() + "/" +uuid + ".sc2", true);
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
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
				int index = 0;
				for (final EObject subModel : models) {
					final URI subModelURI = URI.createURI("archive:" + resourceURI.toString() + "!/" + subModel.eClass().getName() + "-" + Integer.toString(index++) + ".xmi");
					manifest.getModelURIs().add(subModelURI.deresolve(manifestURI).toString());
					try {
						modelService.store(subModel, subModelURI);
					} catch (IOException e1) {
						log.error("Error storing submodel", e1);
					}
				}
				
				try {
					manifest.eResource().save(null);
				} catch (IOException e1) {
					log.error("Error saving manifest", e1);
				}
				return Status.OK_STATUS;
			}
		};
		
		wsJob.schedule();
		try {
			wsJob.join();
		} catch (InterruptedException e1) {
			log.error("Interrupted waiting for ws job to finish", e1);
		}
		
		// by this point we should have detected the new scenario and loaded it as a consequence of the listener above
		return getScenarioInstance(uuid);
	}

	@Override
	public ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) {
		final List<EObject> originalSubModels = new ArrayList<EObject>();
		for (final String subModelURI : original.getSubModelURIs()) {
			
			try {
				final IModelInstance instance = modelService.getModel(URI.createURI(subModelURI));
				originalSubModels.add(instance.getModel());
			} catch (IOException e1) {
				
			}
		}
		
		final Collection<EObject> duppedSubModels = EcoreUtil.copyAll(originalSubModels);
		
		Collection<ScenarioInstance> dependencies = new ArrayList<ScenarioInstance>();
		
		for (final String uuids : original.getDependencyUUIDs()) {
			dependencies.add(getScenarioInstance(uuids));
		}
		
		return insert(destination, dependencies , duppedSubModels);
	}
}
