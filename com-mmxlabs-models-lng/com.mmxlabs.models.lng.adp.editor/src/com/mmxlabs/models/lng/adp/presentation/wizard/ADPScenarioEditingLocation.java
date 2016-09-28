/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class ADPScenarioEditingLocation implements IScenarioEditingLocation {

	private ModelReference modelReference;
	
	@Override
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IExtraValidationContext getExtraValidationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushExtraValidationContext(IExtraValidationContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popExtraValidationContext() {
		// TODO Auto-generated method stub

	}

	@Override
	public EditingDomain getEditingDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdapterFactory getAdapterFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommandHandler getDefaultCommandHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MMXRootObject getRootObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisableCommandProviders(boolean disable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisableUpdates(boolean disable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCurrentViewer(Viewer viewer) {
		// TODO Auto-generated method stub

	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Shell getShell() {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public IStatusProvider getStatusProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NonNull ModelReference getModelReference() {
		// TODO Auto-generated method stub
		return modelReference;
	}

	@Override
	public ScenarioLock getEditorLock() {
		// TODO Auto-generated method stub
		return modelReference.getLock();
	}

}
