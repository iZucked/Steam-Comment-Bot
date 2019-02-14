/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.io.Closeable;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.manifest.Manifest;

@NonNullByDefault
public interface IScenarioDataProvider extends Closeable {

	Manifest getManifest();

	EObject getScenario();

	<T extends EObject> T getTypedScenario(Class<T> cls);

	EditingDomain getEditingDomain();

	CommandStack getCommandStack();

	// String getExtraDataVersion(ISharedDataModelType<?> key);

	<T> T getExtraData(ISharedDataModelType<T> key);

	<T> T getExtraDataProvider(@NonNull ISharedDataModelType<?> key, Class<T> cls);

	void close();

	void setLastEvaluationFailed(boolean failed);

	/**
	 * Returns the model reference. May throw an exception if there is no model reference (i.e. it is not a {@link ModelRecordScenarioDataProvider})
	 * 
	 * @return
	 */
	@NonNull
	ModelReference getModelReference();
}
