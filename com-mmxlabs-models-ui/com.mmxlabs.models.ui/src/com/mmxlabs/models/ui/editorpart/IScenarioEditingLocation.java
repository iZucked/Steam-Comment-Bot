/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public interface IScenarioEditingLocation extends IEditingDomainProvider {

	public abstract boolean isLocked();

	public abstract void setLocked(final boolean locked);

	public abstract IExtraValidationContext getExtraValidationContext();

	public abstract void pushExtraValidationContext(
			final IExtraValidationContext context);

	public abstract void popExtraValidationContext();

	@Override
	public abstract EditingDomain getEditingDomain();

	public abstract AdapterFactory getAdapterFactory();

	public abstract IReferenceValueProviderProvider getReferenceValueProviderCache();

	public abstract ICommandHandler getDefaultCommandHandler();

	public abstract MMXRootObject getRootObject();

	public abstract void setDisableCommandProviders(final boolean disable);

	public abstract void setDisableUpdates(final boolean disable);

	/**
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus, is the current one. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public abstract void setCurrentViewer(final Viewer viewer);

	public abstract ScenarioInstance getScenarioInstance();

	public abstract Shell getShell();

	public abstract ScenarioLock getEditorLock();
	
	public abstract IStatusProvider getStatusProvider();
}