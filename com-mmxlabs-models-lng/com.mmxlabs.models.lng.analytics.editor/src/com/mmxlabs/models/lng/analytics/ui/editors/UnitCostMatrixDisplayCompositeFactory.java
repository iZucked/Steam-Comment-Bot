/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * @author hinton
 *
 */
public class UnitCostMatrixDisplayCompositeFactory implements IDisplayCompositeFactory {

	@Override
	public IDisplayComposite createToplevelComposite(Composite composite, EClass eClass) {
		return new DefaultTopLevelComposite(composite, SWT.NONE);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite composite, EClass eClass) {
		return new DefaultDetailComposite(composite, SWT.NONE) {
			@Override
			public void display(MMXRootObject root, EObject object) {
				if (object instanceof UnitCostMatrix) {
					((UnitCostMatrix) object).getCostLines().clear();
				}
				super.display(root, object);
			}
		};
	}
	
}
