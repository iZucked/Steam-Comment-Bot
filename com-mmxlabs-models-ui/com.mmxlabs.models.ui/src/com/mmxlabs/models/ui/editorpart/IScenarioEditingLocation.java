/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

	boolean isLocked();

	void setLocked(final boolean locked);

	IExtraValidationContext getExtraValidationContext();

	void pushExtraValidationContext(final IExtraValidationContext context);

	void popExtraValidationContext();

	@Override
	EditingDomain getEditingDomain();

	AdapterFactory getAdapterFactory();

	IReferenceValueProviderProvider getReferenceValueProviderCache();

	ICommandHandler getDefaultCommandHandler();

	MMXRootObject getRootObject();

	void setDisableCommandProviders(final boolean disable);

	void setDisableUpdates(final boolean disable);

	/**
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus, is the current one. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	void setCurrentViewer(final Viewer viewer);

	ScenarioInstance getScenarioInstance();

	Shell getShell();

	ScenarioLock getEditorLock();

	IStatusProvider getStatusProvider();
}