/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;

public final class MigrationModelRecord {

	private final @NonNull EObjectWrapper modelRoot;

	private final @NonNull MetamodelLoader metamodelLoader;

	private final Map<ISharedDataModelType<?>, Object> extraData = new HashMap<>();
	private final List<Triple<String, EObject, ISharedDataModelType<?>>> newData = new LinkedList<>();

	public MigrationModelRecord(final @NonNull EObjectWrapper modelRoot, final @NonNull MetamodelLoader metamodelLoader) {
		this.modelRoot = modelRoot;
		this.metamodelLoader = metamodelLoader;
	}

	public @NonNull EObjectWrapper getModelRoot() {
		return modelRoot;
	}

	public @NonNull MetamodelLoader getMetamodelLoader() {
		return metamodelLoader;
	}

	public <T> EObjectWrapper getSharedEObject(final ISharedDataModelType<T> key) {
		return (EObjectWrapper) extraData.get(key);
	}

	public <T> Object getSharedObject(final ISharedDataModelType<T> key) {
		return extraData.get(key);
	}

	public void setDataModelRoot(final ISharedDataModelType<?> key, final EObject eObject) {
		extraData.put(key, eObject);
	}

	public void addDataModelRoot(final ISharedDataModelType<?> key, final EObject eObject, final String fragmentPath) {
		newData.add(new Triple<>(fragmentPath, eObject, key));
	}

	public List<Triple<String, EObject, ISharedDataModelType<?>>> getNewEObjectData() {
		return newData;
	}
}