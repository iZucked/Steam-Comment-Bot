/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
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
		super("Local Scenarios");
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
	}

	public void stop(final ComponentContext context) {
		save();
	}

	public void save() {
		try {
			synchronized (resource) {
				resource.save(options);
				// log.debug("Saved " + resource.getURI());
			}
		} catch (final Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(final Container container) {
		final IScenarioService scenarioService = container.getScenarioService();
		if (scenarioService != null && scenarioService != this) {
			throw new IllegalArgumentException("Cannot delete a container instance that belongs to another scenario service");
		}
		{
			while (container.getElements().isEmpty() == false) {
				delete(container.getElements().get(0));
			}
		}

		final EObject parent = container.eContainer();
		if (parent != null) {
			final EStructuralFeature containment = container.eContainingFeature();
			if (containment != null) {
				if (containment.isMany()) {
					final EList<EObject> value = (EList<EObject>) parent.eGet(containment);
					while (value.remove(container))
						;
				} else {
					parent.eSet(containment, null);
				}
			}
		}

		// destroy models, if there are any
		if (container instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) container;

			unload(instance);

			if (scenarioService != null) {
				fireEvent(ScenarioServiceEvent.PRE_DELETE, instance);
			}
			for (final String modelInstanceURI : instance.getSubModelURIs()) {
				try {
					final IModelInstance modelInstance = modelService.getModel(resolveURI(modelInstanceURI));
					modelInstance.delete();
				} catch (final IOException e) {
					log.error("Whilst deleting instance " + instance.getName() + ", IO exception deleting submodel " + modelInstanceURI, e);
				}
			}

			if (scenarioService != null) {
				try {
					final Resource resource = resourceSet.createResource(resolveURI("instances/" + instance.getUuid() + ".xmi"));
					resource.delete(null);
					resourceSet.getResources().remove(resource);
				} catch (final Throwable th) {
				}
				fireEvent(ScenarioServiceEvent.POST_DELETE, instance);
			}
		}
	}

	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		// store manifest
		saveManifest(scenarioInstance);

		super.save(scenarioInstance);
	}

	private void saveManifest(final ScenarioInstance scenarioInstance) {
		try {
			final Resource resource = resourceSet.createResource(resolveURI("instances/" + scenarioInstance.getUuid() + ".xmi"));
			final ScenarioInstance copy = EcoreUtil.copy(scenarioInstance);
			resource.getContents().add(copy);
			resource.save(null);
			resourceSet.getResources().remove(resource);
		} catch (final Throwable th) {
		}
	}

	@Override
	public ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) throws IOException {
		log.debug("Inserting scenario into " + container);

		// Create new model nodes
		final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();

		// Create a new UUID
		final String uuid = UUID.randomUUID().toString();
		newInstance.setUuid(uuid);

		newInstance.setMetadata(metadata);

		// Copy dependency UUIDs - i.e. other scenario instances
		for (final ScenarioInstance dependency : dependencies) {
			newInstance.getDependencyUUIDs().add(dependency.getUuid());
		}

		// Construct new URIs into the model service for our models.
		int index = 0;
		final List<IModelInstance> modelInstances = new ArrayList<IModelInstance>();
		for (final EObject model : models) {
			// Construct internal URI based on UUID and model class name
			final String uriString = "./" + uuid + "-" + model.eClass().getName() + "-" + index++ + ".xmi";
			final URI resolved = resolveURI(uriString);
			log.debug("Storing submodel into " + resolved);
			try {
				// "Store" - map URI to model instance - this has not persisted the model yet
				final IModelInstance instance = modelService.store(model, resolved);
				// Record model instance for later
				modelInstances.add(instance);
			} catch (IOException e) {
				return null;
			}
			// Record new submodel URI
			newInstance.getSubModelURIs().add(uriString);
		}

		// Persist all the model at once.
		modelService.saveTogether(modelInstances);

		// Update last modified date
		metadata.setLastModified(new Date());

		// Save the scenario instance to a file for recovery
		saveManifest(newInstance);

		// Clear dirty flag
		newInstance.setDirty(false);

		// Finally add to node in the service model.
		container.getElements().add(newInstance);

		return newInstance;
	}

	final EContentAdapter saveAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			if (notification.isTouch() == false) {
				if (notification.getFeature() instanceof EStructuralFeature && ((EStructuralFeature) notification.getFeature()).isTransient())
					return;
				save();
			}
		}

	};

	@Override
	protected ScenarioService initServiceModel() {
		resource = resourceSet.createResource(storeURI);
		boolean resourceExisted = false;
		try {
			resource.load(options);
			resourceExisted = true;
		} catch (final IOException ex) {

		}

		if (resource.getContents().isEmpty()) {
			resource.getContents().add(ScenarioServiceFactory.eINSTANCE.createScenarioService());
			if (resourceExisted) {
				// consider loading backup?
			}
		} else {
			// back-up resource
			try {
				log.debug("Backing up " + storeURI);
				final InputStream inputStream = resourceSet.getURIConverter().createInputStream(storeURI);
				try {
					final OutputStream outputStream = resourceSet.getURIConverter().createOutputStream(URI.createURI(storeURI.toString() + ".backup"));
					int x;
					while ((x = inputStream.read()) != -1) {
						outputStream.write(x);
					}
					outputStream.close();
				} finally {
					inputStream.close();
				}

			} catch (IOException e) {

			}
		}

		final ScenarioService result = (ScenarioService) resource.getContents().get(0);

		result.setDescription("File scenario service with store " + storeURI);

		// modify any old scenarios to fix wrong pointing
		makeRelativeURIs(result);
		recoverLostScenarios(result);

		result.eAdapters().add(saveAdapter);

		return result;
	}

	/**
	 * Simple method to recover any lost scenarios (those which are in the instances/ dir, but not in the scenario service)
	 * 
	 * @param result
	 */
	private void recoverLostScenarios(final ScenarioService result) {
		// gather up all UUIDs
		final HashSet<String> allUUIDs = new HashSet<String>();
		final TreeIterator<EObject> iterator = result.eAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof ScenarioInstance) {
				allUUIDs.add(((ScenarioInstance) o).getUuid());
			}
		}
		final Folder lostAndFound = ScenarioServiceFactory.eINSTANCE.createFolder();
		lostAndFound.setName("Lost & Found");
		// now look for all instances in the spare dir which don't have UUIDs
		final File f = new File(resolveURI("instances/").toFileString());
		if (f.exists() && f.isDirectory()) {
			final HashMap<String, ScenarioInstance> recoveredInstances = new HashMap<String, ScenarioInstance>();
			final HashSet<String> recoveredSubInstances = new HashSet<String>();
			for (final File instanceFile : f.listFiles()) {
				if (instanceFile.getName().endsWith(".xmi")) {
					final String instanceUUID = instanceFile.getName().substring(0, instanceFile.getName().length() - 4);
					if (!allUUIDs.contains(instanceUUID)) {
						log.warn("Recovering instance " + instanceUUID);
						// recover the instance in f, if possible
						try {
							final Resource resource = resourceSet.createResource(URI.createFileURI(instanceFile.getAbsolutePath()));
							resource.load(null);
							final EObject o = resource.getContents().get(0);
							if (o instanceof ScenarioInstance) {
								final ScenarioInstance theInstance = (ScenarioInstance) o;
								final TreeIterator<EObject> contentIterator = o.eAllContents();
								while (contentIterator.hasNext()) {
									final EObject sub = contentIterator.next();
									if (sub instanceof ScenarioInstance) {
										final ScenarioInstance subInstance = (ScenarioInstance) sub;
										if (recoveredInstances.containsKey(subInstance.getUuid())) {
											recoveredInstances.remove(subInstance.getUuid());
										}
										recoveredSubInstances.add(subInstance.getUuid());
									}
								}
								if (!recoveredSubInstances.contains(theInstance.getUuid())) {
									recoveredInstances.put(theInstance.getUuid(), theInstance);
								}

							}
							resourceSet.getResources().remove(resource);
						} catch (final Throwable th) {

						}
					}
				}
			}
			lostAndFound.getElements().addAll(recoveredInstances.values());
		}
		if (lostAndFound.getElements().size() > 0) {
			result.getElements().add(lostAndFound);
		}
	}

	private void makeRelativeURIs(final Container container) {
		if (container instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) container;

			for (int index = 0; index < instance.getSubModelURIs().size(); index++) {
				final String uriString = instance.getSubModelURIs().get(index);
				final URI uri = URI.createURI(uriString);
				if (uri.isRelative() == false) {
					final URI derezzed = uri.deresolve(storeURI);
					if (derezzed.isRelative()) {
						instance.getSubModelURIs().set(index, derezzed.toString());
					}
				}
			}
		}
		for (final Container c : container.getElements()) {
			makeRelativeURIs(c);
		}
	}

	@Override
	public URI resolveURI(final String uriString) {
		final URI uri = URI.createURI(uriString);
		if (uri.isRelative()) {
			return uri.resolve(storeURI);
		} else {
			return uri;
		}
	}
}
