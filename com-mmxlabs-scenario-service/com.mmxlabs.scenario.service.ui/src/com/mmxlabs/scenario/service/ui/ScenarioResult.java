/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXResultRoot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class ScenarioResult {

	private final ScenarioInstance instance;
	private @NonNull final MMXResultRoot resultRoot;
	private final int hash;
	private final ModelReference modelReference;

	public static @NonNull MMXResultRoot getDefaultRoot(final ScenarioInstance instance, final ModelReference reference) {
		// Make sure it is a valid reference
		// assert reference.getScenarioInstance() == instance;

		final MMXResultRoot[] ref = new MMXResultRoot[1];
		ServiceHelper.withAllServices(IDefaultScenarioResultProvider.class, null, s -> {
			ref[0] = s.getDefaultResult(instance);
			return ref[0] == null;
		});
		final MMXResultRoot result = ref[0];
		if (result != null) {
			return result;
		}
		throw new IllegalArgumentException("No default result root for instance");
	}

	public ScenarioResult(final @NonNull ScenarioInstance instance) {
		this.instance = instance;
		final @NonNull ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
		this.modelReference = modelRecord.aquireReference("ScenarioResult:1");
		this.resultRoot = getDefaultRoot(instance, modelReference);
		this.hash = Objects.hash(instance, resultRoot);
	}

	public ScenarioResult(final @NonNull ScenarioInstance instance, @NonNull final MMXResultRoot resultRoot) {
		this.instance = instance;
		this.resultRoot = resultRoot;
		this.hash = Objects.hash(instance, resultRoot);
		@NonNull
		ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
		this.modelReference = modelRecord.aquireReference("ScenarioResult:2");
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.modelReference != null) {
			this.modelReference.close();
		}
		super.finalize();
	}

	public <T extends MMXRootObject> @Nullable T getTypedRoot(final Class<T> cls) {
		return cls.cast(modelReference.getInstance());
	}

	public MMXRootObject getRootObject() {
		return (MMXRootObject) modelReference.getInstance();
	}

	public MMXResultRoot getResultRoot() {
		return resultRoot;
	}

	public <T extends MMXResultRoot> @Nullable T getTypedResult(final Class<T> cls) {
		return cls.cast(resultRoot);
	}

	public ScenarioInstance getScenarioInstance() {
		return instance;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ScenarioResult) {
			final ScenarioResult other = (ScenarioResult) obj;
			return Objects.equals(this.instance, other.instance) && Objects.equals(this.resultRoot, other.resultRoot);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}
}