/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.EventObject;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.IScenarioMigrationService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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

	protected IScenarioMigrationService scenarioMigrationService;
	protected IScenarioCipherProvider _scenarioCipherProvider;

	private Queue<Runnable> delayedRunWhenReadyRunnables = new ConcurrentLinkedQueue<>();
	private boolean ready;

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

	/**
	 * Create a {@link ResourceSet} for loading and saving
	 * 
	 */
	protected @NonNull ResourceSet createResourceSet() {
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(getScenarioCipherProvider());
		return resourceSet;
	}

	protected Pair<@NonNull CommandProviderAwareEditingDomain, @NonNull MMXAdaptersAwareCommandStack> initEditingDomain(final ResourceSet resourceSet, final EObject rootObject,
			final ScenarioInstance instance) {

		final MMXAdaptersAwareCommandStack commandStack = new MMXAdaptersAwareCommandStack(instance);

		commandStack.addCommandStackListener(new CommandStackListener() {

			@Override
			public void commandStackChanged(final EventObject event) {
				// instance.setDirty(commandStack.isSaveNeeded());
			}
		});

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		final MMXRootObject mmxRootObject = (MMXRootObject) rootObject;

		final CommandProviderAwareEditingDomain editingDomain = new CommandProviderAwareEditingDomain(adapterFactory, commandStack, mmxRootObject, resourceSet);

		commandStack.setEditingDomain(editingDomain);

		return new Pair<>(editingDomain, commandStack);

	}
	//
	// @Override
	// public void save(final ScenarioInstance scenarioInstance) throws IOException {
	// final EObject instance = scenarioInstance.getInstance();
	// if (instance == null) {
	// return;
	// }
	//
	// final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.SAVING);
	// try (final ModelReference modelReference = scenarioInstance.getReference()) {
	// if (lock.awaitClaim()) {
	//
	// fireEvent(ScenarioServiceEvent.PRE_SAVE, scenarioInstance);
	//
	// final MMXRootObject rootObject = (MMXRootObject) modelReference.getInstance();
	// final Resource eResource = rootObject.eResource();
	// ResourceHelper.saveResource(eResource);
	//
	// // Update last modified date
	// final Metadata metadata = scenarioInstance.getMetadata();
	// if (metadata != null) {
	// metadata.setLastModified(new Date());
	// }
	//
	// final BasicCommandStack commandStack = (BasicCommandStack) scenarioInstance.getAdapters().get(BasicCommandStack.class);
	// if (commandStack != null) {
	// commandStack.saveIsDone();
	// }
	//
	// scenarioInstance.setDirty(false);
	//
	// fireEvent(ScenarioServiceEvent.POST_SAVE, scenarioInstance);
	// }
	// } finally {
	// lock.release();
	// }
	// }

	// @Override
	// public ScenarioInstance duplicate(final @NonNull ScenarioInstance original, final @NonNull Container destination, final @NonNull String newName) throws Exception {
	// log.debug("Duplicating " + original.getUuid() + " into " + destination);
	// // final IScenarioService originalService = original.getScenarioService();
	// // final IScenarioService originalService = SSDataManager.Instance.findScenarioService(original);
	//
	// // Determine whether or not the model is currently loaded. If it is not currently loaded, then attempt to perform a scenario migration before loading the models. If it is loaded, we can assume
	// // this process has already been performed.
	// // final EObject rootObject;
	//
	// ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(original);
	// try (ModelReference ref = modelRecord.aquireReference()) {
	//
	// // Duplicate the root object data
	// final EObject rootObjectCopy = EcoreUtil.copy(ref.getInstance());
	//
	// // Create the scenario duplicate
	// final ScenarioInstance theDupe = insert(destination, rootObjectCopy, dup -> {
	// // Copy across various bits of information
	// dup.getMetadata().setContentType(original.getMetadata().getContentType());
	// dup.getMetadata().setCreated(original.getMetadata().getCreated());
	// dup.getMetadata().setLastModified(new Date());
	// dup.setName(newName);
	//
	// // Copy version context information
	// dup.setVersionContext(original.getVersionContext());
	// dup.setScenarioVersion(original.getScenarioVersion());
	//
	// dup.setClientVersionContext(original.getClientVersionContext());
	// dup.setClientScenarioVersion(original.getClientScenarioVersion());
	// });
	//
	// return theDupe;
	// }
	// }

	// /**
	// */
	// @Override
	// public void unload(final ScenarioInstance instance) {
	// if (instance.getInstance() == null) {
	// return;
	// }
	//
	// fireEvent(ScenarioServiceEvent.PRE_UNLOAD, instance);
	//
	// final List<ModelReference> modelReferences = instance.getModelReferences();
	// synchronized (modelReferences) {
	//
	// if (!modelReferences.isEmpty()) {
	// log.error("Attempting to unload a scenario which still has open model references");
	// // return;
	// }
	//
	// if (instance.getAdapters() != null) {
	// instance.getAdapters().remove(EditingDomain.class);
	// instance.getAdapters().remove(CommandStack.class);
	// instance.getAdapters().remove(BasicCommandStack.class);
	//
	// final ResourceSet resourceSet = (ResourceSet) instance.getAdapters().remove(ResourceSet.class);
	// if (resourceSet != null) {
	// for (final Resource r : resourceSet.getResources()) {
	// r.unload();
	// }
	// }
	//
	// instance.getAdapters().clear();
	// }
	// instance.setInstance(null);
	// }
	// fireEvent(ScenarioServiceEvent.POST_UNLOAD, instance);
	// }

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

	@Override
	public void notifyReady(Runnable r) {

		synchronized (delayedRunWhenReadyRunnables) {
			if (isReady()) {
				r.run();
			} else {
				delayedRunWhenReadyRunnables.add(r);
			}
		}
	}

	protected void setReady() {
		synchronized (delayedRunWhenReadyRunnables) {
			ready = true;
			while (!delayedRunWhenReadyRunnables.isEmpty()) {
				Runnable r = delayedRunWhenReadyRunnables.poll();
				r.run();
			}
		}
	}

	protected final boolean isReady() {
		return ready;
	}

	@Override
	public <T extends EObject> void execute(@NonNull final T viewInstance, final Consumer<T> c) {
		RunnerHelper.syncExec(() -> {
			c.accept(viewInstance);
		});
	}

	@Override
	public <T extends EObject> void query(@NonNull final T viewInstance, @NonNull final Consumer<T> c) {
		c.accept(viewInstance);
	}

	@Override
	public <U extends Container> @NonNull U executeAdd(@NonNull final Container viewInstance, @NonNull final Supplier<@NonNull U> factory) {
		@NonNull U child = factory.get();
		RunnerHelper.syncExec(() -> {
			viewInstance.getElements().add(child);
		});
		return child;
	}
}
