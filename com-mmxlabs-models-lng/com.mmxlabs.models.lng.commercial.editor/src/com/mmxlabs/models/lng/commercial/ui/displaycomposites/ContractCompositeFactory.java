/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link ContractTopLevelComposite} application.
 * 
 * @author hinton
 * 
 */
public class ContractCompositeFactory extends DefaultDisplayCompositeFactory {
	public ContractCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		return new ContractTopLevelComposite(composite, SWT.NONE, location);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		// This is not expected to be called. The CargoTopLevelComposite will create it's own instances directly.
		throw new UnsupportedOperationException("Unexpected method invocations");
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final List<EObject> external = super.getExternalEditingRange(root, value);

		return external;
	}
}
