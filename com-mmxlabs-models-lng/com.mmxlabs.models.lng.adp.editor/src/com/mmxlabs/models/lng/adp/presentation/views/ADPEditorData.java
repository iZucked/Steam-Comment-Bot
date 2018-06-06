package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
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

	public void setScenarioModel(LNGScenarioModel scenarioModel) {
		this.scenarioModel = scenarioModel;
	}

	public ADPModel getAdpModel() {
		return adpModel;
	}

	public void setAdpModel(ADPModel adpModel) {
		this.adpModel = adpModel;
	}

	public final Map<Object, IStatus> validationErrors = new HashMap<>();

	public ADPEditorData(IScenarioEditingLocation scenarioEditingLocation) {
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
	public void pushExtraValidationContext(IExtraValidationContext context) {
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
		return delegate.getDefaultCommandHandler();
	}

	@Override
	public MMXRootObject getRootObject() {
		return delegate.getRootObject();
	}

	@Override
	public void setDisableCommandProviders(boolean disable) {
		delegate.setDisableCommandProviders(disable);
	}

	@Override
	public void setDisableUpdates(boolean disable) {
		delegate.setDisableUpdates(disable);
	}

	@Override
	public void setCurrentViewer(Viewer viewer) {
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
