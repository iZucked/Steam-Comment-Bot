/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link LoadSlotTopLevelComposite} application.
 * 
 * @author Simon Goodall
 * 
 */
public class LoadSlotCompositeFactory extends DefaultDisplayCompositeFactory {
	public LoadSlotCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		return new LoadSlotTopLevelComposite(composite, SWT.NONE, location);
	}
	
	@Override
	public IDisplayComposite createSublevelComposite(Composite parent,
			EClass eClass, IScenarioEditingLocation location) {
		return new SlotDetailComposite(parent, SWT.NONE);
	}
}
