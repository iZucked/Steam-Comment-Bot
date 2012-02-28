/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * Factory for {@link CargoTopLevelComposite} application.
 * 
 * @author hinton
 *
 */
public class CargoModelCompositeFactory implements IDisplayCompositeFactory {
	public CargoModelCompositeFactory() {
		
	}
	@Override
	public IDisplayComposite createToplevelComposite(Composite composite,
			EClass eClass) {
		return new CargoTopLevelComposite(composite, SWT.NONE);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite composite,
			EClass eClass) {
		// TODO this perhaps should delegate instead; however we do know it is always being called for cargo.
		return new DefaultDetailComposite(composite, SWT.NONE);
	}
}
