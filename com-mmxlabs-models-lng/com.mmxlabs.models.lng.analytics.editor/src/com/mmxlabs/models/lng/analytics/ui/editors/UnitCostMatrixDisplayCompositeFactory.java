/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editors;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * @author hinton
 * 
 */
public class UnitCostMatrixDisplayCompositeFactory extends DefaultDisplayCompositeFactory {
	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		return new DefaultDetailComposite(composite, SWT.NONE) {
			@Override
			public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
				if (object instanceof UnitCostMatrix) {
					((UnitCostMatrix) object).getCostLines().clear();
				}
				super.display(location, root, object, range,dbc,toolkit);
			}
		};
	}
}
