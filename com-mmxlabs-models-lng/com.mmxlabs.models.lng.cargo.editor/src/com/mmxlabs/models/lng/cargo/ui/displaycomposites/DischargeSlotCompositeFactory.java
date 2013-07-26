/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link DischargeSlotTopLevelComposite} application.
 * 
 * @author Simon Goodall
 * 
 */
public class DischargeSlotCompositeFactory extends DefaultDisplayCompositeFactory {
	public DischargeSlotCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location, final FormToolkit toolkit) {
		return new DischargeSlotTopLevelComposite(composite, SWT.NONE, location, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IScenarioEditingLocation location, final FormToolkit toolkit) {
		return new SlotDetailComposite(parent, SWT.NONE, toolkit);
	}
}
