/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
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
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * An abstract base class suitable for most scenario services.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractScenarioService implements IScenarioService {
	private static final Logger log = LoggerFactory.getLogger(AbstractScenarioService.class);
	private final String name;
	private ScenarioService serviceModel;
	protected IModelService modelService;
	private static final EAttribute uuidAttribute = ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid();

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

	@Override
	public EObject load(final ScenarioInstance instance) throws IOException {
		if (instance.getInstance() != null) {
			log.debug("Instance " + instance.getUuid() + " already loaded");
			return instance.getInstance();
		}
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
			final IModelInstance modelInstance = modelService.getModel(URI.createURI(subModelURI));
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

		return implementation;
	}

	public EditingDomain initEditingDomain(final EObject rootObject, final ScenarioInstance instance) {
		final BasicCommandStack commandStack = new BasicCommandStack() {
			@Override
			public void execute(final Command command) {
				synchronized (instance) {
					super.execute(command);
				}
			}
		};

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

		return new CommandProviderAwareEditingDomain(adapterFactory, commandStack, mmxRootObject, commandProviderTracker, resourceSet);

	}

	@Override
	public void save(final ScenarioInstance scenarioInstance) throws IOException {
		final EObject instance = scenarioInstance.getInstance();
		if (instance == null) {
			return;
		}
		final List<IModelInstance> models = new ArrayList<IModelInstance>();
		for (final String uris : scenarioInstance.getSubModelURIs()) {
			final IModelInstance modelInstance = modelService.getModel(URI.createURI(uris));
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
	}

	@Override
	public ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) throws IOException {
		log.debug("Duplicating " + original.getUuid() + " into " + destination);
		final List<EObject> originalSubModels = new ArrayList<EObject>();
		for (final String subModelURI : original.getSubModelURIs()) {
			log.debug("Loading submodel " + subModelURI);
			try {
				final IModelInstance instance = modelService.getModel(URI.createURI(subModelURI));
				originalSubModels.add(instance.getModel());
			} catch (final IOException e1) {
				log.error("IO Exception loading model from " + subModelURI, e1);
			}
		}

		final Collection<EObject> duppedSubModels = EcoreUtil.copyAll(originalSubModels);

		final Collection<ScenarioInstance> dependencies = new ArrayList<ScenarioInstance>();

		for (final String uuids : original.getDependencyUUIDs()) {
			dependencies.add(getScenarioInstance(uuids));
		}

		final ScenarioInstance dup = insert(destination, dependencies, duppedSubModels);
		// Copy across various bits of information.
		dup.getMetadata().setContentType(original.getMetadata().getContentType());
		dup.getMetadata().setCreated(original.getMetadata().getCreated());
		dup.getMetadata().setLastModified(new Date());
		dup.setName(original.getName());

		return dup;
	}
}
