/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelRecordScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IDefaultScenarioResultProvider;

public class ScenarioResultImpl implements ScenarioResult {

	private final @NonNull ScenarioModelRecord modelRecord;
	private final @NonNull IScenarioDataProvider scenarioDataProvider;
	private final @NonNull MMXResultRoot resultRoot;
	private final int hash;
	private ScenarioInstance scenarioInstance;

	private static @NonNull MMXResultRoot getDefaultRoot(final @NonNull ScenarioModelRecord modelRecord, final @NonNull IScenarioDataProvider reference) {
		// Make sure it is a valid reference
		// assert reference.getScenarioInstance() == instance;

		final MMXResultRoot[] ref = new MMXResultRoot[1];
		ServiceHelper.withAllServices(IDefaultScenarioResultProvider.class, null, s -> {
			ref[0] = s.getDefaultResult(modelRecord);
			return ref[0] == null;
		});
		final MMXResultRoot mmxResultRoot = ref[0];
		if (mmxResultRoot != null) {
			return mmxResultRoot;
		}
		throw new IllegalArgumentException("No default result root for instance");
	}

	/**
	 * Only intended for unit tests
	 * 
	 * @param modelRecord
	 * @param resultRoot
	 */
	public ScenarioResultImpl(final @NonNull ScenarioModelRecord modelRecord) {
		this.modelRecord = modelRecord;
		this.scenarioDataProvider = new ModelRecordScenarioDataProvider(modelRecord);
		this.resultRoot = getDefaultRoot(modelRecord, this.scenarioDataProvider);
		this.hash = Objects.hash(modelRecord, resultRoot);
	}

	/**
	 * Only intended for unit tests
	 * 
	 * @param modelRecord
	 * @param resultRoot
	 */
	public ScenarioResultImpl(final @NonNull ScenarioModelRecord modelRecord, @NonNull final MMXResultRoot resultRoot) {
		this.modelRecord = modelRecord;
		this.resultRoot = resultRoot;
		this.hash = Objects.hash(modelRecord, resultRoot);
		this.scenarioDataProvider = new ModelRecordScenarioDataProvider(modelRecord);
	}

	public ScenarioResultImpl(final @NonNull ScenarioInstance instance, @NonNull final MMXResultRoot resultRoot) {
		this(SSDataManager.Instance.getModelRecordChecked(instance), resultRoot);
		this.scenarioInstance = instance;
	}

	public ScenarioResultImpl(@NonNull final ScenarioInstance instance) {
		this(SSDataManager.Instance.getModelRecordChecked(instance));
		this.scenarioInstance = instance;
	}

	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return scenarioDataProvider;
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.scenarioDataProvider != null) {
			this.scenarioDataProvider.close();
		}
		super.finalize();
	}

	@SuppressWarnings("null")
	public <T extends MMXRootObject> @Nullable T getTypedRoot(final @NonNull Class<T> cls) {
		return scenarioDataProvider.getTypedScenario(cls);
	}

	public MMXRootObject getRootObject() {
		return scenarioDataProvider.getTypedScenario(MMXRootObject.class);
	}

	public MMXResultRoot getResultRoot() {
		return resultRoot;
	}

	public <T extends MMXResultRoot> @Nullable T getTypedResult(final Class<T> cls) {
		return cls.cast(resultRoot);
	}

	public @NonNull ScenarioModelRecord getModelRecord() {
		return modelRecord;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ScenarioResultImpl) {
			final ScenarioResultImpl other = (ScenarioResultImpl) obj;
			// return Objects.equals(this.modelRecord, other.modelRecord) &&
			// Objects.equals(this.resultRoot, other.resultRoot);
			// FIXME: ScenarioInstance is optional, but should be part of equals. Some code
			// does equality checks on the scenario instance (i.e. UI)
			return this.modelRecord == other.modelRecord && Objects.equals(this.resultRoot, other.resultRoot);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}
}