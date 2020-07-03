/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class WrappedScenarioEditingLocation implements IScenarioEditingLocation {
	private final @NonNull IScenarioEditingLocation original;

	public WrappedScenarioEditingLocation(final @NonNull IScenarioEditingLocation original) {
		this.original = original;
	}

	@Override
	public boolean isLocked() {
		return original.isLocked();
	}

	@Override
	public IExtraValidationContext getExtraValidationContext() {
		return original.getExtraValidationContext();
	}

	@Override
	public void pushExtraValidationContext(final IExtraValidationContext context) {
		original.pushExtraValidationContext(context);
	}

	@Override
	public void popExtraValidationContext() {
		original.popExtraValidationContext();
	}

	@Override
	public EditingDomain getEditingDomain() {
		return original.getEditingDomain();
	}

	@Override
	public AdapterFactory getAdapterFactory() {
		return original.getAdapterFactory();
	}

	@Override
	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return original.getReferenceValueProviderCache();
	}

	@Override
	public ICommandHandler getDefaultCommandHandler() {
		return original.getDefaultCommandHandler();
	}

	@Override
	public MMXRootObject getRootObject() {
		return original.getRootObject();
	}

	@Override
	public void setDisableCommandProviders(final boolean disable) {
		original.setDisableCommandProviders(disable);

	}

	@Override
	public void setDisableUpdates(final boolean disable) {
		original.setDisableUpdates(disable);
	}

	@Override
	public void setCurrentViewer(final Viewer viewer) {
		original.setCurrentViewer(viewer);

	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		return original.getScenarioInstance();
	}

	@Override
	public Shell getShell() {
		return original.getShell();
	}

	@Override
	public ScenarioLock getEditorLock() {
		return original.getEditorLock();
	}

	@Override
	public IStatusProvider getStatusProvider() {
		return original.getStatusProvider();
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		return original.getModelReference();
	}

	@Override
	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return original.getScenarioDataProvider();
	}
}