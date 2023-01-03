/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.manifest.Manifest;

public class ModelRecordScenarioDataProvider implements IScenarioDataProvider {
	private final @NonNull ScenarioModelRecord modelRecord;
	private final @NonNull ModelReference modelReference;
	private final Map<ISharedDataModelType<?>, ModelReference> cachedReferences = new HashMap<>();
	private final Map<ISharedDataModelType<?>, Object> extraDataProviders = new HashMap<>();

	public ModelRecordScenarioDataProvider(final @NonNull ScenarioModelRecord modelRecord) {
		this(modelRecord, "ModelRecordScenarioDataProvider:1", new NullProgressMonitor());
	}

	public ModelRecordScenarioDataProvider(final @NonNull ScenarioModelRecord modelRecord, final @NonNull String modelReferenceID, final @NonNull IProgressMonitor progressMonitor) {
		this.modelRecord = modelRecord;
		this.modelReference = this.modelRecord.aquireReference(modelReferenceID, progressMonitor);
	}

	@Override
	public Manifest getManifest() {
		return modelRecord.getManifest();
	}

	@Override
	protected void finalize() throws Throwable {
		close();

		super.finalize();
	}

	@Override
	public void close() {

		cachedReferences.values().forEach(ModelReference::close);
		cachedReferences.clear();

		if (modelReference != null) {
			modelReference.close();
		}
	}

	@Override
	public @NonNull EObject getScenario() {
		return modelReference.getInstance();
	}

	@Override
	public <T extends EObject> @NonNull T getTypedScenario(Class<T> cls) {
		return cls.cast(modelReference.getInstance());
	}

	// @Override
	// public @NonNull String getExtraDataVersion(@NonNull final ISharedDataModelType<?> key) {
	//
	// for (final ModelArtifact artifact : modelRecord.getManifest().getModelDependencies()) {
	// if (artifact.getKey().equals(key.getID())) {
	// return artifact.getDataVersion();
	// }
	// }
	// throw new IllegalArgumentException();
	// }

	@Override
	public <T> T getExtraData(@NonNull final ISharedDataModelType<T> key) {
		return (T) cachedReferences.computeIfAbsent(key, (k) -> {
			@NonNull
			final ModelRecord extraDataRecord = modelRecord.getExtraDataRecord(key);

			return extraDataRecord.aquireReference("ModelRecordScenarioDataProvider:2");
		}).getInstance();
	}

	@Override
	public <T> T getExtraDataProvider(@NonNull ISharedDataModelType<?> key, Class<T> cls) {
		return cls.cast(extraDataProviders.computeIfAbsent(key, k -> ISharedDataModelType.REGISTRY.getMakerFunction(k).apply(ModelRecordScenarioDataProvider.this)));
	}

	@Override
	public EditingDomain getEditingDomain() {
		return modelReference.getEditingDomain();
	}

	@Override
	public CommandStack getCommandStack() {
		return modelReference.getCommandStack();
	}

	@Override
	public void setLastEvaluationFailed(boolean failed) {
		modelReference.setLastEvaluationFailed(failed);
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		return modelReference;
	}
}
