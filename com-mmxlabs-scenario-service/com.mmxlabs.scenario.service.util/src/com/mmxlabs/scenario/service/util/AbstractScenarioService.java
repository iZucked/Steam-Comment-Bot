/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.conditions.strings.StringValue;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

/**
 * An abstract base class suitable for most scenario services.
 * 
 */
public abstract class AbstractScenarioService extends AbstractScenarioServiceListenerHandler {

	private static final EAttribute uuidAttribute = ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid();

	private final @NonNull String name;
	protected ScenarioService serviceModel;

	protected IScenarioCipherProvider _scenarioCipherProvider;

	private final Queue<Runnable> delayedRunWhenReadyRunnables = new ConcurrentLinkedQueue<>();
	private boolean ready;

	protected AbstractScenarioService(final @NonNull String name) {
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
	public void notifyReady(final Runnable r) {

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
				final Runnable r = delayedRunWhenReadyRunnables.poll();
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
		final @NonNull U child = factory.get();
		RunnerHelper.syncExec(() -> viewInstance.getElements().add(child));
		return child;
	}

	public IScenarioCipherProvider getScenarioCipherProvider() {
		return _scenarioCipherProvider;
	}

	public void setScenarioCipherProvider(final IScenarioCipherProvider scenarioCipherProvider) {
		this._scenarioCipherProvider = scenarioCipherProvider;
	}
}
