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

import com.mmxlabs.common.io.FileDeleter;
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

	private final ResourceSet resourceSet = new ResourceSetImpl();
	private Resource resource;

	private final Map<Object, Object> options;

	private URI storeURI;

	public FileScenarioService() {
		super("My Scenarios");
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
			// Recursively delete contents
			while (container.getElements().isEmpty() == false) {
				delete(container.getElements().get(0));
			}
		}

		// Remove container ref from it's parent
		final EObject parent = container.eContainer();
		if (parent != null) {
			final EStructuralFeature containment = container.eContainingFeature();
			if (containment != null) {
				if (containment.isMany()) {
					@SuppressWarnings("unchecked")
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

			// Unload scenario prior to deletion
			unload(instance);

			if (scenarioService != null) {
				fireEvent(ScenarioServiceEvent.PRE_DELETE, instance);
			}

			// Find a resource set
			ResourceSet instanceResourceSet = null;
			if (instance.getAdapters() != null) {
				// As we have unloaded, we do not expect to get here...
				instanceResourceSet = (ResourceSet) instance.getAdapters().get(ResourceSet.class);
			}
			if (instanceResourceSet == null) {
				instanceResourceSet = createResourceSet();
			}

			// Create or re-use a Resource - again after unloading we should probably always we creating a new resource
			final URI rooObjectURI = resolveURI(instance.getRootObjectURI());
			if (instanceResourceSet.getResource(rooObjectURI, false) == null) {
				instanceResourceSet.createResource(rooObjectURI);
			}

			// Delete the scenario
			// Copy list as delete will remove it from the resource set
			for (final Resource r : new ArrayList<Resource>(instanceResourceSet.getResources())) {
				try {
					r.unload();
					instanceResourceSet.getResources().remove(r);
					final URI scenaruiURI = resolveURI(instance.getUuid() + ".xmi");
					if (scenaruiURI.isFile()) {
						FileDeleter.delete(new File(scenaruiURI.toFileString()));
					} else {
						log.warn("Unable to securely delete scenario - " + scenaruiURI.toString());
						r.delete(null);
					}
				} catch (final IOException e) {
					log.error("Whilst deleting instance " + instance.getName() + ", IO exception deleting submodel " + r.getURI(), e);
				}
			}

			// Delete backup metadata
			if (scenarioService != null) {
				try {
					final Resource resource = resourceSet.createResource(resolveURI("instances/" + instance.getUuid() + ".xmi"));

					instanceResourceSet.getResources().remove(resource);
					final URI metadataURI = resolveURI("instances/" + instance.getUuid() + ".xmi");
					if (metadataURI.isFile()) {
						FileDeleter.delete(new File(metadataURI.toFileString()));
					} else {
						resource.delete(null);
					}
					resourceSet.getResources().remove(resource);
				} catch (final Throwable th) {
				}
				fireEvent(ScenarioServiceEvent.POST_DELETE, instance);
			}
		}
	}

	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		// store backup manifest
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
	public ScenarioInstance insert(final Container container, final EObject rootObject) throws IOException {
		log.debug("Inserting scenario into " + container);

		// Create new model nodes
		final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();

		// Create a new UUID
		final String uuid = UUID.randomUUID().toString();
		newInstance.setUuid(uuid);

		newInstance.setMetadata(metadata);

		// Construct new URIs into the model service for our models.
		final ResourceSet instanceResourceSet = createResourceSet();
		{
			// Construct internal URI based on UUID and model class name
			final String uriString = "./" + uuid + ".xmi";
			final URI resolved = resolveURI(uriString);
			log.debug("Storing submodel into " + resolved);
			try {
				final Resource instanceResource = instanceResourceSet.createResource(resolved);
				instanceResource.getContents().add(rootObject);
				// "Store" - map URI to model instance
				instanceResource.save(null);
				// Unload instance from memory as no longer needed
				instanceResource.unload();
			} catch (final IOException e) {
				return null;
			}
			// Record new submodel URI
			newInstance.setRootObjectURI(uriString);
		}

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
		public void notifyChanged(final Notification notification) {
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

			} catch (final IOException e) {

			}
		}

		final ScenarioService result = (ScenarioService) resource.getContents().get(0);

		result.setDescription("File scenario service with store " + storeURI);

		// modify any old scenarios to fix wrong pointing
		makeRelativeURIs(result);
		recoverLostScenarios(result);

		result.eAdapters().add(saveAdapter);

		result.setSupportsForking(true);
		result.setSupportsImport(true);

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

			{
				final String uriString = instance.getRootObjectURI();
				final URI uri = URI.createURI(uriString);
				if (uri.isRelative() == false) {
					final URI derezzed = uri.deresolve(storeURI);
					if (derezzed.isRelative()) {
						instance.setRootObjectURI(derezzed.toString());
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

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		destination.getElements().addAll(elements);
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		if (parent instanceof ScenarioInstance) {
			return;
		}
		final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
		f.setName(name);
		parent.getElements().add(f);
	}
}
