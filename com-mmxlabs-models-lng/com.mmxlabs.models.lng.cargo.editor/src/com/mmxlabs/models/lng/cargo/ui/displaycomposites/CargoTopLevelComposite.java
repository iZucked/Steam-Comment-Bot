/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the cargo editor; because the slots are not contained in the cargo any more
 * we need some special-case editing behaviour.
 * 
 * We also have to signify to containers that we will be editing some non-contained objects, so that they can be
 * duplicated.
 *
 * @author hinton
 *
 */
public class CargoTopLevelComposite extends DefaultTopLevelComposite {
	public CargoTopLevelComposite(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected boolean shouldDisplay(EReference ref) {
		return super.shouldDisplay(ref) || ref == CargoPackage.eINSTANCE.getCargo_LoadSlot() ||ref == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	}
}
