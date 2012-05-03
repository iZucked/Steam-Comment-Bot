/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.file;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class FileScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(FileScenarioService.class);

	private static final String PROPERTY_MODEL = "com.mmxlabs.scenario.service.file.model";

	private ResourceSet resourceSet = new ResourceSetImpl();
	private Resource resource;

	private final Map<Object, Object> options;

	private URI storeURI;

	public FileScenarioService() {
		super("File Scenario Service");
		options = new HashMap<Object, Object>();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final Object value = d.get(PROPERTY_MODEL);

		if (value == null) {
			throw new RuntimeException("FileScenarioService: No model URI property set");
		}
		final String modelURIString = value.toString();

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		storeURI = URI.createFileURI(workspaceLocation + modelURIString);
		
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(Path.fromOSString(storeURI.toFileString()));
		if (resource == null) {
			// get first part of it and make project
			final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(modelURIString.split("/")[1]);
			try {
				project.create(null);
				resource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(Path.fromOSString(storeURI.toFileString()));
			} catch (CoreException e) {
				log.error("Exception project folder for store: ", e);
			}
		}
		if (resource != null && resource.getParent().exists() == false) {
			// create folder for resource somehow
			try {
				createFolder(resource.getParent());
			} catch (CoreException e) {
				log.error("Exception creating folder for store: ", e);
			}
		}
	}

	private void createFolder(IResource resource) throws CoreException {
		if (resource.exists()) return;
		createFolder(resource.getParent()); // ensure parent exists
		//create this resource
		if (resource instanceof IProject) {
			((IProject) resource).create(null);
		} else if (resource instanceof IFolder) {
			((IFolder)resource).create(true, true, null);
		}
	}

	public void stop(final ComponentContext context) {
		save();
	}

	public void save() {
		try {
			synchronized (resource) {				
				resource.save(options);
				log.debug("Saved " + resource.getURI());
			}
		} catch (final Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(final ScenarioInstance instance) {
		final EObject container = instance.eContainer();
		final EStructuralFeature containment = instance.eContainingFeature();
		if (container != null && containment != null) {
			if (containment.isMany()) {
				final EList<EObject> value = (EList<EObject>) container.eGet(containment);
				while (value.remove(instance));
			} else {
				container.eSet(containment, null);
			}
		}
		
		// destroy models
		for (final String modelInstanceURI : instance.getSubModelURIs()) {
			try {
				final IModelInstance modelInstance = modelService.getModel(URI.createURI(modelInstanceURI));
				modelInstance.delete();
			} catch (final IOException e) {
				log.error("Whilst deleting instance " + instance.getName() + ", IO exception deleting submodel " + modelInstanceURI, e);
			}
		}
	}

	@Override
	public ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) throws IOException {
		log.debug("Inserting scenario into " + container);
		
		final String uuid = UUID.randomUUID().toString();
		final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
		
		newInstance.setUuid(uuid);
		newInstance.setMetadata(metadata);
		
		for (final ScenarioInstance dependency : dependencies) {
			newInstance.getDependencyUUIDs().add(dependency.getUuid());
		}
		
		int index = 0;
		for (final EObject model : models) {
			URI rel = URI.createURI("../" + uuid + "-" + model.eClass().getName() + "-" + index++ + ".xmi");
			rel = rel.resolve(storeURI);
			log.debug("Storing submodel into " + storeURI);
			try {
				final IModelInstance instance = modelService.store(model, rel);
			} catch (IOException e) {
				return null;
			}
			newInstance.getSubModelURIs().add(rel.toString());
		}
		
		container.getElements().add(newInstance);
		load(newInstance);
		save(newInstance);
		return newInstance;
	}

	final EContentAdapter saveAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			if (notification.isTouch() == false) {
				if (notification.getFeature() instanceof EStructuralFeature && ((EStructuralFeature)notification.getFeature()).isTransient())
					return;
				save();
			}
		}
		
	};
	
	@Override
	protected ScenarioService initServiceModel() {
		resource = resourceSet.createResource(storeURI);
		
		try {
			resource.load(options);
		} catch (final IOException ex) {
			resource.getContents().add(ScenarioServiceFactory.eINSTANCE.createScenarioService());
		}
		
		final ScenarioService result = (ScenarioService) resource.getContents().get(0);
		
		result.setDescription("File scenario service with store " + storeURI);
	
		result.eAdapters().add(saveAdapter);
		
		return result;
	}
}
