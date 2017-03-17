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
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioResult {

	private final ScenarioInstance instance;
	private @NonNull final MMXResultRoot resultRoot;
	private final int hash;
	private final ModelReference modelReference;

	public static @NonNull MMXResultRoot getDefaultRoot(final ScenarioInstance instance, final ModelReference reference) {
		// Make sure it is a valid reference
		assert reference.getScenarioInstance() == instance;

		final MMXResultRoot[] ref = new MMXResultRoot[1];
		ServiceHelper.withAllServices(IDefaultScenarioResultProvider.class, null, s -> {
			ref[0] = s.getDefaultResult(instance);
			return ref[0] == null;
		});
		if (ref[0] != null) {
			return ref[0];
		}
		throw new IllegalArgumentException("No default result root for instance");
	}

	public ScenarioResult(final ScenarioInstance instance) {
		this.instance = instance;
		this.modelReference = instance.getReference("ScenarioResult:1");
		this.resultRoot = getDefaultRoot(instance, modelReference);
		this.hash = Objects.hash(instance, resultRoot);
	}

	public ScenarioResult(final ScenarioInstance instance, @NonNull final MMXResultRoot resultRoot) {
		this.instance = instance;
		this.resultRoot = resultRoot;
		this.hash = Objects.hash(instance, resultRoot);
		this.modelReference = instance.getReference("ScenarioResult:2");
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.modelReference != null) {
			this.modelReference.close();
		}
		super.finalize();
	}

	public <T extends MMXRootObject> @Nullable T getTypedRoot(final Class<T> cls) {
		return cls.cast(instance.getInstance());
	}

	public MMXRootObject getRootObject() {
		return (MMXRootObject) instance.getInstance();
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