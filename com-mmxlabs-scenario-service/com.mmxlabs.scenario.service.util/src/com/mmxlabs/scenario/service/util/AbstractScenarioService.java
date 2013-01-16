/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.conditions.strings.StringValue;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.util.internal.Activator;

/**
 * An abstract base class suitable for most scenario services.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractScenarioService extends AbstractScenarioServiceListenerHandler {
	private static final Logger log = LoggerFactory.getLogger(AbstractScenarioService.class);
	private final String name;
	private ScenarioService serviceModel;
	protected IModelService modelService;
	private static final EAttribute uuidAttribute = ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid();

	private IScenarioMigrationService scenarioMigrationService;

	protected AbstractScenarioService(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public IModelService getModelService() {
		return modelService;
	}

	public void setModelService(final IModelService modelService) {
		this.modelService = modelService;
	}

	// TODO consider replacing these two methods with a faster mapping approach based on a hashtable + adapter
	// to maintain the hashtable's contents.
	@Override
	public boolean exists(final String uuid) {
		return getScenarioInstance(uuid) != null;
	}

	@Override
	public ScenarioInstance getScenarioInstance(final String uuid) {
		final SELECT query = new SELECT(1, new FROM(getServiceModel()), new WHERE(new EObjectAttributeValueCondition(uuidAttribute, new StringValue(uuid))));
		final IQueryResult result = query.execute();
		if (result.isEmpty())
			return null;
		else
			return (ScenarioInstance) result.getEObjects().iterator().next();
	}

	/**
	 * Subclasses should use this method to create the initial service model.
	 * 
	 * @return
	 */
	protected abstract ScenarioService initServiceModel();

	@Override
	public ScenarioService getServiceModel() {
		synchronized (this) {
			if (serviceModel == null) {
				serviceModel = initServiceModel();
				serviceModel.setServiceRef(this);
				serviceModel.setName(getName());
			}
		}
		return serviceModel;
	}

	public abstract URI resolveURI(final String URI);

	@Override
	public EObject load(final ScenarioInstance instance) throws IOException {
		if (instance.getInstance() != null) {
			// log.debug("Instance " + instance.getUuid() + " already loaded");
			return instance.getInstance();
		}

		try {
//			scenarioMigrationService.migrateScenario(this, instance);
		} catch (final Exception e) {
			throw new RuntimeException("Error migrating scenario", e);
		}

		fireEvent(ScenarioServiceEvent.PRE_LOAD, instance);

		log.debug("Instance " + instance.getUuid() + " needs loading");
		final List<EObject> parts = new ArrayList<EObject>();
		final MMXRootObject implementation = MMXCoreFactory.eINSTANCE.createMMXRootObject();

		for (final String uuid : instance.getDependencyUUIDs()) {
			log.debug("Loading dependency " + uuid);
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
						parts.add(depInstance);
					}
				}
			}
		}

		// create MMXRootObject and connect submodel instances into it.
		for (final String subModelURI : instance.getSubModelURIs()) {
			// acquire sub models
			log.debug("Loading submodel from " + subModelURI);
			final IModelInstance modelInstance = modelService.getModel(resolveURI(subModelURI));
			if (modelInstance.getModel() instanceof UUIDObject) {
				implementation.addSubModel((UUIDObject) modelInstance.getModel());
			} else if (modelInstance.getModel() != null) {
				parts.add(modelInstance.getModel());
			} else {
				log.warn("Null value for model instance " + subModelURI);
			}
		}
		parts.add(implementation);
		instance.setInstance(implementation);

		final EditingDomain domain = initEditingDomain(implementation, instance);

		instance.setAdapters(new HashMap<Class<?>, Object>());

		instance.getAdapters().put(EditingDomain.class, domain);
		instance.getAdapters().put(BasicCommandStack.class, (BasicCommandStack) domain.getCommandStack());

		modelService.resolve(parts);

		fireEvent(ScenarioServiceEvent.POST_LOAD, instance);

		return implementation;
	}

	public EditingDomain initEditingDomain(final EObject rootObject, final ScenarioInstance instance) {

		final MMXAdaptersAwareCommandStack commandStack = new MMXAdaptersAwareCommandStack(instance);

		commandStack.addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				instance.setDirty(commandStack.isSaveNeeded());
			}
		});

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker = Activator.getDefault().getCommandProviderTracker();

		// Create the editing domain with a special command stack.
		final MMXRootObject mmxRootObject = (MMXRootObject) rootObject;

		// Assuming there is at least one submodel that it is part of the global resourceset (note this is assuming the current implementation of the ModelService)
		ResourceSet resourceSet = null;
		final Iterator<MMXSubModel> iterator = mmxRootObject.getSubModels().iterator();
		while (iterator.hasNext() && resourceSet == null) {
			final MMXSubModel sub = iterator.next();
			final Resource eResource = sub.getOriginalResource();
			if (eResource != null) {
				resourceSet = eResource.getResourceSet();
			}
		}

		final CommandProviderAwareEditingDomain editingDomain = new CommandProviderAwareEditingDomain(adapterFactory, commandStack, mmxRootObject, commandProviderTracker, resourceSet);

		commandStack.setEditingDomain(editingDomain);

		return editingDomain;

	}

	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.SAVING);
		if (lock.awaitClaim()) {
			try {
				final EObject instance = scenarioInstance.getInstance();
				if (instance == null) {
					return;
				}

				fireEvent(ScenarioServiceEvent.PRE_SAVE, scenarioInstance);

				final List<IModelInstance> models = new ArrayList<IModelInstance>();
				for (final String uris : scenarioInstance.getSubModelURIs()) {
					final IModelInstance modelInstance = modelService.getModel(resolveURI(uris));
					if (modelInstance != null) {
						models.add(modelInstance);
					}
				}
				modelService.saveTogether(models);
				// Update last modified date
				final Metadata metadata = scenarioInstance.getMetadata();
				if (metadata != null) {
					metadata.setLastModified(new Date());
				}
				scenarioInstance.setDirty(false);

				fireEvent(ScenarioServiceEvent.POST_SAVE, scenarioInstance);
			} finally {
				lock.release();
			}
		}
	}

	@Override
	public ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) throws IOException {
		log.debug("Duplicating " + original.getUuid() + " into " + destination);
		final IScenarioService originalService = original.getScenarioService();
		final List<EObject> originalSubModels = new ArrayList<EObject>();

		// Determine whether or not the model is currently loaded. If it is not currently loaded, then attempt to perform a scenario migration before loading the models. If it is loaded, we can assume
		// this process has already been performed.
		final ScenarioInstance cpy;
		final List<File> tmpFiles = new ArrayList<File>();
		try {
			if (original.getInstance() == null) {
				// Not loaded - may need to be migrated!

				// A URIConvertor to handle input/output streams
				final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

				// Create a copy of the data to avoid modifying it unexpectedly. E.g. this could come from a scenario data file on filesystem which should be left unchanged.
				cpy = EcoreUtil.copy(original);
				cpy.getSubModelURIs().clear();
				for (final String subModelURI : original.getSubModelURIs()) {
					final File f = File.createTempFile("migration", "xmi");
					tmpFiles.add(f);
					// Create a temp file and generate a URI to it to pass into migration code.
					final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
					final URI sourceURI = originalService == null ? URI.createURI(subModelURI) : originalService.resolveURI(subModelURI);
					copyURIData(uc, sourceURI, tmpURI);
					cpy.getSubModelURIs().add(tmpURI.toString());
				}

				// Perform the migration!
				try {
//					scenarioMigrationService.migrateScenario(this, cpy);
				} catch (final Exception e) {
					throw new RuntimeException("Error migrating scenario", e);
				}
			} else {
				// Already loaded? Just use the same instance.
				cpy = original;
			}

			// Remember instances which were not loaded originally and unload them after use.
			final Set<IModelInstance> instancesToUnload = new HashSet<IModelInstance>();

			for (final String subModelURI : cpy.getSubModelURIs()) {
				log.debug("Loading submodel " + subModelURI);
				try {
					final URI realURI = originalService == null ? URI.createURI(subModelURI) : originalService.resolveURI(subModelURI);

					final IModelInstance instance = modelService.getModel(realURI);
					if (!instance.isLoaded()) {
						instancesToUnload.add(instance);
					}
					// This will trigger a model load if required.
					originalSubModels.add(instance.getModel());
				} catch (final IOException e1) {
					log.error("IO Exception loading model from " + subModelURI, e1);
				}
			}

			final Collection<EObject> duppedSubModels = EcoreUtil.copyAll(originalSubModels);

			final Collection<ScenarioInstance> dependencies = new ArrayList<ScenarioInstance>();

			for (final String uuids : cpy.getDependencyUUIDs()) {
				dependencies.add(getScenarioInstance(uuids));
			}

			final ScenarioInstance dup = insert(destination, dependencies, duppedSubModels);
			// Copy across various bits of information
			dup.getMetadata().setContentType(cpy.getMetadata().getContentType());
			dup.getMetadata().setCreated(cpy.getMetadata().getCreated());
			dup.getMetadata().setLastModified(new Date());
			dup.setName(cpy.getName());

			// Clean up
			for (final IModelInstance toUnload : instancesToUnload) {
				toUnload.unload();
				toUnload.dispose();
			}
			return dup;
		} finally {
			// Clean up tmp files used for migration.
			for (final File tmpFile : tmpFiles) {
				tmpFile.delete();
			}
		}
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void unload(final ScenarioInstance instance) {
		if (instance.getInstance() == null) {
			return;
		}

		fireEvent(ScenarioServiceEvent.PRE_UNLOAD, instance);

		instance.getAdapters().remove(EditingDomain.class);
		instance.getAdapters().remove(BasicCommandStack.class);

		// create MMXRootObject and connect submodel instances into it.
		for (final String subModelURI : instance.getSubModelURIs()) {
			// acquire sub models
			log.debug("Unloading submodel from " + subModelURI);
			try {
				final IModelInstance modelInstance = modelService.getModel(resolveURI(subModelURI));
				modelInstance.unload();
			} catch (final IOException e) {
				e.printStackTrace();
				// ignore as we are unloading
			}
		}

		final MMXRootObject rootObject = (MMXRootObject) instance.getInstance();
		rootObject.getSubModels().clear();
		rootObject.getProxies().clear();

		instance.setInstance(null);

		fireEvent(ScenarioServiceEvent.POST_UNLOAD, instance);
	}

	/**
	 * @since 3.0
	 */
	public IScenarioMigrationService getScenarioMigrationService() {
		return scenarioMigrationService;
	}

	/**
	 * @since 3.0
	 */
	public void setScenarioMigrationService(final IScenarioMigrationService scenarioMigrationHandler) {
		this.scenarioMigrationService = scenarioMigrationHandler;
	}

	private void copyURIData(final URIConverter uc, final URI src, final URI dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			// Get input stream from original URI
			is = uc.createInputStream(src);

			os = uc.createOutputStream(dest);

			// Copy XMI file contents
			// TODO: Tweak buffer size
			// TODO: Java 7 APIs?
			final byte[] buf = new byte[4096];
			int c;
			while ((c = is.read(buf)) > 0) {
				os.write(buf, 0, c);
			}

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final Exception e) {

				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (final Exception e) {

				}
			}
		}
	}
}
