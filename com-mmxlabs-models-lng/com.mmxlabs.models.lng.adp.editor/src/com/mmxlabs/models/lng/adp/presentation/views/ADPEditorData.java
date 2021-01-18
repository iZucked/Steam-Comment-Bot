/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public final class ADPEditorData implements IScenarioEditingLocation {
	private final IScenarioEditingLocation delegate;
	public LNGScenarioModel scenarioModel;
	public ADPModel adpModel;

	public LNGScenarioModel getScenarioModel() {
		return scenarioModel;
	}

	public void setScenarioModel(final LNGScenarioModel scenarioModel) {
		this.scenarioModel = scenarioModel;
	}

	public ADPModel getAdpModel() {
		return adpModel;
	}

	public void setAdpModel(final ADPModel adpModel) {
		this.adpModel = adpModel;
	}

	public final Map<Object, IStatus> validationErrors = new HashMap<>();

	public ADPEditorData(final IScenarioEditingLocation scenarioEditingLocation) {
		this.delegate = scenarioEditingLocation;

	}

	@Override
	public boolean isLocked() {
		return delegate.isLocked();
	}

	@Override
	public IExtraValidationContext getExtraValidationContext() {
		return delegate.getExtraValidationContext();
	}

	@Override
	public void pushExtraValidationContext(final IExtraValidationContext context) {
		delegate.pushExtraValidationContext(context);
	}

	@Override
	public void popExtraValidationContext() {
		delegate.popExtraValidationContext();
	}

	@Override
	public EditingDomain getEditingDomain() {
		return delegate.getEditingDomain();
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		return delegate.getModelReference();
	}

	@Override
	public AdapterFactory getAdapterFactory() {
		return delegate.getAdapterFactory();
	}

	@Override
	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return delegate.getReferenceValueProviderCache();
	}

	@Override
	public ICommandHandler getDefaultCommandHandler() {

		return new ICommandHandler() {

			@Override
			public void handleCommand(Command command, EObject target, EStructuralFeature feature) {
				delegate.getDefaultCommandHandler().handleCommand(command, target, feature);
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
				return ADPEditorData.this.getReferenceValueProviderCache();
			}

			@Override
			public ModelReference getModelReference() {
				return delegate.getDefaultCommandHandler().getModelReference();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return delegate.getDefaultCommandHandler().getEditingDomain();
			}
		};

	}

	@Override
	public MMXRootObject getRootObject() {
		return delegate.getRootObject();
	}

	@Override
	public void setDisableCommandProviders(final boolean disable) {
		delegate.setDisableCommandProviders(disable);
	}

	@Override
	public void setDisableUpdates(final boolean disable) {
		delegate.setDisableUpdates(disable);
	}

	@Override
	public void setCurrentViewer(final Viewer viewer) {
		delegate.setCurrentViewer(viewer);

	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		return delegate.getScenarioInstance();
	}

	@Override
	public Shell getShell() {
		return delegate.getShell();
	}

	@Override
	public ScenarioLock getEditorLock() {
		return delegate.getEditorLock();
	}

	@Override
	public IStatusProvider getStatusProvider() {
		return delegate.getStatusProvider();
	}

	@Override
	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return delegate.getScenarioDataProvider();
	}

}
