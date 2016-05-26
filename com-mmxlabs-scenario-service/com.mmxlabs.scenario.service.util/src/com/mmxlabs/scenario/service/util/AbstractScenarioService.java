/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

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
	// protected IModelService modelService;
	private static final EAttribute uuidAttribute = ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid();

	private IScenarioMigrationService scenarioMigrationService;
	private IScenarioCipherProvider _scenarioCipherProvider;

	protected AbstractScenarioService(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
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

	@Override
	public abstract URI resolveURI(final String URI);

	@Override
	public EObject load(final ScenarioInstance instance) throws IOException {
		if (instance.getInstance() != null) {
			// log.debug("Instance " + instance.getUuid() + " already loaded");
			return instance.getInstance();
		}
		// Wrap in try-with-resources block on model reference to ensure there is no unload attempt while we are still loading. Do not call ModelReference#getInstance() otherwise we will likely end up
		// looping.
		try (final ModelReference modelReference = instance.getReference()) {
			if (scenarioMigrationService != null) {
				try {
					scenarioMigrationService.migrateScenario(this, instance);
				} catch (final RuntimeException e) {
					throw e;
				} catch (final Exception e) {
					throw new RuntimeException("Error migrating scenario", e);
				}
			}

			fireEvent(ScenarioServiceEvent.PRE_LOAD, instance);

			log.debug("Instance " + instance.getName() + " (" + instance.getUuid() + ") needs loading");

			// create MMXRootObject and connect submodel instances into it.
			final ResourceSet resourceSet = createResourceSet();

			final String rooObjectURI = instance.getRootObjectURI();
			// acquire sub models
			log.debug("Loading rootObject from " + rooObjectURI);
			final URI uri = resolveURI(rooObjectURI);

			final Resource resource = ResourceHelper.loadResource(resourceSet, uri);
			final EObject implementation = resource.getContents().get(0);

			if (implementation == null) {
				throw new IOException("Null value for model instance " + rooObjectURI);
			}
			instance.setInstance(implementation);

			instance.setAdapters(new HashMap<Class<?>, Object>());
			instance.getAdapters().put(ResourceSet.class, resourceSet);

			final EditingDomain domain = initEditingDomain(resourceSet, implementation, instance);
			instance.getAdapters().put(EditingDomain.class, domain);

			// Register under both interfaces
			instance.getAdapters().put(CommandStack.class, domain.getCommandStack());
			instance.getAdapters().put(BasicCommandStack.class, domain.getCommandStack());

			fireEvent(ScenarioServiceEvent.POST_LOAD, instance);
		}
		return instance.getInstance();
	}

	/**
	 * Create a {@link ResourceSet} for loading and saving
	 * 
	 */
	protected ResourceSet createResourceSet() {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(getScenarioCipherProvider());
		return resourceSet;
	}

	private EditingDomain initEditingDomain(final ResourceSet resourceSet, final EObject rootObject, final ScenarioInstance instance) {

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

		// Create the editing domain with a special command stack.
		final MMXRootObject mmxRootObject = (MMXRootObject) rootObject;

		final CommandProviderAwareEditingDomain editingDomain = new CommandProviderAwareEditingDomain(adapterFactory, commandStack, mmxRootObject, resourceSet);

		commandStack.setEditingDomain(editingDomain);

		return editingDomain;

	}

	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		final EObject instance = scenarioInstance.getInstance();
		if (instance == null) {
			return;
		}

		final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.SAVING);
		try (final ModelReference modelReference = scenarioInstance.getReference()) {
			if (lock.awaitClaim()) {

				fireEvent(ScenarioServiceEvent.PRE_SAVE, scenarioInstance);

				final MMXRootObject rootObject = (MMXRootObject) modelReference.getInstance();
				final Resource eResource = rootObject.eResource();
				ResourceHelper.saveResource(eResource);

				// Update last modified date
				final Metadata metadata = scenarioInstance.getMetadata();
				if (metadata != null) {
					metadata.setLastModified(new Date());
				}

				final BasicCommandStack commandStack = (BasicCommandStack) scenarioInstance.getAdapters().get(BasicCommandStack.class);
				if (commandStack != null) {
					commandStack.saveIsDone();
				}

				scenarioInstance.setDirty(false);

				fireEvent(ScenarioServiceEvent.POST_SAVE, scenarioInstance);
			}
		} finally {
			lock.release();
		}
	}

	@Override
	public ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) throws IOException {
		log.debug("Duplicating " + original.getUuid() + " into " + destination);
		final IScenarioService originalService = original.getScenarioService();

		// Determine whether or not the model is currently loaded. If it is not currently loaded, then attempt to perform a scenario migration before loading the models. If it is loaded, we can assume
		// this process has already been performed.
		final ScenarioInstance cpy;
		final List<File> tmpFiles = new ArrayList<File>();
		try {
			boolean unloadScenario = false;
			final EObject rootObject;

			if (original.getInstance() == null) {
				// Not loaded - may need to be migrated!

				// A URIConvertor to handle input/output streams
				final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();

				// Create a copy of the data to avoid modifying it unexpectedly. E.g. this could come from a scenario data file on filesystem which should be left unchanged.
				cpy = EcoreUtil.copy(original);
				{
					final String subModelURI = original.getRootObjectURI();
					final File f = File.createTempFile("migration", ".xmi");
					tmpFiles.add(f);
					// Create a temp file and generate a URI to it to pass into migration code.
					final URI tmpURI = URI.createFileURI(f.getCanonicalPath());
					assert tmpURI != null;
					final URI sourceURI = originalService == null ? URI.createURI(subModelURI) : originalService.resolveURI(subModelURI);
					assert sourceURI != null;
					copyURIData(uc, sourceURI, tmpURI);
					cpy.setRootObjectURI(tmpURI.toString());
				}

				// Perform the migration!
				if (scenarioMigrationService != null) {
					try {
						scenarioMigrationService.migrateScenario(this, cpy);
					} catch (final RuntimeException e) {
						throw e;
					} catch (final Exception e) {
						throw new RuntimeException("Error migrating scenario", e);
					}
				}

				// Load the model so we can copy it
				final ResourceSet resourceSet = createResourceSet();

				final String rooObjectURI = cpy.getRootObjectURI();
				// acquire sub models
				log.debug("Loading rootObject from " + rooObjectURI);
				final URI uri = resolveURI(rooObjectURI);
				final Resource resource = ResourceHelper.loadResource(resourceSet, uri);
				rootObject = resource.getContents().get(0);

				unloadScenario = true;
			} else {
				// Already loaded? Just use the same instance.
				cpy = original;
				rootObject = original.getInstance();
			}

			// Duplicate the root object data
			final EObject rootObjectCopy = EcoreUtil.copy(rootObject);

			// Create the scenario duplicate
			final ScenarioInstance dup = insert(destination, rootObjectCopy);

			// Copy across various bits of information
			dup.getMetadata().setContentType(cpy.getMetadata().getContentType());
			dup.getMetadata().setCreated(cpy.getMetadata().getCreated());
			dup.getMetadata().setLastModified(new Date());
			dup.setName(cpy.getName());

			// Copy version context information
			dup.setVersionContext(cpy.getVersionContext());
			dup.setScenarioVersion(cpy.getScenarioVersion());

			dup.setClientVersionContext(cpy.getClientVersionContext());
			dup.setClientScenarioVersion(cpy.getClientScenarioVersion());

			// Clean up
			if (unloadScenario) {
				unload(original);
			}
			return dup;
		} finally {
			// Clean up tmp files used for migration.
			for (final File tmpFile : tmpFiles) {
				FileDeleter.delete(tmpFile);
			}
		}
	}

	/**
	 */
	@Override
	public void unload(final ScenarioInstance instance) {
		if (instance.getInstance() == null) {
			return;
		}

		fireEvent(ScenarioServiceEvent.PRE_UNLOAD, instance);

		List<ModelReference> modelReferences = instance.getModelReferences();
		synchronized (modelReferences) {

			if (!modelReferences.isEmpty()) {
				log.error("Attempting to unload a scenario which still has open model references");
				// return;
			}

			if (instance.getAdapters() != null) {
				instance.getAdapters().remove(EditingDomain.class);
				instance.getAdapters().remove(CommandStack.class);
				instance.getAdapters().remove(BasicCommandStack.class);

				final ResourceSet resourceSet = (ResourceSet) instance.getAdapters().remove(ResourceSet.class);
				if (resourceSet != null) {
					for (final Resource r : resourceSet.getResources()) {
						r.unload();
					}
				}

				instance.getAdapters().clear();
			}
			instance.setInstance(null);
		}
		fireEvent(ScenarioServiceEvent.POST_UNLOAD, instance);
	}

	/**
	 */
	public IScenarioMigrationService getScenarioMigrationService() {
		return scenarioMigrationService;
	}

	/**
	 */
	public void setScenarioMigrationService(final IScenarioMigrationService scenarioMigrationHandler) {
		this.scenarioMigrationService = scenarioMigrationHandler;
	}

	public IScenarioCipherProvider getScenarioCipherProvider() {
		return _scenarioCipherProvider;
	}

	public void setScenarioCipherProvider(final IScenarioCipherProvider scenarioCipherProvider) {
		this._scenarioCipherProvider = scenarioCipherProvider;
	}

	/**
	 */
	@SuppressWarnings("resource")
	protected void copyURIData(@NonNull final URIConverter uc, @NonNull final URI src, @NonNull final URI dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			// Get input stream from original URI
			is = uc.createInputStream(src);
			os = uc.createOutputStream(dest);

			ByteStreams.copy(is, os);

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {

				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (final IOException e) {

				}
			}
		}
	}
}
